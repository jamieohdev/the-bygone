package com.jamiedev.mod.common.worldgen.feature;

import com.jamiedev.mod.fabric.init.JamiesModBlocks;
import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.floatprovider.ClampedNormalFloatProvider;
import net.minecraft.util.math.floatprovider.FloatProvider;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.CaveSurface;
import net.minecraft.world.gen.feature.util.FeatureContext;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.Optional;
import java.util.OptionalInt;

public class PointedAmberClusterFeature extends Feature<PointedAmberClusterFeatureConfig> {
    public PointedAmberClusterFeature(Codec<PointedAmberClusterFeatureConfig> codec) {
        super(codec);
    }

    public boolean generate(FeatureContext<PointedAmberClusterFeatureConfig> context) {
        StructureWorldAccess structureWorldAccess = context.getWorld();
        BlockPos blockPos = context.getOrigin();
        PointedAmberClusterFeatureConfig PointedAmberClusterFeatureConfig = (PointedAmberClusterFeatureConfig)context.getConfig();
        Random random = context.getRandom();
        if (!AmberHelper.canGenerate(structureWorldAccess, blockPos)) {
            return false;
        } else {
            int i = PointedAmberClusterFeatureConfig.height.get(random);
            float f = PointedAmberClusterFeatureConfig.wetness.get(random);
            float g = PointedAmberClusterFeatureConfig.density.get(random);
            int j = PointedAmberClusterFeatureConfig.radius.get(random);
            int k = PointedAmberClusterFeatureConfig.radius.get(random);

            for(int l = -j; l <= j; ++l) {
                for(int m = -k; m <= k; ++m) {
                    double d = this.amberChance(j, k, l, m, PointedAmberClusterFeatureConfig);
                    BlockPos blockPos2 = blockPos.add(l, 0, m);
                    this.generate(structureWorldAccess, random, blockPos2, l, m, f, d, i, g, PointedAmberClusterFeatureConfig);
                }
            }

            return true;
        }
    }

    private void generate(StructureWorldAccess world, Random random, BlockPos pos, int localX, int localZ, float wetness, double amberChance, int height, float density, PointedAmberClusterFeatureConfig config) {
        Optional<CaveSurface> optional = CaveSurface.create(world, pos, config.floorToCeilingSearchRange, AmberHelper::canGenerate, AmberHelper::cannotGenerate);
        if (!optional.isEmpty()) {
            OptionalInt optionalInt = ((CaveSurface)optional.get()).getCeilingHeight();
            OptionalInt optionalInt2 = ((CaveSurface)optional.get()).getFloorHeight();
            if (!optionalInt.isEmpty() || !optionalInt2.isEmpty()) {
                boolean bl = random.nextFloat() < wetness;
                CaveSurface caveSurface;
                if (bl && optionalInt2.isPresent() && this.canWaterSpawn(world, pos.withY(optionalInt2.getAsInt()))) {
                    int i = optionalInt2.getAsInt();
                    caveSurface = ((CaveSurface)optional.get()).withFloor(OptionalInt.of(i - 1));
                    world.setBlockState(pos.withY(i), Blocks.WATER.getDefaultState(), 2);
                } else {
                    caveSurface = (CaveSurface)optional.get();
                }

                OptionalInt optionalInt3 = caveSurface.getFloorHeight();
                boolean bl2 = random.nextDouble() < amberChance;
                int l;
                int j;
                if (optionalInt.isPresent() && bl2 && !this.isLava(world, pos.withY(optionalInt.getAsInt()))) {
                    j = config.amberBlockLayerThickness.get(random);
                    this.placeAmberBlocks(world, pos.withY(optionalInt.getAsInt()), j, Direction.UP);
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
                if (optionalInt3.isPresent() && bl3 && !this.isLava(world, pos.withY(optionalInt3.getAsInt()))) {
                    m = config.amberBlockLayerThickness.get(random);
                    this.placeAmberBlocks(world, pos.withY(optionalInt3.getAsInt()), m, Direction.DOWN);
                    if (optionalInt.isPresent()) {
                        j = Math.max(0, l + MathHelper.nextBetween(random, -config.maxStalagmiteStalactiteHeightDiff, config.maxStalagmiteStalactiteHeightDiff));
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
                    int r = MathHelper.nextBetween(random, p, q + 1);
                    int s = r - 1;
                    m = o - r;
                    t = s - n;
                } else {
                    m = l;
                    t = j;
                }

                boolean bl4 = random.nextBoolean() && m > 0 && t > 0 && caveSurface.getOptionalHeight().isPresent() && m + t == caveSurface.getOptionalHeight().getAsInt();
                if (optionalInt.isPresent()) {
                    AmberHelper.generatePointedAmber(world, pos.withY(optionalInt.getAsInt() - 1), Direction.DOWN, m, bl4);
                }

                if (optionalInt3.isPresent()) {
                    AmberHelper.generatePointedAmber(world, pos.withY(optionalInt3.getAsInt() + 1), Direction.UP, t, bl4);
                }

            }
        }
    }

    private boolean isLava(WorldView world, BlockPos pos) {
        return world.getBlockState(pos).isOf(Blocks.LAVA);
    }

    private int getHeight(Random random, int localX, int localZ, float density, int height, PointedAmberClusterFeatureConfig config) {
        if (random.nextFloat() > density) {
            return 0;
        } else {
            int i = Math.abs(localX) + Math.abs(localZ);
            float f = (float)MathHelper.clampedMap((double)i, 0.0, (double)config.maxDistanceFromCenterAffectingHeightBias, (double)height / 2.0, 0.0);
            return (int)clampedGaussian(random, 0.0F, (float)height, f, (float)config.heightDeviation);
        }
    }

    private boolean canWaterSpawn(StructureWorldAccess world, BlockPos pos) {
        BlockState blockState = world.getBlockState(pos);
        if (!blockState.isOf(Blocks.WATER) && !blockState.isOf(Blocks.DRIPSTONE_BLOCK) && !blockState.isOf(Blocks.POINTED_DRIPSTONE)) {
            if (world.getBlockState(pos.up()).getFluidState().isIn(FluidTags.WATER)) {
                return false;
            } else {
                Iterator var4 = Direction.Type.HORIZONTAL.iterator();

                Direction direction;
                do {
                    if (!var4.hasNext()) {
                        return this.isStoneOrWater(world, pos.down());
                    }

                    direction = (Direction)var4.next();
                } while(this.isStoneOrWater(world, pos.offset(direction)));

                return false;
            }
        } else {
            return false;
        }
    }

    private boolean isStoneOrWater(WorldAccess world, BlockPos pos) {
        BlockState blockState = world.getBlockState(pos);
        return blockState.isIn(BlockTags.BASE_STONE_OVERWORLD) || blockState.getFluidState().isIn(FluidTags.WATER);
    }

    private void placeAmberBlocks(StructureWorldAccess world, BlockPos pos, int height, Direction direction) {
        BlockPos.Mutable mutable = pos.mutableCopy();

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
        return (double)MathHelper.clampedMap((float)k, 0.0F, (float)config.maxDistanceFromCenterAffectingChanceOfAmberColumn, config.chanceOfAmberColumnAtMaxDistanceFromCenter, 1.0F);
    }

    private static float clampedGaussian(Random random, float min, float max, float mean, float deviation) {
        return ClampedNormalFloatProvider.get(random, mean, deviation, min, max);
    }
}
