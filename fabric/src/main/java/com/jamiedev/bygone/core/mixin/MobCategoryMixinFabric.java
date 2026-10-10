package com.jamiedev.bygone.core.mixin;

import com.jamiedev.bygone.common.weather.weather_types.HauntingsCategoryHolder;

import java.util.Arrays;

import net.minecraft.world.entity.MobCategory;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;

// random priority for consistent injection order
@Mixin(value = MobCategory.class, priority = 1293018462)
public class MobCategoryMixinFabric {
    @Shadow @Final @Mutable private static MobCategory[] $VALUES;
    @Invoker("<init>")
    private static MobCategory bygone$init(String value, int id, String name, int max, boolean friendly, boolean persistant, int despawnDistance){
        throw new AssertionError();
    }

    static{
        $VALUES = Arrays.copyOf($VALUES, $VALUES.length + 1);
        // whuh-oh! mutual recursion! (HauntingsCategoryHolder calls MobCategory.valueOf in <clinit>)
        // ...because all of the fields we access here are static constants of primitive or string type,
        // by the Java Language Specification (section 4.12.4), this works out.
        // ... so be careful with that when changing things in the future??
        $VALUES[$VALUES.length - 1] = bygone$init("BYGONE_HAUNTINGS_MOB", $VALUES.length - 1,
                HauntingsCategoryHolder.CATEGORY_NAME, HauntingsCategoryHolder.CATEGORY_MOB_CAP,
                HauntingsCategoryHolder.CATEGORY_FRIENDLY, HauntingsCategoryHolder.CATEGORY_PERSISTENT,
                HauntingsCategoryHolder.CATEGORY_DESPAWN_RANGE);
    }
}
