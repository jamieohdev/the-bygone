package com.jamiedev.bygone.core.datagen;

import com.jamiedev.bygone.core.registry.BGBlocks;
import com.jamiedev.bygone.core.registry.BGItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import java.util.concurrent.CompletableFuture;
public class BygoneRecipeProvider extends RecipeProvider {
    public BygoneRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void buildRecipes(RecipeOutput exporter) {

        SlabRecipe(exporter, BGBlocks.SHELLSTONE.get(), BGBlocks.SHELLSTONE_SLAB.get());
        StairsRecipe(exporter, BGBlocks.SHELLSTONE.get(), BGBlocks.SHELLSTONE_STAIRS.get());
        WallsRecipe(exporter, BGBlocks.SHELLSTONE.get(), BGBlocks.SHELLSTONE_WALL.get());

        BricksRecipe(exporter, BGBlocks.SHELLSAND.get(), BGBlocks.SHELLSTONE.get());
        BricksRecipe(exporter, BGBlocks.SHELLSTONE.get(), BGBlocks.SHELLSTONE_BRICKS.get());
        ChiseledRecipe(exporter, BGBlocks.SHELLSTONE_BRICKS_SLAB.get(), BGBlocks.CHISELED_SHELLSTONE_BRICKS.get(), RecipeCategory.DECORATIONS);
        ChiseledRecipe(exporter, BGBlocks.SHELLSTONE_BRICKS.get(), BGBlocks.SHELLSTONE_PILLAR.get(), RecipeCategory.DECORATIONS);
        ChiseledRecipe(exporter, BGBlocks.SHELLSTONE.get(), BGBlocks.SHELLSTONE_PILLAR.get(), RecipeCategory.DECORATIONS);

        SlabRecipe(exporter, BGBlocks.SHELLSTONE_BRICKS.get(), BGBlocks.SHELLSTONE_BRICKS_SLAB.get());
        StairsRecipe(exporter, BGBlocks.SHELLSTONE_BRICKS.get(), BGBlocks.SHELLSTONE_BRICKS_STAIRS.get());
        WallsRecipe(exporter, BGBlocks.SHELLSTONE_BRICKS.get(), BGBlocks.SHELLSTONE_BRICKS_WALL.get());
    }


    void BricksRecipe(RecipeOutput exporter, Block input, Block output){
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, output, 4)
                .pattern("XX")
                .pattern("XX")
                .define('X', input.asItem())
                .unlockedBy(RecipeProvider.getHasName(input.asItem()), RecipeProvider.has(input.asItem()))
                .save(exporter, ResourceLocation.parse(RecipeProvider.getSimpleRecipeName(output.asItem())));

    }

    void SlabRecipe(RecipeOutput exporter, Block input, Block output){
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, output, 6)
                .pattern("XXX")
                .define('X', input.asItem())
                .unlockedBy(RecipeProvider.getHasName(input.asItem()), RecipeProvider.has(input.asItem()))
                .save(exporter, ResourceLocation.parse(RecipeProvider.getSimpleRecipeName(output.asItem())));

    }


    void StairsRecipe(RecipeOutput exporter, Block input, Block output){
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, output, 4)
                .pattern("X  ")
                .pattern("XX ")
                .pattern("XXX")
                .define('X', input.asItem())
                .unlockedBy(RecipeProvider.getHasName(input.asItem()), RecipeProvider.has(input.asItem()))
                .save(exporter, ResourceLocation.parse(RecipeProvider.getSimpleRecipeName(output.asItem())));
    }

    void WallsRecipe(RecipeOutput exporter, Block input, Block output){
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, output, 6)
                .pattern("XXX")
                .pattern("XXX")
                .define('X', input.asItem())
                .unlockedBy(RecipeProvider.getHasName(input.asItem()), RecipeProvider.has(input.asItem()))
                .save(exporter, ResourceLocation.parse(RecipeProvider.getSimpleRecipeName(output.asItem())));

    }

    void PressurePlateRecipe(RecipeOutput exporter, Block input, Block output, RecipeCategory recipeCategory, String group){
        ShapedRecipeBuilder.shaped(recipeCategory, output, 1)
                .pattern("XX")
                .define('X', input.asItem())
                .unlockedBy(RecipeProvider.getHasName(input.asItem()), RecipeProvider.has(input.asItem()))
                .group(group)
                .save(exporter, ResourceLocation.parse(RecipeProvider.getSimpleRecipeName(output.asItem())));
    }

    void ChiseledRecipe(RecipeOutput exporter, Block input, Block output, RecipeCategory recipeCategory){
        ShapedRecipeBuilder.shaped(recipeCategory, output, 1)
                .pattern("X")
                .pattern("X")
                .define('X', input.asItem())
                .unlockedBy(RecipeProvider.getHasName(input.asItem()), RecipeProvider.has(input.asItem()))
                .save(exporter, ResourceLocation.parse(RecipeProvider.getSimpleRecipeName(output.asItem())));
    }

    void WoodRecipe(RecipeOutput exporter, Block input, Block output, RecipeCategory recipeCategory){
        ShapedRecipeBuilder.shaped(recipeCategory, output, 3)
                .pattern("XX")
                .pattern("XX")
                .define('X', input.asItem())
                .unlockedBy(RecipeProvider.getHasName(input.asItem()), RecipeProvider.has(input.asItem()))
                .group("bark")
                .save(exporter, ResourceLocation.parse(RecipeProvider.getSimpleRecipeName(output.asItem())));
    }

    void DoorRecipe(RecipeOutput exporter, Block input, Block output, RecipeCategory recipeCategory, String group){
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

    void FenceRecipe(RecipeOutput exporter, Block input, Block output, RecipeCategory recipeCategory, String group){
        ShapedRecipeBuilder.shaped(recipeCategory, output, 3)
                .pattern("WXW")
                .pattern("WXW")
                .define('X', Items.STICK)
                .define('W', input.asItem())
                .unlockedBy(RecipeProvider.getHasName(input.asItem()), RecipeProvider.has(input.asItem()))
                .group(group)
                .save(exporter, ResourceLocation.parse(RecipeProvider.getSimpleRecipeName(output.asItem())));
    }

    void FenceGateRecipe(RecipeOutput exporter, Block input, Block output, RecipeCategory recipeCategory, String group){
        ShapedRecipeBuilder.shaped(recipeCategory, output, 1)
                .pattern("XWX")
                .pattern("XWX")
                .define('X', Items.STICK)
                .define('W', input.asItem())
                .unlockedBy(RecipeProvider.getHasName(input.asItem()), RecipeProvider.has(input.asItem()))
                .group(group)
                .save(exporter, ResourceLocation.parse(RecipeProvider.getSimpleRecipeName(output.asItem())));
    }

    void HangingSignRecipe(RecipeOutput exporter, Block input, Item output, RecipeCategory recipeCategory, String group){
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

    void SignRecipe(RecipeOutput exporter, Block input, Item output, RecipeCategory recipeCategory, String group){
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

    void TrapDoorRecipe(RecipeOutput exporter, Block input, Block output, RecipeCategory recipeCategory, String group){
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