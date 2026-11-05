package com.jamiedev.bygone.core.datagen;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.DataProvider;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.crafting.Recipe;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public class DataGenerators {
    public static List<DataProvider.Factory<?>> providers(CompletableFuture<HolderLookup.Provider> registries,
                                                        Supplier<HolderLookup.Provider> registryLoader,
                                                        UnaryOperator<RecipeOutput> recipeOutput) {
        var advancementRegistries = registries.thenApply(lookup -> registryLoader.get());
        return List.of(
                output -> new ModRecipeProvider(output, registries) {
                    @Override
                    public void buildRecipes(RecipeOutput recipes) {
                        super.buildRecipes(recipeOutput.apply(recipes));
                    }
                },
                output -> new AdvancementProvider(output, advancementRegistries, List.of(new ModAdvancementProvider())));
    }

    public static HolderLookup.Provider loadRegistries(ResourceManager resources, List<RegistryDataLoader.RegistryData<?>> registries) {
        var builtIn = RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY);
        var worldgen = RegistryDataLoader.load(resources, builtIn, registries);
        return HolderLookup.Provider.create(Stream.concat(
                builtIn.listRegistries().map(builtIn::lookupOrThrow), worldgen.listRegistries().map(worldgen::lookupOrThrow)));
    }

    public abstract static class RecipeOutputAdapter implements RecipeOutput {
        protected final RecipeOutput recipes;

        protected RecipeOutputAdapter(RecipeOutput recipes) {
            this.recipes = recipes;
        }

        @Override
        public void accept(ResourceLocation id, Recipe<?> recipe, AdvancementHolder advancement) {
            this.recipes.accept(id, recipe, recipeAdvancement(advancement));
        }

        @Override
        public Advancement.Builder advancement() {
            return this.recipes.advancement();
        }

        protected AdvancementHolder recipeAdvancement(AdvancementHolder advancement) {
            return advancement == null ? null : ModRecipeProvider.preserveAdvancementId(advancement);
        }
    }
}
