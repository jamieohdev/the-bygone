package com.jamiedev.bygone.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.TallGrassBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class SpeedWheatBlock extends DoublePlantBlock
{
    public SpeedWheatBlock(Properties properties) {
        super(properties);
    }

    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        Vec3 vec3 = new Vec3((double)0.25F, (double)0.05F, (double)0.25F);
        if (!level.isClientSide && entity instanceof Player livingentity && !livingentity.isCreative()) {
            livingentity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 20, 2));
            vec3 = new Vec3((double)1.5F, (double)1.25F, (double)1.5F);
        }
        if (!level.isClientSide && entity instanceof Player livingentity && livingentity.isCreative()) {

        }
        if (!level.isClientSide && (entity instanceof LivingEntity livingentity)) {
                livingentity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 20, 2));
                vec3 = new Vec3((double)1.5F, (double)1.25F, (double)1.5F);
        }

    }
}
