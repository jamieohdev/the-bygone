package com.jamiedev.bygone.core.registry;

import com.jamiedev.bygone.Bygone;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.entity.DecoratedPotPattern;

public class BGDecoratedPotPatternsFabric
{
    public static final ResourceKey<DecoratedPotPattern> BEAK = of("beak");
    public static final ResourceKey<DecoratedPotPattern> MURKY = of("murky");

    private static ResourceKey<DecoratedPotPattern> of(String id) {
        return ResourceKey.create(Registries.DECORATED_POT_PATTERN, Bygone.id(id));
    }

    public static DecoratedPotPattern register(Registry<DecoratedPotPattern> registry, ResourceKey<DecoratedPotPattern> resourceKey, String id) {
        return Registry.register(registry, resourceKey, new DecoratedPotPattern(Bygone.id(id)));
    }
}
