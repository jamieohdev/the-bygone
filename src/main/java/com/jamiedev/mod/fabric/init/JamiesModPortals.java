package com.jamiedev.mod.fabric.init;

import com.jamiedev.mod.fabric.JamiesModFabric;
import net.kyrptonaught.customportalapi.api.CustomPortalBuilder;
import net.kyrptonaught.customportalapi.portal.PortalIgnitionSource;
import net.minecraft.item.ItemStack;

public class JamiesModPortals {

    public static void init() {
        CustomPortalBuilder.beginPortal()
                .frameBlock(JamiesModBlocks.BYGONE_PORTAL_FRAME)
                .customPortalBlock(JamiesModBlocks.BYGONE_PORTAL)
                .destDimID(JamiesModFabric.getModId("bygone"))
                .tintColor(0, 1, 0)
                .setPortalSearchYRange(0, 120)
                .flatPortal()
                .lightWithItem(JamiesModItems.HOOK)
                .registerIgniteEvent((player, world, portalPos, framePos, portalIgnitionSource) -> {
                    if (portalIgnitionSource.sourceType == PortalIgnitionSource.SourceType.USEITEM && player != null) {
                        if (player.isCreative())
                            return;
                        ItemStack heldItem = player.getMainHandStack().getItem() == JamiesModItems.HOOK ?
                                player.getMainHandStack() : player.getOffHandStack();

                        heldItem.damage(1 , player, player.getPreferredEquipmentSlot(player.getActiveItem()));
                    }
                })
                .registerPortal();
    }
}
