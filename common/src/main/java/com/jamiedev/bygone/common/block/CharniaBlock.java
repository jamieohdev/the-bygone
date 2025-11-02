package com.jamiedev.bygone.common.block;

import com.jamiedev.bygone.core.registry.BGBlocks;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class CharniaBlock extends DoublePlantBlock implements LiquidBlockContainer {
    public static final MapCodec<CharniaBlock> CODEC = simpleCodec(CharniaBlock::new);
    public static final EnumProperty<DoubleBlockHalf> HALF;
    protected static final float field_31262 = 6.0F;
    protected static final VoxelShape SHAPE;

    static {
        HALF = DoublePlantBlock.HALF;
        SHAPE = Block.box(2.0, 0.0, 2.0, 14.0, 16.0, 14.0);
    }

    KelpBlock ref;

    public CharniaBlock(BlockBehaviour.Properties settings) {
        super(settings);
    }

    @Override
    public MapCodec<CharniaBlock> codec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected boolean mayPlaceOn(BlockState floor, BlockGetter world, BlockPos pos) {
        return floor.isFaceSturdy(world, pos, Direction.UP) && !floor.is(Blocks.MAGMA_BLOCK);
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader world, BlockPos pos, BlockState state) {
        return new ItemStack(BGBlocks.CHARNIA.get());
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockState blockState = super.getStateForPlacement(ctx);
        if (blockState != null) {
            FluidState fluidState = ctx.getLevel().getFluidState(ctx.getClickedPos().above());
            if (fluidState.is(FluidTags.WATER) && fluidState.getAmount() == 8) {
                return blockState;
            }
        }

        return null;
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
            BlockState blockState = world.getBlockState(pos.below());
            return blockState.is(this) && blockState.getValue(HALF) == DoubleBlockHalf.LOWER;
        } else {
            FluidState fluidState = world.getFluidState(pos);
            return super.canSurvive(state, world, pos) && fluidState.is(FluidTags.WATER) && fluidState.getAmount() == 8;
        }
    }

    @Override
    protected FluidState getFluidState(BlockState state) {
        return Fluids.WATER.getSource(false);
    }

    @Override
    public boolean canPlaceLiquid(@Nullable Player player, BlockGetter world, BlockPos pos, BlockState state, Fluid fluid) {
        return false;
    }

    @Override
    public boolean placeLiquid(LevelAccessor world, BlockPos pos, BlockState state, FluidState fluidState) {
        return false;
    }
}
