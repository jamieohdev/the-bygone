package com.jamiedev.bygone.worldgen.feature;

import com.mojang.serialization.Codec;
import java.util.Iterator;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import org.jetbrains.annotations.NotNull;

public class PointedAmberFeature  extends Feature<PointedAmberFeatureConfig> {
    public PointedAmberFeature(Codec<PointedAmberFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<PointedAmberFeatureConfig> context) {
        LevelAccessor worldAccess = context.level();
        BlockPos blockPos = context.origin();
        RandomSource random = context.random();
        PointedAmberFeatureConfig smallAmberFeatureConfig = context.config();
        Optional<Direction> optional = getDirection(worldAccess, blockPos, random);
        if (optional.isEmpty()) {
            return false;
        } else {
            BlockPos blockPos2 = blockPos.relative(optional.get().getOpposite());
            generateAmberBlocks(worldAccess, random, blockPos2, smallAmberFeatureConfig);
            int i = random.nextFloat() < smallAmberFeatureConfig.chanceOfTallerAmber && AmberHelper.canGenerate(worldAccess.getBlockState(blockPos.relative(optional.get()))) ? 2 : 1;
            AmberHelper.generatePointedAmber(worldAccess, blockPos, optional.get(), i, false);
            return true;
        }
    }

    private static Optional<Direction> getDirection(LevelAccessor world, BlockPos pos, @NotNull RandomSource random) {
        boolean bl = AmberHelper.canReplace(world.getBlockState(pos.above()));
        boolean bl2 = AmberHelper.canReplace(world.getBlockState(pos.below()));
        if (bl && bl2) {
            return Optional.of(random.nextBoolean() ? Direction.DOWN : Direction.UP);
        } else if (bl) {
            return Optional.of(Direction.DOWN);
        } else {
            return bl2 ? Optional.of(Direction.UP) : Optional.empty();
        }
    }

    private static void generateAmberBlocks(LevelAccessor world, @NotNull RandomSource random, BlockPos pos, PointedAmberFeatureConfig config) {
        AmberHelper.generateAmberBlock(world, pos);
        Iterator<Direction> var4 = Direction.Plane.HORIZONTAL.iterator();

        while(var4.hasNext()) {
            Direction direction = (Direction)var4.next();
            if (!(random.nextFloat() > config.chanceOfDirectionalSpread)) {
                BlockPos blockPos = pos.relative(direction);
                AmberHelper.generateAmberBlock(world, blockPos);
                if (!(random.nextFloat() > config.chanceOfSpreadRadius2)) {
                    BlockPos blockPos2 = blockPos.relative(Direction.getRandom(random));
                    AmberHelper.generateAmberBlock(world, blockPos2);
                    if (!(random.nextFloat() > config.chanceOfSpreadRadius3)) {
                        BlockPos blockPos3 = blockPos2.relative(Direction.getRandom(random));
                        AmberHelper.generateAmberBlock(world, blockPos3);
                    }
                }
            }
        }

    }
}
