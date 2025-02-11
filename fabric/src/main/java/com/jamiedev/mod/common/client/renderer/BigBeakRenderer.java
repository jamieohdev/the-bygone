package com.jamiedev.mod.common.client.renderer;

import com.jamiedev.mod.common.entities.FungalParentEntity;
import com.jamiedev.mod.fabric.JamiesModFabric;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HorseRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.HorseArmorLayer;
import net.minecraft.client.renderer.entity.layers.SaddleLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import com.jamiedev.mod.common.client.JamiesModModelLayers;
import com.jamiedev.mod.common.client.models.BigBeakModel;
import com.jamiedev.mod.common.entities.BigBeakEntity;

public class BigBeakRenderer  extends MobRenderer<BigBeakEntity, BigBeakModel<BigBeakEntity>> {
    private static final ResourceLocation TEXTURE = JamiesModFabric.getModId("textures/entity/big_beak.png");
    HorseRenderer ref;
    HorseArmorLayer ref2;
    public BigBeakRenderer(EntityRendererProvider.Context context) {
        super(context, new BigBeakModel<>(context.bakeLayer(JamiesModModelLayers.BIG_BEAK)), 0.6F);
        this.addLayer(new SaddleLayer<>(this, new BigBeakModel<>(context.bakeLayer(JamiesModModelLayers.BIG_BEAK_SADDLE)), JamiesModFabric.getModId("textures/entity/big_beak_saddled.png")));
        this.addLayer(new BigBeakArmorFeatureRenderer(this, context.getModelSet()));
    }

    public ResourceLocation getTexture(BigBeakEntity chickenEntity) {
        return TEXTURE;
    }

    protected float getAnimationProgress(BigBeakEntity chickenEntity, float f) {
        float g = Mth.lerp(f, chickenEntity.prevFlapProgress, chickenEntity.flapProgress);
        float h = Mth.lerp(f, chickenEntity.prevMaxWingDeviation, chickenEntity.maxWingDeviation);
        return (Mth.sin(g) + 1.0F) * h;

    }

    @Override
    protected void scale(BigBeakEntity slimeEntity, PoseStack matrixStack, float f) {

        if (slimeEntity.isBaby())
        {
            matrixStack.scale(0.5F, 0.5F, 0.5F);
        }
    }
}
