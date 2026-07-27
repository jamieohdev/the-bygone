package com.jamiedev.bygone.client.renderer.weather;

import com.jamiedev.bygone.common.weather.weather_types.WeatherType;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.world.level.Level;

public abstract class WeatherRenderer<T extends WeatherType> {
    protected final T instance;
    public T getWeatherInstance() { return instance; }
    public WeatherRenderer(T instance) {
        this.instance = instance;
    }

    public abstract void tick(Level level);
    public abstract void render(Level level, LightTexture lightTexture, float partialTick, double camX, double camY, double camZ);
}
