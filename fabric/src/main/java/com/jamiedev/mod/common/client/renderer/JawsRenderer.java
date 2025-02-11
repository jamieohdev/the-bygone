package com.jamiedev.mod.common.client.renderer;

import com.jamiedev.mod.fabric.JamiesModFabric;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.jamiedev.mod.common.client.JamiesModModelLayers;
import com.jamiedev.mod.common.client.models.JawsEntityModel;
import com.jamiedev.mod.common.entities.JawsEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class JawsRenderer extends MobRenderer<JawsEntity, JawsEntityModel>
{

    private static final ResourceLocation TEXTURE = JamiesModFabric.getModId("textures/entity/jaws.png");
    private static final ResourceLocation EXPLOSION_BEAM_TEXTURE = JamiesModFabric.getModId("textures/entity/jaws_attack.png");
    private static final RenderType LAYER;

    public JawsRenderer(EntityRendererProvider.Context context) {
        super(context, new JawsEntityModel(context.bakeLayer(JamiesModModelLayers.JAWS)), 0.5F);
    }

    public boolean shouldRender(JawsEntity guardianEntity, Frustum frustum, double d, double e, double f) {
        if (super.shouldRender(guardianEntity, frustum, d, e, f)) {
            return true;
        } else {


            return false;
        }
    }

    private Vec3 fromLerpedPosition(LivingEntity entity, double yOffset, float delta) {
        double d = Mth.lerp((double)delta, entity.xOld, entity.getX());
        double e = Mth.lerp((double)delta, entity.yOld, entity.getY()) + yOffset;
        double f = Mth.lerp((double)delta, entity.zOld, entity.getZ());
        return new Vec3(d, e, f);
    }

    public void render(JawsEntity guardianEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
        super.render(guardianEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    private static void vertex(VertexConsumer vertexConsumer, PoseStack.Pose matrix, float x, float y, float z, int red, int green, int blue, float u, float v) {
        vertexConsumer.addVertex(matrix, x, y, z).setColor(red, green, blue, 255).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(15728880).setNormal(matrix, 0.0F, 1.0F, 0.0F);
    }

    public ResourceLocation getTexture(JawsEntity guardianEntity) {
        return TEXTURE;
    }

    static {
        LAYER = RenderType.entityCutoutNoCull(EXPLOSION_BEAM_TEXTURE);
    }
}
