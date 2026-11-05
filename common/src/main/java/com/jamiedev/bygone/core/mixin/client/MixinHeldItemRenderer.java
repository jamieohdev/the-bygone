package com.jamiedev.bygone.core.mixin.client;

import com.jamiedev.bygone.client.BygoneClient;
import com.jamiedev.bygone.common.item.MaliciousWarHornItem;
import com.jamiedev.bygone.core.registry.BGDataComponentTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public abstract class MixinHeldItemRenderer {
    @Shadow
    @Final
    private Minecraft minecraft;
    @Shadow
    private ItemStack mainHandItem;
    @Shadow
    private ItemStack offHandItem;
    @Unique
    private int bygone$lastSelectedSlot = -1;

    @Inject(method = "tick", at = @At("HEAD"))
    private void bygone$keepHornSteady(CallbackInfo ci) {
        if (this.minecraft.player == null)
            return;
        int selectedSlot = this.minecraft.player.getInventory().selected;
        ItemStack mainHand = this.minecraft.player.getMainHandItem();
        ItemStack offHand = this.minecraft.player.getOffhandItem();
        if (selectedSlot == this.bygone$lastSelectedSlot && bygone$onlyHornDataChanged(this.mainHandItem, mainHand)) {
            this.mainHandItem = mainHand;
        }
        if (bygone$onlyHornDataChanged(this.offHandItem, offHand)) {
            this.offHandItem = offHand;
        }
        this.bygone$lastSelectedSlot = selectedSlot;
    }

    @Unique
    private static boolean bygone$onlyHornDataChanged(ItemStack previous, ItemStack current) {
        if (previous == current || !(previous.getItem() instanceof MaliciousWarHornItem) || !previous.is(current.getItem())) {
            return false;
        }
        ItemStack previousWithoutTimer = previous.copy();
        ItemStack currentWithoutTimer = current.copy();
        previousWithoutTimer.remove(BGDataComponentTypes.WAR_HORN.get());
        currentWithoutTimer.remove(BGDataComponentTypes.WAR_HORN.get());
        return ItemStack.matches(previousWithoutTimer, currentWithoutTimer);
    }

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
