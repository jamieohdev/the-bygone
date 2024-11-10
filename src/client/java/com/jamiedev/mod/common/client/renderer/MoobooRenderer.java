package com.jamiedev.mod.common.client.renderer;

import com.jamiedev.mod.common.JamiesMod;
import com.jamiedev.mod.common.client.JamiesModModelLayers;
import com.jamiedev.mod.common.client.models.MoobooModel;
import com.jamiedev.mod.common.entities.MoobooEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.SlimeEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.feature.SlimeOverlayFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.SlimeEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.entity.mob.VexEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraft.scoreboard.ScoreboardCriterion;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;

import static net.minecraft.client.render.RenderPhase.TRANSLUCENT_TRANSPARENCY;

public class MoobooRenderer extends MobEntityRenderer<MoobooEntity, MoobooModel<MoobooEntity>> {
    private static final Identifier TEXTURE = JamiesMod.getModId("textures/entity/mooboo.png");


    public MoobooRenderer(EntityRendererFactory.Context context) {
        super(context, new MoobooModel<>(context.getPart(JamiesModModelLayers.MOOBOO)), 0.7F);
    }

    protected int getBlockLight(VexEntity vexEntity, BlockPos blockPos) {
        return 15;
    }

    public Identifier getTexture(MoobooEntity cowEntity) {
        return TEXTURE;
    }

    @Override
    public void render(MoobooEntity entity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i)
    {
        matrixStack.push();
        float h = MathHelper.lerpAngleDegrees(g, entity.prevBodyYaw, entity.bodyYaw);
        float j = MathHelper.lerpAngleDegrees(g, entity.prevHeadYaw, entity.headYaw);
        float k = j - h;
        float m = MathHelper.lerp(g, entity.prevPitch, entity.getPitch());
        float o = 0.0F;
        float p = 0.0F;
        float n;

        n = this.getAnimationProgress(entity, g);
        if (!entity.hasVehicle() && entity.isAlive()) {
            o = entity.limbAnimator.getSpeed(g);
            p = entity.limbAnimator.getPos(g);
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

        k = MathHelper.wrapDegrees(k);

        if(((Entity) entity).isInvisible())
            return;

        VertexConsumer vertexConsumer;
        vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityTranslucent(this.getTexture(entity)));

        int color = ColorHelper.Argb.getArgb(128, 255, 255, 255);
        this.model.animateModel(entity, p, o, g);
        this.model.setAngles(entity, p, o, n, k, m);
        matrixStack.pop();
        model.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, color);
    }
}
