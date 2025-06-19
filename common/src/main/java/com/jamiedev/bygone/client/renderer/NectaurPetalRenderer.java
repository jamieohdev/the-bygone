package com.jamiedev.bygone.client.renderer;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.common.entity.projectile.ExoticArrowEntity;
import com.jamiedev.bygone.common.entity.projectile.NectaurPetalEntity;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class NectaurPetalRenderer extends ArrowRenderer<NectaurPetalEntity> {
    public static final ResourceLocation TEXTURE = Bygone.id("textures/entity/nectaur_petal.png");

    public NectaurPetalRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(NectaurPetalEntity arrowEntity) {
        return TEXTURE;
    }
}
