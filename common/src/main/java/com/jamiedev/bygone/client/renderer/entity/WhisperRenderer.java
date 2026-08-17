package com.jamiedev.bygone.client.renderer.entity;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.client.JamiesModModelLayers;
import com.jamiedev.bygone.client.models.WhisperModel;
import com.jamiedev.bygone.common.entity.WhisperEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WhisperRenderer extends HauntingsMobRenderer<WhisperEntity, WhisperModel<WhisperEntity>> {
    private static final ResourceLocation TEXTURE = Bygone.id("textures/entity/whisper.png");

    public WhisperRenderer(EntityRendererProvider.Context context) {
        super(context, new WhisperModel<>(context.bakeLayer(JamiesModModelLayers.WHISPER)), 0.4F);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull WhisperEntity entity) {
        return TEXTURE;
    }

    @Override
    protected int getBlockLightLevel(@NotNull WhisperEntity entity, @NotNull BlockPos pos) {
        return 15;
    }

    @Nullable
    @Override
    protected RenderType getRenderType(@NotNull WhisperEntity entity, boolean bodyVisible, boolean translucent, boolean glowing) {
        if (bodyVisible || translucent) {
            return RenderType.entityTranslucent(this.getTextureLocation(entity));
        }
        return glowing ? RenderType.outline(this.getTextureLocation(entity)) : null;
    }
}
