package com.jamiedev.bygone.common.effect;


import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class SiphoningEffect extends MobEffect {

    protected static final int ROUNDING_TO = 20;

    public SiphoningEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyEffectTick(@NotNull LivingEntity entity, int amplifier) {
        super.applyEffectTick(entity, amplifier);

        if (!entity.level().isClientSide()) {
            int currentAir = entity.getAirSupply();
            int newAir = currentAir + (amplifier + 1) * 3 + 3;
            entity.setAirSupply(Math.min(newAir, entity.getMaxAirSupply()));
        }
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }
}

