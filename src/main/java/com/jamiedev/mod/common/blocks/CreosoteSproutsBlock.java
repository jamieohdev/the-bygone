package com.jamiedev.mod.common.blocks;

import com.jamiedev.mod.common.init.JamiesModTag;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

public class CreosoteSproutsBlock  extends PlantBlock {
    public static final MapCodec<CreosoteSproutsBlock> CODEC = createCodec(CreosoteSproutsBlock::new);
    protected static final VoxelShape SHAPE = Block.createCuboidShape(2.0, 0.0, 2.0, 14.0, 3.0, 14.0);

    public MapCodec<CreosoteSproutsBlock> getCodec() {
        return CODEC;
    }

    public CreosoteSproutsBlock(AbstractBlock.Settings settings) {
        super(settings);
    }

    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        return floor.isIn(JamiesModTag.CREOSOTE_MAY_PLACE_ON) || super.canPlantOnTop(floor, world, pos);
    }
}
