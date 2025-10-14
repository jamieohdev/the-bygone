package com.jamiedev.bygone.client.renderer.entity;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.client.JamiesModModelLayers;
import com.jamiedev.bygone.common.entity.MoobooEntity;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Vex;

import javax.annotation.Nullable;

public class MoobooRenderer2 extends MobRenderer<MoobooEntity, CowModel<MoobooEntity>> {
    private static final ResourceLocation COW_LOCATION = Bygone.id("textures/entity/mooboo.png");

    SlimeRenderer ref;

    public MoobooRenderer2(EntityRendererProvider.Context context) {
        super(context, new CowModel(context.bakeLayer(JamiesModModelLayers.MOOBOO)), 0.7F);

    }

    @Override
    public ResourceLocation getTextureLocation(MoobooEntity moobooEntity2) {
        return COW_LOCATION;
    }


    protected int getBlockLight(Vex vexEntity, BlockPos blockPos) {
        return 15;
    }

    @Nullable
    @Override
    protected RenderType getRenderType(MoobooEntity p_230496_1_, boolean p_230496_2_, boolean p_230496_3_, boolean p_230496_4_) {
        return RenderType.entityTranslucent(this.getTextureLocation(p_230496_1_));
    }
}
