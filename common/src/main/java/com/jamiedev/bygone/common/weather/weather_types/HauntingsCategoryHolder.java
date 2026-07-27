package com.jamiedev.bygone.common.weather.weather_types;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.common.weather.BygoneWeather;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.Level;

public class HauntingsCategoryHolder {
    public static final String CATEGORY_NAME = "bygone:hauntings_mobs";
    public static final int CATEGORY_MOB_CAP = 70;
    public static final boolean CATEGORY_FRIENDLY = false;
    public static final boolean CATEGORY_PERSISTENT = false;
    public static final int CATEGORY_DESPAWN_RANGE = 64;

    public static final MobCategory HAUNTING_MOB_CATEGORY = MobCategory.valueOf("BYGONE_HAUNTINGS_MOB");
    private static final ResourceLocation HAUNTINGS_LOCATION = Bygone.id("hauntings");

    public static boolean checkHauntingsActive(Level level) {
        if (!(level instanceof ServerLevel serverLevel)) return false;

        BygoneWeather weather = BygoneWeather.getOrDefault(serverLevel);
        if (weather == null) return false;

        return weather.getWeatherType(HAUNTINGS_LOCATION)
            .map(WeatherType::isActive).orElse(false);
    }
}
