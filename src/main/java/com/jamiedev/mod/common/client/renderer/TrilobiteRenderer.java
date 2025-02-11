package com.jamiedev.mod.common.client.renderer;

import com.jamiedev.mod.fabric.JamiesModFabric;
import com.jamiedev.mod.common.client.JamiesModModelLayers;
import com.jamiedev.mod.common.client.models.TrilobiteModel;
import com.jamiedev.mod.common.entities.TrilobiteEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.GlowSquidEntityRenderer;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class TrilobiteRenderer extends MobEntityRenderer<TrilobiteEntity, TrilobiteModel<TrilobiteEntity>> {
    private static final Identifier TEXTURE = JamiesModFabric.getModId("textures/entity/trilobite.png");
GlowSquidEntityRenderer ref;
    public TrilobiteRenderer(EntityRendererFactory.Context context) {
        super(context, new TrilobiteModel(context.getPart(JamiesModModelLayers.TRILOBITE)), 0.3F);
    }

    public Identifier getTexture(TrilobiteEntity coelacanthEntity) {
        return TEXTURE;
    }

   // protected float getAnimationProgress(TrilobiteEntity coelacanthEntity, float f) {
    //    return f;
   // }
    protected int getBlockLight(TrilobiteEntity glowSquidEntity, BlockPos blockPos) {
        int i = (int) MathHelper.clampedLerp(0.0F, 15.0F, 1.0F - (float)glowSquidEntity.getDarkTicksRemaining() / 10.0F);
        return i == 15 ? 15 : Math.max(i, super.getBlockLight(glowSquidEntity, blockPos));
    }
}
