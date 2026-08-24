package com.jamiedev.bygone.common.weather.weather_types;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.Nullable;

// fog (the weather event
// not to be confused with fog
public class FogWeatherEvent extends WeatherType {
    private static final String FOG_AMOUNT = "fogAmount";
    private static final String TIME = "time";
    private static final String IS_INTENSE = "intenseFog";
    public FogWeatherEvent(ResourceLocation id, @Nullable ServerLevel level) {
        super(id, level);

        this.registerProperty(WeatherProperties::ofInt, TIME, 0).setSync(false);
        this.registerProperty(WeatherProperties::ofBool, IS_INTENSE, false);
        this.registerProperty(WeatherProperties::ofFloat, FOG_AMOUNT, 0f);
    }

    @Override
    public float fogModifier() { return (float) this.getProperty(FOG_AMOUNT).getValue(); }

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
