package com.jamiedev.bygone.client.renderer.entity;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.client.JamiesModModelLayers;
import com.jamiedev.bygone.client.models.AmoebaModel;
import com.jamiedev.bygone.client.renderer.entity.layers.AmoebaOuterLayer;
import com.jamiedev.bygone.common.entity.AmoebaEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class AmoebaRenderer extends MobRenderer<AmoebaEntity, AmoebaModel<AmoebaEntity>> {
    private static final ResourceLocation TEXTURE = Bygone.id("textures/entity/amoeba.png");

    public AmoebaRenderer(EntityRendererProvider.Context context) {
        super(context, new AmoebaModel<>(context.bakeLayer(JamiesModModelLayers.AMOEBA)), 0.5F);
        this.addLayer(new AmoebaOuterLayer<>(this, context.getModelSet()));
    }

    @Override
    public ResourceLocation getTextureLocation(AmoebaEntity chickenEntity) {
        return TEXTURE;
    }


}

