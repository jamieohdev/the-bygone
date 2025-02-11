package com.jamiedev.mod.common.client.renderer;

import com.jamiedev.mod.fabric.JamiesModFabric;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import com.jamiedev.mod.common.client.JamiesModModelLayers;
import com.jamiedev.mod.common.client.models.DuckieModel;
import com.jamiedev.mod.common.entities.DuckEntity;

public class DuckieRenderer  extends MobRenderer<DuckEntity, DuckieModel<DuckEntity>> {
    private static final ResourceLocation TEXTURE = JamiesModFabric.getModId("textures/entity/duckie.png");

    public DuckieRenderer(EntityRendererProvider.Context context) {
        super(context, new DuckieModel(context.bakeLayer(JamiesModModelLayers.DUCKIE)), 0.3F);
    }

    public ResourceLocation getTexture(DuckEntity chickenEntity) {
        return TEXTURE;
    }

    protected float getAnimationProgress(DuckEntity chickenEntity, float f) {
        float g = Mth.lerp(f, chickenEntity.prevFlapProgress, chickenEntity.flapProgress);
        float h = Mth.lerp(f, chickenEntity.prevMaxWingDeviation, chickenEntity.maxWingDeviation);
        return (Mth.sin(g) + 1.0F) * h;
    }
}
