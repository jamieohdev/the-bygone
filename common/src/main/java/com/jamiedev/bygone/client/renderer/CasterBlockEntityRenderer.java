package com.jamiedev.bygone.client.renderer;

import com.jamiedev.bygone.common.block.entity.CasterBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class CasterBlockEntityRenderer implements BlockEntityRenderer<CasterBlockEntity> {

    public CasterBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {}

    @Override
    public void render(CasterBlockEntity entity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
//        if (entity.renderSpike && !entity.isRemoved()) {
//            matrices.push();
//            Direction d = entity.getWorld().getBlockState(entity.getPos()).get(CasterBlock.FACING);
//            Vec3i direction = d.getVector();
//            matrices.translate(direction.getX() + 0.5, direction.getY() - 0.5, direction.getZ() + 0.5);
//            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(d.asRotation()));
//            if (d == Direction.NORTH || d == Direction.SOUTH)
//                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90));
//            else if (d == Direction.EAST || d == Direction.WEST)
//                matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(90));
//            matrices.pop();
//        }
    }
}
