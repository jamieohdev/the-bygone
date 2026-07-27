package com.jamiedev.bygone.core.mixin;

import com.jamiedev.bygone.common.weather.BygoneWeather;
import com.jamiedev.bygone.core.registry.BGDimensions;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin {
    @Shadow public abstract ServerLevel getLevel();

    @Inject(method = "advanceWeatherCycle", at = @At("TAIL"))
    private void bygone$advanceWeatherCycle(CallbackInfo ci) {
        if (!this.getLevel().dimension().equals(BGDimensions.BYGONE_LEVEL_KEY)) return;
        BygoneWeather.getOrDefault(this.getLevel()).tick();
    }
}
