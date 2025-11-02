package com.jamiedev.bygone.client.renderer.entity;

import com.jamiedev.bygone.common.block.entity.AmphoraBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;

public class AmphoraBlockEntityRenderer implements BlockEntityRenderer<AmphoraBlockEntity> {

    private static final float WOBBLE_AMPLITUDE = 0.125F;

    public AmphoraBlockEntityRenderer(BlockEntityRendererProvider.Context context) {

    }

    public void render(AmphoraBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        poseStack.pushPose();
        Direction direction = blockEntity.getDirection();
        poseStack.translate(0.5F, 0.0F, (double) 0.5F);
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - direction.toYRot()));
        poseStack.translate(-0.5F, 0.0F, (double) -0.5F);
        AmphoraBlockEntity.WobbleStyle decoratedpotblockentity$wobblestyle = blockEntity.lastWobbleStyle;
        if (decoratedpotblockentity$wobblestyle != null && blockEntity.getLevel() != null) {
            float f = ((float) (blockEntity.getLevel().getGameTime() - blockEntity.wobbleStartedAtTick) + partialTick) / (float) decoratedpotblockentity$wobblestyle.duration;
            if (f >= 0.0F && f <= 1.0F) {
                if (decoratedpotblockentity$wobblestyle == AmphoraBlockEntity.WobbleStyle.POSITIVE) {
                    float f1 = 0.015625F;
                    float f2 = f * ((float) Math.PI * 2F);
                    float f3 = -1.5F * (Mth.cos(f2) + 0.5F) * Mth.sin(f2 / 2.0F);
                    poseStack.rotateAround(Axis.XP.rotation(f3 * 0.015625F), 0.5F, 0.0F, 0.5F);
                    float f4 = Mth.sin(f2);
                    poseStack.rotateAround(Axis.ZP.rotation(f4 * 0.015625F), 0.5F, 0.0F, 0.5F);
                } else {
                    float f5 = Mth.sin(-f * 3.0F * (float) Math.PI) * 0.125F;
                    float f6 = 1.0F - f;
                    poseStack.rotateAround(Axis.YP.rotation(f5 * f6), 0.5F, 0.0F, 0.5F);
                }
            }
        }

        poseStack.popPose();
    }

    private void renderSide(ModelPart modelPart, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay, Material material) {
        modelPart.render(poseStack, material.buffer(buffer, RenderType::entitySolid), packedLight, packedOverlay);
    }
}
