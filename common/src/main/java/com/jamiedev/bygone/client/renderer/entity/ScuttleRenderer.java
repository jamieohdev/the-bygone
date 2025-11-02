package com.jamiedev.bygone.client.renderer.entity;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.client.JamiesModModelLayers;
import com.jamiedev.bygone.client.models.ScuttleModel;
import com.jamiedev.bygone.common.entity.ScuttleEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.GuardianRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class ScuttleRenderer extends MobRenderer<ScuttleEntity, ScuttleModel> {
    private static final RenderType LAYER;
    private static final ResourceLocation TEXTURE = Bygone.id("textures/entity/scuttle.png");

    static {
        LAYER = RenderType.entityCutoutNoCull(TEXTURE);
    }

    GuardianRenderer ref;

    public ScuttleRenderer(EntityRendererProvider.Context context) {
        super(context, new ScuttleModel(context.bakeLayer(JamiesModModelLayers.SCUTTLE)), 0.5F);
    }

    private static void vertex(VertexConsumer vertexConsumer, PoseStack.Pose matrix, float x, float y, float z, int red, int green, int blue, float u, float v) {
        vertexConsumer.addVertex(matrix, x, y, z).setColor(red, green, blue, 255).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(15728880).setNormal(matrix, 0.0F, 1.0F, 0.0F);
    }

    @Override
    protected void scale(ScuttleEntity slimeEntity, PoseStack matrixStack, float f) {

    }

    @Override
    public ResourceLocation getTextureLocation(ScuttleEntity slimeEntity) {
        return TEXTURE;
    }

    private Vec3 fromLerpedPosition(LivingEntity entity, double yOffset, float delta) {
        double d = Mth.lerp(delta, entity.xOld, entity.getX());
        double e = Mth.lerp(delta, entity.yOld, entity.getY()) + yOffset;
        double f = Mth.lerp(delta, entity.zOld, entity.getZ());
        return new Vec3(d, e, f);
    }

    @Override
    public void render(ScuttleEntity guardianEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
        super.render(guardianEntity, f, g, matrixStack, vertexConsumerProvider, i);


    }

    @Override
    public boolean shouldRender(ScuttleEntity guardianEntity, Frustum frustum, double d, double e, double f) {
        if (super.shouldRender(guardianEntity, frustum, d, e, f)) {
            return true;
        } else {
            if (guardianEntity.hasProjTarget()) {
                LivingEntity livingEntity = guardianEntity.getProjTarget();
                if (livingEntity != null) {
                    Vec3 vec3d = this.fromLerpedPosition(livingEntity, livingEntity.getBbHeight() * 0.5, 1.0F);
                    Vec3 vec3d2 = this.fromLerpedPosition(guardianEntity, guardianEntity.getEyeHeight(), 1.0F);
                    return frustum.isVisible(new AABB(vec3d2.x, vec3d2.y, vec3d2.z, vec3d.x, vec3d.y, vec3d.z));
                }
            }

            return false;
        }
    }
}
