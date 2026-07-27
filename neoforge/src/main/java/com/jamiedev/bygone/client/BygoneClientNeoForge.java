package com.jamiedev.bygone.client;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.BygoneNeoForge;
import com.jamiedev.bygone.client.particles.BlemishParticle;
import com.jamiedev.bygone.client.particles.UpsidedownDropParticle;
import com.jamiedev.bygone.client.screen.PortalOverlay;
import com.jamiedev.bygone.common.block.JamiesModWoodType;
import com.jamiedev.bygone.core.registry.BGParticleTypes;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;


public class BygoneClientNeoForge {

    public static void init(IEventBus eventBus) {
        eventBus.addListener(BygoneClientNeoForge::setup);
        eventBus.addListener(BygoneClientNeoForge::fluidRegister);
        eventBus.addListener(BygoneClientNeoForge::createRenderers);
        eventBus.addListener(BygoneClientNeoForge::createModelLayers);
        eventBus.addListener(BygoneClientNeoForge::registerParticleFactories);
        eventBus.addListener(BygoneClientNeoForge::renderGui);
    }

    public static void fluidRegister(final RegisterClientExtensionsEvent event) {
        event.registerFluidType(new LithoClientExtensions(), BygoneNeoForge.LITHO_TYPE.get());
    }

    static void setup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            BygoneClient.registerRenderLayers(ItemBlockRenderTypes::setRenderLayer);
            BygoneClient.registerModelPredicateProviders();
            Sheets.addWoodType(JamiesModWoodType.ANCIENT);
        });
    }


    static void createRenderers(EntityRenderersEvent.RegisterRenderers event) {
        BygoneClient.createEntityRenderers();
    }


    static void createModelLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        BygoneClient.createModelLayers(event::registerLayerDefinition);
    }


    static void registerParticleFactories(RegisterParticleProvidersEvent event) {
        BygoneClient.registerParticleFactories(event::registerSpriteSet);
        event.registerSpriteSet(BGParticleTypes.BLEMISH, BlemishParticle.BlemishBlockProvider::new);
        event.registerSpriteSet(BGParticleTypes.UPSIDEDOWN, UpsidedownDropParticle.Provider::new);

    }


    public static void renderGui(RegisterGuiLayersEvent event) {
        event.registerAbove(VanillaGuiLayers.CAMERA_OVERLAYS, ResourceLocation.fromNamespaceAndPath(Bygone.MOD_ID, "portal_overlay"), new PortalOverlay());
   }
}
