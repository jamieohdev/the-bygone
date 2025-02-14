package com.jamiedev.bygone;

import com.jamiedev.bygone.init.JamiesModBlocks;
import com.jamiedev.bygone.init.JamiesModItems;
import net.kyrptonaught.customportalapi.api.CustomPortalBuilder;
import net.kyrptonaught.customportalapi.portal.PortalIgnitionSource;
import net.minecraft.world.item.ItemStack;

public class JamiesModPortalsFabric {

    public static void init() {
        CustomPortalBuilder.beginPortal()
                .frameBlock(JamiesModBlocks.BYGONE_PORTAL_FRAME)
                .customPortalBlock(JamiesModBlocks.BYGONE_PORTAL)
                .destDimID(Bygone.id("bygone"))
                .tintColor(0, 1, 0)
                .setPortalSearchYRange(0, 120)
                .flatPortal()
                .lightWithItem(JamiesModItems.ARCANE_CORE)
                .registerIgniteEvent((player, world, portalPos, framePos, portalIgnitionSource) -> {
                    if (portalIgnitionSource.sourceType == PortalIgnitionSource.SourceType.USEITEM && player != null) {
                        if (player.isCreative())
                            return;
                        ItemStack heldItem = player.getMainHandItem().getItem() == JamiesModItems.HOOK ?
                                player.getMainHandItem() : player.getOffhandItem();

                        heldItem.hurtAndBreak(1 , player, player.getEquipmentSlotForItem(player.getUseItem()));
                    }
                })
                .registerPortal();
        CustomPortalBuilder.beginPortal()
                .frameBlock(JamiesModBlocks.BYGONE_PORTAL_FRAME_PLACEABLE)
                .customPortalBlock(JamiesModBlocks.BYGONE_PORTAL)
                .destDimID(Bygone.id("bygone"))
                .tintColor(0, 1, 0)
                .setPortalSearchYRange(0, 120)
                .flatPortal()
                .lightWithItem(JamiesModItems.ARCANE_CORE)
                .onlyLightInOverworld()
                .registerIgniteEvent((player, world, portalPos, framePos, portalIgnitionSource) -> {
                    if (portalIgnitionSource.sourceType == PortalIgnitionSource.SourceType.USEITEM && player != null) {
                        if (player.isCreative())
                            return;
                        ItemStack heldItem = player.getMainHandItem().getItem() == JamiesModItems.HOOK ?
                                player.getMainHandItem() : player.getOffhandItem();

                        heldItem.hurtAndBreak(1 , player, player.getEquipmentSlotForItem(player.getUseItem()));
                    }
                })
                .registerPortal();
        CustomPortalBuilder.beginPortal()
                .frameBlock(JamiesModBlocks.BYGONE_PORTAL_FRAME_BLOCK)
                .customPortalBlock(JamiesModBlocks.BYGONE_PORTAL)
                .destDimID(Bygone.id("bygone"))
                .tintColor(0, 1, 0)
                .setPortalSearchYRange(0, 120)
                .flatPortal()
                .lightWithItem(JamiesModItems.ARCANE_CORE)
                .onlyLightInOverworld()
                .registerIgniteEvent((player, world, portalPos, framePos, portalIgnitionSource) -> {
                    if (portalIgnitionSource.sourceType == PortalIgnitionSource.SourceType.USEITEM && player != null) {
                        if (player.isCreative())
                            return;
                        ItemStack heldItem = player.getMainHandItem().getItem() == JamiesModItems.HOOK ?
                                player.getMainHandItem() : player.getOffhandItem();

                        heldItem.hurtAndBreak(1 , player, player.getEquipmentSlotForItem(player.getUseItem()));
                    }
                })
                .registerPortal();
    }
}
