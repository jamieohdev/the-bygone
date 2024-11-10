package com.jamiedev.mod.common.worldgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;

import java.util.List;

public class PrimordialCoralTreeFeature extends PrimordialCoralFeature {
    public PrimordialCoralTreeFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    protected boolean generateCoral(WorldAccess world, Random random, BlockPos pos, BlockState state) {
        BlockPos.Mutable mutable = pos.mutableCopy();
        int i = random.nextInt(3) + 1;

        for(int j = 0; j < i; ++j) {
            if (!this.generateCoralPiece(world, random, mutable, state)) {
                return true;
            }

            mutable.move(Direction.UP);
        }

        BlockPos blockPos = mutable.toImmutable();
        int k = random.nextInt(3) + 2;
        List<Direction> list = Direction.Type.HORIZONTAL.getShuffled(random);
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
