package com.jamiedev.bygone.core.datagen;

import com.jamiedev.bygone.core.datagen.DataGenerators.RecipeOutputAdapter;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.minecraft.core.HolderLookup;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.ServerPacksSource;
import net.minecraft.server.packs.resources.MultiPackResourceManager;
import net.minecraft.world.level.validation.DirectoryValidator;

import java.nio.file.Path;
import java.util.List;

public class DataFabricGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        var pack = generator.createPack();
        var providers = DataGenerators.providers(generator.getRegistries(), DataFabricGenerator::loadRegistries,
                recipes -> new RecipeOutputAdapter(recipes) {});
        for (var provider : providers) {
            pack.addProvider((output, registries) -> provider.create(output));
        }
    }

    private static HolderLookup.Provider loadRegistries() {
        var packs = ServerPacksSource.createPackRepository(Path.of("datapacks"), new DirectoryValidator(path -> false));
        packs.reload();
        packs.setSelected(List.of("vanilla"));
        try (var resources = new MultiPackResourceManager(PackType.SERVER_DATA, packs.openAllSelected())) {
            return DataGenerators.loadRegistries(resources, DynamicRegistries.getDynamicRegistries());
        }
    }
}
