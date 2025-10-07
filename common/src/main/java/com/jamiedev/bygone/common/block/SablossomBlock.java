package com.jamiedev.bygone.common.block;

import com.jamiedev.bygone.core.registry.BGBlocks;
import com.jamiedev.bygone.core.registry.BGParticleTypes;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.NetherVines;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SablossomBlock extends GrowingPlantHeadBlock
{
    public static final MapCodec<SablossomBlock> CODEC = simpleCodec(SablossomBlock::new);
    public static final VoxelShape SHAPE = Block.box((double)4.0F, (double)0.0F, (double)4.0F, (double)12.0F, (double)15.0F, (double)12.0F);

    public MapCodec<SablossomBlock> codec() {
        return CODEC;
    }

    public SablossomBlock(BlockBehaviour.Properties p) {
        super(p, Direction.UP, SHAPE, false, 0.1);
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
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (random.nextDouble() <= 0.95) {
            double d = pos.getX() + random.nextDouble() * 10.0 - 5.0;
            double e = pos.getY() + random.nextDouble() * 5.0;
            double f = pos.getZ() + random.nextDouble() * 10.0 - 5.0;
            level.addParticle((ParticleOptions) BGParticleTypes.SABLOSSOM, d, e, f, 0.0, 0.0, 0.0);
        }
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos blockpos = pos.relative(this.growthDirection.getOpposite());
        BlockState blockstate = level.getBlockState(blockpos);
        return !this.canAttachTo(blockstate) ? false : blockstate.is(this.getHeadBlock()) || blockstate.is(this.getBodyBlock()) || blockstate.is(BGBlocks.SABLE_LEAVES.get())
                || blockstate.isFaceSturdy(level, blockpos, this.growthDirection);
    }
}