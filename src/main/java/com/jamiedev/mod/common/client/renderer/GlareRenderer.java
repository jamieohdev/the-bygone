package com.jamiedev.mod.common.client.renderer;

import com.jamiedev.mod.fabric.JamiesModFabric;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import com.jamiedev.mod.common.client.JamiesModModelLayers;
import com.jamiedev.mod.common.client.models.GlareModel;
import com.jamiedev.mod.common.entities.GlareEntity;

public class GlareRenderer  extends MobRenderer<GlareEntity, GlareModel<GlareEntity>> {
    private static final ResourceLocation TEXTURE = JamiesModFabric.getModId("textures/entity/glare.png");

    public GlareRenderer(EntityRendererProvider.Context context) {
        super(context, new GlareModel(context.bakeLayer(JamiesModModelLayers.GLARE)), 0.5F);
    }

    public ResourceLocation getTexture(GlareEntity chickenEntity) {
        return TEXTURE;
    }

    protected float getAnimationProgress(GlareEntity chickenEntity, float f) {
        return f;
    }

    protected void scale(GlareEntity daGlare, PoseStack matrixStack, float f) {
        int i = daGlare.getSize();
        float g = 1.0F + 0.15F * (float)i;
        matrixStack.scale(g, g, g);
        matrixStack.translate(0.0F, 1.3125F, 0.1875F);
    }
}
