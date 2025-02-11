package com.jamiedev.mod.common.client.renderer;

import com.jamiedev.mod.common.blocks.entity.BygoneBrushableBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class BygoneBrushableBlockEntityRenderer  implements BlockEntityRenderer<BygoneBrushableBlockEntity> {
    private final ItemRenderer itemRenderer;

    public BygoneBrushableBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = context.getItemRenderer();
    }

    public void render(BygoneBrushableBlockEntity brushableBlockEntity, float f, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i, int j) {
        if (brushableBlockEntity.getLevel() != null) {
            int k = (Integer)brushableBlockEntity.getBlockState().getValue(BlockStateProperties.DUSTED);
            if (k > 0) {
                Direction direction = brushableBlockEntity.getHitDirection();
                if (direction != null) {
                    ItemStack itemStack = brushableBlockEntity.getItem();
                    if (!itemStack.isEmpty()) {
                        matrixStack.pushPose();
                        matrixStack.translate(0.0F, 0.5F, 0.0F);
                        float[] fs = this.getTranslation(direction, k);
                        matrixStack.translate(fs[0], fs[1], fs[2]);
                        matrixStack.mulPose(Axis.YP.rotationDegrees(75.0F));
                        boolean bl = direction == Direction.EAST || direction == Direction.WEST;
                        matrixStack.mulPose(Axis.YP.rotationDegrees((float)((bl ? 90 : 0) + 11)));
                        matrixStack.scale(0.5F, 0.5F, 0.5F);
                        int l = LevelRenderer.getLightColor(brushableBlockEntity.getLevel(), brushableBlockEntity.getBlockState(), brushableBlockEntity.getBlockPos().relative(direction));
                        this.itemRenderer.renderStatic(itemStack, ItemDisplayContext.FIXED, l, OverlayTexture.NO_OVERLAY, matrixStack, vertexConsumerProvider, brushableBlockEntity.getLevel(), 0);
                        matrixStack.popPose();
                    }
                }
            }
        }
    }

    private float[] getTranslation(Direction direction, int dustedLevel) {
        float[] fs = new float[]{0.5F, 0.0F, 0.5F};
        float f = (float)dustedLevel / 10.0F * 0.75F;
        switch (direction) {
            case EAST:
                fs[0] = 0.73F + f;
                break;
            case WEST:
                fs[0] = 0.25F - f;
                break;
            case UP:
                fs[1] = 0.25F + f;
                break;
            case DOWN:
                fs[1] = -0.23F - f;
                break;
            case NORTH:
                fs[2] = 0.25F - f;
                break;
            case SOUTH:
                fs[2] = 0.73F + f;
        }

        return fs;
    }
}
