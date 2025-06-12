package com.jamiedev.bygone.client.renderer;

import com.google.common.collect.Maps;
import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.client.JamiesModModelLayers;
import com.jamiedev.bygone.client.models.BigBeakModel;
import com.jamiedev.bygone.common.entity.BigBeakVariants;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.Util;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HorseRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.HorseArmorLayer;
import net.minecraft.client.renderer.entity.layers.SaddleLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import com.jamiedev.bygone.common.entity.BigBeakEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class BigBeakRenderer  extends MobRenderer<BigBeakEntity, BigBeakModel<BigBeakEntity>> {
    private static final ResourceLocation TEXTURE = Bygone.id("textures/entity/big_beak.png");

    private static final Map VARIANTS = (Map) Util.make(Maps.newEnumMap(BigBeakVariants.class), (p_349902_) -> {
        p_349902_.put(BigBeakVariants.NORMAL, Bygone.id("textures/entity/big_beak.png"));
        p_349902_.put(BigBeakVariants.TROPICAL, Bygone.id("textures/entity/big_beak/tropical.png"));
        p_349902_.put(BigBeakVariants.PEACHY, Bygone.id("textures/entity/big_beak/peachy.png"));
        p_349902_.put(BigBeakVariants.BLUEBILL, Bygone.id("textures/entity/big_beak/bluebill.png"));
        p_349902_.put(BigBeakVariants.FROSTY, Bygone.id("textures/entity/big_beak/frosty.png"));
        p_349902_.put(BigBeakVariants.NOMAD, Bygone.id("textures/entity/big_beak/nomad.png"));
        p_349902_.put(BigBeakVariants.SAVANNA, Bygone.id("textures/entity/big_beak/savanna.png"));
        p_349902_.put(BigBeakVariants.TRANS, Bygone.id("textures/entity/big_beak/trans.png"));
        p_349902_.put(BigBeakVariants.LESBIAN, Bygone.id("textures/entity/big_beak/lesbian.png"));
        p_349902_.put(BigBeakVariants.MLM, Bygone.id("textures/entity/big_beak/mlm.png"));
    });

    public BigBeakRenderer(EntityRendererProvider.Context context) {
        super(context, new BigBeakModel<>(context.bakeLayer(JamiesModModelLayers.BIG_BEAK)), 0.6F);
        this.addLayer(new SaddleLayer<>(this, new BigBeakModel<>(context.bakeLayer(JamiesModModelLayers.BIG_BEAK_SADDLE)), Bygone.id("textures/entity/big_beak_saddled.png")));
        this.addLayer(new BigBeakArmorFeatureRenderer(this, context.getModelSet()));
    }

    @NotNull
    @Override
    public ResourceLocation getTextureLocation(BigBeakEntity entity) {
        return (ResourceLocation) VARIANTS.get(entity.getVariant());
    }

    @Override
    protected float getBob(BigBeakEntity chickenEntity, float f) {
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
