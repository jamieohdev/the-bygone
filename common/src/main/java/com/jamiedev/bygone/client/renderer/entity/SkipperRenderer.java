package com.jamiedev.bygone.client.renderer.entity;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.client.JamiesModModelLayers;
import com.jamiedev.bygone.client.models.SkipperModel;
import com.jamiedev.bygone.client.renderer.entity.layers.SkipperSaddleLayer;
import com.jamiedev.bygone.common.entity.SkipperEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class SkipperRenderer extends MobRenderer<SkipperEntity, SkipperModel> {
    private static final ResourceLocation TEXTURE = Bygone.id("textures/entity/skipper.png");

    public SkipperRenderer(EntityRendererProvider.Context rendererContext) {
        super(rendererContext, new SkipperModel(rendererContext.bakeLayer(JamiesModModelLayers.SKIPPER)), 0.7F);
        this.addLayer(new SkipperSaddleLayer(this, rendererContext.bakeLayer(JamiesModModelLayers.SKIPPER_SADDLE)));
    }

    @Override
    public ResourceLocation getTextureLocation(SkipperEntity skipper) {
        return TEXTURE;
    }
}
