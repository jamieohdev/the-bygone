package com.jamiedev.bygone.common.block.shelf;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class ShelfFungiWallFanBlock extends ShelfFungiFanBlock {
    public static final DirectionProperty FACING;
    public static final MapCodec<ShelfFungiWallFanBlock> CODEC = simpleCodec(ShelfFungiWallFanBlock::new);
    private static final Map<Direction, VoxelShape> FACING_TO_SHAPE;

    static {
        FACING = HorizontalDirectionalBlock.FACING;
        FACING_TO_SHAPE = Maps.newEnumMap(ImmutableMap.of(
                Direction.NORTH, box(0.0, 4.0, 5.0, 16.0, 12.0, 16.0),
                Direction.SOUTH, box(0.0, 4.0, 0.0, 16.0, 12.0, 11.0),
                Direction.WEST, box(5.0, 4.0, 0.0, 16.0, 12.0, 16.0),
                Direction.EAST, box(0.0, 4.0, 0.0, 11.0, 12.0, 16.0)));
    }

    public ShelfFungiWallFanBlock(BlockBehaviour.Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    public static boolean shouldConnectTo(BlockGetter world, BlockPos pos, Direction direction) {
        return MultifaceBlock.canAttachTo(world, direction, pos, world.getBlockState(pos));
    }

    @Override
    public MapCodec<? extends ShelfFungiWallFanBlock> codec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return FACING_TO_SHAPE.get(state.getValue(FACING));
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
    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
        return direction.getOpposite() == state.getValue(FACING) && !state.canSurvive(world, pos) ? Blocks.AIR.defaultBlockState() : state;
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        Direction direction = state.getValue(FACING);
        BlockPos blockPos = pos.relative(direction.getOpposite());
        BlockState blockState = world.getBlockState(blockPos);
        return blockState.isFaceSturdy(world, blockPos, direction);
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockState blockState = super.getStateForPlacement(ctx);
        LevelReader worldView = ctx.getLevel();
        BlockPos blockPos = ctx.getClickedPos();
        Direction[] directions = ctx.getNearestLookingDirections();
        Direction[] var6 = directions;
        int var7 = directions.length;

        for (int var8 = 0; var8 < var7; ++var8) {
            Direction direction = var6[var8];
            if (direction.getAxis().isHorizontal()) {
                blockState = blockState.setValue(FACING, direction.getOpposite());
                if (blockState.canSurvive(worldView, blockPos)) {
                    return blockState;
                }
            }
        }

        return null;
    }
}
