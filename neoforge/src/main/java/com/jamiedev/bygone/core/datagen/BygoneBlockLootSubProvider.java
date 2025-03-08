package com.jamiedev.bygone.core.datagen;

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
        dropSelf(BGBlocks.POLISHED_BYSTONE.get());
        dropSelf(BGBlocks.POLISHED_BYSTONE_SLAB.get());
        dropSelf(BGBlocks.POLISHED_BYSTONE_STAIRS.get());
        dropSelf(BGBlocks.POLISHED_BYSTONE_WALL.get());

        dropSelf(BGBlocks.POLISHED_BYSTONE_BRICK.get());
        dropSelf(BGBlocks.POLISHED_BYSTONE_BRICK_SLAB.get());
        dropSelf(BGBlocks.POLISHED_BYSTONE_BRICK_STAIRS.get());
        dropSelf(BGBlocks.POLISHED_BYSTONE_BRICK_WALL.get());

        dropSelf(BGBlocks.CHISELED_POLISHED_BYSTONE.get());
        dropSelf(BGBlocks.POLISHED_BYSTONE_SHINGLES.get());

        dropSelf(BGBlocks.POLISHED_BYSLATE.get());
        dropSelf(BGBlocks.POLISHED_BYSLATE_SLAB.get());
        dropSelf(BGBlocks.POLISHED_BYSLATE_STAIRS.get());
        dropSelf(BGBlocks.POLISHED_BYSLATE_WALL.get());

        dropSelf(BGBlocks.POLISHED_BYSLATE_BRICK.get());
        dropSelf(BGBlocks.POLISHED_BYSLATE_BRICK_SLAB.get());
        dropSelf(BGBlocks.POLISHED_BYSLATE_BRICK_STAIRS.get());
        dropSelf(BGBlocks.POLISHED_BYSLATE_BRICK_WALL.get());

        dropSelf(BGBlocks.CHISELED_POLISHED_BYSLATE.get());
        dropSelf(BGBlocks.POLISHED_BYSLATE_SHINGLES.get());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return Bygone.getKnownBlocks().toList();
    }
}