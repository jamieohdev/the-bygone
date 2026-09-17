package com.jamiedev.bygone.client.renderer.entity;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.client.JamiesModModelLayers;
import com.jamiedev.bygone.client.models.ArcaneMechanismModel;
import com.jamiedev.bygone.client.models.MurklingModel;
import com.jamiedev.bygone.common.entity.BygonePortalEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;

public class BygonePortalRenderer extends LivingEntityRenderer<BygonePortalEntity, ArcaneMechanismModel<BygonePortalEntity>> {
    private static final ResourceLocation TEXTURE = Bygone.id("textures/entity/arcane_mechanism.png");

    public BygonePortalRenderer(EntityRendererProvider.Context context) {
        super(context, new ArcaneMechanismModel<>(context.bakeLayer(JamiesModModelLayers.BYGONE_PORTAL)), 0);
    }

    @Override
    public void render(BygonePortalEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, 255);
        poseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(BygonePortalEntity bygonePortalEntity) {
        return TEXTURE;
    }

    @Override
    protected boolean shouldShowName(BygonePortalEntity entity) {
        return false;
    }
}
