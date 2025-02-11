package com.jamiedev.mod.common.client.renderer;

import com.jamiedev.mod.fabric.JamiesModFabric;
import com.jamiedev.mod.common.entities.projectile.ExoticArrowEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.util.Identifier;

public class ExoticArrowRenderer extends ProjectileEntityRenderer<ExoticArrowEntity> {
    public static final Identifier TEXTURE = JamiesModFabric.getModId("textures/entity/exotic_arrow.png");

    public ExoticArrowRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    public Identifier getTexture(ExoticArrowEntity arrowEntity) {
        return TEXTURE;
    }
}
