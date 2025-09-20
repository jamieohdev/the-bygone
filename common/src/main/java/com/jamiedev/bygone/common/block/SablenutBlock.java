package com.jamiedev.bygone.common.block;

import com.jamiedev.bygone.core.registry.BGBlocks;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class SablenutBlock extends FallingBlock
{
    CocoaBlock ref;
    public static final int MAX_AGE = 3;
    public static final IntegerProperty AGE;
    public static final DirectionProperty FACING;

    public static final MapCodec<SablenutBlock> CODEC = simpleCodec(SablenutBlock::new);

    public SablenutBlock(Properties properties) {
        super(properties);
        this.registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(FACING, Direction.UP)).setValue(AGE, 0));
    }

    protected boolean isRandomlyTicking(BlockState state) {
        return (Integer)state.getValue(AGE) < 3;
    }

    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (level.random.nextInt(5) == 0) {
            int i = (Integer)state.getValue(AGE);
            if (i < 3) {
                level.setBlock(pos, (BlockState)state.setValue(AGE, i + 1), 2);
            }
        }
    }

    protected BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        return facing == state.getValue(FACING) && !state.canSurvive(level, currentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, facing, facingState, level, currentPos, facingPos);
    }

    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return (Integer)state.getValue(AGE) < 3;
    }

    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        level.setBlock(pos, (BlockState)state.setValue(AGE, (Integer)state.getValue(AGE) + 1), 2);
    }

    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockState blockstate = level.getBlockState(pos.relative((Direction)state.getValue(FACING)));
        return blockstate.is(BlockTags.JUNGLE_LOGS);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{FACING, AGE});
    }

    protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
        return false;
    }

    @Override
    protected MapCodec<SablenutBlock> codec() {
        return CODEC;
    }

    @Nullable
    public static BlockState damage(BlockState state) {
        if (state.is(BGBlocks.SABLENUT.get())) {
            return (BlockState)BGBlocks.CHIPPED_SABLENUT.get().defaultBlockState();
        }  else {
            return (BlockState)Blocks.AIR.defaultBlockState();
        }
    }

    static {
        AGE = BlockStateProperties.AGE_3;
        FACING = BlockStateProperties.FACING;
  }
}

