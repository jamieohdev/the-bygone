package com.jamiedev.mod.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.jamiedev.mod.common.client.JamiesModClient;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
@Mixin(ItemInHandRenderer.class)
public abstract class MixinHeldItemRenderer {
    @Inject(method = "renderFirstPersonItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/HeldItemRenderer;renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", shift = At.Shift.BEFORE, ordinal = 1))
    public void swordblocking$blockingPosition(AbstractClientPlayer player, float tickDelta, float pitch, InteractionHand hand, float swingProgress, ItemStack item, float equipProgress, PoseStack matrices, MultiBufferSource vertexConsumers, int light, CallbackInfo ci) {
        if (!JamiesModClient.isWeaponBlocking(player))
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