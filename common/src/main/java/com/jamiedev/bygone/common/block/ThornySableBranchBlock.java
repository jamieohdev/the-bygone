package com.jamiedev.bygone.common.block;

import com.jamiedev.bygone.core.registry.BGBlocks;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ThornySableBranchBlock extends GrowingPlantHeadBlock {
    public static final VoxelShape SHAPE = Block.box(4.0F, 0.0F, 4.0F, 12.0F, 15.0F, 12.0F);
    public static final MapCodec<ThornySableBranchBlock> CODEC = simpleCodec(ThornySableBranchBlock::new);
    CactusBlock ref;

    public ThornySableBranchBlock(Properties p_154864_) {
        super(p_154864_, Direction.UP, SHAPE, false, 0.001);
    }

    public MapCodec<ThornySableBranchBlock> codec() {
        return CODEC;
    }

    protected int getBlocksToGrowWhenBonemealed(RandomSource p_222649_) {
        return NetherVines.getBlocksToGrowWhenBonemealed(p_222649_);
    }

    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        Vec3 vec3 = new Vec3(0.75F, 0.65F, 0.75F);
        if (entity instanceof LivingEntity livingentity) {
            if (livingentity.hasEffect(MobEffects.MOVEMENT_SPEED)) {
                vec3 = new Vec3(0.95F, 0.85F, 0.95F);
            }
        }
        entity.makeStuckInBlock(state, vec3);
        entity.hurt(level.damageSources().cactus(), 1.0F);
    }

    protected Block getBodyBlock() {
        return BGBlocks.THORNY_SABLE_BRANCH_PLANT.get();
    }

    protected boolean canGrowInto(BlockState p_154869_) {
        return NetherVines.isValidGrowthState(p_154869_);
    }

    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(BlockTags.DIRT) || state.is(Blocks.FARMLAND);
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos blockpos = pos.relative(this.growthDirection.getOpposite());
        BlockState blockstate = level.getBlockState(blockpos);
        return this.canAttachTo(blockstate) && (blockstate.is(this.getHeadBlock()) || blockstate.is(this.getBodyBlock()) || blockstate.is(BGBlocks.SABLE_LEAVES.get())
                || blockstate.isFaceSturdy(level, blockpos, this.growthDirection));
    }
}

