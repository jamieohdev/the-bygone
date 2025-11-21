package com.jamiedev.bygone.client.renderer.entity;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.client.JamiesModModelLayers;
import com.jamiedev.bygone.client.models.AquifawnModel;
import com.jamiedev.bygone.client.models.AquifawnModel;
import com.jamiedev.bygone.client.models.BigBeakModel;
import com.jamiedev.bygone.common.entity.AquifawnEntity;
import net.minecraft.client.model.PigModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.GuardianRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.PigRenderer;
import net.minecraft.client.renderer.entity.layers.SaddleLayer;
import net.minecraft.resources.ResourceLocation;

public class AquifawnRenderer extends MobRenderer<AquifawnEntity, AquifawnModel<AquifawnEntity>>
{

    private static final ResourceLocation TEXTURE = Bygone.id("textures/entity/aquifawn.png");

    public AquifawnRenderer(EntityRendererProvider.Context context) {
        super(context, new AquifawnModel<>(context.bakeLayer(JamiesModModelLayers.AQUIFAWN)), 0.5F);
        this.addLayer(new SaddleLayer(this, new AquifawnModel<>(context.bakeLayer(JamiesModModelLayers.AQUIFAWN_SADDLE)),
                Bygone.id("textures/entity/aquifawn_saddled.png")));

    }

    @Override
    public ResourceLocation getTextureLocation(AquifawnEntity aquifawnEntity) {
        return TEXTURE;
    }
}
