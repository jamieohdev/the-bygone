package com.jamiedev.bygone.common.blocks;

import com.jamiedev.bygone.Bygone;
import net.fabricmc.fabric.api.object.builder.v1.block.type.BlockSetTypeBuilder;
import net.minecraft.world.level.block.state.properties.BlockSetType;

public class JamiesModBlockSetType
{
    public static final BlockSetType ANCIENT = BlockSetTypeBuilder.copyOf(BlockSetType.WARPED).register(
            Bygone.getModId("ancient"));

    public void init() {
    }
}
