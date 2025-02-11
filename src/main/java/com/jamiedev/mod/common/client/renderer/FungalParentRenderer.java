package com.jamiedev.mod.common.client.renderer;

import com.jamiedev.mod.common.client.JamiesModModelLayers;
import com.jamiedev.mod.common.client.models.*;
import com.jamiedev.mod.common.entities.FungalParentEntity;
import com.jamiedev.mod.fabric.JamiesModFabric;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.render.entity.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;

public class FungalParentRenderer  extends MobRenderer<FungalParentEntity, FungalParentModel<FungalParentEntity>> {
    private static final ResourceLocation TEXTURE = JamiesModFabric.getModId("textures/entity/fungalparent1.png");
    private final RandomSource random = RandomSource.create();


    public FungalParentRenderer(EntityRendererProvider.Context context) {
        super(context, new FungalParentModel(context.bakeLayer(JamiesModModelLayers.FUNGALPARENT)), 0.5F);
    }

    public void render(FungalParentEntity endermanEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {

        super.render(endermanEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    public Vec3 getPositionOffset(FungalParentEntity endermanEntity, float f) {
        if (endermanEntity.isBaby()) {
            double d = 0.01 * (double)endermanEntity.getScale();
            return new Vec3(this.random.nextGaussian() * d, 0.0, this.random.nextGaussian() * d);
        } else {
            return super.getRenderOffset(endermanEntity, f);
        }
    }

    @Override
    protected void scale(FungalParentEntity slimeEntity, PoseStack matrixStack, float f) {

        if (slimeEntity.isBaby())
        {
            matrixStack.scale(0.5F, 0.5F, 0.5F);

        }
    }

    public ResourceLocation getTexture(FungalParentEntity endermanEntity) {
        return TEXTURE;
    }
}
