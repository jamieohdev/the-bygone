package com.jamiedev.bygone.client.renderer.entity;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.client.JamiesModModelLayers;
import com.jamiedev.bygone.client.models.TrilobiteModel;
import com.jamiedev.bygone.common.entity.TrilobiteEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.GlowSquidRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class TrilobiteRenderer extends MobRenderer<TrilobiteEntity, TrilobiteModel<TrilobiteEntity>> {
    private static final ResourceLocation TEXTURE = Bygone.id("textures/entity/trilobite.png");
    GlowSquidRenderer ref;

    public TrilobiteRenderer(EntityRendererProvider.Context context) {
        super(context, new TrilobiteModel<>(context.bakeLayer(JamiesModModelLayers.TRILOBITE)), 0.3F);
    }

    @Override
    public ResourceLocation getTextureLocation(TrilobiteEntity coelacanthEntity) {
        return TEXTURE;
    }

    // protected float getAnimationProgress(TrilobiteEntity coelacanthEntity, float f) {
    //    return f;
    // }
    protected int getBlockLight(TrilobiteEntity glowSquidEntity, BlockPos blockPos) {
        int i = (int) Mth.clampedLerp(0.0F, 15.0F, 1.0F - (float) glowSquidEntity.getDarkTicksRemaining() / 10.0F);
        return i == 15 ? 15 : Math.max(i, super.getBlockLightLevel(glowSquidEntity, blockPos));
    }
}
