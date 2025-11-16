package com.jamiedev.bygone.client.renderer.entity;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.client.JamiesModModelLayers;
import com.jamiedev.bygone.client.models.CoelacanthModel;
import com.jamiedev.bygone.common.entity.CoelacanthEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.SalmonRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class CoelacanthRenderer extends MobRenderer<CoelacanthEntity, CoelacanthModel<CoelacanthEntity>> {
    private static final ResourceLocation TEXTURE = Bygone.id("textures/entity/fish/coelacanth.png");

    public CoelacanthRenderer(EntityRendererProvider.Context context) {
        super(context, new CoelacanthModel<>(context.bakeLayer(JamiesModModelLayers.COELACANTH)), 0.6F);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull CoelacanthEntity coelacanthEntity) {
        return TEXTURE;
    }

    @Override
    protected void setupRotations(@NotNull CoelacanthEntity coelacanth, @NotNull PoseStack matrixStack, float bob, float yBodyRot, float partialTick, float scale) {
        super.setupRotations(coelacanth, matrixStack, bob, yBodyRot, partialTick, scale);
        float rotationScaleFactor = 1.0F;
        float rotationSpeedFactor = 1.0F;
        if (!coelacanth.isInWater()) {
            rotationScaleFactor = 1.3F;
            rotationSpeedFactor = 1.7F;
        }

        float rotationDegrees = rotationScaleFactor * 4.3F * Mth.sin(rotationSpeedFactor * 0.6F * bob);
        matrixStack.mulPose(Axis.YP.rotationDegrees(rotationDegrees));
        matrixStack.translate(0.0F, 0.0F, -0.4F);
        if (!coelacanth.isInWater()) {
            matrixStack.translate(0.2F, 0.1F, 0.0F);
            matrixStack.mulPose(Axis.ZP.rotationDegrees(90.0F));
        }

    }
}
