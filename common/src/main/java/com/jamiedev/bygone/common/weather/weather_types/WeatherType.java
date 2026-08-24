package com.jamiedev.bygone.common.weather.weather_types;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.client.renderer.weather.WeatherRenderer;
import com.jamiedev.bygone.common.weather.BygoneWeather;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings("rawtypes")
public abstract class WeatherType {

    private final ResourceLocation id;
    public ResourceLocation getId() {return this.id;}
    final @Nullable ServerLevel level;
    public WeatherType(ResourceLocation id, @Nullable ServerLevel level) {
        this.id = id;
        this.level = level;
    }

    private final Map<String, WeatherProperties.WeatherProperty<?>> propertySet = new HashMap<>();
    public Set<WeatherProperties.WeatherProperty> getProperties() {return new HashSet<>(propertySet.values());}

    protected <T> WeatherProperties.WeatherProperty<?> registerProperty(TriFunction<String, T, ResourceLocation, WeatherProperties.WeatherProperty<T>> supplierBiFunction, String identifier, T value) {
        WeatherProperties.WeatherProperty<T> property = supplierBiFunction.apply(identifier, value, this.getId());
        propertySet.put(identifier, property);
        return propertySet.get(identifier);
    }

    @SuppressWarnings("unchecked")
    public <T> WeatherProperties.WeatherProperty<T> getProperty(String identifier) {
        return (WeatherProperties.WeatherProperty<T>) propertySet.get(identifier);
    }

    public abstract void startWeather();
    public abstract void clearWeather();
    public abstract boolean isActive();
    public float fogModifier() { return 0.0f; }

    public void tick() {}

    public Set<WeatherProperties.WeatherProperty> queryStates(Set<WeatherProperties.WeatherProperty> weatherPropertySet) {
        propertySet.values().forEach(property -> {
            if (property.checkDirty() && property.shouldSync()) weatherPropertySet.add(property);
        });
        return weatherPropertySet;
    }

    public void load(CompoundTag tag) {
        propertySet.values().forEach(property -> property.parseSelf(tag));
    }

    public CompoundTag save() {
        CompoundTag compoundTag = new CompoundTag();
        propertySet.values().forEach(property -> property.appendToTag(compoundTag));
        return compoundTag;
    }

    @SuppressWarnings("rawtypes")
    public static class Factory<T extends WeatherType> {
        private final ResourceLocation id;
        public ResourceLocation getLocation() {
            return this.id;
        }
        private final BiFunction<ResourceLocation, ServerLevel, T> supplier;
        private final Supplier<Function<T, WeatherRenderer<T>>> renderer;
        public Factory(
            String id, BiFunction<ResourceLocation, ServerLevel, T> supplier,
            Supplier<Function<T, WeatherRenderer<T>>> renderer
        ) {
            this.id = Bygone.id(id);
            this.supplier = supplier;
            this.renderer = renderer;
        }

        private T instance;
        public T get(ServerLevel level) {
            if (instance == null)
                instance = construct(level);
            return instance;
        }

        private T construct(ServerLevel level) {
            Bygone.LOGGER.info("constructing weather of type {}", id);
            return supplier.apply(id, level);
        }

        public WeatherRenderer<T> getRenderer() {
            return renderer.get().apply(instance);
        }

        public ResourceKey<Factory> getKey() {
            return ResourceKey.create(
                BygoneWeather.WEATHER_TYPE_REGISTRY_KEY, id
            );
        }
    }
}
