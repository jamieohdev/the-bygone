package com.jamiedev.mod.client.renderer;

import com.jamiedev.mod.JamiesMod;
import com.jamiedev.mod.client.JamiesModModelLayers;
import com.jamiedev.mod.client.models.BigBeakModel;
import com.jamiedev.mod.entities.BigBeakEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.feature.HorseArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.item.AnimalArmorItem;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;

public class BigBeakArmorFeatureRenderer extends FeatureRenderer<BigBeakEntity, BigBeakModel<BigBeakEntity>> {
    private final BigBeakModel<BigBeakEntity> model;


    HorseArmorFeatureRenderer ref;

    public BigBeakArmorFeatureRenderer(FeatureRendererContext<BigBeakEntity, BigBeakModel<BigBeakEntity>> context, EntityModelLoader loader) {
        super(context);
        this.model = new BigBeakModel<>(loader.getModelPart(JamiesModModelLayers.BIG_BEAK_ARMOR));
    }

    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, BigBeakEntity BigBeakEntity, float f, float g, float h, float j, float k, float l) {
        ItemStack itemStack = BigBeakEntity.getBodyArmor();
        Item var13 = itemStack.getItem();
        if (var13 instanceof AnimalArmorItem animalArmorItem) {
            if (animalArmorItem.getType() == JamiesMod.BIG_BEAK_ARMOR) {
                ((BigBeakModel)this.getContextModel()).copyStateTo(this.model);
                this.model.animateModel(BigBeakEntity, f, g, h);
                this.model.setAngles(BigBeakEntity, f, g, j, k, l);
                int m;
                if (itemStack.isIn(ItemTags.DYEABLE)) {
                    m = ColorHelper.Argb.fullAlpha(DyedColorComponent.getColor(itemStack, -6265536));
                } else {
                    m = -1;
                }

                VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutoutNoCull(animalArmorItem.getEntityTexture()));
                this.model.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, m);
                return;
            }
        }

    }
}
