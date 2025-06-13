package com.jamiedev.bygone.client.renderer;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.client.JamiesModModelLayers;
import com.jamiedev.bygone.client.models.NectaurModel;
import com.jamiedev.bygone.common.entity.GlareEntity;
import com.jamiedev.bygone.common.entity.NectaurEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class NectaurRenderer extends MobRenderer<NectaurEntity, NectaurModel<NectaurEntity>> {
    private static final ResourceLocation TEXTURE = Bygone.id("textures/entity/nectaur.png");

    public NectaurRenderer(EntityRendererProvider.Context context) {
        super(context, new NectaurModel<>(context.bakeLayer(JamiesModModelLayers.NECTAUR)), 0.8F);
    }

    @Override
    public ResourceLocation getTextureLocation(NectaurEntity chickenEntity) {
        return TEXTURE;
    }

    @Override
    protected void scale(NectaurEntity daGlare, PoseStack matrixStack, float f) {

        matrixStack.translate(0.0F, 1.5F, 0.0F);
    }
}
