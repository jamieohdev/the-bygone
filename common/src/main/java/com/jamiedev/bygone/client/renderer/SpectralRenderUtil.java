package com.jamiedev.bygone.client.renderer;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public final class SpectralRenderUtil {

    public static final float MINIMUM_ALPHA = 0.25F;

    private SpectralRenderUtil() {
    }

    public static float healthAlpha(Entity entity) {
        if (!(entity instanceof LivingEntity living) || living.getMaxHealth() <= 0.0F) {
            return 1.0F;
        }
        float fraction = Mth.clamp(living.getHealth() / living.getMaxHealth(), 0.0F, 1.0F);
        return MINIMUM_ALPHA + (1.0F - MINIMUM_ALPHA) * fraction;
    }

    public static int applyAlpha(int color, float alpha) {
        int alphaByte = (int) (((color >>> 24) & 0xFF) * Mth.clamp(alpha, 0.0F, 1.0F));
        return (alphaByte << 24) | (color & 0x00FFFFFF);
    }
}
