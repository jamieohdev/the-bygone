package com.jamiedev.bygone.common.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;

public class UpsidedownTallPlantBlock extends UpsidedownPlantBlock {

    public static final EnumProperty<DoubleBlockHalf> HALF;
    public static final MapCodec<UpsidedownTallPlantBlock> CODEC = simpleCodec(UpsidedownTallPlantBlock::new);

    static {
        HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
    }

    public UpsidedownTallPlantBlock(BlockBehaviour.Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any().setValue(HALF, DoubleBlockHalf.LOWER));
    }

    public static void placeAt(LevelAccessor world, BlockState state, BlockPos pos, int flags) {
        BlockPos blockPos = pos.below();
        world.setBlock(pos, withWaterloggedState(world, pos, state.setValue(HALF, DoubleBlockHalf.LOWER)), flags);
        world.setBlock(blockPos, withWaterloggedState(world, blockPos, state.setValue(HALF, DoubleBlockHalf.UPPER)), flags);
    }

    public static BlockState withWaterloggedState(LevelReader world, BlockPos pos, BlockState state) {
        return state.hasProperty(BlockStateProperties.WATERLOGGED) ? state.setValue(BlockStateProperties.WATERLOGGED, world.isWaterAt(pos)) : state;
    }

    protected static void onBreakInCreative(Level world, BlockPos pos, BlockState state, Player player) {
        DoubleBlockHalf doubleBlockHalf = state.getValue(HALF);
        if (doubleBlockHalf == DoubleBlockHalf.UPPER) {
            BlockPos blockPos = pos.above();
            BlockState blockState = world.getBlockState(blockPos);
            if (blockState.is(state.getBlock()) && blockState.getValue(HALF) == DoubleBlockHalf.LOWER) {
                BlockState blockState2 = blockState.getFluidState().is(Fluids.WATER) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState();
                world.setBlock(blockPos, blockState2, 35);
                world.levelEvent(player, 2001, blockPos, getId(blockState));
            }
        }

    }

    @Override
    public MapCodec<? extends UpsidedownTallPlantBlock> codec() {
        return CODEC;
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
        DoubleBlockHalf doubleBlockHalf = state.getValue(HALF);
        if (direction.getAxis() == Direction.Axis.Y && doubleBlockHalf == DoubleBlockHalf.LOWER == (direction == Direction.UP) && (!neighborState.is(this) || neighborState.getValue(HALF) == doubleBlockHalf)) {
            return Blocks.AIR.defaultBlockState();
        } else {
            return doubleBlockHalf == DoubleBlockHalf.LOWER && direction == Direction.DOWN && !state.canSurvive(world, pos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, direction, neighborState, world, pos, neighborPos);
        }
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockPos blockPos = ctx.getClickedPos();
        Level world = ctx.getLevel();
        return blockPos.getY() < world.getMaxBuildHeight() - 1 && world.getBlockState(blockPos.below()).canBeReplaced(ctx) ? super.getStateForPlacement(ctx) : null;
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        BlockPos blockPos = pos.below();
        world.setBlock(blockPos, withWaterloggedState(world, blockPos, this.defaultBlockState().setValue(HALF, DoubleBlockHalf.UPPER)), 3);
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        if (state.getValue(HALF) != DoubleBlockHalf.UPPER) {
            return super.canSurvive(state, world, pos);
        } else {
            BlockState blockState = world.getBlockState(pos.below());
            return blockState.is(this) && blockState.getValue(HALF) == DoubleBlockHalf.LOWER;
        }
    }

    @Override
    public BlockState playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
        if (!world.isClientSide) {
            if (player.isCreative()) {
                onBreakInCreative(world, pos, state, player);
            } else {
                dropResources(state, world, pos, null, player, player.getMainHandItem());
            }
        }

        return super.playerWillDestroy(world, pos, state, player);
    }

    @Override
    public void playerDestroy(Level world, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
        super.playerDestroy(world, player, pos, Blocks.AIR.defaultBlockState(), blockEntity, tool);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HALF);
    }

    @Override
    protected long getSeed(BlockState state, BlockPos pos) {
        return Mth.getSeed(pos.getX(), pos.below(state.getValue(HALF) == DoubleBlockHalf.LOWER ? 0 : 1).getY(), pos.getZ());
    }
}
