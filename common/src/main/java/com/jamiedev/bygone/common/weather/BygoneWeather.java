package com.jamiedev.bygone.common.weather;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.client.renderer.weather.HauntingsRenderer;
import com.jamiedev.bygone.client.renderer.weather.InvertedRainRenderer;
import com.jamiedev.bygone.client.renderer.weather.WeatherRenderer;
import com.jamiedev.bygone.common.weather.weather_types.HauntingsEvent;
import com.jamiedev.bygone.common.weather.weather_types.InvertedRain;
import com.jamiedev.bygone.common.weather.weather_types.WeatherProperties;
import com.jamiedev.bygone.common.weather.weather_types.WeatherType;
import com.jamiedev.bygone.core.network.PacketHandler;
import com.jamiedev.bygone.core.network.SyncWeatherS2C;
import com.jamiedev.bygone.core.registry.BGDimensions;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("rawtypes")
public class BygoneWeather extends SavedData {
    public static final ResourceKey<Registry<WeatherType.Factory>> WEATHER_TYPE_REGISTRY_KEY
        = ResourceKey.createRegistryKey(Bygone.id("weather_types"));
    public static Registry<WeatherType.Factory> WEATHER_TYPES;

    public static void bootstrap(Consumer<WeatherType.Factory> consumer) {
        consumer.accept(new WeatherType.Factory<InvertedRain>("inverted_rain", InvertedRain::new, () -> InvertedRainRenderer::new));
        consumer.accept(new WeatherType.Factory<HauntingsEvent>("hauntings", HauntingsEvent::new, () -> HauntingsRenderer::new));
    }

    @Nullable private final ServerLevel level;
    public static final String NAME = "bygone_weather";

    private final Collection<WeatherType> instancedWeatherTypes;
    public BygoneWeather(@Nullable ServerLevel level) {
        this.level = level;
        instancedWeatherTypes = WEATHER_TYPES.stream()
            .map((inst) -> inst.get(level))
            .collect(Collectors.toSet());
    }

    public Optional<WeatherType> getWeatherType(ResourceLocation weatherLocation) {
        return instancedWeatherTypes.stream().filter((weatherType)
            -> weatherType.getId().equals(weatherLocation)).findFirst();
    }

    public static BygoneWeather getOrDefault(ServerLevel level) {
        if (!level.dimension().equals(BGDimensions.BYGONE_LEVEL_KEY)) return null;
        return level.getDataStorage().computeIfAbsent(
            new SavedData.Factory<>(
                () -> new BygoneWeather(level),
                (tag, provider)
                    -> BygoneWeather.create(tag, provider, level),
                DataFixTypes.LEVEL
            ), BygoneWeather.NAME
        );
    }

    public static BygoneWeather create(CompoundTag tag, HolderLookup.Provider provider, ServerLevel serverLevel) {
        BygoneWeather weather = new BygoneWeather(serverLevel);
        return weather.load(tag, provider);
    }

    public BygoneWeather load(CompoundTag tag, HolderLookup.Provider provider) {
        instancedWeatherTypes.forEach((weather) -> {
            if (tag.get(weather.getId().getPath()) instanceof CompoundTag compoundTag) weather.load(compoundTag);
        });
        return this;
    }

    @Override public @NotNull CompoundTag save(@NotNull CompoundTag compoundTag, @Nullable HolderLookup.Provider provider) {
        instancedWeatherTypes.forEach((weather)
            -> compoundTag.put(weather.getId().getPath(), weather.save()));
        return compoundTag;
    }

    public void informPlayerOfState(ServerPlayer player) {
        CompoundTag stateTag = save(new CompoundTag(), null);
        PacketHandler.sendTo(new SyncWeatherS2C(stateTag), player);
    }

    public void tick() {
        if (level == null) return;

        Set<WeatherProperties.WeatherProperty> weatherPropertySet = new HashSet<>();
        for (WeatherType weather : instancedWeatherTypes) {
            weather.tick();
            weatherPropertySet = weather.queryStates(weatherPropertySet);
        }

        CompoundTag stateTag = new CompoundTag();
        for (WeatherProperties.WeatherProperty property : weatherPropertySet) {
            if (!stateTag.contains(property.getOwner().getPath(), 10))
                stateTag.put(property.getOwner().getPath(), new CompoundTag());
            CompoundTag weatherTag = stateTag.getCompound(property.getOwner().getPath());
            property.appendToTag(weatherTag);
        }

        if (!stateTag.isEmpty()) {
            PacketHandler.sendPacketToAllInLevel(
                level, new SyncWeatherS2C(stateTag));
            this.setDirty();
        }
    }

    public static class Client {
        private static Client INSTANCE;
        public static Client getInstance() {
            if (INSTANCE == null) INSTANCE = new Client();
            return INSTANCE;
        }

        private final BygoneWeather weatherContext;
        public void updateContext(CompoundTag compoundTag) {
            weatherContext.load(compoundTag, null);
        }
        private final Collection<WeatherRenderer> instancedWeatherRenderers;

        public Stream<WeatherRenderer> stream() { return instancedWeatherRenderers.stream(); }

        private Client() {
            weatherContext = new BygoneWeather(null);
            instancedWeatherRenderers = WEATHER_TYPES.stream()
                .map(WeatherType.Factory::getRenderer)
                .collect(Collectors.toSet());
        }
    }
}