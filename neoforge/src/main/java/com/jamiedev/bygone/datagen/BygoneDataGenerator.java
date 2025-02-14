package com.jamiedev.bygone.datagen;

import com.jamiedev.bygone.init.JamiesModBiomes;
import com.jamiedev.bygone.init.JamiesModConfiguredFeatures;
import com.jamiedev.bygone.init.JamiesModPlacedFeatures;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;


public class BygoneDataGenerator  {
	public static void onInitializeDataGenerator(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
		CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
		PackOutput output = generator.getPackOutput();
		BlockTagsProvider blockTagsProvider = new BygoneBlockTagProvider(output,lookupProvider,existingFileHelper);
		generator.addProvider(true,blockTagsProvider);
		generator.addProvider(true,new BygoneItemTagProvider(output,lookupProvider,blockTagsProvider.contentsGetter(),existingFileHelper));
		generator.addProvider(true,BygoneLootTableProvider.create(output,lookupProvider));
		generator.addProvider(true, new BygoneRecipeProvider(output,lookupProvider));
	}

	public void buildRegistry(RegistrySetBuilder registryBuilder) {
		registryBuilder.add(Registries.CONFIGURED_FEATURE, JamiesModConfiguredFeatures::bootstrap);
		registryBuilder.add(Registries.PLACED_FEATURE, JamiesModPlacedFeatures::bootstrap);
		registryBuilder.add(Registries.BIOME, JamiesModBiomes::bootstrap);

		/**registryBuilder.addRegistry(RegistryKeys.PROCESSOR_LIST, JamiesModProcessorLists::bootstrap);
		registryBuilder.addRegistry(RegistryKeys.STRUCTURE_SET, JamiesModStructureSets::bootstrap);
		registryBuilder.addRegistry(RegistryKeys.STRUCTURE, JamiesModStructures::bootstrap);
		registryBuilder.addRegistry(RegistryKeys.TEMPLATE_POOL, JamiesModStructurePools::bootstrap);**/
	}
}
