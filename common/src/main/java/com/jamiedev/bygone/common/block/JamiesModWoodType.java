package com.jamiedev.bygone.common.block;

import com.jamiedev.bygone.Bygone;
import net.minecraft.world.level.block.state.properties.WoodType;

public class JamiesModWoodType {
    public static final WoodType ANCIENT = WoodType.register(
            new WoodType(Bygone.id("ancient").toString(), JamiesModBlockSetType.ANCIENT));

    public static final WoodType SABLE = WoodType.register(
            new WoodType(Bygone.id("sable").toString(), JamiesModBlockSetType.SABLE));

    public void init() {
    }
}
