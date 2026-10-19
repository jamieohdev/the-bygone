package com.jamiedev.bygone.core.mixin.client;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.core.registry.BGMobEffects;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.client.gui.Gui$HeartType")
public enum HeartTypeMixinFabric{
    BYGONE_HEART_HAUNTED(
            Bygone.id("hud/heart/haunted_full"),
            Bygone.id("hud/heart/haunted_full_blinking"),
            Bygone.id("hud/heart/haunted_half"),
            Bygone.id("hud/heart/haunted_half_blinking"),
            Bygone.id("hud/heart/haunted_hardcore_full"),
            Bygone.id("hud/heart/haunted_hardcore_full_blinking"),
            Bygone.id("hud/heart/haunted_hardcore_half"),
            Bygone.id("hud/heart/haunted_hardcore_half_blinking")
    );
    @Shadow HeartTypeMixinFabric(ResourceLocation full, ResourceLocation fullBlinking, ResourceLocation half, ResourceLocation halfBlinking, ResourceLocation hardcoreFull, ResourceLocation hardcoreBlinking, ResourceLocation hardcoreHalf, ResourceLocation hardcoreHalfBlinking){}



    @Inject(method = "forPlayer", at = @At("HEAD"), cancellable = true)
    private static void injectForPlayer(Player player, CallbackInfoReturnable<Object> cir) {
        if (player.hasEffect(BGMobEffects.HAUNTED.get())) {
            cir.setReturnValue(BYGONE_HEART_HAUNTED);
        }
    }
};