package com.jamiedev.bygone.core.registry;

import com.jamiedev.bygone.client.renderer.effect.HauntingsEffectRenderer;
import com.jamiedev.bygone.common.commands.BygoneWeatherCommand;
import com.jamiedev.bygone.common.weather.BygoneWeather;
import com.jamiedev.bygone.core.init.JamiesModTag;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.saveddata.maps.MapDecorationTypes;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.jamiedev.bygone.Bygone;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.village.WandererTradesEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

@EventBusSubscriber(modid = Bygone.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class BGEvents
{
    private static final VillagerTrades.ItemListing bygoneMapTrade = new VillagerTrades.TreasureMapForEmeralds(
            10,
            JamiesModTag.ON_BYGONE_PORTAL_MAPS,
            "Bygone Portal Map",
            MapDecorationTypes.GREEN_BANNER,
            12,
            5
    );

    @SubscribeEvent
    public static void addWanderingTraderTrades(WandererTradesEvent event)
    {
        List<VillagerTrades.ItemListing> rareList = event.getRareTrades();
        List<VillagerTrades.ItemListing> commonList = event.getGenericTrades();
        rareList.add(bygoneMapTrade);
    }

    @SubscribeEvent
    public static void onBrewingRecipeRegister(RegisterBrewingRecipesEvent event) {
        PotionBrewing.Builder builder = event.getBuilder();

        builder.addMix(Potions.AWKWARD, BGItems.LITHOPLASM.get(), BGPotionsNeoForge.HAUNTED_POTION);
        builder.addMix(BGPotionsNeoForge.HAUNTED_POTION, Items.REDSTONE, BGPotionsNeoForge.LONG_HAUNTED_POTION);
    }

    @SubscribeEvent
    public static void addCustomTrades(VillagerTradesEvent event) {
        if(event.getType() == VillagerProfession.CARTOGRAPHER) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();

            event.getTrades().get(1).add(new VillagerTrades.TreasureMapForEmeralds(
                    10,
                    JamiesModTag.ON_BYGONE_PORTAL_MAPS,
                    "Bygone Portal Map",
                    MapDecorationTypes.GREEN_BANNER,
                    12,
                    5
            ));
        }
    }

    @SubscribeEvent
    public static void onEntityJoinLevel(final EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            BygoneWeather weather = BygoneWeather.getOrDefault(serverPlayer.serverLevel());
            if (weather != null) weather.informPlayerOfState(serverPlayer);
        }
    }

    @SubscribeEvent
    public static void addCommands(final RegisterCommandsEvent event) {
        BygoneWeatherCommand.register(event.getDispatcher(), event.getBuildContext());
    }
}
