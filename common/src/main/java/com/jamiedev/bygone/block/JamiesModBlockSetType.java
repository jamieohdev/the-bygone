package com.jamiedev.bygone.block;

import com.jamiedev.bygone.Bygone;
import net.fabricmc.fabric.api.object.builder.v1.block.type.BlockSetTypeBuilder;
import net.minecraft.world.level.block.state.properties.BlockSetType;

public class JamiesModBlockSetType
{
    public static final BlockSetType ANCIENT = BlockSetTypeBuilder.copyOf(BlockSetType.WARPED).register(
            Bygone.id("ancient"));

    public void init() {
    }
}
