package com.jamiedev.bygone.datagen;

import com.jamiedev.bygone.init.JamiesModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;

import java.util.Set;

public class BygoneBlockLootSubProvider extends BlockLootSubProvider {


    protected BygoneBlockLootSubProvider(HolderLookup.Provider pRegistries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), pRegistries);
    }

    @Override
    public void generate() {
        dropSelf(JamiesModBlocks.CLAYSTONE_BRICKS);
        dropSelf(JamiesModBlocks.CLAYSTONE_BRICKS_WALL);
        dropSelf(JamiesModBlocks.CLAYSTONE_BRICKS_STAIRS);
        dropSelf(JamiesModBlocks.CLAYSTONE_BRICKS_SLAB);
    }
}