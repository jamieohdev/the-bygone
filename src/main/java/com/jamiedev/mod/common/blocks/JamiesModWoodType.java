package com.jamiedev.mod.common.blocks;

import com.jamiedev.mod.fabric.JamiesModFabric;
import net.fabricmc.fabric.api.object.builder.v1.block.type.WoodTypeBuilder;
import net.minecraft.block.WoodType;
public class JamiesModWoodType
{
    public static final WoodType ANCIENT = WoodTypeBuilder.copyOf(WoodType.OAK).register(
            JamiesModFabric.getModId( "ancient"), JamiesModBlockSetType.ANCIENT);

    public void init() {
    }
}
