package com.jamiedev.bygone.common.weather.weather_types;

import com.jamiedev.bygone.core.network.HauntingsTollS2C;
import com.jamiedev.bygone.core.network.PacketHandler;
import com.jamiedev.bygone.core.registry.BGCriteria;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class HauntingsEvent extends WeatherType {
    private static final String TIME = "time";
    private static final String ENABLED = "enabled"; // bandage but works for now
    public HauntingsEvent(ResourceLocation id, @Nullable ServerLevel level) {
        super(id, level);

        this.registerProperty(WeatherProperties::ofInt, TIME, 0).setSync(false);
        this.registerProperty(WeatherProperties::ofBool, ENABLED, false);
    }

    private static final int HAUNTING_DURATION = 12000; // like 10 or so minutes
    private static final int HAUNTING_CYCLE = (HAUNTING_DURATION * 5); // every 50 minutes, for now

    public static final int DESPAWN_TICKS = 100;

    @Override
    public float fogModifier() { return (boolean) this.getProperty(ENABLED).getValue() ? 2f : 0f; }

    private final List<ServerPlayer> playerList = new ArrayList<>();

    @Override public void tick() {
        assert level != null;

        WeatherProperties.WeatherProperty<Integer> time = this.getProperty(TIME);
        time.setValue((time.getValue() + 1) % (HAUNTING_CYCLE + HAUNTING_DURATION));

        boolean previousEnabled = (boolean) this.getProperty(ENABLED).getValue();
        this.getProperty(ENABLED).setValue(time.getValue() > HAUNTING_CYCLE);
        boolean currentlyEnabled = (boolean) this.getProperty(ENABLED).getValue();
        // hauntings starting
        if (!previousEnabled && currentlyEnabled) {
            playerList.clear();
            playerList.addAll(level.players());
            //
            PacketHandler.sendPacketToAllInLevel(
                this.level, new HauntingsTollS2C()
            );
        } else if (!currentlyEnabled && previousEnabled) {
            // hauntings ended
            for (ServerPlayer player : playerList)
                BGCriteria.SURVIVE_HAUNTINGS_CRITERION.trigger(player);
        }
        super.tick();
    }

    @Override public boolean isActive() {
        return ((boolean) this.getProperty(ENABLED).getValue());
    }
    @Override public void startWeather() {
        this.getProperty(TIME).setValue(HAUNTING_CYCLE);
    }
    @Override public void clearWeather() {
        this.getProperty(TIME).setValue(0);
    }
}
