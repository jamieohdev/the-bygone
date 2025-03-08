package com.jamiedev.bygone;

import com.jamiedev.bygone.core.registry.BGBlocks;
import com.jamiedev.bygone.core.registry.BGItems;
import net.kyrptonaught.customportalapi.api.CustomPortalBuilder;

public class JamiesModPortalsNeoForge {


    public static void init() {
        CustomPortalBuilder.beginPortal()
                .frameBlock(BGBlocks.BYGONE_PORTAL_FRAME.get())
                .customPortalBlock(() -> (net.kyrptonaught.customportalapi.CustomPortalBlock) BGBlocks.BYGONE_PORTAL.get())
                .destDimID(Bygone.id("bygone"))
                .tintColor(0, 1, 0)
                .flatPortal()
                .lightWithItem(BGItems.ARCANE_CORE.get())
                .registerPortal();
        CustomPortalBuilder.beginPortal()
                .frameBlock(BGBlocks.BYGONE_PORTAL_FRAME_PLACEABLE.get())
                .customPortalBlock(() -> (net.kyrptonaught.customportalapi.CustomPortalBlock) BGBlocks.BYGONE_PORTAL.get())
                .destDimID(Bygone.id("bygone"))
                .tintColor(0, 1, 0)
                .flatPortal()
                .lightWithItem(BGItems.ARCANE_CORE.get())
                .onlyLightInOverworld()
                .registerPortal();
        CustomPortalBuilder.beginPortal()
                .frameBlock(BGBlocks.BYGONE_PORTAL_FRAME_BLOCK.get())
                .customPortalBlock(() -> (net.kyrptonaught.customportalapi.CustomPortalBlock) BGBlocks.BYGONE_PORTAL.get())
                .destDimID(Bygone.id("bygone"))
                .tintColor(0, 1, 0)
                .flatPortal()
                .lightWithItem(BGItems.ARCANE_CORE.get())
                .onlyLightInOverworld()
                .registerPortal();
    }
}
