package com.jamiedev.bygone.client.renderer.entity;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.client.JamiesModModelLayers;
import com.jamiedev.bygone.client.models.WallowModel;
import com.jamiedev.bygone.common.entity.WallowEntity;
import com.jamiedev.bygone.common.entity.WraithEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class WallowRenderer extends HauntingsMobRenderer<WallowEntity, WallowModel<WallowEntity>> {
    private static final ResourceLocation TEXTURE = Bygone.id("textures/entity/wallow.png");

    public WallowRenderer(EntityRendererProvider.Context context) {
        super(context, new WallowModel<>(context.bakeLayer(JamiesModModelLayers.WALLOW)), 0.5F);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull WallowEntity WallowEntity) {
        return TEXTURE;
    }
}
