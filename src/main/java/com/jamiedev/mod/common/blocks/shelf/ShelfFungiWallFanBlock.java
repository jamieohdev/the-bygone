package com.jamiedev.mod.common.blocks.shelf;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Property;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class ShelfFungiWallFanBlock extends ShelfFungiFanBlock
{
    public static final MapCodec<ShelfFungiWallFanBlock> CODEC = createCodec(ShelfFungiWallFanBlock::new);
    public static final DirectionProperty FACING;
    private static final Map<Direction, VoxelShape> FACING_TO_SHAPE;

    public MapCodec<? extends ShelfFungiWallFanBlock> getCodec() {
        return CODEC;
    }

    public ShelfFungiWallFanBlock(AbstractBlock.Settings settings) {
        super(settings);
        this.setDefaultState((BlockState)((BlockState)((BlockState)this.stateManager.getDefaultState()).with(FACING, Direction.NORTH)));
    }

    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return (VoxelShape)FACING_TO_SHAPE.get(state.get(FACING));
    }

    protected BlockState rotate(BlockState state, BlockRotation rotation) {
        return (BlockState)state.with(FACING, rotation.rotate((Direction)state.get(FACING)));
    }

    protected BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation((Direction)state.get(FACING)));
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{FACING});
    }

    protected BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        return direction.getOpposite() == state.get(FACING) && !state.canPlaceAt(world, pos) ? Blocks.AIR.getDefaultState() : state;
    }

    protected boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        Direction direction = (Direction)state.get(FACING);
        BlockPos blockPos = pos.offset(direction.getOpposite());
        BlockState blockState = world.getBlockState(blockPos);
        return blockState.isSideSolidFullSquare(world, blockPos, direction);
    }

    @Nullable
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState blockState = super.getPlacementState(ctx);
        WorldView worldView = ctx.getWorld();
        BlockPos blockPos = ctx.getBlockPos();
        Direction[] directions = ctx.getPlacementDirections();
        Direction[] var6 = directions;
        int var7 = directions.length;

        for(int var8 = 0; var8 < var7; ++var8) {
            Direction direction = var6[var8];
            if (direction.getAxis().isHorizontal()) {
                blockState = (BlockState)blockState.with(FACING, direction.getOpposite());
                if (blockState.canPlaceAt(worldView, blockPos)) {
                    return blockState;
                }
            }
        }

        return null;
    }

    public static boolean shouldConnectTo(BlockView world, BlockPos pos, Direction direction) {
        return MultifaceGrowthBlock.canGrowOn(world, direction, pos, world.getBlockState(pos));
    }


    static {
        FACING = HorizontalFacingBlock.FACING;
        FACING_TO_SHAPE = Maps.newEnumMap(ImmutableMap.of(
                Direction.NORTH, Block.createCuboidShape(0.0, 4.0, 5.0, 16.0, 12.0, 16.0),
                Direction.SOUTH, Block.createCuboidShape(0.0, 4.0, 0.0, 16.0, 12.0, 11.0),
                Direction.WEST, Block.createCuboidShape(5.0, 4.0, 0.0, 16.0, 12.0, 16.0),
                Direction.EAST, Block.createCuboidShape(0.0, 4.0, 0.0, 11.0, 12.0, 16.0)));
    }
}
