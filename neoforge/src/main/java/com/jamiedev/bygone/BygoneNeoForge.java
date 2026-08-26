package com.jamiedev.bygone;


import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.jamiedev.bygone.client.BygoneClientNeoForge;
import com.jamiedev.bygone.common.block.entity.GumboPotBlockEntity;
import com.jamiedev.bygone.common.util.ServerTickHandler;
import com.jamiedev.bygone.common.util.VexDeathTracker;
import com.jamiedev.bygone.common.weather.BygoneWeather;
import com.jamiedev.bygone.common.weather.weather_types.HauntingsCategoryHolder;
import com.jamiedev.bygone.core.datagen.BygoneDataGenerator;
import com.jamiedev.bygone.core.registry.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.PathType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent;
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.function.Supplier;

@Mod(Bygone.MOD_ID)
public class BygoneNeoForge {
    public static DeferredRegister<Fluid> fluidRegister = DeferredRegister.create(Registries.FLUID, Bygone.MOD_ID);

    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(
            NeoForgeRegistries.Keys.FLUID_TYPES,
            Bygone.MOD_ID
    );

    public static final Supplier<FluidType> LITHO_TYPE = FLUID_TYPES.register(
            "litho_type",
            () -> new FluidType(
                    FluidType.Properties.create()
                            .descriptionId("block.bygone.litho")
                            .canSwim(true)
                            .canDrown(true)
                            .pathType(PathType.WATER)
                            .adjacentPathType(null)
                            .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_EMPTY)
                            .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
                            .lightLevel(8)
            ) {

                @Override
                public boolean canConvertToSource(@NotNull FluidState state, @NotNull LevelReader reader, @NotNull BlockPos pos) {
                    if (reader instanceof Level level) {
                        return level.getGameRules().getBoolean(GameRules.RULE_WATER_SOURCE_CONVERSION);
                    } else {
                        return super.canConvertToSource(state, reader, pos);
                    }
                }
            }
    );

    static {
        BygoneWeather.WEATHER_TYPES = new RegistryBuilder<>(BygoneWeather.WEATHER_TYPE_REGISTRY_KEY).create();
    }

    public BygoneNeoForge(IEventBus eventBus, Dist dist) {
        BGDataComponentsNeoForge.DATA_COMPONENTS.register(eventBus);
        BGDecoratedPotPatternsNeoForge.POT_PATTERNS.register(eventBus);
        BGAttributesNeoForge.init(eventBus);
        Bygone.init();

        fluidRegister.register(eventBus);
        FLUID_TYPES.register(eventBus);

        BGPotionsNeoForge.register(eventBus);

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
        eventBus.addListener(BGDataComponentsNeoForge::init);
        NeoForge.EVENT_BUS.addListener(this::blockModifications);
        NeoForge.EVENT_BUS.addListener(this::damageEvent);
        NeoForge.EVENT_BUS.addListener(this::onLivingDeath);
        NeoForge.EVENT_BUS.addListener(this::onServerTick);
    }

    private void blockModifications(final BlockEvent.BlockToolModificationEvent event) {
        if (event.getItemAbility() == ItemAbilities.HOE_TILL && event.getLevel().getBlockState(event.getPos().above()).isAir()) {
            BlockState state = event.getState();
            if (state.is(BGBlocks.CLAYSTONE.get()))
                event.setFinalState(BGBlocks.CLAYSTONE_FARMLAND.get().defaultBlockState());
            else if (state.is(BGBlocks.COARSE_CLAYSTONE.get()))
                event.setFinalState(BGBlocks.CLAYSTONE.get().defaultBlockState());
        }
        if (event.getItemAbility() == ItemAbilities.SHOVEL_FLATTEN && event.getLevel().getBlockState(event.getPos().above()).isAir()) {
            BlockState state = event.getState();
            if (state.is(BGBlocks.MOSSY_CLAYSTONE.get()))
                event.setFinalState(BGBlocks.MOSSY_CLAYSTONE_PATH.get().defaultBlockState());
            else if (state.is(BGBlocks.ALPHA_MOSSY_CLAYSTONE.get()))
                event.setFinalState(BGBlocks.ALPHA_MOSSY_CLAYSTONE_PATH.get().defaultBlockState());
        }
    }

    public void modifyDefaultComponents(ModifyDefaultComponentsEvent event) {
        BGDataComponents.gumboBootstrap((item, component) ->
            event.modify(item, builder -> builder.set(BGDataComponents.GUMBO_INGREDIENT_DATA.value(), component))
        );
    }

    void damageEvent(LivingDamageEvent.Pre event) {

    }

    void onLivingDeath(LivingDeathEvent event) {
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
        // TODO should these be enqueued (Startraveler)
        event.enqueueWork(() -> {

            Set<Block> validBlocks = Sets.newHashSet(BlockEntityType.BRUSHABLE_BLOCK.validBlocks);
            validBlocks.addAll(Sets.newHashSet(BGBlocks.SUSPICIOUS_SHELLSAND.get(), BGBlocks.SUSPICIOUS_CLAYSTONE.get()));
            BlockEntityType.BRUSHABLE_BLOCK.validBlocks = ImmutableSet.copyOf(validBlocks);

            //BGDataComponentsNeoForge.init();
            BGDecoratedPotPatternsNeoForge.expandVanilla();
            Bygone.registerStrippables();
            Bygone.addFlammable();
            //JamiesModPortalsNeoForge.init();
            GumboPotBlockEntity.GumboScooping.setFilled(Items.BOWL, BGItems.GUMBO_BOWL.get());
            GumboPotBlockEntity.GumboScooping.setFilled(Items.GLASS_BOTTLE, BGItems.GUMBO_BOTTLE.get());
        });
    }

    void addValidBlocks(BlockEntityTypeAddBlocksEvent event) {
        Bygone.addValidBlocks(event::modify);
    }

    void registerEvent(RegisterEvent event) {
        Registry<?> registry = event.getRegistry();

        if (registry == BuiltInRegistries.TRIGGER_TYPES)
            BGCriteria.init();

        if (registry == BuiltInRegistries.BLOCK) {
            AttachmentTypesNeoForge.init();
            Bygone.registerBuiltIn();
        }

        if (registry != BygoneWeather.WEATHER_TYPES) return;

        BygoneWeather.bootstrap(
            (weatherType) -> {
                event.register(
                    BygoneWeather.WEATHER_TYPE_REGISTRY_KEY,
                    register -> register.register(
                        weatherType.getKey(), weatherType
                    )
                );
            }
        );
    }

}