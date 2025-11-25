package com.jamiedev.bygone.common.worldgen.feature;

import com.jamiedev.bygone.common.worldgen.feature.config.DiskShelfFungiConfig;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.stateproviders.RuleBasedBlockStateProvider;

public class DiskShelfFungiFeature extends Feature<DiskShelfFungiConfig> {
    public static final int WALL_THICKNESS = 1;

    public DiskShelfFungiFeature(Codec<DiskShelfFungiConfig> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<DiskShelfFungiConfig> context) {
        DiskShelfFungiConfig config = context.config();
        BlockPos blockpos = context.origin();
        WorldGenLevel worldgenlevel = context.level();
        RandomSource randomsource = context.random();
        boolean flag = false;
        int yPos = blockpos.getY();
        int top = yPos + config.halfHeight();
        int bottom = yPos - config.halfHeight() - 1;
        int radius = config.radius().sample(randomsource);
        int radiusSquared = radius * radius;
        float wallWidth = WALL_THICKNESS * Mth.sqrt(2);
        float wallRadiusSquared = (radius - wallWidth) * (radius - wallWidth);
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

        for (BlockPos pos : BlockPos.betweenClosed(
                blockpos.offset(-radius, 0, -radius),
                blockpos.offset(radius, 0, radius)
        )) {
            int xOffset = pos.getX() - blockpos.getX();
            int yOffset = pos.getZ() - blockpos.getZ();
            float rSquaredOffset = xOffset * xOffset + yOffset * yOffset;
            if (rSquaredOffset <= radiusSquared) {
                flag |= this.placeColumn(
                        config,
                        worldgenlevel,
                        randomsource,
                        top,
                        bottom,
                        mutablePos.set(pos),
                        rSquaredOffset >= wallRadiusSquared
                );
            }
        }

        return flag;
    }


    protected boolean placeColumn(DiskShelfFungiConfig config, WorldGenLevel level, RandomSource random, int maxY, int minY, BlockPos.MutableBlockPos mutablePos, boolean isWall) {

        boolean placedAnyBlocks = false;
        TagKey<Block> replaces = config.replaces();
        boolean placeInAir = config.placeInAir();
        RuleBasedBlockStateProvider outsideProvider = config.outsideStateProvider();
        RuleBasedBlockStateProvider insideProvider = config.insideStateProvider();

        if (isWall) {
            for (int j = maxY; j > minY; --j) {
                mutablePos.setY(j);

                BlockState state = level.getBlockState(mutablePos);

                if (state.is(replaces) || (placeInAir && state.isAir())) {
                    BlockState blockState = outsideProvider.getState(level, random, mutablePos);
                    level.setBlock(mutablePos, blockState, 2);
                    this.markAboveForPostProcessing(level, mutablePos);
                    placedAnyBlocks = true;
                } else {
                    break;
                }
            }
        } else {
            boolean shouldPlaceInterior = true;
            int topThickness = Math.min(WALL_THICKNESS, maxY - minY);
            for (int j = 0; j < topThickness; j++) {
                mutablePos.setY(maxY - j);

                BlockState state = level.getBlockState(mutablePos);
                if (state.is(replaces) || (placeInAir && state.isAir())) {
                    BlockState blockState = outsideProvider.getState(level, random, mutablePos);
                    level.setBlock(mutablePos, blockState, 2);
                    this.markAboveForPostProcessing(level, mutablePos);
                    placedAnyBlocks = true;
                } else {
                    shouldPlaceInterior = false;
                    break;
                }
            }
            if (shouldPlaceInterior) {
                mutablePos.move(Direction.DOWN);
                BlockState state = level.getBlockState(mutablePos);

                if (state.is(replaces) || (placeInAir && state.isAir())) {
                    BlockState blockState = insideProvider.getState(level, random, mutablePos);
                    level.setBlock(mutablePos, blockState, 2);
                    this.markAboveForPostProcessing(level, mutablePos);
                }
            }
        }


        return placedAnyBlocks;
    }
}
