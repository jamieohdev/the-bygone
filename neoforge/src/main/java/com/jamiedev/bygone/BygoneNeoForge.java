package com.jamiedev.bygone;


import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.jamiedev.bygone.client.BygoneClientNeoForge;
import com.jamiedev.bygone.common.block.entity.GumboPotBlockEntity;
import com.jamiedev.bygone.common.util.ServerTickHandler;
import com.jamiedev.bygone.common.util.VexDeathTracker;
import com.jamiedev.bygone.core.datagen.BygoneDataGenerator;
import com.jamiedev.bygone.core.registry.*;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
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

import java.util.Set;

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
                Items.MOSS_BLOCK, builder -> builder.set(
                        BGDataComponents.GUMBO_INGREDIENT_DATA.value(),
                        new GumboPotBlockEntity.GumboIngredientComponent(new FoodProperties.Builder().nutrition(2)
                                .saturationModifier(0.05f)
                                .build())
                )
        );
        event.modify(
                Items.MOSS_CARPET, builder -> builder.set(
                        BGDataComponents.GUMBO_INGREDIENT_DATA.value(),
                        new GumboPotBlockEntity.GumboIngredientComponent(new FoodProperties.Builder().nutrition(1)
                                .saturationModifier(0.05f)
                                .build())
                )
        );
        event.modify(
                BGBlocks.ALPHA_MOSS_BLOCK.get().asItem(), builder -> builder.set(
                        BGDataComponents.GUMBO_INGREDIENT_DATA.value(),
                        new GumboPotBlockEntity.GumboIngredientComponent(new FoodProperties.Builder().nutrition(2)
                                .saturationModifier(0.05f)
                                .build())
                )
        );
        event.modify(
                BGBlocks.ALPHA_MOSS_CARPET.get().asItem(), builder -> builder.set(
                        BGDataComponents.GUMBO_INGREDIENT_DATA.value(),
                        new GumboPotBlockEntity.GumboIngredientComponent(new FoodProperties.Builder().nutrition(1)
                                .saturationModifier(0.05f)
                                .build())
                )
        );
        event.modify(
                Items.STICK, builder -> builder.set(
                        BGDataComponents.GUMBO_INGREDIENT_DATA.value(),
                        new GumboPotBlockEntity.GumboIngredientComponent(new FoodProperties.Builder().nutrition(1)
                                .saturationModifier(0.02f)
                                .build())
                )
        );
        event.modify(
                Items.BAMBOO, builder -> builder.set(
                        BGDataComponents.GUMBO_INGREDIENT_DATA.value(),
                        new GumboPotBlockEntity.GumboIngredientComponent(new FoodProperties.Builder().nutrition(4)
                                .saturationModifier(0.3f)
                                .build())
                )
        );
        event.modify(
                Items.BONE, builder -> builder.set(
                        BGDataComponents.GUMBO_INGREDIENT_DATA.value(),
                        new GumboPotBlockEntity.GumboIngredientComponent(new FoodProperties.Builder().nutrition(4)
                                .saturationModifier(0.6f)
                                .build())
                )
        );
        event.modify(
                Items.SLIME_BALL, builder -> builder.set(
                        BGDataComponents.GUMBO_INGREDIENT_DATA.value(),
                        new GumboPotBlockEntity.GumboIngredientComponent(new FoodProperties.Builder().nutrition(2)
                                .saturationModifier(0.2f)
                                .effect(new MobEffectInstance(MobEffects.CONFUSION, 100, 0), 0.3f)
                                .build())
                )
        );
        event.modify(
                Items.LEATHER, builder -> builder.set(
                        BGDataComponents.GUMBO_INGREDIENT_DATA.value(),
                        new GumboPotBlockEntity.GumboIngredientComponent(new FoodProperties.Builder().nutrition(2)
                                .saturationModifier(0.2f)
                                .build())
                )
        );
        event.modify(
                Items.LEATHER_BOOTS, builder -> builder.set(
                        BGDataComponents.GUMBO_INGREDIENT_DATA.value(),
                        new GumboPotBlockEntity.GumboIngredientComponent(new FoodProperties.Builder().nutrition(6)
                                .saturationModifier(0.2f)
                                .build())
                )
        );
        event.modify(
                Items.LEATHER_CHESTPLATE, builder -> builder.set(
                        BGDataComponents.GUMBO_INGREDIENT_DATA.value(),
                        new GumboPotBlockEntity.GumboIngredientComponent(new FoodProperties.Builder().nutrition(8)
                                .saturationModifier(0.2f)
                                .build())
                )
        );
        event.modify(
                Items.LEATHER_HELMET, builder -> builder.set(
                        BGDataComponents.GUMBO_INGREDIENT_DATA.value(),
                        new GumboPotBlockEntity.GumboIngredientComponent(new FoodProperties.Builder().nutrition(6)
                                .saturationModifier(0.2f)
                                .build())
                )
        );
        event.modify(
                Items.LEATHER_LEGGINGS, builder -> builder.set(
                        BGDataComponents.GUMBO_INGREDIENT_DATA.value(),
                        new GumboPotBlockEntity.GumboIngredientComponent(new FoodProperties.Builder().nutrition(6)
                                .saturationModifier(0.2f)
                                .build())
                )
        );
        event.modify(
                BGBlocks.BLEMISH.get().asItem(), builder -> builder.set(
                        BGDataComponents.GUMBO_INGREDIENT_DATA.value(),
                        new GumboPotBlockEntity.GumboIngredientComponent(new FoodProperties.Builder().nutrition(2)
                                .saturationModifier(0.05f)
                                .build())
                )
        );
        event.modify(
                BGBlocks.BLEMISH_VEIN.get().asItem(), builder -> builder.set(
                        BGDataComponents.GUMBO_INGREDIENT_DATA.value(),
                        new GumboPotBlockEntity.GumboIngredientComponent(new FoodProperties.Builder().nutrition(2)
                                .saturationModifier(0.05f)
                                .build())
                )
        );
        event.modify(
                BGBlocks.ORANGE_MUSHROOM_BLOCK.get().asItem(), builder -> builder.set(
                        BGDataComponents.GUMBO_INGREDIENT_DATA.value(),
                        new GumboPotBlockEntity.GumboIngredientComponent(new FoodProperties.Builder().nutrition(3)
                                .saturationModifier(0.1f)
                                .effect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 200, 0), 0.05f)
                                .build())
                )
        );
        event.modify(
                BGBlocks.PINK_MUSHROOM_BLOCK.get().asItem(), builder -> builder.set(
                        BGDataComponents.GUMBO_INGREDIENT_DATA.value(),
                        new GumboPotBlockEntity.GumboIngredientComponent(new FoodProperties.Builder().nutrition(3)
                                .saturationModifier(0.1f)
                                .effect(new MobEffectInstance(MobEffects.REGENERATION, 100, 0), 0.05f)
                                .build())
                )
        );
        event.modify(
                BGBlocks.PURPLE_MUSHROOM_BLOCK.get().asItem(), builder -> builder.set(
                        BGDataComponents.GUMBO_INGREDIENT_DATA.value(),
                        new GumboPotBlockEntity.GumboIngredientComponent(new FoodProperties.Builder().nutrition(3)
                                .saturationModifier(0.1f)
                                .effect(new MobEffectInstance(MobEffects.WEAKNESS, 200, 0), 0.05f)
                                .build())
                )
        );
        event.modify(
                BGItems.ORANGE_FUNGI.get(), builder -> builder.set(
                        BGDataComponents.GUMBO_INGREDIENT_DATA.value(),
                        new GumboPotBlockEntity.GumboIngredientComponent(new FoodProperties.Builder().nutrition(3)
                                .saturationModifier(0.1f)
                                .effect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 400, 0), 0.05f)
                                .effect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 400, 0), 0.025f)
                                .build())
                )
        );
        event.modify(
                BGItems.PINK_FUNGI.get(), builder -> builder.set(
                        BGDataComponents.GUMBO_INGREDIENT_DATA.value(),
                        new GumboPotBlockEntity.GumboIngredientComponent(new FoodProperties.Builder().nutrition(3)
                                .saturationModifier(0.1f)
                                .effect(new MobEffectInstance(MobEffects.REGENERATION, 200, 0), 0.05f)
                                .effect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 400, 0), 0.025f)
                                .build())
                )
        );
        event.modify(
                BGItems.PURPLE_FUNGI.get(), builder -> builder.set(
                        BGDataComponents.GUMBO_INGREDIENT_DATA.value(),
                        new GumboPotBlockEntity.GumboIngredientComponent(new FoodProperties.Builder().nutrition(3)
                                .saturationModifier(0.1f)
                                .effect(new MobEffectInstance(MobEffects.WEAKNESS, 400, 0), 0.05f)
                                .effect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 400, 0), 0.025f)
                                .build())
                )
        );
        event.modify(
                Items.CRIMSON_FUNGUS, builder -> builder.set(
                        BGDataComponents.GUMBO_INGREDIENT_DATA.value(),
                        new GumboPotBlockEntity.GumboIngredientComponent(new FoodProperties.Builder().nutrition(3)
                                .saturationModifier(0.1f)
                                .effect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 400, 0), 0.05f)
                                .effect(new MobEffectInstance(MobEffects.WEAKNESS, 400, 0), 0.025f)
                                .build())
                )
        );
        event.modify(
                Items.CRIMSON_ROOTS, builder -> builder.set(
                        BGDataComponents.GUMBO_INGREDIENT_DATA.value(),
                        new GumboPotBlockEntity.GumboIngredientComponent(new FoodProperties.Builder().nutrition(1)
                                .saturationModifier(0.1f)
                                .effect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 400, 0), 0.05f)
                                .effect(new MobEffectInstance(MobEffects.WEAKNESS, 400, 0), 0.025f)
                                .build())
                )
        );
        event.modify(
                Items.NETHER_SPROUTS, builder -> builder.set(
                        BGDataComponents.GUMBO_INGREDIENT_DATA.value(),
                        new GumboPotBlockEntity.GumboIngredientComponent(new FoodProperties.Builder().nutrition(1)
                                .saturationModifier(0.1f)
                                .effect(new MobEffectInstance(MobEffects.CONFUSION, 100, 0), 0.05f)
                                .build())
                )
        );
        event.modify(
                Items.WARPED_FUNGUS, builder -> builder.set(
                        BGDataComponents.GUMBO_INGREDIENT_DATA.value(),
                        new GumboPotBlockEntity.GumboIngredientComponent(new FoodProperties.Builder().nutrition(3)
                                .saturationModifier(0.1f)
                                .effect(new MobEffectInstance(MobEffects.INVISIBILITY, 400, 0), 0.05f)
                                .effect(new MobEffectInstance(MobEffects.POISON, 400, 0), 0.025f)
                                .build())
                )
        );
        event.modify(
                Items.WARPED_ROOTS, builder -> builder.set(
                        BGDataComponents.GUMBO_INGREDIENT_DATA.value(),
                        new GumboPotBlockEntity.GumboIngredientComponent(new FoodProperties.Builder().nutrition(1)
                                .saturationModifier(0.1f)
                                .effect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 400, 0), 0.05f)
                                .effect(new MobEffectInstance(MobEffects.WEAKNESS, 400, 0), 0.025f)
                                .build())
                )
        );
        event.modify(
                Items.BROWN_MUSHROOM, builder -> builder.set(
                        BGDataComponents.GUMBO_INGREDIENT_DATA.value(),
                        new GumboPotBlockEntity.GumboIngredientComponent(new FoodProperties.Builder().nutrition(3)
                                .saturationModifier(0.1f)
                                .effect(new MobEffectInstance(MobEffects.DIG_SPEED, 100, 0), 0.05f)
                                .build())
                )
        );
        event.modify(
                Items.RED_MUSHROOM, builder -> builder.set(
                        BGDataComponents.GUMBO_INGREDIENT_DATA.value(),
                        new GumboPotBlockEntity.GumboIngredientComponent(new FoodProperties.Builder().nutrition(3)
                                .saturationModifier(0.1f)
                                .effect(new MobEffectInstance(MobEffects.JUMP, 100, 1), 0.05f)
                                .build())
                )
        );
        event.modify(
                Items.BROWN_MUSHROOM_BLOCK, builder -> builder.set(
                        BGDataComponents.GUMBO_INGREDIENT_DATA.value(),
                        new GumboPotBlockEntity.GumboIngredientComponent(new FoodProperties.Builder().nutrition(3)
                                .saturationModifier(0.1f)
                                .build())
                )
        );
        event.modify(
                Items.RED_MUSHROOM_BLOCK, builder -> builder.set(
                        BGDataComponents.GUMBO_INGREDIENT_DATA.value(),
                        new GumboPotBlockEntity.GumboIngredientComponent(new FoodProperties.Builder().nutrition(3)
                                .saturationModifier(0.1f)
                                .build())
                )
        );
        event.modify(
                Items.BROWN_MUSHROOM_BLOCK, builder -> builder.set(
                        BGDataComponents.GUMBO_INGREDIENT_DATA.value(),
                        new GumboPotBlockEntity.GumboIngredientComponent(new FoodProperties.Builder().nutrition(3)
                                .saturationModifier(0.1f)
                                .build())
                )
        );
        event.modify(
                Items.RED_MUSHROOM_BLOCK, builder -> builder.set(
                        BGDataComponents.GUMBO_INGREDIENT_DATA.value(),
                        new GumboPotBlockEntity.GumboIngredientComponent(new FoodProperties.Builder().nutrition(3)
                                .saturationModifier(0.1f)
                                .build())
                )
        );


        event.modify(
                BGBlocks.SHELF_FUNGUS.get().asItem(), builder -> builder.set(
                        BGDataComponents.GUMBO_INGREDIENT_DATA.value(),
                        new GumboPotBlockEntity.GumboIngredientComponent(new FoodProperties.Builder().nutrition(3)
                                .saturationModifier(0.1f)
                                .build())
                )
        );
        event.modify(
                BGBlocks.SHELF_MOLD.get().asItem(), builder -> builder.set(
                        BGDataComponents.GUMBO_INGREDIENT_DATA.value(),
                        new GumboPotBlockEntity.GumboIngredientComponent(new FoodProperties.Builder().nutrition(3)
                                .saturationModifier(0.1f)
                                .build())
                )
        );
        event.modify(
                BGBlocks.SHELF_MOLD_MOSS.get().asItem(), builder -> builder.set(
                        BGDataComponents.GUMBO_INGREDIENT_DATA.value(),
                        new GumboPotBlockEntity.GumboIngredientComponent(new FoodProperties.Builder().nutrition(2)
                                .saturationModifier(0.1f)
                                .build())
                )
        );
        event.modify(
                BGBlocks.SHELF_ROOTS.get().asItem(), builder -> builder.set(
                        BGDataComponents.GUMBO_INGREDIENT_DATA.value(),
                        new GumboPotBlockEntity.GumboIngredientComponent(new FoodProperties.Builder().nutrition(1)
                                .saturationModifier(0.05f)
                                .build())
                )
        );
        event.modify(
                BGBlocks.SHELF_SPROUTS.get().asItem(), builder -> builder.set(
                        BGDataComponents.GUMBO_INGREDIENT_DATA.value(),
                        new GumboPotBlockEntity.GumboIngredientComponent(new FoodProperties.Builder().nutrition(1)
                                .saturationModifier(0.05f)
                                .build())
                )
        );
        event.modify(
                BGBlocks.ORANGE_FUNGI_VINES.get().asItem(), builder -> builder.set(
                        BGDataComponents.GUMBO_INGREDIENT_DATA.value(),
                        new GumboPotBlockEntity.GumboIngredientComponent(new FoodProperties.Builder().nutrition(1)
                                .saturationModifier(0.05f)
                                .build())
                )
        );
        event.modify(
                BGBlocks.PINK_FUNGI_VINES.get().asItem(), builder -> builder.set(
                        BGDataComponents.GUMBO_INGREDIENT_DATA.value(),
                        new GumboPotBlockEntity.GumboIngredientComponent(new FoodProperties.Builder().nutrition(1)
                                .saturationModifier(0.05f)
                                .build())
                )
        );
        event.modify(
                BGBlocks.PURPLE_FUNGI_VINES.get().asItem(), builder -> builder.set(
                        BGDataComponents.GUMBO_INGREDIENT_DATA.value(),
                        new GumboPotBlockEntity.GumboIngredientComponent(new FoodProperties.Builder().nutrition(1)
                                .saturationModifier(0.05f)
                                .build())
                )
        );
        event.modify(
                BGBlocks.BELLADONNA.get().asItem(), builder -> builder.set(
                        BGDataComponents.GUMBO_INGREDIENT_DATA.value(),
                        new GumboPotBlockEntity.GumboIngredientComponent(new FoodProperties.Builder().nutrition(1)
                                .saturationModifier(0.02f)
                                .effect(new MobEffectInstance(MobEffects.WITHER, 400, 2), 1.0f)
                                .build())
                )
        );
    }

    void entityTick(EntityTickEvent.Post event) {
        Entity entity = event.getEntity();
        if (entity instanceof Cow cow && !entity.level().isClientSide) {
        //    Bygone.tickCow(cow);
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
        // TODO should these be enqueued (Startraveler)
        event.enqueueWork(() -> {

            Set<Block> validBlocks = Sets.newHashSet(BlockEntityType.BRUSHABLE_BLOCK.validBlocks);
            validBlocks.addAll(Sets.newHashSet(BGBlocks.SUSPICIOUS_CLAYSTONE.get()));
            BlockEntityType.BRUSHABLE_BLOCK.validBlocks = ImmutableSet.copyOf(validBlocks);


            BGDataComponentsNeoForge.init();
            Bygone.registerStrippables();
            Bygone.addFlammable();
            JamiesModPortalsNeoForge.init();
            GumboPotBlockEntity.GumboScooping.setFilled(Items.BOWL, BGItems.GUMBO_BOWL.get());
            GumboPotBlockEntity.GumboScooping.setFilled(Items.GLASS_BOTTLE, BGItems.GUMBO_BOTTLE.get());
        });
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