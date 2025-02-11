package com.jamiedev.bygone.common.blocks.shelf;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ShelfFungiFanBlock  extends Block
{
    public static final MapCodec<ShelfFungiFanBlock> CODEC = simpleCodec(ShelfFungiFanBlock::new);
    private static final VoxelShape SHAPE = Block.box(2.0, 0.0, 2.0, 14.0, 4.0, 14.0);

    public MapCodec<? extends ShelfFungiFanBlock> codec() {
        return CODEC;
    }

    public ShelfFungiFanBlock(BlockBehaviour.Properties settings) {
        super(settings);
    }

    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    public static boolean shouldConnectTo(BlockGetter world, BlockPos pos, Direction direction) {
        return MultifaceBlock.canAttachTo(world, direction, pos, world.getBlockState(pos));
    }

}
