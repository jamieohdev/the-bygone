package com.jamiedev.bygone.common.weather.weather_types;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.Nullable;

// fog (the weather event
// not to be confused with fog
public class FogWeatherEvent extends WeatherType {
    public FogWeatherEvent(ResourceLocation id, @Nullable ServerLevel level) {
        super(id, level);
    }

    @Override
    public void startWeather() {

    }

    @Override
    public void clearWeather() {

    }

    @Override
    public boolean isActive() {
        return false;
    }
}
