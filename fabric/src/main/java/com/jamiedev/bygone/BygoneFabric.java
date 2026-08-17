package com.jamiedev.bygone;

import com.jamiedev.bygone.client.screen.PortalOverlay;
import com.jamiedev.bygone.common.block.entity.GumboPotBlockEntity;
import com.jamiedev.bygone.common.commands.BygoneWeatherCommand;
import com.jamiedev.bygone.common.util.ServerTickHandler;
import com.jamiedev.bygone.common.util.VexDeathTracker;
import com.jamiedev.bygone.common.weather.BygoneWeather;
import com.jamiedev.bygone.core.init.JamiesModTag;
import com.jamiedev.bygone.core.network.PacketHandler;
import com.jamiedev.bygone.core.registry.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.saveddata.maps.MapDecorationTypes;

public class BygoneFabric implements ModInitializer {


    public static void initEvents() {

        GumboPotBlockEntity.GumboScooping.setFilled(Items.BOWL, BGItems.GUMBO_BOWL.get());
        GumboPotBlockEntity.GumboScooping.setFilled(Items.GLASS_BOTTLE, BGItems.GUMBO_BOTTLE.get());

        DefaultItemComponentEvents.MODIFY.register(event -> {
            BGDataComponents.gumboBootstrap((item, component) ->
               event.modify(item, builder -> builder.set(BGDataComponents.GUMBO_INGREDIENT_DATA.value(), component)));
        });

        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            BlockPos pos = hitResult.getBlockPos();
            BlockState state = world.getBlockState(pos);
            ItemStack stack = player.getItemInHand(hand);

            if (stack.getItem() instanceof ShovelItem && (state.is(BGBlocks.MOSSY_CLAYSTONE.get()))) {
                BlockPos blockAbovePos = pos.above();
                BlockState blockAboveState = world.getBlockState(blockAbovePos);
                if (blockAboveState.isAir()) {
                    world.playSound(null, pos, SoundEvents.SHOVEL_FLATTEN, SoundSource.BLOCKS, 1.0F, 1.0F);
                    world.setBlock(pos, BGBlocks.MOSSY_CLAYSTONE_PATH.get().defaultBlockState(), Block.UPDATE_CLIENTS);

                    if (!player.isCreative()) {
                        stack.hurtAndBreak(1, player, player.getEquipmentSlotForItem(player.getUseItem()));
                    }
                    return InteractionResult.SUCCESS;
                }
            }

            if (stack.getItem() instanceof ShovelItem && (state.is(BGBlocks.ALPHA_MOSSY_CLAYSTONE.get()))) {
                BlockPos blockAbovePos = pos.above();
                BlockState blockAboveState = world.getBlockState(blockAbovePos);
                if (blockAboveState.isAir()) {
                    world.playSound(null, pos, SoundEvents.SHOVEL_FLATTEN, SoundSource.BLOCKS, 1.0F, 1.0F);
                    world.setBlock(pos, BGBlocks.ALPHA_MOSSY_CLAYSTONE_PATH.get().defaultBlockState(), Block.UPDATE_CLIENTS);

                    if (!player.isCreative()) {
                        stack.hurtAndBreak(1, player, player.getEquipmentSlotForItem(player.getUseItem()));
                    }
                    return InteractionResult.SUCCESS;
                }
            }

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

    static {
        BygoneWeather.WEATHER_TYPES = FabricRegistryBuilder.createSimple(
            BygoneWeather.WEATHER_TYPE_REGISTRY_KEY
        ).attribute(RegistryAttribute.SYNCED).buildAndRegister();
    }

    @Override
    public void onInitialize() {
        BGBlockEntitiesFabric.register();
        initEvents();
        BGDataComponentsFabric.init();
        BGAttributesFabric.init();
        Bygone.init();

        Bygone.registerBuiltIn();

        BygoneWeather.bootstrap(
            (weatherType) ->
                Registry.register(
                    BygoneWeather.WEATHER_TYPES,
                    weatherType.getKey(), weatherType
                )
        );

        BGPotionsFabric.init();

        Bygone.registerSpawnPlacements(SpawnPlacements::register);
        //JamiesModPortalsFabric.init();

        BGCriteria.init();

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
            BygoneWeatherCommand.register(dispatcher, registryAccess));

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
                            5
                    ));
                }
        );

//        CommandRegistrationCallback.EVENT.register(
//            (callback) -> {}
//        );

        Bygone.initAttributes(FabricDefaultAttributeRegistry::register);

        Bygone.addValidBlocks((type, block) -> type.addSupportedBlock(block));

        Bygone.registerStrippables();
        Bygone.addFlammable();

        PacketHandler.registerPackets();

        ServerLivingEntityEvents.AFTER_DEATH.register((entity, damageSource) -> {
            if (entity instanceof Vex vex && entity.level() instanceof ServerLevel serverLevel) {
                VexDeathTracker.onVexDeath(vex, serverLevel);
            }
        });

        ServerTickEvents.END_SERVER_TICK.register(ServerTickHandler::onServerTick);

    }
}
