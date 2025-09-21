package com.jamiedev.bygone;


import com.jamiedev.bygone.client.BygoneClientNeoForge;
import com.jamiedev.bygone.common.block.entity.GumboPotBlockEntity;
import com.jamiedev.bygone.common.util.ServerTickHandler;
import com.jamiedev.bygone.common.util.VexDeathTracker;
import com.jamiedev.bygone.core.datagen.BygoneDataGenerator;
import com.jamiedev.bygone.core.registry.*;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent;
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.Optional;

@Mod(Bygone.MOD_ID)
public class BygoneNeoForge {

    IPayloadContext ctx;

    public BygoneNeoForge(IEventBus eventBus, Dist dist) {
        BGMobEffectsNeoForge.init(eventBus);
        BGDataComponentsNeoForge.DATA_COMPONENTS.register(eventBus);
        Bygone.init();

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
        eventBus.addListener(this::modifyDefaultComponents);
        NeoForge.EVENT_BUS.addListener(this::entityTick);
        NeoForge.EVENT_BUS.addListener(this::damageEvent);
        NeoForge.EVENT_BUS.addListener(this::onLivingDeath);
        NeoForge.EVENT_BUS.addListener(this::onServerTick);
    }

    //

    public void modifyDefaultComponents(ModifyDefaultComponentsEvent event) {
        event.modify(
                Items.BOWL, builder -> builder.set(
                        BGDataComponents.GUMBO_SCOOP_DATA.value(),
                        new GumboPotBlockEntity.GumboScoopComponent(
                                BGItems.GUMBO_BOWL.get().builtInRegistryHolder().key(),
                                Optional.of(Items.BOWL.builtInRegistryHolder().key())
                        )
                )
        );
        event.modify(
                Items.GLASS_BOTTLE, builder -> builder.set(
                        BGDataComponents.GUMBO_SCOOP_DATA.value(),
                        new GumboPotBlockEntity.GumboScoopComponent(
                                BGItems.GUMBO_BOTTLE.get().builtInRegistryHolder().key(),
                                Optional.of(Items.GLASS_BOTTLE.builtInRegistryHolder().key())
                        )
                )
        );
    }

    void entityTick(EntityTickEvent.Post event) {
        Entity entity = event.getEntity();
        if (entity instanceof Cow cow && !entity.level().isClientSide) {
            Bygone.tickCow(cow);
        }
    }

    void damageEvent(LivingDamageEvent.Pre event) {

    }

    void onLivingDeath(LivingDeathEvent event) {
        //Bygone.LOGGER.info("Entity died: {} (type: {})", event.getEntity().getClass().getSimpleName(), event.getEntity().getType());
        if (event.getEntity() instanceof Vex vex && event.getEntity().level() instanceof ServerLevel serverLevel) {
            VexDeathTracker.onVexDeath(vex, serverLevel);
        }
    }

    void onServerTick(ServerTickEvent.Post event) {
        ServerTickHandler.onServerTick(event.getServer());
    }

    void createAttributes(EntityAttributeCreationEvent event) {
        Bygone.initAttributes(event::put);
    }


    void spawnPlacements(RegisterSpawnPlacementsEvent event) {
        Bygone.registerSpawnPlacements((entityType, spawnPlacementType, types, spawnPredicate) -> event.register(
                entityType,
                spawnPlacementType,
                types,
                spawnPredicate,
                RegisterSpawnPlacementsEvent.Operation.REPLACE
        ));
    }

    void setup(FMLCommonSetupEvent event) {
        BGDataComponentsNeoForge.init();
        Bygone.registerStrippables();
        Bygone.addFlammable();
        JamiesModPortalsNeoForge.init();
    }

    void addValidBlocks(BlockEntityTypeAddBlocksEvent event) {
        Bygone.addValidBlocks(event::modify);
    }

    void registerEvent(RegisterEvent event) {
        Registry<?> registry = event.getRegistry();

        if (registry == BuiltInRegistries.BLOCK) {
            AttachmentTypesNeoForge.init();
            Bygone.registerBuiltIn();
        }
    }

}