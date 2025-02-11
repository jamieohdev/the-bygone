package com.jamiedev.mod.common.client.renderer;

import com.jamiedev.mod.fabric.JamiesModFabric;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.GlowSquidRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import com.jamiedev.mod.common.client.JamiesModModelLayers;
import com.jamiedev.mod.common.client.models.TrilobiteModel;
import com.jamiedev.mod.common.entities.TrilobiteEntity;

public class TrilobiteRenderer extends MobRenderer<TrilobiteEntity, TrilobiteModel<TrilobiteEntity>> {
    private static final ResourceLocation TEXTURE = JamiesModFabric.getModId("textures/entity/trilobite.png");
GlowSquidRenderer ref;
    public TrilobiteRenderer(EntityRendererProvider.Context context) {
        super(context, new TrilobiteModel(context.bakeLayer(JamiesModModelLayers.TRILOBITE)), 0.3F);
    }

    public ResourceLocation getTexture(TrilobiteEntity coelacanthEntity) {
        return TEXTURE;
    }

   // protected float getAnimationProgress(TrilobiteEntity coelacanthEntity, float f) {
    //    return f;
   // }
    protected int getBlockLight(TrilobiteEntity glowSquidEntity, BlockPos blockPos) {
        int i = (int) Mth.clampedLerp(0.0F, 15.0F, 1.0F - (float)glowSquidEntity.getDarkTicksRemaining() / 10.0F);
        return i == 15 ? 15 : Math.max(i, super.getBlockLightLevel(glowSquidEntity, blockPos));
    }
}
