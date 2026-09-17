package com.jamiedev.bygone.client.renderer.entity;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.common.entity.projectile.LithoArrowEntity;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class LithoplasmArrowRenderer extends ArrowRenderer<LithoArrowEntity> {
    public static final ResourceLocation TEXTURE = Bygone.id("textures/entity/lithoplasm_arrow.png");

    public LithoplasmArrowRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(LithoArrowEntity arrowEntity) {
        return TEXTURE;
    }
}
