package com.jamiedev.mod.common.worldgen.feature;

import com.jamiedev.mod.fabric.init.JamiesModBlocks;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class AmberUnderFeature  extends Feature<NoneFeatureConfiguration> {
    public AmberUnderFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel structureWorldAccess = context.level();
        BlockPos blockPos = context.origin();
        RandomSource random = context.random();
        if (!structureWorldAccess.isEmptyBlock(blockPos)) {
            return false;
        } else {
            BlockState blockState = structureWorldAccess.getBlockState(blockPos.above());
            if (!blockState.is(JamiesModBlocks.AMBERSTONE) && !blockState.is(JamiesModBlocks.COBBLED_AMBERSTONE)) {
                return false;
            } else {
                structureWorldAccess.setBlock(blockPos, JamiesModBlocks.FLOWING_AMBER.defaultBlockState(), 2);

                for(int i = 0; i < 1500; ++i) {
                    BlockPos blockPos2 = blockPos.offset(random.nextInt(8) - random.nextInt(8), -random.nextInt(12), random.nextInt(8) - random.nextInt(8));
                    if (structureWorldAccess.getBlockState(blockPos2).isAir()) {
                        int j = 0;
                        Direction[] var9 = Direction.values();
                        int var10 = var9.length;

                        for (Direction direction : var9) {
                            if (structureWorldAccess.getBlockState(blockPos2.relative(direction)).is(JamiesModBlocks.FLOWING_AMBER)) {
                                ++j;
                            }

                            if (j > 1) {
                                break;
                            }
                        }

                        if (j == 1) {
                            structureWorldAccess.setBlock(blockPos2, JamiesModBlocks.FLOWING_AMBER.defaultBlockState(), 2);
                        }
                    }
                }

                return true;
            }
        }
    }
}
