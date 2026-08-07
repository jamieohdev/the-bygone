package com.jamiedev.bygone.client.renderer.entity;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.client.JamiesModModelLayers;
import com.jamiedev.bygone.client.models.ScareModel2;
import com.jamiedev.bygone.common.entity.ScareEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ScareRenderer extends MobRenderer<ScareEntity, ScareModel2<ScareEntity>> {
    private static final ResourceLocation TEXTURE = Bygone.id("textures/entity/scare.png");

    public ScareRenderer(EntityRendererProvider.Context context) {
        super(context, new ScareModel2<>(context.bakeLayer(JamiesModModelLayers.SCARE)), 0.5F);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull ScareEntity entity) {
        return TEXTURE;
    }

    @Nullable
    @Override
    protected RenderType getRenderType(@NotNull ScareEntity entity, boolean bodyVisible, boolean translucent, boolean glowing) {
        if (bodyVisible || translucent) {
            return RenderType.entityTranslucent(this.getTextureLocation(entity));
        }
        return glowing ? RenderType.outline(this.getTextureLocation(entity)) : null;
    }
}
