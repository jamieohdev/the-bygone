package com.jamiedev.bygone.common.effect;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

// Because access transformers weren't working.
public class PublicMobEffect extends MobEffect {
    public PublicMobEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    public PublicMobEffect(MobEffectCategory category, int color, ParticleOptions particle) {
        super(category, color, particle);
    }
}
