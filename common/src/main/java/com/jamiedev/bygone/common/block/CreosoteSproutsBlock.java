package com.jamiedev.bygone.common.block;

import com.jamiedev.bygone.core.init.JamiesModTag;
import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CreosoteSproutsBlock  extends BushBlock {
    public static final MapCodec<CreosoteSproutsBlock> CODEC = simpleCodec(CreosoteSproutsBlock::new);
    protected static final VoxelShape SHAPE = Block.box(2.0, 0.0, 2.0, 14.0, 3.0, 14.0);

    @Override
    public MapCodec<CreosoteSproutsBlock> codec() {
        return CODEC;
    }

    public CreosoteSproutsBlock(BlockBehaviour.Properties settings) {
        super(settings);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected boolean mayPlaceOn(BlockState floor, BlockGetter world, BlockPos pos) {
        return floor.is(JamiesModTag.CREOSOTE_MAY_PLACE_ON) || floor.is(BlockTags.NYLIUM) ||super.mayPlaceOn(floor, world, pos);
    }
}
