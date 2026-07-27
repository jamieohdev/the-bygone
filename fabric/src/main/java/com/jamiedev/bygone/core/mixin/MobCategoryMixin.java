package com.jamiedev.bygone.core.mixin;

import com.jamiedev.bygone.common.weather.weather_types.HauntingsCategoryHolder;
import net.minecraft.world.entity.MobCategory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(MobCategory.class)
public enum MobCategoryMixin {
    BYGONE_HAUNTINGS_MOB(
        HauntingsCategoryHolder.CATEGORY_NAME, HauntingsCategoryHolder.CATEGORY_MOB_CAP,
        HauntingsCategoryHolder.CATEGORY_FRIENDLY, HauntingsCategoryHolder.CATEGORY_PERSISTENT,
        HauntingsCategoryHolder.CATEGORY_DESPAWN_RANGE
    );

    @Shadow MobCategoryMixin(
        String name, int max,
        boolean isFriendly,
        boolean isPersistent,
        int despawnDistance
    ) {}
}
