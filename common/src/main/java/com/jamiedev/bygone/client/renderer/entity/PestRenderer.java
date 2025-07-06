package com.jamiedev.bygone.client.renderer.entity;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.client.JamiesModModelLayers;
import com.jamiedev.bygone.client.models.PestModel;
import com.jamiedev.bygone.common.entity.PestEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class PestRenderer extends MobRenderer<PestEntity, PestModel<PestEntity>> {
    private static final ResourceLocation TEXTURE = Bygone.id("textures/entity/pest.png");

    public PestRenderer(EntityRendererProvider.Context context) {
        super(context, new PestModel<>(context.bakeLayer(JamiesModModelLayers.PEST)), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(PestEntity chickenEntity) {
        return TEXTURE;
    }

}
