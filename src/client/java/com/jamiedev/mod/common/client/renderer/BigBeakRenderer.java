package com.jamiedev.mod.common.client.renderer;

import com.jamiedev.mod.fabric.JamiesModFabric;
import com.jamiedev.mod.common.client.JamiesModModelLayers;
import com.jamiedev.mod.common.client.models.BigBeakModel;
import com.jamiedev.mod.common.entities.BigBeakEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.HorseEntityRenderer;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.HorseArmorFeatureRenderer;
import net.minecraft.client.render.entity.feature.SaddleFeatureRenderer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class BigBeakRenderer  extends MobEntityRenderer<BigBeakEntity, BigBeakModel<BigBeakEntity>> {
    private static final Identifier TEXTURE = JamiesModFabric.getModId("textures/entity/big_beak.png");
    HorseEntityRenderer ref;
    HorseArmorFeatureRenderer ref2;
    public BigBeakRenderer(EntityRendererFactory.Context context) {
        super(context, new BigBeakModel<>(context.getPart(JamiesModModelLayers.BIG_BEAK)), 0.6F);
        this.addFeature(new SaddleFeatureRenderer<>(this, new BigBeakModel<>(context.getPart(JamiesModModelLayers.BIG_BEAK_SADDLE)), JamiesModFabric.getModId("textures/entity/big_beak_saddled.png")));
        this.addFeature(new BigBeakArmorFeatureRenderer(this, context.getModelLoader()));
    }

    public Identifier getTexture(BigBeakEntity chickenEntity) {
        return TEXTURE;
    }

    protected float getAnimationProgress(BigBeakEntity chickenEntity, float f) {
        float g = MathHelper.lerp(f, chickenEntity.prevFlapProgress, chickenEntity.flapProgress);
        float h = MathHelper.lerp(f, chickenEntity.prevMaxWingDeviation, chickenEntity.maxWingDeviation);
        return (MathHelper.sin(g) + 1.0F) * h;

    }
}
