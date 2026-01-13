package com.jamiedev.bygone.client.renderer.entity;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.client.JamiesModModelLayers;
import com.jamiedev.bygone.client.models.MoobooModel;
import com.jamiedev.bygone.common.entity.MoobooEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.SlimeRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Vex;

import javax.annotation.Nullable;

public class MoobooRenderer extends MobRenderer<MoobooEntity, MoobooModel<MoobooEntity>> {
    private static final ResourceLocation COW_LOCATION = (Bygone.id("textures/entity/mooboo_r.png"));

    SlimeRenderer ref;

    public MoobooRenderer(EntityRendererProvider.Context context) {
        super(context, new MoobooModel<>(context.bakeLayer(JamiesModModelLayers.MOOBOO)), 0.7F);

    }

    @Override
    public ResourceLocation getTextureLocation(MoobooEntity moobooEntity2) {
        return COW_LOCATION;
    }


    protected int getBlockLightLevel(MoobooEntity mooboo, BlockPos blockPos) {
        return 15;
    }

    @Nullable
    @Override
    protected RenderType getRenderType(MoobooEntity p_230496_1_, boolean p_230496_2_, boolean p_230496_3_, boolean p_230496_4_) {
        return RenderType.entityTranslucent(this.getTextureLocation(p_230496_1_));
    }
}
