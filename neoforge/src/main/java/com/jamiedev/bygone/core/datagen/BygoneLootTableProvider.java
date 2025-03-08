package com.jamiedev.bygone.core.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.WritableRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class BygoneLootTableProvider extends LootTableProvider {
    public BygoneLootTableProvider(PackOutput pOutput, Set<ResourceKey<LootTable>> pRequiredTables, List<SubProviderEntry> pSubProviders, CompletableFuture<HolderLookup.Provider> pRegistries) {
        super(pOutput, pRequiredTables, pSubProviders, pRegistries);
    }

    public static BygoneLootTableProvider create(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pRegistries) {
        return new BygoneLootTableProvider(
                pOutput,
                BuiltInLootTables.all(),
                List.of(
                        new SubProviderEntry(BygoneBlockLootSubProvider::new, LootContextParamSets.BLOCK)
                ),
                pRegistries
        );
    }

    @Override
    protected void validate(WritableRegistry<LootTable> writableregistry, ValidationContext validationcontext, ProblemReporter.Collector problemreporter$collector) {
        // Do not validate against all registered loot tables
    }
}