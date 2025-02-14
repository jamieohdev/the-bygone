package com.jamiedev.bygone.client;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

public class BygoneClientNeoForge {

    public static void init(IEventBus eventBus) {
        eventBus.addListener(BygoneClientNeoForge::setup);
        eventBus.addListener(BygoneClientNeoForge::createRenderers);
        eventBus.addListener(BygoneClientNeoForge::createModelLayers);
    }

    static void setup(FMLClientSetupEvent event) {
        BygoneClient.registerRenderLayers(ItemBlockRenderTypes::setRenderLayer);
        BygoneClient.registerModelPredicateProviders();
    }

    static void createRenderers(EntityRenderersEvent.RegisterRenderers event) {
        BygoneClient.createEntityRenderers();
    }

    static void createModelLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        BygoneClient.createModelLayers(event::registerLayerDefinition);
    }

}
