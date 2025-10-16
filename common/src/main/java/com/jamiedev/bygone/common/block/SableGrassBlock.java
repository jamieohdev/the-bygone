package com.jamiedev.bygone.common.block;

import com.jamiedev.bygone.core.registry.BGBlocks;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.NetherVines;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SableGrassBlock extends GrowingPlantHeadBlock
{
    public static final MapCodec<SableGrassBlock> CODEC = simpleCodec(SableGrassBlock::new);
    public static final VoxelShape SHAPE = Block.box((double)4.0F, (double)0.0F, (double)4.0F, (double)12.0F, (double)15.0F, (double)12.0F);
    double growPerTickProbability;

    public MapCodec<SableGrassBlock> codec() {
        return CODEC;
    }

    public SableGrassBlock(BlockBehaviour.Properties p_154864_) {
        super(p_154864_, Direction.UP, SHAPE, false, 0.00005);
    }

    protected int getBlocksToGrowWhenBonemealed(RandomSource p_222649_) {
        return NetherVines.getBlocksToGrowWhenBonemealed(p_222649_);
    }

    protected Block getBodyBlock() {
        return BGBlocks.SABLE_GRASS_PLANT.get();
    }

    protected boolean canGrowInto(BlockState p_154869_) {
        return NetherVines.isValidGrowthState(p_154869_);
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos blockpos = pos.relative(this.growthDirection.getOpposite());
        BlockState blockstate = level.getBlockState(blockpos);
        return !this.canAttachTo(blockstate) ? false : blockstate.is(this.getHeadBlock()) || blockstate.is(this.getBodyBlock()) || blockstate.is(BGBlocks.SABLE_LEAVES.get())
                || blockstate.isFaceSturdy(level, blockpos, this.growthDirection);
    }

    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        BlockPos blockpos = pos.relative(this.growthDirection);
        int i = Math.min((Integer)state.getValue(AGE) + 1, 25);
        int j = this.getBlocksToGrowWhenBonemealed(random);

        for(int k = 0; k < j && this.canGrowInto(level.getBlockState(blockpos)); ++k) {
            if (random.nextInt(5) == 1)
            {
                level.setBlockAndUpdate(blockpos, BGBlocks.SABLOSSOM.get().defaultBlockState());
                level.setBlockAndUpdate(blockpos.below(), BGBlocks.SABLE_GRASS_PLANT.get().defaultBlockState());
            }
            else
            {
                level.setBlockAndUpdate(blockpos, (BlockState)state.setValue(AGE, i));
                blockpos = blockpos.relative(this.growthDirection);
                i = Math.min(i + 1, 25);
            }
        }

    }

    @Override
    public boolean isMaxAge(BlockState state) {
        return (Integer)state.getValue(AGE) == 5;
    }
}