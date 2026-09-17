package com.jamiedev.bygone.client.renderer.entity;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.client.JamiesModModelLayers;
import com.jamiedev.bygone.client.models.MournModel;
import com.jamiedev.bygone.common.entity.MournEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MournRenderer extends HauntingsMobRenderer<MournEntity, MournModel<MournEntity>> {
    private static final ResourceLocation TEXTURE = Bygone.id("textures/entity/mourn.png");

    public MournRenderer(EntityRendererProvider.Context context) {
        super(context, new MournModel<>(context.bakeLayer(JamiesModModelLayers.MOURN)), 0.5F);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull MournEntity entity) {
        return TEXTURE;
    }

    @Nullable
    @Override
    protected RenderType getRenderType(@NotNull MournEntity entity, boolean bodyVisible, boolean translucent, boolean glowing) {
        if (bodyVisible || translucent) {
            return RenderType.entityTranslucent(this.getTextureLocation(entity));
        }
        return glowing ? RenderType.outline(this.getTextureLocation(entity)) : null;
    }
}
