package com.jamiedev.bygone.common.worldgen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class PointedAmberClusterFeatureConfig implements FeatureConfiguration {
    public static final Codec<PointedAmberClusterFeatureConfig> CODEC = RecordCodecBuilder.create((instance) -> {
        return instance.group(Codec.intRange(1, 512).fieldOf("floor_to_ceiling_search_range").forGetter((config) -> {
            return config.floorToCeilingSearchRange;
        }), IntProvider.codec(1, 128).fieldOf("height").forGetter((config) -> {
            return config.height;
        }), IntProvider.codec(1, 128).fieldOf("radius").forGetter((config) -> {
            return config.radius;
        }), Codec.intRange(0, 64).fieldOf("max_stalagmite_stalactite_height_diff").forGetter((config) -> {
            return config.maxStalagmiteStalactiteHeightDiff;
        }), Codec.intRange(1, 64).fieldOf("height_deviation").forGetter((config) -> {
            return config.heightDeviation;
        }), IntProvider.codec(0, 128).fieldOf("amber_block_layer_thickness").forGetter((config) -> {
            return config.amberBlockLayerThickness;
        }), FloatProvider.codec(0.0F, 2.0F).fieldOf("density").forGetter((config) -> {
            return config.density;
        }), FloatProvider.codec(0.0F, 2.0F).fieldOf("wetness").forGetter((config) -> {
            return config.wetness;
        }), Codec.floatRange(0.0F, 1.0F).fieldOf("chance_of_amber_column_at_max_distance_from_center").forGetter((config) -> {
            return config.chanceOfAmberColumnAtMaxDistanceFromCenter;
        }), Codec.intRange(1, 64).fieldOf("max_distance_from_edge_affecting_chance_of_amber_column").forGetter((config) -> {
            return config.maxDistanceFromCenterAffectingChanceOfAmberColumn;
        }), Codec.intRange(1, 64).fieldOf("max_distance_from_center_affecting_height_bias").forGetter((config) -> {
            return config.maxDistanceFromCenterAffectingHeightBias;
        })).apply(instance, PointedAmberClusterFeatureConfig::new);
    });
    public final int floorToCeilingSearchRange;
    public final IntProvider height;
    public final IntProvider radius;
    public final int maxStalagmiteStalactiteHeightDiff;
    public final int heightDeviation;
    public final IntProvider amberBlockLayerThickness;
    public final FloatProvider density;
    public final FloatProvider wetness;
    public final float chanceOfAmberColumnAtMaxDistanceFromCenter;
    public final int maxDistanceFromCenterAffectingChanceOfAmberColumn;
    public final int maxDistanceFromCenterAffectingHeightBias;

    public PointedAmberClusterFeatureConfig(int floorToCeilingSearchRange, IntProvider height, IntProvider radius, int maxStalagmiteStalactiteHeightDiff, int heightDeviation, IntProvider amberBlockLayerThickness, FloatProvider density, FloatProvider wetness, float wetnessMean, int maxDistanceFromCenterAffectingChanceOfAmberColumn, int maxDistanceFromCenterAffectingHeightBias) {
        this.floorToCeilingSearchRange = floorToCeilingSearchRange;
        this.height = height;
        this.radius = radius;
        this.maxStalagmiteStalactiteHeightDiff = maxStalagmiteStalactiteHeightDiff;
        this.heightDeviation = heightDeviation;
        this.amberBlockLayerThickness = amberBlockLayerThickness;
        this.density = density;
        this.wetness = wetness;
        this.chanceOfAmberColumnAtMaxDistanceFromCenter = wetnessMean;
        this.maxDistanceFromCenterAffectingChanceOfAmberColumn = maxDistanceFromCenterAffectingChanceOfAmberColumn;
        this.maxDistanceFromCenterAffectingHeightBias = maxDistanceFromCenterAffectingHeightBias;
    }
}
