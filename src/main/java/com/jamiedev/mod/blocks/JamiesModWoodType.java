package com.jamiedev.mod.blocks;

import com.jamiedev.mod.JamiesMod;
import net.fabricmc.fabric.api.object.builder.v1.block.type.WoodTypeBuilder;
import net.minecraft.block.WoodType;
public class JamiesModWoodType
{
    public static final WoodType ANCIENT = WoodTypeBuilder.copyOf(WoodType.OAK).register(
            JamiesMod.getModId( "ancient"), JamiesModBlockSetType.ANCIENT);

    public void init() {
    }
}
