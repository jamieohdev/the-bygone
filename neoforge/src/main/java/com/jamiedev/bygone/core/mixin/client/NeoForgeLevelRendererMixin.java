package com.jamiedev.bygone.core.mixin.client;

import com.jamiedev.bygone.common.weather.BygoneWeather;
import com.jamiedev.bygone.core.registry.BGDimensions;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(LevelRenderer.class)
public class NeoForgeLevelRendererMixin {

    @Shadow @Nullable
    private ClientLevel level;

    @Inject(
        method = "renderSnowAndRain",
        at = @At(value = "TAIL")
    )
    private void bygone$renderWeather(
        LightTexture lightTexture, float partialTick, double camX, double camY, double camZ, CallbackInfo ci
    ) {
        assert level != null;
        if (!level.dimension().equals(BGDimensions.BYGONE_LEVEL_KEY)) return;

        BygoneWeather.Client clientWeather = BygoneWeather.Client.getInstance();
        clientWeather.stream().forEach((renderer)
            -> renderer.render(level, lightTexture, partialTick, camX, camY, camZ)
        );
    }
}
