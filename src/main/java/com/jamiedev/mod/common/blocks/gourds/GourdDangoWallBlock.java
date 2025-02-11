package com.jamiedev.mod.common.blocks.gourds;

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

public class GourdDangoWallBlock extends GourdDangoBlock {

    public static MapCodec<GourdDangoWallBlock> CODEC;
    public static final DirectionProperty FACING;
    protected static final float field_31285 = 2.5F;
    private static final Map<Direction, VoxelShape> BOUNDING_SHAPES;

    public MapCodec<GourdDangoWallBlock> codec() {
        return CODEC;
    }

    public GourdDangoWallBlock(BlockBehaviour.Properties settings) {
        super(settings);
        this.registerDefaultState((BlockState) ((BlockState) this.stateDefinition.any()).setValue(FACING, Direction.NORTH));
    }

    public String getDescriptionId() {
        return this.asItem().getDescriptionId();
    }

    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return getBoundingShape(state);
    }

    public static VoxelShape getBoundingShape(BlockState state) {
        return (VoxelShape) BOUNDING_SHAPES.get(state.getValue(FACING));
    }

    protected boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        return canPlaceAt(world, pos, (Direction) state.getValue(FACING));
    }

    public static boolean canPlaceAt(LevelReader world, BlockPos pos, Direction facing) {
        BlockPos blockPos = pos.relative(facing.getOpposite());
        BlockState blockState = world.getBlockState(blockPos);
        return blockState.isFaceSturdy(world, blockPos, facing);
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockState blockState = this.defaultBlockState();
        LevelReader worldView = ctx.getLevel();
        BlockPos blockPos = ctx.getClickedPos();
        Direction[] directions = ctx.getNearestLookingDirections();
        Direction[] var6 = directions;
        int var7 = directions.length;

        for (int var8 = 0; var8 < var7; ++var8) {
            Direction direction = var6[var8];
            if (direction.getAxis().isHorizontal()) {
                Direction direction2 = direction.getOpposite();
                blockState = (BlockState) blockState.setValue(FACING, direction2);
                if (blockState.canSurvive(worldView, blockPos)) {
                    return blockState;
                }
            }
        }

        return null;
    }

    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
        return direction.getOpposite() == state.getValue(FACING) && !state.canSurvive(world, pos) ? Blocks.AIR.defaultBlockState() : state;
    }


    protected BlockState rotate(BlockState state, Rotation rotation) {
        return (BlockState) state.setValue(FACING, rotation.rotate((Direction) state.getValue(FACING)));
    }

    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation((Direction) state.getValue(FACING)));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{FACING});
    }

    static {
        FACING = HorizontalDirectionalBlock.FACING;
        BOUNDING_SHAPES = Maps.newEnumMap(ImmutableMap.of(Direction.NORTH, Block.box(5.5, 3.0, 11.0, 10.5, 13.0, 16.0), Direction.SOUTH, Block.box(5.5, 3.0, 0.0, 10.5, 13.0, 5.0), Direction.WEST, Block.box(11.0, 3.0, 5.5, 16.0, 13.0, 10.5), Direction.EAST, Block.box(0.0, 3.0, 5.5, 5.0, 13.0, 10.5)));
    }
}