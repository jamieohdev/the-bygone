package com.jamiedev.bygone.core.datagen;

import com.jamiedev.bygone.core.registry.BGBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class BygoneRecipeProvider extends RecipeProvider {
    public BygoneRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void buildRecipes(@NotNull RecipeOutput exporter) {
/*
        slabRecipe(exporter, BGBlocks.SHELLSTONE.get(), BGBlocks.SHELLSTONE_SLAB.get());
        stairsRecipe(exporter, BGBlocks.SHELLSTONE.get(), BGBlocks.SHELLSTONE_STAIRS.get());
        wallsRecipe(exporter, BGBlocks.SHELLSTONE.get(), BGBlocks.SHELLSTONE_WALL.get());

        bricksRecipe(exporter, BGBlocks.SHELLSAND.get(), BGBlocks.SHELLSTONE.get());
        bricksRecipe(exporter, BGBlocks.SHELLSTONE.get(), BGBlocks.SHELLSTONE_BRICKS.get());
        chiseledRecipe(exporter, BGBlocks.SHELLSTONE_BRICKS_SLAB.get(), BGBlocks.CHISELED_SHELLSTONE_BRICKS.get(), RecipeCategory.DECORATIONS);
        chiseledRecipe(exporter, BGBlocks.SHELLSTONE_BRICKS.get(), BGBlocks.SHELLSTONE_PILLAR.get(), RecipeCategory.DECORATIONS);
        chiseledRecipe(exporter, BGBlocks.SHELLSTONE.get(), BGBlocks.SHELLSTONE_PILLAR.get(), RecipeCategory.DECORATIONS);

        slabRecipe(exporter, BGBlocks.SHELLSTONE_BRICKS.get(), BGBlocks.SHELLSTONE_BRICKS_SLAB.get());
        stairsRecipe(exporter, BGBlocks.SHELLSTONE_BRICKS.get(), BGBlocks.SHELLSTONE_BRICKS_STAIRS.get());
        wallsRecipe(exporter, BGBlocks.SHELLSTONE_BRICKS.get(), BGBlocks.SHELLSTONE_BRICKS_WALL.get());

        glowConcretePowder(exporter, BGBlocks.BLACK_GLOW_CONCRETE_POWDER.get(), Items.BLACK_DYE);
        glowConcretePowder(exporter, BGBlocks.BLUE_GLOW_CONCRETE_POWDER.get(), Items.BLUE_DYE);
        glowConcretePowder(exporter, BGBlocks.BROWN_GLOW_CONCRETE_POWDER.get(), Items.BROWN_DYE);
        glowConcretePowder(exporter, BGBlocks.CYAN_GLOW_CONCRETE_POWDER.get(), Items.CYAN_DYE);
        glowConcretePowder(exporter, BGBlocks.GRAY_GLOW_CONCRETE_POWDER.get(), Items.GRAY_DYE);
        glowConcretePowder(exporter, BGBlocks.GREEN_GLOW_CONCRETE_POWDER.get(), Items.GREEN_DYE);
        glowConcretePowder(exporter, BGBlocks.LIGHT_BLUE_GLOW_CONCRETE_POWDER.get(), Items.LIGHT_BLUE_DYE);
        glowConcretePowder(exporter, BGBlocks.LIGHT_GRAY_GLOW_CONCRETE_POWDER.get(), Items.LIGHT_GRAY_DYE);
        glowConcretePowder(exporter, BGBlocks.LIME_GLOW_CONCRETE_POWDER.get(), Items.LIME_DYE);
        glowConcretePowder(exporter, BGBlocks.MAGENTA_GLOW_CONCRETE_POWDER.get(), Items.MAGENTA_DYE);
        glowConcretePowder(exporter, BGBlocks.ORANGE_GLOW_CONCRETE_POWDER.get(), Items.ORANGE_DYE);
        glowConcretePowder(exporter, BGBlocks.PINK_GLOW_CONCRETE_POWDER.get(), Items.PINK_DYE);
        glowConcretePowder(exporter, BGBlocks.PURPLE_GLOW_CONCRETE_POWDER.get(), Items.PURPLE_DYE);
        glowConcretePowder(exporter, BGBlocks.RED_GLOW_CONCRETE_POWDER.get(), Items.RED_DYE);
        glowConcretePowder(exporter, BGBlocks.WHITE_GLOW_CONCRETE_POWDER.get(), Items.WHITE_DYE);
        glowConcretePowder(exporter, BGBlocks.YELLOW_GLOW_CONCRETE_POWDER.get(), Items.YELLOW_DYE);*/
    }



    protected static void glowConcretePowder(RecipeOutput recipeOutput, ItemLike glowConcretePowder, ItemLike dye) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, glowConcretePowder, 8).requires(dye).requires(BGBlocks.SHELLSAND.get(), 4).requires(BGBlocks.GLOW_GRAVEL.get(), 4).group("glow_concrete_powder").unlockedBy("has_shellsand", has(BGBlocks.SHELLSAND.get())).unlockedBy("has_glow_gravel", has(BGBlocks.GLOW_GRAVEL.get())).save(recipeOutput);
    }

    void bricksRecipe(RecipeOutput exporter, Block input, Block output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, output, 4)
                .pattern("XX")
                .pattern("XX")
                .define('X', input.asItem())
                .unlockedBy(RecipeProvider.getHasName(input.asItem()), RecipeProvider.has(input.asItem()))
                .save(exporter, ResourceLocation.parse(RecipeProvider.getSimpleRecipeName(output.asItem())));

    }

    void slabRecipe(RecipeOutput exporter, Block input, Block output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, output, 6)
                .pattern("XXX")
                .define('X', input.asItem())
                .unlockedBy(RecipeProvider.getHasName(input.asItem()), RecipeProvider.has(input.asItem()))
                .save(exporter, ResourceLocation.parse(RecipeProvider.getSimpleRecipeName(output.asItem())));

    }


    void stairsRecipe(RecipeOutput exporter, Block input, Block output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, output, 4)
                .pattern("X  ")
                .pattern("XX ")
                .pattern("XXX")
                .define('X', input.asItem())
                .unlockedBy(RecipeProvider.getHasName(input.asItem()), RecipeProvider.has(input.asItem()))
                .save(exporter, ResourceLocation.parse(RecipeProvider.getSimpleRecipeName(output.asItem())));
    }

    void wallsRecipe(RecipeOutput exporter, Block input, Block output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, output, 6)
                .pattern("XXX")
                .pattern("XXX")
                .define('X', input.asItem())
                .unlockedBy(RecipeProvider.getHasName(input.asItem()), RecipeProvider.has(input.asItem()))
                .save(exporter, ResourceLocation.parse(RecipeProvider.getSimpleRecipeName(output.asItem())));

    }

    void pressurePlateRecipe(RecipeOutput exporter, Block input, Block output, RecipeCategory recipeCategory, String group) {
        ShapedRecipeBuilder.shaped(recipeCategory, output, 1)
                .pattern("XX")
                .define('X', input.asItem())
                .unlockedBy(RecipeProvider.getHasName(input.asItem()), RecipeProvider.has(input.asItem()))
                .group(group)
                .save(exporter, ResourceLocation.parse(RecipeProvider.getSimpleRecipeName(output.asItem())));
    }

    void chiseledRecipe(RecipeOutput exporter, Block input, Block output, RecipeCategory recipeCategory) {
        ShapedRecipeBuilder.shaped(recipeCategory, output, 1)
                .pattern("X")
                .pattern("X")
                .define('X', input.asItem())
                .unlockedBy(RecipeProvider.getHasName(input.asItem()), RecipeProvider.has(input.asItem()))
                .save(exporter, ResourceLocation.parse(RecipeProvider.getSimpleRecipeName(output.asItem())));
    }

    void woodRecipe(RecipeOutput exporter, Block input, Block output, RecipeCategory recipeCategory) {
        ShapedRecipeBuilder.shaped(recipeCategory, output, 3)
                .pattern("XX")
                .pattern("XX")
                .define('X', input.asItem())
                .unlockedBy(RecipeProvider.getHasName(input.asItem()), RecipeProvider.has(input.asItem()))
                .group("bark")
                .save(exporter, ResourceLocation.parse(RecipeProvider.getSimpleRecipeName(output.asItem())));
    }

    void doorRecipe(RecipeOutput exporter, Block input, Block output, RecipeCategory recipeCategory, String group) {
        ShapedRecipeBuilder.shaped(recipeCategory, output, 3)
                .pattern("XX")
                .pattern("XX")
                .pattern("XX")
                .define('X', input.asItem())
                .unlockedBy(RecipeProvider.getHasName(input.asItem()), RecipeProvider.has(input.asItem()))
                .group(group)
                .showNotification(true)
                .save(exporter, ResourceLocation.parse(RecipeProvider.getSimpleRecipeName(output.asItem())));
    }

    void fenceRecipe(RecipeOutput exporter, Block input, Block output, RecipeCategory recipeCategory, String group) {
        ShapedRecipeBuilder.shaped(recipeCategory, output, 3)
                .pattern("WXW")
                .pattern("WXW")
                .define('X', Items.STICK)
                .define('W', input.asItem())
                .unlockedBy(RecipeProvider.getHasName(input.asItem()), RecipeProvider.has(input.asItem()))
                .group(group)
                .save(exporter, ResourceLocation.parse(RecipeProvider.getSimpleRecipeName(output.asItem())));
    }

    void fenceGateRecipe(RecipeOutput exporter, Block input, Block output, RecipeCategory recipeCategory, String group) {
        ShapedRecipeBuilder.shaped(recipeCategory, output, 1)
                .pattern("XWX")
                .pattern("XWX")
                .define('X', Items.STICK)
                .define('W', input.asItem())
                .unlockedBy(RecipeProvider.getHasName(input.asItem()), RecipeProvider.has(input.asItem()))
                .group(group)
                .save(exporter, ResourceLocation.parse(RecipeProvider.getSimpleRecipeName(output.asItem())));
    }

    void hangingSignRecipe(RecipeOutput exporter, Block input, Item output, RecipeCategory recipeCategory, String group) {
        ShapedRecipeBuilder.shaped(recipeCategory, output, 1)
                .pattern("X X")
                .pattern("WWW")
                    .pattern("WWW")
                    .define('X', Blocks.CHAIN)
                    .define('W', input.asItem())
                    .unlockedBy(RecipeProvider.getHasName(input), RecipeProvider.has(input))
                    .group(group)
                    .save(exporter, ResourceLocation.parse(RecipeProvider.getSimpleRecipeName(output.asItem())));
        }

        void signRecipe(RecipeOutput exporter, Block input, Item output, RecipeCategory recipeCategory, String group) {
            ShapedRecipeBuilder.shaped(recipeCategory, output, 3)
                    .pattern("WWW")
                    .pattern("WWW")
                    .pattern(" X ")
                    .define('X', Items.STICK)
                    .define('W', input.asItem())
                    .unlockedBy(RecipeProvider.getHasName(input), RecipeProvider.has(input))
                    .group(group)
                    .showNotification(true)
                    .save(exporter, ResourceLocation.parse(RecipeProvider.getSimpleRecipeName(output.asItem())));
        }

        void trapDoorRecipe(RecipeOutput exporter, Block input, Block output, RecipeCategory recipeCategory, String group) {
            ShapedRecipeBuilder.shaped(recipeCategory, output, 2)
                    .pattern("XXX")
                    .pattern("XXX")
                    .define('X', input.asItem())
                    .unlockedBy(RecipeProvider.getHasName(input.asItem()), RecipeProvider.has(input.asItem()))
                    .group(group)
                    .showNotification(true)
                    .save(exporter, ResourceLocation.parse(RecipeProvider.getSimpleRecipeName(output.asItem())));
        }


}