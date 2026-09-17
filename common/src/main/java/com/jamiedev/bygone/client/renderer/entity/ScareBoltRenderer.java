package com.jamiedev.bygone.client.renderer.entity;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.common.entity.projectile.ScareBoltEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class ScareBoltRenderer extends EntityRenderer<ScareBoltEntity> {
    private static final ResourceLocation TEXTURE = Bygone.id("textures/entity/scare_bolt.png");
    private static final RenderType RENDER_TYPE = RenderType.entityTranslucent(TEXTURE);

    public ScareBoltRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected int getBlockLightLevel(@NotNull ScareBoltEntity entity, @NotNull BlockPos pos) {
        return 15;
    }

    @Override
    public void render(@NotNull ScareBoltEntity entity, float entityYaw, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        poseStack.scale(0.6F, 0.6F, 0.6F);
        poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
        PoseStack.Pose pose = poseStack.last();
        VertexConsumer consumer = buffer.getBuffer(RENDER_TYPE);
        vertex(consumer, pose, packedLight, -0.5F, -0.25F, 0, 1);
        vertex(consumer, pose, packedLight, 0.5F, -0.25F, 1, 1);
        vertex(consumer, pose, packedLight, 0.5F, 0.75F, 1, 0);
        vertex(consumer, pose, packedLight, -0.5F, 0.75F, 0, 0);
        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    private static void vertex(VertexConsumer consumer, PoseStack.Pose pose, int packedLight, float x, float y, int u, int v) {
        consumer.addVertex(pose, x, y, 0.0F)
                .setColor(-1)
                .setUv((float) u, (float) v)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(packedLight)
                .setNormal(pose, 0.0F, 1.0F, 0.0F);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull ScareBoltEntity entity) {
        return TEXTURE;
    }
}
