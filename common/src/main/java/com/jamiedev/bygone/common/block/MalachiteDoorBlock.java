package com.jamiedev.bygone.common.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.function.BiConsumer;

public class MalachiteDoorBlock extends Block implements SimpleWaterloggedBlock {
    public static final MapCodec<MalachiteDoorBlock> CODEC = RecordCodecBuilder.mapCodec((p_308821_) -> p_308821_.group(BlockSetType.CODEC.fieldOf("block_set_type").forGetter(MalachiteDoorBlock::type), propertiesCodec()).apply(p_308821_, MalachiteDoorBlock::new));
    public static final DirectionProperty FACING;
    public static final BooleanProperty OPEN;
    public static final EnumProperty<DoorHingeSide> HINGE;
    public static final BooleanProperty POWERED;
    public static final EnumProperty<DoubleBlockHalf> HALF;
    public static final BooleanProperty WATERLOGGED;
    protected static final VoxelShape SOUTH_AABB;
    protected static final VoxelShape NORTH_AABB;
    protected static final VoxelShape WEST_AABB;
    protected static final VoxelShape EAST_AABB;
    private final BlockSetType type;

    public MapCodec<? extends MalachiteDoorBlock> codec() {
        return CODEC;
    }

    public MalachiteDoorBlock(BlockSetType type, BlockBehaviour.Properties properties) {
        super(properties.sound(type.soundType()));
        this.type = type;
        this.registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)
                this.stateDefinition.any())
                .setValue(WATERLOGGED, false)
                .setValue(FACING, Direction.NORTH))
                .setValue(OPEN, false))
                .setValue(HINGE, DoorHingeSide.LEFT)).setValue(POWERED, false))
                .setValue(HALF, DoubleBlockHalf.LOWER));
    }

    public BlockSetType type() {
        return this.type;
    }

    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Direction direction = (Direction)state.getValue(FACING);
        boolean flag = !(Boolean)state.getValue(OPEN);
        boolean flag1 = state.getValue(HINGE) == DoorHingeSide.RIGHT;
        VoxelShape var10000;
        switch (direction) {
            case SOUTH -> var10000 = flag ? SOUTH_AABB : (flag1 ? EAST_AABB : WEST_AABB);
            case WEST -> var10000 = flag ? WEST_AABB : (flag1 ? SOUTH_AABB : NORTH_AABB);
            case NORTH -> var10000 = flag ? NORTH_AABB : (flag1 ? WEST_AABB : EAST_AABB);
            default -> var10000 = flag ? EAST_AABB : (flag1 ? NORTH_AABB : SOUTH_AABB);
        }

        return var10000;
    }

    protected BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        DoubleBlockHalf doubleblockhalf = (DoubleBlockHalf)state.getValue(HALF);
        if (state.getValue(WATERLOGGED)) {
           level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        if (facing.getAxis() == Direction.Axis.Y && doubleblockhalf == DoubleBlockHalf.LOWER == (facing == Direction.UP)) {
            return facingState.getBlock() instanceof MalachiteDoorBlock && facingState.getValue(HALF) != doubleblockhalf ? (BlockState)facingState.setValue(HALF, doubleblockhalf) : Blocks.AIR.defaultBlockState();
        } else {
            return doubleblockhalf == DoubleBlockHalf.LOWER && facing == Direction.DOWN && !state.canSurvive(level, currentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, facing, facingState, level, currentPos, facingPos);
        }
    }

    @Override
    protected FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    protected void onExplosionHit(BlockState state, Level level, BlockPos pos, Explosion explosion, BiConsumer<ItemStack, BlockPos> dropConsumer) {
        if (explosion.canTriggerBlocks() && state.getValue(HALF) == DoubleBlockHalf.LOWER && this.type.canOpenByWindCharge() && !(Boolean)state.getValue(POWERED)) {
            this.setOpen((Entity)null, level, state, pos, !this.isOpen(state));
        }

        super.onExplosionHit(state, level, pos, explosion, dropConsumer);
    }

    protected static void preventDropFromBottomPart(Level level, BlockPos pos, BlockState state, Player player) {
        DoubleBlockHalf doubleblockhalf = (DoubleBlockHalf)state.getValue(HALF);
        if (doubleblockhalf == DoubleBlockHalf.UPPER) {
            BlockPos blockpos = pos.below();
            BlockState blockstate = level.getBlockState(blockpos);
            if (blockstate.is(state.getBlock()) && blockstate.getValue(HALF) == DoubleBlockHalf.LOWER) {
                BlockState blockstate1 = blockstate.getFluidState().is(Fluids.WATER) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState();
                level.setBlock(blockpos, blockstate1, 35);
                level.levelEvent(player, 2001, blockpos, Block.getId(blockstate));
            }
        }

    }

    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide && (player.isCreative() || !player.hasCorrectToolForDrops(state))) {
            preventDropFromBottomPart(level, pos, state, player);
        }

        return super.playerWillDestroy(level, pos, state, player);
    }

    protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
        boolean var10000;
        switch (pathComputationType) {
            case LAND:
            case AIR:
                var10000 = (Boolean)state.getValue(OPEN);
                break;
            case WATER:
                var10000 = false;
                break;
            default:
                throw new MatchException((String)null, (Throwable)null);
        }

        return var10000;
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos blockpos = context.getClickedPos();
        Level level = context.getLevel();
        FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
        boolean bl = fluidState.getType() == Fluids.WATER;
        if (blockpos.getY() < level.getMaxBuildHeight() - 1 && level.getBlockState(blockpos.above()).canBeReplaced(context)) {
            boolean flag = level.hasNeighborSignal(blockpos) || level.hasNeighborSignal(blockpos.above());
            return (BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.defaultBlockState()
                    .setValue(FACING, context.getHorizontalDirection()))
                    .setValue(HINGE, this.getHinge(context)))
                    .setValue(POWERED, flag))
                    .setValue(OPEN, flag))
                    .setValue(WATERLOGGED,bl)
                    .setValue(HALF, DoubleBlockHalf.LOWER);
        } else {
            return null;
        }
    }

    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        level.setBlock(pos.above(), (BlockState)state.setValue(HALF, DoubleBlockHalf.UPPER), 3);
    }

    private DoorHingeSide getHinge(BlockPlaceContext context) {
        BlockGetter blockgetter = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        Direction direction = context.getHorizontalDirection();
        BlockPos blockpos1 = blockpos.above();
        Direction direction1 = direction.getCounterClockWise();
        BlockPos blockpos2 = blockpos.relative(direction1);
        BlockState blockstate = blockgetter.getBlockState(blockpos2);
        BlockPos blockpos3 = blockpos1.relative(direction1);
        BlockState blockstate1 = blockgetter.getBlockState(blockpos3);
        Direction direction2 = direction.getClockWise();
        BlockPos blockpos4 = blockpos.relative(direction2);
        BlockState blockstate2 = blockgetter.getBlockState(blockpos4);
        BlockPos blockpos5 = blockpos1.relative(direction2);
        BlockState blockstate3 = blockgetter.getBlockState(blockpos5);
        int i = (blockstate.isCollisionShapeFullBlock(blockgetter, blockpos2) ? -1 : 0) + (blockstate1.isCollisionShapeFullBlock(blockgetter, blockpos3) ? -1 : 0) + (blockstate2.isCollisionShapeFullBlock(blockgetter, blockpos4) ? 1 : 0) + (blockstate3.isCollisionShapeFullBlock(blockgetter, blockpos5) ? 1 : 0);
        boolean flag = blockstate.getBlock() instanceof MalachiteDoorBlock && blockstate.getValue(HALF) == DoubleBlockHalf.LOWER;
        boolean flag1 = blockstate2.getBlock() instanceof MalachiteDoorBlock && blockstate2.getValue(HALF) == DoubleBlockHalf.LOWER;
        if ((!flag || flag1) && i <= 0) {
            if ((!flag1 || flag) && i >= 0) {
                int j = direction.getStepX();
                int k = direction.getStepZ();
                Vec3 vec3 = context.getClickLocation();
                double d0 = vec3.x - (double)blockpos.getX();
                double d1 = vec3.z - (double)blockpos.getZ();
                return j < 0 && d1 < (double)0.5F || j > 0 && d1 > (double)0.5F || k < 0 && d0 > (double)0.5F || k > 0 && d0 < (double)0.5F ? DoorHingeSide.RIGHT : DoorHingeSide.LEFT;
            } else {
                return DoorHingeSide.LEFT;
            }
        } else {
            return DoorHingeSide.RIGHT;
        }
    }

    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!this.type.canOpenByHand()) {
            return InteractionResult.PASS;
        } else {
            state = (BlockState)state.cycle(OPEN);
            level.setBlock(pos, state, 10);
            this.playSound(player, level, pos, (Boolean)state.getValue(OPEN));
            level.gameEvent(player, this.isOpen(state) ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pos);
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
    }

    public boolean isOpen(BlockState state) {
        return (Boolean)state.getValue(OPEN);
    }

    public void setOpen(@Nullable Entity entity, Level level, BlockState state, BlockPos pos, boolean open) {
        if (state.is(this) && (Boolean)state.getValue(OPEN) != open) {
            level.setBlock(pos, (BlockState)state.setValue(OPEN, open), 10);
            this.playSound(entity, level, pos, open);
            level.gameEvent(entity, open ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pos);
        }

    }

    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        boolean flag = level.hasNeighborSignal(pos) || level.hasNeighborSignal(pos.relative(state.getValue(HALF) == DoubleBlockHalf.LOWER ? Direction.UP : Direction.DOWN));
        if (!this.defaultBlockState().is(block) && flag != (Boolean)state.getValue(POWERED)) {
            if (flag != (Boolean)state.getValue(OPEN)) {
                this.playSound((Entity)null, level, pos, flag);
                level.gameEvent((Entity)null, flag ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pos);
            }

            level.setBlock(pos, (BlockState)((BlockState)state.setValue(POWERED, flag)).setValue(OPEN, flag), 2);
        }

    }

    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos blockpos = pos.below();
        BlockState blockstate = level.getBlockState(blockpos);
        return state.getValue(HALF) == DoubleBlockHalf.LOWER ? blockstate.isFaceSturdy(level, blockpos, Direction.UP) : blockstate.is(this);
    }

    private void playSound(@Nullable Entity source, Level level, BlockPos pos, boolean isOpening) {
        level.playSound(source, pos, isOpening ? this.type.doorOpen() : this.type.doorClose(), SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.1F + 0.9F);
    }


    @Deprecated
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return (BlockState)state.setValue(FACING, rotation.rotate((Direction)state.getValue(FACING)));
    }


    @Deprecated
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return mirror == Mirror.NONE ? state : (BlockState)state.rotate(mirror.getRotation((Direction)state.getValue(FACING))).cycle(HINGE);
    }

    protected long getSeed(BlockState state, BlockPos pos) {
        return Mth.getSeed(pos.getX(), pos.below(state.getValue(HALF) == DoubleBlockHalf.LOWER ? 0 : 1).getY(), pos.getZ());
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{HALF, FACING, OPEN, HINGE, POWERED, WATERLOGGED});
    }

    public static boolean isWoodenDoor(BlockState state) {
        Block var2 = state.getBlock();
        if (var2 instanceof MalachiteDoorBlock doorblock) {
            if (doorblock.type().canOpenByHand()) {
                return true;
            }
        }

        return false;
    }

    static {
        WATERLOGGED = BlockStateProperties.WATERLOGGED;
        FACING = HorizontalDirectionalBlock.FACING;
        OPEN = BlockStateProperties.OPEN;
        HINGE = BlockStateProperties.DOOR_HINGE;
        POWERED = BlockStateProperties.POWERED;
        HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
        SOUTH_AABB = Block.box((double)0.0F, (double)0.0F, (double)0.0F, (double)16.0F, (double)16.0F, (double)3.0F);
        NORTH_AABB = Block.box((double)0.0F, (double)0.0F, (double)13.0F, (double)16.0F, (double)16.0F, (double)16.0F);
        WEST_AABB = Block.box((double)13.0F, (double)0.0F, (double)0.0F, (double)16.0F, (double)16.0F, (double)16.0F);
        EAST_AABB = Block.box((double)0.0F, (double)0.0F, (double)0.0F, (double)3.0F, (double)16.0F, (double)16.0F);
    }
}
