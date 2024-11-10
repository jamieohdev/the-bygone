package com.jamiedev.mod.common.blocks.entity;

import com.jamiedev.mod.common.blocks.BlemishVeinBlock;
import com.jamiedev.mod.common.init.JamiesModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.MultifaceGrowthBlock;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public interface BlemishSpreadable {
    BlemishSpreadable VEIN_ONLY_SPREADER = new BlemishSpreadable() {
        public boolean spread(WorldAccess world, BlockPos pos, BlockState state, @Nullable Collection<Direction> directions, boolean markForPostProcessing) {
            if (directions == null) {
                return ((BlemishVeinBlock) JamiesModBlocks.BLEMISH_VEIN).getSamePositionOnlyGrower().grow(world.getBlockState(pos), world, pos, markForPostProcessing) > 0L;
            } else if (!directions.isEmpty()) {
                return (state.isAir() || state.getFluidState().isOf(Fluids.WATER)) && BlemishVeinBlock.place(world, pos, state, directions);
            } else {
                return BlemishSpreadable.super.spread(world, pos, state, directions, markForPostProcessing);
            }
        }

        public int spread(BlemishSpreadManager.Cursor cursor, WorldAccess world, BlockPos catalystPos, Random random, BlemishSpreadManager spreadManager, boolean shouldConvertToBlock) {
            return cursor.getDecay() > 0 ? cursor.getCharge() : 0;
        }

        public int getDecay(int oldDecay) {
            return Math.max(oldDecay - 1, 0);
        }
    };

    default byte getUpdate() {
        return 1;
    }

    default void spreadAtSamePosition(WorldAccess world, BlockState state, BlockPos pos, Random random) {
    }

    default boolean method_41470(WorldAccess world, BlockPos pos, Random random) {
        return false;
    }

    default boolean spread(WorldAccess world, BlockPos pos, BlockState state, @Nullable Collection<Direction> directions, boolean markForPostProcessing) {
        return ((MultifaceGrowthBlock) JamiesModBlocks.BLEMISH_VEIN).getGrower().grow(state, world, pos, markForPostProcessing) > 0L;
    }

    default boolean shouldConvertToSpreadable() {
        return true;
    }

    default int getDecay(int oldDecay) {
        return 1;
    }

    int spread(BlemishSpreadManager.Cursor cursor, WorldAccess world, BlockPos catalystPos, Random random, BlemishSpreadManager spreadManager, boolean shouldConvertToBlock);
}
