package com.jamiedev.bygone.core.registry;

import com.jamiedev.bygone.core.init.JamiesModTag;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.saveddata.maps.MapDecorationTypes;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.jamiedev.bygone.Bygone;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(modid = Bygone.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class BGEvents
{
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
}
