package com.jamiedev.bygone.core.datagen;
import com.jamiedev.bygone.Bygone;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class BygoneDataPackProvider extends DatapackBuiltinEntriesProvider {
    public BygoneDataPackProvider(PackOutput output,RegistrySetBuilder registrySetBuilder, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, registrySetBuilder, Set.of(Bygone.MOD_ID));
    }
}
