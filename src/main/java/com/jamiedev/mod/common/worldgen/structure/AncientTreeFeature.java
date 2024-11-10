package com.jamiedev.mod.common.worldgen.structure;

import com.jamiedev.mod.common.init.JamiesModBlocks;
import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.Fluids;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.TreeFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;
import net.minecraft.world.gen.foliage.FoliagePlacer;
import net.minecraft.world.gen.foliage.FoliagePlacerType;

public class AncientTreeFeature  extends Feature<AncientTreeFeatureConfig> {
    public AncientTreeFeature(Codec<AncientTreeFeatureConfig> codec) {
        super(codec);
    }

    public boolean generate(FeatureContext<AncientTreeFeatureConfig> context) {
        StructureWorldAccess structureWorldAccess = context.getWorld();
        BlockPos blockPos = context.getOrigin();
        if (isNotSuitable(structureWorldAccess, blockPos)) {
            return false;
        } else {
            Random random = context.getRandom();
            AncientTreeFeatureConfig twistingVinesFeatureConfig = (AncientTreeFeatureConfig)context.getConfig();
            int i = twistingVinesFeatureConfig.spreadWidth();
            int j = twistingVinesFeatureConfig.spreadHeight();
            int k = twistingVinesFeatureConfig.maxHeight();
            BlockPos.Mutable mutable = new BlockPos.Mutable();

            for(int l = 0; l < i * i; ++l) {
                mutable.set(blockPos).move(MathHelper.nextInt(random, -i, i), MathHelper.nextInt(random, -j, j), MathHelper.nextInt(random, -i, i));
                if (canGenerate(structureWorldAccess, mutable) && !isNotSuitable(structureWorldAccess, mutable)) {
                    int m = MathHelper.nextInt(random, 1, k);
                    if (random.nextInt(6) == 0) {
                        m *= 2;
                    }

                    if (random.nextInt(5) == 0) {
                        m = 1;
                    }

                    generateVineColumn(structureWorldAccess, random, mutable, m, 17, 25);
                }
            }

            return true;
        }
    }

    private static boolean canGenerate(WorldAccess world, BlockPos.Mutable pos) {
        do {
            pos.move(0, -1, 0);
            if (world.isOutOfHeightLimit(pos)) {
                return false;
            }
        } while(world.getBlockState(pos).isAir());

        pos.move(0, 1, 0);
        return true;
    }

    public static void generateVineColumn(WorldAccess world, Random random, BlockPos.Mutable pos, int maxLength, int minAge, int maxAge) {
        for(int i = 1; i <= maxLength; ++i) {
            if (world.isAir(pos)) {
                if (i == maxLength || !world.isAir(pos.up())) {
                    world.setBlockState(pos, (BlockState)Blocks.JUNGLE_LOG.getDefaultState(), 2);
                    break;
                }

                world.setBlockState(pos, Blocks.JUNGLE_LOG.getDefaultState(), 2);
            }

            pos.move(Direction.UP);
        }

    }

    private static boolean isNotSuitable(WorldAccess world, BlockPos pos) {
        if (!world.isAir(pos)) {
            return true;
        } else {
            BlockState blockState = world.getBlockState(pos.down());
            return !blockState.isOf(JamiesModBlocks.LIMBOSLATE) && !blockState.isOf(JamiesModBlocks.LIMBOSTONE) && !blockState.isOf(Blocks.MOSS_BLOCK);
        }
    }

    protected FoliagePlacerType<?> getType() {
        return FoliagePlacerType.ACACIA_FOLIAGE_PLACER;
    }
    protected boolean isPositionInvalid(Random random, int dx, int y, int dz, int radius, boolean giantTrunk) {
        int i;
        int j;
        if (giantTrunk) {
            i = Math.min(Math.abs(dx), Math.abs(dx - 1));
            j = Math.min(Math.abs(dz), Math.abs(dz - 1));
        } else {
            i = Math.abs(dx);
            j = Math.abs(dz);
        }

        return this.isInvalidForLeaves(random, i, y, j, radius, giantTrunk);
    }
    private static boolean placeFoliageBlock(TestableWorld world, FoliagePlacer.BlockPlacer placer, Random random, TreeFeatureConfig config, float chance, BlockPos origin, BlockPos.Mutable pos) {
        if (pos.getManhattanDistance(origin) >= 7) {
            return false;
        } else {
            return random.nextFloat() > chance ? false : placeFoliageBlock(world, placer, random, config, pos);
        }
    }

    protected static boolean placeFoliageBlock(TestableWorld world, FoliagePlacer.BlockPlacer placer, Random random, TreeFeatureConfig config, BlockPos pos) {
        if (!TreeFeature.canReplace(world, pos)) {
            return false;
        } else {
            BlockState blockState = config.foliageProvider.get(random, pos);
            if (blockState.contains(Properties.WATERLOGGED)) {
                blockState = (BlockState)blockState.with(Properties.WATERLOGGED, world.testFluidState(pos, (fluidState) -> {
                    return fluidState.isEqualAndStill(Fluids.WATER);
                }));
            }

            placer.placeBlock(pos, blockState);
            return true;
        }
    }

    protected void generateSquare(TestableWorld world, FoliagePlacer.BlockPlacer placer, Random random, TreeFeatureConfig config, BlockPos centerPos, int radius, int y, boolean giantTrunk) {
        int i = giantTrunk ? 1 : 0;
        BlockPos.Mutable mutable = new BlockPos.Mutable();

        for(int j = -radius; j <= radius + i; ++j) {
            for(int k = -radius; k <= radius + i; ++k) {
                if (!this.isPositionInvalid(random, j, y, k, radius, giantTrunk)) {
                    mutable.set(centerPos, j, y, k);
                    placeFoliageBlock(world, placer, random, config, mutable);
                }
            }
        }

    }

    protected void generate(TestableWorld world, FoliagePlacer.BlockPlacer placer, Random random, TreeFeatureConfig config, int trunkHeight, FoliagePlacer.TreeNode treeNode, int foliageHeight, int radius, int offset) {
        boolean bl = treeNode.isGiantTrunk();
        BlockPos blockPos = treeNode.getCenter().up(offset);
        this.generateSquare(world, placer, random, config, blockPos, radius + treeNode.getFoliageRadius(), -1 - foliageHeight, bl);
        this.generateSquare(world, placer, random, config, blockPos, radius - 1, -foliageHeight, bl);
        this.generateSquare(world, placer, random, config, blockPos, radius + treeNode.getFoliageRadius() - 1, 0, bl);
    }

    public int getRandomHeight(Random random, int trunkHeight, TreeFeatureConfig config) {
        return 0;
    }

    protected boolean isInvalidForLeaves(Random random, int dx, int y, int dz, int radius, boolean giantTrunk) {
        if (y == 0) {
            return (dx > 1 || dz > 1) && dx != 0 && dz != 0;
        } else {
            return dx == radius && dz == radius && radius > 0;
        }
    }
}
