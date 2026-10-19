package com.jamiedev.bygone.core.mixin;

import com.jamiedev.bygone.common.weather.weather_types.HauntingsCategoryHolder;

import net.minecraft.world.entity.MobCategory;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(MobCategory.class)
public enum MobCategoryMixinFabric {
    BYGONE_HAUNTINGS_MOB(
            HauntingsCategoryHolder.CATEGORY_NAME,
            HauntingsCategoryHolder.CATEGORY_MOB_CAP,
            HauntingsCategoryHolder.CATEGORY_FRIENDLY,
            HauntingsCategoryHolder.CATEGORY_PERSISTENT,
            HauntingsCategoryHolder.CATEGORY_DESPAWN_RANGE
    );
    @Shadow MobCategoryMixinFabric(String name, int max, boolean friendly, boolean persistant, int despawnDistance){}
}
