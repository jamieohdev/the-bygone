package com.jamiedev.bygone.common.worldgen.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.RuleBasedBlockStateProvider;

public record DiskShelfFungiConfig(RuleBasedBlockStateProvider outsideStateProvider,
        RuleBasedBlockStateProvider insideStateProvider,
        TagKey<Block> replaces,
        boolean placeInAir,
        IntProvider radius,
        int halfHeight) implements FeatureConfiguration {
    public static final Codec<DiskShelfFungiConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            RuleBasedBlockStateProvider.CODEC.fieldOf("outside_state_provider")
                    .forGetter(DiskShelfFungiConfig::outsideStateProvider),
            RuleBasedBlockStateProvider.CODEC.fieldOf("inside_state_provider")
                    .forGetter(DiskShelfFungiConfig::insideStateProvider),
            TagKey.codec(Registries.BLOCK).fieldOf("replaces").forGetter(DiskShelfFungiConfig::replaces),
            Codec.BOOL.fieldOf("place_in_air").forGetter(DiskShelfFungiConfig::placeInAir),
            IntProvider.NON_NEGATIVE_CODEC.fieldOf("radius").forGetter(DiskShelfFungiConfig::radius),
            Codec.INT.fieldOf("half_height").forGetter(DiskShelfFungiConfig::halfHeight)
    ).apply(instance, DiskShelfFungiConfig::new));
}
