package com.jamiedev.bygone.common.worldgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Stream;

public class BycoralClawFeature extends BycoralFeature {
    public BycoralClawFeature(Codec<BlockStateConfiguration> codec) {
        super(codec);
    }

    @Override
    protected boolean placeFeature(@NotNull LevelAccessor world, @NotNull RandomSource random, @NotNull BlockPos pos, @NotNull BlockState state) {
        if (!this.placeBycoralBlock(world, random, pos, state)) {
            return false;
        } else {
            Direction primaryBranchingDirection = Direction.Plane.HORIZONTAL.getRandomDirection(random);
            int numberOfBranches = random.nextInt(2) + 2;
            List<Direction> directionsToPickFrom = Util.toShuffledList(Stream.of(primaryBranchingDirection, primaryBranchingDirection.getClockWise(), primaryBranchingDirection.getCounterClockWise()), random);
            List<Direction> directionsToBranchIn = directionsToPickFrom.subList(0, numberOfBranches);

            for (Direction branchDirection : directionsToBranchIn) {
                BlockPos.MutableBlockPos mutable = pos.mutable();
                int stepsInOtherBranchDirection = random.nextInt(2) + 1;
                mutable.move(branchDirection);
                int stepsInPrimaryBranchDirection;
                Direction otherBranchDirection;
                if (branchDirection == primaryBranchingDirection) {
                    otherBranchDirection = primaryBranchingDirection;
                    stepsInPrimaryBranchDirection = random.nextInt(3) + 2;
                } else {
                    mutable.move(Direction.UP);
                    Direction[] directions = new Direction[]{branchDirection, Direction.UP};
                    otherBranchDirection = Util.getRandom(directions, random);
                    stepsInPrimaryBranchDirection = random.nextInt(3) + 3;
                }

                for (int i = 0; i < stepsInOtherBranchDirection && this.placeBycoralBlock(world, random, mutable, state); ++i) {
                    mutable.move(otherBranchDirection);
                }

                mutable.move(otherBranchDirection.getOpposite());
                mutable.move(Direction.UP);

                for (int i = 0; i < stepsInPrimaryBranchDirection; ++i) {
                    mutable.move(primaryBranchingDirection);
                    if (!this.placeBycoralBlock(world, random, mutable, state)) {
                        break;
                    }

                    if (random.nextFloat() < 0.25F) {
                        mutable.move(Direction.UP);
                    }
                }
            }

            return true;
        }
    }
}
