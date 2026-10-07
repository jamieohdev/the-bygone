package com.jamiedev.bygone.core.mixin.client;

import com.jamiedev.bygone.client.renderer.LithoFluidFog;
import com.jamiedev.bygone.client.renderer.effect.FogEffectRenderer;
import com.jamiedev.bygone.core.registry.BGDimensions;
import com.jamiedev.bygone.core.registry.BGFogTypes;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FogType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FogRenderer.class)
public abstract class FogRendererMixin {

    @Shadow private static float fogRed;
    @Shadow private static float fogGreen;
    @Shadow private static float fogBlue;

    @Inject(method = "setupFog", at = @At("TAIL"))
    private static void bygone$modifyFogFabric(
        Camera camera, FogRenderer.FogMode fogMode, float farPlaneDistance,
        boolean shouldCreateFog, float partialTick, CallbackInfo ci,
        @Local FogRenderer.FogData fogData
    ) {
        Level level = camera.getEntity().level();

        if (!level.dimension().equals(BGDimensions.BYGONE_LEVEL_KEY)) return;
        if (camera.getFluidInCamera() != FogType.NONE) return;

        RenderSystem.setShaderFogEnd(fogData.end * FogEffectRenderer.getDefaultFogMultiplier());
    }

    @Inject(method = "setupColor", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;clearColor(FFFF)V", shift = At.Shift.BEFORE))
    private static void bygone$modifySetColorFabric(Camera activeRenderInfo, float partialTicks, ClientLevel level, int renderDistanceChunks, float bossColorModifier, CallbackInfo ci) {
        if (activeRenderInfo.getFluidInCamera() != BGFogTypes.LITHO)
        {
            return;
        }
        fogRed = LithoFluidFog.lithoFogColor.x;
        fogGreen = LithoFluidFog.lithoFogColor.y;
        fogBlue = LithoFluidFog.lithoFogColor.z;
    }

    @Inject(method = "setupFog", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderFogStart(F)V", shift = At.Shift.BEFORE))
    private static void bygone$modifySetFogFabric(
            Camera camera, FogRenderer.FogMode fogMode, float farPlaneDistance,
            boolean shouldCreateFog, float partialTick, CallbackInfo ci,
            @Local FogRenderer.FogData fogData
    ) {
        FogType fogType = camera.getFluidInCamera();
        if (fogType != BGFogTypes.LITHO) {
            return;
        }

        fogData.start = LithoFluidFog.lithoFogStart;
        fogData.end = LithoFluidFog.lithoFogEnd;

    }
}
