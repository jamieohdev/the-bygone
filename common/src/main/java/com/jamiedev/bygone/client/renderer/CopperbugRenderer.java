package com.jamiedev.bygone.client.renderer;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.client.JamiesModModelLayers;
import com.jamiedev.bygone.client.models.CopperbugModel;
import com.jamiedev.bygone.common.entity.CopperbugEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Silverfish;

public class CopperbugRenderer extends MobRenderer<CopperbugEntity, CopperbugModel<CopperbugEntity>> {
    private static final ResourceLocation TEXTURE = Bygone.id("textures/entity/copperbug.png");

    public CopperbugRenderer(EntityRendererProvider.Context context) {
        super(context, new CopperbugModel<>(context.bakeLayer(JamiesModModelLayers.COPPERBUG)), 0.3F);
    }

    protected float getLyingAngle(Silverfish silverfishEntity) {
        return 180.0F;
    }

    @Override
    public ResourceLocation getTextureLocation(CopperbugEntity coelacanthEntity) {
        return TEXTURE;
    }

    protected int getBlockLight(CopperbugEntity cop, BlockPos blockPos) {
        return 8;
    }
}
