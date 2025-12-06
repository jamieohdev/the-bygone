package com.jamiedev.bygone.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class SpeedWheatBlock extends DoublePlantBlock {
    public static final int SPEED_WHEAT_SPEED_DURATION = 20;
    public static final int SPEED_WHEAT_SPEED_AMPLIFIER = 2;
    public SpeedWheatBlock(Properties properties) {
        super(properties);
    }

    protected void entityInside(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull Entity entity) {
        if (!level.isClientSide && (entity instanceof LivingEntity livingentity)) {
            livingentity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, SPEED_WHEAT_SPEED_DURATION, SPEED_WHEAT_SPEED_AMPLIFIER));
        }

    }
}
