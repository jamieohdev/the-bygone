package com.jamiedev.bygone.common.blocks.shelf;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.MapCodec;
import net.minecraft.Util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ShelfFungiWallFanBlock2 extends ShelfFungiFanBlock
{
    public static final MapCodec<ShelfFungiWallFanBlock2> CODEC = simpleCodec(ShelfFungiWallFanBlock2::new);
    public static final BooleanProperty UP;
    public static final BooleanProperty NORTH;
    public static final BooleanProperty EAST;
    public static final BooleanProperty SOUTH;
    public static final BooleanProperty WEST;
    public static final Map<Direction, BooleanProperty> FACING_PROPERTIES;
    protected static final float field_31275 = 1.0F;
    private static final VoxelShape UP_SHAPE;
    private static final VoxelShape EAST_SHAPE;
    private static final VoxelShape WEST_SHAPE;
    private static final VoxelShape SOUTH_SHAPE;
    private static final VoxelShape NORTH_SHAPE;
    private final Map<BlockState, VoxelShape> shapesByState;

    @Override
    public MapCodec<ShelfFungiWallFanBlock2> codec() {
        return CODEC;
    }

    public ShelfFungiWallFanBlock2(BlockBehaviour.Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any().setValue(UP, false).setValue(NORTH, false).setValue(EAST, false).setValue(SOUTH, false).setValue(WEST, false));
        this.shapesByState = ImmutableMap.copyOf((Map)this.stateDefinition.getPossibleStates().stream().collect(Collectors.toMap(Function.identity(), ShelfFungiWallFanBlock2::getShapeForState)));
    }

    private static VoxelShape getShapeForState(BlockState state) {
        VoxelShape voxelShape = Shapes.empty();
        if (state.getValue(UP)) {
            voxelShape = UP_SHAPE;
        }

        if (state.getValue(NORTH)) {
            voxelShape = Shapes.or(voxelShape, SOUTH_SHAPE);
        }

        if (state.getValue(SOUTH)) {
            voxelShape = Shapes.or(voxelShape, NORTH_SHAPE);
        }

        if (state.getValue(EAST)) {
            voxelShape = Shapes.or(voxelShape, WEST_SHAPE);
        }

        if (state.getValue(WEST)) {
            voxelShape = Shapes.or(voxelShape, EAST_SHAPE);
        }

        return voxelShape.isEmpty() ? Shapes.block() : voxelShape;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return this.shapesByState.get(state);
    }

    @Override
    protected boolean propagatesSkylightDown(BlockState state, BlockGetter world, BlockPos pos) {
        return true;
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        return this.hasAdjacentBlocks(this.getPlacementShape(state, world, pos));
    }

    private boolean hasAdjacentBlocks(BlockState state) {
        return this.getAdjacentBlockCount(state) > 0;
    }

    private int getAdjacentBlockCount(BlockState state) {
        int i = 0;
        Iterator<BooleanProperty> var3 = FACING_PROPERTIES.values().iterator();

        while(var3.hasNext()) {
            BooleanProperty booleanProperty = (BooleanProperty)var3.next();
            if (state.getValue(booleanProperty)) {
                ++i;
            }
        }

        return i;
    }

    private boolean shouldHaveSide(BlockGetter world, BlockPos pos, Direction side) {
        if (side == Direction.DOWN) {
            return false;
        } else {
            BlockPos blockPos = pos.relative(side);
            if (shouldConnectTo(world, blockPos, side)) {
                return true;
            } else if (side.getAxis() == Direction.Axis.Y) {
                return false;
            } else {
                BooleanProperty booleanProperty = FACING_PROPERTIES.get(side);
                BlockState blockState = world.getBlockState(pos.above());
                return blockState.is(this) && blockState.getValue(booleanProperty);
            }
        }
    }

    public static boolean shouldConnectTo(BlockGetter world, BlockPos pos, Direction direction) {
        return MultifaceBlock.canAttachTo(world, direction, pos, world.getBlockState(pos));
    }

    private BlockState getPlacementShape(BlockState state, BlockGetter world, BlockPos pos) {
        BlockPos blockPos = pos.above();
        if (state.getValue(UP)) {
            state = state.setValue(UP, shouldConnectTo(world, blockPos, Direction.DOWN));
        }

        BlockState blockState = null;
        Iterator<Direction> var6 = Direction.Plane.HORIZONTAL.iterator();

        while(true) {
            Direction direction;
            BooleanProperty booleanProperty;
            do {
                if (!var6.hasNext()) {
                    return state;
                }

                direction = (Direction)var6.next();
                booleanProperty = getFacingProperty(direction);
            } while(!(Boolean)state.getValue(booleanProperty));

            boolean bl = this.shouldHaveSide(world, pos, direction);
            if (!bl) {
                if (blockState == null) {
                    blockState = world.getBlockState(blockPos);
                }

                bl = blockState.is(this) && blockState.getValue(booleanProperty);
            }

            state = state.setValue(booleanProperty, bl);
        }
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
        if (direction == Direction.DOWN) {
            return super.updateShape(state, direction, neighborState, world, pos, neighborPos);
        } else {
            BlockState blockState = this.getPlacementShape(state, world, pos);
            return !this.hasAdjacentBlocks(blockState) ? Blocks.AIR.defaultBlockState() : blockState;
        }
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if (world.getGameRules().getBoolean(GameRules.RULE_DO_VINES_SPREAD)) {
            if (random.nextInt(4) == 0) {
                Direction direction = Direction.getRandom(random);
                BlockPos blockPos = pos.above();
                BlockPos blockPos2;
                BlockState blockState;
                Direction direction2;
                if (direction.getAxis().isHorizontal() && !(Boolean)state.getValue(getFacingProperty(direction))) {
                    if (this.canGrowAt(world, pos)) {
                        blockPos2 = pos.relative(direction);
                        blockState = world.getBlockState(blockPos2);
                        if (blockState.isAir()) {
                            direction2 = direction.getClockWise();
                            Direction direction3 = direction.getCounterClockWise();
                            boolean bl = state.getValue(getFacingProperty(direction2));
                            boolean bl2 = state.getValue(getFacingProperty(direction3));
                            BlockPos blockPos3 = blockPos2.relative(direction2);
                            BlockPos blockPos4 = blockPos2.relative(direction3);
                            if (bl && shouldConnectTo(world, blockPos3, direction2)) {
                                world.setBlock(blockPos2, this.defaultBlockState().setValue(getFacingProperty(direction2), true), 2);
                            } else if (bl2 && shouldConnectTo(world, blockPos4, direction3)) {
                                world.setBlock(blockPos2, this.defaultBlockState().setValue(getFacingProperty(direction3), true), 2);
                            } else {
                                Direction direction4 = direction.getOpposite();
                                if (bl && world.isEmptyBlock(blockPos3) && shouldConnectTo(world, pos.relative(direction2), direction4)) {
                                    world.setBlock(blockPos3, this.defaultBlockState().setValue(getFacingProperty(direction4), true), 2);
                                } else if (bl2 && world.isEmptyBlock(blockPos4) && shouldConnectTo(world, pos.relative(direction3), direction4)) {
                                    world.setBlock(blockPos4, this.defaultBlockState().setValue(getFacingProperty(direction4), true), 2);
                                } else if ((double)random.nextFloat() < 0.05 && shouldConnectTo(world, blockPos2.above(), Direction.UP)) {
                                    world.setBlock(blockPos2, this.defaultBlockState().setValue(UP, true), 2);
                                }
                            }
                        } else if (shouldConnectTo(world, blockPos2, direction)) {
                            world.setBlock(pos, state.setValue(getFacingProperty(direction), true), 2);
                        }

                    }
                } else {
                    if (direction == Direction.UP && pos.getY() < world.getMaxBuildHeight() - 1) {
                        if (this.shouldHaveSide(world, pos, direction)) {
                            world.setBlock(pos, state.setValue(UP, true), 2);
                            return;
                        }

                        if (world.isEmptyBlock(blockPos)) {
                            if (!this.canGrowAt(world, pos)) {
                                return;
                            }

                            BlockState blockState2 = state;
                            Iterator<Direction> var17 = Direction.Plane.HORIZONTAL.iterator();

                            while(true) {
                                do {
                                    if (!var17.hasNext()) {
                                        if (this.hasHorizontalSide(blockState2)) {
                                            world.setBlock(blockPos, blockState2, 2);
                                        }

                                        return;
                                    }

                                    direction2 = (Direction)var17.next();
                                } while(!random.nextBoolean() && shouldConnectTo(world, blockPos.relative(direction2), direction2));

                                blockState2 = blockState2.setValue(getFacingProperty(direction2), false);
                            }
                        }
                    }

                    if (pos.getY() > world.getMinBuildHeight()) {
                        blockPos2 = pos.below();
                        blockState = world.getBlockState(blockPos2);
                        if (blockState.isAir() || blockState.is(this)) {
                            BlockState blockState3 = blockState.isAir() ? this.defaultBlockState() : blockState;
                            BlockState blockState4 = this.getGrownState(state, blockState3, random);
                            if (blockState3 != blockState4 && this.hasHorizontalSide(blockState4)) {
                                world.setBlock(blockPos2, blockState4, 2);
                            }
                        }
                    }

                }
            }
        }
    }

    private BlockState getGrownState(BlockState above, BlockState state, RandomSource random) {
        Iterator<Direction> var4 = Direction.Plane.HORIZONTAL.iterator();

        while(var4.hasNext()) {
            Direction direction = (Direction)var4.next();
            if (random.nextBoolean()) {
                BooleanProperty booleanProperty = getFacingProperty(direction);
                if (above.getValue(booleanProperty)) {
                    state = state.setValue(booleanProperty, true);
                }
            }
        }

        return state;
    }

    private boolean hasHorizontalSide(BlockState state) {
        return state.getValue(NORTH) || state.getValue(EAST) || state.getValue(SOUTH) || state.getValue(WEST);
    }

    private boolean canGrowAt(BlockGetter world, BlockPos pos) {

        Iterable<BlockPos> iterable = BlockPos.betweenClosed(pos.getX() - 4, pos.getY() - 1, pos.getZ() - 4, pos.getX() + 4, pos.getY() + 1, pos.getZ() + 4);
        int j = 5;
        Iterator<BlockPos> var6 = iterable.iterator();

        while(var6.hasNext()) {
            BlockPos blockPos = (BlockPos)var6.next();
            if (world.getBlockState(blockPos).is(this)) {
                --j;
                if (j <= 0) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    protected boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
        BlockState blockState = context.getLevel().getBlockState(context.getClickedPos());
        if (blockState.is(this)) {
            return this.getAdjacentBlockCount(blockState) < FACING_PROPERTIES.size();
        } else {
            return super.canBeReplaced(state, context);
        }
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockState blockState = ctx.getLevel().getBlockState(ctx.getClickedPos());
        boolean bl = blockState.is(this);
        BlockState blockState2 = bl ? blockState : this.defaultBlockState();
        Direction[] var5 = ctx.getNearestLookingDirections();
        int var6 = var5.length;

        for(int var7 = 0; var7 < var6; ++var7) {
            Direction direction = var5[var7];
            if (direction != Direction.DOWN) {
                BooleanProperty booleanProperty = getFacingProperty(direction);
                boolean bl2 = bl && blockState.getValue(booleanProperty);
                if (!bl2 && this.shouldHaveSide(ctx.getLevel(), ctx.getClickedPos(), direction)) {
                    return blockState2.setValue(booleanProperty, true);
                }
            }
        }

        return bl ? blockState2 : null;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(UP, NORTH, EAST, SOUTH, WEST);
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        switch (rotation) {
            case CLOCKWISE_180:
                return state.setValue(NORTH, state.getValue(SOUTH)).setValue(EAST, state.getValue(WEST)).setValue(SOUTH, state.getValue(NORTH)).setValue(WEST, state.getValue(EAST));
            case COUNTERCLOCKWISE_90:
                return state.setValue(NORTH, state.getValue(EAST)).setValue(EAST, state.getValue(SOUTH)).setValue(SOUTH, state.getValue(WEST)).setValue(WEST, state.getValue(NORTH));
            case CLOCKWISE_90:
                return state.setValue(NORTH, state.getValue(WEST)).setValue(EAST, state.getValue(NORTH)).setValue(SOUTH, state.getValue(EAST)).setValue(WEST, state.getValue(SOUTH));
            default:
                return state;
        }
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        switch (mirror) {
            case LEFT_RIGHT:
                return state.setValue(NORTH, state.getValue(SOUTH)).setValue(SOUTH, state.getValue(NORTH));
            case FRONT_BACK:
                return state.setValue(EAST, state.getValue(WEST)).setValue(WEST, state.getValue(EAST));
            default:
                return super.mirror(state, mirror);
        }
    }

    public static BooleanProperty getFacingProperty(Direction direction) {
        return FACING_PROPERTIES.get(direction);
    }

    static {
        UP = PipeBlock.UP;
        NORTH = PipeBlock.NORTH;
        EAST = PipeBlock.EAST;
        SOUTH = PipeBlock.SOUTH;
        WEST = PipeBlock.WEST;
        FACING_PROPERTIES = PipeBlock.PROPERTY_BY_DIRECTION.entrySet().stream().filter((entry) -> {
            return entry.getKey() != Direction.DOWN;
        }).collect(Util.toMap());
        UP_SHAPE = Block.box(0.0, 15.0, 0.0, 16.0, 16.0, 16.0);
        EAST_SHAPE = Block.box(0.0, 4.0, 0.0, 11.0, 12.0, 16.0);
        WEST_SHAPE = Block.box(5.0, 4.0, 0.0, 16.0, 12.0, 16.0);
        SOUTH_SHAPE = Block.box(0.0, 4.0, 0.0, 16.0, 12.0, 11.0);
        NORTH_SHAPE = Block.box(0.0, 4.0, 5.0, 16.0, 12.0, 16.0);
    }
}
