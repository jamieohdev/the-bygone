package com.jamiedev.bygone.common.worldgen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class PointedAmberFeatureConfig implements FeatureConfiguration {
    public static final Codec<PointedAmberFeatureConfig> CODEC = RecordCodecBuilder.create((instance) -> {
        return instance.group(Codec.floatRange(0.0F, 1.0F).fieldOf("chance_of_taller_amber").orElse(0.2F).forGetter((config) -> {
            return config.chanceOfTallerAmber;
        }), Codec.floatRange(0.0F, 1.0F).fieldOf("chance_of_directional_spread").orElse(0.7F).forGetter((config) -> {
            return config.chanceOfDirectionalSpread;
        }), Codec.floatRange(0.0F, 1.0F).fieldOf("chance_of_spread_radius2").orElse(0.5F).forGetter((config) -> {
            return config.chanceOfSpreadRadius2;
        }), Codec.floatRange(0.0F, 1.0F).fieldOf("chance_of_spread_radius3").orElse(0.5F).forGetter((config) -> {
            return config.chanceOfSpreadRadius3;
        })).apply(instance, PointedAmberFeatureConfig::new);
    });
    public final float chanceOfTallerAmber;
    public final float chanceOfDirectionalSpread;
    public final float chanceOfSpreadRadius2;
    public final float chanceOfSpreadRadius3;

    public PointedAmberFeatureConfig(float chanceOfTallerAmber, float chanceOfDirectionalSpread, float chanceOfSpreadRadius2, float chanceOfSpreadRadius3) {
        this.chanceOfTallerAmber = chanceOfTallerAmber;
        this.chanceOfDirectionalSpread = chanceOfDirectionalSpread;
        this.chanceOfSpreadRadius2 = chanceOfSpreadRadius2;
        this.chanceOfSpreadRadius3 = chanceOfSpreadRadius3;
    }
}
