package com.jamiedev.bygone.core.mixin.client;

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

import java.util.Arrays;

// randomly generated priority for hopefully consistent injection order
// ...while it wouldn't be the end of the world in this case, it's good practice
@Mixin(value = Gui.HeartType.class, priority = 780849458)
@Environment(EnvType.CLIENT)
public class HeartTypeMixinFabric {
    @Shadow @Final @Mutable private static Gui.HeartType[] $VALUES;
    @Invoker("<init>")
    private static Gui.HeartType bygone$init(String name, int id, ResourceLocation full, ResourceLocation fullBlinking, ResourceLocation half, ResourceLocation halfBlinking, ResourceLocation hardcoreFull, ResourceLocation hardcoreBlinking, ResourceLocation hardcoreHalf, ResourceLocation hardcoreHalfBlinking) {
        throw new AssertionError();
    }

    @Unique private static final Gui.HeartType HAUNTED;
    static{
        $VALUES = Arrays.copyOf($VALUES, $VALUES.length + 1);
        $VALUES[$VALUES.length - 1] = HAUNTED = bygone$init("HAUNTED", $VALUES.length - 1,
                Bygone.id("hud/heart/haunted_full"),
                Bygone.id("hud/heart/haunted_full_blinking"),
                Bygone.id("hud/heart/haunted_half"),
                Bygone.id("hud/heart/haunted_half_blinking"),
                Bygone.id("hud/heart/haunted_hardcore_full"),
                Bygone.id("hud/heart/haunted_hardcore_full_blinking"),
                Bygone.id("hud/heart/haunted_hardcore_half"),
                Bygone.id("hud/heart/haunted_hardcore_half_blinking"));
    }

    @Inject(method = "forPlayer", at = @At("HEAD"), cancellable = true)
    private static void injectForPlayer(Player player, CallbackInfoReturnable<Gui.HeartType> cir) {
        if (player.hasEffect(BGMobEffects.HAUNTED.get())) {
            cir.setReturnValue(HAUNTED);
        }
    }
};