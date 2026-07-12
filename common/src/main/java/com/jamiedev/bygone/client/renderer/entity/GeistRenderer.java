package com.jamiedev.bygone.client.renderer.entity;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.client.JamiesModModelLayers;
import com.jamiedev.bygone.client.models.GeistModel;
import com.jamiedev.bygone.client.renderer.entity.layers.GeistEyesLayer;
import com.jamiedev.bygone.common.entity.GeistEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GeistRenderer extends MobRenderer<GeistEntity, GeistModel<GeistEntity>> {
    private static final ResourceLocation TEXTURE = Bygone.id("textures/entity/geist.png");


    public GeistRenderer(EntityRendererProvider.Context context) {
        super(context, new GeistModel<>(context.bakeLayer(JamiesModModelLayers.GEIST)), 0.5F);
        this.addLayer(new GeistEyesLayer<>(this));
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull GeistEntity GeistEntity) {
        return TEXTURE;
    }

    @Nullable
    @Override
    protected RenderType getRenderType(@NotNull GeistEntity entity, boolean bodyVisible, boolean translucent, boolean glowing) {
        if (bodyVisible || translucent) {
            return RenderType.entityTranslucent(this.getTextureLocation(entity));
        }
        return glowing ? RenderType.outline(this.getTextureLocation(entity)) : null;
    }
}
