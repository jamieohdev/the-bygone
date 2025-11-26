package com.jamiedev.bygone.common.worldgen.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.levelgen.feature.configurations.BlockPileConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public class AncientForestVegetationFeatureConfig extends BlockPileConfiguration {
    public static final Codec<AncientForestVegetationFeatureConfig> VEGETATION_CODEC = RecordCodecBuilder.create((instance) -> {
        return instance.group(BlockStateProvider.CODEC.fieldOf("state_provider").forGetter((config) -> {
            return config.stateProvider;
        }), ExtraCodecs.POSITIVE_INT.fieldOf("spread_width").forGetter((config) -> {
            return config.spreadWidth;
        }), ExtraCodecs.POSITIVE_INT.fieldOf("spread_height").forGetter((config) -> {
            return config.spreadHeight;
        })).apply(instance, AncientForestVegetationFeatureConfig::new);
    });
    public final int spreadWidth;
    public final int spreadHeight;

    public AncientForestVegetationFeatureConfig(BlockStateProvider stateProvider, int spreadWidth, int spreadHeight) {
        super(stateProvider);
        this.spreadWidth = spreadWidth;
        this.spreadHeight = spreadHeight;
    }
}
