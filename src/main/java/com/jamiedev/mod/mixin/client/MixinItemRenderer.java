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

    @Inject(method = "Lnet/minecraft/client/render/item/ItemRenderer;renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/world/World;III)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/ItemRenderer;renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V"))
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
