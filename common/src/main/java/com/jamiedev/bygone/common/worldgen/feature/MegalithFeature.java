package com.jamiedev.bygone.common.worldgen.feature;

import com.jamiedev.bygone.common.worldgen.feature.config.MegalithConfig;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class MegalithFeature extends Feature<MegalithConfig> {
    public MegalithFeature(Codec<MegalithConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean place(FeaturePlaceContext<MegalithConfig> context) {
        WorldGenLevel level = context.level();
        RandomSource random = context.random();
        MegalithConfig megalithConfig = context.config();
        BlockPos blockPos = context.origin();
        int lengthX = megalithConfig.sizeX().sample(random);
        int lengthY = megalithConfig.sizeY().sample(random);
        int lengthZ = megalithConfig.sizeZ().sample(random);

        for (int x = 0; x < lengthX; x++) {
            for (int y = 0; y < lengthY; y++) {
                for (int z = 0; z < lengthZ; z++) {
                    BlockPos currPos = blockPos.offset(x, y, z);
                    // Early exit, if it cannot replace blocks and the current block is not air
                    if (!megalithConfig.canReplaceBlocks()) {
                        if (!level.isEmptyBlock(currPos)) {
                            continue;
                        }
                    }

                    BlockState blockState = Blocks.AIR.defaultBlockState();
                    // Early exit with air, if on an edge and erosion chance met
                    if ((x == 0 || x == lengthX - 1 ||
                            y == 0 || y == lengthY - 1 ||
                            z == 0 || z == lengthZ - 1)) {
                        if (random.nextDouble() < megalithConfig.erosionChance()) {
                            this.setBlock(level, currPos, blockState);
                            continue;
                        }
                    }
                    // Otherwise, use the block provider
                    blockState = megalithConfig.blocks().getState(random, currPos);
                    this.setBlock(level, currPos, blockState);
                }
            }
        }

        return true;
    }
}
