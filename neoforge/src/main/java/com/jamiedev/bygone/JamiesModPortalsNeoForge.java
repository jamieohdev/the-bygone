package com.jamiedev.bygone;

import com.jamiedev.bygone.core.registry.BGBlocks;
import com.jamiedev.bygone.core.registry.BGItems;
import net.kyrptonaught.customportalapi.api.CustomPortalBuilder;

public class JamiesModPortalsNeoForge {


    public static void init() {
        CustomPortalBuilder.beginPortal()
                .frameBlock(BGBlocks.BYGONE_PORTAL_FRAME)
                .customPortalBlock(() -> BGBlocks.BYGONE_PORTAL)
                .destDimID(Bygone.id("bygone"))
                .tintColor(0, 1, 0)
                .flatPortal()
                .lightWithItem(BGItems.ARCANE_CORE)
                .registerPortal();
        CustomPortalBuilder.beginPortal()
                .frameBlock(BGBlocks.BYGONE_PORTAL_FRAME_PLACEABLE)
                .customPortalBlock(() -> BGBlocks.BYGONE_PORTAL)
                .destDimID(Bygone.id("bygone"))
                .tintColor(0, 1, 0)
                .flatPortal()
                .lightWithItem(BGItems.ARCANE_CORE)
                .onlyLightInOverworld()
                .registerPortal();
        CustomPortalBuilder.beginPortal()
                .frameBlock(BGBlocks.BYGONE_PORTAL_FRAME_BLOCK)
                .customPortalBlock(() -> BGBlocks.BYGONE_PORTAL)
                .destDimID(Bygone.id("bygone"))
                .tintColor(0, 1, 0)
                .flatPortal()
                .lightWithItem(BGItems.ARCANE_CORE)
                .onlyLightInOverworld()
                .registerPortal();
    }
}
