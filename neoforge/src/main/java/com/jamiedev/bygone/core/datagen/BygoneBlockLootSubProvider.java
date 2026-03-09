package com.jamiedev.bygone.core.datagen;

import com.jamiedev.bygone.Bygone;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;

import java.util.Map;
import java.util.Set;

public class BygoneBlockLootSubProvider extends BlockLootSubProvider {

    protected BygoneBlockLootSubProvider(HolderLookup.Provider pRegistries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), pRegistries);
    }

	public static boolean ownsEntry(Map.Entry<ResourceKey<Block>, Block> entry) {
		return entry.getKey().location().getNamespace().equals(Bygone.MOD_ID);
	}

	public static boolean hasItem(Block block) {
		return !block.asItem().equals(Items.AIR);
	}

    @Override
    protected Iterable<Block> getKnownBlocks() {
		return BuiltInRegistries.BLOCK.entrySet().stream()
				.filter(BygoneBlockLootSubProvider::ownsEntry)
				.map(Map.Entry::getValue)
				.filter(BygoneBlockLootSubProvider::hasItem)
				.toList();
    }

	@Override
	public void generate() {
		this.getKnownBlocks().forEach(this::dropSelf);
	}

}