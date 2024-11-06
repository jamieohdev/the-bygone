package com.jamiedev.mod.client.renderer;

import com.jamiedev.mod.JamiesMod;
import com.jamiedev.mod.client.JamiesModModelLayers;
import com.jamiedev.mod.client.models.DuckieModel;
import com.jamiedev.mod.entities.DuckEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class DuckieRenderer  extends MobEntityRenderer<DuckEntity, DuckieModel<DuckEntity>> {
    private static final Identifier TEXTURE = JamiesMod.getModId("textures/entity/duckie.png");

    public DuckieRenderer(EntityRendererFactory.Context context) {
        super(context, new DuckieModel(context.getPart(JamiesModModelLayers.DUCKIE)), 0.3F);
    }

    public Identifier getTexture(DuckEntity chickenEntity) {
        return TEXTURE;
    }

    protected float getAnimationProgress(DuckEntity chickenEntity, float f) {
        float g = MathHelper.lerp(f, chickenEntity.prevFlapProgress, chickenEntity.flapProgress);
        float h = MathHelper.lerp(f, chickenEntity.prevMaxWingDeviation, chickenEntity.maxWingDeviation);
        return (MathHelper.sin(g) + 1.0F) * h;
    }
}
