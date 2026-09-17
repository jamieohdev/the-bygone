package com.jamiedev.bygone.common.effect;

import com.jamiedev.bygone.core.init.JamiesModTag;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public class PlasmilkEffect extends MobEffect {

	protected static int TICK_DURATION = 20;

	public PlasmilkEffect(MobEffectCategory category, int color) {
		super(category, color);
	}

	@Override
	public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
		return duration % TICK_DURATION == 0;
	}

	@Override
	public boolean applyEffectTick(LivingEntity entity, int amplifier) {
		if (entity.level().isClientSide) return true;

		for (MobEffectInstance instance : entity.getActiveEffects()) {
			if (!instance.getEffect().is(JamiesModTag.IGNORES_PLASMILK)) {
				entity.removeEffect(instance.getEffect());
			}
		}
		return true;
	}

}
