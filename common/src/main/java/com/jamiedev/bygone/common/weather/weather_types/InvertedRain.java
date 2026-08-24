package com.jamiedev.bygone.common.weather.weather_types;

import com.jamiedev.bygone.common.weather.weather_types.WeatherProperties.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;

import javax.annotation.Nullable;

public class InvertedRain extends WeatherType {
    private static final String TIME = "time";
    private static final String IS_RAINING = "isRaining";
    private static final String RAIN_AMOUNT = "rainAmount";
    private static final String FOG_AMOUNT = "fogAmount";

    public InvertedRain(ResourceLocation id, ServerLevel level) {
        super(id, level);

        this.registerProperty(WeatherProperties::ofInt, TIME, 0).setSync(false);
        this.registerProperty(WeatherProperties::ofBool, IS_RAINING, false);
        this.registerProperty(WeatherProperties::ofFloat, RAIN_AMOUNT, 0.0f);
        this.registerProperty(WeatherProperties::ofFloat, FOG_AMOUNT, 0.0f);
    }

    @Override public boolean isActive() {
        return (boolean) this.getProperty(IS_RAINING).getValue();
    }

    @Override
    public void startWeather() {
        assert level != null;
        WeatherProperty<Boolean> isRaining = this.getProperty(IS_RAINING);
        WeatherProperty<Integer> time = this.getProperty(TIME);
        if (isRaining.getValue()) return;

        isRaining.setValue(true);
        time.setValue(ServerLevel.RAIN_DURATION.sample(level.random));
    }

    @Override
    public void clearWeather() {
        assert level != null;
        WeatherProperty<Boolean> isRaining = this.getProperty(IS_RAINING);
        WeatherProperty<Integer> time = this.getProperty(TIME);
        if (!isRaining.getValue()) return;

        isRaining.setValue(false);
        time.setValue(ServerLevel.RAIN_DELAY.sample(level.random));
    }

    @Override
    public float fogModifier() { return (float) this.getProperty(FOG_AMOUNT).getValue(); }

    public float getRainAmount() {
        return (float) this.getProperty(RAIN_AMOUNT).getValue();
    }

    @Override
    public void tick() {
        assert level != null;
        WeatherProperty<Integer> time = this.getProperty(TIME);
        WeatherProperty<Boolean> isRaining = this.getProperty(IS_RAINING);
        WeatherProperty<Float> rain = this.getProperty(RAIN_AMOUNT);

        if (time.getValue() > 0) {
            time.setValue(time.getValue() - 1);
            if (time.getValue() == 0) isRaining.setValue(!isRaining.getValue());
        } else {
            if (isRaining.getValue()) time.setValue(ServerLevel.RAIN_DURATION.sample(level.random));
            else time.setValue(ServerLevel.RAIN_DELAY.sample(level.random));
        }

        if (isRaining.getValue()) {
            if ((float) this.getProperty(FOG_AMOUNT).getValue() <= 0f)
                this.getProperty(FOG_AMOUNT).setValue((float) Math.clamp(level.random.nextFloat() * 0.5f, 0.1, 0.5f));
        } else if ((float) this.getProperty(FOG_AMOUNT).getValue() > 0f)
            this.getProperty(FOG_AMOUNT).setValue(0f);

        float rainValue = rain.getValue() + (isRaining.getValue() ? 1 : -1) * 0.01f;
        rain.setValue(Math.clamp(rainValue, 0f, 1f));

        super.tick();
    }
}
