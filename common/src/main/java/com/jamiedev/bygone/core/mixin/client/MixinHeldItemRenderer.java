package com.jamiedev.bygone.core.mixin.client;

import com.jamiedev.bygone.client.BygoneClient;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public abstract class MixinHeldItemRenderer {
    @Inject(method = "renderArmWithItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;renderItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", shift = At.Shift.BEFORE, ordinal = 1))
    public void bygone$blockingPosition(AbstractClientPlayer player, float tickDelta, float pitch, InteractionHand hand, float swingProgress, ItemStack item, float equipProgress, PoseStack matrices, MultiBufferSource vertexConsumers, int light, CallbackInfo ci) {
        if (!BygoneClient.isWeaponBlocking(player))
            return;
        boolean bl = hand == InteractionHand.MAIN_HAND;
        HumanoidArm arm = bl ? player.getMainArm() : player.getMainArm().getOpposite();
        int k = arm == HumanoidArm.RIGHT ? 1 : -1;
        matrices.translate(k * -0.14142136F, 0.08F, 0.14142136F);
        matrices.mulPose(Axis.XP.rotationDegrees(-102.25F));
        matrices.mulPose(Axis.YP.rotationDegrees(k * 13.365F));
        matrices.mulPose(Axis.ZP.rotationDegrees(k * 78.05F));
    }
}