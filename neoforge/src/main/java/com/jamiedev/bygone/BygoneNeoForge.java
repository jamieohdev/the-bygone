package com.jamiedev.bygone;


import com.jamiedev.bygone.client.BygoneClientNeoForge;
import com.jamiedev.bygone.datagen.BygoneDataGenerator;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

@Mod(Bygone.MOD_ID)
public class BygoneNeoForge {

    public BygoneNeoForge(IEventBus eventBus, Dist dist) {
        eventBus.addListener(PacketHandlerNeoForge::register);
        if (dist.isClient()) {
            BygoneClientNeoForge.init(eventBus);
        }
        eventBus.addListener(this::registerEvent);
        eventBus.addListener(BygoneDataGenerator::onInitializeDataGenerator);
        eventBus.addListener(this::setup);
        eventBus.addListener(this::spawnPlacements);
        eventBus.addListener(this::createAttributes);
        eventBus.addListener(this::addValidBlocks);
        NeoForge.EVENT_BUS.addListener(this::damageEvent);
        Bygone.init();
    }

    void damageEvent(LivingDamageEvent.Pre event) {

    }

    void createAttributes(EntityAttributeCreationEvent event) {
        Bygone.initAttributes(event::put);
    }

    void spawnPlacements(RegisterSpawnPlacementsEvent event) {
        Bygone.registerSpawnPlacements((entityType, spawnPlacementType, types, spawnPredicate) -> event.register(entityType,spawnPlacementType,types,spawnPredicate, RegisterSpawnPlacementsEvent.Operation.REPLACE));
    }

    void setup(FMLCommonSetupEvent event) {
        Bygone.registerStrippables();
    }

    void addValidBlocks(BlockEntityTypeAddBlocksEvent event) {
        Bygone.addValidBlocks(event::modify);
    }

    void registerEvent(RegisterEvent event) {
        Registry<?> registry = event.getRegistry();

        if (registry == BuiltInRegistries.BLOCK) {
            Bygone.registerBuiltIn();
        }
    }

}