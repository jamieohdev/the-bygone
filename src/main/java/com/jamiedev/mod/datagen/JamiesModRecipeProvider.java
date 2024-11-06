package com.jamiedev.mod.datagen;

import com.jamiedev.mod.init.JamiesModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;
public class JamiesModRecipeProvider  extends FabricRecipeProvider {
    public JamiesModRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate(RecipeExporter exporter) {
        this.SlabRecipe(exporter, JamiesModBlocks.COBBLED_BYSLATE, JamiesModBlocks.COBBLED_BYSLATE_SLAB);
        this.StairsRecipe(exporter, JamiesModBlocks.COBBLED_BYSLATE, JamiesModBlocks.COBBLED_BYSLATE_STAIRS);
        this.WallsRecipe(exporter, JamiesModBlocks.COBBLED_BYSLATE, JamiesModBlocks.COBBLED_BYSLATE_WALL);
        this.SlabRecipe(exporter, JamiesModBlocks.COBBLED_BYSTONE, JamiesModBlocks.COBBLED_BYSTONE_SLAB);
        this.StairsRecipe(exporter, JamiesModBlocks.COBBLED_BYSTONE, JamiesModBlocks.COBBLED_BYSTONE_STAIRS);
        this.WallsRecipe(exporter, JamiesModBlocks.COBBLED_BYSTONE, JamiesModBlocks.COBBLED_BYSTONE_WALL);
    }

    void SlabRecipe(RecipeExporter exporter, Block input, Block output){
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, output, 6)
                .pattern("XXX")
                .input('X', input.asItem())
                .criterion(hasItem(input.asItem()), conditionsFromItem(input.asItem()))
                .offerTo(exporter, Identifier.of(getRecipeName(output.asItem())));

    }


    void StairsRecipe(RecipeExporter exporter, Block input, Block output){
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, output, 4)
                .pattern("X  ")
                .pattern("XX ")
                .pattern("XXX")
                .input('X', input.asItem())
                .criterion(hasItem(input.asItem()), conditionsFromItem(input.asItem()))
                .offerTo(exporter, Identifier.of(getRecipeName(output.asItem())));
    }

    void WallsRecipe(RecipeExporter exporter, Block input, Block output){
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, output, 6)
                .pattern("XXX")
                .pattern("XXX")
                .input('X', input.asItem())
                .criterion(hasItem(input.asItem()), conditionsFromItem(input.asItem()))
                .offerTo(exporter, Identifier.of(getRecipeName(output.asItem())));

    }

    void PressurePlateRecipe(RecipeExporter exporter, Block input, Block output, RecipeCategory recipeCategory, String group){
        ShapedRecipeJsonBuilder.create(recipeCategory, output, 1)
                .pattern("XX")
                .input('X', input.asItem())
                .criterion(hasItem(input.asItem()), conditionsFromItem(input.asItem()))
                .group(group)
                .offerTo(exporter, Identifier.of(getRecipeName(output.asItem())));
    }

    void ChiseledRecipe(RecipeExporter exporter, Block input, Block output, RecipeCategory recipeCategory){
        ShapedRecipeJsonBuilder.create(recipeCategory, output, 1)
                .pattern("X")
                .pattern("X")
                .input('X', input.asItem())
                .criterion(hasItem(input.asItem()), conditionsFromItem(input.asItem()))
                .offerTo(exporter, Identifier.of(getRecipeName(output.asItem())));
    }

    void WoodRecipe(RecipeExporter exporter, Block input, Block output, RecipeCategory recipeCategory){
        ShapedRecipeJsonBuilder.create(recipeCategory, output, 3)
                .pattern("XX")
                .pattern("XX")
                .input('X', input.asItem())
                .criterion(hasItem(input.asItem()), conditionsFromItem(input.asItem()))
                .group("bark")
                .offerTo(exporter, Identifier.of(getRecipeName(output.asItem())));
    }

    void DoorRecipe(RecipeExporter exporter, Block input, Block output, RecipeCategory recipeCategory, String group){
        ShapedRecipeJsonBuilder.create(recipeCategory, output, 3)
                .pattern("XX")
                .pattern("XX")
                .pattern("XX")
                .input('X', input.asItem())
                .criterion(hasItem(input.asItem()), conditionsFromItem(input.asItem()))
                .group(group)
                .showNotification(true)
                .offerTo(exporter, Identifier.of(getRecipeName(output.asItem())));
    }

    void FenceRecipe(RecipeExporter exporter, Block input, Block output, RecipeCategory recipeCategory, String group){
        ShapedRecipeJsonBuilder.create(recipeCategory, output, 3)
                .pattern("WXW")
                .pattern("WXW")
                .input('X', Items.STICK)
                .input('W', input.asItem())
                .criterion(hasItem(input.asItem()), conditionsFromItem(input.asItem()))
                .group(group)
                .offerTo(exporter, Identifier.of(getRecipeName(output.asItem())));
    }

    void FenceGateRecipe(RecipeExporter exporter, Block input, Block output, RecipeCategory recipeCategory, String group){
        ShapedRecipeJsonBuilder.create(recipeCategory, output, 1)
                .pattern("XWX")
                .pattern("XWX")
                .input('X', Items.STICK)
                .input('W', input.asItem())
                .criterion(hasItem(input.asItem()), conditionsFromItem(input.asItem()))
                .group(group)
                .offerTo(exporter, Identifier.of(getRecipeName(output.asItem())));
    }

    void HanginSignRecipe(RecipeExporter exporter, Block input, Item output, RecipeCategory recipeCategory, String group){
        ShapedRecipeJsonBuilder.create(recipeCategory, output, 1)
                .pattern("X X")
                .pattern("WWW")
                .pattern("WWW")
                .input('X', Blocks.CHAIN)
                .input('W', input.asItem())
                .criterion(hasItem(input), conditionsFromItem(input))
                .group(group)
                .offerTo(exporter, Identifier.of(getRecipeName(output.asItem())));
    }

    void SignRecipe(RecipeExporter exporter, Block input, Item output, RecipeCategory recipeCategory, String group){
        ShapedRecipeJsonBuilder.create(recipeCategory, output, 3)
                .pattern("WWW")
                .pattern("WWW")
                .pattern(" X ")
                .input('X', Items.STICK)
                .input('W', input.asItem())
                .criterion(hasItem(input), conditionsFromItem(input))
                .group(group)
                .showNotification(true)
                .offerTo(exporter, Identifier.of(getRecipeName(output.asItem())));
    }

    void TrapDoorRecipe(RecipeExporter exporter, Block input, Block output, RecipeCategory recipeCategory, String group){
        ShapedRecipeJsonBuilder.create(recipeCategory, output, 2)
                .pattern("XXX")
                .pattern("XXX")
                .input('X', input.asItem())
                .criterion(hasItem(input.asItem()), conditionsFromItem(input.asItem()))
                .group(group)
                .showNotification(true)
                .offerTo(exporter, Identifier.of(getRecipeName(output.asItem())));
    }

}