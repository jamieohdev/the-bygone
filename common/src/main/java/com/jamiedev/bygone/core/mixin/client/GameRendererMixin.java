package com.jamiedev.bygone.core.mixin.client;

import com.jamiedev.bygone.client.renderer.effect.HauntingsEffectRenderer;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Shadow @Final Minecraft minecraft;

    @Inject(method = "renderLevel", at = @At(
        value = "INVOKE",
        shift = At.Shift.AFTER,
        target = "Lnet/minecraft/client/renderer/LevelRenderer;renderLevel(Lnet/minecraft/client/DeltaTracker;ZLnet/minecraft/client/Camera;Lnet/minecraft/client/renderer/GameRenderer;Lnet/minecraft/client/renderer/LightTexture;Lorg/joml/Matrix4f;Lorg/joml/Matrix4f;)V"
    ))
    private void bygone$postChainRender(DeltaTracker deltaTracker, CallbackInfo ci) {
        HauntingsEffectRenderer.getInstance()
            .render(minecraft, deltaTracker.getGameTimeDeltaTicks());
    }
}
