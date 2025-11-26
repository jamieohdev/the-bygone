package com.jamiedev.bygone.common.worldgen.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.levelgen.GeodeCrackSettings;
import net.minecraft.world.level.levelgen.GeodeLayerSettings;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public record FeatureFilledGeodeConfig(FeatureFilledGeodeBlockAndFeatureSettings geodeBlockSettings,
        GeodeLayerSettings geodeLayerSettings,
        GeodeCrackSettings geodeCrackSettings,
        double usePotentialPlacementsChance,
        double useAlternateLayer0Chance,
        boolean placementsRequireLayer0Alternate,
        IntProvider outerWallDistance,
        IntProvider distributionPoints,
        IntProvider pointOffset,
        int minGenOffset,
        int maxGenOffset,
        double noiseMultiplier,
        int invalidBlocksThreshold) implements FeatureConfiguration {
    public static final Codec<Double> CHANCE_RANGE = Codec.doubleRange(0.0F, 1.0F);
    public static final Codec<FeatureFilledGeodeConfig> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            FeatureFilledGeodeBlockAndFeatureSettings.CODEC.fieldOf("blocks")
                    .forGetter(FeatureFilledGeodeConfig::geodeBlockSettings),
            GeodeLayerSettings.CODEC.fieldOf("layers").forGetter(FeatureFilledGeodeConfig::geodeLayerSettings),
            GeodeCrackSettings.CODEC.fieldOf("crack").forGetter(FeatureFilledGeodeConfig::geodeCrackSettings),
            CHANCE_RANGE.fieldOf("use_potential_placements_chance")
                    .orElse(0.35)
                    .forGetter(FeatureFilledGeodeConfig::usePotentialPlacementsChance),
            CHANCE_RANGE.fieldOf("use_alternate_layer0_chance")
                    .orElse((double) 0.0F)
                    .forGetter(FeatureFilledGeodeConfig::useAlternateLayer0Chance),
            Codec.BOOL.fieldOf("placements_require_layer0_alternate")
                    .orElse(true)
                    .forGetter(FeatureFilledGeodeConfig::placementsRequireLayer0Alternate),
            IntProvider.codec(1, 20)
                    .fieldOf("outer_wall_distance")
                    .orElse(UniformInt.of(4, 5))
                    .forGetter(FeatureFilledGeodeConfig::outerWallDistance),
            IntProvider.codec(1, 20)
                    .fieldOf("distribution_points")
                    .orElse(UniformInt.of(3, 4))
                    .forGetter(FeatureFilledGeodeConfig::distributionPoints),
            IntProvider.codec(0, 10)
                    .fieldOf("point_offset")
                    .orElse(UniformInt.of(1, 2))
                    .forGetter(FeatureFilledGeodeConfig::pointOffset),
            Codec.INT.fieldOf("min_gen_offset").orElse(-16).forGetter(FeatureFilledGeodeConfig::minGenOffset),
            Codec.INT.fieldOf("max_gen_offset").orElse(16).forGetter(FeatureFilledGeodeConfig::maxGenOffset),
            CHANCE_RANGE.fieldOf("noise_multiplier").orElse(0.05).forGetter(FeatureFilledGeodeConfig::noiseMultiplier),
            Codec.INT.fieldOf("invalid_blocks_threshold").forGetter(FeatureFilledGeodeConfig::invalidBlocksThreshold)
    ).apply(instance, FeatureFilledGeodeConfig::new));
}
