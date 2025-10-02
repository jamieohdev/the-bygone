package com.jamiedev.bygone.common.worldgen.feature;

import com.jamiedev.bygone.common.worldgen.feature.config.SableBranchConfig;
import com.jamiedev.bygone.core.registry.BGBlocks;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class SableGrassFeature extends Feature<SableBranchConfig>
{

    public SableGrassFeature(Codec<SableBranchConfig> codec) {
        super(codec);
    }

    public boolean place(FeaturePlaceContext<SableBranchConfig> context) {
        WorldGenLevel worldgenlevel = context.level();
        BlockPos blockpos = context.origin();
        if (isInvalidPlacementLocation(worldgenlevel, blockpos)) {
            return false;
        } else {
            RandomSource randomsource = context.random();
            SableBranchConfig SableBranchconfig = (SableBranchConfig)context.config();
            int i = SableBranchconfig.spreadWidth();
            int j = SableBranchconfig.spreadHeight();
            int k = 3;
            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

            for(int l = 0; l < i * i; ++l) {
                blockpos$mutableblockpos.set(blockpos).move(Mth.nextInt(randomsource, -i, i), Mth.nextInt(randomsource, -j, j), Mth.nextInt(randomsource, -i, i));
                if (findFirstAirBlockAboveGround(worldgenlevel, blockpos$mutableblockpos) && !isInvalidPlacementLocation(worldgenlevel, blockpos$mutableblockpos)) {
                    int i1 = Mth.nextInt(randomsource, 3, 4);
                    if (randomsource.nextInt(6) == 0) {
                        i1 *= 2;
                    }

                    if (randomsource.nextInt(5) == 0) {
                        i1 = 1;
                    }

                    placeWeepingVinesColumn(worldgenlevel, randomsource, blockpos$mutableblockpos, i1, 17, 25);
                }
            }

            return true;
        }
    }

    private static boolean findFirstAirBlockAboveGround(LevelAccessor level, BlockPos.MutableBlockPos pos) {
        do {
            pos.move(0, -1, 0);
            if (level.isOutsideBuildHeight(pos)) {
                return false;
            }
        } while(level.getBlockState(pos).isAir());

        pos.move(0, 1, 0);
        return true;
    }

    public static void placeWeepingVinesColumn(LevelAccessor level, RandomSource random, BlockPos.MutableBlockPos pos, int length, int minAge, int maxAge) {
        for(int i = 1; i <= length; ++i) {
            if (level.isEmptyBlock(pos)) {
                if (i == length || !level.isEmptyBlock(pos.above())) {

                    if (random.nextInt(5) == 1)
                    {
                        level.setBlock(pos, (BlockState) BGBlocks.SABLOSSOM.get().defaultBlockState().setValue(GrowingPlantHeadBlock.AGE, Mth.nextInt(random, minAge, maxAge)), 2);
                        break;
                    }
                    else
                    {
                        level.setBlock(pos, (BlockState) BGBlocks.SABLE_GRASS.get().defaultBlockState().setValue(GrowingPlantHeadBlock.AGE, Mth.nextInt(random, minAge, maxAge)), 2);
                        break;
                    }
                }

                level.setBlock(pos, BGBlocks.SABLE_GRASS_PLANT.get().defaultBlockState(), 2);
            }

            pos.move(Direction.UP);
        }

    }

    private static boolean isInvalidPlacementLocation(LevelAccessor level, BlockPos pos) {
        if (!level.isEmptyBlock(pos)) {
            return true;
        } else {
            BlockState blockstate = level.getBlockState(pos.below());
            return !blockstate.is(BGBlocks.SABLE_MOSS_BLOCK.get())
                    && !blockstate.is(BGBlocks.SABLE_WOOD.get())
                    && !blockstate.is(BGBlocks.UMBRAL_SOIL.get());
        }
    }
}
