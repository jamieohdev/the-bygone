package com.jamiedev.mod.common.blocks.entity;

import com.jamiedev.mod.common.blocks.BlemishVeinBlock;
import com.jamiedev.mod.fabric.init.JamiesModBlocks;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;

public interface BlemishSpreadable {
    BlemishSpreadable VEIN_ONLY_SPREADER = new BlemishSpreadable() {
        public boolean spread(LevelAccessor world, BlockPos pos, BlockState state, @Nullable Collection<Direction> directions, boolean markForPostProcessing) {
            if (directions == null) {
                return ((BlemishVeinBlock) JamiesModBlocks.BLEMISH_VEIN).getSamePositionOnlyGrower().spreadAll(world.getBlockState(pos), world, pos, markForPostProcessing) > 0L;
            } else if (!directions.isEmpty()) {
                return (state.isAir() || state.getFluidState().is(Fluids.WATER)) && BlemishVeinBlock.place(world, pos, state, directions);
            } else {
                return BlemishSpreadable.super.spread(world, pos, state, directions, markForPostProcessing);
            }
        }

        public int spread(BlemishSpreadManager.Cursor cursor, LevelAccessor world, BlockPos catalystPos, RandomSource random, BlemishSpreadManager spreadManager, boolean shouldConvertToBlock) {
            return cursor.getDecay() > 0 ? cursor.getCharge() : 0;
        }

        public int getDecay(int oldDecay) {
            return Math.max(oldDecay - 1, 0);
        }
    };

    default byte getUpdate() {
        return 1;
    }

    default void spreadAtSamePosition(LevelAccessor world, BlockState state, BlockPos pos, RandomSource random) {
    }

    default boolean method_41470(LevelAccessor world, BlockPos pos, RandomSource random) {
        return false;
    }

    default boolean spread(LevelAccessor world, BlockPos pos, BlockState state, @Nullable Collection<Direction> directions, boolean markForPostProcessing) {
        return ((MultifaceBlock) JamiesModBlocks.BLEMISH_VEIN).getSpreader().spreadAll(state, world, pos, markForPostProcessing) > 0L;
    }

    default boolean shouldConvertToSpreadable() {
        return true;
    }

    default int getDecay(int oldDecay) {
        return 1;
    }

    int spread(BlemishSpreadManager.Cursor cursor, LevelAccessor world, BlockPos catalystPos, RandomSource random, BlemishSpreadManager spreadManager, boolean shouldConvertToBlock);
}
