package com.jamiedev.bygone.client.renderer.entity;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.common.entity.MoobooEntity2;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.CowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.monster.Vex;

public class MoobooRenderer2 extends MobRenderer<MoobooEntity2, CowModel<MoobooEntity2>> {
    private static final ResourceLocation COW_LOCATION = Bygone.id("textures/entity/mooboo.png");

    public MoobooRenderer2(EntityRendererProvider.Context p_173956_) {
        super(p_173956_, new CowModel(p_173956_.bakeLayer(ModelLayers.COW)), 0.7F);
    }

    protected int getBlockLight(Vex vexEntity, BlockPos blockPos) {
        return 15;
    }

    public ResourceLocation getTextureLocation(MoobooEntity2 entity) {
        return COW_LOCATION;
    }
}
