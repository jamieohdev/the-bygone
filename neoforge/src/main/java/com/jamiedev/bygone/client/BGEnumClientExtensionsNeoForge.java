package com.jamiedev.bygone.client;

import net.minecraft.client.gui.Gui;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;

public class BGEnumClientExtensionsNeoForge {
    public static final EnumProxy<Gui.HeartType> BYGONE_HEART_HAUNTED = new EnumProxy<>(
            Gui.HeartType.class,
            id("hud/heart/haunted_full"),
            id("hud/heart/haunted_full_blinking"),
            id("hud/heart/haunted_half"),
            id("hud/heart/haunted_half_blinking"),
            id("hud/heart/haunted_hardcore_full"),
            id("hud/heart/haunted_hardcore_full_blinking"),
            id("hud/heart/haunted_hardcore_half"),
            id("hud/heart/haunted_hardcore_half_blinking")
    );

    private static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath("bygone", path);
    }
}
