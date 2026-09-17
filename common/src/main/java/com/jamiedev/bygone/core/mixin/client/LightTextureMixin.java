package com.jamiedev.bygone.core.mixin.client;

import com.jamiedev.bygone.client.renderer.effect.HauntingsEffectRenderer;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.LightTexture;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// https://github.com/Alchemists-Of-Yore/No-Mans-Land/blob/alchemists-tools/src/main/java/com/farcr/nomansland/common/mixin/client/LightTextureMixin.java
// cappin we love you
@Mixin(LightTexture.class)
public class LightTextureMixin {
    @ModifyArg(method = "updateLightTexture",
        at = @At(
            value = "INVOKE",
            target = "Lorg/joml/Vector3f;add(Lorg/joml/Vector3fc;)Lorg/joml/Vector3f;"
        ),
        index = 0
    )
    private Vector3fc bygone$hauntingsMoonDarkenSkyLight(Vector3fc v, @Local(ordinal = 0) int skyLight, @Local(ordinal = 2) Vector3f skyLightColor) {
        HauntingsEffectRenderer.getInstance().modifySkyLightColor(skyLightColor, skyLight);
        return skyLightColor;
    }

    @Inject(method = "updateLightTexture",
        at = @At(
            value = "INVOKE",
            target = "Lorg/joml/Vector3f;set(FFF)Lorg/joml/Vector3f;",
            shift = At.Shift.AFTER
        )
    )
    private void bygone$hauntingsDarkenBlockLight(float partialTicks, CallbackInfo ci, @Local(ordinal = 1) int blockLight, @Local(ordinal = 1) Vector3f blockLightColor) {
        HauntingsEffectRenderer.getInstance().modifyBlockLightColor(blockLightColor, blockLight);
    }

    @ModifyArg(method = "updateLightTexture",
        at = @At(
            value = "INVOKE",
            target = "Lorg/joml/Vector3f;lerp(Lorg/joml/Vector3fc;F)Lorg/joml/Vector3f;",
            ordinal = 2
        ),
        index = 1
    )
    private float bygone$hauntingsDarkenAmbientLight(float ambientLight) {
        return HauntingsEffectRenderer.getInstance().modifyAmbientLightFactor(ambientLight);
    }
}
