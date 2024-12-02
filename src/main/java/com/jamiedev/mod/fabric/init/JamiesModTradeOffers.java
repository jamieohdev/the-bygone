package com.jamiedev.mod.fabric.init;

import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.item.map.MapDecorationTypes;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerProfession;

public class JamiesModTradeOffers
{
    public static void init() {
        TradeOfferHelper.registerVillagerOffers(VillagerProfession.CARTOGRAPHER, 2, //10 1 12 10
                factories -> factories.add(new TradeOffers.SellMapFactory(10, JamiesModTag.ON_BYGONE_PORTAL_MAPS, "Bygone Portal Map", MapDecorationTypes.BANNER_GREEN, 12, 10)));
        TradeOfferHelper.registerWanderingTraderOffers(1, factories -> {
            factories.add(new TradeOffers.SellItemFactory(JamiesModBlocks.ALPHA_MOSS_BLOCK, 2, 1, 6, 8));
            factories.add(new TradeOffers.SellMapFactory(10, JamiesModTag.ON_BYGONE_PORTAL_MAPS, "Bygone Portal Map", MapDecorationTypes.BANNER_GREEN, 12, 10));
        });
    }
}
