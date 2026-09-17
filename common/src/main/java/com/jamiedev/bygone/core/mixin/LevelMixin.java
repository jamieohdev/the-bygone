package com.jamiedev.bygone.core.mixin;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.common.weather.BygoneWeather;
import com.jamiedev.bygone.core.registry.BGDimensions;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Level.class)
public class LevelMixin {
    @Unique private final Level bygone$Self = (Level) (Object) this;
    @Unique boolean bygone$isBygone() {
        return bygone$Self.dimension().equals(BGDimensions.BYGONE_LEVEL_KEY);
    }

    @Inject(method = "getRainLevel", at = @At("HEAD"), cancellable = true)
    private void bygone$injectRainLevel(float delta, CallbackInfoReturnable<Float> cir) {
        if (bygone$isBygone()) cir.setReturnValue(0f);
    }

    @Inject(method = "getThunderLevel", at = @At("HEAD"), cancellable = true)
    private void bygone$injectThunderLevel(float delta, CallbackInfoReturnable<Float> cir) {
        if (bygone$isBygone()) cir.setReturnValue(0f);
    }
}
