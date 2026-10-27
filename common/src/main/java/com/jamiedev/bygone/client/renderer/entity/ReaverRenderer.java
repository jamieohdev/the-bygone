package com.jamiedev.bygone.client.renderer.entity;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.client.JamiesModModelLayers;
import com.jamiedev.bygone.client.models.ReaverModel;
import com.jamiedev.bygone.client.renderer.entity.layers.ReaverSpectralLayer;
import com.jamiedev.bygone.common.entity.ReaverEntity;
import com.jamiedev.bygone.core.registry.BGRenderTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ReaverRenderer extends HauntingsMobRenderer<ReaverEntity, ReaverModel<ReaverEntity>> {
    private static final ResourceLocation TEXTURE = Bygone.id("textures/entity/reaver.png");

    public ReaverRenderer(EntityRendererProvider.Context context) {
        super(context, new ReaverModel<>(context.bakeLayer(JamiesModModelLayers.REAVER)), 0.5F);
        this.addLayer(new ReaverSpectralLayer(this));
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull ReaverEntity reaverEntity) {
        return TEXTURE;
    }

    @Override
    public void render(@NotNull ReaverEntity reaverEntity, float entityYaw, float partialTicks,
                       @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight) {
        this.model.setBodyRenderBuffer(buffer, this.getTextureLocation(reaverEntity));
        try {
            super.render(reaverEntity, entityYaw, partialTicks, poseStack, buffer, packedLight);
        } finally {
            this.model.setBodyRenderBuffer(null, null);
            this.model.setDepthOnly(false);
        }
    }

    @Nullable
    @Override
    protected RenderType getRenderType(@NotNull ReaverEntity reaverEntity, boolean bodyVisible, boolean translucent, boolean glowing) {
        this.model.setDepthOnly(false);
        if (bodyVisible || translucent) {
            if (!reaverEntity.isInvisible()) {
                this.model.setDepthOnly(true);
                return BGRenderTypes.reaverBodyDepth(this.getTextureLocation(reaverEntity));
            }
            return RenderType.entityTranslucent(this.getTextureLocation(reaverEntity));
        }
        return glowing ? RenderType.outline(this.getTextureLocation(reaverEntity)) : null;
    }
}
