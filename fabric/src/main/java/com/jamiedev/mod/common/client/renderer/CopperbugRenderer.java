package com.jamiedev.mod.common.client.renderer;

import com.jamiedev.mod.common.client.JamiesModModelLayers;
import com.jamiedev.mod.common.client.models.CopperbugModel;
import com.jamiedev.mod.common.entities.CopperbugEntity;
import com.jamiedev.mod.fabric.JamiesModFabric;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.GlowSquidRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Silverfish;

public class CopperbugRenderer  extends MobRenderer<CopperbugEntity, CopperbugModel<CopperbugEntity>> {
    private static final ResourceLocation TEXTURE = JamiesModFabric.getModId("textures/entity/copperbug.png");
    GlowSquidRenderer ref;
    public CopperbugRenderer(EntityRendererProvider.Context context) {
        super(context, new CopperbugModel<CopperbugEntity>(context.bakeLayer(JamiesModModelLayers.COPPERBUG)), 0.3F);
    }

    protected float getLyingAngle(Silverfish silverfishEntity) {
        return 180.0F;
    }

    public ResourceLocation getTexture(CopperbugEntity coelacanthEntity) {
        return TEXTURE;
    }

    // protected float getAnimationProgress(CopperbugEntity coelacanthEntity, float f) {
    //    return f;
    // }
    protected int getBlockLight(CopperbugEntity cop, BlockPos blockPos) {
        return 15;
    }
}
