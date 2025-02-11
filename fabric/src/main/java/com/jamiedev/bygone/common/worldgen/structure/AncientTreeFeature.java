package com.jamiedev.bygone.common.worldgen.structure;

import com.jamiedev.bygone.fabric.init.JamiesModBlocks;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraft.world.level.material.Fluids;

public class AncientTreeFeature  extends Feature<AncientTreeFeatureConfig> {
    public AncientTreeFeature(Codec<AncientTreeFeatureConfig> codec) {
        super(codec);
    }

    public boolean place(FeaturePlaceContext<AncientTreeFeatureConfig> context) {
        WorldGenLevel structureWorldAccess = context.level();
        BlockPos blockPos = context.origin();
        if (isNotSuitable(structureWorldAccess, blockPos)) {
            return false;
        } else {
            RandomSource random = context.random();
            AncientTreeFeatureConfig twistingVinesFeatureConfig = (AncientTreeFeatureConfig)context.config();
            int i = twistingVinesFeatureConfig.spreadWidth();
            int j = twistingVinesFeatureConfig.spreadHeight();
            int k = twistingVinesFeatureConfig.maxHeight();
            BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

            for(int l = 0; l < i * i; ++l) {
                mutable.set(blockPos).move(Mth.nextInt(random, -i, i), Mth.nextInt(random, -j, j), Mth.nextInt(random, -i, i));
                if (canGenerate(structureWorldAccess, mutable) && !isNotSuitable(structureWorldAccess, mutable)) {
                    int m = Mth.nextInt(random, 1, k);
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

    private static boolean canGenerate(LevelAccessor world, BlockPos.MutableBlockPos pos) {
        do {
            pos.move(0, -1, 0);
            if (world.isOutsideBuildHeight(pos)) {
                return false;
            }
        } while(world.getBlockState(pos).isAir());

        pos.move(0, 1, 0);
        return true;
    }

    public static void generateVineColumn(LevelAccessor world, RandomSource random, BlockPos.MutableBlockPos pos, int maxLength, int minAge, int maxAge) {
        for(int i = 1; i <= maxLength; ++i) {
            if (world.isEmptyBlock(pos)) {
                if (i == maxLength || !world.isEmptyBlock(pos.above())) {
                    world.setBlock(pos, (BlockState)Blocks.JUNGLE_LOG.defaultBlockState(), 2);
                    break;
                }

                world.setBlock(pos, Blocks.JUNGLE_LOG.defaultBlockState(), 2);
            }

            pos.move(Direction.UP);
        }

    }

    private static boolean isNotSuitable(LevelAccessor world, BlockPos pos) {
        if (!world.isEmptyBlock(pos)) {
            return true;
        } else {
            BlockState blockState = world.getBlockState(pos.below());
            return !blockState.is(JamiesModBlocks.LIMBOSLATE) && !blockState.is(JamiesModBlocks.LIMBOSTONE) && !blockState.is(Blocks.MOSS_BLOCK);
        }
    }

    protected FoliagePlacerType<?> getType() {
        return FoliagePlacerType.ACACIA_FOLIAGE_PLACER;
    }
    protected boolean isPositionInvalid(RandomSource random, int dx, int y, int dz, int radius, boolean giantTrunk) {
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
    private static boolean placeFoliageBlock(LevelSimulatedReader world, FoliagePlacer.FoliageSetter placer, RandomSource random, TreeConfiguration config, float chance, BlockPos origin, BlockPos.MutableBlockPos pos) {
        if (pos.distManhattan(origin) >= 7) {
            return false;
        } else {
            return random.nextFloat() > chance ? false : placeFoliageBlock(world, placer, random, config, pos);
        }
    }

    protected static boolean placeFoliageBlock(LevelSimulatedReader world, FoliagePlacer.FoliageSetter placer, RandomSource random, TreeConfiguration config, BlockPos pos) {
        if (!TreeFeature.validTreePos(world, pos)) {
            return false;
        } else {
            BlockState blockState = config.foliageProvider.getState(random, pos);
            if (blockState.hasProperty(BlockStateProperties.WATERLOGGED)) {
                blockState = (BlockState)blockState.setValue(BlockStateProperties.WATERLOGGED, world.isFluidAtPosition(pos, (fluidState) -> {
                    return fluidState.isSourceOfType(Fluids.WATER);
                }));
            }

            placer.set(pos, blockState);
            return true;
        }
    }

    protected void generateSquare(LevelSimulatedReader world, FoliagePlacer.FoliageSetter placer, RandomSource random, TreeConfiguration config, BlockPos centerPos, int radius, int y, boolean giantTrunk) {
        int i = giantTrunk ? 1 : 0;
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

        for(int j = -radius; j <= radius + i; ++j) {
            for(int k = -radius; k <= radius + i; ++k) {
                if (!this.isPositionInvalid(random, j, y, k, radius, giantTrunk)) {
                    mutable.setWithOffset(centerPos, j, y, k);
                    placeFoliageBlock(world, placer, random, config, mutable);
                }
            }
        }

    }

    protected void generate(LevelSimulatedReader world, FoliagePlacer.FoliageSetter placer, RandomSource random, TreeConfiguration config, int trunkHeight, FoliagePlacer.FoliageAttachment treeNode, int foliageHeight, int radius, int offset) {
        boolean bl = treeNode.doubleTrunk();
        BlockPos blockPos = treeNode.pos().above(offset);
        this.generateSquare(world, placer, random, config, blockPos, radius + treeNode.radiusOffset(), -1 - foliageHeight, bl);
        this.generateSquare(world, placer, random, config, blockPos, radius - 1, -foliageHeight, bl);
        this.generateSquare(world, placer, random, config, blockPos, radius + treeNode.radiusOffset() - 1, 0, bl);
    }

    public int getRandomHeight(RandomSource random, int trunkHeight, TreeConfiguration config) {
        return 0;
    }

    protected boolean isInvalidForLeaves(RandomSource random, int dx, int y, int dz, int radius, boolean giantTrunk) {
        if (y == 0) {
            return (dx > 1 || dz > 1) && dx != 0 && dz != 0;
        } else {
            return dx == radius && dz == radius && radius > 0;
        }
    }
}
