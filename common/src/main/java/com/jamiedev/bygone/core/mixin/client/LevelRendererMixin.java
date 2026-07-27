package com.jamiedev.bygone.core.mixin.client;

import com.jamiedev.bygone.client.renderer.weather.WeatherRenderer;
import com.jamiedev.bygone.common.weather.BygoneWeather;
import com.jamiedev.bygone.core.registry.BGDimensions;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {

    @Shadow @Nullable
    private ClientLevel level;

    @Shadow
    @Final
    private static Logger LOGGER;

    @Inject(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/TickRateManager;runsNormally()Z",
            shift = At.Shift.AFTER
        )
    )
    private void bygone$tickRenderers(CallbackInfo ci) {
        assert level != null;
        if (!level.dimension().equals(BGDimensions.BYGONE_LEVEL_KEY)) return;
        if (!level.tickRateManager().runsNormally()) return;

        BygoneWeather.Client clientWeather = BygoneWeather.Client.getInstance();
        clientWeather.stream().forEach((renderer) -> renderer.tick(level));
    }
}
