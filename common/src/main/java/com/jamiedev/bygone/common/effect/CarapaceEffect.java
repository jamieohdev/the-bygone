package com.jamiedev.bygone.common.effect;

import net.minecraft.tags.FluidTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class CarapaceEffect extends MobEffect {
    public static final int CARAPACE_EFFECT_LENGTH_BASE = 2;
    public static final int CARAPACE_EFFECT_LENGTH_PER_LEVEL = 200;

    public CarapaceEffect() {
        super(MobEffectCategory.BENEFICIAL, 0x67CEEB);
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if (!entity.level().isClientSide && entity instanceof Player player && !player.isEyeInFluid(FluidTags.WATER)) {
            player.addEffect(new MobEffectInstance(
                    MobEffects.DOLPHINS_GRACE,
                    CARAPACE_EFFECT_LENGTH_BASE + CARAPACE_EFFECT_LENGTH_PER_LEVEL * (amplifier + 1),
                    0,
                    true,
                    false,
                    false
            ));
        }
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }
}
