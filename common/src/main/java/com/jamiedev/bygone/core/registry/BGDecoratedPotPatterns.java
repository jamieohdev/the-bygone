package com.jamiedev.bygone.core.registry;

import com.jamiedev.bygone.Bygone;

import com.kekecreations.jinxedlib.core.util.JinxedRegistryHelper;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.DecoratedPotPattern;

import java.util.function.Supplier;

public class BGDecoratedPotPatterns{
    public static final ResourceKey<DecoratedPotPattern> BEAK_KEY = create("beak");
    public static final ResourceKey<DecoratedPotPattern> MURKY_KEY = create("murky");

    public static final Supplier<DecoratedPotPattern> BEAK = register(BEAK_KEY, Bygone.id("beak_pottery_pattern"));
    public static final Supplier<DecoratedPotPattern> MURKY = register(MURKY_KEY, Bygone.id("murky_pottery_pattern"));



    private static ResourceKey<DecoratedPotPattern> create(String id){
        return ResourceKey.create(Registries.DECORATED_POT_PATTERN, Bygone.id(id));
    }
    private static <T> Supplier<DecoratedPotPattern> register(ResourceKey<DecoratedPotPattern> id, ResourceLocation asset) {
        return JinxedRegistryHelper.register(
                BuiltInRegistries.DECORATED_POT_PATTERN,
                id.location().getNamespace(), id.location().getPath(),
                () -> new DecoratedPotPattern(asset)
        );
    }
    public static void init(){}
}
