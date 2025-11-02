package com.jamiedev.bygone.client.renderer.entity;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.common.entity.projectile.ExoticArrowEntity;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class ExoticArrowRenderer extends ArrowRenderer<ExoticArrowEntity> {
    public static final ResourceLocation TEXTURE = Bygone.id("textures/entity/exotic_arrow.png");

    public ExoticArrowRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(ExoticArrowEntity arrowEntity) {
        return TEXTURE;
    }
}
