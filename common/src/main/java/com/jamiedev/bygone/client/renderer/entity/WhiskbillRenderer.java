package com.jamiedev.bygone.client.renderer.entity;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.client.JamiesModModelLayers;
import com.jamiedev.bygone.client.models.WhiskbillModel;
import com.jamiedev.bygone.common.entity.WhiskbillEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class WhiskbillRenderer extends MobRenderer<WhiskbillEntity, WhiskbillModel<WhiskbillEntity>> {
    private static final ResourceLocation TEXTURE = Bygone.id("textures/entity/whiskbill.png");

    public WhiskbillRenderer(EntityRendererProvider.Context context) {
        super(context, new WhiskbillModel<>(context.bakeLayer(JamiesModModelLayers.WHISKBILL)), 0.85F);
    }

    @Override
    public ResourceLocation getTextureLocation(WhiskbillEntity chickenEntity) {
        return TEXTURE;
    }


    @Override
    protected void scale(WhiskbillEntity slimeEntity, PoseStack matrixStack, float f) {
        if (slimeEntity.isBaby())
        {
            matrixStack.scale(0.45F, 0.45F, 0.45F);
        }
    }
}
