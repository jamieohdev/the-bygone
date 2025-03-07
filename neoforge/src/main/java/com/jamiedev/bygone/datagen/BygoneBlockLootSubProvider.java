package com.jamiedev.bygone.datagen;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.init.JamiesModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;

import java.util.Set;

public class BygoneBlockLootSubProvider extends BlockLootSubProvider {


    protected BygoneBlockLootSubProvider(HolderLookup.Provider pRegistries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), pRegistries);
    }

    @Override
    public void generate() {

        dropSelf(JamiesModBlocks.POLISHED_BYSTONE);
        dropSelf(JamiesModBlocks.POLISHED_BYSTONE_SLAB);
        dropSelf(JamiesModBlocks.POLISHED_BYSTONE_STAIRS);
        dropSelf(JamiesModBlocks.POLISHED_BYSTONE_WALL);

        dropSelf(JamiesModBlocks.POLISHED_BYSTONE_BRICK);
        dropSelf(JamiesModBlocks.POLISHED_BYSTONE_BRICK_SLAB);
        dropSelf(JamiesModBlocks.POLISHED_BYSTONE_BRICK_STAIRS);
        dropSelf(JamiesModBlocks.POLISHED_BYSTONE_BRICK_WALL);

        dropSelf(JamiesModBlocks.CHISELED_POLISHED_BYSTONE);
        dropSelf(JamiesModBlocks.POLISHED_BYSTONE_SHINGLES);

        dropSelf(JamiesModBlocks.POLISHED_BYSLATE);
        dropSelf(JamiesModBlocks.POLISHED_BYSLATE_SLAB);
        dropSelf(JamiesModBlocks.POLISHED_BYSLATE_STAIRS);
        dropSelf(JamiesModBlocks.POLISHED_BYSLATE_WALL);

        dropSelf(JamiesModBlocks.POLISHED_BYSLATE_BRICK);
        dropSelf(JamiesModBlocks.POLISHED_BYSLATE_BRICK_SLAB);
        dropSelf(JamiesModBlocks.POLISHED_BYSLATE_BRICK_STAIRS);
        dropSelf(JamiesModBlocks.POLISHED_BYSLATE_BRICK_WALL);

        dropSelf(JamiesModBlocks.CHISELED_POLISHED_BYSLATE);
        dropSelf(JamiesModBlocks.POLISHED_BYSLATE_SHINGLES);
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return Bygone.getKnownBlocks().toList();
    }
}