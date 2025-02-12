package com.jamiedev.bygone.client;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

public class BygoneClientNeoForge {

    public static void init(IEventBus eventBus) {
        eventBus.addListener(BygoneClientNeoForge::setup);
    }

    static void setup(FMLClientSetupEvent event) {
        BygoneClient.registerRenderLayers(ItemBlockRenderTypes::setRenderLayer);
    }

}
