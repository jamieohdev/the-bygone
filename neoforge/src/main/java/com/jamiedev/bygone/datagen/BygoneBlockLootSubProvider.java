package com.jamiedev.bygone.datagen;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.core.registry.BGBlocks;
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

        dropSelf(BGBlocks.POLISHED_BYSTONE);
        dropSelf(BGBlocks.POLISHED_BYSTONE_SLAB);
        dropSelf(BGBlocks.POLISHED_BYSTONE_STAIRS);
        dropSelf(BGBlocks.POLISHED_BYSTONE_WALL);

        dropSelf(BGBlocks.POLISHED_BYSTONE_BRICK);
        dropSelf(BGBlocks.POLISHED_BYSTONE_BRICK_SLAB);
        dropSelf(BGBlocks.POLISHED_BYSTONE_BRICK_STAIRS);
        dropSelf(BGBlocks.POLISHED_BYSTONE_BRICK_WALL);

        dropSelf(BGBlocks.CHISELED_POLISHED_BYSTONE);
        dropSelf(BGBlocks.POLISHED_BYSTONE_SHINGLES);

        dropSelf(BGBlocks.POLISHED_BYSLATE);
        dropSelf(BGBlocks.POLISHED_BYSLATE_SLAB);
        dropSelf(BGBlocks.POLISHED_BYSLATE_STAIRS);
        dropSelf(BGBlocks.POLISHED_BYSLATE_WALL);

        dropSelf(BGBlocks.POLISHED_BYSLATE_BRICK);
        dropSelf(BGBlocks.POLISHED_BYSLATE_BRICK_SLAB);
        dropSelf(BGBlocks.POLISHED_BYSLATE_BRICK_STAIRS);
        dropSelf(BGBlocks.POLISHED_BYSLATE_BRICK_WALL);

        dropSelf(BGBlocks.CHISELED_POLISHED_BYSLATE);
        dropSelf(BGBlocks.POLISHED_BYSLATE_SHINGLES);
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return Bygone.getKnownBlocks().toList();
    }
}