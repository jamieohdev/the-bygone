package com.jamiedev.bygone.client.renderer.entity;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.client.JamiesModModelLayers;
import com.jamiedev.bygone.client.models.MurklingModel;
import com.jamiedev.bygone.common.entity.MurklingEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class MurklingRenderer extends MobRenderer<MurklingEntity, MurklingModel<MurklingEntity>>
{
    final ResourceLocation TEXTURE = Bygone.id("textures/entity/murkling.png    ");

    public MurklingRenderer(EntityRendererProvider.Context context) {
        super(context, new MurklingModel<>(context.bakeLayer(JamiesModModelLayers.MURKLING)), 0.35F);
    }

    @Override
    public ResourceLocation getTextureLocation(MurklingEntity murklingEntity) {
        return TEXTURE;
    }
}
