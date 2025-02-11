package com.jamiedev.bygone.client.renderer;

import com.jamiedev.bygone.client.JamiesModModelLayers;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.LivingEntity;

public class MoobooTransRenderer <T extends LivingEntity> extends RenderLayer<T, CowModel<T>> {
    private final EntityModel<T> model;

    public MoobooTransRenderer(RenderLayerParent<T, CowModel<T>> context, EntityModelSet loader) {
        super(context);
        this.model = new CowModel<>(loader.bakeLayer(JamiesModModelLayers.MOOBOO_TRANS));
    }

    public void render(PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i, T livingEntity, float f, float g, float h, float j, float k, float l) {
        Minecraft minecraftClient = Minecraft.getInstance();
        boolean bl = minecraftClient.shouldEntityAppearGlowing(livingEntity) && livingEntity.isInvisible();
        if (!livingEntity.isInvisible() || bl) {
            VertexConsumer vertexConsumer;
            if (bl) {
                vertexConsumer = vertexConsumerProvider.getBuffer(RenderType.outline(this.getTextureLocation(livingEntity)));
            } else {
                vertexConsumer = vertexConsumerProvider.getBuffer(RenderType.entityTranslucent(this.getTextureLocation(livingEntity)));
            }

            ((CowModel) this.getParentModel()).copyPropertiesTo(this.model);
            this.model.prepareMobModel(livingEntity, f, g, h);
            this.model.setupAnim(livingEntity, f, g, j, k, l);
            this.model.renderToBuffer(matrixStack, vertexConsumer, i, LivingEntityRenderer.getOverlayCoords(livingEntity, 0.0F));
        }
    }
}