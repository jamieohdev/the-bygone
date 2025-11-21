package com.jamiedev.bygone.client.renderer.entity;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.client.JamiesModModelLayers;
import com.jamiedev.bygone.client.models.PrimordialFishModelA;
import com.jamiedev.bygone.client.models.PrimordialFishModelB;
import com.jamiedev.bygone.client.renderer.entity.layers.PrimordialFishPatternLayer;
import com.jamiedev.bygone.common.entity.PrimordialFishEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.ColorableHierarchicalModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class PrimordialFishRenderer extends MobRenderer<PrimordialFishEntity, ColorableHierarchicalModel<PrimordialFishEntity>> {
    private static final ResourceLocation MODEL_A_TEXTURE = Bygone.id("textures/entity/fish/primordial_a.png");
    private static final ResourceLocation MODEL_B_TEXTURE = Bygone.id("textures/entity/fish/primordial_b.png");
    private final ColorableHierarchicalModel<PrimordialFishEntity> modelA = this.getModel();
    private final ColorableHierarchicalModel<PrimordialFishEntity> modelB;

    public PrimordialFishRenderer(EntityRendererProvider.Context context) {
        super(
                context,
                new PrimordialFishModelA<>(context.bakeLayer(JamiesModModelLayers.PRIMORDIAL_FISH_SMALL)),
                0.15F
        );
        this.modelB = new PrimordialFishModelB<>(context.bakeLayer(JamiesModModelLayers.PRIMORDIAL_FISH_LARGE));
        this.addLayer(new PrimordialFishPatternLayer(this, context.getModelSet()));
    }

    public @NotNull ResourceLocation getTextureLocation(PrimordialFishEntity entity) {
        return switch (entity.getVariant().base()) {
            case SMALL, SMALL_GLOW -> MODEL_A_TEXTURE;
            case LARGE, LARGE_GLOW -> MODEL_B_TEXTURE;
        };
    }

    @Override
    public void render(PrimordialFishEntity entity, float entityYaw, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight) {
        ColorableHierarchicalModel<PrimordialFishEntity> model = switch (entity.getVariant().base()) {
            case SMALL, SMALL_GLOW -> this.modelA;
            case LARGE, LARGE_GLOW -> this.modelB;
        };

        this.model = model;
        model.setColor(entity.getBaseColor().getTextureDiffuseColor());
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
        model.setColor(-1);
    }

    @Override
    protected void setupRotations(@NotNull PrimordialFishEntity entity, @NotNull PoseStack poseStack, float bob, float yBodyRot, float partialTick, float scale) {
        super.setupRotations(entity, poseStack, bob, yBodyRot, partialTick, scale);
        float f = 4.3F * Mth.sin(0.6F * bob);
        poseStack.mulPose(Axis.YP.rotationDegrees(f));
        if (!entity.isInWater()) {
            poseStack.translate(0.2F, 0.1F, 0.0F);
            poseStack.mulPose(Axis.ZP.rotationDegrees(90.0F));
        }

    }
}