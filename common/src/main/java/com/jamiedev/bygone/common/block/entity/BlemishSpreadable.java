package com.jamiedev.bygone.common.block.entity;

import com.jamiedev.bygone.common.block.BlemishVeinBlock;
import com.jamiedev.bygone.core.registry.BGBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public interface BlemishSpreadable {
    BlemishSpreadable VEIN_ONLY_SPREADER = new BlemishSpreadable() {
        @Override
        public boolean spread(LevelAccessor world, BlockPos pos, BlockState state, @Nullable Collection<Direction> directions, boolean markForPostProcessing) {
            if (directions == null) {
                return ((BlemishVeinBlock) BGBlocks.BLEMISH_VEIN).getSamePositionOnlyGrower().spreadAll(world.getBlockState(pos), world, pos, markForPostProcessing) > 0L;
            } else if (!directions.isEmpty()) {
                return (state.isAir() || state.getFluidState().is(Fluids.WATER)) && BlemishVeinBlock.place(world, pos, state, directions);
            } else {
                return BlemishSpreadable.super.spread(world, pos, state, directions, markForPostProcessing);
            }
        }

        @Override
        public int spread(BlemishSpreadManager.Cursor cursor, LevelAccessor world, BlockPos catalystPos, @NotNull RandomSource random, BlemishSpreadManager spreadManager, boolean shouldConvertToBlock) {
            return cursor.getDecay() > 0 ? cursor.getCharge() : 0;
        }

        @Override
        public int getDecay(int oldDecay) {
            return Math.max(oldDecay - 1, 0);
        }
    };

    default byte getUpdate() {
        return 1;
    }

    default void spreadAtSamePosition(LevelAccessor world, BlockState state, BlockPos pos, @NotNull RandomSource random) {
    }

    default boolean method_41470(LevelAccessor world, BlockPos pos, @NotNull RandomSource random) {
        return false;
    }

    default boolean spread(LevelAccessor world, BlockPos pos, BlockState state, @Nullable Collection<Direction> directions, boolean markForPostProcessing) {
        return ((MultifaceBlock) BGBlocks.BLEMISH_VEIN).getSpreader().spreadAll(state, world, pos, markForPostProcessing) > 0L;
    }

    default boolean shouldConvertToSpreadable() {
        return true;
    }

    default int getDecay(int oldDecay) {
        return 1;
    }

    int spread(BlemishSpreadManager.Cursor cursor, LevelAccessor world, BlockPos catalystPos, @NotNull RandomSource random, BlemishSpreadManager spreadManager, boolean shouldConvertToBlock);
}
