package com.jamiedev.bygone.core.registry;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.client.renderer.effect.HauntingsEffectRenderer;
import com.jamiedev.bygone.common.commands.BygoneWeatherCommand;
import com.jamiedev.bygone.common.weather.BygoneWeather;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;

import java.io.IOException;

@EventBusSubscriber(modid = Bygone.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class BGCommonSetupEvents {
    @SubscribeEvent
    public static void addRegistries(final NewRegistryEvent event) {
        event.register(BygoneWeather.WEATHER_TYPES);
    }

    @SubscribeEvent
    public static void registerShaders(final RegisterShadersEvent event) {
        HauntingsEffectRenderer.getInstance().resizeOrCreate();
    }
}
