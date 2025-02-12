package com.jamiedev.bygone.block;

import com.jamiedev.bygone.Bygone;
import net.minecraft.world.level.block.state.properties.WoodType;
public class JamiesModWoodType
{
    public static final WoodType ANCIENT = WoodType.register(
            new WoodType(Bygone.id( "ancient").toString(), JamiesModBlockSetType.ANCIENT));

    public void init() {
    }
}
