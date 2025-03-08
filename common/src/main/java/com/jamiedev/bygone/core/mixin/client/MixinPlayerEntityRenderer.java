package com.jamiedev.bygone.core.mixin.client;

import com.jamiedev.bygone.client.BygoneClient;
import com.jamiedev.bygone.common.item.VerdigrisBladeItem;
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
    private static void bygone$getArmPose(AbstractClientPlayer player, InteractionHand hand, CallbackInfoReturnable<HumanoidModel.ArmPose> cir) {

        ItemStack handStack = player.getItemInHand(hand);
        ItemStack offStack = player.getItemInHand(hand.equals(InteractionHand.MAIN_HAND) ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND);
        if ((handStack.getItem() instanceof VerdigrisBladeItem) && !BygoneClient.canWeaponBlock(player))
            return;

        if (BygoneClient.isWeaponBlocking(player)) {
            cir.setReturnValue(HumanoidModel.ArmPose.BLOCK);
        } else if (handStack.getItem() instanceof VerdigrisBladeItem &&
                (cir.getReturnValue() == HumanoidModel.ArmPose.ITEM || cir.getReturnValue() == HumanoidModel.ArmPose.BLOCK)) {
            cir.setReturnValue(HumanoidModel.ArmPose.EMPTY);
        }
    }
}