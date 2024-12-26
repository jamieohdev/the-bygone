package com.jamiedev.mod.common.client.renderer;

import com.jamiedev.mod.common.client.JamiesModModelLayers;
import com.jamiedev.mod.common.client.models.CopperbugModel;
import com.jamiedev.mod.common.entities.CopperbugEntity;
import com.jamiedev.mod.fabric.JamiesModFabric;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.GlowSquidEntityRenderer;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.entity.mob.SilverfishEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class CopperbugRenderer  extends MobEntityRenderer<CopperbugEntity, CopperbugModel<CopperbugEntity>> {
    private static final Identifier TEXTURE = JamiesModFabric.getModId("textures/entity/copperbug.png");
    GlowSquidEntityRenderer ref;
    public CopperbugRenderer(EntityRendererFactory.Context context) {
        super(context, new CopperbugModel<CopperbugEntity>(context.getPart(JamiesModModelLayers.COPPERBUG)), 0.3F);
    }

    protected float getLyingAngle(SilverfishEntity silverfishEntity) {
        return 180.0F;
    }

    public Identifier getTexture(CopperbugEntity coelacanthEntity) {
        return TEXTURE;
    }

    // protected float getAnimationProgress(CopperbugEntity coelacanthEntity, float f) {
    //    return f;
    // }
    protected int getBlockLight(CopperbugEntity cop, BlockPos blockPos) {
        return 15;
    }
}
