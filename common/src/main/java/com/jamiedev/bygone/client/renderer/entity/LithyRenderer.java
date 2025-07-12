package com.jamiedev.bygone.client.renderer.entity;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.client.JamiesModModelLayers;
import com.jamiedev.bygone.client.models.GlareModel;
import com.jamiedev.bygone.client.models.LithyModel;
import com.jamiedev.bygone.common.entity.LithyEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class LithyRenderer extends MobRenderer<LithyEntity, LithyModel<LithyEntity>>
{
    private static final ResourceLocation TEXTURE = Bygone.id("textures/entity/lithy.png");


    public LithyRenderer(EntityRendererProvider.Context context) {
        super(context, new LithyModel<>(context.bakeLayer(JamiesModModelLayers.LITHY)), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(LithyEntity lithyEntity) {
        return TEXTURE;
    }
}
