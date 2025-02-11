package com.jamiedev.mod.mixin.client;


import com.jamiedev.mod.common.items.VerdigrisBladeItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(ItemRenderer.class)
public class MixinItemRenderer {

    @Inject(method = "renderStatic(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/world/level/Level;III)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/ItemRenderer;render(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IILnet/minecraft/client/resources/model/BakedModel;)V"))
    private void renderItemMixin(@Nullable LivingEntity entity, ItemStack stack, ItemDisplayContext renderMode, boolean leftHanded, PoseStack matrices, MultiBufferSource vertexConsumers,
                                 @Nullable Level world, int light, int overlay, int seed, CallbackInfo info) {
        if (entity != null && (renderMode == ItemDisplayContext.FIRST_PERSON_LEFT_HAND || renderMode == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND)
                && entity.isBlocking() && (stack.getItem() instanceof VerdigrisBladeItem)) {

           // matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(leftHanded ? -90F : 90F)); // POS 90 OPPOSITE OF DIR WE WANT BLOCKING NEG 90 POINT AWAY FROM PLAYER?
           // matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(leftHanded ? -45F : 45F)); // POS 90 towards player??
            matrices.mulPose(Axis.XP.rotationDegrees(0f));
            matrices.mulPose(Axis.YP.rotationDegrees(0f));
            matrices.mulPose(Axis.ZP.rotationDegrees(-20f));
            ;
        }
    }

    @Shadow
    public BakedModel getModel(ItemStack stack, @Nullable Level world, @Nullable LivingEntity entity, int seed) {
        return null;
    }
}
