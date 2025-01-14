package com.jamiedev.mod.common.client.renderer;

import com.jamiedev.mod.common.client.JamiesModModelLayers;
import com.jamiedev.mod.common.client.models.*;
import com.jamiedev.mod.common.entities.FungalParentEntity;
import com.jamiedev.mod.fabric.JamiesModFabric;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;

public class FungalParentRenderer  extends MobEntityRenderer<FungalParentEntity, FungalParentModel<FungalParentEntity>> {
    private static final Identifier TEXTURE = JamiesModFabric.getModId("textures/entity/fungalparent1.png");
    private final Random random = Random.create();


    public FungalParentRenderer(EntityRendererFactory.Context context) {
        super(context, new FungalParentModel(context.getPart(JamiesModModelLayers.FUNGALPARENT)), 0.5F);
    }

    public void render(FungalParentEntity endermanEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {

        super.render(endermanEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    public Vec3d getPositionOffset(FungalParentEntity endermanEntity, float f) {
        if (endermanEntity.isBaby()) {
            double d = 0.01 * (double)endermanEntity.getScale();
            return new Vec3d(this.random.nextGaussian() * d, 0.0, this.random.nextGaussian() * d);
        } else {
            return super.getPositionOffset(endermanEntity, f);
        }
    }

    @Override
    protected void scale(FungalParentEntity slimeEntity, MatrixStack matrixStack, float f) {

        if (slimeEntity.isBaby())
        {
            matrixStack.scale(0.5F, 0.5F, 0.5F);

        }
    }

    public Identifier getTexture(FungalParentEntity endermanEntity) {
        return TEXTURE;
    }
}
