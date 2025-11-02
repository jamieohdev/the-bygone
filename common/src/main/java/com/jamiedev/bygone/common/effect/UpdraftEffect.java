package com.jamiedev.bygone.common.effect;

import com.jamiedev.bygone.core.network.PacketHandler;
import com.jamiedev.bygone.core.network.UpdraftMovementS2C;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class UpdraftEffect extends MobEffect {

    public UpdraftEffect() {
        super(MobEffectCategory.BENEFICIAL, 0x87CEEB);
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if (!entity.level().isClientSide && entity instanceof Player player) {
            double upwardVelocity = 0.15 * (amplifier + 1);
            boolean isDescending = player.isShiftKeyDown();

            if (isDescending) {
                upwardVelocity = -0.08;
            }

            if (player instanceof ServerPlayer serverPlayer) {
                PacketHandler.sendTo(new UpdraftMovementS2C(upwardVelocity, isDescending), serverPlayer);

                if (serverPlayer.level() instanceof ServerLevel serverLevel) {
                    double x = serverPlayer.getX();
                    double y = serverPlayer.getY() - 0.5;
                    double z = serverPlayer.getZ();

                    for (int i = 0; i < 8; i++) {
                        double offsetX = (serverPlayer.getRandom().nextDouble() - 0.5) * 2.0;
                        double offsetZ = (serverPlayer.getRandom().nextDouble() - 0.5) * 2.0;
                        double offsetY = serverPlayer.getRandom().nextDouble() * 0.2 - 0.1;

                        serverLevel.sendParticles(
                                ParticleTypes.CLOUD,
                                x + offsetX,
                                y + offsetY,
                                z + offsetZ,
                                1,
                                0.0, 0.0, 0.0,
                                0.01
                        );
                    }

                    if (serverPlayer.tickCount % 5 == 0) {
                        for (int i = 0; i < 3; i++) {
                            double ringRadius = 1.5;
                            double angle = serverPlayer.getRandom().nextDouble() * 2 * Math.PI;
                            double ringX = x + Math.cos(angle) * ringRadius;
                            double ringZ = z + Math.sin(angle) * ringRadius;

                            serverLevel.sendParticles(
                                    ParticleTypes.CLOUD,
                                    ringX,
                                    y - 0.3,
                                    ringZ,
                                    1,
                                    0.1, 0.0, 0.1,
                                    0.0
                            );
                        }
                    }
                }
            }

            entity.resetFallDistance();
        }
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }
}