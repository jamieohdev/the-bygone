package com.jamiedev.bygone.block;

import com.jamiedev.bygone.Bygone;
import net.fabricmc.fabric.api.object.builder.v1.block.type.WoodTypeBuilder;
import net.minecraft.world.level.block.state.properties.WoodType;
public class JamiesModWoodType
{
    public static final WoodType ANCIENT = WoodTypeBuilder.copyOf(WoodType.OAK).register(
            Bygone.id( "ancient"), JamiesModBlockSetType.ANCIENT);

    public void init() {
    }
}
