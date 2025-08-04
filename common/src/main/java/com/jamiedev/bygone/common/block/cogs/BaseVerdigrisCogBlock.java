package com.jamiedev.bygone.common.block.cogs;

import com.google.common.collect.ImmutableMap;
import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.core.registry.BGBlocks;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.ticks.TickPriority;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BaseVerdigrisCogBlock extends PoweredBlock {
    public static final MapCodec<PoweredBlock> CODEC = RecordCodecBuilder.mapCodec(
            p_309135_ -> p_309135_.group(
                            VerdigrisStage.CODEC.fieldOf("verdigris_stage").forGetter(poweredBlock -> ((BaseVerdigrisCogBlock) poweredBlock).verdigrisStage), propertiesCodec()
                    )
                    .apply(p_309135_, BaseVerdigrisCogBlock::new)
    );
    public static final BooleanProperty UP;
    public static final BooleanProperty NORTH;
    public static final BooleanProperty EAST;
    public static final BooleanProperty SOUTH;
    public static final BooleanProperty WEST;
    public static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION;
    protected static final float AABB_OFFSET = 1.0F;
    private static final VoxelShape UP_AABB;
    private static final VoxelShape WEST_AABB;
    private static final VoxelShape EAST_AABB;
    private static final VoxelShape NORTH_AABB;
    private static final VoxelShape SOUTH_AABB;
    private final ImmutableMap shapesCache;
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public final VerdigrisStage verdigrisStage;
    public static final IntegerProperty DELAY;
    public static final BooleanProperty POWERED;

    public MapCodec<PoweredBlock> codec() {
        return CODEC;
    }

    public BaseVerdigrisCogBlock(VerdigrisStage stage, BlockBehaviour.Properties properties) {
        super(properties);
        this.verdigrisStage = stage;
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(UP, false).setValue(NORTH, false).setValue(EAST, false).setValue(SOUTH, false).setValue(WEST, false).setValue(POWERED, true).setValue(DELAY, Integer.valueOf(1)));
        this.shapesCache = ImmutableMap.copyOf((Map) this.stateDefinition.getPossibleStates().stream().collect(Collectors.toMap(Function.identity(), BaseVerdigrisCogBlock::calculateShape)));
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        super.animateTick(state, level, pos, random);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        level.scheduleTick(pos, this, verdigrisStage.degradationAmount, TickPriority.VERY_HIGH);
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        super.tick(state, level, pos, random);
        var newState = verdigrisStage.shouldPower ? state.setValue(POWERED, !state.getValue(POWERED)) : state.setValue(POWERED, false);
        level.setBlockAndUpdate(pos, newState);
        if (random.nextFloat() < 0.001F) {
            var replaceState = nextBLockState(state, level, pos);
            level.setBlockAndUpdate(pos, replaceState);
        }
        if (verdigrisStage.shouldPower)
            level.scheduleTick(pos, this, verdigrisStage.degradationAmount, TickPriority.VERY_HIGH);
    }

    public BlockState nextBLockState(BlockState oldState, Level level, BlockPos pos) {
        VerdigrisStage verdigrisStage = ((BaseVerdigrisCogBlock) oldState.getBlock()).verdigrisStage;
        if (verdigrisStage == verdigrisStage.last())
            return oldState;
        var nextBlock = BuiltInRegistries.BLOCK.get(ResourceLocation.fromNamespaceAndPath(Bygone.MOD_ID, verdigrisStage.next().stage.toLowerCase() + "_verdigris_cog"));
        level.scheduleTick(pos, nextBlock, 1, TickPriority.VERY_HIGH);
        return nextBlock.withPropertiesOf(oldState);
    }

    @Override
    protected int getSignal(BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side) {
        if (blockState.getValue(POWERED) && (side == Direction.UP || side == Direction.DOWN))
            return super.getSignal(blockState, blockAccess, pos, side);
        else
            return 0;
    }

    private static VoxelShape calculateShape(BlockState state) {
        VoxelShape voxelshape = Shapes.empty();
        if (state.getValue(UP)) {
            voxelshape = UP_AABB;
        }

        if (state.getValue(NORTH)) {
            voxelshape = Shapes.or(voxelshape, NORTH_AABB);
        }

        if (state.getValue(SOUTH)) {
            voxelshape = Shapes.or(voxelshape, SOUTH_AABB);
        }

        if (state.getValue(EAST)) {
            voxelshape = Shapes.or(voxelshape, EAST_AABB);
        }

        if (state.getValue(WEST)) {
            voxelshape = Shapes.or(voxelshape, WEST_AABB);
        }

        return voxelshape.isEmpty() ? Shapes.block() : voxelshape;
    }

    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return (VoxelShape) this.shapesCache.get(state);
    }

    protected boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos) {
        return true;
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        Direction direction = state.getValue(FACING);
        return this.hasFaces(this.getUpdatedState(state, level, pos));
    }

    private boolean canAttachTo(BlockGetter blockReader, BlockPos pos, Direction direction) {
        BlockState blockstate = blockReader.getBlockState(pos);
        return blockstate.isFaceSturdy(blockReader, pos, direction);
    }

    private boolean hasFaces(BlockState state) {
        return this.countFaces(state) > 0;
    }

    private int countFaces(BlockState state) {
        int i = 0;
        for (BooleanProperty booleanproperty : PROPERTY_BY_DIRECTION.values()) {
            if (state.getValue(booleanproperty)) {
                i++;
            }
        }
        return i;
    }

    private boolean canSupportAtFace(BlockGetter level, BlockPos pos, Direction direction) {
        if (direction == Direction.DOWN) {
            return false;
        } else {
            BlockPos blockpos = pos.relative(direction);
            if (isAcceptableNeighbour(level, blockpos, direction)) {
                return true;
            } else if (direction.getAxis() == Direction.Axis.Y) {
                return false;
            } else {
                BooleanProperty booleanproperty = (BooleanProperty) PROPERTY_BY_DIRECTION.get(direction);
                BlockState blockstate = level.getBlockState(pos.above());
                return blockstate.is(this) && blockstate.getValue(booleanproperty);
            }
        }
    }

    public static boolean isAcceptableNeighbour(BlockGetter blockReader, BlockPos neighborPos, Direction attachedFace) {
        return MultifaceBlock.canAttachTo(blockReader, attachedFace, neighborPos, blockReader.getBlockState(neighborPos));
    }


    private BlockState getUpdatedState(BlockState state, BlockGetter level, BlockPos pos) {
        BlockPos blockpos = pos.above();
        if (state.getValue(UP)) {
            state = state.setValue(UP, isAcceptableNeighbour(level, blockpos, Direction.DOWN));
        }

        BlockState blockstate = null;

        for (Direction direction : Direction.Plane.HORIZONTAL) {
            BooleanProperty booleanproperty = getPropertyForFace(direction);
            if (state.getValue(booleanproperty)) {
                boolean flag = this.canSupportAtFace(level, pos, direction);
                if (!flag) {
                    if (blockstate == null) {
                        blockstate = level.getBlockState(blockpos);
                    }

                    flag = blockstate.is(this) && blockstate.getValue(booleanproperty);
                }

                state = state.setValue(booleanproperty, flag);
            }
        }

        return state;
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        if (facing == Direction.DOWN) {
            return super.updateShape(state, facing, facingState, level, currentPos, facingPos);
        } else {
            BlockState blockstate = this.getUpdatedState(state, level, currentPos);
            return !this.hasFaces(blockstate) ? Blocks.AIR.defaultBlockState() : blockstate;
        }
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, movedByPiston);
        if (level.isClientSide())
            return;
        BlockState blockstate = this.getUpdatedState(state, level, pos);
        if (!this.hasFaces(blockstate)) {
            Block.dropResources(blockstate, level, pos);
            level.removeBlock(pos, movedByPiston);
        }
    }

    protected int getInputSignal(Level level, BlockPos pos, BlockState state) {
        Direction direction = state.getValue(FACING);
        BlockPos blockPos = pos.relative(direction);
        int i = level.getSignal(blockPos, direction);
        if (i >= 15) {
            return i;
        } else {
            BlockState blockState = level.getBlockState(blockPos);
            return Math.max(i, blockState.is(Blocks.REDSTONE_WIRE) ? blockState.getValue(RedStoneWireBlock.POWER) : 0);
        }
    }

    protected boolean sideInputDiodesOnly() {
        return false;
    }

    protected int getAlternateSignal(SignalGetter level, BlockPos pos, BlockState state) {
        Direction direction = state.getValue(FACING);
        Direction direction2 = direction.getClockWise();
        Direction direction3 = direction.getCounterClockWise();
        boolean bl = this.sideInputDiodesOnly();
        return Math.max(level.getControlInputSignal(pos.relative(direction2), direction2, bl), level.getControlInputSignal(pos.relative(direction3), direction3, bl));
    }

    protected boolean isSignalSource(BlockState state) {
        return true;
    }

    protected boolean shouldTurnOn(Level level, BlockPos pos, BlockState state) {
        return this.getInputSignal(level, pos, state) > 0;
    }

    protected int getDelay(BlockState state) {
        return state.getValue(DELAY) * 2;
    }

    private BlockState copyRandomFaces(BlockState sourceState, BlockState spreadState, RandomSource random) {
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            if (random.nextBoolean()) {
                BooleanProperty booleanproperty = getPropertyForFace(direction);
                if (sourceState.getValue(booleanproperty)) {
                    spreadState = spreadState.setValue(booleanproperty, true);
                }
            }
        }

        return spreadState;
    }

    private boolean hasHorizontalConnection(BlockState state) {
        return state.getValue(NORTH) || state.getValue(EAST) || state.getValue(SOUTH) || state.getValue(WEST);
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState blockstate = context.getLevel().getBlockState(context.getClickedPos());
        boolean flag = blockstate.is(this);
        BlockState blockstate1 = flag ? blockstate : this.defaultBlockState();

        for (Direction direction : context.getNearestLookingDirections()) {
            if (direction != Direction.DOWN) {
                BooleanProperty booleanproperty = getPropertyForFace(direction);
                boolean flag1 = flag && blockstate.getValue(booleanproperty);
                if (!flag1 && this.canSupportAtFace(context.getLevel(), context.getClickedPos(), direction)) {
                    return blockstate1.setValue(booleanproperty, true);
                }
            }
        }

        return flag ? blockstate1 : null;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(UP, NORTH, EAST, SOUTH, WEST, POWERED, DELAY, FACING);
    }

    /**
     * @deprecated
     */
    @Deprecated
    protected BlockState rotate(BlockState state, Rotation rotate) {
        switch (rotate) {
            case CLOCKWISE_180 -> {
                return state.setValue(NORTH, state.getValue(SOUTH)).setValue(EAST, state.getValue(WEST)).setValue(SOUTH, state.getValue(NORTH)).setValue(WEST, state.getValue(EAST));
            }
            case COUNTERCLOCKWISE_90 -> {
                return state.setValue(NORTH, state.getValue(EAST)).setValue(EAST, state.getValue(SOUTH)).setValue(SOUTH, state.getValue(WEST)).setValue(WEST, state.getValue(NORTH));
            }
            case CLOCKWISE_90 -> {
                return state.setValue(NORTH, state.getValue(WEST)).setValue(EAST, state.getValue(NORTH)).setValue(SOUTH, state.getValue(EAST)).setValue(WEST, state.getValue(SOUTH));
            }
            default -> {
                return state;
            }
        }
    }

    /**
     * @deprecated
     */
    @Deprecated
    protected BlockState mirror(BlockState state, Mirror mirror) {
        switch (mirror) {
            case LEFT_RIGHT -> {
                return state.setValue(NORTH, state.getValue(SOUTH)).setValue(SOUTH, state.getValue(NORTH));
            }
            case FRONT_BACK -> {
                return state.setValue(EAST, state.getValue(WEST)).setValue(WEST, state.getValue(EAST));
            }
            default -> {
                return super.mirror(state, mirror);
            }
        }
    }

    public static BooleanProperty getPropertyForFace(Direction face) {
        return PROPERTY_BY_DIRECTION.get(face);
    }

    static {
        POWERED = BlockStateProperties.POWERED;
        DELAY = IntegerProperty.create("delay", 0, 2);
        UP = PipeBlock.UP;
        NORTH = PipeBlock.NORTH;
        EAST = PipeBlock.EAST;
        SOUTH = PipeBlock.SOUTH;
        WEST = PipeBlock.WEST;
        PROPERTY_BY_DIRECTION = PipeBlock.PROPERTY_BY_DIRECTION.entrySet().stream().filter((p_57886_) -> p_57886_.getKey() != Direction.DOWN).collect(Util.toMap());
        UP_AABB = Block.box(0.0F, 15.0F, 0.0F, 16.0F, 16.0F, 16.0F);
        WEST_AABB = Block.box(0.0F, 0.0F, 0.0F, 1.0F, 16.0F, 16.0F);
        EAST_AABB = Block.box(15.0F, 0.0F, 0.0F, 16.0F, 16.0F, 16.0F);
        NORTH_AABB = Block.box(0.0F, 0.0F, 0.0F, 16.0F, 16.0F, 1.0F);
        SOUTH_AABB = Block.box(0.0F, 0.0F, 15.0F, 16.0F, 16.0F, 16.0F);
    }

    public enum VerdigrisStage implements StringRepresentable {
        PRISTINE("pristine", 10),
        TARNISHED("tarnished", 30),
        RAMSHACKLED("ramshackled", 60),
        BROKEN("broken", 0, false);

        public static final Codec<VerdigrisStage> CODEC = StringRepresentable.fromEnum(VerdigrisStage::values);
        private String stage;
        private int degradationAmount;
        private boolean shouldPower;

        private VerdigrisStage(String stage, int degradationAmount) {
            this(stage, degradationAmount, true);
        }

        private VerdigrisStage(String stage, int degradationAmount, boolean shouldPower) {
            this.stage = stage;
            this.degradationAmount = degradationAmount;
            this.shouldPower = shouldPower;
        }

        @Override
        public String getSerializedName() {
            return stage;
        }

        private static final VerdigrisStage[] vals = values();

        public VerdigrisStage next() {
            return vals[(this.ordinal() + 1) % vals.length];
        }

        public VerdigrisStage last() {
            return vals[vals.length - 1];
        }
    }

}