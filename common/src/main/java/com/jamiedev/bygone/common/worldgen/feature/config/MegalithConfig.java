package com.jamiedev.bygone.common.worldgen.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public record MegalithConfig(IntProvider sizeX, IntProvider sizeY, IntProvider sizeZ,
                             BlockStateProvider megalithBlocks, double erosionChance, int maxErosionDepth,
                             IntProvider fallenBlockCount, IntProvider floatingBlockCount,
                             int fallenBlockSearchRange, boolean canReplaceBlocks
) implements FeatureConfiguration {

    public static final Codec<MegalithConfig> CODEC = RecordCodecBuilder.create(
            (instance) ->
                    instance.group(
                            IntProvider.codec(1, 16).fieldOf("size_x").forGetter(MegalithConfig::sizeX),
                            IntProvider.codec(1, 16).fieldOf("size_y").forGetter(MegalithConfig::sizeY),
                            IntProvider.codec(1, 16).fieldOf("size_z").forGetter(MegalithConfig::sizeZ),
                            BlockStateProvider.CODEC.fieldOf("megalith_blocks").forGetter(MegalithConfig::megalithBlocks),
                            Codec.doubleRange(0.0D, 1.0D).fieldOf("erosion_chance").forGetter(MegalithConfig::erosionChance),
                            Codec.intRange(0, 16).fieldOf("max_erosion_depth").forGetter(MegalithConfig::maxErosionDepth),
                            IntProvider.codec(0, 8).fieldOf("fallen_block_count").forGetter(MegalithConfig::fallenBlockCount),
                            IntProvider.codec(0, 8).fieldOf("floating_block_count").forGetter(MegalithConfig::floatingBlockCount),
                            Codec.intRange(0, 32).fieldOf("fallen_block_search_range").forGetter(MegalithConfig::fallenBlockSearchRange),
                            Codec.BOOL.fieldOf("can_replace_blocks").forGetter(MegalithConfig::canReplaceBlocks)
                    ).apply(instance, MegalithConfig::new)
    );


}
