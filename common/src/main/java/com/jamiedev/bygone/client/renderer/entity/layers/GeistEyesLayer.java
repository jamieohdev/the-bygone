package com.jamiedev.bygone.client.renderer.entity.layers;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.client.models.GeistModel;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class GeistEyesLayer<T extends Entity, M extends GeistModel<T>> extends EyesLayer<T, M> {
    private static final RenderType Geist_EYES = RenderType.eyes(Bygone.id("textures/entity/geist_outer.png"));

    public GeistEyesLayer(RenderLayerParent<T, M> parent) {
        super(parent);
    }

    @Override
    public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight, @NotNull T livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!livingEntity.isInvisible()) {
            super.render(
                    poseStack,
                    buffer,
                    packedLight,
                    livingEntity,
                    limbSwing,
                    limbSwingAmount,
                    partialTicks,
                    ageInTicks,
                    netHeadYaw,
                    headPitch
            );
        }
    }

    public @NotNull RenderType renderType() {
        return Geist_EYES;
    }
}
