package com.jamiedev.bygone.client.renderer;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.client.JamiesModModelLayers;
import com.jamiedev.bygone.client.models.PestModel;
import com.jamiedev.bygone.client.models.WhiskbillModel;
import com.jamiedev.bygone.common.entity.PestEntity;
import com.jamiedev.bygone.common.entity.WhiskbillEntity;
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

}
