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
    public void tick() {
        assert level != null;
        WeatherProperties.WeatherProperty<Integer> time = this.getProperty(TIME);
        WeatherProperties.WeatherProperty<Boolean> isRaining = this.getProperty(IS_INTENSE);
        WeatherProperties.WeatherProperty<Float> rain = this.getProperty(FOG_AMOUNT);

        if (time.getValue() > 0) {
            time.setValue(time.getValue() - 1);
            if (time.getValue() == 0) isRaining.setValue(!isRaining.getValue());
        } else {
            if (isRaining.getValue()) time.setValue(ServerLevel.RAIN_DURATION.sample(level.random));
            else time.setValue(ServerLevel.RAIN_DELAY.sample(level.random));
        }

        if (isRaining.getValue()) {
            if ((float) this.getProperty(FOG_AMOUNT).getValue() <= 0f)
                this.getProperty(FOG_AMOUNT).setValue((float) Math.clamp(level.random.nextFloat() * 1.5f, 0.5, 1.5f));
        } else if ((float) this.getProperty(FOG_AMOUNT).getValue() > 0f)
            this.getProperty(FOG_AMOUNT).setValue(0f);

        super.tick();
    }

    @Override
    public float fogModifier() { return (float) this.getProperty(FOG_AMOUNT).getValue(); }

    @Override
    public void startWeather() {
        assert level != null;
        WeatherProperties.WeatherProperty<Boolean> isRaining = this.getProperty(IS_INTENSE);
        WeatherProperties.WeatherProperty<Integer> time = this.getProperty(TIME);
        if (isRaining.getValue()) return;

        isRaining.setValue(true);
        time.setValue(ServerLevel.RAIN_DURATION.sample(level.random));
    }

    @Override
    public void clearWeather() {
        assert level != null;
        WeatherProperties.WeatherProperty<Boolean> isRaining = this.getProperty(IS_INTENSE);
        WeatherProperties.WeatherProperty<Integer> time = this.getProperty(TIME);
        if (!isRaining.getValue()) return;

        isRaining.setValue(false);
        time.setValue(ServerLevel.RAIN_DELAY.sample(level.random));
    }

    @Override public boolean isActive() {
        return (boolean) this.getProperty(IS_INTENSE).getValue();
    }

}
