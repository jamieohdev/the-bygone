package com.jamiedev.bygone.common.worldgen.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public record MegalithConfig(IntProvider sizeX, IntProvider sizeY, IntProvider sizeZ,
                             double erosionChance, BlockStateProvider blocks, boolean canReplaceBlocks
) implements FeatureConfiguration {

    public static final Codec<MegalithConfig> CODEC = RecordCodecBuilder.create(
            (instance) ->
                    instance.group(
                            IntProvider.codec(1, 16).fieldOf("size_x").forGetter(MegalithConfig::sizeX),
                            IntProvider.codec(1, 16).fieldOf("size_y").forGetter(MegalithConfig::sizeY),
                            IntProvider.codec(1, 16).fieldOf("size_z").forGetter(MegalithConfig::sizeZ),
                            Codec.doubleRange(0.0D, 1.0D).fieldOf("erosion_chance").forGetter(MegalithConfig::erosionChance),
                            BlockStateProvider.CODEC.fieldOf("blocks").forGetter(MegalithConfig::blocks),
                            Codec.BOOL.fieldOf("can_replace_blocks").forGetter(MegalithConfig::canReplaceBlocks)
                    ).apply(instance, MegalithConfig::new)
    );


}
