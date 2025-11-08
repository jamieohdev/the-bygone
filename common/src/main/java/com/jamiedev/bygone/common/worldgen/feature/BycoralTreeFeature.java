package com.jamiedev.bygone.common.worldgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BycoralTreeFeature extends BycoralFeature {
    public BycoralTreeFeature(Codec<BlockStateConfiguration> codec) {
        super(codec);
    }

    @Override
    protected boolean placeFeature(@NotNull LevelAccessor world, @NotNull RandomSource random, @NotNull BlockPos pos, @NotNull BlockState state) {
        BlockPos.MutableBlockPos mutable = pos.mutable();
        int stemHeight = random.nextInt(3) + 1;

        for (int j = 0; j < stemHeight; ++j) {
            if (!this.placeBycoralBlock(world, random, mutable, state)) {
                return true;
            }

            mutable.move(Direction.UP);
        }

        BlockPos blockPos = mutable.immutable();
        int numberOfBranches = random.nextInt(3) + 2;
        List<Direction> shuffledHorizontalDirections = Direction.Plane.HORIZONTAL.shuffledCopy(random);
        List<Direction> branchDirections = shuffledHorizontalDirections.subList(0, numberOfBranches);

        for (Direction direction : branchDirections) {
            mutable.set(blockPos);
            mutable.move(direction);
            int branchHeight = random.nextInt(5) + 2;
            int stepsSinceMoveSideways = 0;

            for (int n = 0; n < branchHeight && this.placeBycoralBlock(world, random, mutable, state); ++n) {
                ++stepsSinceMoveSideways;
                mutable.move(Direction.UP);
                if (n == 0 || stepsSinceMoveSideways >= 2 && random.nextFloat() < 0.25F) {
                    mutable.move(direction);
                    stepsSinceMoveSideways = 0;
                }
            }
        }

        return true;
    }
}
