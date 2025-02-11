package com.jamiedev.bygone.client.renderer;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.client.JamiesModModelLayers;
import com.jamiedev.bygone.client.models.DuckieModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import com.jamiedev.bygone.common.entities.DuckEntity;

public class DuckieRenderer  extends MobRenderer<DuckEntity, DuckieModel<DuckEntity>> {
    private static final ResourceLocation TEXTURE = Bygone.getModId("textures/entity/duckie.png");

    public DuckieRenderer(EntityRendererProvider.Context context) {
        super(context, new DuckieModel<>(context.bakeLayer(JamiesModModelLayers.DUCKIE)), 0.3F);
    }

    @Override
    public ResourceLocation getTextureLocation(DuckEntity chickenEntity) {
        return TEXTURE;
    }

    protected float getAnimationProgress(DuckEntity chickenEntity, float f) {
        float g = Mth.lerp(f, chickenEntity.prevFlapProgress, chickenEntity.flapProgress);
        float h = Mth.lerp(f, chickenEntity.prevMaxWingDeviation, chickenEntity.maxWingDeviation);
        return (Mth.sin(g) + 1.0F) * h;
    }
}
