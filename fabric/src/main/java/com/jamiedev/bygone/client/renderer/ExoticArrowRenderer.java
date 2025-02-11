package com.jamiedev.bygone.client.renderer;

import com.jamiedev.bygone.Bygone;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import com.jamiedev.bygone.common.entities.projectile.ExoticArrowEntity;

public class ExoticArrowRenderer extends ArrowRenderer<ExoticArrowEntity> {
    public static final ResourceLocation TEXTURE = Bygone.getModId("textures/entity/exotic_arrow.png");

    public ExoticArrowRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(ExoticArrowEntity arrowEntity) {
        return TEXTURE;
    }
}
