package com.jamiedev.mod.common.blocks.shelf;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

public class ShelfFungiFanBlock  extends Block
{
    public static final MapCodec<ShelfFungiFanBlock> CODEC = createCodec(ShelfFungiFanBlock::new);
    private static final VoxelShape SHAPE = Block.createCuboidShape(2.0, 0.0, 2.0, 14.0, 4.0, 14.0);

    public MapCodec<? extends ShelfFungiFanBlock> getCodec() {
        return CODEC;
    }

    public ShelfFungiFanBlock(AbstractBlock.Settings settings) {
        super(settings);
    }

    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    public static boolean shouldConnectTo(BlockView world, BlockPos pos, Direction direction) {
        return MultifaceGrowthBlock.canGrowOn(world, direction, pos, world.getBlockState(pos));
    }

}
