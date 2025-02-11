package com.jamiedev.mod.common.blocks.shelf;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class ShelfFungiWallFanBlock extends ShelfFungiFanBlock
{
    public static final MapCodec<ShelfFungiWallFanBlock> CODEC = simpleCodec(ShelfFungiWallFanBlock::new);
    public static final DirectionProperty FACING;
    private static final Map<Direction, VoxelShape> FACING_TO_SHAPE;

    public MapCodec<? extends ShelfFungiWallFanBlock> codec() {
        return CODEC;
    }

    public ShelfFungiWallFanBlock(BlockBehaviour.Properties settings) {
        super(settings);
        this.registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(FACING, Direction.NORTH)));
    }

    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return (VoxelShape)FACING_TO_SHAPE.get(state.getValue(FACING));
    }

    protected BlockState rotate(BlockState state, Rotation rotation) {
        return (BlockState)state.setValue(FACING, rotation.rotate((Direction)state.getValue(FACING)));
    }

    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation((Direction)state.getValue(FACING)));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{FACING});
    }

    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
        return direction.getOpposite() == state.getValue(FACING) && !state.canSurvive(world, pos) ? Blocks.AIR.defaultBlockState() : state;
    }

    protected boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        Direction direction = (Direction)state.getValue(FACING);
        BlockPos blockPos = pos.relative(direction.getOpposite());
        BlockState blockState = world.getBlockState(blockPos);
        return blockState.isFaceSturdy(world, blockPos, direction);
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockState blockState = super.getStateForPlacement(ctx);
        LevelReader worldView = ctx.getLevel();
        BlockPos blockPos = ctx.getClickedPos();
        Direction[] directions = ctx.getNearestLookingDirections();
        Direction[] var6 = directions;
        int var7 = directions.length;

        for(int var8 = 0; var8 < var7; ++var8) {
            Direction direction = var6[var8];
            if (direction.getAxis().isHorizontal()) {
                blockState = (BlockState)blockState.setValue(FACING, direction.getOpposite());
                if (blockState.canSurvive(worldView, blockPos)) {
                    return blockState;
                }
            }
        }

        return null;
    }

    public static boolean shouldConnectTo(BlockGetter world, BlockPos pos, Direction direction) {
        return MultifaceBlock.canAttachTo(world, direction, pos, world.getBlockState(pos));
    }


    static {
        FACING = HorizontalDirectionalBlock.FACING;
        FACING_TO_SHAPE = Maps.newEnumMap(ImmutableMap.of(
                Direction.NORTH, Block.box(0.0, 4.0, 5.0, 16.0, 12.0, 16.0),
                Direction.SOUTH, Block.box(0.0, 4.0, 0.0, 16.0, 12.0, 11.0),
                Direction.WEST, Block.box(5.0, 4.0, 0.0, 16.0, 12.0, 16.0),
                Direction.EAST, Block.box(0.0, 4.0, 0.0, 11.0, 12.0, 16.0)));
    }
}
