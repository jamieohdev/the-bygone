package com.jamiedev.bygone.client.renderer.entity;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.client.JamiesModModelLayers;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.jamiedev.bygone.client.models.MoobooModel;
import com.jamiedev.bygone.common.entity.MoobooEntity;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.monster.Vex;

public class MoobooRenderer extends MobRenderer<MoobooEntity, MoobooModel<MoobooEntity>> {
    private static final ResourceLocation TEXTURE = Bygone.id("textures/entity/mooboo.png");


    public MoobooRenderer(EntityRendererProvider.Context context) {
        super(context, new MoobooModel<>(context.bakeLayer(JamiesModModelLayers.MOOBOO)), 0.7F);
    }

    protected int getBlockLight(Vex vexEntity, BlockPos blockPos) {
        return 15;
    }

    @Override
    public ResourceLocation getTextureLocation(MoobooEntity cowEntity) {
        return TEXTURE;
    }

    @Override
    public void render(MoobooEntity entity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i)
    {
        matrixStack.pushPose();
        float h = Mth.rotLerp(g, entity.yBodyRotO, entity.yBodyRot);
        float j = Mth.rotLerp(g, entity.yHeadRotO, entity.yHeadRot);
        float k = j - h;
        float m = Mth.lerp(g, entity.xRotO, entity.getXRot());
        float o = 0.0F;
        float p = 0.0F;
        float n;

        n = this.getBob(entity, g);
        if (!entity.isPassenger() && entity.isAlive()) {
            o = entity.walkAnimation.speed(g);
            p = entity.walkAnimation.position(g);
            if (entity.isBaby()) {
                p *= 3.0F;
            }

            if (o > 1.0F) {
                o = 1.0F;
            }
        }
        //if (shouldFlipUpsideDown(entity)) {
            m *= +1.0F;
            k *= +1.0F;
       // }

        k = Mth.wrapDegrees(k);

        if(entity.isInvisible())
            return;

        VertexConsumer vertexConsumer;
        vertexConsumer = vertexConsumerProvider.getBuffer(RenderType.entityTranslucent(this.getTextureLocation(entity)));

        int color = FastColor.ARGB32.color(128, 255, 255, 255);
        this.model.prepareMobModel(entity, p, o, g);
        this.model.setupAnim(entity, p, o, n, k, m);
        matrixStack.popPose();
        model.renderToBuffer(matrixStack, vertexConsumer, i, OverlayTexture.NO_OVERLAY, color);
    }
}
