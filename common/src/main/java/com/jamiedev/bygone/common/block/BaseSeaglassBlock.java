package com.jamiedev.bygone.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.TransparentBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class BaseSeaglassBlock extends TransparentBlock {
    /**
     * Seaglass code was inspired by <a href="https://github.com/Scouter456/Clear_Fluid_Glass">Scouter456's Clear Fluid Glass!</a>
     */

    public static final EnumProperty<NeighborOption> UP = EnumProperty.create("up", NeighborOption.class);
    public static final EnumProperty<NeighborOption> DOWN = EnumProperty.create("down", NeighborOption.class);
    public static final EnumProperty<NeighborOption> NORTH = EnumProperty.create("north", NeighborOption.class);
    public static final EnumProperty<NeighborOption> SOUTH = EnumProperty.create("south", NeighborOption.class);
    public static final EnumProperty<NeighborOption> EAST = EnumProperty.create("east", NeighborOption.class);
    public static final EnumProperty<NeighborOption> WEST = EnumProperty.create("west", NeighborOption.class);
    protected static final VoxelShape SHAPE_DOWN = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 0.01D, 16.0D);
    protected static final VoxelShape SHAPE_UP = Block.box(0.0D, 15.99D, 0.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape SHAPE_NORTH = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 0.01D);
    protected static final VoxelShape SHAPE_EAST = Block.box(15.99D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape SHAPE_SOUTH = Block.box(0.0D, 0.0D, 15.99D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape SHAPE_WEST = Block.box(0.0D, 0.0D, 0.0D, 0.01D, 16.0D, 16.0D);
    protected static final Map<Direction, EnumProperty<NeighborOption>> ADJACENT_PROPERTY_BY_DIRECTION = Map.of(
            Direction.DOWN,
            DOWN,
            Direction.UP,
            UP,
            Direction.NORTH,
            NORTH,
            Direction.EAST,
            EAST,
            Direction.SOUTH,
            SOUTH,
            Direction.WEST,
            WEST
    );
    protected static final Map<Direction, VoxelShape> OCCLUSION_SHAPES_BY_DIRECTION = Map.of(
            Direction.DOWN,
            SHAPE_DOWN,
            Direction.UP,
            SHAPE_UP,
            Direction.NORTH,
            SHAPE_NORTH,
            Direction.EAST,
            SHAPE_EAST,
            Direction.SOUTH,
            SHAPE_SOUTH,
            Direction.WEST,
            SHAPE_WEST
    );
    protected static final Map<BlockState, VoxelShape> OCCLUSION_SHAPES_BY_STATE = new HashMap<>();

    public BaseSeaglassBlock(BlockBehaviour.Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(UP, NeighborOption.OTHER)
                .setValue(DOWN, NeighborOption.OTHER)
                .setValue(EAST, NeighborOption.OTHER)
                .setValue(WEST, NeighborOption.OTHER)
                .setValue(NORTH, NeighborOption.OTHER)
                .setValue(SOUTH, NeighborOption.OTHER));
    }

    protected static VoxelShape computeShapeFromState(@NotNull BlockState state) {
        VoxelShape shape = Shapes.empty();
        for (Direction direction : Direction.values()) {

            if (state.getOptionalValue(ADJACENT_PROPERTY_BY_DIRECTION.get(direction))
                    .orElse(NeighborOption.OTHER) == NeighborOption.WATER) {
                shape = Shapes.or(shape, OCCLUSION_SHAPES_BY_DIRECTION.get(direction));
            }
        }
        return shape;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        LevelReader level = context.getLevel();
        BlockPos blockPos = context.getClickedPos();
        BlockState toReturn = super.getStateForPlacement(context);
        if (toReturn != null) {
            for (Direction direction : Direction.values()) {
                BlockPos relativePos = blockPos.relative(direction);
                BlockState relativeState = level.getBlockState(relativePos);
                toReturn = toReturn.trySetValue(
                        ADJACENT_PROPERTY_BY_DIRECTION.get(direction),
                        NeighborOption.getNeighborTypeFor(this, level, relativeState, relativePos)
                );
            }
        }
        return toReturn;
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_58032_) {
        p_58032_.add(UP, DOWN, NORTH, SOUTH, EAST, WEST);
    }

    @Override
    public @NotNull BlockState updateShape(@NotNull BlockState blockState, @NotNull Direction direction, @NotNull BlockState neighborState, @NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull BlockPos neighborPos) {
        BlockState toReturn = blockState;
        for (Direction relative : Direction.values()) {
            BlockPos relativePos = pos.relative(relative);
            BlockState relativeState = level.getBlockState(relativePos);
            toReturn = toReturn.trySetValue(
                    ADJACENT_PROPERTY_BY_DIRECTION.get(relative),
                    NeighborOption.getNeighborTypeFor(this, level, relativeState, relativePos)
            );
        }
        return toReturn;
    }

    @Override
    public @NotNull VoxelShape getOcclusionShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
        return OCCLUSION_SHAPES_BY_STATE.computeIfAbsent(state, BaseSeaglassBlock::computeShapeFromState);
    }

    public enum NeighborOption implements StringRepresentable {
        THIS("this", (Block asker, LevelReader level, BlockState neighbor, BlockPos pos) -> neighbor.is(asker)), WATER(
                "water",
                (Block asker, LevelReader level, BlockState neighbor, BlockPos pos) -> neighbor.getFluidState()
                        .is(FluidTags.WATER)
        ), OTHER("other", (Block asker, LevelReader level, BlockState neighbor, BlockPos pos) -> true);

        private final NeighborLookupPredicate isCorrect;
        private final String name;

        NeighborOption(String name, NeighborLookupPredicate isCorrect) {
            this.name = name;
            this.isCorrect = isCorrect;
        }

        public static NeighborOption getNeighborTypeFor(Block asker, LevelReader level, BlockState neighbor, BlockPos pos) {
            for (NeighborOption option : NeighborOption.values()) {
                if (option.isCorrect.test(asker, level, neighbor, pos)) {
                    return option;
                }
            }
            return OTHER;
        }

        @Override
        public @NotNull String getSerializedName() {
            return this.name;
        }
    }

    @FunctionalInterface
    public interface NeighborLookupPredicate {
        boolean test(Block block, LevelReader level, BlockState state, BlockPos pos);
    }
}
