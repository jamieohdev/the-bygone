package com.jamiedev.mod.common.client.renderer;

import com.jamiedev.mod.common.client.JamiesModModelLayers;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.render.entity.model.CowEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;

public class MoobooTransRenderer <T extends LivingEntity> extends FeatureRenderer<T, CowEntityModel<T>> {
    private final EntityModel<T> model;

    public MoobooTransRenderer(FeatureRendererContext<T, CowEntityModel<T>> context, EntityModelLoader loader) {
        super(context);
        this.model = new CowEntityModel<>(loader.getModelPart(JamiesModModelLayers.MOOBOO_TRANS));
    }

    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T livingEntity, float f, float g, float h, float j, float k, float l) {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        boolean bl = minecraftClient.hasOutline(livingEntity) && livingEntity.isInvisible();
        if (!livingEntity.isInvisible() || bl) {
            VertexConsumer vertexConsumer;
            if (bl) {
                vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getOutline(this.getTexture(livingEntity)));
            } else {
                vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityTranslucent(this.getTexture(livingEntity)));
            }

            ((CowEntityModel) this.getContextModel()).copyStateTo(this.model);
            this.model.animateModel(livingEntity, f, g, h);
            this.model.setAngles(livingEntity, f, g, j, k, l);
            this.model.render(matrixStack, vertexConsumer, i, LivingEntityRenderer.getOverlay(livingEntity, 0.0F));
        }
    }
}