package com.jamiedev.bygone.common.blocks.gourds;

import com.jamiedev.bygone.fabric.init.JamiesModBlocks;
import com.mojang.serialization.MapCodec;

import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.NetherVines;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;

public class GourdVineBlock extends GrowingPlantHeadBlock {
    public static final MapCodec<GourdVineBlock> CODEC = simpleCodec(GourdVineBlock::new);
    protected static final VoxelShape SHAPE = Block.box(4.0, 0.0, 4.0, 12.0, 16.0, 12.0);

    @Override
    public MapCodec<GourdVineBlock> codec() {
        return CODEC;
    }

    public GourdVineBlock(BlockBehaviour.Properties settings) {
        super(settings, Direction.DOWN, SHAPE, false, 0.1);
    }

    @Override
    protected int getBlocksToGrowWhenBonemealed(RandomSource random) {
        return NetherVines.getBlocksToGrowWhenBonemealed(random);
    }

    @Override
    protected Block getBodyBlock() {
        return JamiesModBlocks.GOURD_VINE;
    }

    @Override
    protected boolean canGrowInto(BlockState state) {
        return NetherVines.isValidGrowthState(state);
    }
}
