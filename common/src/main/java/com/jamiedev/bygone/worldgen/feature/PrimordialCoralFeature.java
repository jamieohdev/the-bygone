package com.jamiedev.bygone.worldgen.feature;

import com.jamiedev.bygone.init.JamiesModTag;
import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.BaseCoralWallFanBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SeaPickleBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import java.util.Optional;

public abstract class PrimordialCoralFeature extends Feature<NoneFeatureConfiguration> {
    public PrimordialCoralFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        RandomSource random = context.random();
        WorldGenLevel structureWorldAccess = context.level();
        BlockPos blockPos = context.origin();
        Optional<Block> optional = BuiltInRegistries.BLOCK.getRandomElementOf(JamiesModTag.CORAL_BLOCKS, random).map(Holder::value);
        return optional.filter(block -> this.generateCoral(structureWorldAccess, random, blockPos, block.defaultBlockState())).isPresent();
    }

    protected abstract boolean generateCoral(LevelAccessor world, RandomSource random, BlockPos pos, BlockState state);

    protected boolean generateCoralPiece(LevelAccessor world, RandomSource random, BlockPos pos, BlockState state) {
        BlockPos blockPos = pos.above();
        BlockState blockState = world.getBlockState(pos);
        if ((blockState.is(Blocks.WATER) || blockState.is(JamiesModTag.CORALS)) && world.getBlockState(blockPos).is(Blocks.WATER)) {
            world.setBlock(pos, state, 3);
            if (random.nextFloat() < 0.25F) {
                BuiltInRegistries.BLOCK.getRandomElementOf(JamiesModTag.CORALS, random).map(Holder::value).ifPresent((block) -> {
                    world.setBlock(blockPos, block.defaultBlockState(), 2);
                });
            } else if (random.nextFloat() < 0.05F) {
                world.setBlock(blockPos, Blocks.SEA_PICKLE.defaultBlockState().setValue(SeaPickleBlock.PICKLES, random.nextInt(4) + 1), 2);
            }

            for (Direction direction : Direction.Plane.HORIZONTAL) {
                if (random.nextFloat() < 0.2F) {
                    BlockPos blockPos2 = pos.relative(direction);
                    if (world.getBlockState(blockPos2).is(Blocks.WATER)) {
                        BuiltInRegistries.BLOCK.getRandomElementOf(JamiesModTag.WALL_CORALS, random).map(Holder::value).ifPresent((block) -> {
                            BlockState blockState2 = block.defaultBlockState();
                            if (blockState2.hasProperty(BaseCoralWallFanBlock.FACING)) {
                                blockState2 = blockState2.setValue(BaseCoralWallFanBlock.FACING, direction);
                            }

                            world.setBlock(blockPos2, blockState2, 2);
                        });
                    }
                }
            }

            return true;
        } else {
            return false;
        }
    }
}
