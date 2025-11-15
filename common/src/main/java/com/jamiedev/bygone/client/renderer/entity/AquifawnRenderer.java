package com.jamiedev.bygone.client.renderer.entity;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.client.JamiesModModelLayers;
import com.jamiedev.bygone.client.models.AquifawnModel;
import com.jamiedev.bygone.client.models.AquifawnModel;
import com.jamiedev.bygone.common.entity.AquifawnEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.GuardianRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class AquifawnRenderer extends MobRenderer<AquifawnEntity, AquifawnModel<AquifawnEntity>>
{

    private static final ResourceLocation TEXTURE = Bygone.id("textures/entity/aquifawn.png");

    public AquifawnRenderer(EntityRendererProvider.Context context) {
        super(context, new AquifawnModel<>(context.bakeLayer(JamiesModModelLayers.AQUIFAWN)), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(AquifawnEntity aquifawnEntity) {
        return TEXTURE;
    }
}
