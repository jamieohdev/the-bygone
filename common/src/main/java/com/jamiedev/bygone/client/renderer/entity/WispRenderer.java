package com.jamiedev.bygone.client.renderer.entity;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.client.JamiesModModelLayers;
import com.jamiedev.bygone.client.models.WispModel;
import com.jamiedev.bygone.common.entity.WispEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class WispRenderer extends MobRenderer<WispEntity, WispModel<WispEntity>> {
    private static final ResourceLocation TEXTURE = Bygone.id("textures/entity/wisp.png");


    public WispRenderer(EntityRendererProvider.Context context) {
        super(context, new WispModel<>(context.bakeLayer(JamiesModModelLayers.WISP)), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(WispEntity lithyEntity) {
        return TEXTURE;
    }
}
