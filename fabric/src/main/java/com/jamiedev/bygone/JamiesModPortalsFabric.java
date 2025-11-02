package com.jamiedev.bygone;

import com.jamiedev.bygone.core.registry.BGBlocks;
import com.jamiedev.bygone.core.registry.BGItems;
import net.kyrptonaught.customportalapi.CustomPortalBlock;
import net.kyrptonaught.customportalapi.api.CustomPortalBuilder;
import net.kyrptonaught.customportalapi.portal.PortalIgnitionSource;
import net.minecraft.world.item.ItemStack;

public class JamiesModPortalsFabric {

    public static void init() {
        CustomPortalBuilder.beginPortal()
                .frameBlock(BGBlocks.BYGONE_PORTAL_FRAME.get())
                .customPortalBlock((CustomPortalBlock) BGBlocks.BYGONE_PORTAL.get())
                .destDimID(Bygone.id("bygone"))
                .tintColor(0, 1, 0)
                .setPortalSearchYRange(0, 120)
                .flatPortal()
                .lightWithItem(BGItems.ARCANE_CORE.get())
                .registerIgniteEvent((player, world, portalPos, framePos, portalIgnitionSource) -> {
                    if (portalIgnitionSource.sourceType == PortalIgnitionSource.SourceType.USEITEM && player != null) {
                        if (player.isCreative())
                            return;
                        ItemStack heldItem = player.getMainHandItem().getItem() == BGItems.HOOK ?
                                player.getMainHandItem() : player.getOffhandItem();

                        heldItem.hurtAndBreak(1, player, player.getEquipmentSlotForItem(player.getUseItem()));
                    }
                })
                .registerPortal();
        CustomPortalBuilder.beginPortal()
                .frameBlock(BGBlocks.BYGONE_PORTAL_FRAME_PLACEABLE.get())
                .customPortalBlock((CustomPortalBlock) BGBlocks.BYGONE_PORTAL.get())
                .destDimID(Bygone.id("bygone"))
                .tintColor(0, 1, 0)
                .setPortalSearchYRange(0, 120)
                .flatPortal()
                .lightWithItem(BGItems.ARCANE_CORE.get())
                .onlyLightInOverworld()
                .registerIgniteEvent((player, world, portalPos, framePos, portalIgnitionSource) -> {
                    if (portalIgnitionSource.sourceType == PortalIgnitionSource.SourceType.USEITEM && player != null) {
                        if (player.isCreative())
                            return;
                        ItemStack heldItem = player.getMainHandItem().getItem() == BGItems.HOOK ?
                                player.getMainHandItem() : player.getOffhandItem();

                        heldItem.hurtAndBreak(1, player, player.getEquipmentSlotForItem(player.getUseItem()));
                    }
                })
                .registerPortal();
        CustomPortalBuilder.beginPortal()
                .frameBlock(BGBlocks.BYGONE_PORTAL_FRAME_BLOCK.get())
                .customPortalBlock((CustomPortalBlock) BGBlocks.BYGONE_PORTAL.get())
                .destDimID(Bygone.id("bygone"))
                .tintColor(0, 1, 0)
                .setPortalSearchYRange(0, 120)
                .flatPortal()
                .lightWithItem(BGItems.ARCANE_CORE.get())
                .onlyLightInOverworld()
                .registerIgniteEvent((player, world, portalPos, framePos, portalIgnitionSource) -> {
                    if (portalIgnitionSource.sourceType == PortalIgnitionSource.SourceType.USEITEM && player != null) {
                        if (player.isCreative())
                            return;
                        ItemStack heldItem = player.getMainHandItem().getItem() == BGItems.HOOK ?
                                player.getMainHandItem() : player.getOffhandItem();

                        heldItem.hurtAndBreak(1, player, player.getEquipmentSlotForItem(player.getUseItem()));
                    }
                })
                .registerPortal();
    }
}
