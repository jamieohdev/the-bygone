package com.jamiedev.bygone.core.mixin;

import com.jamiedev.bygone.common.weather.BygoneWeather;
import com.jamiedev.bygone.core.registry.BGDimensions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLevel.class)
public class ServerLevelMixinFabric {
    @Inject(method = "addPlayer", at = @At("HEAD"))
    private void bygone$syncFabricWeather(ServerPlayer player, CallbackInfo ci) {
        if (!player.serverLevel().dimension().equals(BGDimensions.BYGONE_LEVEL_KEY)) return;
        BygoneWeather.getOrDefault(player.serverLevel()).informPlayerOfState(player);
    }
}
