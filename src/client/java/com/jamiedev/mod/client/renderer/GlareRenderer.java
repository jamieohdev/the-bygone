package com.jamiedev.mod.client.renderer;

import com.jamiedev.mod.JamiesMod;
import com.jamiedev.mod.client.JamiesModModelLayers;
import com.jamiedev.mod.client.models.GlareModel;
import com.jamiedev.mod.entities.GlareEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class GlareRenderer  extends MobEntityRenderer<GlareEntity, GlareModel<GlareEntity>> {
    private static final Identifier TEXTURE = JamiesMod.getModId("textures/entity/glare.png");

    public GlareRenderer(EntityRendererFactory.Context context) {
        super(context, new GlareModel(context.getPart(JamiesModModelLayers.GLARE)), 0.5F);
    }

    public Identifier getTexture(GlareEntity chickenEntity) {
        return TEXTURE;
    }

    protected float getAnimationProgress(GlareEntity chickenEntity, float f) {
        return f;
    }

    protected void scale(GlareEntity daGlare, MatrixStack matrixStack, float f) {
        int i = daGlare.getSize();
        float g = 1.0F + 0.15F * (float)i;
        matrixStack.scale(g, g, g);
        matrixStack.translate(0.0F, 1.3125F, 0.1875F);
    }
}
