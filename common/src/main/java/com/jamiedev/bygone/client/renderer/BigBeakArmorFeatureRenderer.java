package com.jamiedev.bygone.client.renderer;

import com.jamiedev.bygone.client.JamiesModModelLayers;
import com.jamiedev.bygone.client.models.BigBeakModel;
import com.jamiedev.bygone.items.CustomAnimalArmorItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.HorseArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import com.jamiedev.bygone.entities.BigBeakEntity;

public class BigBeakArmorFeatureRenderer extends RenderLayer<BigBeakEntity, BigBeakModel<BigBeakEntity>> {
    private final BigBeakModel<BigBeakEntity> model;


    HorseArmorLayer ref;

    public BigBeakArmorFeatureRenderer(RenderLayerParent<BigBeakEntity, BigBeakModel<BigBeakEntity>> context, EntityModelSet loader) {
        super(context);
        this.model = new BigBeakModel<>(loader.bakeLayer(JamiesModModelLayers.BIG_BEAK_ARMOR));
    }

    @Override
    public void render(PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i, BigBeakEntity BigBeakEntity, float f, float g, float h, float j, float k, float l) {
        ItemStack itemStack = BigBeakEntity.getBodyArmorItem();
        Item var13 = itemStack.getItem();
        if (var13 instanceof CustomAnimalArmorItem animalArmorItem) {
            if (animalArmorItem.getBodyType() == CustomAnimalArmorItem.BodyType.BIG_BEAK) {
                this.getParentModel().copyPropertiesTo(this.model);
                this.model.prepareMobModel(BigBeakEntity, f, g, h);
                this.model.setupAnim(BigBeakEntity, f, g, j, k, l);
                int m;
                if (itemStack.is(ItemTags.DYEABLE)) {
                    m = FastColor.ARGB32.opaque(DyedItemColor.getOrDefault(itemStack, -6265536));
                } else {
                    m = -1;
                }

                VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderType.entityCutoutNoCull(animalArmorItem.getTexture()));
                this.model.renderToBuffer(matrixStack, vertexConsumer, i, OverlayTexture.NO_OVERLAY, m);
            }
        }

    }
}
