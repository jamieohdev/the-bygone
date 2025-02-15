package com.jamiedev.bygone;

import com.jamiedev.bygone.block.BygonePortalBlock;
import com.jamiedev.bygone.init.JamiesModBlocks;
import com.jamiedev.bygone.init.JamiesModItems;
import net.kyrptonaught.customportalapi.CustomPortalBlock;
import net.kyrptonaught.customportalapi.api.CustomPortalBuilder;
import net.kyrptonaught.customportalapi.portal.PortalIgnitionSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Supplier;

public class JamiesModPortalsNeoForge {


    public static void init() {
        CustomPortalBuilder.beginPortal()
                .frameBlock(JamiesModBlocks.BYGONE_PORTAL_FRAME)
                .customPortalBlock(() -> JamiesModBlocks.BYGONE_PORTAL)
                .destDimID(Bygone.id("bygone"))
                .tintColor(0, 1, 0)
                .flatPortal()
                .lightWithItem(JamiesModItems.ARCANE_CORE)
                .registerPortal();
        CustomPortalBuilder.beginPortal()
                .frameBlock(JamiesModBlocks.BYGONE_PORTAL_FRAME_PLACEABLE)
                .customPortalBlock(() -> JamiesModBlocks.BYGONE_PORTAL)
                .destDimID(Bygone.id("bygone"))
                .tintColor(0, 1, 0)
                .flatPortal()
                .lightWithItem(JamiesModItems.ARCANE_CORE)
                .onlyLightInOverworld()
                .registerPortal();
        CustomPortalBuilder.beginPortal()
                .frameBlock(JamiesModBlocks.BYGONE_PORTAL_FRAME_BLOCK)
                .customPortalBlock(() -> JamiesModBlocks.BYGONE_PORTAL)
                .destDimID(Bygone.id("bygone"))
                .tintColor(0, 1, 0)
                .flatPortal()
                .lightWithItem(JamiesModItems.ARCANE_CORE)
                .onlyLightInOverworld()
                .registerPortal();
    }
}
