package com.jamiedev.bygone.client.renderer.entity;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.client.JamiesModModelLayers;
import com.jamiedev.bygone.client.models.PestModel;
import com.jamiedev.bygone.common.entity.PeskyEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class PeskyRenderer extends MobRenderer<PeskyEntity, PestModel<PeskyEntity>> {
    private static final ResourceLocation TEXTURE = Bygone.id("textures/entity/pesky.png");

    public PeskyRenderer(EntityRendererProvider.Context context) {
        super(context, new PestModel<>(context.bakeLayer(JamiesModModelLayers.PESKY)), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(PeskyEntity chickenEntity) {
        return TEXTURE;
    }

    protected void scale(PeskyEntity entity, PoseStack poseStack, float partialTickTime) {
        poseStack.scale(1.2F, 1.2F, 1.2F);
    }

}
