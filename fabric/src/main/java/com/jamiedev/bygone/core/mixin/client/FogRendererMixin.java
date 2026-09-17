package com.jamiedev.bygone.core.mixin.client;

import com.jamiedev.bygone.client.renderer.effect.FogEffectRenderer;
import com.jamiedev.bygone.core.registry.BGDimensions;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FogRenderer.class)
public abstract class FogRendererMixin {

    @Inject(method = "setupFog", at = @At("TAIL"))
    private static void bygone$modifyFogFabric(
        Camera camera, FogRenderer.FogMode fogMode, float farPlaneDistance,
        boolean shouldCreateFog, float partialTick, CallbackInfo ci,
        @Local FogRenderer.FogData fogData
    ) {
        Level level = camera.getEntity().level();
        if (!level.dimension().equals(BGDimensions.BYGONE_LEVEL_KEY)) return;

        RenderSystem.setShaderFogEnd(fogData.end * FogEffectRenderer.getDefaultFogMultiplier());
    }
}
