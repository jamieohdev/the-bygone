package com.jamiedev.mod.fabric.init;

import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.level.saveddata.maps.MapDecorationTypes;

public class JamiesModTradeOffers
{
    public static void init() {
        TradeOfferHelper.registerVillagerOffers(VillagerProfession.CARTOGRAPHER, 2, //10 1 12 10
                factories -> factories.add(new VillagerTrades.TreasureMapForEmeralds(10, JamiesModTag.ON_BYGONE_PORTAL_MAPS, "Bygone Portal Map", MapDecorationTypes.GREEN_BANNER, 12, 10)));
        TradeOfferHelper.registerWanderingTraderOffers(1, factories -> {
            factories.add(new VillagerTrades.ItemsForEmeralds(JamiesModBlocks.ALPHA_MOSS_BLOCK, 2, 1, 6, 8));
            factories.add(new VillagerTrades.TreasureMapForEmeralds(10, JamiesModTag.ON_BYGONE_PORTAL_MAPS, "Bygone Portal Map", MapDecorationTypes.GREEN_BANNER, 12, 10));
        });
    }
}
