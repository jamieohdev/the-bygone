package com.jamiedev.bygone.core.registry;

import com.jamiedev.bygone.Bygone;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.DecoratedPotPattern;

import javax.annotation.Nullable;
import java.util.Map;

public class BGDecoratedPotPatterns
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
