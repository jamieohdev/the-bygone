package com.jamiedev.bygone.common.worldgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PrimordialCoralTreeFeature extends PrimordialCoralFeature {
    public PrimordialCoralTreeFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    protected boolean generateCoral(LevelAccessor world, @NotNull RandomSource random, BlockPos pos, BlockState state) {
        BlockPos.MutableBlockPos mutable = pos.mutable();
        int i = random.nextInt(3) + 1;

        for (int j = 0; j < i; ++j) {
            if (!this.generateCoralPiece(world, random, mutable, state)) {
                return true;
            }

            mutable.move(Direction.UP);
        }

        BlockPos blockPos = mutable.immutable();
        int k = random.nextInt(3) + 2;
        List<Direction> list = Direction.Plane.HORIZONTAL.shuffledCopy(random);
        List<Direction> list2 = list.subList(0, k);

        for (Direction direction : list2) {
            mutable.set(blockPos);
            mutable.move(direction);
            int l = random.nextInt(5) + 2;
            int m = 0;

            for (int n = 0; n < l && this.generateCoralPiece(world, random, mutable, state); ++n) {
                ++m;
                mutable.move(Direction.UP);
                if (n == 0 || m >= 2 && random.nextFloat() < 0.25F) {
                    mutable.move(direction);
                    m = 0;
                }
            }
        }

        return true;
    }
}
