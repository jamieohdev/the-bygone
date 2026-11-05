package com.jamiedev.bygone.core.datagen;

import com.jamiedev.bygone.core.datagen.DataGenerators.RecipeOutputAdapter;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.crafting.Recipe;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.registries.DataPackRegistriesHooks;

public class DataNeoGenerator {
    public static void gatherData(GatherDataEvent event) {
        var generator = event.getGenerator();
        var providers = DataGenerators.providers(event.getLookupProvider(),
                () -> DataGenerators.loadRegistries(event.getResourceManager(PackType.SERVER_DATA), DataPackRegistriesHooks.getDataPackRegistries()),
                recipes -> new RecipeOutputAdapter(recipes) {
                    @Override
                    public void accept(ResourceLocation id, Recipe<?> recipe, AdvancementHolder advancement, ICondition... conditions) {
                        this.recipes.accept(id, recipe, recipeAdvancement(advancement), conditions);
                    }
                });
        for (var provider : providers) {
            generator.addProvider(event.includeServer(), provider.create(generator.getPackOutput()));
        }
    }
}
