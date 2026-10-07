package com.jamiedev.bygone.client.renderer.entity;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.client.JamiesModModelLayers;
import com.jamiedev.bygone.client.models.ArcaneMechanismModel;
import com.jamiedev.bygone.common.entity.BygonePortalEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class BygonePortalRenderer extends EntityRenderer<BygonePortalEntity> {
    private static final ResourceLocation TEXTURE = Bygone.id("textures/entity/arcane_mechanism.png");

    private final ArcaneMechanismModel<BygonePortalEntity> model;
    public BygonePortalRenderer(EntityRendererProvider.Context context) {
        super(context);
        model = new ArcaneMechanismModel<>(context.bakeLayer(JamiesModModelLayers.BYGONE_PORTAL));
    }

    @Override
    public void render(BygonePortalEntity entity, float yaw, float partialTicks, PoseStack poses, MultiBufferSource buffer, int packedLight) {
        VertexConsumer consumer = buffer.getBuffer(this.model.renderType(TEXTURE));
        poses.pushPose();
        poses.scale(-1F, -1F, 1F);
        poses.translate(0F, -1.501F, 0F);
        this.model.setupAnim(entity, 0, 0, entity.tickCount + partialTicks, 0, 0);
        this.model.renderToBuffer(poses, consumer, 255, OverlayTexture.NO_OVERLAY, -1);
        poses.popPose();
        super.render(entity, yaw, partialTicks, poses, buffer, 255);
    }

    @Override
    public ResourceLocation getTextureLocation(BygonePortalEntity bygonePortalEntity) {
        return TEXTURE;
    }
}
