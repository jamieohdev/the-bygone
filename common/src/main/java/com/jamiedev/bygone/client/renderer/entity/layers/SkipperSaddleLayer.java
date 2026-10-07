package com.jamiedev.bygone.client.renderer.entity.layers;

import com.jamiedev.bygone.client.models.SkipperModel;
import com.jamiedev.bygone.common.entity.SkipperEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;

public class SkipperSaddleLayer extends RenderLayer<SkipperEntity, SkipperModel> {
    private final ModelPart saddlePart;

    public SkipperSaddleLayer(RenderLayerParent<SkipperEntity, SkipperModel> renderer, ModelPart rootPart) {
        super(renderer);
        this.saddlePart = rootPart.getChild("Base");
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, SkipperEntity skipper,
                       float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYawDegrees, float headPitchDegrees) {
        if (skipper.isSaddled()) {
            this.saddlePart.copyFrom(this.getParentModel().getBasePart());
            VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(this.getTextureLocation(skipper)));
            this.saddlePart.render(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY);
        }
    }
}
