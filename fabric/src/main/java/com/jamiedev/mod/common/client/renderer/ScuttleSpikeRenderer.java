package com.jamiedev.mod.common.client.renderer;

import com.jamiedev.mod.fabric.JamiesModFabric;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import com.jamiedev.mod.common.client.JamiesModModelLayers;
import com.jamiedev.mod.common.client.models.ScuttleSpikeModel;
import com.jamiedev.mod.common.entities.projectile.ScuttleSpikeEntity;

public class ScuttleSpikeRenderer  extends EntityRenderer<ScuttleSpikeEntity> {
    public static final ResourceLocation TEXTURE = JamiesModFabric.getModId("textures/entity/scuttlespike.png");
    private final ScuttleSpikeModel model;

    public ScuttleSpikeRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new ScuttleSpikeModel(context.bakeLayer(JamiesModModelLayers.SCUTTLE_SPIKE));
    }

    public void render(ScuttleSpikeEntity tridentEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
        matrixStack.pushPose();
        matrixStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(g, tridentEntity.yRotO, tridentEntity.getYRot()) - 90.0F));
        matrixStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(g, tridentEntity.xRotO, tridentEntity.getXRot()) + 90.0F));
        VertexConsumer vertexConsumer = ItemRenderer.getFoilBufferDirect(vertexConsumerProvider, this.model.renderType(this.getTexture(tridentEntity)), false, false);
        this.model.renderToBuffer(matrixStack, vertexConsumer, i, OverlayTexture.NO_OVERLAY);
        matrixStack.popPose();
        super.render(tridentEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    public ResourceLocation getTexture(ScuttleSpikeEntity tridentEntity) {
        return TEXTURE;
    }
}