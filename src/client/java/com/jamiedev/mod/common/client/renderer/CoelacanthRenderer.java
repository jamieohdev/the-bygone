package com.jamiedev.mod.common.client.renderer;

import com.jamiedev.mod.common.JamiesMod;
import com.jamiedev.mod.common.client.JamiesModModelLayers;
import com.jamiedev.mod.common.client.models.CoelacanthModel;
import com.jamiedev.mod.common.entities.CoelacanthEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.SalmonEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

public class CoelacanthRenderer extends MobEntityRenderer<CoelacanthEntity, CoelacanthModel<CoelacanthEntity>> {
    private static final Identifier TEXTURE = JamiesMod.getModId("textures/entity/coelacanth.png");
SalmonEntityRenderer ref;
    public CoelacanthRenderer(EntityRendererFactory.Context context) {
        super(context, new CoelacanthModel(context.getPart(JamiesModModelLayers.COELACANTH)), 0.6F);
    }

    public Identifier getTexture(CoelacanthEntity coelacanthEntity) {
        return TEXTURE;
    }

   // protected float getAnimationProgress(CoelacanthEntity coelacanthEntity, float f) {
    //    return f;
    //}

    protected void setupTransforms(CoelacanthEntity salmonEntity, MatrixStack matrixStack, float f, float g, float h, float i) {
        super.setupTransforms(salmonEntity, matrixStack, f, g, h, i);
        float j = 1.0F;
        float k = 1.0F;
        if (!salmonEntity.isTouchingWater()) {
            j = 1.3F;
            k = 1.7F;
        }

        float l = j * 4.3F * MathHelper.sin(k * 0.6F * f);
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(l));
        matrixStack.translate(0.0F, 0.0F, -0.4F);
        if (!salmonEntity.isTouchingWater()) {
            matrixStack.translate(0.2F, 0.1F, 0.0F);
            matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(90.0F));
        }

    }
}
