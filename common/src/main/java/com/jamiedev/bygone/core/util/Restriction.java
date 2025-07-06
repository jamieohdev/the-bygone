package com.jamiedev.bygone.core.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.Structure;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public record Restriction(@Nullable ResourceKey<Structure> hintStructureKey, ResourceKey<Enforcement> enforcement,
                          float multiplier, @Nullable ItemStack lockedBiomeToast, List<ResourceLocation> advancements) {


    @SuppressWarnings("OptionalUsedAsFieldOrParameterType") // Vanilla does this too
    private static Restriction create(Optional<ResourceKey<Structure>> hintStructureKey, ResourceKey<Enforcement> enforcer, float multiplier, Optional<ItemStack> lockedBiomeToast, List<ResourceLocation> advancements) {
        return new Restriction(hintStructureKey.orElse(null), enforcer, multiplier, lockedBiomeToast.orElse(null), advancements);
    }

    public static Optional<Restriction> getRestrictionForBiome(Biome biome, Entity entity) {
        if (entity instanceof Player player) {
            RegistryAccess access = entity.level().registryAccess();

        }
        return Optional.empty();
    }

    public static boolean isBiomeSafeFor(Biome biome, Entity entity) {
        return getRestrictionForBiome(biome, entity).isEmpty();
    }
}