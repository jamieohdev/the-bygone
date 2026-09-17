package com.jamiedev.bygone.core.mixin;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.core.registry.BGMobEffects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Gui;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Mixin(Gui.HeartType.class)
@Environment(EnvType.CLIENT)
@Debug(export = true)
public class HeartTypeMixinFabric {
    @Shadow
    @Final
    @Mutable
    private static Gui.HeartType[] $VALUES;

    @Unique
    private static final Gui.HeartType HAUNTED = stellarity$addVariant(
            "haunted",
            Bygone.id( "hud/heart/haunted_full"),
            Bygone.id( "hud/heart/haunted_full_blinking"),
            Bygone.id( "hud/heart/haunted_half"),
            Bygone.id( "hud/heart/haunted_half_blinking"),
            Bygone.id( "hud/heart/haunted_hardcore_full"),
            Bygone.id( "hud/heart/haunted_hardcore_full_blinking"),
            Bygone.id( "hud/heart/haunted_hardcore_half"),
            Bygone.id( "hud/heart/haunted_hardcore_half_blinking"));

    @Invoker("<init>")
    private static Gui.HeartType stellarity$invokeInit(String internalName, int internalId, ResourceLocation resourceLocation, ResourceLocation resourceLocation2, ResourceLocation resourceLocation3, ResourceLocation resourceLocation4, ResourceLocation resourceLocation5, ResourceLocation resourceLocation6, ResourceLocation resourceLocation7, ResourceLocation resourceLocation8) {
        throw new AssertionError();
    }

    @Unique
    private static Gui.HeartType stellarity$addVariant(String internalName, ResourceLocation resourceLocation, ResourceLocation resourceLocation2, ResourceLocation resourceLocation3, ResourceLocation resourceLocation4, ResourceLocation resourceLocation5, ResourceLocation resourceLocation6, ResourceLocation resourceLocation7, ResourceLocation resourceLocation8) {
        assert $VALUES != null;
        ArrayList<Gui.HeartType> list = new ArrayList<>(List.of($VALUES));
        Gui.HeartType heartType = stellarity$invokeInit(internalName, $VALUES.length, resourceLocation, resourceLocation2, resourceLocation3, resourceLocation4, resourceLocation5, resourceLocation6, resourceLocation7, resourceLocation8);
        list.add(heartType);
        $VALUES = list.toArray(new Gui.HeartType[]{});
        return heartType;
    }

    @Inject(method = "forPlayer", at = @At("HEAD"), cancellable = true)
    private static void injectForPlayer(Player player, CallbackInfoReturnable<Gui.HeartType> cir) {
        if (player.hasEffect(BGMobEffects.HAUNTED.get())) {
            cir.setReturnValue(HAUNTED);
        }
    }
};