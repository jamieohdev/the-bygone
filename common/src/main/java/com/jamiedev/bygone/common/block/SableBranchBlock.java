package com.jamiedev.bygone.common.block;

import com.jamiedev.bygone.core.registry.BGBlocks;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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

public class SableBranchBlock extends GrowingPlantHeadBlock {
    public static final VoxelShape SHAPE = Block.box(4.0F, 0.0F, 4.0F, 12.0F, 15.0F, 12.0F);
    public static final MapCodec<SableBranchBlock> CODEC = simpleCodec(SableBranchBlock::new);

    public SableBranchBlock(BlockBehaviour.Properties p_154864_) {
        super(p_154864_, Direction.UP, SHAPE, false, 0.00005);
    }

    public MapCodec<SableBranchBlock> codec() {
        return CODEC;
    }

    protected int getBlocksToGrowWhenBonemealed(RandomSource p_222649_) {
        return NetherVines.getBlocksToGrowWhenBonemealed(p_222649_);
    }

    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        Vec3 vec3 = new Vec3(0.85F, 0.75F, 0.85F);
        if (entity instanceof LivingEntity livingentity) {
            if (livingentity.hasEffect(MobEffects.MOVEMENT_SPEED)) {
                vec3 = new Vec3(0.95F, 0.85F, 0.95F);
            }
        }

        entity.makeStuckInBlock(state, vec3);
    }

    protected Block getBodyBlock() {
        return BGBlocks.SABLE_BRANCH_PLANT.get();
    }

    protected boolean canGrowInto(BlockState p_154869_) {
        return NetherVines.isValidGrowthState(p_154869_);
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos blockpos = pos.relative(this.growthDirection.getOpposite());
        BlockState blockstate = level.getBlockState(blockpos);
        return this.canAttachTo(blockstate) && (blockstate.is(this.getHeadBlock()) || blockstate.is(this.getBodyBlock()) || blockstate.is(BGBlocks.SABLE_LEAVES.get())
                || blockstate.isFaceSturdy(level, blockpos, this.growthDirection));
    }

    @Override
    public boolean isMaxAge(BlockState state) {
        return state.getValue(AGE) == 5;
    }
}

