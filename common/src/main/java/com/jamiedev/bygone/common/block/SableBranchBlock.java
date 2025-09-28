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
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SableBranchBlock extends GrowingPlantHeadBlock {
    public static final MapCodec<SableBranchBlock> CODEC = simpleCodec(SableBranchBlock::new);
    public static final VoxelShape SHAPE = Block.box((double)4.0F, (double)0.0F, (double)4.0F, (double)12.0F, (double)15.0F, (double)12.0F);

    public MapCodec<SableBranchBlock> codec() {
        return CODEC;
    }

    public SableBranchBlock(BlockBehaviour.Properties p_154864_) {
        super(p_154864_, Direction.UP, SHAPE, false, 0.1);
    }

    protected int getBlocksToGrowWhenBonemealed(RandomSource p_222649_) {
        return NetherVines.getBlocksToGrowWhenBonemealed(p_222649_);
    }

    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        Vec3 vec3 = new Vec3((double)0.25F, (double)0.05F, (double)0.25F);
        if (entity instanceof LivingEntity livingentity) {
            if (livingentity.hasEffect(MobEffects.MOVEMENT_SPEED)) {
                vec3 = new Vec3((double)0.5F, (double)0.25F, (double)0.5F);
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
}

