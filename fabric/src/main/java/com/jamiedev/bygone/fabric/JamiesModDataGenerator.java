package com.jamiedev.bygone.fabric;

import com.jamiedev.bygone.fabric.datagen.*;
import com.jamiedev.bygone.fabric.init.JamiesModBiomes;
import com.jamiedev.bygone.fabric.init.JamiesModConfiguredFeatures;
import com.jamiedev.bygone.fabric.init.JamiesModPlacedFeatures;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;


public class JamiesModDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		pack.addProvider(JamiesModBlockTagProvider::new);
		pack.addProvider(JamiesModItemTagProvider::new);
		pack.addProvider(JamiesModLootTableProvider::new);
		pack.addProvider(JamiesModModelProvider::new);
		pack.addProvider(JamiesModRecipeProvider::new);
	}

	@Override
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
