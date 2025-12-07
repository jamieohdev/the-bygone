package com.jamiedev.bygone.client.renderer.entity;

import com.google.common.collect.Maps;
import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.client.JamiesModModelLayers;
import com.jamiedev.bygone.client.models.MurklingModel;
import com.jamiedev.bygone.common.entity.MurklingVariants;
import com.jamiedev.bygone.common.entity.MurklingEntity;
import net.minecraft.Util;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public class MurklingRenderer extends MobRenderer<MurklingEntity, MurklingModel<MurklingEntity>>
{
    private static final Map VARIANTS = Util.make(Maps.newEnumMap(MurklingVariants.class), (p_349902_) -> {
        p_349902_.put(MurklingVariants.FUSCHIA, Bygone.id("textures/entity/murkling/fuschia.png"));
        p_349902_.put(MurklingVariants.OLIVE, Bygone.id("textures/entity/murkling/olive.png"));
        p_349902_.put(MurklingVariants.TEAL, Bygone.id("textures/entity/murkling/teal.png"));
    });

    public MurklingRenderer(EntityRendererProvider.Context context) {
        super(context, new MurklingModel<>(context.bakeLayer(JamiesModModelLayers.MURKLING)), 0.35F);
    }

    @Override
    public ResourceLocation getTextureLocation(MurklingEntity entity) {
        return (ResourceLocation) VARIANTS.get(entity.getVariant());
    }
}
