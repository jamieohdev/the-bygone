package com.jamiedev.bygone;

import com.jamiedev.bygone.core.registry.BGBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class LootTablesDatagen  extends FabricBlockLootTableProvider
{
    // i hate you so much datagen
    protected LootTablesDatagen(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        dropSelf(BGBlocks.AMPHORA.get());
    }
}
