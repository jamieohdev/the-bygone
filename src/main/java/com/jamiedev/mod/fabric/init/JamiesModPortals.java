package com.jamiedev.mod.fabric.init;

import com.jamiedev.mod.fabric.JamiesModFabric;
import net.kyrptonaught.customportalapi.api.CustomPortalBuilder;
import net.kyrptonaught.customportalapi.portal.PortalIgnitionSource;
import net.minecraft.world.item.ItemStack;

public class JamiesModPortals {

    public static void init() {
        CustomPortalBuilder.beginPortal()
                .frameBlock(JamiesModBlocks.BYGONE_PORTAL_FRAME)
                .customPortalBlock(JamiesModBlocks.BYGONE_PORTAL)
                .destDimID(JamiesModFabric.getModId("bygone"))
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
                .destDimID(JamiesModFabric.getModId("bygone"))
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
                .destDimID(JamiesModFabric.getModId("bygone"))
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
