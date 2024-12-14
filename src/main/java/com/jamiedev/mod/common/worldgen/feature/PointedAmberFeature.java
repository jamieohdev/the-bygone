package com.jamiedev.mod.common.worldgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

import java.util.Iterator;
import java.util.Optional;

public class PointedAmberFeature  extends Feature<PointedAmberFeatureConfig> {
    public PointedAmberFeature(Codec<PointedAmberFeatureConfig> codec) {
        super(codec);
    }

    public boolean generate(FeatureContext<PointedAmberFeatureConfig> context) {
        WorldAccess worldAccess = context.getWorld();
        BlockPos blockPos = context.getOrigin();
        Random random = context.getRandom();
        PointedAmberFeatureConfig smallAmberFeatureConfig = (PointedAmberFeatureConfig)context.getConfig();
        Optional<Direction> optional = getDirection(worldAccess, blockPos, random);
        if (optional.isEmpty()) {
            return false;
        } else {
            BlockPos blockPos2 = blockPos.offset(((Direction)optional.get()).getOpposite());
            generateAmberBlocks(worldAccess, random, blockPos2, smallAmberFeatureConfig);
            int i = random.nextFloat() < smallAmberFeatureConfig.chanceOfTallerAmber && AmberHelper.canGenerate(worldAccess.getBlockState(blockPos.offset((Direction)optional.get()))) ? 2 : 1;
            AmberHelper.generatePointedAmber(worldAccess, blockPos, (Direction)optional.get(), i, false);
            return true;
        }
    }

    private static Optional<Direction> getDirection(WorldAccess world, BlockPos pos, Random random) {
        boolean bl = AmberHelper.canReplace(world.getBlockState(pos.up()));
        boolean bl2 = AmberHelper.canReplace(world.getBlockState(pos.down()));
        if (bl && bl2) {
            return Optional.of(random.nextBoolean() ? Direction.DOWN : Direction.UP);
        } else if (bl) {
            return Optional.of(Direction.DOWN);
        } else {
            return bl2 ? Optional.of(Direction.UP) : Optional.empty();
        }
    }

    private static void generateAmberBlocks(WorldAccess world, Random random, BlockPos pos, PointedAmberFeatureConfig config) {
        AmberHelper.generateAmberBlock(world, pos);
        Iterator var4 = Direction.Type.HORIZONTAL.iterator();

        while(var4.hasNext()) {
            Direction direction = (Direction)var4.next();
            if (!(random.nextFloat() > config.chanceOfDirectionalSpread)) {
                BlockPos blockPos = pos.offset(direction);
                AmberHelper.generateAmberBlock(world, blockPos);
                if (!(random.nextFloat() > config.chanceOfSpreadRadius2)) {
                    BlockPos blockPos2 = blockPos.offset(Direction.random(random));
                    AmberHelper.generateAmberBlock(world, blockPos2);
                    if (!(random.nextFloat() > config.chanceOfSpreadRadius3)) {
                        BlockPos blockPos3 = blockPos2.offset(Direction.random(random));
                        AmberHelper.generateAmberBlock(world, blockPos3);
                    }
                }
            }
        }

    }
}
