package com.jamiedev.bygone.common.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.ConduitBlock;

public class CarapaceEffect extends MobEffect
{

    public CarapaceEffect() {
        super(MobEffectCategory.BENEFICIAL, 0x67CEEB);
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if (!entity.level().isClientSide && entity instanceof Player player) {
            player.addEffect(new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 200, 0, false, false, false));
            player.addEffect(new MobEffectInstance(MobEffects.CONDUIT_POWER, 200, 0, false, false, false));
        }
        return true;
    }
}
