package com.jamiedev.bygone.core.datagen;

import com.jamiedev.bygone.core.registry.BGBiomes;
import com.jamiedev.bygone.core.registry.BGConfiguredFeatures;
import com.jamiedev.bygone.core.registry.BGPlacedFeatures;
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
		//generator.addProvider(true,blockTagsProvider);
		//generator.addProvider(true,new BygoneItemTagProvider(output,lookupProvider,blockTagsProvider.contentsGetter(),existingFileHelper));
		//generator.addProvider(true,BygoneLootTableProvider.create(output,lookupProvider));
		//generator.addProvider(true, new BygoneRecipeProvider(output,lookupProvider));

		//generator.addProvider(true,new BygoneDataPackProvider(output,buildRegistry(),lookupProvider));
		generator.addProvider(event.includeServer(), new BygoneAdvancementProvider(output, lookupProvider, existingFileHelper));
	}

	public static RegistrySetBuilder buildRegistry() {
		RegistrySetBuilder registryBuilder =new RegistrySetBuilder();
		//registryBuilder.add(Registries.CONFIGURED_FEATURE, BGConfiguredFeatures::bootstrap);
		//registryBuilder.add(Registries.PLACED_FEATURE, BGPlacedFeatures::bootstrap);
		//registryBuilder.add(Registries.BIOME, BGBiomes::bootstrap);

		/**registryBuilder.addRegistry(RegistryKeys.PROCESSOR_LIST, JamiesModProcessorLists::bootstrap);
		registryBuilder.addRegistry(RegistryKeys.STRUCTURE_SET, JamiesModStructureSets::bootstrap);
		registryBuilder.addRegistry(RegistryKeys.STRUCTURE, JamiesModStructures::bootstrap);
		registryBuilder.addRegistry(RegistryKeys.TEMPLATE_POOL, JamiesModStructurePools::bootstrap);**/
		return registryBuilder;
	}
}
