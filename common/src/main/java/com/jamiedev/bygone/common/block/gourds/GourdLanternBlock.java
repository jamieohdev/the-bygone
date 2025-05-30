package com.jamiedev.bygone.common.block.gourds;

import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.LevelReader;
import org.jetbrains.annotations.NotNull;
import com.jamiedev.bygone.core.registry.BGBlocks;
import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.GrowingPlantBodyBlock;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.WeepingVinesPlantBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class GourdLanternBlock extends GrowingPlantBodyBlock {
    public static final MapCodec<GourdLanternBlock> CODEC = simpleCodec(GourdLanternBlock::new);
    public static final BooleanProperty HANGING;
    public static final BooleanProperty WATERLOGGED;
    protected static final VoxelShape STANDING_SHAPE;
    protected static final VoxelShape HANGING_SHAPE;

    WeepingVinesPlantBlock ref;


    @Override
    public MapCodec<GourdLanternBlock> codec() {
        return CODEC;
    }

    public GourdLanternBlock(BlockBehaviour.Properties settings) {
        super(settings, Direction.DOWN, HANGING_SHAPE, false);
        this.registerDefaultState(this.stateDefinition.any().setValue(HANGING, false).setValue(WATERLOGGED, false));
    }
    @Override
    protected void onProjectileHit(Level world, BlockState state, BlockHitResult hit, Projectile projectile) {
        BlockPos blockPos = hit.getBlockPos();
        if (!world.isClientSide) {
            BlockState gourdState = world.getBlockState(blockPos);
            FallingBlockEntity fallingGourd = FallingBlockEntity.fall(world, blockPos, gourdState);
            world.addFreshEntity(fallingGourd);
        }

    }

    @Override
    protected void tick(BlockState state, ServerLevel world, BlockPos pos, @NotNull RandomSource random) {
        if (!state.canSurvive(world, pos)) {
            BlockState gourdState = world.getBlockState(pos);
            FallingBlockEntity fallingGourd = FallingBlockEntity.fall(world, pos, gourdState);
            world.addFreshEntity(fallingGourd);
        }
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.above()).isFaceSturdy(level, pos.above(), Direction.DOWN)
                || level.getBlockState(pos.below()).isFaceSturdy(level, pos.below(), Direction.UP);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return state.getValue(HANGING) ? HANGING_SHAPE : STANDING_SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HANGING, WATERLOGGED);
    }



    @Override
    protected FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }
    @Override
    protected GrowingPlantHeadBlock getHeadBlock() {
        return (GrowingPlantHeadBlock) BGBlocks.GOURD_VINE.get();
    }
    static {
        HANGING = BlockStateProperties.HANGING;
        WATERLOGGED = BlockStateProperties.WATERLOGGED;
        STANDING_SHAPE = Shapes.or(Block.box(3.0, 0.0, 3.0, 13.0, 5.0, 13.0), Block.box(6.0, 7.0, 6.0, 10.0, 9.0, 10.0));
        HANGING_SHAPE = Shapes.or(Block.box(3.0, 1.0, 3.0, 13.0, 5.0, 13.0), Block.box(6.0, 8.0, 6.0, 10.0, 10.0, 10.0));

    }
}
