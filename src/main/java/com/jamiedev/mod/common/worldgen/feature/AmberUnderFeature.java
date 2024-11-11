package com.jamiedev.mod.common.worldgen.feature;

import com.jamiedev.mod.fabric.init.JamiesModBlocks;
import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class AmberUnderFeature  extends Feature<DefaultFeatureConfig> {
    public AmberUnderFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        StructureWorldAccess structureWorldAccess = context.getWorld();
        BlockPos blockPos = context.getOrigin();
        Random random = context.getRandom();
        if (!structureWorldAccess.isAir(blockPos)) {
            return false;
        } else {
            BlockState blockState = structureWorldAccess.getBlockState(blockPos.up());
            if (!blockState.isOf(JamiesModBlocks.AMBERSTONE) && !blockState.isOf(JamiesModBlocks.COBBLED_AMBERSTONE)) {
                return false;
            } else {
                structureWorldAccess.setBlockState(blockPos, JamiesModBlocks.FLOWING_AMBER.getDefaultState(), 2);

                for(int i = 0; i < 1500; ++i) {
                    BlockPos blockPos2 = blockPos.add(random.nextInt(8) - random.nextInt(8), -random.nextInt(12), random.nextInt(8) - random.nextInt(8));
                    if (structureWorldAccess.getBlockState(blockPos2).isAir()) {
                        int j = 0;
                        Direction[] var9 = Direction.values();
                        int var10 = var9.length;

                        for (Direction direction : var9) {
                            if (structureWorldAccess.getBlockState(blockPos2.offset(direction)).isOf(JamiesModBlocks.FLOWING_AMBER)) {
                                ++j;
                            }

                            if (j > 1) {
                                break;
                            }
                        }

                        if (j == 1) {
                            structureWorldAccess.setBlockState(blockPos2, JamiesModBlocks.FLOWING_AMBER.getDefaultState(), 2);
                        }
                    }
                }

                return true;
            }
        }
    }
}
