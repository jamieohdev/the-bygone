package com.jamiedev.bygone.client.renderer;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.client.JamiesModModelLayers;
import com.jamiedev.bygone.client.models.CoelacanthModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.SalmonRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import com.jamiedev.bygone.entities.CoelacanthEntity;

public class CoelacanthRenderer extends MobRenderer<CoelacanthEntity, CoelacanthModel<CoelacanthEntity>> {
    private static final ResourceLocation TEXTURE = Bygone.id("textures/entity/coelacanth.png");
SalmonRenderer ref;
    public CoelacanthRenderer(EntityRendererProvider.Context context) {
        super(context, new CoelacanthModel<>(context.bakeLayer(JamiesModModelLayers.COELACANTH)), 0.6F);
    }

    @Override
    public ResourceLocation getTextureLocation(CoelacanthEntity coelacanthEntity) {
        return TEXTURE;
    }

   // protected float getAnimationProgress(CoelacanthEntity coelacanthEntity, float f) {
    //    return f;
    //}

    @Override
    protected void setupRotations(CoelacanthEntity salmonEntity, PoseStack matrixStack, float f, float g, float h, float i) {
        super.setupRotations(salmonEntity, matrixStack, f, g, h, i);
        float j = 1.0F;
        float k = 1.0F;
        if (!salmonEntity.isInWater()) {
            j = 1.3F;
            k = 1.7F;
        }

        float l = j * 4.3F * Mth.sin(k * 0.6F * f);
        matrixStack.mulPose(Axis.YP.rotationDegrees(l));
        matrixStack.translate(0.0F, 0.0F, -0.4F);
        if (!salmonEntity.isInWater()) {
            matrixStack.translate(0.2F, 0.1F, 0.0F);
            matrixStack.mulPose(Axis.ZP.rotationDegrees(90.0F));
        }

    }
}
