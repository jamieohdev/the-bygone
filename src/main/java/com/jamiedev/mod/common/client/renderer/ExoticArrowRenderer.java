package com.jamiedev.mod.common.client.renderer;

import com.jamiedev.mod.fabric.JamiesModFabric;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import com.jamiedev.mod.common.entities.projectile.ExoticArrowEntity;

public class ExoticArrowRenderer extends ArrowRenderer<ExoticArrowEntity> {
    public static final ResourceLocation TEXTURE = JamiesModFabric.getModId("textures/entity/exotic_arrow.png");

    public ExoticArrowRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    public ResourceLocation getTexture(ExoticArrowEntity arrowEntity) {
        return TEXTURE;
    }
}
