package com.jamiedev.bygone.common.worldgen.feature;

import com.jamiedev.bygone.core.init.JamiesModTag;
import com.jamiedev.bygone.core.registry.BGBlocks;
import com.mojang.serialization.Codec;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.BaseCoralWallFanBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class BycoralFeature extends Feature<BlockStateConfiguration> {
    public BycoralFeature(Codec<BlockStateConfiguration> codec) {
        super(codec);
    }


    @SuppressWarnings("SameParameterValue")
    protected @Nullable BlockPos findFloorInColumn(@NotNull LevelAccessor level, @NotNull BlockPos startingPos, int maxDistance, @NotNull TagKey<Block> passable, @NotNull TagKey<Block> invalidSurface) {

        BlockState startingState = level.getBlockState(startingPos);
        BlockPos.MutableBlockPos mutableBlockPos = startingPos.mutable();

        int distanceMoved = 1;
        boolean foundSurface = true;

        Direction toCheckIn = startingState.is(passable) ? Direction.DOWN : Direction.UP;
        mutableBlockPos.move(toCheckIn);
        while (level.getBlockState(mutableBlockPos).is(passable)) {

            mutableBlockPos.move(toCheckIn);
            distanceMoved++;
            if (distanceMoved > maxDistance) {
                foundSurface = false;
                break;
            }
        }
        boolean isValidSurface = !level.getBlockState(mutableBlockPos).is(invalidSurface);
        mutableBlockPos.move(toCheckIn.getOpposite());


        return foundSurface && isValidSurface ? mutableBlockPos.immutable() : null;
    }

    @Override
    public boolean place(@NotNull FeaturePlaceContext<BlockStateConfiguration> context) {
        RandomSource randomsource = context.random();
        WorldGenLevel worldgenlevel = context.level();
        BlockPos blockpos = context.origin();
        BlockState state = context.config().state;
        return this.placeFeature(
                worldgenlevel,
                randomsource,
                blockpos,
                state
        );
    }


    protected abstract boolean placeFeature(@NotNull LevelAccessor level, @NotNull RandomSource random, @NotNull BlockPos pos, @NotNull BlockState state);

    protected boolean placeBycoralBlock(@NotNull LevelAccessor level, @NotNull RandomSource random, BlockPos pos, @NotNull BlockState state) {
        return this.placeBycoralBlock(level, random, pos, state, true);
    }

    protected boolean placeBycoralBlock(@NotNull LevelAccessor level, @NotNull RandomSource random, @NotNull BlockPos pos, @NotNull BlockState state, boolean allowDecoratives) {
        BlockPos blockPos = pos.above();
        BlockState blockstate = level.getBlockState(pos);
        if ((blockstate.is(Blocks.WATER) || blockstate.is(JamiesModTag.CORAL_BLOCK_REPLACEABLE)) && level.getBlockState(
                        blockPos)
                .is(Blocks.WATER)) {
            level.setBlock(pos, state, 3);
            if (allowDecoratives) {
                if (random.nextFloat() < 0.25F) {
                    BuiltInRegistries.BLOCK
                            .getRandomElementOf(JamiesModTag.CORALS, random)
                            .map(Holder::value)
                            .ifPresent(coralPlantBlock -> level.setBlock(
                                    blockPos,
                                    coralPlantBlock.defaultBlockState(),
                                    2
                            ));
                } else if (random.nextFloat() < 0.05F) {
                    BlockState toPlace = Util.getRandom(
                            List.of(
                                    BGBlocks.CRINOID.get().defaultBlockState(),
                                    BGBlocks.PRIMORDIAL_URCHIN.get()
                                            .defaultBlockState()
                                            .trySetValue(BlockStateProperties.WATERLOGGED, true),
                                    BGBlocks.BREATH_POD.get().defaultBlockState()
                            ), random
                    );
                    level.setBlock(
                            blockPos, toPlace, 2
                    );
                }

                for (Direction direction : Direction.Plane.HORIZONTAL) {
                    if (random.nextFloat() < 0.2F) {
                        BlockPos relativeBlock = pos.relative(direction);
                        if (level.getBlockState(relativeBlock).is(Blocks.WATER)) {
                            BuiltInRegistries.BLOCK.getRandomElementOf(JamiesModTag.WALL_CORALS, random)
                                    .map(Holder::value)
                                    .ifPresent(block -> {
                                        BlockState blockstate1 = block.defaultBlockState();
                                        if (blockstate1.hasProperty(BaseCoralWallFanBlock.FACING)) {
                                            blockstate1 = blockstate1.setValue(BaseCoralWallFanBlock.FACING, direction);
                                        }

                                        level.setBlock(relativeBlock, blockstate1, 2);
                                    });
                        }
                    }
                }
            }


            return true;
        } else {
            return false;
        }
    }


}
