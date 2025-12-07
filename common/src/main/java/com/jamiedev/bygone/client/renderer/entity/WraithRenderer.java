package com.jamiedev.bygone.client.renderer.entity;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.client.JamiesModModelLayers;
import com.jamiedev.bygone.client.models.WraithModel;
import com.jamiedev.bygone.client.renderer.entity.layers.WraithEyesLayer;
import com.jamiedev.bygone.common.entity.WraithEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class WraithRenderer extends MobRenderer<WraithEntity, WraithModel<WraithEntity>> {
    private static final ResourceLocation TEXTURE = Bygone.id("textures/entity/wraith.png");


    public WraithRenderer(EntityRendererProvider.Context context) {
        super(context, new WraithModel<>(context.bakeLayer(JamiesModModelLayers.WRAITH)), 0.5F);
        this.addLayer(new WraithEyesLayer<>(this));
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull WraithEntity WraithEntity) {
        return TEXTURE;
    }
}
