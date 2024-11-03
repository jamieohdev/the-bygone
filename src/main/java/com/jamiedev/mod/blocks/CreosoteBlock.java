package com.jamiedev.mod.blocks;

import com.jamiedev.mod.init.JamiesModTag;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

public class CreosoteBlock extends PlantBlock {
    public static final MapCodec<CreosoteBlock> CODEC = createCodec(CreosoteBlock::new);
    protected static final float field_31080 = 6.0F;
    protected static final VoxelShape SHAPE = Block.createCuboidShape(2.0, 0.0, 2.0, 14.0, 13.0, 14.0);

    public MapCodec<CreosoteBlock> getCodec() {
        return CODEC;
    }

    public CreosoteBlock(AbstractBlock.Settings settings) {
        super(settings);
    }

    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        return floor.isIn(JamiesModTag.CREOSOTE_MAY_PLACE_ON);
    }
}
