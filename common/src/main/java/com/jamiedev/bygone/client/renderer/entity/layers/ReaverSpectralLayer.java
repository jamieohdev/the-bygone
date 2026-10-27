package com.jamiedev.bygone.client.renderer.entity.layers;

import com.jamiedev.bygone.client.models.ReaverModel;
import com.jamiedev.bygone.common.entity.ReaverEntity;
import com.jamiedev.bygone.core.registry.BGRenderTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.jetbrains.annotations.NotNull;

public class ReaverSpectralLayer extends RenderLayer<ReaverEntity, ReaverModel<ReaverEntity>> {
    public ReaverSpectralLayer(RenderLayerParent<ReaverEntity, ReaverModel<ReaverEntity>> parent) {
        super(parent);
    }

    @Override
    public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight,
                       @NotNull ReaverEntity reaverEntity, float limbSwing, float limbSwingAmount,
                       float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (reaverEntity.isInvisible()) {
            return;
        }
        float elapsed = reaverEntity.getAttackPhaseTicks(partialTicks);
        float opacity = 1.0F;
        boolean lunging = false;
        switch (reaverEntity.getAttackPhase()) {
            case LUNGE -> lunging = true;
            case FLURRY -> { }
            case RECOVERY -> {
                if (reaverEntity.getRecoverySource() != ReaverEntity.AttackPhase.FLURRY) {
                    return;
                }
                opacity = Math.max(0, 1.0F - elapsed / 6.0F);
                elapsed += ReaverEntity.FLURRY_TICKS;
            }
            default -> {
                return;
            }
        }
        if (opacity <= 0) {
            return;
        }
        VertexConsumer vertexConsumer = buffer.getBuffer(BGRenderTypes.reaverAfterimages(this.getTextureLocation(reaverEntity)));
        if (lunging) {
            this.getParentModel().renderLungeAfterimages(poseStack, vertexConsumer,
                    packedLight, OverlayTexture.NO_OVERLAY, opacity);
        } else {
            this.getParentModel().renderClawAfterimages(poseStack, vertexConsumer,
                    packedLight, OverlayTexture.NO_OVERLAY, elapsed, reaverEntity.getClawPattern(), opacity);
        }
    }
}
