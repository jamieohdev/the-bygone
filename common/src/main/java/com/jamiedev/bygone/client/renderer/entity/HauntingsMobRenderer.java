package com.jamiedev.bygone.client.renderer.entity;

import com.jamiedev.bygone.client.models.HauntingsMobModel;
import com.jamiedev.bygone.client.renderer.effect.HauntingsEffectRenderer;
import com.jamiedev.bygone.common.entity.HauntEntity;
import com.jamiedev.bygone.common.weather.weather_types.HauntingsEvent;
import com.jamiedev.bygone.core.extension.LivingEntityExtension;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.world.entity.Mob;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public abstract class HauntingsMobRenderer<T extends Mob, M extends HauntingsMobModel<T>> extends MobRenderer<T, M> {
    public HauntingsMobRenderer(EntityRendererProvider.Context context, M model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    @Override
    public void render(T entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        int fadeTicks = ((LivingEntityExtension) entity).bygone$getHauntingsFadeTicks();
        if (fadeTicks <= -1) fadeTicks = HauntingsEvent.DESPAWN_TICKS;
        model.setFadeAlpha((float) fadeTicks / HauntingsEvent.DESPAWN_TICKS);

        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Nullable @Override
    protected RenderType getRenderType(@NotNull T entity, boolean bodyVisible, boolean translucent, boolean glowing) {
        return RenderType.entityTranslucent(this.getTextureLocation(entity));
    }
}
