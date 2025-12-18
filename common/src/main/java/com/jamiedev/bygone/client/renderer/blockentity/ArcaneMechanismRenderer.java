package com.jamiedev.bygone.client.renderer.blockentity;

import com.jamiedev.bygone.common.block.entity.ArcaneMechanismEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;

public class ArcaneMechanismRenderer implements BlockEntityRenderer<ArcaneMechanismEntity>

{
    @Override
    public void render(ArcaneMechanismEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        float f = (float)blockEntity.tickCount + partialTick;
    }
}
