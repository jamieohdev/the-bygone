package com.jamiedev.bygone.core.datagen;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.core.registry.BGBlocks;
import com.jamiedev.bygone.core.registry.BGItems;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.ItemLike;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected CompletableFuture<?> run(CachedOutput output, HolderLookup.Provider registries) {
        return super.run(new RecipeDataOutput(output), registries);
    }

    @Override
    public void buildRecipes(RecipeOutput output) {
        shapelessRecipes(output);
        shapedRecipes(output);
        stonecuttingRecipes(output);
        cookingRecipes(output);
    }

    private static void shapelessRecipes(RecipeOutput output) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BGItems.AMARANTH_GRAIN.get(), 9)
                .requires(BGBlocks.AMARANTH_BLOCK.get())
                .unlockedBy("has_ingredient_1", has(BGBlocks.AMARANTH_BLOCK.get()))
                .save(output, Bygone.id("amaranth_grain"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BGBlocks.ANCIENT_BUTTON.get(), 1)
                .requires(BGBlocks.ANCIENT_PLANKS.get())
                .group("wooden_button")
                .unlockedBy("has_ancient_planks", has(BGBlocks.ANCIENT_PLANKS.get()))
                .save(output, Bygone.id("ancient_button_from_ancient_planks"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, BGBlocks.ANCIENT_PLANKS.get(), 4)
                .requires(tag("bygone:ancient_logs"))
                .group("planks")
                .unlockedBy("has_logs", has(tag("bygone:ancient_logs")))
                .save(output, Bygone.id("ancient_planks"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, BGBlocks.ANGRY_MEGALITH_LANTERN.get(), 1)
                .requires(BGBlocks.ANGRY_MEGALITH_FACE.get())
                .requires(BGItems.LITHOPLASM.get())
                .unlockedBy("has_ingredient_1", has(BGBlocks.ANGRY_MEGALITH_FACE.get()))
                .unlockedBy("has_ingredient_2", has(BGItems.LITHOPLASM.get()))
                .save(output, Bygone.id("angry_megalith_lantern"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, BGBlocks.AMARANTH_BLOCK.get(), 1)
                .requires(BGItems.AMARANTH_GRAIN.get())
                .requires(BGItems.AMARANTH_GRAIN.get())
                .requires(BGItems.AMARANTH_GRAIN.get())
                .requires(BGItems.AMARANTH_GRAIN.get())
                .requires(BGItems.AMARANTH_GRAIN.get())
                .requires(BGItems.AMARANTH_GRAIN.get())
                .requires(BGItems.AMARANTH_GRAIN.get())
                .requires(BGItems.AMARANTH_GRAIN.get())
                .requires(BGItems.AMARANTH_GRAIN.get())
                .unlockedBy("has_ingredient_1", has(BGItems.AMARANTH_GRAIN.get()))
                .save(output, Bygone.id("bale_block"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BGItems.AMARANTH_GRAIN.get(), 9)
                .requires(BGBlocks.AMARANTH_BLOCK.get())
                .unlockedBy("has_ingredient_1", has(BGBlocks.AMARANTH_BLOCK.get()))
                .save(output, Bygone.id("bale_block_to_amaranth_grain"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, BGBlocks.BLACK_GLOW_CONCRETE_POWDER.get(), 8)
                .requires(Items.BLACK_DYE)
                .requires(BGBlocks.SHELLSAND.get())
                .requires(BGBlocks.SHELLSAND.get())
                .requires(BGBlocks.SHELLSAND.get())
                .requires(BGBlocks.SHELLSAND.get())
                .requires(BGBlocks.GLOW_GRAVEL.get())
                .requires(BGBlocks.GLOW_GRAVEL.get())
                .requires(BGBlocks.GLOW_GRAVEL.get())
                .requires(BGBlocks.GLOW_GRAVEL.get())
                .group("glow_concrete_powder")
                .unlockedBy("has_ingredient_1", has(BGBlocks.SHELLSAND.get()))
                .unlockedBy("has_ingredient_2", has(BGBlocks.GLOW_GRAVEL.get()))
                .save(output, Bygone.id("black_glow_concrete_powder"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, BGBlocks.BLUE_GLOW_CONCRETE_POWDER.get(), 8)
                .requires(Items.BLUE_DYE)
                .requires(BGBlocks.SHELLSAND.get())
                .requires(BGBlocks.SHELLSAND.get())
                .requires(BGBlocks.SHELLSAND.get())
                .requires(BGBlocks.SHELLSAND.get())
                .requires(BGBlocks.GLOW_GRAVEL.get())
                .requires(BGBlocks.GLOW_GRAVEL.get())
                .requires(BGBlocks.GLOW_GRAVEL.get())
                .requires(BGBlocks.GLOW_GRAVEL.get())
                .group("glow_concrete_powder")
                .unlockedBy("has_ingredient_1", has(BGBlocks.SHELLSAND.get()))
                .unlockedBy("has_ingredient_2", has(BGBlocks.GLOW_GRAVEL.get()))
                .save(output, Bygone.id("blue_glow_concrete_powder"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, BGBlocks.BROWN_GLOW_CONCRETE_POWDER.get(), 8)
                .requires(Items.BROWN_DYE)
                .requires(BGBlocks.SHELLSAND.get())
                .requires(BGBlocks.SHELLSAND.get())
                .requires(BGBlocks.SHELLSAND.get())
                .requires(BGBlocks.SHELLSAND.get())
                .requires(BGBlocks.GLOW_GRAVEL.get())
                .requires(BGBlocks.GLOW_GRAVEL.get())
                .requires(BGBlocks.GLOW_GRAVEL.get())
                .requires(BGBlocks.GLOW_GRAVEL.get())
                .group("glow_concrete_powder")
                .unlockedBy("has_ingredient_1", has(BGBlocks.SHELLSAND.get()))
                .unlockedBy("has_ingredient_2", has(BGBlocks.GLOW_GRAVEL.get()))
                .save(output, Bygone.id("brown_glow_concrete_powder"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BGItems.COPPER_NUGGET.get(), 9)
                .requires(Items.COPPER_INGOT)
                .unlockedBy("has_ingredient_1", has(Items.COPPER_INGOT))
                .save(output, Bygone.id("copper_nugget"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BGItems.CRUSHED_SEAGLASS.get(), 2)
                .requires(Items.CHARCOAL)
                .requires(Items.QUARTZ)
                .requires(BGBlocks.SEAGLASS_BLOCK.get())
                .requires(BGBlocks.SEAGLASS_BLOCK.get())
                .group("crushed_seaglass")
                .unlockedBy("has_ingredient_1", has(BGBlocks.SEAGLASS_BLOCK.get()))
                .save(output, Bygone.id("crushed_seaglass"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, BGBlocks.CRYPTIC_MEGALITH_LANTERN.get(), 1)
                .requires(BGBlocks.CRYPTIC_MEGALITH_FACE.get())
                .requires(BGItems.LITHOPLASM.get())
                .unlockedBy("has_ingredient_1", has(BGBlocks.CRYPTIC_MEGALITH_FACE.get()))
                .unlockedBy("has_ingredient_2", has(BGItems.LITHOPLASM.get()))
                .save(output, Bygone.id("cryptic_megalith_lantern"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, BGBlocks.CYAN_GLOW_CONCRETE_POWDER.get(), 8)
                .requires(Items.CYAN_DYE)
                .requires(BGBlocks.SHELLSAND.get())
                .requires(BGBlocks.SHELLSAND.get())
                .requires(BGBlocks.SHELLSAND.get())
                .requires(BGBlocks.SHELLSAND.get())
                .requires(BGBlocks.GLOW_GRAVEL.get())
                .requires(BGBlocks.GLOW_GRAVEL.get())
                .requires(BGBlocks.GLOW_GRAVEL.get())
                .requires(BGBlocks.GLOW_GRAVEL.get())
                .group("glow_concrete_powder")
                .unlockedBy("has_ingredient_1", has(BGBlocks.SHELLSAND.get()))
                .unlockedBy("has_ingredient_2", has(BGBlocks.GLOW_GRAVEL.get()))
                .save(output, Bygone.id("cyan_glow_concrete_powder"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, BGBlocks.BLACK_AMPHORA.get(), 1)
                .requires(Items.BLACK_DYE)
                .requires(Ingredient.of(BGBlocks.AMPHORA.get(), BGBlocks.BLUE_AMPHORA.get(), BGBlocks.BROWN_AMPHORA.get(), BGBlocks.CYAN_AMPHORA.get(), BGBlocks.GRAY_AMPHORA.get(), BGBlocks.GREEN_AMPHORA.get(), BGBlocks.LIGHT_BLUE_AMPHORA.get(), BGBlocks.LIGHT_GRAY_AMPHORA.get(), BGBlocks.LIME_AMPHORA.get(), BGBlocks.MAGENTA_AMPHORA.get(), BGBlocks.ORANGE_AMPHORA.get(), BGBlocks.PINK_AMPHORA.get(), BGBlocks.PURPLE_AMPHORA.get(), BGBlocks.RED_AMPHORA.get(), BGBlocks.YELLOW_AMPHORA.get(), BGBlocks.WHITE_AMPHORA.get()))
                .group("wool")
                .unlockedBy("has_ingredient_1", has(BGBlocks.AMPHORA.get()))
                .unlockedBy("has_ingredient_2", has(BGBlocks.BLUE_AMPHORA.get()))
                .unlockedBy("has_ingredient_3", has(BGBlocks.BROWN_AMPHORA.get()))
                .unlockedBy("has_ingredient_4", has(BGBlocks.CYAN_AMPHORA.get()))
                .unlockedBy("has_ingredient_5", has(BGBlocks.GRAY_AMPHORA.get()))
                .unlockedBy("has_ingredient_6", has(BGBlocks.GREEN_AMPHORA.get()))
                .unlockedBy("has_ingredient_7", has(BGBlocks.LIGHT_BLUE_AMPHORA.get()))
                .unlockedBy("has_ingredient_8", has(BGBlocks.LIGHT_GRAY_AMPHORA.get()))
                .unlockedBy("has_ingredient_9", has(BGBlocks.LIME_AMPHORA.get()))
                .unlockedBy("has_ingredient_10", has(BGBlocks.MAGENTA_AMPHORA.get()))
                .unlockedBy("has_ingredient_11", has(BGBlocks.ORANGE_AMPHORA.get()))
                .unlockedBy("has_ingredient_12", has(BGBlocks.PINK_AMPHORA.get()))
                .unlockedBy("has_ingredient_13", has(BGBlocks.PURPLE_AMPHORA.get()))
                .unlockedBy("has_ingredient_14", has(BGBlocks.RED_AMPHORA.get()))
                .unlockedBy("has_ingredient_15", has(BGBlocks.YELLOW_AMPHORA.get()))
                .unlockedBy("has_ingredient_16", has(BGBlocks.WHITE_AMPHORA.get()))
                .save(output, Bygone.id("dye_black_amphora"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, BGBlocks.BLUE_AMPHORA.get(), 1)
                .requires(Items.BLUE_DYE)
                .requires(Ingredient.of(BGBlocks.AMPHORA.get(), BGBlocks.BLACK_AMPHORA.get(), BGBlocks.BROWN_AMPHORA.get(), BGBlocks.CYAN_AMPHORA.get(), BGBlocks.GRAY_AMPHORA.get(), BGBlocks.GREEN_AMPHORA.get(), BGBlocks.LIGHT_BLUE_AMPHORA.get(), BGBlocks.LIGHT_GRAY_AMPHORA.get(), BGBlocks.LIME_AMPHORA.get(), BGBlocks.MAGENTA_AMPHORA.get(), BGBlocks.ORANGE_AMPHORA.get(), BGBlocks.PINK_AMPHORA.get(), BGBlocks.PURPLE_AMPHORA.get(), BGBlocks.RED_AMPHORA.get(), BGBlocks.YELLOW_AMPHORA.get(), BGBlocks.WHITE_AMPHORA.get()))
                .group("wool")
                .unlockedBy("has_ingredient_1", has(BGBlocks.AMPHORA.get()))
                .unlockedBy("has_ingredient_2", has(BGBlocks.BLACK_AMPHORA.get()))
                .unlockedBy("has_ingredient_3", has(BGBlocks.BROWN_AMPHORA.get()))
                .unlockedBy("has_ingredient_4", has(BGBlocks.CYAN_AMPHORA.get()))
                .unlockedBy("has_ingredient_5", has(BGBlocks.GRAY_AMPHORA.get()))
                .unlockedBy("has_ingredient_6", has(BGBlocks.GREEN_AMPHORA.get()))
                .unlockedBy("has_ingredient_7", has(BGBlocks.LIGHT_BLUE_AMPHORA.get()))
                .unlockedBy("has_ingredient_8", has(BGBlocks.LIGHT_GRAY_AMPHORA.get()))
                .unlockedBy("has_ingredient_9", has(BGBlocks.LIME_AMPHORA.get()))
                .unlockedBy("has_ingredient_10", has(BGBlocks.MAGENTA_AMPHORA.get()))
                .unlockedBy("has_ingredient_11", has(BGBlocks.ORANGE_AMPHORA.get()))
                .unlockedBy("has_ingredient_12", has(BGBlocks.PINK_AMPHORA.get()))
                .unlockedBy("has_ingredient_13", has(BGBlocks.PURPLE_AMPHORA.get()))
                .unlockedBy("has_ingredient_14", has(BGBlocks.RED_AMPHORA.get()))
                .unlockedBy("has_ingredient_15", has(BGBlocks.YELLOW_AMPHORA.get()))
                .unlockedBy("has_ingredient_16", has(BGBlocks.WHITE_AMPHORA.get()))
                .save(output, Bygone.id("dye_blue_amphora"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, BGBlocks.BROWN_AMPHORA.get(), 1)
                .requires(Items.BROWN_DYE)
                .requires(Ingredient.of(BGBlocks.AMPHORA.get(), BGBlocks.BLACK_AMPHORA.get(), BGBlocks.BLUE_AMPHORA.get(), BGBlocks.CYAN_AMPHORA.get(), BGBlocks.GRAY_AMPHORA.get(), BGBlocks.GREEN_AMPHORA.get(), BGBlocks.LIGHT_BLUE_AMPHORA.get(), BGBlocks.LIGHT_GRAY_AMPHORA.get(), BGBlocks.LIME_AMPHORA.get(), BGBlocks.MAGENTA_AMPHORA.get(), BGBlocks.ORANGE_AMPHORA.get(), BGBlocks.PINK_AMPHORA.get(), BGBlocks.PURPLE_AMPHORA.get(), BGBlocks.RED_AMPHORA.get(), BGBlocks.YELLOW_AMPHORA.get(), BGBlocks.WHITE_AMPHORA.get()))
                .group("wool")
                .unlockedBy("has_ingredient_1", has(BGBlocks.AMPHORA.get()))
                .unlockedBy("has_ingredient_2", has(BGBlocks.BLACK_AMPHORA.get()))
                .unlockedBy("has_ingredient_3", has(BGBlocks.BLUE_AMPHORA.get()))
                .unlockedBy("has_ingredient_4", has(BGBlocks.CYAN_AMPHORA.get()))
                .unlockedBy("has_ingredient_5", has(BGBlocks.GRAY_AMPHORA.get()))
                .unlockedBy("has_ingredient_6", has(BGBlocks.GREEN_AMPHORA.get()))
                .unlockedBy("has_ingredient_7", has(BGBlocks.LIGHT_BLUE_AMPHORA.get()))
                .unlockedBy("has_ingredient_8", has(BGBlocks.LIGHT_GRAY_AMPHORA.get()))
                .unlockedBy("has_ingredient_9", has(BGBlocks.LIME_AMPHORA.get()))
                .unlockedBy("has_ingredient_10", has(BGBlocks.MAGENTA_AMPHORA.get()))
                .unlockedBy("has_ingredient_11", has(BGBlocks.ORANGE_AMPHORA.get()))
                .unlockedBy("has_ingredient_12", has(BGBlocks.PINK_AMPHORA.get()))
                .unlockedBy("has_ingredient_13", has(BGBlocks.PURPLE_AMPHORA.get()))
                .unlockedBy("has_ingredient_14", has(BGBlocks.RED_AMPHORA.get()))
                .unlockedBy("has_ingredient_15", has(BGBlocks.YELLOW_AMPHORA.get()))
                .unlockedBy("has_ingredient_16", has(BGBlocks.WHITE_AMPHORA.get()))
                .save(output, Bygone.id("dye_brown_amphora"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, BGBlocks.CYAN_AMPHORA.get(), 1)
                .requires(Items.CYAN_DYE)
                .requires(Ingredient.of(BGBlocks.AMPHORA.get(), BGBlocks.BLACK_AMPHORA.get(), BGBlocks.BLUE_AMPHORA.get(), BGBlocks.BROWN_AMPHORA.get(), BGBlocks.GRAY_AMPHORA.get(), BGBlocks.GREEN_AMPHORA.get(), BGBlocks.LIGHT_BLUE_AMPHORA.get(), BGBlocks.LIGHT_GRAY_AMPHORA.get(), BGBlocks.LIME_AMPHORA.get(), BGBlocks.MAGENTA_AMPHORA.get(), BGBlocks.ORANGE_AMPHORA.get(), BGBlocks.PINK_AMPHORA.get(), BGBlocks.PURPLE_AMPHORA.get(), BGBlocks.RED_AMPHORA.get(), BGBlocks.YELLOW_AMPHORA.get(), BGBlocks.WHITE_AMPHORA.get()))
                .group("wool")
                .unlockedBy("has_ingredient_1", has(BGBlocks.AMPHORA.get()))
                .unlockedBy("has_ingredient_2", has(BGBlocks.BLACK_AMPHORA.get()))
                .unlockedBy("has_ingredient_3", has(BGBlocks.BLUE_AMPHORA.get()))
                .unlockedBy("has_ingredient_4", has(BGBlocks.BROWN_AMPHORA.get()))
                .unlockedBy("has_ingredient_5", has(BGBlocks.GRAY_AMPHORA.get()))
                .unlockedBy("has_ingredient_6", has(BGBlocks.GREEN_AMPHORA.get()))
                .unlockedBy("has_ingredient_7", has(BGBlocks.LIGHT_BLUE_AMPHORA.get()))
                .unlockedBy("has_ingredient_8", has(BGBlocks.LIGHT_GRAY_AMPHORA.get()))
                .unlockedBy("has_ingredient_9", has(BGBlocks.LIME_AMPHORA.get()))
                .unlockedBy("has_ingredient_10", has(BGBlocks.MAGENTA_AMPHORA.get()))
                .unlockedBy("has_ingredient_11", has(BGBlocks.ORANGE_AMPHORA.get()))
                .unlockedBy("has_ingredient_12", has(BGBlocks.PINK_AMPHORA.get()))
                .unlockedBy("has_ingredient_13", has(BGBlocks.PURPLE_AMPHORA.get()))
                .unlockedBy("has_ingredient_14", has(BGBlocks.RED_AMPHORA.get()))
                .unlockedBy("has_ingredient_15", has(BGBlocks.YELLOW_AMPHORA.get()))
                .unlockedBy("has_ingredient_16", has(BGBlocks.WHITE_AMPHORA.get()))
                .save(output, Bygone.id("dye_cyan_amphora"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, BGBlocks.GRAY_AMPHORA.get(), 1)
                .requires(Items.GRAY_DYE)
                .requires(Ingredient.of(BGBlocks.AMPHORA.get(), BGBlocks.BLACK_AMPHORA.get(), BGBlocks.BLUE_AMPHORA.get(), BGBlocks.BROWN_AMPHORA.get(), BGBlocks.CYAN_AMPHORA.get(), BGBlocks.GREEN_AMPHORA.get(), BGBlocks.LIGHT_BLUE_AMPHORA.get(), BGBlocks.LIGHT_GRAY_AMPHORA.get(), BGBlocks.LIME_AMPHORA.get(), BGBlocks.MAGENTA_AMPHORA.get(), BGBlocks.ORANGE_AMPHORA.get(), BGBlocks.PINK_AMPHORA.get(), BGBlocks.PURPLE_AMPHORA.get(), BGBlocks.RED_AMPHORA.get(), BGBlocks.YELLOW_AMPHORA.get(), BGBlocks.WHITE_AMPHORA.get()))
                .group("wool")
                .unlockedBy("has_ingredient_1", has(BGBlocks.AMPHORA.get()))
                .unlockedBy("has_ingredient_2", has(BGBlocks.BLACK_AMPHORA.get()))
                .unlockedBy("has_ingredient_3", has(BGBlocks.BLUE_AMPHORA.get()))
                .unlockedBy("has_ingredient_4", has(BGBlocks.BROWN_AMPHORA.get()))
                .unlockedBy("has_ingredient_5", has(BGBlocks.CYAN_AMPHORA.get()))
                .unlockedBy("has_ingredient_6", has(BGBlocks.GREEN_AMPHORA.get()))
                .unlockedBy("has_ingredient_7", has(BGBlocks.LIGHT_BLUE_AMPHORA.get()))
                .unlockedBy("has_ingredient_8", has(BGBlocks.LIGHT_GRAY_AMPHORA.get()))
                .unlockedBy("has_ingredient_9", has(BGBlocks.LIME_AMPHORA.get()))
                .unlockedBy("has_ingredient_10", has(BGBlocks.MAGENTA_AMPHORA.get()))
                .unlockedBy("has_ingredient_11", has(BGBlocks.ORANGE_AMPHORA.get()))
                .unlockedBy("has_ingredient_12", has(BGBlocks.PINK_AMPHORA.get()))
                .unlockedBy("has_ingredient_13", has(BGBlocks.PURPLE_AMPHORA.get()))
                .unlockedBy("has_ingredient_14", has(BGBlocks.RED_AMPHORA.get()))
                .unlockedBy("has_ingredient_15", has(BGBlocks.YELLOW_AMPHORA.get()))
                .unlockedBy("has_ingredient_16", has(BGBlocks.WHITE_AMPHORA.get()))
                .save(output, Bygone.id("dye_gray_amphora"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, BGBlocks.GREEN_AMPHORA.get(), 1)
                .requires(Items.GREEN_DYE)
                .requires(Ingredient.of(BGBlocks.AMPHORA.get(), BGBlocks.BLACK_AMPHORA.get(), BGBlocks.BLUE_AMPHORA.get(), BGBlocks.BROWN_AMPHORA.get(), BGBlocks.CYAN_AMPHORA.get(), BGBlocks.GRAY_AMPHORA.get(), BGBlocks.LIGHT_BLUE_AMPHORA.get(), BGBlocks.LIGHT_GRAY_AMPHORA.get(), BGBlocks.LIME_AMPHORA.get(), BGBlocks.MAGENTA_AMPHORA.get(), BGBlocks.ORANGE_AMPHORA.get(), BGBlocks.PINK_AMPHORA.get(), BGBlocks.PURPLE_AMPHORA.get(), BGBlocks.RED_AMPHORA.get(), BGBlocks.YELLOW_AMPHORA.get(), BGBlocks.WHITE_AMPHORA.get()))
                .group("wool")
                .unlockedBy("has_ingredient_1", has(BGBlocks.AMPHORA.get()))
                .unlockedBy("has_ingredient_2", has(BGBlocks.BLACK_AMPHORA.get()))
                .unlockedBy("has_ingredient_3", has(BGBlocks.BLUE_AMPHORA.get()))
                .unlockedBy("has_ingredient_4", has(BGBlocks.BROWN_AMPHORA.get()))
                .unlockedBy("has_ingredient_5", has(BGBlocks.CYAN_AMPHORA.get()))
                .unlockedBy("has_ingredient_6", has(BGBlocks.GRAY_AMPHORA.get()))
                .unlockedBy("has_ingredient_7", has(BGBlocks.LIGHT_BLUE_AMPHORA.get()))
                .unlockedBy("has_ingredient_8", has(BGBlocks.LIGHT_GRAY_AMPHORA.get()))
                .unlockedBy("has_ingredient_9", has(BGBlocks.LIME_AMPHORA.get()))
                .unlockedBy("has_ingredient_10", has(BGBlocks.MAGENTA_AMPHORA.get()))
                .unlockedBy("has_ingredient_11", has(BGBlocks.ORANGE_AMPHORA.get()))
                .unlockedBy("has_ingredient_12", has(BGBlocks.PINK_AMPHORA.get()))
                .unlockedBy("has_ingredient_13", has(BGBlocks.PURPLE_AMPHORA.get()))
                .unlockedBy("has_ingredient_14", has(BGBlocks.RED_AMPHORA.get()))
                .unlockedBy("has_ingredient_15", has(BGBlocks.YELLOW_AMPHORA.get()))
                .unlockedBy("has_ingredient_16", has(BGBlocks.WHITE_AMPHORA.get()))
                .save(output, Bygone.id("dye_green_amphora"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, BGBlocks.LIGHT_BLUE_AMPHORA.get(), 1)
                .requires(Items.LIGHT_BLUE_DYE)
                .requires(Ingredient.of(BGBlocks.AMPHORA.get(), BGBlocks.BLACK_AMPHORA.get(), BGBlocks.BLUE_AMPHORA.get(), BGBlocks.BROWN_AMPHORA.get(), BGBlocks.CYAN_AMPHORA.get(), BGBlocks.GRAY_AMPHORA.get(), BGBlocks.GREEN_AMPHORA.get(), BGBlocks.LIGHT_GRAY_AMPHORA.get(), BGBlocks.LIME_AMPHORA.get(), BGBlocks.MAGENTA_AMPHORA.get(), BGBlocks.ORANGE_AMPHORA.get(), BGBlocks.PINK_AMPHORA.get(), BGBlocks.PURPLE_AMPHORA.get(), BGBlocks.RED_AMPHORA.get(), BGBlocks.YELLOW_AMPHORA.get(), BGBlocks.WHITE_AMPHORA.get()))
                .group("wool")
                .unlockedBy("has_ingredient_1", has(BGBlocks.AMPHORA.get()))
                .unlockedBy("has_ingredient_2", has(BGBlocks.BLACK_AMPHORA.get()))
                .unlockedBy("has_ingredient_3", has(BGBlocks.BLUE_AMPHORA.get()))
                .unlockedBy("has_ingredient_4", has(BGBlocks.BROWN_AMPHORA.get()))
                .unlockedBy("has_ingredient_5", has(BGBlocks.CYAN_AMPHORA.get()))
                .unlockedBy("has_ingredient_6", has(BGBlocks.GRAY_AMPHORA.get()))
                .unlockedBy("has_ingredient_7", has(BGBlocks.GREEN_AMPHORA.get()))
                .unlockedBy("has_ingredient_8", has(BGBlocks.LIGHT_GRAY_AMPHORA.get()))
                .unlockedBy("has_ingredient_9", has(BGBlocks.LIME_AMPHORA.get()))
                .unlockedBy("has_ingredient_10", has(BGBlocks.MAGENTA_AMPHORA.get()))
                .unlockedBy("has_ingredient_11", has(BGBlocks.ORANGE_AMPHORA.get()))
                .unlockedBy("has_ingredient_12", has(BGBlocks.PINK_AMPHORA.get()))
                .unlockedBy("has_ingredient_13", has(BGBlocks.PURPLE_AMPHORA.get()))
                .unlockedBy("has_ingredient_14", has(BGBlocks.RED_AMPHORA.get()))
                .unlockedBy("has_ingredient_15", has(BGBlocks.YELLOW_AMPHORA.get()))
                .unlockedBy("has_ingredient_16", has(BGBlocks.WHITE_AMPHORA.get()))
                .save(output, Bygone.id("dye_light_blue_amphora"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, BGBlocks.LIGHT_GRAY_AMPHORA.get(), 1)
                .requires(Items.LIGHT_GRAY_DYE)
                .requires(Ingredient.of(BGBlocks.AMPHORA.get(), BGBlocks.BLACK_AMPHORA.get(), BGBlocks.BLACK_AMPHORA.get(), BGBlocks.BLUE_AMPHORA.get(), BGBlocks.BROWN_AMPHORA.get(), BGBlocks.CYAN_AMPHORA.get(), BGBlocks.GRAY_AMPHORA.get(), BGBlocks.GREEN_AMPHORA.get(), BGBlocks.LIGHT_BLUE_AMPHORA.get(), BGBlocks.LIME_AMPHORA.get(), BGBlocks.MAGENTA_AMPHORA.get(), BGBlocks.ORANGE_AMPHORA.get(), BGBlocks.PINK_AMPHORA.get(), BGBlocks.PURPLE_AMPHORA.get(), BGBlocks.RED_AMPHORA.get(), BGBlocks.YELLOW_AMPHORA.get(), BGBlocks.WHITE_AMPHORA.get()))
                .group("wool")
                .unlockedBy("has_ingredient_1", has(BGBlocks.AMPHORA.get()))
                .unlockedBy("has_ingredient_2", has(BGBlocks.BLACK_AMPHORA.get()))
                .unlockedBy("has_ingredient_3", has(BGBlocks.BLUE_AMPHORA.get()))
                .unlockedBy("has_ingredient_4", has(BGBlocks.BROWN_AMPHORA.get()))
                .unlockedBy("has_ingredient_5", has(BGBlocks.CYAN_AMPHORA.get()))
                .unlockedBy("has_ingredient_6", has(BGBlocks.GRAY_AMPHORA.get()))
                .unlockedBy("has_ingredient_7", has(BGBlocks.GREEN_AMPHORA.get()))
                .unlockedBy("has_ingredient_8", has(BGBlocks.LIGHT_BLUE_AMPHORA.get()))
                .unlockedBy("has_ingredient_9", has(BGBlocks.LIME_AMPHORA.get()))
                .unlockedBy("has_ingredient_10", has(BGBlocks.MAGENTA_AMPHORA.get()))
                .unlockedBy("has_ingredient_11", has(BGBlocks.ORANGE_AMPHORA.get()))
                .unlockedBy("has_ingredient_12", has(BGBlocks.PINK_AMPHORA.get()))
                .unlockedBy("has_ingredient_13", has(BGBlocks.PURPLE_AMPHORA.get()))
                .unlockedBy("has_ingredient_14", has(BGBlocks.RED_AMPHORA.get()))
                .unlockedBy("has_ingredient_15", has(BGBlocks.YELLOW_AMPHORA.get()))
                .unlockedBy("has_ingredient_16", has(BGBlocks.WHITE_AMPHORA.get()))
                .save(output, Bygone.id("dye_light_gray_amphora"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, BGBlocks.LIME_AMPHORA.get(), 1)
                .requires(Items.LIME_DYE)
                .requires(Ingredient.of(BGBlocks.AMPHORA.get(), BGBlocks.BLACK_AMPHORA.get(), BGBlocks.BLUE_AMPHORA.get(), BGBlocks.BROWN_AMPHORA.get(), BGBlocks.CYAN_AMPHORA.get(), BGBlocks.GRAY_AMPHORA.get(), BGBlocks.GREEN_AMPHORA.get(), BGBlocks.LIGHT_BLUE_AMPHORA.get(), BGBlocks.LIGHT_GRAY_AMPHORA.get(), BGBlocks.MAGENTA_AMPHORA.get(), BGBlocks.ORANGE_AMPHORA.get(), BGBlocks.PINK_AMPHORA.get(), BGBlocks.PURPLE_AMPHORA.get(), BGBlocks.RED_AMPHORA.get(), BGBlocks.YELLOW_AMPHORA.get(), BGBlocks.WHITE_AMPHORA.get()))
                .group("wool")
                .unlockedBy("has_ingredient_1", has(BGBlocks.AMPHORA.get()))
                .unlockedBy("has_ingredient_2", has(BGBlocks.BLACK_AMPHORA.get()))
                .unlockedBy("has_ingredient_3", has(BGBlocks.BLUE_AMPHORA.get()))
                .unlockedBy("has_ingredient_4", has(BGBlocks.BROWN_AMPHORA.get()))
                .unlockedBy("has_ingredient_5", has(BGBlocks.CYAN_AMPHORA.get()))
                .unlockedBy("has_ingredient_6", has(BGBlocks.GRAY_AMPHORA.get()))
                .unlockedBy("has_ingredient_7", has(BGBlocks.GREEN_AMPHORA.get()))
                .unlockedBy("has_ingredient_8", has(BGBlocks.LIGHT_BLUE_AMPHORA.get()))
                .unlockedBy("has_ingredient_9", has(BGBlocks.LIGHT_GRAY_AMPHORA.get()))
                .unlockedBy("has_ingredient_10", has(BGBlocks.MAGENTA_AMPHORA.get()))
                .unlockedBy("has_ingredient_11", has(BGBlocks.ORANGE_AMPHORA.get()))
                .unlockedBy("has_ingredient_12", has(BGBlocks.PINK_AMPHORA.get()))
                .unlockedBy("has_ingredient_13", has(BGBlocks.PURPLE_AMPHORA.get()))
                .unlockedBy("has_ingredient_14", has(BGBlocks.RED_AMPHORA.get()))
                .unlockedBy("has_ingredient_15", has(BGBlocks.YELLOW_AMPHORA.get()))
                .unlockedBy("has_ingredient_16", has(BGBlocks.WHITE_AMPHORA.get()))
                .save(output, Bygone.id("dye_lime_amphora"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, BGBlocks.MAGENTA_AMPHORA.get(), 1)
                .requires(Items.MAGENTA_DYE)
                .requires(Ingredient.of(BGBlocks.AMPHORA.get(), BGBlocks.BLACK_AMPHORA.get(), BGBlocks.BLUE_AMPHORA.get(), BGBlocks.BROWN_AMPHORA.get(), BGBlocks.CYAN_AMPHORA.get(), BGBlocks.GRAY_AMPHORA.get(), BGBlocks.GREEN_AMPHORA.get(), BGBlocks.LIGHT_BLUE_AMPHORA.get(), BGBlocks.LIGHT_GRAY_AMPHORA.get(), BGBlocks.LIME_AMPHORA.get(), BGBlocks.ORANGE_AMPHORA.get(), BGBlocks.PINK_AMPHORA.get(), BGBlocks.PURPLE_AMPHORA.get(), BGBlocks.RED_AMPHORA.get(), BGBlocks.YELLOW_AMPHORA.get(), BGBlocks.WHITE_AMPHORA.get()))
                .group("wool")
                .unlockedBy("has_ingredient_1", has(BGBlocks.AMPHORA.get()))
                .unlockedBy("has_ingredient_2", has(BGBlocks.BLACK_AMPHORA.get()))
                .unlockedBy("has_ingredient_3", has(BGBlocks.BLUE_AMPHORA.get()))
                .unlockedBy("has_ingredient_4", has(BGBlocks.BROWN_AMPHORA.get()))
                .unlockedBy("has_ingredient_5", has(BGBlocks.CYAN_AMPHORA.get()))
                .unlockedBy("has_ingredient_6", has(BGBlocks.GRAY_AMPHORA.get()))
                .unlockedBy("has_ingredient_7", has(BGBlocks.GREEN_AMPHORA.get()))
                .unlockedBy("has_ingredient_8", has(BGBlocks.LIGHT_BLUE_AMPHORA.get()))
                .unlockedBy("has_ingredient_9", has(BGBlocks.LIGHT_GRAY_AMPHORA.get()))
                .unlockedBy("has_ingredient_10", has(BGBlocks.LIME_AMPHORA.get()))
                .unlockedBy("has_ingredient_11", has(BGBlocks.ORANGE_AMPHORA.get()))
                .unlockedBy("has_ingredient_12", has(BGBlocks.PINK_AMPHORA.get()))
                .unlockedBy("has_ingredient_13", has(BGBlocks.PURPLE_AMPHORA.get()))
                .unlockedBy("has_ingredient_14", has(BGBlocks.RED_AMPHORA.get()))
                .unlockedBy("has_ingredient_15", has(BGBlocks.YELLOW_AMPHORA.get()))
                .unlockedBy("has_ingredient_16", has(BGBlocks.WHITE_AMPHORA.get()))
                .save(output, Bygone.id("dye_magenta_amphora"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, BGBlocks.ORANGE_AMPHORA.get(), 1)
                .requires(Items.ORANGE_DYE)
                .requires(Ingredient.of(BGBlocks.AMPHORA.get(), BGBlocks.BLACK_AMPHORA.get(), BGBlocks.BLUE_AMPHORA.get(), BGBlocks.BROWN_AMPHORA.get(), BGBlocks.CYAN_AMPHORA.get(), BGBlocks.GRAY_AMPHORA.get(), BGBlocks.GREEN_AMPHORA.get(), BGBlocks.LIGHT_BLUE_AMPHORA.get(), BGBlocks.LIGHT_GRAY_AMPHORA.get(), BGBlocks.LIME_AMPHORA.get(), BGBlocks.MAGENTA_AMPHORA.get(), BGBlocks.PINK_AMPHORA.get(), BGBlocks.PURPLE_AMPHORA.get(), BGBlocks.RED_AMPHORA.get(), BGBlocks.YELLOW_AMPHORA.get(), BGBlocks.WHITE_AMPHORA.get()))
                .group("wool")
                .unlockedBy("has_ingredient_1", has(BGBlocks.AMPHORA.get()))
                .unlockedBy("has_ingredient_2", has(BGBlocks.BLACK_AMPHORA.get()))
                .unlockedBy("has_ingredient_3", has(BGBlocks.BLUE_AMPHORA.get()))
                .unlockedBy("has_ingredient_4", has(BGBlocks.BROWN_AMPHORA.get()))
                .unlockedBy("has_ingredient_5", has(BGBlocks.CYAN_AMPHORA.get()))
                .unlockedBy("has_ingredient_6", has(BGBlocks.GRAY_AMPHORA.get()))
                .unlockedBy("has_ingredient_7", has(BGBlocks.GREEN_AMPHORA.get()))
                .unlockedBy("has_ingredient_8", has(BGBlocks.LIGHT_BLUE_AMPHORA.get()))
                .unlockedBy("has_ingredient_9", has(BGBlocks.LIGHT_GRAY_AMPHORA.get()))
                .unlockedBy("has_ingredient_10", has(BGBlocks.LIME_AMPHORA.get()))
                .unlockedBy("has_ingredient_11", has(BGBlocks.MAGENTA_AMPHORA.get()))
                .unlockedBy("has_ingredient_12", has(BGBlocks.PINK_AMPHORA.get()))
                .unlockedBy("has_ingredient_13", has(BGBlocks.PURPLE_AMPHORA.get()))
                .unlockedBy("has_ingredient_14", has(BGBlocks.RED_AMPHORA.get()))
                .unlockedBy("has_ingredient_15", has(BGBlocks.YELLOW_AMPHORA.get()))
                .unlockedBy("has_ingredient_16", has(BGBlocks.WHITE_AMPHORA.get()))
                .save(output, Bygone.id("dye_orange_amphora"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, BGBlocks.PINK_AMPHORA.get(), 1)
                .requires(Items.PINK_DYE)
                .requires(Ingredient.of(BGBlocks.AMPHORA.get(), BGBlocks.BLACK_AMPHORA.get(), BGBlocks.BLUE_AMPHORA.get(), BGBlocks.BROWN_AMPHORA.get(), BGBlocks.CYAN_AMPHORA.get(), BGBlocks.GRAY_AMPHORA.get(), BGBlocks.GREEN_AMPHORA.get(), BGBlocks.LIGHT_BLUE_AMPHORA.get(), BGBlocks.LIGHT_GRAY_AMPHORA.get(), BGBlocks.LIME_AMPHORA.get(), BGBlocks.MAGENTA_AMPHORA.get(), BGBlocks.ORANGE_AMPHORA.get(), BGBlocks.PURPLE_AMPHORA.get(), BGBlocks.RED_AMPHORA.get(), BGBlocks.YELLOW_AMPHORA.get(), BGBlocks.WHITE_AMPHORA.get()))
                .group("wool")
                .unlockedBy("has_ingredient_1", has(BGBlocks.AMPHORA.get()))
                .unlockedBy("has_ingredient_2", has(BGBlocks.BLACK_AMPHORA.get()))
                .unlockedBy("has_ingredient_3", has(BGBlocks.BLUE_AMPHORA.get()))
                .unlockedBy("has_ingredient_4", has(BGBlocks.BROWN_AMPHORA.get()))
                .unlockedBy("has_ingredient_5", has(BGBlocks.CYAN_AMPHORA.get()))
                .unlockedBy("has_ingredient_6", has(BGBlocks.GRAY_AMPHORA.get()))
                .unlockedBy("has_ingredient_7", has(BGBlocks.GREEN_AMPHORA.get()))
                .unlockedBy("has_ingredient_8", has(BGBlocks.LIGHT_BLUE_AMPHORA.get()))
                .unlockedBy("has_ingredient_9", has(BGBlocks.LIGHT_GRAY_AMPHORA.get()))
                .unlockedBy("has_ingredient_10", has(BGBlocks.LIME_AMPHORA.get()))
                .unlockedBy("has_ingredient_11", has(BGBlocks.MAGENTA_AMPHORA.get()))
                .unlockedBy("has_ingredient_12", has(BGBlocks.ORANGE_AMPHORA.get()))
                .unlockedBy("has_ingredient_13", has(BGBlocks.PURPLE_AMPHORA.get()))
                .unlockedBy("has_ingredient_14", has(BGBlocks.RED_AMPHORA.get()))
                .unlockedBy("has_ingredient_15", has(BGBlocks.YELLOW_AMPHORA.get()))
                .unlockedBy("has_ingredient_16", has(BGBlocks.WHITE_AMPHORA.get()))
                .save(output, Bygone.id("dye_pink_amphora"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, BGBlocks.PURPLE_AMPHORA.get(), 1)
                .requires(Items.PURPLE_DYE)
                .requires(Ingredient.of(BGBlocks.AMPHORA.get(), BGBlocks.BLACK_AMPHORA.get(), BGBlocks.BLUE_AMPHORA.get(), BGBlocks.BROWN_AMPHORA.get(), BGBlocks.CYAN_AMPHORA.get(), BGBlocks.GRAY_AMPHORA.get(), BGBlocks.GREEN_AMPHORA.get(), BGBlocks.LIGHT_BLUE_AMPHORA.get(), BGBlocks.LIGHT_GRAY_AMPHORA.get(), BGBlocks.LIME_AMPHORA.get(), BGBlocks.MAGENTA_AMPHORA.get(), BGBlocks.ORANGE_AMPHORA.get(), BGBlocks.PINK_AMPHORA.get(), BGBlocks.RED_AMPHORA.get(), BGBlocks.YELLOW_AMPHORA.get(), BGBlocks.WHITE_AMPHORA.get()))
                .group("wool")
                .unlockedBy("has_ingredient_1", has(BGBlocks.AMPHORA.get()))
                .unlockedBy("has_ingredient_2", has(BGBlocks.BLACK_AMPHORA.get()))
                .unlockedBy("has_ingredient_3", has(BGBlocks.BLUE_AMPHORA.get()))
                .unlockedBy("has_ingredient_4", has(BGBlocks.BROWN_AMPHORA.get()))
                .unlockedBy("has_ingredient_5", has(BGBlocks.CYAN_AMPHORA.get()))
                .unlockedBy("has_ingredient_6", has(BGBlocks.GRAY_AMPHORA.get()))
                .unlockedBy("has_ingredient_7", has(BGBlocks.GREEN_AMPHORA.get()))
                .unlockedBy("has_ingredient_8", has(BGBlocks.LIGHT_BLUE_AMPHORA.get()))
                .unlockedBy("has_ingredient_9", has(BGBlocks.LIGHT_GRAY_AMPHORA.get()))
                .unlockedBy("has_ingredient_10", has(BGBlocks.LIME_AMPHORA.get()))
                .unlockedBy("has_ingredient_11", has(BGBlocks.MAGENTA_AMPHORA.get()))
                .unlockedBy("has_ingredient_12", has(BGBlocks.ORANGE_AMPHORA.get()))
                .unlockedBy("has_ingredient_13", has(BGBlocks.PINK_AMPHORA.get()))
                .unlockedBy("has_ingredient_14", has(BGBlocks.RED_AMPHORA.get()))
                .unlockedBy("has_ingredient_15", has(BGBlocks.YELLOW_AMPHORA.get()))
                .unlockedBy("has_ingredient_16", has(BGBlocks.WHITE_AMPHORA.get()))
                .save(output, Bygone.id("dye_purple_amphora"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, BGBlocks.RED_AMPHORA.get(), 1)
                .requires(Items.RED_DYE)
                .requires(Ingredient.of(BGBlocks.AMPHORA.get(), BGBlocks.BLACK_AMPHORA.get(), BGBlocks.BLUE_AMPHORA.get(), BGBlocks.BROWN_AMPHORA.get(), BGBlocks.CYAN_AMPHORA.get(), BGBlocks.GRAY_AMPHORA.get(), BGBlocks.GREEN_AMPHORA.get(), BGBlocks.LIGHT_BLUE_AMPHORA.get(), BGBlocks.LIGHT_GRAY_AMPHORA.get(), BGBlocks.LIME_AMPHORA.get(), BGBlocks.MAGENTA_AMPHORA.get(), BGBlocks.ORANGE_AMPHORA.get(), BGBlocks.PINK_AMPHORA.get(), BGBlocks.PURPLE_AMPHORA.get(), BGBlocks.YELLOW_AMPHORA.get(), BGBlocks.WHITE_AMPHORA.get()))
                .group("wool")
                .unlockedBy("has_ingredient_1", has(BGBlocks.AMPHORA.get()))
                .unlockedBy("has_ingredient_2", has(BGBlocks.BLACK_AMPHORA.get()))
                .unlockedBy("has_ingredient_3", has(BGBlocks.BLUE_AMPHORA.get()))
                .unlockedBy("has_ingredient_4", has(BGBlocks.BROWN_AMPHORA.get()))
                .unlockedBy("has_ingredient_5", has(BGBlocks.CYAN_AMPHORA.get()))
                .unlockedBy("has_ingredient_6", has(BGBlocks.GRAY_AMPHORA.get()))
                .unlockedBy("has_ingredient_7", has(BGBlocks.GREEN_AMPHORA.get()))
                .unlockedBy("has_ingredient_8", has(BGBlocks.LIGHT_BLUE_AMPHORA.get()))
                .unlockedBy("has_ingredient_9", has(BGBlocks.LIGHT_GRAY_AMPHORA.get()))
                .unlockedBy("has_ingredient_10", has(BGBlocks.LIME_AMPHORA.get()))
                .unlockedBy("has_ingredient_11", has(BGBlocks.MAGENTA_AMPHORA.get()))
                .unlockedBy("has_ingredient_12", has(BGBlocks.ORANGE_AMPHORA.get()))
                .unlockedBy("has_ingredient_13", has(BGBlocks.PINK_AMPHORA.get()))
                .unlockedBy("has_ingredient_14", has(BGBlocks.PURPLE_AMPHORA.get()))
                .unlockedBy("has_ingredient_15", has(BGBlocks.YELLOW_AMPHORA.get()))
                .unlockedBy("has_ingredient_16", has(BGBlocks.WHITE_AMPHORA.get()))
                .save(output, Bygone.id("dye_red_amphora"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, BGBlocks.WHITE_AMPHORA.get(), 1)
                .requires(Items.WHITE_DYE)
                .requires(Ingredient.of(BGBlocks.AMPHORA.get(), BGBlocks.BLACK_AMPHORA.get(), BGBlocks.BLUE_AMPHORA.get(), BGBlocks.BROWN_AMPHORA.get(), BGBlocks.CYAN_AMPHORA.get(), BGBlocks.GRAY_AMPHORA.get(), BGBlocks.GREEN_AMPHORA.get(), BGBlocks.LIGHT_BLUE_AMPHORA.get(), BGBlocks.LIGHT_GRAY_AMPHORA.get(), BGBlocks.LIME_AMPHORA.get(), BGBlocks.MAGENTA_AMPHORA.get(), BGBlocks.ORANGE_AMPHORA.get(), BGBlocks.PINK_AMPHORA.get(), BGBlocks.PURPLE_AMPHORA.get(), BGBlocks.RED_AMPHORA.get(), BGBlocks.YELLOW_AMPHORA.get()))
                .group("wool")
                .unlockedBy("has_ingredient_1", has(BGBlocks.AMPHORA.get()))
                .unlockedBy("has_ingredient_2", has(BGBlocks.BLACK_AMPHORA.get()))
                .unlockedBy("has_ingredient_3", has(BGBlocks.BLUE_AMPHORA.get()))
                .unlockedBy("has_ingredient_4", has(BGBlocks.BROWN_AMPHORA.get()))
                .unlockedBy("has_ingredient_5", has(BGBlocks.CYAN_AMPHORA.get()))
                .unlockedBy("has_ingredient_6", has(BGBlocks.GRAY_AMPHORA.get()))
                .unlockedBy("has_ingredient_7", has(BGBlocks.GREEN_AMPHORA.get()))
                .unlockedBy("has_ingredient_8", has(BGBlocks.LIGHT_BLUE_AMPHORA.get()))
                .unlockedBy("has_ingredient_9", has(BGBlocks.LIGHT_GRAY_AMPHORA.get()))
                .unlockedBy("has_ingredient_10", has(BGBlocks.LIME_AMPHORA.get()))
                .unlockedBy("has_ingredient_11", has(BGBlocks.MAGENTA_AMPHORA.get()))
                .unlockedBy("has_ingredient_12", has(BGBlocks.ORANGE_AMPHORA.get()))
                .unlockedBy("has_ingredient_13", has(BGBlocks.PINK_AMPHORA.get()))
                .unlockedBy("has_ingredient_14", has(BGBlocks.PURPLE_AMPHORA.get()))
                .unlockedBy("has_ingredient_15", has(BGBlocks.RED_AMPHORA.get()))
                .unlockedBy("has_ingredient_16", has(BGBlocks.YELLOW_AMPHORA.get()))
                .save(output, Bygone.id("dye_white_amphora"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, BGBlocks.YELLOW_AMPHORA.get(), 1)
                .requires(Items.YELLOW_DYE)
                .requires(Ingredient.of(BGBlocks.AMPHORA.get(), BGBlocks.BLACK_AMPHORA.get(), BGBlocks.BLUE_AMPHORA.get(), BGBlocks.BROWN_AMPHORA.get(), BGBlocks.CYAN_AMPHORA.get(), BGBlocks.GRAY_AMPHORA.get(), BGBlocks.GREEN_AMPHORA.get(), BGBlocks.LIGHT_BLUE_AMPHORA.get(), BGBlocks.LIGHT_GRAY_AMPHORA.get(), BGBlocks.LIME_AMPHORA.get(), BGBlocks.MAGENTA_AMPHORA.get(), BGBlocks.ORANGE_AMPHORA.get(), BGBlocks.PINK_AMPHORA.get(), BGBlocks.PURPLE_AMPHORA.get(), BGBlocks.RED_AMPHORA.get(), BGBlocks.WHITE_AMPHORA.get()))
                .group("wool")
                .unlockedBy("has_ingredient_1", has(BGBlocks.AMPHORA.get()))
                .unlockedBy("has_ingredient_2", has(BGBlocks.BLACK_AMPHORA.get()))
                .unlockedBy("has_ingredient_3", has(BGBlocks.BLUE_AMPHORA.get()))
                .unlockedBy("has_ingredient_4", has(BGBlocks.BROWN_AMPHORA.get()))
                .unlockedBy("has_ingredient_5", has(BGBlocks.CYAN_AMPHORA.get()))
                .unlockedBy("has_ingredient_6", has(BGBlocks.GRAY_AMPHORA.get()))
                .unlockedBy("has_ingredient_7", has(BGBlocks.GREEN_AMPHORA.get()))
                .unlockedBy("has_ingredient_8", has(BGBlocks.LIGHT_BLUE_AMPHORA.get()))
                .unlockedBy("has_ingredient_9", has(BGBlocks.LIGHT_GRAY_AMPHORA.get()))
                .unlockedBy("has_ingredient_10", has(BGBlocks.LIME_AMPHORA.get()))
                .unlockedBy("has_ingredient_11", has(BGBlocks.MAGENTA_AMPHORA.get()))
                .unlockedBy("has_ingredient_12", has(BGBlocks.ORANGE_AMPHORA.get()))
                .unlockedBy("has_ingredient_13", has(BGBlocks.PINK_AMPHORA.get()))
                .unlockedBy("has_ingredient_14", has(BGBlocks.PURPLE_AMPHORA.get()))
                .unlockedBy("has_ingredient_15", has(BGBlocks.RED_AMPHORA.get()))
                .unlockedBy("has_ingredient_16", has(BGBlocks.WHITE_AMPHORA.get()))
                .save(output, Bygone.id("dye_yellow_amphora"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, BGItems.GOURD_DANGO.get(), 3)
                .requires(BGItems.BEIGE_SLICE.get())
                .requires(BGItems.MUAVE_SLICE.get())
                .requires(BGItems.VERDANT_SLICE.get())
                .unlockedBy("has_ingredient_1", has(BGItems.BEIGE_SLICE.get()))
                .unlockedBy("has_ingredient_2", has(BGItems.MUAVE_SLICE.get()))
                .unlockedBy("has_ingredient_3", has(BGItems.VERDANT_SLICE.get()))
                .save(output, Bygone.id("glow_gourd_dango"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BGItems.GOURD_SOUP.get(), 1)
                .requires(BGItems.BEIGE_SLICE.get())
                .requires(BGItems.MUAVE_SLICE.get())
                .requires(BGItems.VERDANT_SLICE.get())
                .requires(Items.BOWL)
                .unlockedBy("has_ingredient_1", has(BGItems.BEIGE_SLICE.get()))
                .unlockedBy("has_ingredient_2", has(BGItems.MUAVE_SLICE.get()))
                .unlockedBy("has_ingredient_3", has(BGItems.VERDANT_SLICE.get()))
                .save(output, Bygone.id("glow_gourd_soup"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, BGBlocks.GLOWING_PLASMIC_BYSLATE_BRICKS.get(), 1)
                .requires(BGBlocks.PLASMIC_BYSLATE_BRICKS.get())
                .requires(BGItems.LITHOPLASM.get())
                .unlockedBy("has_ingredient_1", has(BGBlocks.PLASMIC_BYSLATE_BRICKS.get()))
                .unlockedBy("has_ingredient_2", has(BGItems.LITHOPLASM.get()))
                .save(output, Bygone.id("glowing_plasmic_byslate_bricks"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, BGBlocks.GRAY_GLOW_CONCRETE_POWDER.get(), 8)
                .requires(Items.GRAY_DYE)
                .requires(BGBlocks.SHELLSAND.get())
                .requires(BGBlocks.SHELLSAND.get())
                .requires(BGBlocks.SHELLSAND.get())
                .requires(BGBlocks.SHELLSAND.get())
                .requires(BGBlocks.GLOW_GRAVEL.get())
                .requires(BGBlocks.GLOW_GRAVEL.get())
                .requires(BGBlocks.GLOW_GRAVEL.get())
                .requires(BGBlocks.GLOW_GRAVEL.get())
                .group("glow_concrete_powder")
                .unlockedBy("has_ingredient_1", has(BGBlocks.SHELLSAND.get()))
                .unlockedBy("has_ingredient_2", has(BGBlocks.GLOW_GRAVEL.get()))
                .save(output, Bygone.id("gray_glow_concrete_powder"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, BGBlocks.GREEN_GLOW_CONCRETE_POWDER.get(), 8)
                .requires(Items.GREEN_DYE)
                .requires(BGBlocks.SHELLSAND.get())
                .requires(BGBlocks.SHELLSAND.get())
                .requires(BGBlocks.SHELLSAND.get())
                .requires(BGBlocks.SHELLSAND.get())
                .requires(BGBlocks.GLOW_GRAVEL.get())
                .requires(BGBlocks.GLOW_GRAVEL.get())
                .requires(BGBlocks.GLOW_GRAVEL.get())
                .requires(BGBlocks.GLOW_GRAVEL.get())
                .group("glow_concrete_powder")
                .unlockedBy("has_ingredient_1", has(BGBlocks.SHELLSAND.get()))
                .unlockedBy("has_ingredient_2", has(BGBlocks.GLOW_GRAVEL.get()))
                .save(output, Bygone.id("green_glow_concrete_powder"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, BGBlocks.HOLLOW_MEGALITH_LANTERN.get(), 1)
                .requires(BGBlocks.HOLLOW_MEGALITH_FACE.get())
                .requires(BGItems.LITHOPLASM.get())
                .unlockedBy("has_ingredient_1", has(BGBlocks.HOLLOW_MEGALITH_FACE.get()))
                .unlockedBy("has_ingredient_2", has(BGItems.LITHOPLASM.get()))
                .save(output, Bygone.id("hollow_megalith_lantern"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.WHITE_DYE, 1)
                .requires(BGBlocks.MONTSECHIA.get())
                .group("white_dye")
                .unlockedBy("has_ingredient_1", has(BGBlocks.MONTSECHIA.get()))
                .save(output, Bygone.id("light_blue_dye_from_montsechia"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, BGBlocks.LIGHT_BLUE_GLOW_CONCRETE_POWDER.get(), 8)
                .requires(Items.LIGHT_BLUE_DYE)
                .requires(BGBlocks.SHELLSAND.get())
                .requires(BGBlocks.SHELLSAND.get())
                .requires(BGBlocks.SHELLSAND.get())
                .requires(BGBlocks.SHELLSAND.get())
                .requires(BGBlocks.GLOW_GRAVEL.get())
                .requires(BGBlocks.GLOW_GRAVEL.get())
                .requires(BGBlocks.GLOW_GRAVEL.get())
                .requires(BGBlocks.GLOW_GRAVEL.get())
                .group("glow_concrete_powder")
                .unlockedBy("has_ingredient_1", has(BGBlocks.SHELLSAND.get()))
                .unlockedBy("has_ingredient_2", has(BGBlocks.GLOW_GRAVEL.get()))
                .save(output, Bygone.id("light_blue_glow_concrete_powder"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.LIGHT_GRAY_DYE, 1)
                .requires(BGBlocks.MOON_BLOSSOM.get())
                .group("white_dye")
                .unlockedBy("has_ingredient_1", has(BGBlocks.MOON_BLOSSOM.get()))
                .save(output, Bygone.id("light_gray_dye_from_moon_blossom"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, BGBlocks.LIGHT_GRAY_GLOW_CONCRETE_POWDER.get(), 8)
                .requires(Items.LIGHT_GRAY_DYE)
                .requires(BGBlocks.SHELLSAND.get())
                .requires(BGBlocks.SHELLSAND.get())
                .requires(BGBlocks.SHELLSAND.get())
                .requires(BGBlocks.SHELLSAND.get())
                .requires(BGBlocks.GLOW_GRAVEL.get())
                .requires(BGBlocks.GLOW_GRAVEL.get())
                .requires(BGBlocks.GLOW_GRAVEL.get())
                .requires(BGBlocks.GLOW_GRAVEL.get())
                .group("glow_concrete_powder")
                .unlockedBy("has_ingredient_1", has(BGBlocks.SHELLSAND.get()))
                .unlockedBy("has_ingredient_2", has(BGBlocks.GLOW_GRAVEL.get()))
                .save(output, Bygone.id("light_gray_glow_concrete_powder"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.LIME_DYE, 1)
                .requires(BGBlocks.LIME_SAGARIA.get())
                .group("lime_dye")
                .unlockedBy("has_ingredient_1", has(BGBlocks.LIME_SAGARIA.get()))
                .save(output, Bygone.id("lime_dye_from_lime_sagaria"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, BGBlocks.LIME_GLOW_CONCRETE_POWDER.get(), 8)
                .requires(Items.LIME_DYE)
                .requires(BGBlocks.SHELLSAND.get())
                .requires(BGBlocks.SHELLSAND.get())
                .requires(BGBlocks.SHELLSAND.get())
                .requires(BGBlocks.SHELLSAND.get())
                .requires(BGBlocks.GLOW_GRAVEL.get())
                .requires(BGBlocks.GLOW_GRAVEL.get())
                .requires(BGBlocks.GLOW_GRAVEL.get())
                .requires(BGBlocks.GLOW_GRAVEL.get())
                .group("glow_concrete_powder")
                .unlockedBy("has_ingredient_1", has(BGBlocks.SHELLSAND.get()))
                .unlockedBy("has_ingredient_2", has(BGBlocks.GLOW_GRAVEL.get()))
                .save(output, Bygone.id("lime_glow_concrete_powder"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, BGBlocks.LIT_CRACKED_MEGALITH_BLOCK.get(), 1)
                .requires(BGBlocks.CRACKED_MEGALITH_BLOCK.get())
                .requires(BGItems.LITHOPLASM.get())
                .unlockedBy("has_ingredient_1", has(BGBlocks.CRACKED_MEGALITH_BLOCK.get()))
                .unlockedBy("has_ingredient_2", has(BGItems.LITHOPLASM.get()))
                .save(output, Bygone.id("lit_cracked_megalith_block"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, BGItems.LITHOPLASM_ARROW.get(), 1)
                .requires(Items.ARROW)
                .requires(BGItems.LITHOPLASM.get())
                .unlockedBy("has_ingredient_1", has(BGItems.LITHOPLASM.get()))
                .save(output, Bygone.id("lithoplasm_arrow"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, BGItems.LITHOPLASMIC_POWDER.get(), 9)
                .requires(BGBlocks.LITHOPLASMIC_POWDER_BLOCK.get())
                .group("lithoplasmic_powder")
                .unlockedBy("has_ingredient_1", has(BGBlocks.LITHOPLASMIC_POWDER_BLOCK.get()))
                .save(output, Bygone.id("lithoplasmic_powder"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, BGBlocks.LITHOPLASMIC_POWDER_BLOCK.get(), 1)
                .requires(BGItems.LITHOPLASMIC_POWDER.get())
                .requires(BGItems.LITHOPLASMIC_POWDER.get())
                .requires(BGItems.LITHOPLASMIC_POWDER.get())
                .requires(BGItems.LITHOPLASMIC_POWDER.get())
                .requires(BGItems.LITHOPLASMIC_POWDER.get())
                .requires(BGItems.LITHOPLASMIC_POWDER.get())
                .requires(BGItems.LITHOPLASMIC_POWDER.get())
                .requires(BGItems.LITHOPLASMIC_POWDER.get())
                .requires(BGItems.LITHOPLASMIC_POWDER.get())
                .group("lithoplasmic_powder")
                .unlockedBy("has_ingredient_1", has(BGItems.LITHOPLASMIC_POWDER.get()))
                .save(output, Bygone.id("lithoplasmic_powder_block"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, BGBlocks.MAGENTA_GLOW_CONCRETE_POWDER.get(), 8)
                .requires(Items.MAGENTA_DYE)
                .requires(BGBlocks.SHELLSAND.get())
                .requires(BGBlocks.SHELLSAND.get())
                .requires(BGBlocks.SHELLSAND.get())
                .requires(BGBlocks.SHELLSAND.get())
                .requires(BGBlocks.GLOW_GRAVEL.get())
                .requires(BGBlocks.GLOW_GRAVEL.get())
                .requires(BGBlocks.GLOW_GRAVEL.get())
                .requires(BGBlocks.GLOW_GRAVEL.get())
                .group("glow_concrete_powder")
                .unlockedBy("has_ingredient_1", has(BGBlocks.SHELLSAND.get()))
                .unlockedBy("has_ingredient_2", has(BGBlocks.GLOW_GRAVEL.get()))
                .save(output, Bygone.id("magenta_glow_concrete_powder"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, BGBlocks.MEGALITH_LANTERN.get(), 1)
                .requires(BGBlocks.MEGALITH_FACE.get())
                .requires(BGItems.LITHOPLASM.get())
                .unlockedBy("has_ingredient_1", has(BGBlocks.MEGALITH_FACE.get()))
                .unlockedBy("has_ingredient_2", has(BGItems.LITHOPLASM.get()))
                .save(output, Bygone.id("megalith_lantern"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, BGBlocks.ORANGE_GLOW_CONCRETE_POWDER.get(), 8)
                .requires(Items.ORANGE_DYE)
                .requires(BGBlocks.SHELLSAND.get())
                .requires(BGBlocks.SHELLSAND.get())
                .requires(BGBlocks.SHELLSAND.get())
                .requires(BGBlocks.SHELLSAND.get())
                .requires(BGBlocks.GLOW_GRAVEL.get())
                .requires(BGBlocks.GLOW_GRAVEL.get())
                .requires(BGBlocks.GLOW_GRAVEL.get())
                .requires(BGBlocks.GLOW_GRAVEL.get())
                .group("glow_concrete_powder")
                .unlockedBy("has_ingredient_1", has(BGBlocks.SHELLSAND.get()))
                .unlockedBy("has_ingredient_2", has(BGBlocks.GLOW_GRAVEL.get()))
                .save(output, Bygone.id("orange_glow_concrete_powder"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.PINK_DYE, 1)
                .requires(BGBlocks.PINK_SAGARIA.get())
                .group("pink_dye")
                .unlockedBy("has_ingredient_1", has(BGBlocks.PINK_SAGARIA.get()))
                .save(output, Bygone.id("pink_dye_from_pink_sagaria"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, BGBlocks.PINK_GLOW_CONCRETE_POWDER.get(), 8)
                .requires(Items.PINK_DYE)
                .requires(BGBlocks.SHELLSAND.get())
                .requires(BGBlocks.SHELLSAND.get())
                .requires(BGBlocks.SHELLSAND.get())
                .requires(BGBlocks.SHELLSAND.get())
                .requires(BGBlocks.GLOW_GRAVEL.get())
                .requires(BGBlocks.GLOW_GRAVEL.get())
                .requires(BGBlocks.GLOW_GRAVEL.get())
                .requires(BGBlocks.GLOW_GRAVEL.get())
                .group("glow_concrete_powder")
                .unlockedBy("has_ingredient_1", has(BGBlocks.SHELLSAND.get()))
                .unlockedBy("has_ingredient_2", has(BGBlocks.GLOW_GRAVEL.get()))
                .save(output, Bygone.id("pink_glow_concrete_powder"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, BGBlocks.PLASMIC_BYSLATE.get(), 1)
                .requires(BGBlocks.BYSLATE.get())
                .requires(BGItems.LITHOPLASM.get())
                .unlockedBy("has_ingredient_1", has(BGBlocks.BYSLATE.get()))
                .unlockedBy("has_ingredient_2", has(BGItems.LITHOPLASM.get()))
                .save(output, Bygone.id("plasmic_byslate"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.PURPLE_DYE, 1)
                .requires(BGBlocks.PURPLE_SAGARIA.get())
                .group("purple_dye")
                .unlockedBy("has_ingredient_1", has(BGBlocks.PURPLE_SAGARIA.get()))
                .save(output, Bygone.id("purple_dye_from_purple_sagaria"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, BGBlocks.PURPLE_GLOW_CONCRETE_POWDER.get(), 8)
                .requires(Items.PURPLE_DYE)
                .requires(BGBlocks.SHELLSAND.get())
                .requires(BGBlocks.SHELLSAND.get())
                .requires(BGBlocks.SHELLSAND.get())
                .requires(BGBlocks.SHELLSAND.get())
                .requires(BGBlocks.GLOW_GRAVEL.get())
                .requires(BGBlocks.GLOW_GRAVEL.get())
                .requires(BGBlocks.GLOW_GRAVEL.get())
                .requires(BGBlocks.GLOW_GRAVEL.get())
                .group("glow_concrete_powder")
                .unlockedBy("has_ingredient_1", has(BGBlocks.SHELLSAND.get()))
                .unlockedBy("has_ingredient_2", has(BGBlocks.GLOW_GRAVEL.get()))
                .save(output, Bygone.id("purple_glow_concrete_powder"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.RED_DYE, 1)
                .requires(BGBlocks.ROSE.get())
                .group("red_dye")
                .unlockedBy("has_ingredient_1", has(BGBlocks.ROSE.get()))
                .save(output, Bygone.id("red_dye_from_rose"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, BGBlocks.RED_GLOW_CONCRETE_POWDER.get(), 8)
                .requires(Items.RED_DYE)
                .requires(BGBlocks.SHELLSAND.get())
                .requires(BGBlocks.SHELLSAND.get())
                .requires(BGBlocks.SHELLSAND.get())
                .requires(BGBlocks.SHELLSAND.get())
                .requires(BGBlocks.GLOW_GRAVEL.get())
                .requires(BGBlocks.GLOW_GRAVEL.get())
                .requires(BGBlocks.GLOW_GRAVEL.get())
                .requires(BGBlocks.GLOW_GRAVEL.get())
                .group("glow_concrete_powder")
                .unlockedBy("has_ingredient_1", has(BGBlocks.SHELLSAND.get()))
                .unlockedBy("has_ingredient_2", has(BGBlocks.GLOW_GRAVEL.get()))
                .save(output, Bygone.id("red_glow_concrete_powder"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BGBlocks.SABLE_BUTTON.get(), 1)
                .requires(BGBlocks.SABLE_PLANKS.get())
                .group("wooden_button")
                .unlockedBy("has_ingredient_1", has(BGBlocks.SABLE_PLANKS.get()))
                .save(output, Bygone.id("sable_button_from_sable_planks"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, BGBlocks.SABLE_MOSSY_BYSLATE.get(), 1)
                .requires(BGBlocks.BYSLATE.get())
                .requires(BGBlocks.SABLE_MOSS_BLOCK.get())
                .group("sable_mossy_byslate")
                .unlockedBy("has_ingredient_1", has(BGBlocks.BYSLATE.get()))
                .unlockedBy("has_ingredient_2", has(BGBlocks.SABLE_MOSS_BLOCK.get()))
                .save(output, Bygone.id("sable_mossy_byslate_from_moss_block"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BGBlocks.SABLENUT.get(), 9)
                .requires(BGBlocks.SABLENUT_BLOCK.get())
                .unlockedBy("has_ingredient_1", has(BGBlocks.SABLENUT_BLOCK.get()))
                .save(output, Bygone.id("sable_nut_block_to_sable_nut"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, BGBlocks.SABLE_PLANKS.get(), 4)
                .requires(tag("bygone:sable_logs"))
                .group("planks")
                .unlockedBy("has_logs", has(tag("bygone:sable_logs")))
                .save(output, Bygone.id("sable_planks"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, BGBlocks.PRISTINE_VERDIGRIS_COG.get(), 1)
                .requires(Items.REDSTONE)
                .requires(BGItems.VERDIGRIS_SCRAP.get())
                .unlockedBy("has_ingredient_1", has(BGItems.VERDIGRIS_SCRAP.get()))
                .save(output, Bygone.id("verdigris_cog"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BGItems.VERDIGRIS_INGOT.get(), 1)
                .requires(BGItems.VERDIGRIS_SCRAP.get())
                .requires(BGItems.VERDIGRIS_SCRAP.get())
                .requires(BGItems.VERDIGRIS_SCRAP.get())
                .requires(BGItems.VERDIGRIS_SCRAP.get())
                .requires(Items.COPPER_INGOT)
                .requires(Items.COPPER_INGOT)
                .requires(Items.COPPER_INGOT)
                .requires(Items.COPPER_INGOT)
                .group("verdigris_ingot")
                .unlockedBy("has_ingredient_1", has(BGItems.VERDIGRIS_SCRAP.get()))
                .save(output, Bygone.id("verdigris_ingot"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BGItems.VERDIGRIS_INGOT.get(), 9)
                .requires(BGBlocks.VERDIGRIS_BLOCK.get())
                .group("netherite_ingot")
                .unlockedBy("has_ingredient_1", has(BGBlocks.VERDIGRIS_BLOCK.get()))
                .save(output, Bygone.id("verdigris_ingot_from_verdigris_block"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, BGBlocks.WHITE_GLOW_CONCRETE_POWDER.get(), 8)
                .requires(Items.WHITE_DYE)
                .requires(BGBlocks.SHELLSAND.get())
                .requires(BGBlocks.SHELLSAND.get())
                .requires(BGBlocks.SHELLSAND.get())
                .requires(BGBlocks.SHELLSAND.get())
                .requires(BGBlocks.GLOW_GRAVEL.get())
                .requires(BGBlocks.GLOW_GRAVEL.get())
                .requires(BGBlocks.GLOW_GRAVEL.get())
                .requires(BGBlocks.GLOW_GRAVEL.get())
                .group("glow_concrete_powder")
                .unlockedBy("has_ingredient_1", has(BGBlocks.SHELLSAND.get()))
                .unlockedBy("has_ingredient_2", has(BGBlocks.GLOW_GRAVEL.get()))
                .save(output, Bygone.id("white_glow_concrete_powder"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.WRITABLE_BOOK, 1)
                .requires(Items.BOOK)
                .requires(Items.INK_SAC)
                .requires(BGItems.EXOTIC_PLUMAGE.get())
                .unlockedBy("has_ingredient_1", has(BGItems.EXOTIC_PLUMAGE.get()))
                .save(output, Bygone.id("writable_book"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.YELLOW_DYE, 1)
                .requires(BGBlocks.YELLOW_SAGARIA.get())
                .group("yellow_dye")
                .unlockedBy("has_ingredient_1", has(BGBlocks.YELLOW_SAGARIA.get()))
                .save(output, Bygone.id("yellow_dye_from_yellow_sagaria"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, BGBlocks.YELLOW_GLOW_CONCRETE_POWDER.get(), 8)
                .requires(Items.YELLOW_DYE)
                .requires(BGBlocks.SHELLSAND.get())
                .requires(BGBlocks.SHELLSAND.get())
                .requires(BGBlocks.SHELLSAND.get())
                .requires(BGBlocks.SHELLSAND.get())
                .requires(BGBlocks.GLOW_GRAVEL.get())
                .requires(BGBlocks.GLOW_GRAVEL.get())
                .requires(BGBlocks.GLOW_GRAVEL.get())
                .requires(BGBlocks.GLOW_GRAVEL.get())
                .group("glow_concrete_powder")
                .unlockedBy("has_ingredient_1", has(BGBlocks.SHELLSAND.get()))
                .unlockedBy("has_ingredient_2", has(BGBlocks.GLOW_GRAVEL.get()))
                .save(output, Bygone.id("yellow_glow_concrete_powder"));
    }

    private static void shapedRecipes(RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BGItems.AMARANTH_LOAF.get(), 1)
                .pattern("###")
                .define('#', BGItems.AMARANTH_GRAIN.get())
                .unlockedBy("has_ingredient_1", has(BGItems.AMARANTH_GRAIN.get()))
                .save(output, Bygone.id("amaranth_loaf"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.AMBER_SANDSTONE.get(), 4)
                .pattern("XX")
                .pattern("XX")
                .define('X', BGBlocks.AMBER_SAND.get())
                .unlockedBy("has_ingredient_1", has(BGBlocks.AMBER_SAND.get()))
                .save(output, Bygone.id("amber_sandstone"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.AMBER_SANDSTONE_SLAB.get(), 6)
                .pattern("XXX")
                .define('X', BGBlocks.AMBER_SANDSTONE.get())
                .unlockedBy("has_ingredient_1", has(BGBlocks.AMBER_SANDSTONE.get()))
                .save(output, Bygone.id("amber_sandstone_slab"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.AMBER_SANDSTONE_STAIRS.get(), 4)
                .pattern("X  ")
                .pattern("XX ")
                .pattern("XXX")
                .define('X', BGBlocks.AMBER_SANDSTONE.get())
                .unlockedBy("has_ingredient_1", has(BGBlocks.AMBER_SANDSTONE.get()))
                .save(output, Bygone.id("amber_sandstone_stairs"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.AMBER_SANDSTONE_WALL.get(), 6)
                .pattern("XXX")
                .pattern("XXX")
                .define('X', BGBlocks.AMBER_SANDSTONE.get())
                .unlockedBy("has_ingredient_1", has(BGBlocks.AMBER_SANDSTONE.get()))
                .save(output, Bygone.id("amber_sandstone_wall"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BGItems.AMOEBA_GEL_ON_A_STICK.get(), 1)
                .pattern("# ")
                .pattern(" X")
                .define('#', Items.FISHING_ROD)
                .define('X', BGItems.AMOEBA_GEL.get())
                .unlockedBy("has_ingredient_1", has(BGItems.AMOEBA_GEL.get()))
                .save(output, Bygone.id("amoeba_gel_on_a_stick"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BGBlocks.AMPHORA.get(), 1)
                .pattern("# #")
                .pattern("# #")
                .pattern("###")
                .define('#', BGBlocks.CLAYSTONE.get())
                .unlockedBy("has_ingredient_1", has(BGBlocks.CLAYSTONE.get()))
                .save(output, Bygone.id("amphora"));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, BGBlocks.ANCIENT_DOOR.get(), 3)
                .pattern("XX")
                .pattern("XX")
                .pattern("XX")
                .define('X', BGBlocks.ANCIENT_PLANKS.get())
                .group("wooden_door")
                .unlockedBy("has_ancient_planks", has(BGBlocks.ANCIENT_PLANKS.get()))
                .save(output, Bygone.id("ancient_door"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BGBlocks.ANCIENT_FENCE.get(), 3)
                .pattern("WXW")
                .pattern("WXW")
                .define('W', BGBlocks.ANCIENT_PLANKS.get())
                .define('X', Items.STICK)
                .group("wooden_fence")
                .unlockedBy("has_ancient_planks", has(BGBlocks.ANCIENT_PLANKS.get()))
                .save(output, Bygone.id("ancient_fence"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BGBlocks.ANCIENT_FENCE_GATE.get(), 1)
                .pattern("XWX")
                .pattern("XWX")
                .define('W', BGBlocks.ANCIENT_PLANKS.get())
                .define('X', Items.STICK)
                .group("wooden_fence_gate")
                .unlockedBy("has_ancient_planks", has(BGBlocks.ANCIENT_PLANKS.get()))
                .save(output, Bygone.id("ancient_fence_gate"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BGItems.ANCIENT_HANGING_SIGN.get(), 1)
                .pattern("X X")
                .pattern("WWW")
                .pattern("WWW")
                .define('W', BGBlocks.ANCIENT_PLANKS.get())
                .define('X', Items.CHAIN)
                .group("hanging_sign")
                .unlockedBy("has_ancient_planks", has(BGBlocks.ANCIENT_PLANKS.get()))
                .save(output, Bygone.id("ancient_hanging_sign"));

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BGItems.ANCIENT_HOOK.get(), 1)
                .pattern(" # ")
                .pattern(" I ")
                .define('#', BGItems.VERDIGRIS_INGOT.get())
                .define('I', Items.BLAZE_ROD)
                .unlockedBy("has_ingredient_1", has(BGItems.VERDIGRIS_INGOT.get()))
                .save(output, Bygone.id("ancient_hook"));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, BGBlocks.ANCIENT_PRESSURE_PLATE.get(), 1)
                .pattern("XX")
                .define('X', BGBlocks.ANCIENT_PLANKS.get())
                .group("wooden_pressure_plate")
                .unlockedBy("has_ancient_planks", has(BGBlocks.ANCIENT_PLANKS.get()))
                .save(output, Bygone.id("ancient_pressure_plate"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BGItems.ANCIENT_SIGN.get(), 3)
                .pattern("WWW")
                .pattern("WWW")
                .pattern(" X ")
                .define('W', BGBlocks.ANCIENT_PLANKS.get())
                .define('X', Items.STICK)
                .group("wooden_sign")
                .unlockedBy("has_ancient_planks", has(BGBlocks.ANCIENT_PLANKS.get()))
                .save(output, Bygone.id("ancient_sign"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.ANCIENT_SLAB.get(), 6)
                .pattern("XXX")
                .define('X', BGBlocks.ANCIENT_PLANKS.get())
                .unlockedBy("has_ancient_planks", has(BGBlocks.ANCIENT_PLANKS.get()))
                .save(output, Bygone.id("ancient_slab"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.ANCIENT_STAIRS.get(), 4)
                .pattern("X  ")
                .pattern("XX ")
                .pattern("XXX")
                .define('X', BGBlocks.ANCIENT_PLANKS.get())
                .unlockedBy("has_ancient_planks", has(BGBlocks.ANCIENT_PLANKS.get()))
                .save(output, Bygone.id("ancient_stairs"));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, BGBlocks.ANCIENT_TRAPDOOR.get(), 2)
                .pattern("XXX")
                .pattern("XXX")
                .define('X', BGBlocks.ANCIENT_PLANKS.get())
                .group("wooden_trapdoor")
                .unlockedBy("has_ancient_planks", has(BGBlocks.ANCIENT_PLANKS.get()))
                .save(output, Bygone.id("ancient_trapdoor"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.ANCIENT_WOOD.get(), 3)
                .pattern("XX")
                .pattern("XX")
                .define('X', BGBlocks.ANCIENT_LOG.get())
                .group("bark")
                .unlockedBy("has_ancient_log", has(BGBlocks.ANCIENT_LOG.get()))
                .save(output, Bygone.id("ancient_wood"));

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BGItems.ARCANE_MECHANISM.get(), 1)
                .pattern(" S ")
                .pattern("SCS")
                .pattern(" S ")
                .define('C', BGItems.ARCANE_CORE.get())
                .define('S', Items.COPPER_INGOT)
                .unlockedBy("has_ingredient_1", has(BGItems.ARCANE_CORE.get()))
                .save(output, Bygone.id("arcane_mechanism"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.CHISELED_AMBER_SANDSTONE.get(), 1)
                .pattern("#")
                .pattern("#")
                .define('#', BGBlocks.AMBER_SANDSTONE_SLAB.get())
                .unlockedBy("has_ingredient_1", has(BGBlocks.AMBER_SANDSTONE_SLAB.get()))
                .save(output, Bygone.id("chiseled_amber_sandstone"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.CHISELED_PLASMIC_BYSLATE_BRICKS.get(), 1)
                .pattern("#")
                .pattern("#")
                .define('#', BGBlocks.PLASMIC_BYSLATE_BRICKS_SLAB.get())
                .unlockedBy("has_ingredient_1", has(BGBlocks.PLASMIC_BYSLATE_BRICKS_SLAB.get()))
                .save(output, Bygone.id("chiseled_plasmic_byslate_bricks"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.CHISELED_POLISHED_BYSLATE.get(), 1)
                .pattern("#")
                .pattern("#")
                .define('#', BGBlocks.POLISHED_BYSLATE_SLAB.get())
                .unlockedBy("has_ingredient_1", has(BGBlocks.POLISHED_BYSLATE_SLAB.get()))
                .save(output, Bygone.id("chiseled_polished_byslate"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.CHISELED_POLISHED_BYSTONE.get(), 1)
                .pattern("#")
                .pattern("#")
                .define('#', BGBlocks.POLISHED_BYSTONE_SLAB.get())
                .unlockedBy("has_ingredient_1", has(BGBlocks.POLISHED_BYSTONE_SLAB.get()))
                .save(output, Bygone.id("chiseled_polished_bystone"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.CHISELED_SHELLSTONE_BRICKS.get(), 1)
                .pattern("#")
                .pattern("#")
                .define('#', BGBlocks.SHELLSTONE_BRICKS_SLAB.get())
                .unlockedBy("has_ingredient_1", has(BGBlocks.SHELLSTONE_BRICKS_SLAB.get()))
                .save(output, Bygone.id("chiseled_shellstone_bricks"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.CHISELED_TERRACLAYSTONE.get(), 1)
                .pattern("#")
                .pattern("#")
                .define('#', BGBlocks.TERRACLAYSTONE_SLAB.get())
                .unlockedBy("has_ingredient_1", has(BGBlocks.TERRACLAYSTONE_SLAB.get()))
                .save(output, Bygone.id("chiseled_terraclaystone"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.CLAYSTONE_BRICKS.get(), 4)
                .pattern("XX")
                .pattern("XX")
                .define('X', BGBlocks.CLAYSTONE.get())
                .unlockedBy("has_claystone", has(BGBlocks.CLAYSTONE.get()))
                .save(output, Bygone.id("claystone_bricks"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.CLAYSTONE_BRICKS_SLAB.get(), 6)
                .pattern("XXX")
                .define('X', BGBlocks.CLAYSTONE_BRICKS.get())
                .unlockedBy("has_claystone_bricks", has(BGBlocks.CLAYSTONE_BRICKS.get()))
                .save(output, Bygone.id("claystone_bricks_slab"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.CLAYSTONE_BRICKS_STAIRS.get(), 4)
                .pattern("X  ")
                .pattern("XX ")
                .pattern("XXX")
                .define('X', BGBlocks.CLAYSTONE_BRICKS.get())
                .unlockedBy("has_claystone_bricks", has(BGBlocks.CLAYSTONE_BRICKS.get()))
                .save(output, Bygone.id("claystone_bricks_stairs"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.CLAYSTONE_BRICKS_WALL.get(), 6)
                .pattern("XXX")
                .pattern("XXX")
                .define('X', BGBlocks.CLAYSTONE_BRICKS.get())
                .unlockedBy("has_claystone_bricks", has(BGBlocks.CLAYSTONE_BRICKS.get()))
                .save(output, Bygone.id("claystone_bricks_wall"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.COARSE_CLAYSTONE.get(), 4)
                .pattern("DG")
                .pattern("GD")
                .define('D', BGBlocks.CLAYSTONE.get())
                .define('G', Items.GRAVEL)
                .unlockedBy("has_ingredient_1", has(BGBlocks.CLAYSTONE.get()))
                .save(output, Bygone.id("coarse_claystone"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.COBBLED_BYSLATE_SLAB.get(), 6)
                .pattern("XXX")
                .define('X', BGBlocks.COBBLED_BYSLATE.get())
                .unlockedBy("has_cobbled_byslate", has(BGBlocks.COBBLED_BYSLATE.get()))
                .save(output, Bygone.id("cobbled_byslate_slab"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.COBBLED_BYSLATE_STAIRS.get(), 4)
                .pattern("X  ")
                .pattern("XX ")
                .pattern("XXX")
                .define('X', BGBlocks.COBBLED_BYSLATE.get())
                .unlockedBy("has_cobbled_byslate", has(BGBlocks.COBBLED_BYSLATE.get()))
                .save(output, Bygone.id("cobbled_byslate_stairs"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.COBBLED_BYSLATE_WALL.get(), 6)
                .pattern("XXX")
                .pattern("XXX")
                .define('X', BGBlocks.COBBLED_BYSLATE.get())
                .unlockedBy("has_cobbled_byslate", has(BGBlocks.COBBLED_BYSLATE.get()))
                .save(output, Bygone.id("cobbled_byslate_wall"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.COBBLED_BYSTONE_SLAB.get(), 6)
                .pattern("XXX")
                .define('X', BGBlocks.COBBLED_BYSTONE.get())
                .unlockedBy("has_cobbled_bystone", has(BGBlocks.COBBLED_BYSTONE.get()))
                .save(output, Bygone.id("cobbled_bystone_slab"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.COBBLED_BYSTONE_STAIRS.get(), 4)
                .pattern("X  ")
                .pattern("XX ")
                .pattern("XXX")
                .define('X', BGBlocks.COBBLED_BYSTONE.get())
                .unlockedBy("has_cobbled_bystone", has(BGBlocks.COBBLED_BYSTONE.get()))
                .save(output, Bygone.id("cobbled_bystone_stairs"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.COBBLED_BYSTONE_WALL.get(), 6)
                .pattern("XXX")
                .pattern("XXX")
                .define('X', BGBlocks.COBBLED_BYSTONE.get())
                .unlockedBy("has_cobbled_bystone", has(BGBlocks.COBBLED_BYSTONE.get()))
                .save(output, Bygone.id("cobbled_bystone_wall"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.COPPER_INGOT, 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', BGItems.COPPER_NUGGET.get())
                .group("iron_ingot")
                .unlockedBy("has_ingredient_1", has(BGItems.COPPER_NUGGET.get()))
                .save(output, Bygone.id("copper_ingot_from_nuggets"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.CUT_AMBER_SANDSTONE.get(), 4)
                .pattern("XX")
                .pattern("XX")
                .define('X', BGBlocks.AMBER_SANDSTONE.get())
                .unlockedBy("has_ingredient_1", has(BGBlocks.AMBER_SANDSTONE.get()))
                .save(output, Bygone.id("cut_amber_sandstone"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.CUT_AMBER_SANDSTONE_SLAB.get(), 6)
                .pattern("XXX")
                .define('X', BGBlocks.CUT_AMBER_SANDSTONE.get())
                .unlockedBy("has_ingredient_1", has(BGBlocks.CUT_AMBER_SANDSTONE.get()))
                .save(output, Bygone.id("cut_amber_sandstone_slab"));

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BGItems.ECHO_GONG.get(), 1)
                .pattern(" S ")
                .pattern("SCS")
                .pattern(" S ")
                .define('C', BGItems.VERDIGRIS_INGOT.get())
                .define('S', Items.ECHO_SHARD)
                .unlockedBy("has_ingredient_1", has(BGItems.VERDIGRIS_INGOT.get()))
                .save(output, Bygone.id("echo_gong"));

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BGItems.EXOTIC_ARROW.get(), 4)
                .pattern("X")
                .pattern("#")
                .pattern("Y")
                .define('#', Items.STICK)
                .define('X', Items.FLINT)
                .define('Y', BGItems.EXOTIC_PLUMAGE.get())
                .unlockedBy("has_ingredient_1", has(BGItems.EXOTIC_PLUMAGE.get()))
                .save(output, Bygone.id("exotic_arrow"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.GLOWING_PLASMIC_BYSLATE_BRICKS_SLAB.get(), 6)
                .pattern("XXX")
                .define('X', BGBlocks.GLOWING_PLASMIC_BYSLATE_BRICKS.get())
                .unlockedBy("has_ingredient_1", has(BGBlocks.GLOWING_PLASMIC_BYSLATE_BRICKS.get()))
                .save(output, Bygone.id("glowing_plasmic_byslate_bricks_slab"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.GLOWING_PLASMIC_BYSLATE_BRICKS_STAIRS.get(), 4)
                .pattern("X  ")
                .pattern("XX ")
                .pattern("XXX")
                .define('X', BGBlocks.GLOWING_PLASMIC_BYSLATE_BRICKS.get())
                .unlockedBy("has_ingredient_1", has(BGBlocks.GLOWING_PLASMIC_BYSLATE_BRICKS.get()))
                .save(output, Bygone.id("glowing_plasmic_byslate_bricks_stairs"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.GLOWING_PLASMIC_BYSLATE_BRICKS_WALL.get(), 6)
                .pattern("XXX")
                .pattern("XXX")
                .define('X', BGBlocks.GLOWING_PLASMIC_BYSLATE_BRICKS.get())
                .unlockedBy("has_ingredient_1", has(BGBlocks.GLOWING_PLASMIC_BYSLATE_BRICKS.get()))
                .save(output, Bygone.id("glowing_plasmic_byslate_bricks_wall"));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, BGBlocks.LITHINE_LAMP.get(), 1)
                .pattern(" R ")
                .pattern("RGR")
                .pattern(" R ")
                .define('G', Items.GLOWSTONE)
                .define('R', BGItems.LITHOPLASM.get())
                .unlockedBy("has_ingredient_1", has(BGItems.LITHOPLASM.get()))
                .save(output, Bygone.id("lithine_lamp"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.MALACHITE_CHISELED.get(), 1)
                .pattern("X")
                .pattern("X")
                .define('X', BGBlocks.MALACHITE_SLAB.get())
                .unlockedBy("has_malachite", has(BGBlocks.MALACHITE.get()))
                .save(output, Bygone.id("malachite_chiseled"));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, BGBlocks.MALACHITE_DOOR.get(), 3)
                .pattern("XX")
                .pattern("XX")
                .pattern("XX")
                .define('X', BGBlocks.MALACHITE.get())
                .group("wooden_door")
                .unlockedBy("has_ingredient_1", has(BGBlocks.MALACHITE.get()))
                .save(output, Bygone.id("malachite_door"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.MALACHITE_PILLAR.get(), 2)
                .pattern("X")
                .pattern("X")
                .define('X', BGBlocks.MALACHITE.get())
                .unlockedBy("has_ingredient_1", has(BGBlocks.MALACHITE.get()))
                .save(output, Bygone.id("malachite_pillar"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.MALACHITE_SLAB.get(), 6)
                .pattern("XXX")
                .define('X', BGBlocks.MALACHITE.get())
                .unlockedBy("has_malachite", has(BGBlocks.MALACHITE.get()))
                .save(output, Bygone.id("malachite_slab"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.MALACHITE_STAIRS.get(), 4)
                .pattern("X  ")
                .pattern("XX ")
                .pattern("XXX")
                .define('X', BGBlocks.MALACHITE.get())
                .unlockedBy("has_malachite", has(BGBlocks.MALACHITE.get()))
                .save(output, Bygone.id("malachite_stairs"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.MALACHITE_TILE.get(), 4)
                .pattern("XX")
                .pattern("XX")
                .define('X', BGBlocks.MALACHITE.get())
                .group("bark")
                .unlockedBy("has_malachite", has(BGBlocks.MALACHITE.get()))
                .save(output, Bygone.id("malachite_tile"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.MALACHITE_TILE_SLAB.get(), 6)
                .pattern("XXX")
                .define('X', BGBlocks.MALACHITE_TILE.get())
                .unlockedBy("has_malachite_tile", has(BGBlocks.MALACHITE_TILE.get()))
                .save(output, Bygone.id("malachite_tile_slab"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.MALACHITE_TILE_STAIRS.get(), 4)
                .pattern("X  ")
                .pattern("XX ")
                .pattern("XXX")
                .define('X', BGBlocks.MALACHITE_TILE.get())
                .unlockedBy("has_malachite_tile", has(BGBlocks.MALACHITE_TILE.get()))
                .save(output, Bygone.id("malachite_tile_stairs"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.MALACHITE_TILE_WALL.get(), 6)
                .pattern("XXX")
                .pattern("XXX")
                .define('X', BGBlocks.MALACHITE_TILE.get())
                .unlockedBy("has_malachite_tile", has(BGBlocks.MALACHITE_TILE.get()))
                .save(output, Bygone.id("malachite_tile_wall"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.MALACHITE_WALL.get(), 6)
                .pattern("XXX")
                .pattern("XXX")
                .define('X', BGBlocks.MALACHITE.get())
                .unlockedBy("has_malachite", has(BGBlocks.MALACHITE.get()))
                .save(output, Bygone.id("malachite_wall"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BGBlocks.MEGALITH_TOTEM.get(), 1)
                .pattern(" # ")
                .pattern("#X#")
                .pattern(" # ")
                .define('#', BGBlocks.MEGALITH_BLOCK.get())
                .define('X', BGItems.LITHOPLASM.get())
                .unlockedBy("has_ingredient_1", has(BGBlocks.MEGALITH_BLOCK.get()))
                .unlockedBy("has_ingredient_2", has(BGItems.LITHOPLASM.get()))
                .save(output, Bygone.id("megalith_totem"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.ORANGE_FUNGAL_BRICKS.get(), 4)
                .pattern("XX")
                .pattern("XX")
                .define('X', BGBlocks.ORANGE_MUSHROOM_BLOCK.get())
                .unlockedBy("has_orange_mushroom_block", has(BGBlocks.ORANGE_MUSHROOM_BLOCK.get()))
                .save(output, Bygone.id("orange_fungal_bricks"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.ORANGE_FUNGAL_SLAB.get(), 6)
                .pattern("XXX")
                .define('X', BGBlocks.ORANGE_FUNGAL_BRICKS.get())
                .unlockedBy("has_orange_fungal_bricks", has(BGBlocks.ORANGE_FUNGAL_BRICKS.get()))
                .save(output, Bygone.id("orange_fungal_slab"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.ORANGE_FUNGAL_STAIRS.get(), 4)
                .pattern("X  ")
                .pattern("XX ")
                .pattern("XXX")
                .define('X', BGBlocks.ORANGE_FUNGAL_BRICKS.get())
                .unlockedBy("has_orange_fungal_bricks", has(BGBlocks.ORANGE_FUNGAL_BRICKS.get()))
                .save(output, Bygone.id("orange_fungal_stairs"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.ORANGE_FUNGAL_WALL.get(), 6)
                .pattern("XXX")
                .pattern("XXX")
                .define('X', BGBlocks.ORANGE_FUNGAL_BRICKS.get())
                .unlockedBy("has_orange_fungal_bricks", has(BGBlocks.ORANGE_FUNGAL_BRICKS.get()))
                .save(output, Bygone.id("orange_fungal_wall"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.PINK_FUNGAL_BRICKS.get(), 4)
                .pattern("XX")
                .pattern("XX")
                .define('X', BGBlocks.PINK_MUSHROOM_BLOCK.get())
                .unlockedBy("has_pink_mushroom_block", has(BGBlocks.PINK_MUSHROOM_BLOCK.get()))
                .save(output, Bygone.id("pink_fungal_bricks"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.PINK_FUNGAL_SLAB.get(), 6)
                .pattern("XXX")
                .define('X', BGBlocks.PINK_FUNGAL_BRICKS.get())
                .unlockedBy("has_pink_fungal_bricks", has(BGBlocks.PINK_FUNGAL_BRICKS.get()))
                .save(output, Bygone.id("pink_fungal_slab"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.PINK_FUNGAL_STAIRS.get(), 4)
                .pattern("X  ")
                .pattern("XX ")
                .pattern("XXX")
                .define('X', BGBlocks.PINK_FUNGAL_BRICKS.get())
                .unlockedBy("has_pink_fungal_bricks", has(BGBlocks.PINK_FUNGAL_BRICKS.get()))
                .save(output, Bygone.id("pink_fungal_stairs"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.PINK_FUNGAL_WALL.get(), 6)
                .pattern("XXX")
                .pattern("XXX")
                .define('X', BGBlocks.PINK_FUNGAL_BRICKS.get())
                .unlockedBy("has_pink_fungal_bricks", has(BGBlocks.PINK_FUNGAL_BRICKS.get()))
                .save(output, Bygone.id("pink_fungal_wall"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.PLASMIC_BYSLATE_BRICKS.get(), 4)
                .pattern("XX")
                .pattern("XX")
                .define('X', BGBlocks.PLASMIC_BYSLATE.get())
                .unlockedBy("has_ingredient_1", has(BGBlocks.PLASMIC_BYSLATE.get()))
                .save(output, Bygone.id("plasmic_byslate_bricks"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.PLASMIC_BYSLATE_BRICKS_SLAB.get(), 6)
                .pattern("XXX")
                .define('X', BGBlocks.PLASMIC_BYSLATE_BRICKS.get())
                .unlockedBy("has_ingredient_1", has(BGBlocks.PLASMIC_BYSLATE_BRICKS.get()))
                .save(output, Bygone.id("plasmic_byslate_bricks_slab"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.PLASMIC_BYSLATE_BRICKS_STAIRS.get(), 4)
                .pattern("X  ")
                .pattern("XX ")
                .pattern("XXX")
                .define('X', BGBlocks.PLASMIC_BYSLATE_BRICKS.get())
                .unlockedBy("has_ingredient_1", has(BGBlocks.PLASMIC_BYSLATE_BRICKS.get()))
                .save(output, Bygone.id("plasmic_byslate_bricks_stairs"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.PLASMIC_BYSLATE_BRICKS_WALL.get(), 6)
                .pattern("XXX")
                .pattern("XXX")
                .define('X', BGBlocks.PLASMIC_BYSLATE_BRICKS.get())
                .unlockedBy("has_ingredient_1", has(BGBlocks.PLASMIC_BYSLATE_BRICKS.get()))
                .save(output, Bygone.id("plasmic_byslate_bricks_wall"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.PLASMIC_BYSLATE_SLAB.get(), 6)
                .pattern("XXX")
                .define('X', BGBlocks.PLASMIC_BYSLATE.get())
                .unlockedBy("has_ingredient_1", has(BGBlocks.PLASMIC_BYSLATE.get()))
                .save(output, Bygone.id("plasmic_byslate_slab"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.PLASMIC_BYSLATE_STAIRS.get(), 4)
                .pattern("X  ")
                .pattern("XX ")
                .pattern("XXX")
                .define('X', BGBlocks.PLASMIC_BYSLATE.get())
                .unlockedBy("has_ingredient_1", has(BGBlocks.PLASMIC_BYSLATE.get()))
                .save(output, Bygone.id("plasmic_byslate_stairs"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.PLASMIC_BYSLATE_WALL.get(), 6)
                .pattern("XXX")
                .pattern("XXX")
                .define('X', BGBlocks.PLASMIC_BYSLATE.get())
                .unlockedBy("has_ingredient_1", has(BGBlocks.PLASMIC_BYSLATE.get()))
                .save(output, Bygone.id("plasmic_byslate_wall"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.POLISHED_BYSLATE.get(), 4)
                .pattern("XX")
                .pattern("XX")
                .define('X', BGBlocks.BYSLATE.get())
                .unlockedBy("has_ingredient_1", has(BGBlocks.BYSLATE.get()))
                .save(output, Bygone.id("polished_byslate"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.POLISHED_BYSLATE_BRICK.get(), 4)
                .pattern("XX")
                .pattern("XX")
                .define('X', BGBlocks.POLISHED_BYSLATE.get())
                .unlockedBy("has_ingredient_1", has(BGBlocks.POLISHED_BYSLATE.get()))
                .save(output, Bygone.id("polished_byslate_bricks"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.POLISHED_BYSLATE_BRICK_SLAB.get(), 6)
                .pattern("XXX")
                .define('X', BGBlocks.POLISHED_BYSLATE_BRICK.get())
                .unlockedBy("has_ingredient_1", has(BGBlocks.POLISHED_BYSLATE_BRICK.get()))
                .save(output, Bygone.id("polished_byslate_bricks_slab"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.POLISHED_BYSLATE_BRICK_STAIRS.get(), 4)
                .pattern("X  ")
                .pattern("XX ")
                .pattern("XXX")
                .define('X', BGBlocks.POLISHED_BYSLATE_BRICK.get())
                .unlockedBy("has_ingredient_1", has(BGBlocks.POLISHED_BYSLATE_BRICK.get()))
                .save(output, Bygone.id("polished_byslate_bricks_stairs"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.POLISHED_BYSLATE_BRICK_WALL.get(), 6)
                .pattern("XXX")
                .pattern("XXX")
                .define('X', BGBlocks.POLISHED_BYSLATE_BRICK.get())
                .unlockedBy("has_ingredient_1", has(BGBlocks.POLISHED_BYSLATE_BRICK.get()))
                .save(output, Bygone.id("polished_byslate_bricks_wall"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.POLISHED_BYSLATE_SHINGLES.get(), 4)
                .pattern("XX")
                .pattern("XX")
                .define('X', BGBlocks.POLISHED_BYSLATE_BRICK.get())
                .unlockedBy("has_ingredient_1", has(BGBlocks.POLISHED_BYSLATE_BRICK.get()))
                .save(output, Bygone.id("polished_byslate_shingles"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.POLISHED_BYSLATE_SLAB.get(), 6)
                .pattern("XXX")
                .define('X', BGBlocks.POLISHED_BYSLATE.get())
                .unlockedBy("has_ingredient_1", has(BGBlocks.POLISHED_BYSLATE.get()))
                .save(output, Bygone.id("polished_byslate_slab"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.POLISHED_BYSLATE_STAIRS.get(), 4)
                .pattern("X  ")
                .pattern("XX ")
                .pattern("XXX")
                .define('X', BGBlocks.POLISHED_BYSLATE.get())
                .unlockedBy("has_ingredient_1", has(BGBlocks.POLISHED_BYSLATE.get()))
                .save(output, Bygone.id("polished_byslate_stairs"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.POLISHED_BYSLATE_WALL.get(), 6)
                .pattern("XXX")
                .pattern("XXX")
                .define('X', BGBlocks.POLISHED_BYSLATE.get())
                .unlockedBy("has_ingredient_1", has(BGBlocks.POLISHED_BYSLATE.get()))
                .save(output, Bygone.id("polished_byslate_wall"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.POLISHED_BYSTONE.get(), 4)
                .pattern("XX")
                .pattern("XX")
                .define('X', BGBlocks.BYSTONE.get())
                .unlockedBy("has_ingredient_1", has(BGBlocks.BYSTONE.get()))
                .save(output, Bygone.id("polished_bystone"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.POLISHED_BYSTONE_BRICK.get(), 4)
                .pattern("XX")
                .pattern("XX")
                .define('X', BGBlocks.POLISHED_BYSTONE.get())
                .unlockedBy("has_ingredient_1", has(BGBlocks.POLISHED_BYSTONE.get()))
                .save(output, Bygone.id("polished_bystone_bricks"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.POLISHED_BYSTONE_BRICK_SLAB.get(), 6)
                .pattern("XXX")
                .define('X', BGBlocks.POLISHED_BYSTONE_BRICK.get())
                .unlockedBy("has_ingredient_1", has(BGBlocks.POLISHED_BYSTONE_BRICK.get()))
                .save(output, Bygone.id("polished_bystone_bricks_slab"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.POLISHED_BYSTONE_BRICK_STAIRS.get(), 4)
                .pattern("X  ")
                .pattern("XX ")
                .pattern("XXX")
                .define('X', BGBlocks.POLISHED_BYSTONE_BRICK.get())
                .unlockedBy("has_ingredient_1", has(BGBlocks.POLISHED_BYSTONE_BRICK.get()))
                .save(output, Bygone.id("polished_bystone_bricks_stairs"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.POLISHED_BYSTONE_BRICK_WALL.get(), 6)
                .pattern("XXX")
                .pattern("XXX")
                .define('X', BGBlocks.POLISHED_BYSTONE_BRICK.get())
                .unlockedBy("has_ingredient_1", has(BGBlocks.POLISHED_BYSTONE_BRICK.get()))
                .save(output, Bygone.id("polished_bystone_bricks_wall"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.POLISHED_BYSTONE_SHINGLES.get(), 4)
                .pattern("XX")
                .pattern("XX")
                .define('X', BGBlocks.POLISHED_BYSTONE_BRICK.get())
                .unlockedBy("has_ingredient_1", has(BGBlocks.POLISHED_BYSTONE_BRICK.get()))
                .save(output, Bygone.id("polished_bystone_shingles"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.POLISHED_BYSTONE_SLAB.get(), 6)
                .pattern("XXX")
                .define('X', BGBlocks.POLISHED_BYSTONE.get())
                .unlockedBy("has_ingredient_1", has(BGBlocks.POLISHED_BYSTONE.get()))
                .save(output, Bygone.id("polished_bystone_slab"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.POLISHED_BYSTONE_STAIRS.get(), 4)
                .pattern("X  ")
                .pattern("XX ")
                .pattern("XXX")
                .define('X', BGBlocks.POLISHED_BYSTONE.get())
                .unlockedBy("has_ingredient_1", has(BGBlocks.POLISHED_BYSTONE.get()))
                .save(output, Bygone.id("polished_bystone_stairs"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.POLISHED_BYSTONE_WALL.get(), 6)
                .pattern("XXX")
                .pattern("XXX")
                .define('X', BGBlocks.POLISHED_BYSTONE.get())
                .unlockedBy("has_ingredient_1", has(BGBlocks.POLISHED_BYSTONE.get()))
                .save(output, Bygone.id("polished_bystone_wall"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.PURPLE_FUNGAL_BRICKS.get(), 4)
                .pattern("XX")
                .pattern("XX")
                .define('X', BGBlocks.PURPLE_MUSHROOM_BLOCK.get())
                .unlockedBy("has_purple_mushroom_block", has(BGBlocks.PURPLE_MUSHROOM_BLOCK.get()))
                .save(output, Bygone.id("purple_fungal_bricks"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.PURPLE_FUNGAL_SLAB.get(), 6)
                .pattern("XXX")
                .define('X', BGBlocks.PURPLE_FUNGAL_BRICKS.get())
                .unlockedBy("has_purple_fungal_bricks", has(BGBlocks.PURPLE_FUNGAL_BRICKS.get()))
                .save(output, Bygone.id("purple_fungal_slab"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.PURPLE_FUNGAL_STAIRS.get(), 4)
                .pattern("X  ")
                .pattern("XX ")
                .pattern("XXX")
                .define('X', BGBlocks.PURPLE_FUNGAL_BRICKS.get())
                .unlockedBy("has_purple_fungal_bricks", has(BGBlocks.PURPLE_FUNGAL_BRICKS.get()))
                .save(output, Bygone.id("purple_fungal_stairs"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.PURPLE_FUNGAL_WALL.get(), 6)
                .pattern("XXX")
                .pattern("XXX")
                .define('X', BGBlocks.PURPLE_FUNGAL_BRICKS.get())
                .unlockedBy("has_purple_fungal_bricks", has(BGBlocks.PURPLE_FUNGAL_BRICKS.get()))
                .save(output, Bygone.id("purple_fungal_wall"));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, BGBlocks.SABLE_DOOR.get(), 3)
                .pattern("XX")
                .pattern("XX")
                .pattern("XX")
                .define('X', BGBlocks.SABLE_PLANKS.get())
                .group("wooden_door")
                .unlockedBy("has_ingredient_1", has(BGBlocks.SABLE_PLANKS.get()))
                .save(output, Bygone.id("sable_door"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BGBlocks.SABLE_FENCE.get(), 3)
                .pattern("WXW")
                .pattern("WXW")
                .define('W', BGBlocks.SABLE_PLANKS.get())
                .define('X', Items.STICK)
                .group("wooden_fence")
                .unlockedBy("has_ingredient_1", has(BGBlocks.SABLE_PLANKS.get()))
                .save(output, Bygone.id("sable_fence"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BGBlocks.SABLE_FENCE_GATE.get(), 1)
                .pattern("XWX")
                .pattern("XWX")
                .define('W', BGBlocks.SABLE_PLANKS.get())
                .define('X', Items.STICK)
                .group("wooden_fence_gate")
                .unlockedBy("has_ingredient_1", has(BGBlocks.SABLE_PLANKS.get()))
                .save(output, Bygone.id("sable_fence_gate"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BGItems.SABLE_HANGING_SIGN.get(), 1)
                .pattern("X X")
                .pattern("WWW")
                .pattern("WWW")
                .define('W', BGBlocks.SABLE_PLANKS.get())
                .define('X', Items.CHAIN)
                .group("hanging_sign")
                .unlockedBy("has_ingredient_1", has(BGBlocks.SABLE_PLANKS.get()))
                .save(output, Bygone.id("sable_hanging_sign"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BGBlocks.SABLE_MOSS_CARPET.get(), 3)
                .pattern("##")
                .define('#', BGBlocks.SABLE_MOSS_BLOCK.get())
                .group("carpet")
                .unlockedBy("has_ingredient_1", has(BGBlocks.SABLE_MOSS_BLOCK.get()))
                .save(output, Bygone.id("sable_moss_carpet"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.SABLE_MOSSY_BYSLATE_SLAB.get(), 6)
                .pattern("###")
                .define('#', BGBlocks.SABLE_MOSSY_BYSLATE.get())
                .unlockedBy("has_ingredient_1", has(BGBlocks.SABLE_MOSSY_BYSLATE.get()))
                .save(output, Bygone.id("sable_mossy_byslate_slab"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.SABLE_MOSSY_BYSLATE_STAIRS.get(), 4)
                .pattern("#  ")
                .pattern("## ")
                .pattern("###")
                .define('#', BGBlocks.SABLE_MOSSY_BYSLATE.get())
                .unlockedBy("has_ingredient_1", has(BGBlocks.SABLE_MOSSY_BYSLATE.get()))
                .save(output, Bygone.id("sable_mossy_byslate_stairs"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BGBlocks.SABLE_MOSSY_BYSLATE_WALL.get(), 6)
                .pattern("###")
                .pattern("###")
                .define('#', BGBlocks.SABLE_MOSSY_BYSLATE.get())
                .unlockedBy("has_ingredient_1", has(BGBlocks.SABLE_MOSSY_BYSLATE.get()))
                .save(output, Bygone.id("sable_mossy_byslate_wall"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BGBlocks.SABLENUT_BLOCK.get(), 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', BGBlocks.SABLENUT.get())
                .unlockedBy("has_ingredient_1", has(BGBlocks.SABLENUT.get()))
                .save(output, Bygone.id("sable_nut_block"));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, BGBlocks.SABLE_PRESSURE_PLATE.get(), 1)
                .pattern("XX")
                .define('X', BGBlocks.SABLE_PLANKS.get())
                .group("wooden_pressure_plate")
                .unlockedBy("has_ingredient_1", has(BGBlocks.SABLE_PLANKS.get()))
                .save(output, Bygone.id("sable_pressure_plate"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BGItems.SABLE_SIGN.get(), 3)
                .pattern("WWW")
                .pattern("WWW")
                .pattern(" X ")
                .define('W', BGBlocks.SABLE_PLANKS.get())
                .define('X', Items.STICK)
                .group("wooden_sign")
                .unlockedBy("has_ingredient_1", has(BGBlocks.SABLE_PLANKS.get()))
                .save(output, Bygone.id("sable_sign"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.SABLE_SLAB.get(), 6)
                .pattern("XXX")
                .define('X', BGBlocks.SABLE_PLANKS.get())
                .unlockedBy("has_sable_planks", has(BGBlocks.SABLE_PLANKS.get()))
                .save(output, Bygone.id("sable_slab"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.SABLE_STAIRS.get(), 4)
                .pattern("X  ")
                .pattern("XX ")
                .pattern("XXX")
                .define('X', BGBlocks.SABLE_PLANKS.get())
                .unlockedBy("has_sable_planks", has(BGBlocks.SABLE_PLANKS.get()))
                .save(output, Bygone.id("sable_stairs"));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, BGBlocks.SABLE_TRAPDOOR.get(), 2)
                .pattern("XXX")
                .pattern("XXX")
                .define('X', BGBlocks.SABLE_PLANKS.get())
                .group("wooden_trapdoor")
                .unlockedBy("has_ingredient_1", has(BGBlocks.SABLE_PLANKS.get()))
                .save(output, Bygone.id("sable_trapdoor"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.SABLE_WOOD.get(), 3)
                .pattern("XX")
                .pattern("XX")
                .define('X', BGBlocks.SABLE_LOG.get())
                .group("bark")
                .unlockedBy("has_sable_log", has(BGBlocks.SABLE_LOG.get()))
                .save(output, Bygone.id("sable_wood"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BGItems.SCALE_BOOTS.get(), 1)
                .pattern("   ")
                .pattern("W W")
                .pattern("W W")
                .define('W', BGItems.SCALE.get())
                .unlockedBy("has_scale", has(BGItems.SCALE.get()))
                .save(output, Bygone.id("scale_boots"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BGItems.SCALE_CHESTPLATE.get(), 1)
                .pattern("W W")
                .pattern("WWW")
                .pattern("WWW")
                .define('W', BGItems.SCALE.get())
                .unlockedBy("has_scale", has(BGItems.SCALE.get()))
                .save(output, Bygone.id("scale_chestplate"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BGItems.SCALE_HELMET.get(), 1)
                .pattern("WWW")
                .pattern("W W")
                .pattern("   ")
                .define('W', BGItems.SCALE.get())
                .unlockedBy("has_scale", has(BGItems.SCALE.get()))
                .save(output, Bygone.id("scale_helmet"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BGItems.SCALE_LEGGINGS.get(), 1)
                .pattern("WWW")
                .pattern("W W")
                .pattern("W W")
                .define('W', BGItems.SCALE.get())
                .unlockedBy("has_scale", has(BGItems.SCALE.get()))
                .save(output, Bygone.id("scale_leggings"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BGBlocks.SEAGLASS_PANE.get(), 16)
                .pattern("###")
                .pattern("###")
                .define('#', BGBlocks.SEAGLASS_BLOCK.get())
                .unlockedBy("has_ingredient_1", has(BGBlocks.SEAGLASS_BLOCK.get()))
                .save(output, Bygone.id("seaglass_pane"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.SHELLSTONE.get(), 4)
                .pattern("XX")
                .pattern("XX")
                .define('X', BGBlocks.SHELLSAND.get())
                .unlockedBy("has_ingredient_1", has(BGBlocks.SHELLSAND.get()))
                .save(output, Bygone.id("shellstone"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.SHELLSTONE_BRICKS.get(), 4)
                .pattern("XX")
                .pattern("XX")
                .define('X', BGBlocks.SHELLSTONE.get())
                .unlockedBy("has_ingredient_1", has(BGBlocks.SHELLSTONE.get()))
                .save(output, Bygone.id("shellstone_bricks"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.SHELLSTONE_BRICKS_SLAB.get(), 6)
                .pattern("XXX")
                .define('X', BGBlocks.SHELLSTONE_BRICKS.get())
                .unlockedBy("has_ingredient_1", has(BGBlocks.SHELLSTONE_BRICKS.get()))
                .save(output, Bygone.id("shellstone_bricks_slab"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.SHELLSTONE_BRICKS_STAIRS.get(), 4)
                .pattern("X  ")
                .pattern("XX ")
                .pattern("XXX")
                .define('X', BGBlocks.SHELLSTONE_BRICKS.get())
                .unlockedBy("has_ingredient_1", has(BGBlocks.SHELLSTONE_BRICKS.get()))
                .save(output, Bygone.id("shellstone_bricks_stairs"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.SHELLSTONE_BRICKS_WALL.get(), 6)
                .pattern("XXX")
                .pattern("XXX")
                .define('X', BGBlocks.SHELLSTONE_BRICKS.get())
                .unlockedBy("has_ingredient_1", has(BGBlocks.SHELLSTONE_BRICKS.get()))
                .save(output, Bygone.id("shellstone_bricks_wall"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.SHELLSTONE_PILLAR.get(), 2)
                .pattern("#")
                .pattern("#")
                .define('#', BGBlocks.SHELLSTONE.get())
                .unlockedBy("has_ingredient_1", has(BGBlocks.SHELLSTONE.get()))
                .save(output, Bygone.id("shellstone_pillar"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.SHELLSTONE_PILLAR.get(), 2)
                .pattern("#")
                .pattern("#")
                .define('#', BGBlocks.SHELLSTONE_BRICKS.get())
                .unlockedBy("has_ingredient_1", has(BGBlocks.SHELLSTONE_BRICKS.get()))
                .save(output, Bygone.id("shellstone_pillar_from_shellstone_bricks"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.SHELLSTONE_SLAB.get(), 6)
                .pattern("XXX")
                .define('X', BGBlocks.SHELLSTONE.get())
                .unlockedBy("has_ingredient_1", has(BGBlocks.SHELLSTONE.get()))
                .save(output, Bygone.id("shellstone_slab"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.SHELLSTONE_STAIRS.get(), 4)
                .pattern("X  ")
                .pattern("XX ")
                .pattern("XXX")
                .define('X', BGBlocks.SHELLSTONE.get())
                .unlockedBy("has_ingredient_1", has(BGBlocks.SHELLSTONE.get()))
                .save(output, Bygone.id("shellstone_stairs"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.SHELLSTONE_WALL.get(), 6)
                .pattern("XXX")
                .pattern("XXX")
                .define('X', BGBlocks.SHELLSTONE.get())
                .unlockedBy("has_ingredient_1", has(BGBlocks.SHELLSTONE.get()))
                .save(output, Bygone.id("shellstone_wall"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.SMOOTH_AMBER_SANDSTONE_SLAB.get(), 6)
                .pattern("XXX")
                .define('X', BGBlocks.SMOOTH_AMBER_SANDSTONE.get())
                .unlockedBy("has_ingredient_1", has(BGBlocks.SMOOTH_AMBER_SANDSTONE.get()))
                .save(output, Bygone.id("smooth_amber_sandstone_slab"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.SMOOTH_AMBER_SANDSTONE_STAIRS.get(), 4)
                .pattern("X  ")
                .pattern("XX ")
                .pattern("XXX")
                .define('X', BGBlocks.SMOOTH_AMBER_SANDSTONE.get())
                .unlockedBy("has_ingredient_1", has(BGBlocks.SMOOTH_AMBER_SANDSTONE.get()))
                .save(output, Bygone.id("smooth_amber_sandstone_stairs"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.STRIPPED_ANCIENT_WOOD.get(), 3)
                .pattern("XX")
                .pattern("XX")
                .define('X', BGBlocks.STRIPPED_ANCIENT_LOG.get())
                .group("bark")
                .unlockedBy("has_stripped_ancient_log", has(BGBlocks.STRIPPED_ANCIENT_LOG.get()))
                .save(output, Bygone.id("stripped_ancient_wood"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.TERRACLAYSTONE.get(), 4)
                .pattern("DG")
                .pattern("GD")
                .define('D', BGBlocks.CLAYSTONE.get())
                .define('G', tag("minecraft:terracotta"))
                .unlockedBy("has_ingredient_1", has(BGBlocks.CLAYSTONE.get()))
                .save(output, Bygone.id("terraclaystone"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.TERRACLAYSTONE_BRICKS.get(), 4)
                .pattern("XX")
                .pattern("XX")
                .define('X', BGBlocks.TERRACLAYSTONE.get())
                .unlockedBy("has_ingredient_1", has(BGBlocks.TERRACLAYSTONE.get()))
                .save(output, Bygone.id("terraclaystone_bricks"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.TERRACLAYSTONE_BRICKS_SLAB.get(), 6)
                .pattern("XXX")
                .define('X', BGBlocks.TERRACLAYSTONE_BRICKS.get())
                .unlockedBy("has_ingredient_1", has(BGBlocks.TERRACLAYSTONE_BRICKS.get()))
                .save(output, Bygone.id("terraclaystone_bricks_slab"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.TERRACLAYSTONE_BRICKS_STAIRS.get(), 4)
                .pattern("X  ")
                .pattern("XX ")
                .pattern("XXX")
                .define('X', BGBlocks.TERRACLAYSTONE_BRICKS.get())
                .unlockedBy("has_ingredient_1", has(BGBlocks.TERRACLAYSTONE_BRICKS.get()))
                .save(output, Bygone.id("terraclaystone_bricks_stairs"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.TERRACLAYSTONE_BRICKS_WALL.get(), 6)
                .pattern("XXX")
                .pattern("XXX")
                .define('X', BGBlocks.TERRACLAYSTONE_BRICKS.get())
                .unlockedBy("has_ingredient_1", has(BGBlocks.TERRACLAYSTONE_BRICKS.get()))
                .save(output, Bygone.id("terraclaystone_bricks_wall"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.TERRACLAYSTONE_SLAB.get(), 6)
                .pattern("XXX")
                .define('X', BGBlocks.TERRACLAYSTONE.get())
                .unlockedBy("has_ingredient_1", has(BGBlocks.TERRACLAYSTONE.get()))
                .save(output, Bygone.id("terraclaystone_slab"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.TERRACLAYSTONE_STAIRS.get(), 4)
                .pattern("X  ")
                .pattern("XX ")
                .pattern("XXX")
                .define('X', BGBlocks.TERRACLAYSTONE.get())
                .unlockedBy("has_ingredient_1", has(BGBlocks.TERRACLAYSTONE.get()))
                .save(output, Bygone.id("terraclaystone_stairs"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.TERRACLAYSTONE_WALL.get(), 6)
                .pattern("XXX")
                .pattern("XXX")
                .define('X', BGBlocks.TERRACLAYSTONE.get())
                .unlockedBy("has_ingredient_1", has(BGBlocks.TERRACLAYSTONE.get()))
                .save(output, Bygone.id("terraclaystone_wall"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BGBlocks.VERDIGRIS_BLOCK.get(), 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', BGItems.VERDIGRIS_INGOT.get())
                .unlockedBy("has_ingredient_1", has(BGItems.VERDIGRIS_INGOT.get()))
                .save(output, Bygone.id("verdigris_block"));

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BGItems.WHIRLIWEED_BUNDLE.get(), 4)
                .pattern("X")
                .pattern("#")
                .pattern("I")
                .define('#', Items.STRING)
                .define('I', BGItems.VERDIGRIS_SCRAP.get())
                .define('X', tag("bygone:whirliweeds"))
                .unlockedBy("has_ingredient_1", has(BGItems.VERDIGRIS_SCRAP.get()))
                .unlockedBy("has_ingredient_2", has(tag("bygone:whirliweeds")))
                .save(output, Bygone.id("whirliweed_bundle"));
    }

    private static void stonecuttingRecipes(RecipeOutput output) {
        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BGBlocks.AMBER_SANDSTONE.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.AMBER_SANDSTONE_SLAB.get(), 2)
                .unlockedBy("has_ingredient_1", has(BGBlocks.AMBER_SANDSTONE.get()))
                .save(output, Bygone.id("amber_sandstone_from_amber_sandstone_slab_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BGBlocks.AMBER_SANDSTONE.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.AMBER_SANDSTONE_STAIRS.get(), 1)
                .unlockedBy("has_ingredient_1", has(BGBlocks.AMBER_SANDSTONE.get()))
                .save(output, Bygone.id("amber_sandstone_from_amber_sandstone_stairs_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BGBlocks.AMBER_SANDSTONE.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.AMBER_SANDSTONE_WALL.get(), 1)
                .unlockedBy("has_ingredient_1", has(BGBlocks.AMBER_SANDSTONE.get()))
                .save(output, Bygone.id("amber_sandstone_from_amber_sandstone_wall_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BGBlocks.BYSLATE.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.POLISHED_BYSLATE.get(), 1)
                .unlockedBy("has_ingredient_1", has(BGBlocks.BYSLATE.get()))
                .save(output, Bygone.id("byslate_from_polished_byslate_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BGBlocks.BYSTONE.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.POLISHED_BYSTONE.get(), 1)
                .unlockedBy("has_ingredient_1", has(BGBlocks.BYSTONE.get()))
                .save(output, Bygone.id("bystone_from_polished_bystone_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BGBlocks.AMBER_SANDSTONE.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.CHISELED_AMBER_SANDSTONE.get(), 1)
                .unlockedBy("has_ingredient_1", has(BGBlocks.AMBER_SANDSTONE.get()))
                .save(output, Bygone.id("chiseled_amber_sandstone_from_amber_sandstone_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BGBlocks.PLASMIC_BYSLATE_BRICKS.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.CHISELED_PLASMIC_BYSLATE_BRICKS.get(), 1)
                .unlockedBy("has_ingredient_1", has(BGBlocks.PLASMIC_BYSLATE_BRICKS.get()))
                .save(output, Bygone.id("chiseled_plasmic_byslate_bricks_from_plasmic_byslate_bricks_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BGBlocks.POLISHED_BYSLATE.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.CHISELED_POLISHED_BYSLATE.get(), 1)
                .unlockedBy("has_ingredient_1", has(BGBlocks.POLISHED_BYSLATE.get()))
                .save(output, Bygone.id("chiseled_polished_byslate_from_polished_byslate_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BGBlocks.POLISHED_BYSTONE.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.CHISELED_POLISHED_BYSTONE.get(), 1)
                .unlockedBy("has_ingredient_1", has(BGBlocks.POLISHED_BYSTONE.get()))
                .save(output, Bygone.id("chiseled_polished_bystone_from_polished_bystone_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BGBlocks.SHELLSTONE_BRICKS.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.CHISELED_SHELLSTONE_BRICKS.get(), 1)
                .unlockedBy("has_ingredient_1", has(BGBlocks.SHELLSTONE_BRICKS.get()))
                .save(output, Bygone.id("chiseled_shellstone_bricks_from_shellstone_bricks_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BGBlocks.TERRACLAYSTONE.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.CHISELED_TERRACLAYSTONE.get(), 1)
                .unlockedBy("has_ingredient_1", has(BGBlocks.TERRACLAYSTONE.get()))
                .save(output, Bygone.id("chiseled_terraclaystone_from_terraclaystone_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BGBlocks.CLAYSTONE_BRICKS_SLAB.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.CLAYSTONE_BRICKS.get(), 2)
                .unlockedBy("has_claystone_bricks_slab", has(BGBlocks.CLAYSTONE_BRICKS_SLAB.get()))
                .save(output, Bygone.id("claystone_bricks_from_claystone_bricks_slab_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BGBlocks.CLAYSTONE_BRICKS_STAIRS.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.CLAYSTONE_BRICKS.get(), 1)
                .unlockedBy("has_claystone_bricks_stairs", has(BGBlocks.CLAYSTONE_BRICKS_STAIRS.get()))
                .save(output, Bygone.id("claystone_bricks_from_claystone_bricks_stairs_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BGBlocks.CLAYSTONE_BRICKS_WALL.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.CLAYSTONE_BRICKS.get(), 1)
                .unlockedBy("has_claystone_bricks_wall", has(BGBlocks.CLAYSTONE_BRICKS_WALL.get()))
                .save(output, Bygone.id("claystone_bricks_from_claystone_bricks_wall_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BGBlocks.COBBLED_BYSLATE.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.COBBLED_BYSLATE_SLAB.get(), 1)
                .unlockedBy("has_ingredient_1", has(BGBlocks.COBBLED_BYSLATE.get()))
                .save(output, Bygone.id("cobbled_byslate_slab_from_cobbled_byslate_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BGBlocks.COBBLED_BYSLATE.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.COBBLED_BYSLATE_STAIRS.get(), 1)
                .unlockedBy("has_ingredient_1", has(BGBlocks.COBBLED_BYSLATE.get()))
                .save(output, Bygone.id("cobbled_byslate_stairs_from_cobbled_byslate_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BGBlocks.COBBLED_BYSLATE.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.COBBLED_BYSLATE_WALL.get(), 1)
                .unlockedBy("has_ingredient_1", has(BGBlocks.COBBLED_BYSLATE.get()))
                .save(output, Bygone.id("cobbled_byslate_wall_from_cobbled_byslate_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BGBlocks.COBBLED_BYSTONE.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.COBBLED_BYSTONE_SLAB.get(), 1)
                .unlockedBy("has_ingredient_1", has(BGBlocks.COBBLED_BYSTONE.get()))
                .save(output, Bygone.id("cobbled_bystone_slab_from_cobbled_bystone_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BGBlocks.COBBLED_BYSTONE.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.COBBLED_BYSTONE_STAIRS.get(), 1)
                .unlockedBy("has_ingredient_1", has(BGBlocks.COBBLED_BYSTONE.get()))
                .save(output, Bygone.id("cobbled_bystone_stairs_from_cobbled_bystone_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BGBlocks.COBBLED_BYSTONE.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.COBBLED_BYSTONE_WALL.get(), 1)
                .unlockedBy("has_ingredient_1", has(BGBlocks.COBBLED_BYSTONE.get()))
                .save(output, Bygone.id("cobbled_bystone_wall_from_cobbled_bystone_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BGBlocks.PLASMIC_BYSLATE_BRICKS.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.CRACKED_PLASMIC_BYSLATE_BRICKS.get(), 1)
                .unlockedBy("has_ingredient_1", has(BGBlocks.PLASMIC_BYSLATE_BRICKS.get()))
                .save(output, Bygone.id("cracked_plasmic_byslate_bricks_from_plasmic_byslate_bricks_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BGBlocks.SHELLSTONE_BRICKS.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.CRACKED_SHELLSTONE_BRICKS.get(), 1)
                .unlockedBy("has_ingredient_1", has(BGBlocks.SHELLSTONE_BRICKS.get()))
                .save(output, Bygone.id("cracked_shellstone_bricks_from_shellstone_bricks_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BGBlocks.AMBER_SANDSTONE.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.CUT_AMBER_SANDSTONE.get(), 1)
                .unlockedBy("has_ingredient_1", has(BGBlocks.AMBER_SANDSTONE.get()))
                .save(output, Bygone.id("cut_amber_sandstone_from_amber_sandstone_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BGBlocks.CUT_AMBER_SANDSTONE.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.CUT_AMBER_SANDSTONE_SLAB.get(), 2)
                .unlockedBy("has_ingredient_1", has(BGBlocks.CUT_AMBER_SANDSTONE.get()))
                .save(output, Bygone.id("cut_amber_sandstone_slab_from_cut_amber_sandstone_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BGBlocks.GLOWING_PLASMIC_BYSLATE_BRICKS.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.GLOWING_PLASMIC_BYSLATE_BRICKS_SLAB.get(), 2)
                .unlockedBy("has_ingredient_1", has(BGBlocks.GLOWING_PLASMIC_BYSLATE_BRICKS.get()))
                .save(output, Bygone.id("glowing_plasmic_byslate_bricks_from_glowing_plasmic_byslate_bricks_slab_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BGBlocks.GLOWING_PLASMIC_BYSLATE_BRICKS.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.GLOWING_PLASMIC_BYSLATE_BRICKS_STAIRS.get(), 1)
                .unlockedBy("has_ingredient_1", has(BGBlocks.GLOWING_PLASMIC_BYSLATE_BRICKS.get()))
                .save(output, Bygone.id("glowing_plasmic_byslate_bricks_from_glowing_plasmic_byslate_bricks_stairs_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BGBlocks.GLOWING_PLASMIC_BYSLATE_BRICKS.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.GLOWING_PLASMIC_BYSLATE_BRICKS_WALL.get(), 1)
                .unlockedBy("has_ingredient_1", has(BGBlocks.GLOWING_PLASMIC_BYSLATE_BRICKS.get()))
                .save(output, Bygone.id("glowing_plasmic_byslate_bricks_from_glowing_plasmic_byslate_bricks_wall_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BGBlocks.MALACHITE.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.MALACHITE_CHISELED.get(), 1)
                .unlockedBy("has_malachite", has(BGBlocks.MALACHITE.get()))
                .save(output, Bygone.id("malachite_chiseled_from_malachite_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BGBlocks.MALACHITE.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.MALACHITE_PILLAR.get(), 1)
                .unlockedBy("has_ingredient_1", has(BGBlocks.MALACHITE.get()))
                .save(output, Bygone.id("malachite_pillar_from_malachite_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BGBlocks.MALACHITE.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.MALACHITE_SLAB.get(), 1)
                .unlockedBy("has_malachite", has(BGBlocks.MALACHITE.get()))
                .save(output, Bygone.id("malachite_slab_from_malachite_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BGBlocks.MALACHITE.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.MALACHITE_STAIRS.get(), 1)
                .unlockedBy("has_malachite", has(BGBlocks.MALACHITE.get()))
                .save(output, Bygone.id("malachite_stairs_from_malachite_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BGBlocks.MALACHITE.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.MALACHITE_TILE.get(), 1)
                .unlockedBy("has_malachite", has(BGBlocks.MALACHITE.get()))
                .save(output, Bygone.id("malachite_tile_from_malachite_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BGBlocks.MALACHITE_TILE.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.MALACHITE_TILE_SLAB.get(), 1)
                .unlockedBy("has_malachite_tile", has(BGBlocks.MALACHITE_TILE.get()))
                .save(output, Bygone.id("malachite_tile_slab_from_malachite_tile_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BGBlocks.MALACHITE_TILE.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.MALACHITE_TILE_STAIRS.get(), 1)
                .unlockedBy("has_malachite_tile", has(BGBlocks.MALACHITE_TILE.get()))
                .save(output, Bygone.id("malachite_tile_stairs_from_malachite_tile_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BGBlocks.MALACHITE_TILE.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.MALACHITE_TILE_WALL.get(), 1)
                .unlockedBy("has_malachite_tile", has(BGBlocks.MALACHITE_TILE.get()))
                .save(output, Bygone.id("malachite_tile_wall_from_malachite_tile_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BGBlocks.MALACHITE.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.MALACHITE_WALL.get(), 1)
                .unlockedBy("has_malachite", has(BGBlocks.MALACHITE.get()))
                .save(output, Bygone.id("malachite_wall_from_malachite_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BGBlocks.ORANGE_FUNGAL_SLAB.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.ORANGE_FUNGAL_BRICKS.get(), 2)
                .unlockedBy("has_orange_fungal_slab", has(BGBlocks.ORANGE_FUNGAL_SLAB.get()))
                .save(output, Bygone.id("orange_fungal_bricks_from_orange_fungal_slab_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BGBlocks.ORANGE_FUNGAL_STAIRS.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.ORANGE_FUNGAL_BRICKS.get(), 1)
                .unlockedBy("has_orange_fungal_stairs", has(BGBlocks.ORANGE_FUNGAL_STAIRS.get()))
                .save(output, Bygone.id("orange_fungal_bricks_from_orange_fungal_stairs_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BGBlocks.ORANGE_FUNGAL_WALL.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.ORANGE_FUNGAL_BRICKS.get(), 1)
                .unlockedBy("has_orange_fungal_wall", has(BGBlocks.ORANGE_FUNGAL_WALL.get()))
                .save(output, Bygone.id("orange_fungal_bricks_from_orange_fungal_wall_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BGBlocks.PINK_FUNGAL_SLAB.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.PINK_FUNGAL_BRICKS.get(), 2)
                .unlockedBy("has_pink_fungal_slab", has(BGBlocks.PINK_FUNGAL_SLAB.get()))
                .save(output, Bygone.id("pink_fungal_bricks_from_pink_fungal_slab_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BGBlocks.PINK_FUNGAL_STAIRS.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.PINK_FUNGAL_BRICKS.get(), 1)
                .unlockedBy("has_pink_fungal_stairs", has(BGBlocks.PINK_FUNGAL_STAIRS.get()))
                .save(output, Bygone.id("pink_fungal_bricks_from_pink_fungal_stairs_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BGBlocks.PINK_FUNGAL_WALL.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.PINK_FUNGAL_BRICKS.get(), 1)
                .unlockedBy("has_pink_fungal_wall", has(BGBlocks.PINK_FUNGAL_WALL.get()))
                .save(output, Bygone.id("pink_fungal_bricks_from_pink_fungal_wall_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BGBlocks.PLASMIC_BYSLATE_BRICKS.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.PLASMIC_BYSLATE_BRICKS_SLAB.get(), 2)
                .unlockedBy("has_ingredient_1", has(BGBlocks.PLASMIC_BYSLATE_BRICKS.get()))
                .save(output, Bygone.id("plasmic_byslate_bricks_from_plasmic_byslate_bricks_slab_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BGBlocks.PLASMIC_BYSLATE_BRICKS.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.PLASMIC_BYSLATE_BRICKS_STAIRS.get(), 1)
                .unlockedBy("has_ingredient_1", has(BGBlocks.PLASMIC_BYSLATE_BRICKS.get()))
                .save(output, Bygone.id("plasmic_byslate_bricks_from_plasmic_byslate_bricks_stairs_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BGBlocks.PLASMIC_BYSLATE_BRICKS.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.PLASMIC_BYSLATE_BRICKS_WALL.get(), 1)
                .unlockedBy("has_ingredient_1", has(BGBlocks.PLASMIC_BYSLATE_BRICKS.get()))
                .save(output, Bygone.id("plasmic_byslate_bricks_from_plasmic_byslate_bricks_wall_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BGBlocks.PLASMIC_BYSLATE.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.PLASMIC_BYSLATE_SLAB.get(), 2)
                .unlockedBy("has_ingredient_1", has(BGBlocks.PLASMIC_BYSLATE.get()))
                .save(output, Bygone.id("plasmic_byslate_from_plasmic_byslate_slab_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BGBlocks.PLASMIC_BYSLATE.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.PLASMIC_BYSLATE_STAIRS.get(), 1)
                .unlockedBy("has_ingredient_1", has(BGBlocks.PLASMIC_BYSLATE.get()))
                .save(output, Bygone.id("plasmic_byslate_from_plasmic_byslate_stairs_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BGBlocks.PLASMIC_BYSLATE.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.PLASMIC_BYSLATE_WALL.get(), 1)
                .unlockedBy("has_ingredient_1", has(BGBlocks.PLASMIC_BYSLATE.get()))
                .save(output, Bygone.id("plasmic_byslate_from_plasmic_byslate_wall_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BGBlocks.POLISHED_BYSLATE_BRICK.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.POLISHED_BYSLATE_BRICK_SLAB.get(), 2)
                .unlockedBy("has_ingredient_1", has(BGBlocks.POLISHED_BYSLATE_BRICK.get()))
                .save(output, Bygone.id("polished_byslate_bricks_from_polished_byslate_bricks_slab_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BGBlocks.POLISHED_BYSLATE_BRICK.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.POLISHED_BYSLATE_BRICK_STAIRS.get(), 1)
                .unlockedBy("has_ingredient_1", has(BGBlocks.POLISHED_BYSLATE_BRICK.get()))
                .save(output, Bygone.id("polished_byslate_bricks_from_polished_byslate_bricks_stairs_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BGBlocks.POLISHED_BYSLATE_BRICK.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.POLISHED_BYSLATE_BRICK_WALL.get(), 1)
                .unlockedBy("has_ingredient_1", has(BGBlocks.POLISHED_BYSLATE_BRICK.get()))
                .save(output, Bygone.id("polished_byslate_bricks_from_polished_byslate_bricks_wall_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BGBlocks.POLISHED_BYSLATE.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.CHISELED_POLISHED_BYSLATE.get(), 1)
                .unlockedBy("has_ingredient_1", has(BGBlocks.POLISHED_BYSLATE.get()))
                .save(output, Bygone.id("polished_byslate_from_chiseled_polished_byslate_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BGBlocks.POLISHED_BYSLATE.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.POLISHED_BYSLATE_BRICK.get(), 1)
                .unlockedBy("has_ingredient_1", has(BGBlocks.POLISHED_BYSLATE.get()))
                .save(output, Bygone.id("polished_byslate_from_polished_byslate_bricks_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BGBlocks.POLISHED_BYSLATE.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.POLISHED_BYSLATE_SHINGLES.get(), 1)
                .unlockedBy("has_ingredient_1", has(BGBlocks.POLISHED_BYSLATE.get()))
                .save(output, Bygone.id("polished_byslate_from_polished_byslate_shingles_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BGBlocks.POLISHED_BYSLATE.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.POLISHED_BYSLATE_SLAB.get(), 2)
                .unlockedBy("has_ingredient_1", has(BGBlocks.POLISHED_BYSLATE.get()))
                .save(output, Bygone.id("polished_byslate_from_polished_byslate_slab_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BGBlocks.POLISHED_BYSLATE.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.POLISHED_BYSLATE_STAIRS.get(), 1)
                .unlockedBy("has_ingredient_1", has(BGBlocks.POLISHED_BYSLATE.get()))
                .save(output, Bygone.id("polished_byslate_from_polished_byslate_stairs_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BGBlocks.POLISHED_BYSLATE.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.POLISHED_BYSLATE_WALL.get(), 1)
                .unlockedBy("has_ingredient_1", has(BGBlocks.POLISHED_BYSLATE.get()))
                .save(output, Bygone.id("polished_byslate_from_polished_byslate_wall_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BGBlocks.POLISHED_BYSTONE_BRICK.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.POLISHED_BYSTONE_BRICK_SLAB.get(), 2)
                .unlockedBy("has_ingredient_1", has(BGBlocks.POLISHED_BYSTONE_BRICK.get()))
                .save(output, Bygone.id("polished_bystone_bricks_from_polished_bystone_bricks_slab_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BGBlocks.POLISHED_BYSTONE_BRICK.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.POLISHED_BYSTONE_BRICK_STAIRS.get(), 1)
                .unlockedBy("has_ingredient_1", has(BGBlocks.POLISHED_BYSTONE_BRICK.get()))
                .save(output, Bygone.id("polished_bystone_bricks_from_polished_bystone_bricks_stairs_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BGBlocks.POLISHED_BYSTONE_BRICK.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.POLISHED_BYSTONE_BRICK_WALL.get(), 1)
                .unlockedBy("has_ingredient_1", has(BGBlocks.POLISHED_BYSTONE_BRICK.get()))
                .save(output, Bygone.id("polished_bystone_bricks_from_polished_bystone_bricks_wall_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BGBlocks.POLISHED_BYSTONE.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.CHISELED_POLISHED_BYSTONE.get(), 1)
                .unlockedBy("has_ingredient_1", has(BGBlocks.POLISHED_BYSTONE.get()))
                .save(output, Bygone.id("polished_bystone_from_chiseled_polished_bystone_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BGBlocks.POLISHED_BYSTONE.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.POLISHED_BYSTONE_BRICK.get(), 1)
                .unlockedBy("has_ingredient_1", has(BGBlocks.POLISHED_BYSTONE.get()))
                .save(output, Bygone.id("polished_bystone_from_polished_bystone_bricks_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BGBlocks.POLISHED_BYSTONE.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.POLISHED_BYSTONE_SHINGLES.get(), 1)
                .unlockedBy("has_ingredient_1", has(BGBlocks.POLISHED_BYSTONE.get()))
                .save(output, Bygone.id("polished_bystone_from_polished_bystone_shingles_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BGBlocks.POLISHED_BYSTONE.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.POLISHED_BYSTONE_SLAB.get(), 2)
                .unlockedBy("has_ingredient_1", has(BGBlocks.POLISHED_BYSTONE.get()))
                .save(output, Bygone.id("polished_bystone_from_polished_bystone_slab_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BGBlocks.POLISHED_BYSTONE.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.POLISHED_BYSTONE_STAIRS.get(), 1)
                .unlockedBy("has_ingredient_1", has(BGBlocks.POLISHED_BYSTONE.get()))
                .save(output, Bygone.id("polished_bystone_from_polished_bystone_stairs_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BGBlocks.POLISHED_BYSTONE.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.POLISHED_BYSTONE_WALL.get(), 1)
                .unlockedBy("has_ingredient_1", has(BGBlocks.POLISHED_BYSTONE.get()))
                .save(output, Bygone.id("polished_bystone_from_polished_bystone_wall_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BGBlocks.PURPLE_FUNGAL_SLAB.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.PURPLE_FUNGAL_BRICKS.get(), 2)
                .unlockedBy("has_purple_fungal_slab", has(BGBlocks.PURPLE_FUNGAL_SLAB.get()))
                .save(output, Bygone.id("purple_fungal_bricks_from_purple_fungal_slab_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BGBlocks.PURPLE_FUNGAL_STAIRS.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.PURPLE_FUNGAL_BRICKS.get(), 1)
                .unlockedBy("has_purple_fungal_stairs", has(BGBlocks.PURPLE_FUNGAL_STAIRS.get()))
                .save(output, Bygone.id("purple_fungal_bricks_from_purple_fungal_stairs_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BGBlocks.PURPLE_FUNGAL_WALL.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.PURPLE_FUNGAL_BRICKS.get(), 1)
                .unlockedBy("has_purple_fungal_wall", has(BGBlocks.PURPLE_FUNGAL_WALL.get()))
                .save(output, Bygone.id("purple_fungal_bricks_from_purple_fungal_wall_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BGBlocks.SABLE_MOSSY_BYSLATE.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.SABLE_MOSSY_BYSLATE_SLAB.get(), 2)
                .unlockedBy("has_ingredient_1", has(BGBlocks.SABLE_MOSSY_BYSLATE.get()))
                .save(output, Bygone.id("sable_mossy_byslate_slab_from_sable_mossy_byslate_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BGBlocks.SABLE_MOSSY_BYSLATE.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.SABLE_MOSSY_BYSLATE_STAIRS.get(), 1)
                .unlockedBy("has_ingredient_1", has(BGBlocks.SABLE_MOSSY_BYSLATE.get()))
                .save(output, Bygone.id("sable_mossy_byslate_stairs_from_sable_mossy_byslate_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BGBlocks.SABLE_MOSSY_BYSLATE.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.SABLE_MOSSY_BYSLATE_WALL.get(), 1)
                .unlockedBy("has_ingredient_1", has(BGBlocks.SABLE_MOSSY_BYSLATE.get()))
                .save(output, Bygone.id("sable_mossy_byslate_wall_from_sable_mossy_byslate_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BGBlocks.SHELLSTONE_BRICKS_SLAB.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.SHELLSTONE_BRICKS.get(), 2)
                .unlockedBy("has_ingredient_1", has(BGBlocks.SHELLSTONE_BRICKS_SLAB.get()))
                .save(output, Bygone.id("shellstone_bricks_from_shellstone_bricks_slab_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BGBlocks.SHELLSTONE_BRICKS_STAIRS.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.SHELLSTONE_BRICKS.get(), 1)
                .unlockedBy("has_ingredient_1", has(BGBlocks.SHELLSTONE_BRICKS_STAIRS.get()))
                .save(output, Bygone.id("shellstone_bricks_from_shellstone_bricks_stairs_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BGBlocks.SHELLSTONE_BRICKS_WALL.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.SHELLSTONE_BRICKS.get(), 1)
                .unlockedBy("has_ingredient_1", has(BGBlocks.SHELLSTONE_BRICKS_WALL.get()))
                .save(output, Bygone.id("shellstone_bricks_from_shellstone_bricks_wall_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BGBlocks.SHELLSTONE.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.SHELLSTONE_SLAB.get(), 2)
                .unlockedBy("has_ingredient_1", has(BGBlocks.SHELLSTONE.get()))
                .save(output, Bygone.id("shellstone_from_shellstone_slab_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BGBlocks.SHELLSTONE.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.SHELLSTONE_STAIRS.get(), 1)
                .unlockedBy("has_ingredient_1", has(BGBlocks.SHELLSTONE.get()))
                .save(output, Bygone.id("shellstone_from_shellstone_stairs_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BGBlocks.SHELLSTONE.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.SHELLSTONE_WALL.get(), 1)
                .unlockedBy("has_ingredient_1", has(BGBlocks.SHELLSTONE.get()))
                .save(output, Bygone.id("shellstone_from_shellstone_wall_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BGBlocks.SHELLSTONE_BRICKS.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.SHELLSTONE_PILLAR.get(), 1)
                .unlockedBy("has_ingredient_1", has(BGBlocks.SHELLSTONE_BRICKS.get()))
                .save(output, Bygone.id("shellstone_pillar_from_shellstone_bricks_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BGBlocks.SHELLSTONE.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.SHELLSTONE_PILLAR.get(), 1)
                .unlockedBy("has_ingredient_1", has(BGBlocks.SHELLSTONE.get()))
                .save(output, Bygone.id("shellstone_pillar_from_shellstone_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BGBlocks.SMOOTH_AMBER_SANDSTONE.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.SMOOTH_AMBER_SANDSTONE_SLAB.get(), 2)
                .unlockedBy("has_ingredient_1", has(BGBlocks.SMOOTH_AMBER_SANDSTONE.get()))
                .save(output, Bygone.id("smooth_amber_sandstone_slab_from_smooth_amber_sandstone_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BGBlocks.SMOOTH_AMBER_SANDSTONE.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.SMOOTH_AMBER_SANDSTONE_STAIRS.get(), 1)
                .unlockedBy("has_ingredient_1", has(BGBlocks.SMOOTH_AMBER_SANDSTONE.get()))
                .save(output, Bygone.id("smooth_amber_sandstone_stairs_from_smooth_amber_sandstone_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BGBlocks.TERRACLAYSTONE_BRICKS.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.TERRACLAYSTONE_BRICKS_SLAB.get(), 2)
                .unlockedBy("has_ingredient_1", has(BGBlocks.TERRACLAYSTONE_BRICKS.get()))
                .save(output, Bygone.id("terraclaystone_bricks_from_terraclaystone_bricks_slab_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BGBlocks.TERRACLAYSTONE_BRICKS.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.TERRACLAYSTONE_BRICKS_STAIRS.get(), 1)
                .unlockedBy("has_ingredient_1", has(BGBlocks.TERRACLAYSTONE_BRICKS.get()))
                .save(output, Bygone.id("terraclaystone_bricks_from_terraclaystone_bricks_stairs_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BGBlocks.TERRACLAYSTONE_BRICKS.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.TERRACLAYSTONE_BRICKS_WALL.get(), 1)
                .unlockedBy("has_ingredient_1", has(BGBlocks.TERRACLAYSTONE_BRICKS.get()))
                .save(output, Bygone.id("terraclaystone_bricks_from_terraclaystone_bricks_wall_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BGBlocks.TERRACLAYSTONE.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.TERRACLAYSTONE_SLAB.get(), 2)
                .unlockedBy("has_ingredient_1", has(BGBlocks.TERRACLAYSTONE.get()))
                .save(output, Bygone.id("terraclaystone_from_terraclaystone_slab_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BGBlocks.TERRACLAYSTONE.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.TERRACLAYSTONE_STAIRS.get(), 1)
                .unlockedBy("has_ingredient_1", has(BGBlocks.TERRACLAYSTONE.get()))
                .save(output, Bygone.id("terraclaystone_from_terraclaystone_stairs_stonecutting"));

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(BGBlocks.TERRACLAYSTONE.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.TERRACLAYSTONE_WALL.get(), 1)
                .unlockedBy("has_ingredient_1", has(BGBlocks.TERRACLAYSTONE.get()))
                .save(output, Bygone.id("terraclaystone_from_terraclaystone_wall_stonecutting"));
    }

    private static void cookingRecipes(RecipeOutput output) {
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(BGBlocks.COBBLED_BYSLATE.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.BYSLATE.get(), 0.1F, 200)
                .unlockedBy("has_ingredient_1", has(BGBlocks.COBBLED_BYSLATE.get()))
                .save(output, Bygone.id("byslate"));

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(BGBlocks.COBBLED_BYSTONE.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.BYSTONE.get(), 0.1F, 200)
                .unlockedBy("has_ingredient_1", has(BGBlocks.COBBLED_BYSTONE.get()))
                .save(output, Bygone.id("bystone"));

        SimpleCookingRecipeBuilder.blasting(Ingredient.of(BGBlocks.BYSLATE_COAL_ORE.get()), RecipeCategory.MISC, Items.COAL, 0.1F, 100)
                .group("coal")
                .unlockedBy("has_ingredient_1", has(BGBlocks.BYSLATE_COAL_ORE.get()))
                .save(output, Bygone.id("coal_from_blasting_byslate_coal_ore"));

        SimpleCookingRecipeBuilder.blasting(Ingredient.of(BGBlocks.BYSTONE_COAL_ORE.get()), RecipeCategory.MISC, Items.COAL, 0.1F, 100)
                .group("coal")
                .unlockedBy("has_ingredient_1", has(BGBlocks.BYSTONE_COAL_ORE.get()))
                .save(output, Bygone.id("coal_from_blasting_bystone_coal_ore"));

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(BGBlocks.BYSLATE_COAL_ORE.get()), RecipeCategory.MISC, Items.COAL, 0.1F, 200)
                .group("coal")
                .unlockedBy("has_ingredient_1", has(BGBlocks.BYSLATE_COAL_ORE.get()))
                .save(output, Bygone.id("coal_from_smelting_byslate_coal_ore"));

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(BGBlocks.BYSTONE_COAL_ORE.get()), RecipeCategory.MISC, Items.COAL, 0.1F, 200)
                .group("coal")
                .unlockedBy("has_ingredient_1", has(BGBlocks.BYSTONE_COAL_ORE.get()))
                .save(output, Bygone.id("coal_from_smelting_bystone_coal_ore"));

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(BGItems.COELECANTH.get()), RecipeCategory.FOOD, BGItems.COELECANTH_COOKED.get(), 0.35F, 200)
                .unlockedBy("has_ingredient_1", has(BGItems.COELECANTH.get()))
                .save(output, Bygone.id("cooked_coelacanth"));

        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(BGItems.COELECANTH.get()), RecipeCategory.FOOD, BGItems.COELECANTH_COOKED.get(), 0.35F, 600)
                .unlockedBy("has_ingredient_1", has(BGItems.COELECANTH.get()))
                .save(output, Bygone.id("cooked_coelacanth_from_campfire_cooking"));

        SimpleCookingRecipeBuilder.smoking(Ingredient.of(BGItems.COELECANTH.get()), RecipeCategory.FOOD, BGItems.COELECANTH_COOKED.get(), 0.35F, 100)
                .unlockedBy("has_ingredient_1", has(BGItems.COELECANTH.get()))
                .save(output, Bygone.id("cooked_coelacanth_from_smoking"));

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(BGItems.POULTRY.get()), RecipeCategory.FOOD, BGItems.COOKED_POULTRY.get(), 0.35F, 200)
                .unlockedBy("has_ingredient_1", has(BGItems.POULTRY.get()))
                .save(output, Bygone.id("cooked_poultry"));

        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(BGItems.POULTRY.get()), RecipeCategory.FOOD, BGItems.COOKED_POULTRY.get(), 0.35F, 600)
                .unlockedBy("has_ingredient_1", has(BGItems.POULTRY.get()))
                .save(output, Bygone.id("cooked_poultry_from_campfire_cooking"));

        SimpleCookingRecipeBuilder.smoking(Ingredient.of(BGItems.POULTRY.get()), RecipeCategory.FOOD, BGItems.COOKED_POULTRY.get(), 0.35F, 100)
                .unlockedBy("has_ingredient_1", has(BGItems.POULTRY.get()))
                .save(output, Bygone.id("cooked_poultry_from_smoking"));

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(BGItems.PRIMORDIAL_FISH.get()), RecipeCategory.FOOD, BGItems.COOKED_PRIMORDIAL_FISH.get(), 0.35F, 200)
                .unlockedBy("has_ingredient_1", has(BGItems.PRIMORDIAL_FISH.get()))
                .save(output, Bygone.id("cooked_primordial_fish"));

        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(BGItems.PRIMORDIAL_FISH.get()), RecipeCategory.FOOD, BGItems.COOKED_PRIMORDIAL_FISH.get(), 0.35F, 600)
                .unlockedBy("has_ingredient_1", has(BGItems.PRIMORDIAL_FISH.get()))
                .save(output, Bygone.id("cooked_primordial_fish_from_campfire_cooking"));

        SimpleCookingRecipeBuilder.smoking(Ingredient.of(BGItems.PRIMORDIAL_FISH.get()), RecipeCategory.FOOD, BGItems.COOKED_PRIMORDIAL_FISH.get(), 0.35F, 100)
                .unlockedBy("has_ingredient_1", has(BGItems.PRIMORDIAL_FISH.get()))
                .save(output, Bygone.id("cooked_primordial_fish_from_smoking"));

        SimpleCookingRecipeBuilder.blasting(Ingredient.of(BGBlocks.BYSLATE_COPPER_ORE.get()), RecipeCategory.MISC, Items.COPPER_INGOT, 0.1F, 100)
                .group("coal")
                .unlockedBy("has_ingredient_1", has(BGBlocks.BYSLATE_COPPER_ORE.get()))
                .save(output, Bygone.id("copper_from_blasting_byslate_copper_ore"));

        SimpleCookingRecipeBuilder.blasting(Ingredient.of(BGBlocks.BYSTONE_COPPER_ORE.get()), RecipeCategory.MISC, Items.COPPER_INGOT, 0.1F, 100)
                .group("coal")
                .unlockedBy("has_ingredient_1", has(BGBlocks.BYSTONE_COPPER_ORE.get()))
                .save(output, Bygone.id("copper_from_blasting_bystone_copper_ore"));

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(BGBlocks.BYSLATE_COPPER_ORE.get()), RecipeCategory.MISC, Items.COPPER_INGOT, 0.1F, 200)
                .group("coal")
                .unlockedBy("has_ingredient_1", has(BGBlocks.BYSLATE_COPPER_ORE.get()))
                .save(output, Bygone.id("copper_from_smelting_byslate_copper_ore"));

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(BGBlocks.BYSTONE_COPPER_ORE.get()), RecipeCategory.MISC, Items.COPPER_INGOT, 0.1F, 200)
                .group("coal")
                .unlockedBy("has_ingredient_1", has(BGBlocks.BYSTONE_COPPER_ORE.get()))
                .save(output, Bygone.id("copper_from_smelting_bystone_copper_ore"));

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(BGBlocks.PLASMIC_BYSLATE_BRICKS.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.CRACKED_PLASMIC_BYSLATE_BRICKS.get(), 0.1F, 200)
                .unlockedBy("has_ingredient_1", has(BGBlocks.PLASMIC_BYSLATE_BRICKS.get()))
                .save(output, Bygone.id("cracked_plasmic_byslate_bricks"));

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(BGBlocks.SHELLSTONE_BRICKS.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.CRACKED_SHELLSTONE_BRICKS.get(), 0.1F, 200)
                .unlockedBy("has_ingredient_1", has(BGBlocks.SHELLSTONE_BRICKS.get()))
                .save(output, Bygone.id("cracked_shellstone_bricks"));

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(BGBlocks.AMBER_SAND.get()), RecipeCategory.BUILDING_BLOCKS, Items.GLASS, 0.1F, 200)
                .unlockedBy("has_ingredient_1", has(BGBlocks.AMBER_SAND.get()))
                .save(output, Bygone.id("glass"));

        SimpleCookingRecipeBuilder.blasting(Ingredient.of(BGBlocks.BYSLATE_GOLD_ORE.get()), RecipeCategory.MISC, Items.GOLD_INGOT, 0.1F, 100)
                .group("coal")
                .unlockedBy("has_ingredient_1", has(BGBlocks.BYSLATE_GOLD_ORE.get()))
                .save(output, Bygone.id("gold_from_blasting_byslate_gold_ore"));

        SimpleCookingRecipeBuilder.blasting(Ingredient.of(BGBlocks.BYSTONE_GOLD_ORE.get()), RecipeCategory.MISC, Items.GOLD_INGOT, 0.1F, 100)
                .group("coal")
                .unlockedBy("has_ingredient_1", has(BGBlocks.BYSTONE_GOLD_ORE.get()))
                .save(output, Bygone.id("gold_from_blasting_bystone_gold_ore"));

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(BGBlocks.BYSLATE_GOLD_ORE.get()), RecipeCategory.MISC, Items.GOLD_INGOT, 0.1F, 200)
                .group("coal")
                .unlockedBy("has_ingredient_1", has(BGBlocks.BYSLATE_GOLD_ORE.get()))
                .save(output, Bygone.id("gold_from_smelting_byslate_gold_ore"));

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(BGBlocks.BYSTONE_GOLD_ORE.get()), RecipeCategory.MISC, Items.GOLD_INGOT, 0.1F, 200)
                .group("coal")
                .unlockedBy("has_ingredient_1", has(BGBlocks.BYSTONE_GOLD_ORE.get()))
                .save(output, Bygone.id("gold_from_smelting_bystone_gold_ore"));

        SimpleCookingRecipeBuilder.blasting(Ingredient.of(BGItems.GOLD_BIG_BEAK_ARMOR.get()), RecipeCategory.MISC, Items.GOLD_NUGGET, 0.1F, 100)
                .unlockedBy("has_ingredient_1", has(BGItems.GOLD_BIG_BEAK_ARMOR.get()))
                .save(output, Bygone.id("gold_nugget_from_blasting"));

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(BGItems.GOLD_BIG_BEAK_ARMOR.get()), RecipeCategory.MISC, Items.GOLD_NUGGET, 0.1F, 200)
                .unlockedBy("has_ingredient_1", has(BGItems.GOLD_BIG_BEAK_ARMOR.get()))
                .save(output, Bygone.id("gold_nugget_from_smelting"));

        SimpleCookingRecipeBuilder.blasting(Ingredient.of(BGBlocks.BYSLATE_IRON_ORE.get()), RecipeCategory.MISC, Items.IRON_INGOT, 0.1F, 100)
                .group("coal")
                .unlockedBy("has_ingredient_1", has(BGBlocks.BYSLATE_IRON_ORE.get()))
                .save(output, Bygone.id("iron_from_blasting_byslate_iron_ore"));

        SimpleCookingRecipeBuilder.blasting(Ingredient.of(BGBlocks.BYSTONE_IRON_ORE.get()), RecipeCategory.MISC, Items.IRON_INGOT, 0.1F, 100)
                .group("coal")
                .unlockedBy("has_ingredient_1", has(BGBlocks.BYSTONE_IRON_ORE.get()))
                .save(output, Bygone.id("iron_from_blasting_bystone_iron_ore"));

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(BGBlocks.BYSLATE_IRON_ORE.get()), RecipeCategory.MISC, Items.IRON_INGOT, 0.1F, 200)
                .group("coal")
                .unlockedBy("has_ingredient_1", has(BGBlocks.BYSLATE_IRON_ORE.get()))
                .save(output, Bygone.id("iron_from_smelting_byslate_iron_ore"));

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(BGBlocks.BYSTONE_IRON_ORE.get()), RecipeCategory.MISC, Items.IRON_INGOT, 0.1F, 200)
                .group("coal")
                .unlockedBy("has_ingredient_1", has(BGBlocks.BYSTONE_IRON_ORE.get()))
                .save(output, Bygone.id("iron_from_smelting_bystone_iron_ore"));

        SimpleCookingRecipeBuilder.blasting(Ingredient.of(BGItems.IRON_BIG_BEAK_ARMOR.get()), RecipeCategory.MISC, Items.IRON_NUGGET, 0.1F, 100)
                .unlockedBy("has_ingredient_1", has(BGItems.IRON_BIG_BEAK_ARMOR.get()))
                .save(output, Bygone.id("iron_nugget_from_blasting"));

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(BGItems.IRON_BIG_BEAK_ARMOR.get()), RecipeCategory.MISC, Items.IRON_NUGGET, 0.1F, 200)
                .unlockedBy("has_ingredient_1", has(BGItems.IRON_BIG_BEAK_ARMOR.get()))
                .save(output, Bygone.id("iron_nugget_from_smelting"));

        SimpleCookingRecipeBuilder.blasting(Ingredient.of(BGBlocks.LITHINE_ORE.get()), RecipeCategory.MISC, BGItems.LITHOPLASM.get(), 0.1F, 100)
                .group("lithoplasm")
                .unlockedBy("has_ingredient_1", has(BGBlocks.LITHINE_ORE.get()))
                .save(output, Bygone.id("lithoplasm_from_blasting_lithine_ore"));

        SimpleCookingRecipeBuilder.blasting(Ingredient.of(BGBlocks.PLASMIC_LITHINE_ORE.get()), RecipeCategory.MISC, BGItems.LITHOPLASM.get(), 0.1F, 100)
                .group("lithoplasm")
                .unlockedBy("has_ingredient_1", has(BGBlocks.PLASMIC_LITHINE_ORE.get()))
                .save(output, Bygone.id("lithoplasm_from_blasting_plasmic_lithine_ore"));

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(BGBlocks.LITHINE_ORE.get()), RecipeCategory.MISC, BGItems.LITHOPLASM.get(), 0.1F, 200)
                .group("lithoplasm")
                .unlockedBy("has_ingredient_1", has(BGBlocks.LITHINE_ORE.get()))
                .save(output, Bygone.id("lithoplasm_from_smelting_lithine_ore"));

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(BGBlocks.PLASMIC_LITHINE_ORE.get()), RecipeCategory.MISC, BGItems.LITHOPLASM.get(), 0.1F, 200)
                .group("lithoplasm")
                .unlockedBy("has_ingredient_1", has(BGBlocks.PLASMIC_LITHINE_ORE.get()))
                .save(output, Bygone.id("lithoplasm_from_smelting_plasmic_lithine_ore"));

        miscSmelting(Ingredient.of(BGItems.LITHOPLASM.get()), BGItems.LITHOPLASMIC_POWDER.get(), 0.1F, 200)
                .group("lithoplasm")
                .unlockedBy("has_ingredient_1", has(BGItems.LITHOPLASM.get()))
                .save(output, Bygone.id("lithoplasmic_powder_from_smelting_lithoplasm"));

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(BGBlocks.SABLENUT.get()), RecipeCategory.FOOD, BGItems.ROASTED_SABLE_NUT.get(), 0.35F, 200)
                .unlockedBy("has_ingredient_1", has(BGBlocks.SABLENUT.get()))
                .save(output, Bygone.id("roasted_sable_nut"));

        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(BGBlocks.SABLENUT.get()), RecipeCategory.FOOD, BGItems.ROASTED_SABLE_NUT.get(), 0.35F, 600)
                .unlockedBy("has_ingredient_1", has(BGBlocks.SABLENUT.get()))
                .save(output, Bygone.id("roasted_sable_nut_from_campfire_cooking"));

        SimpleCookingRecipeBuilder.smoking(Ingredient.of(BGBlocks.SABLENUT.get()), RecipeCategory.FOOD, BGItems.ROASTED_SABLE_NUT.get(), 0.35F, 100)
                .unlockedBy("has_ingredient_1", has(BGBlocks.SABLENUT.get()))
                .save(output, Bygone.id("roasted_sable_nut_from_smoking"));

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(BGBlocks.SHELLSAND.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.SEAGLASS_BLOCK.get(), 0.1F, 200)
                .unlockedBy("has_ingredient_1", has(BGBlocks.SHELLSAND.get()))
                .save(output, Bygone.id("seaglass"));

        miscSmelting(Ingredient.of(BGBlocks.AMBER_SANDSTONE.get()), BGBlocks.SMOOTH_AMBER_SANDSTONE.get(), 0.1F, 200)
                .group("coal")
                .unlockedBy("has_ingredient_1", has(BGBlocks.AMBER_SANDSTONE.get()))
                .save(output, Bygone.id("smooth_amber_sandstone_from_smelting_amber_sandstone"));

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(BGItems.CRUSHED_SEAGLASS.get()), RecipeCategory.BUILDING_BLOCKS, BGBlocks.SMOOTH_SEAGLASS.get(), 0.2F, 200)
                .unlockedBy("has_ingredient_1", has(BGItems.CRUSHED_SEAGLASS.get()))
                .save(output, Bygone.id("smooth_seaglass"));

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(BGBlocks.VERDIGRIS_SCRAP_BLOCK.get()), RecipeCategory.MISC, BGItems.VERDIGRIS_SCRAP.get(), 2.0F, 200)
                .unlockedBy("has_ingredient_1", has(BGBlocks.VERDIGRIS_SCRAP_BLOCK.get()))
                .save(output, Bygone.id("verdigris_scrap"));

        SimpleCookingRecipeBuilder.blasting(Ingredient.of(BGBlocks.VERDIGRIS_SCRAP_BLOCK.get()), RecipeCategory.MISC, BGItems.VERDIGRIS_SCRAP.get(), 2.0F, 100)
                .unlockedBy("has_ingredient_1", has(BGBlocks.VERDIGRIS_SCRAP_BLOCK.get()))
                .save(output, Bygone.id("verdigris_scrap_from_blasting"));
    }

    public static AdvancementHolder preserveAdvancementId(AdvancementHolder advancement) {
        ResourceLocation recipe = advancement.value().rewards().recipes().getFirst();
        ResourceLocation id = switch (recipe.getPath()) {
            case "ancient_hook" -> Bygone.id("recipes/misc/ancient_hook");
            case "arcane_mechanism" -> Bygone.id("recipes/misc/arcane_mechanism");
            case "echo_gong" -> Bygone.id("recipes/misc/echo_gong");
            case "exotic_arrow" -> Bygone.id("recipes/misc/exotic_arrow");
            case "lithoplasm_arrow" -> Bygone.id("recipes/misc/lithoplasm_arrow");
            case "whirliweed_bundle" -> Bygone.id("recipes/misc/whirliweed_bundle");
            default -> advancement.id();
        };
        return new AdvancementHolder(id, advancement.value());
    }

    private static SimpleCookingRecipeBuilder miscSmelting(Ingredient ingredient, ItemLike result, float experience, int cookingTime) {
        return SimpleCookingRecipeBuilder.generic(ingredient, RecipeCategory.MISC, result, experience, cookingTime,
                RecipeSerializer.SMELTING_RECIPE, (group, category, input, output, xp, time) ->
                        new SmeltingRecipe(group, CookingBookCategory.MISC, input, output, xp, time));
    }

    private static TagKey<Item> tag(String id) {
        return TagKey.create(Registries.ITEM, ResourceLocation.parse(id));
    }
}
