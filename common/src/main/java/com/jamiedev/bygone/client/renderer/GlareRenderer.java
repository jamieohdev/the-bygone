package com.jamiedev.bygone.client.renderer;

import com.jamiedev.bygone.Bygone;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import com.jamiedev.bygone.client.JamiesModModelLayers;
import com.jamiedev.bygone.client.models.GlareModel;
import com.jamiedev.bygone.common.entity.GlareEntity;

public class GlareRenderer  extends MobRenderer<GlareEntity, GlareModel<GlareEntity>> {
    private static final ResourceLocation TEXTURE = Bygone.id("textures/entity/glare.png");

    public GlareRenderer(EntityRendererProvider.Context context) {
        super(context, new GlareModel<>(context.bakeLayer(JamiesModModelLayers.GLARE)), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(GlareEntity chickenEntity) {
        return TEXTURE;
    }

    protected float getAnimationProgress(GlareEntity chickenEntity, float f) {
        return f;
    }

    @Override
    protected void scale(GlareEntity daGlare, PoseStack matrixStack, float f) {
        int i = daGlare.getSize();
        float g = 1.0F + 0.15F * (float)i;
        matrixStack.scale(g, g, g);
        matrixStack.translate(0.0F, 1.3125F, 0.1875F);
    }
}
