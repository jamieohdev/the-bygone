package com.jamiedev.mod.common.client.renderer;

import com.jamiedev.mod.common.JamiesMod;
import com.jamiedev.mod.common.client.JamiesModModelLayers;
import com.jamiedev.mod.common.client.models.JawsEntityModel;
import com.jamiedev.mod.common.entities.JawsEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class JawsRenderer extends MobEntityRenderer<JawsEntity, JawsEntityModel>
{

    private static final Identifier TEXTURE = JamiesMod.getModId("textures/entity/jaws.png");
    private static final Identifier EXPLOSION_BEAM_TEXTURE = JamiesMod.getModId("textures/entity/jaws_attack.png");
    private static final RenderLayer LAYER;

    public JawsRenderer(EntityRendererFactory.Context context) {
        super(context, new JawsEntityModel(context.getPart(JamiesModModelLayers.JAWS)), 0.5F);
    }

    public boolean shouldRender(JawsEntity guardianEntity, Frustum frustum, double d, double e, double f) {
        if (super.shouldRender(guardianEntity, frustum, d, e, f)) {
            return true;
        } else {


            return false;
        }
    }

    private Vec3d fromLerpedPosition(LivingEntity entity, double yOffset, float delta) {
        double d = MathHelper.lerp((double)delta, entity.lastRenderX, entity.getX());
        double e = MathHelper.lerp((double)delta, entity.lastRenderY, entity.getY()) + yOffset;
        double f = MathHelper.lerp((double)delta, entity.lastRenderZ, entity.getZ());
        return new Vec3d(d, e, f);
    }

    public void render(JawsEntity guardianEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        super.render(guardianEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    private static void vertex(VertexConsumer vertexConsumer, MatrixStack.Entry matrix, float x, float y, float z, int red, int green, int blue, float u, float v) {
        vertexConsumer.vertex(matrix, x, y, z).color(red, green, blue, 255).texture(u, v).overlay(OverlayTexture.DEFAULT_UV).light(15728880).normal(matrix, 0.0F, 1.0F, 0.0F);
    }

    public Identifier getTexture(JawsEntity guardianEntity) {
        return TEXTURE;
    }

    static {
        LAYER = RenderLayer.getEntityCutoutNoCull(EXPLOSION_BEAM_TEXTURE);
    }
}
