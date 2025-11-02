package com.jamiedev.bygone.common.worldgen.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public record SmallCloudConfig(int bounds, BlockStateProvider block) implements FeatureConfiguration {
    public static final Codec<SmallCloudConfig> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            Codec.INT.fieldOf("bounds").forGetter(SmallCloudConfig::bounds),
            BlockStateProvider.CODEC.fieldOf("blocks").forGetter(SmallCloudConfig::block)
    ).apply(instance, SmallCloudConfig::new));
}