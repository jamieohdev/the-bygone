package com.jamiedev.bygone.client.renderer.entity;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.client.JamiesModModelLayers;
import com.jamiedev.bygone.client.models.SabeastModel;
import com.jamiedev.bygone.client.renderer.entity.layers.SabeastEyesLayer;
import com.jamiedev.bygone.common.entity.SabeastEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class SabeastRenderer extends MobRenderer<SabeastEntity, SabeastModel<SabeastEntity>>
{
    private static final ResourceLocation TEXTURE = Bygone.id("textures/entity/sabeast.png");

    public SabeastRenderer(EntityRendererProvider.Context context) {
        super(context, new SabeastModel<>(context.bakeLayer(JamiesModModelLayers.SABEAST)), 0.5F);
        this.addLayer(new SabeastEyesLayer<>(this));
    }

    @Override
    public ResourceLocation getTextureLocation(SabeastEntity SabeastEntity) {
        return TEXTURE;
    }
}
