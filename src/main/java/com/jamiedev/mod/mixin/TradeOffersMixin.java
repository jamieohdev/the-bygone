package com.jamiedev.mod.mixin;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.jamiedev.mod.fabric.init.JamiesModTag;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.item.Items;
import net.minecraft.item.map.MapDecorationTypes;
import net.minecraft.registry.tag.StructureTags;
import net.minecraft.util.Util;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerProfession;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

@Mixin(TradeOffers.class)
public class TradeOffersMixin
{
        @Shadow
        public static final Map<VillagerProfession, Int2ObjectMap<TradeOffers.Factory[]>> PROFESSION_TO_LEVELED_TRADE = (Map)Util.make(Maps.newHashMap(), (map) -> {
           map.put(VillagerProfession.CARTOGRAPHER, copyToFastUtilMap(ImmutableMap.of(1, new TradeOffers.Factory[]
                   {
                   new TradeOffers.BuyItemFactory(Items.PAPER, 24, 16, 2),
                  new TradeOffers.SellItemFactory(Items.MAP, 7, 1, 1)}, 2,
                   new TradeOffers.Factory[]{new TradeOffers.BuyItemFactory(Items.GLASS_PANE, 11, 16, 10),
                   new TradeOffers.SellMapFactory(13, StructureTags.ON_OCEAN_EXPLORER_MAPS, "filled_map.monument", MapDecorationTypes.MONUMENT, 12, 5)}, 3,
                new TradeOffers.Factory[]{new TradeOffers.BuyItemFactory(Items.COMPASS, 1, 12, 20),
                  new TradeOffers.SellMapFactory(14, StructureTags.ON_WOODLAND_EXPLORER_MAPS, "filled_map.mansion", MapDecorationTypes.MANSION, 12, 10),
                        new TradeOffers.SellMapFactory(10, JamiesModTag.ON_BYGONE_PORTAL_MAPS, "Bygone Portal Map", MapDecorationTypes.BANNER_GREEN, 12, 10),
                        new TradeOffers.SellMapFactory(12, StructureTags.ON_TRIAL_CHAMBERS_MAPS, "filled_map.trial_chambers", MapDecorationTypes.TRIAL_CHAMBERS, 12, 10)}, 4,
                   new TradeOffers.Factory[]{new TradeOffers.SellItemFactory(Items.ITEM_FRAME, 7, 1, 15),
                           new TradeOffers.SellItemFactory(Items.WHITE_BANNER, 3, 1, 15),
                           new TradeOffers.SellItemFactory(Items.BLUE_BANNER, 3, 1, 15),
                           new TradeOffers.SellItemFactory(Items.LIGHT_BLUE_BANNER, 3, 1, 15),
                           new TradeOffers.SellItemFactory(Items.RED_BANNER, 3, 1, 15),
                           new TradeOffers.SellItemFactory(Items.PINK_BANNER, 3, 1, 15),
                           new TradeOffers.SellItemFactory(Items.GREEN_BANNER, 3, 1, 15),
                           new TradeOffers.SellItemFactory(Items.LIME_BANNER, 3, 1, 15),
                           new TradeOffers.SellItemFactory(Items.GRAY_BANNER, 3, 1, 15),
                           new TradeOffers.SellItemFactory(Items.BLACK_BANNER, 3, 1, 15),
                           new TradeOffers.SellItemFactory(Items.PURPLE_BANNER, 3, 1, 15),
                           new TradeOffers.SellItemFactory(Items.MAGENTA_BANNER, 3, 1, 15),
                           new TradeOffers.SellItemFactory(Items.CYAN_BANNER, 3, 1, 15),
                           new TradeOffers.SellItemFactory(Items.BROWN_BANNER, 3, 1, 15),
                           new TradeOffers.SellItemFactory(Items.YELLOW_BANNER, 3, 1, 15),
                           new TradeOffers.SellItemFactory(Items.ORANGE_BANNER, 3, 1, 15),
                           new TradeOffers.SellItemFactory(Items.LIGHT_GRAY_BANNER, 3, 1, 15)}, 5,
                   new TradeOffers.Factory[]{new TradeOffers.SellItemFactory(Items.GLOBE_BANNER_PATTERN, 8, 1, 30)})));
     });

    private static Int2ObjectMap<TradeOffers.Factory[]> copyToFastUtilMap(ImmutableMap<Integer, TradeOffers.Factory[]> map) {
        return new Int2ObjectOpenHashMap(map);
    }
}
