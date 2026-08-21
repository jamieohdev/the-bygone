package com.jamiedev.bygone.common.weather.weather_types;

import com.jamiedev.bygone.core.network.HauntingsTollS2C;
import com.jamiedev.bygone.core.network.PacketHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.Nullable;

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

    @Override public void tick() {
        assert level != null;

        WeatherProperties.WeatherProperty<Integer> time = this.getProperty(TIME);
        time.setValue((time.getValue() + 1) % (HAUNTING_CYCLE + HAUNTING_DURATION));

        boolean previousEnabled = (boolean) this.getProperty(ENABLED).getValue();
        this.getProperty(ENABLED).setValue(time.getValue() > HAUNTING_CYCLE);
        boolean currentlyEnabled = (boolean) this.getProperty(ENABLED).getValue();
        if (!previousEnabled && currentlyEnabled) {
            PacketHandler.sendPacketToAllInLevel(
                this.level, new HauntingsTollS2C()
            );
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
