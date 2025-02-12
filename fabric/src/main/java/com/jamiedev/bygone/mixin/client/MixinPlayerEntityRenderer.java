package com.jamiedev.bygone.mixin.client;

import com.jamiedev.bygone.client.BygoneClientFabric;
import com.jamiedev.bygone.items.VerdigrisBladeItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerRenderer.class)
public abstract class MixinPlayerEntityRenderer {
    @Inject(at = @At(value = "RETURN"), method = "getArmPose", cancellable = true)
    @Environment(EnvType.CLIENT)
    private static void swordblocking$getArmPose(AbstractClientPlayer player, InteractionHand hand, CallbackInfoReturnable<HumanoidModel.ArmPose> cir) {

        ItemStack handStack = player.getItemInHand(hand);
        ItemStack offStack = player.getItemInHand(hand.equals(InteractionHand.MAIN_HAND) ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND);
        if ((handStack.getItem() instanceof VerdigrisBladeItem) && !BygoneClientFabric.canWeaponBlock(player))
            return;

        if (BygoneClientFabric.isWeaponBlocking(player)) {
            cir.setReturnValue(HumanoidModel.ArmPose.BLOCK);
        } else if (handStack.getItem() instanceof VerdigrisBladeItem &&
                (cir.getReturnValue() == HumanoidModel.ArmPose.ITEM || cir.getReturnValue() == HumanoidModel.ArmPose.BLOCK)) {
            cir.setReturnValue(HumanoidModel.ArmPose.EMPTY);
        }
    }
}