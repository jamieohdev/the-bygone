package com.jamiedev.bygone;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.jamiedev.bygone.common.block.entity.GumboPotBlockEntity;
import com.jamiedev.bygone.common.util.ServerTickHandler;
import com.jamiedev.bygone.common.util.VexDeathTracker;
import com.jamiedev.bygone.core.init.JamiesModTag;
import com.jamiedev.bygone.core.network.PacketHandler;
import com.jamiedev.bygone.core.registry.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.saveddata.maps.MapDecorationTypes;

import java.util.Set;

public class BygoneFabric implements ModInitializer {


    public static void initEvents() {

        GumboPotBlockEntity.GumboScooping.setFilled(Items.BOWL, BGItems.GUMBO_BOWL.get());
        GumboPotBlockEntity.GumboScooping.setFilled(Items.GLASS_BOTTLE, BGItems.GUMBO_BOTTLE.get());

        DefaultItemComponentEvents.MODIFY.register(event -> {

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
        });


        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            BlockPos pos = hitResult.getBlockPos();
            BlockState state = world.getBlockState(pos);
            ItemStack stack = player.getItemInHand(hand);


            if (stack.getItem() instanceof HoeItem && (state.is(BGBlocks.CLAYSTONE.get()) || state.is(BGBlocks.MOSSY_CLAYSTONE.get()))) {
                BlockPos blockAbovePos = pos.above();
                BlockState blockAboveState = world.getBlockState(blockAbovePos);
                if (blockAboveState.isAir()) {
                    world.playSound(null, pos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                    world.setBlock(pos, BGBlocks.CLAYSTONE_FARMLAND.get().defaultBlockState(), Block.UPDATE_CLIENTS);

                    if (!player.isCreative()) {
                        stack.hurtAndBreak(1, player, player.getEquipmentSlotForItem(player.getUseItem()));

                    }

                    return InteractionResult.SUCCESS;
                }
            }
            if (stack.getItem() instanceof HoeItem && (state.is(BGBlocks.COARSE_CLAYSTONE.get()))) {
                BlockPos blockAbovePos = pos.above();
                BlockState blockAboveState = world.getBlockState(blockAbovePos);
                if (blockAboveState.isAir()) {
                    world.playSound(null, pos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                    world.setBlock(pos, BGBlocks.CLAYSTONE.get().defaultBlockState(), Block.UPDATE_CLIENTS);

                    if (!player.isCreative()) {
                        stack.hurtAndBreak(1, player, player.getEquipmentSlotForItem(player.getUseItem()));

                    }

                    return InteractionResult.SUCCESS;
                }
            }
            return InteractionResult.PASS;
        });
    }

    @Override
    public void onInitialize() {

        Set<Block> validBlocks = Sets.newHashSet(BlockEntityType.BRUSHABLE_BLOCK.validBlocks);
        validBlocks.addAll(Sets.newHashSet(BGBlocks.SUSPICIOUS_CLAYSTONE.get(), BGBlocks.SUSPICIOUS_SHELLSAND.get()));
        BlockEntityType.BRUSHABLE_BLOCK.validBlocks = ImmutableSet.copyOf(validBlocks);

        initEvents();
        BGMobEffectsFabric.init();
        BGDataComponentsFabric.init();
        Bygone.init();

        Bygone.registerBuiltIn();

        Bygone.registerSpawnPlacements(SpawnPlacements::register);
        JamiesModPortalsFabric.init();

        BGCriteria.init();

        TradeOfferHelper.registerVillagerOffers(
                VillagerProfession.CARTOGRAPHER, 2, //10 1 12 10
                factories -> factories.add(new VillagerTrades.TreasureMapForEmeralds(
                        10,
                        JamiesModTag.ON_BYGONE_PORTAL_MAPS,
                        "Bygone Portal Map",
                        MapDecorationTypes.GREEN_BANNER,
                        12,
                        10
                ))
        );
        TradeOfferHelper.registerWanderingTraderOffers(
                1, factories -> {
                    factories.add(new VillagerTrades.ItemsForEmeralds(BGBlocks.ALPHA_MOSS_BLOCK.get(), 2, 1, 6, 8));
                    factories.add(new VillagerTrades.TreasureMapForEmeralds(
                            10,
                            JamiesModTag.ON_BYGONE_PORTAL_MAPS,
                            "Bygone Portal Map",
                            MapDecorationTypes.GREEN_BANNER,
                            12,
                            10
                    ));
                }
        );

        Bygone.initAttributes(FabricDefaultAttributeRegistry::register);


        Bygone.addValidBlocks((type, block) -> type.addSupportedBlock(block));

        Bygone.registerStrippables();
        Bygone.addFlammable();
        Bygone.LOGGER.info("Registering Entities for {}", Bygone.MOD_ID);

        PacketHandler.registerPackets();

        ServerLivingEntityEvents.AFTER_DEATH.register((entity, damageSource) -> {
            if (entity instanceof Vex vex && entity.level() instanceof ServerLevel serverLevel) {
                VexDeathTracker.onVexDeath(vex, serverLevel);
            }
        });

        ServerTickEvents.END_SERVER_TICK.register(ServerTickHandler::onServerTick);

    }
}
