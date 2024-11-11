package com.jamiedev.mod.common.worldgen.feature;

import com.jamiedev.mod.fabric.init.JamiesModTag;
import com.mojang.serialization.Codec;
import net.minecraft.block.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

import java.util.Optional;

public abstract class PrimordialCoralFeature extends Feature<DefaultFeatureConfig> {
    public PrimordialCoralFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        Random random = context.getRandom();
        StructureWorldAccess structureWorldAccess = context.getWorld();
        BlockPos blockPos = context.getOrigin();
        Optional<Block> optional = Registries.BLOCK.getRandomEntry(JamiesModTag.CORAL_BLOCKS, random).map(RegistryEntry::value);
        return optional.filter(block -> this.generateCoral(structureWorldAccess, random, blockPos, ((Block) block).getDefaultState())).isPresent();
    }

    protected abstract boolean generateCoral(WorldAccess world, Random random, BlockPos pos, BlockState state);

    protected boolean generateCoralPiece(WorldAccess world, Random random, BlockPos pos, BlockState state) {
        BlockPos blockPos = pos.up();
        BlockState blockState = world.getBlockState(pos);
        if ((blockState.isOf(Blocks.WATER) || blockState.isIn(JamiesModTag.CORALS)) && world.getBlockState(blockPos).isOf(Blocks.WATER)) {
            world.setBlockState(pos, state, 3);
            if (random.nextFloat() < 0.25F) {
                Registries.BLOCK.getRandomEntry(JamiesModTag.CORALS, random).map(RegistryEntry::value).ifPresent((block) -> {
                    world.setBlockState(blockPos, block.getDefaultState(), 2);
                });
            } else if (random.nextFloat() < 0.05F) {
                world.setBlockState(blockPos, (BlockState)Blocks.SEA_PICKLE.getDefaultState().with(SeaPickleBlock.PICKLES, random.nextInt(4) + 1), 2);
            }

            for (Direction direction : Direction.Type.HORIZONTAL) {
                if (random.nextFloat() < 0.2F) {
                    BlockPos blockPos2 = pos.offset(direction);
                    if (world.getBlockState(blockPos2).isOf(Blocks.WATER)) {
                        Registries.BLOCK.getRandomEntry(JamiesModTag.WALL_CORALS, random).map(RegistryEntry::value).ifPresent((block) -> {
                            BlockState blockState2 = block.getDefaultState();
                            if (blockState2.contains(DeadCoralWallFanBlock.FACING)) {
                                blockState2 = (BlockState) blockState2.with(DeadCoralWallFanBlock.FACING, direction);
                            }

                            world.setBlockState(blockPos2, blockState2, 2);
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
