package com.jamiedev.bygone.client.renderer.entity.layers;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.client.JamiesModModelLayers;
import com.jamiedev.bygone.client.models.PrimordialFishModelA;
import com.jamiedev.bygone.client.models.PrimordialFishModelB;
import com.jamiedev.bygone.common.entity.PrimordialFishEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.ColorableHierarchicalModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class PrimordialFishPatternLayer extends RenderLayer<PrimordialFishEntity, ColorableHierarchicalModel<PrimordialFishEntity>> {

    private static final Map<PrimordialFishEntity.Pattern, ResourceLocation> TEXTURE_LOOKUP = new HashMap<>();

    private final PrimordialFishModelA<PrimordialFishEntity> modelA;
    private final PrimordialFishModelB<PrimordialFishEntity> modelB;

    public PrimordialFishPatternLayer(RenderLayerParent<PrimordialFishEntity, ColorableHierarchicalModel<PrimordialFishEntity>> renderer, EntityModelSet modelSet) {
        super(renderer);
        this.modelA = new PrimordialFishModelA<>(modelSet.bakeLayer(JamiesModModelLayers.PRIMORDIAL_FISH_SMALL_PATTERN));
        this.modelB = new PrimordialFishModelB<>(modelSet.bakeLayer(JamiesModModelLayers.PRIMORDIAL_FISH_LARGE_PATTERN));
    }

    public static @NotNull ResourceLocation computeTexture(PrimordialFishEntity.Pattern fishPattern) {

        return Bygone.id("textures/entity/fish/primordial_" + fishPattern.base()
                .getTextureType() + "_pattern_" + (fishPattern.getRawId()) + ".png");
    }

    private static @NotNull ResourceLocation getCachedTextureLocation(PrimordialFishEntity.Pattern fishPattern) {
        return TEXTURE_LOOKUP.computeIfAbsent(fishPattern, PrimordialFishPatternLayer::computeTexture);
    }

    @Override
    public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight, PrimordialFishEntity livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        PrimordialFishEntity.Pattern fishPattern = livingEntity.getVariant();
        EntityModel<PrimordialFishEntity> model = switch (fishPattern.base()) {
            case SMALL, SMALL_GLOW -> this.modelA;
            case LARGE, LARGE_GLOW -> this.modelB;
        };
        boolean shouldGlow = fishPattern.base().glows();
        ResourceLocation texture = getCachedTextureLocation(fishPattern);
        int patternColor = livingEntity.getPatternColor().getTextureDiffuseColor();
        coloredCutoutModelCopyLayerRender(
                this.getParentModel(),
                model,
                texture,
                poseStack,
                buffer,
                shouldGlow ? LightTexture.pack(LightTexture.block(packedLight), 15) : packedLight,
                livingEntity,
                limbSwing,
                limbSwingAmount,
                ageInTicks,
                netHeadYaw,
                headPitch,
                partialTicks,
                patternColor
        );
    }
}