package com.jamiedev.mod.common.client.renderer;

import com.jamiedev.mod.common.JamiesMod;
import com.jamiedev.mod.common.client.JamiesModModelLayers;
import com.jamiedev.mod.common.client.models.ScuttleSpikeModel;
import com.jamiedev.mod.common.entities.projectile.ScuttleSpikeEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

public class ScuttleSpikeRenderer  extends EntityRenderer<ScuttleSpikeEntity> {
    public static final Identifier TEXTURE = JamiesMod.getModId("textures/entity/scuttlespike.png");
    private final ScuttleSpikeModel model;

    public ScuttleSpikeRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.model = new ScuttleSpikeModel(context.getPart(JamiesModModelLayers.SCUTTLE_SPIKE));
    }

    public void render(ScuttleSpikeEntity tridentEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(MathHelper.lerp(g, tridentEntity.prevYaw, tridentEntity.getYaw()) - 90.0F));
        matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(MathHelper.lerp(g, tridentEntity.prevPitch, tridentEntity.getPitch()) + 90.0F));
        VertexConsumer vertexConsumer = ItemRenderer.getDirectItemGlintConsumer(vertexConsumerProvider, this.model.getLayer(this.getTexture(tridentEntity)), false, false);
        this.model.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV);
        matrixStack.pop();
        super.render(tridentEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    public Identifier getTexture(ScuttleSpikeEntity tridentEntity) {
        return TEXTURE;
    }
}