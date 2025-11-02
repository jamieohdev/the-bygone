package com.jamiedev.bygone.common.block;

import com.google.common.base.Predicates;
import com.jamiedev.bygone.core.registry.BGBlocks;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BygonePortalFramePlaceableBlock extends Block {
    public static final DirectionProperty FACING;
    public static final MapCodec<BygonePortalFramePlaceableBlock> CODEC = simpleCodec(BygonePortalFramePlaceableBlock::new);
    protected static final VoxelShape FRAME_SHAPE;

    private static BlockPattern COMPLETED_FRAME;

    static {
        FACING = HorizontalDirectionalBlock.FACING;
        FRAME_SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 13.0, 16.0);
    }

    public BygonePortalFramePlaceableBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    public static BlockPattern getCompletedFramePattern() {
        if (COMPLETED_FRAME == null) {
            COMPLETED_FRAME = BlockPatternBuilder.start().aisle("?vvv?", ">???<", ">???<", ">???<", "?^^^?").where('?',
                            BlockInWorld.hasState(BlockStatePredicate.ANY))
                    .where('^', BlockInWorld.hasState(BlockStatePredicate.forBlock(BGBlocks.BYGONE_PORTAL_FRAME.get()).where(FACING, Predicates.equalTo(Direction.SOUTH))))
                    .where('>', BlockInWorld.hasState(BlockStatePredicate.forBlock(BGBlocks.BYGONE_PORTAL_FRAME.get()).where(FACING, Predicates.equalTo(Direction.WEST))))
                    .where('v', BlockInWorld.hasState(BlockStatePredicate.forBlock(BGBlocks.BYGONE_PORTAL_FRAME.get()).where(FACING, Predicates.equalTo(Direction.NORTH))))
                    .where('<', BlockInWorld.hasState(BlockStatePredicate.forBlock(BGBlocks.BYGONE_PORTAL_FRAME.get()).where(FACING, Predicates.equalTo(Direction.EAST)))).build();
        }

        return COMPLETED_FRAME;
    }

    @Override
    public MapCodec<BygonePortalFramePlaceableBlock> codec() {
        return CODEC;
    }

    @Override
    protected boolean useShapeForLightOcclusion(BlockState state) {
        return true;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return FRAME_SHAPE;
    }

    @Override
    protected boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    protected int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
        return 0;
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }
}
