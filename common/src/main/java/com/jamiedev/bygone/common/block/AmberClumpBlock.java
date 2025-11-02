package com.jamiedev.bygone.common.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

import java.util.function.ToIntFunction;

public class AmberClumpBlock extends MultifaceBlock implements SimpleWaterloggedBlock {

    private static final BooleanProperty WATERLOGGED;
    public static final MapCodec<AmberClumpBlock> CODEC = simpleCodec(AmberClumpBlock::new);

    static {
        WATERLOGGED = BlockStateProperties.WATERLOGGED;
    }

    private final MultifaceSpreader spreader = new MultifaceSpreader(this);
    VineBlock ref;

    public AmberClumpBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(WATERLOGGED, false));
    }

    public static ToIntFunction<BlockState> emission(int light) {
        return (p_181221_) -> MultifaceBlock.hasAnyFace(p_181221_) ? light : 0;
    }

    public MapCodec<AmberClumpBlock> codec() {
        return CODEC;
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(WATERLOGGED);
    }

    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    protected boolean canBeReplaced(BlockState state, BlockPlaceContext useContext) {
        return !useContext.getItemInHand().is(Items.GLOW_LICHEN) || super.canBeReplaced(state, useContext);
    }

    protected FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    protected boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos) {
        return state.getFluidState().isEmpty();
    }

    public MultifaceSpreader getSpreader() {
        return this.spreader;
    }
}
