package com.jamiedev.bygone.common.worldgen.feature;

import com.mojang.serialization.Codec;

import java.util.Iterator;
import java.util.Optional;
import java.util.OptionalInt;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ClampedNormalFloat;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Column;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import org.jetbrains.annotations.NotNull;

public class PointedAmberClusterFeature extends Feature<PointedAmberClusterFeatureConfig> {
    public PointedAmberClusterFeature(Codec<PointedAmberClusterFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<PointedAmberClusterFeatureConfig> context) {
        WorldGenLevel structureWorldAccess = context.level();
        BlockPos blockPos = context.origin();
        PointedAmberClusterFeatureConfig PointedAmberClusterFeatureConfig = context.config();
        RandomSource random = context.random();
        if (!AmberHelper.canGenerate(structureWorldAccess, blockPos)) {
            return false;
        } else {
            int i = PointedAmberClusterFeatureConfig.height.sample(random);
            float f = PointedAmberClusterFeatureConfig.wetness.sample(random);
            float g = PointedAmberClusterFeatureConfig.density.sample(random);
            int j = PointedAmberClusterFeatureConfig.radius.sample(random);
            int k = PointedAmberClusterFeatureConfig.radius.sample(random);

            for(int l = -j; l <= j; ++l) {
                for(int m = -k; m <= k; ++m) {
                    double d = this.amberChance(j, k, l, m, PointedAmberClusterFeatureConfig);
                    BlockPos blockPos2 = blockPos.offset(l, 0, m);
                    this.generate(structureWorldAccess, random, blockPos2, l, m, f, d, i, g, PointedAmberClusterFeatureConfig);
                }
            }

            return true;
        }
    }

    private void generate(WorldGenLevel world, @NotNull RandomSource random, BlockPos pos, int localX, int localZ, float wetness, double amberChance, int height, float density, PointedAmberClusterFeatureConfig config) {
        Optional<Column> optional = Column.scan(world, pos, config.floorToCeilingSearchRange, AmberHelper::canGenerate, AmberHelper::cannotGenerate);
        if (!optional.isEmpty()) {
            OptionalInt optionalInt = optional.get().getCeiling();
            OptionalInt optionalInt2 = optional.get().getFloor();
            if (!optionalInt.isEmpty() || !optionalInt2.isEmpty()) {
                boolean bl = random.nextFloat() < wetness;
                Column caveSurface;
                if (bl && optionalInt2.isPresent() && this.canWaterSpawn(world, pos.atY(optionalInt2.getAsInt()))) {
                    int i = optionalInt2.getAsInt();
                    caveSurface = optional.get().withFloor(OptionalInt.of(i - 1));
                    world.setBlock(pos.atY(i), Blocks.WATER.defaultBlockState(), 2);
                } else {
                    caveSurface = optional.get();
                }

                OptionalInt optionalInt3 = caveSurface.getFloor();
                boolean bl2 = random.nextDouble() < amberChance;
                int l;
                int j;
                if (optionalInt.isPresent() && bl2 && !this.isLava(world, pos.atY(optionalInt.getAsInt()))) {
                    j = config.amberBlockLayerThickness.sample(random);
                    this.placeAmberBlocks(world, pos.atY(optionalInt.getAsInt()), j, Direction.UP);
                    int k;
                    if (optionalInt3.isPresent()) {
                        k = Math.min(height, optionalInt.getAsInt() - optionalInt3.getAsInt());
                    } else {
                        k = height;
                    }

                    l = this.getHeight(random, localX, localZ, density, k, config);
                } else {
                    l = 0;
                }

                boolean bl3 = random.nextDouble() < amberChance;
                int m;
                if (optionalInt3.isPresent() && bl3 && !this.isLava(world, pos.atY(optionalInt3.getAsInt()))) {
                    m = config.amberBlockLayerThickness.sample(random);
                    this.placeAmberBlocks(world, pos.atY(optionalInt3.getAsInt()), m, Direction.DOWN);
                    if (optionalInt.isPresent()) {
                        j = Math.max(0, l + Mth.randomBetweenInclusive(random, -config.maxStalagmiteStalactiteHeightDiff, config.maxStalagmiteStalactiteHeightDiff));
                    } else {
                        j = this.getHeight(random, localX, localZ, density, height, config);
                    }
                } else {
                    j = 0;
                }

                int t;
                if (optionalInt.isPresent() && optionalInt3.isPresent() && optionalInt.getAsInt() - l <= optionalInt3.getAsInt() + j) {
                    int n = optionalInt3.getAsInt();
                    int o = optionalInt.getAsInt();
                    int p = Math.max(o - l, n + 1);
                    int q = Math.min(n + j, o - 1);
                    int r = Mth.randomBetweenInclusive(random, p, q + 1);
                    int s = r - 1;
                    m = o - r;
                    t = s - n;
                } else {
                    m = l;
                    t = j;
                }

                boolean bl4 = random.nextBoolean() && m > 0 && t > 0 && caveSurface.getHeight().isPresent() && m + t == caveSurface.getHeight().getAsInt();
                if (optionalInt.isPresent()) {
                    AmberHelper.generatePointedAmber(world, pos.atY(optionalInt.getAsInt() - 1), Direction.DOWN, m, bl4);
                }

                /**if (optionalInt3.isPresent()) {
                    AmberHelper.generatePointedAmber(world, pos.atY(optionalInt3.getAsInt() + 1), Direction.UP, t, bl4);
                }**/

            }
        }
    }

    private boolean isLava(LevelReader world, BlockPos pos) {
        return world.getBlockState(pos).is(Blocks.LAVA);
    }

    private int getHeight(RandomSource random, int localX, int localZ, float density, int height, PointedAmberClusterFeatureConfig config) {
        if (random.nextFloat() > density) {
            return 0;
        } else {
            int i = Math.abs(localX) + Math.abs(localZ);
            float f = (float)Mth.clampedMap(i, 0.0, config.maxDistanceFromCenterAffectingHeightBias, (double)height / 2.0, 0.0);
            return (int)clampedGaussian(random, 0.0F, (float)height, f, (float)config.heightDeviation);
        }
    }

    private boolean canWaterSpawn(WorldGenLevel world, BlockPos pos) {
        BlockState blockState = world.getBlockState(pos);
        if (!blockState.is(Blocks.WATER) && !blockState.is(Blocks.DRIPSTONE_BLOCK) && !blockState.is(Blocks.POINTED_DRIPSTONE)) {
            if (world.getBlockState(pos.above()).getFluidState().is(FluidTags.WATER)) {
                return false;
            } else {
                Iterator<Direction> var4 = Direction.Plane.HORIZONTAL.iterator();

                Direction direction;
                do {
                    if (!var4.hasNext()) {
                        return this.isStoneOrWater(world, pos.below());
                    }

                    direction = (Direction)var4.next();
                } while(this.isStoneOrWater(world, pos.relative(direction)));

                return false;
            }
        } else {
            return false;
        }
    }

    private boolean isStoneOrWater(LevelAccessor world, BlockPos pos) {
        BlockState blockState = world.getBlockState(pos);
        return blockState.is(BlockTags.BASE_STONE_OVERWORLD) || blockState.getFluidState().is(FluidTags.WATER);
    }

    private void placeAmberBlocks(WorldGenLevel world, BlockPos pos, int height, Direction direction) {
        BlockPos.MutableBlockPos mutable = pos.mutable();

        for(int i = 0; i < height; ++i) {
            if (!AmberHelper.generateAmberBlock(world, mutable)) {
                return;
            }

            mutable.move(direction);
        }

    }

    private double amberChance(int radiusX, int radiusZ, int localX, int localZ, PointedAmberClusterFeatureConfig config) {
        int i = radiusX - Math.abs(localX);
        int j = radiusZ - Math.abs(localZ);
        int k = Math.min(i, j);
        return Mth.clampedMap((float)k, 0.0F, (float)config.maxDistanceFromCenterAffectingChanceOfAmberColumn, config.chanceOfAmberColumnAtMaxDistanceFromCenter, 1.0F);
    }

    private static float clampedGaussian(RandomSource random, float min, float max, float mean, float deviation) {
        return ClampedNormalFloat.sample(random, mean, deviation, min, max);
    }
}
