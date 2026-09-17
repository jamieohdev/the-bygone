package com.jamiedev.bygone.client;

import com.jamiedev.bygone.Bygone;
import net.minecraft.client.gui.Gui;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;

@Mod(value = Bygone.MOD_ID, dist = Dist.CLIENT)
public class BGEnumClientExtensionsNeoForge
{
    public static final EnumProxy<Gui.HeartType> BYGONE_HEART_HAUNTED = new EnumProxy<>(
            Gui.HeartType.class,
            Bygone.id( "hud/heart/haunted_full"),
            Bygone.id( "hud/heart/haunted_full_blinking"),
            Bygone.id( "hud/heart/haunted_half"),
            Bygone.id( "hud/heart/haunted_half_blinking"),
            Bygone.id( "hud/heart/haunted_hardcore_full"),
            Bygone.id( "hud/heart/haunted_hardcore_full_blinking"),
            Bygone.id( "hud/heart/haunted_hardcore_half"),
            Bygone.id( "hud/heart/haunted_hardcore_half_blinking")
    );
}
