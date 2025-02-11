package com.jamiedev.bygone.common.blocks;

import com.jamiedev.bygone.fabric.init.JamiesModBlocks;
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;

public class JamiesModStrippableBlocks
{
    public static void registerStrippables() {
        StrippableBlockRegistry.register(JamiesModBlocks.ANCIENT_LOG, JamiesModBlocks.STRIPPED_ANCIENT_LOG);
        StrippableBlockRegistry.register(JamiesModBlocks.ANCIENT_WOOD, JamiesModBlocks.STRIPPED_ANCIENT_WOOD);
    }
}
