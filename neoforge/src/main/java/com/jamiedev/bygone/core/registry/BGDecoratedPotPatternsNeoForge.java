package com.jamiedev.bygone.core.registry;

import com.google.common.collect.ImmutableMap;
import com.jamiedev.bygone.Bygone;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.DecoratedPotPattern;
import net.minecraft.world.level.block.entity.DecoratedPotPatterns;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BGDecoratedPotPatternsNeoForge
{
    public static final DeferredRegister<DecoratedPotPattern> POT_PATTERNS = DeferredRegister.create(Registries.DECORATED_POT_PATTERN, Bygone.MOD_ID);

    public static final Holder<DecoratedPotPattern> BEAK = POT_PATTERNS.register("beak_pottery_pattern",
            () -> new DecoratedPotPattern(ResourceLocation.fromNamespaceAndPath(Bygone.MOD_ID,
                    "beak_pottery_pattern")));

    public static final Holder<DecoratedPotPattern> MURKY = POT_PATTERNS.register("murky_pottery_pattern",
            () -> new DecoratedPotPattern(ResourceLocation.fromNamespaceAndPath(Bygone.MOD_ID,
                    "murky_pottery_pattern")));

    public static void expandVanilla() {
        ImmutableMap.Builder<Item, ResourceKey<DecoratedPotPattern>> itemsToPot = new ImmutableMap.Builder<>();
        itemsToPot.putAll(DecoratedPotPatterns.ITEM_TO_POT_TEXTURE);
        itemsToPot.put(BGItems.BEAK_POTTERY_SHERD.get(), create(BEAK.getRegisteredName()));
        itemsToPot.put(BGItems.MURKY_POTTERY_SHERD.get(), create(MURKY.getRegisteredName()));
        DecoratedPotPatterns.ITEM_TO_POT_TEXTURE = itemsToPot.build();
    }

    private static ResourceKey<DecoratedPotPattern> create(String p_272919_) {
        return ResourceKey.create(Registries.DECORATED_POT_PATTERN, ResourceLocation.parse(p_272919_));
    }

}
