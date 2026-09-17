package com.jamiedev.bygone.core.datagen;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.core.registry.BGBlocks;
import com.jamiedev.bygone.core.registry.BGItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class BygoneRecipeProvider extends RecipeProvider {
    public BygoneRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void buildRecipes(@NotNull RecipeOutput exporter) {

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BGItems.AMARANTH_LOAF.get(),1)
                .pattern("AAA")
                .define('A', BGItems.AMARANTH_GRAIN.get())
                .unlockedBy(getHasName(BGItems.AMARANTH_GRAIN.get()), has(BGItems.AMARANTH_GRAIN.get()))
                .save(exporter, ResourceLocation.parse(getSimpleRecipeName(BGItems.AMARANTH_LOAF.get())));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS,BGBlocks.AMBER_SANDSTONE.get(),1)
                .pattern("SS")
                .pattern("SS")
                .define('S', BGBlocks.AMBER_SAND.get())
                .unlockedBy(getHasName(BGBlocks.AMBER_SAND.get()), has(BGBlocks.AMBER_SAND.get()))
                .save(exporter, ResourceLocation.parse(getSimpleRecipeName(BGBlocks.AMBER_SANDSTONE.get())));

        stonecutterResultFromBase(exporter,RecipeCategory.BUILDING_BLOCKS,BGBlocks.AMBER_SANDSTONE_SLAB.get(),BGBlocks.AMBER_SANDSTONE.get());
        stonecutterResultFromBase(exporter,RecipeCategory.BUILDING_BLOCKS,BGBlocks.AMBER_SANDSTONE_STAIRS.get(),BGBlocks.AMBER_SANDSTONE.get());
        stonecutterResultFromBase(exporter,RecipeCategory.BUILDING_BLOCKS,BGBlocks.AMBER_SANDSTONE_WALL.get(),BGBlocks.AMBER_SANDSTONE.get());

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS,BGBlocks.AMBER_SANDSTONE_SLAB.get(),6)
                .pattern("SSS")
                .define('S', BGBlocks.AMBER_SANDSTONE.get())
                .unlockedBy(getHasName(BGBlocks.AMBER_SANDSTONE.get()), has(BGBlocks.AMBER_SANDSTONE.get()))
                .save(exporter, ResourceLocation.parse(getSimpleRecipeName(BGBlocks.AMBER_SANDSTONE_SLAB.get())));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS,BGBlocks.AMBER_SANDSTONE_STAIRS.get(),4)
                .pattern("S  ")
                .pattern("SS ")
                .pattern("SSS")
                .define('S', BGBlocks.AMBER_SANDSTONE.get())
                .unlockedBy(getHasName(BGBlocks.AMBER_SANDSTONE.get()), has(BGBlocks.AMBER_SANDSTONE.get()))
                .save(exporter, ResourceLocation.parse(getSimpleRecipeName(BGBlocks.AMBER_SANDSTONE_STAIRS.get())));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS,BGBlocks.AMBER_SANDSTONE_WALL.get(),6)
                .pattern("SSS")
                .pattern("SSS")
                .define('S', BGBlocks.AMBER_SANDSTONE.get())
                .unlockedBy(getHasName(BGBlocks.AMBER_SANDSTONE.get()), has(BGBlocks.AMBER_SANDSTONE.get()))
                .save(exporter, ResourceLocation.parse(getSimpleRecipeName(BGBlocks.AMBER_SANDSTONE_WALL.get())));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,BGItems.AMOEBA_GEL_ON_A_STICK.get(),1)
                .pattern("# ")
                .pattern(" X")
                .define('#',Items.FISHING_ROD)
                .define('X',BGItems.AMOEBA_GEL.get())
                .unlockedBy(getHasName(BGItems.AMOEBA_GEL.get()), has(BGItems.AMOEBA_GEL.get()))
                .save(exporter, ResourceLocation.parse(getSimpleRecipeName(BGItems.AMOEBA_GEL_ON_A_STICK.get())));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,BGBlocks.AMPHORA.get(),1)
                .pattern("# #")
                .pattern("# #")
                .pattern("###")
                .define('#',BGBlocks.CLAYSTONE.get())
                .unlockedBy(getHasName(BGBlocks.CLAYSTONE.get()), has(BGBlocks.CLAYSTONE.get()))
                .save(exporter, ResourceLocation.parse(getSimpleRecipeName(BGBlocks.AMPHORA.get())));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE,BGBlocks.ANCIENT_BUTTON.get(),1)
                .requires(BGBlocks.ANCIENT_PLANKS.get())
                .unlockedBy(getHasName(BGBlocks.ANCIENT_PLANKS.get()), has(BGBlocks.ANCIENT_PLANKS.get()))
                .save(exporter, ResourceLocation.parse(getSimpleRecipeName(BGBlocks.ANCIENT_BUTTON.get())));

        doorRecipe(exporter, BGBlocks.ANCIENT_PLANKS.get(), BGBlocks.ANCIENT_DOOR.get(), RecipeCategory.REDSTONE, "ancient_door");
        fenceRecipe(exporter,BGBlocks.ANCIENT_PLANKS.get(),BGBlocks.ANCIENT_FENCE.get(),RecipeCategory.DECORATIONS,"ancient_fence");
        fenceGateRecipe(exporter,BGBlocks.ANCIENT_PLANKS.get(),BGBlocks.ANCIENT_FENCE_GATE.get(),RecipeCategory.DECORATIONS,"ancient_fence_gate");
        hangingSignRecipe(exporter, BGBlocks.ANCIENT_PLANKS.get(), BGItems.ANCIENT_HANGING_SIGN.get(), RecipeCategory.DECORATIONS, "ancient_hanging_sign");

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT,BGItems.ANCIENT_HOOK.get(),1)
                .pattern(" X ")
                .pattern(" I")
                .define('X',BGItems.VERDIGRIS_INGOT.get())
                .define('I', Items.BLAZE_POWDER)
                .unlockedBy(getHasName(BGItems.VERDIGRIS_INGOT.get()), has(BGItems.VERDIGRIS_INGOT.get()))
                .save(exporter, ResourceLocation.parse(getSimpleRecipeName(BGItems.ANCIENT_HOOK.get())));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS,BGBlocks.ANCIENT_PLANKS.get(),4)
                .requires(BGBlocks.ANCIENT_LOG.get())
                .unlockedBy(getHasName(BGBlocks.ANCIENT_LOG.get()), has(BGBlocks.ANCIENT_LOG.get()))
                .save(exporter, ResourceLocation.parse(getSimpleRecipeName(BGBlocks.ANCIENT_PLANKS.get())));

        pressurePlateRecipe(exporter, BGBlocks.ANCIENT_PLANKS.get(), BGBlocks.ANCIENT_PRESSURE_PLATE.get(), RecipeCategory.REDSTONE, "ancient_pressure_plate");
        signRecipe(exporter, BGBlocks.ANCIENT_PLANKS.get(), BGItems.ANCIENT_SIGN.get(), RecipeCategory.DECORATIONS, "ancient_sign");
        slabRecipe(exporter,BGBlocks.ANCIENT_PLANKS.get(), BGBlocks.ANCIENT_SLAB.get());
        stairsRecipe(exporter,BGBlocks.ANCIENT_PLANKS.get(), BGBlocks.ANCIENT_STAIRS.get());
        trapDoorRecipe(exporter, BGBlocks.ANCIENT_PLANKS.get(), BGBlocks.ANCIENT_TRAPDOOR.get(), RecipeCategory.REDSTONE, "ancient_trapdoor");
        woodRecipe(exporter,BGBlocks.ANCIENT_LOG.get(), BGBlocks.ANCIENT_WOOD.get(), RecipeCategory.BUILDING_BLOCKS);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE,BGBlocks.ANGRY_MEGALITH_LANTERN.get(),1)
                .requires(BGBlocks.ANGRY_MEGALITH_FACE.get())
                .requires(BGItems.LITHOPLASM.get())
                .unlockedBy(getHasName(BGItems.LITHOPLASM.get()), has(BGItems.LITHOPLASM.get()))
                .save(exporter, ResourceLocation.parse(getSimpleRecipeName(BGBlocks.ANGRY_MEGALITH_LANTERN.get())));















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

    protected static void oreSmelting(RecipeOutput recipeOutput, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult,
                                      float pExperience, int pCookingTIme, String pGroup) {
        oreCooking(recipeOutput, RecipeSerializer.SMELTING_RECIPE, SmeltingRecipe::new, pIngredients, pCategory, pResult,
                pExperience, pCookingTIme, pGroup, "_from_smelting");
    }

    protected static void oreBlasting(RecipeOutput recipeOutput, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult,
                                      float pExperience, int pCookingTime, String pGroup) {
        oreCooking(recipeOutput, RecipeSerializer.BLASTING_RECIPE, BlastingRecipe::new, pIngredients, pCategory, pResult,
                pExperience, pCookingTime, pGroup, "_from_blasting");
    }

    protected static <T extends AbstractCookingRecipe> void oreCooking(RecipeOutput recipeOutput, RecipeSerializer<T> pCookingSerializer, AbstractCookingRecipe.Factory<T> factory,
                                                                       List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup, String pRecipeName) {
        for(ItemLike itemlike : pIngredients) {
            SimpleCookingRecipeBuilder.generic(Ingredient.of(itemlike), pCategory, pResult, pExperience, pCookingTime, pCookingSerializer, factory).group(pGroup).unlockedBy(getHasName(itemlike), has(itemlike))
                    .save(recipeOutput, Bygone.MOD_ID + ":" + getItemName(pResult) + pRecipeName + "_" + getItemName(itemlike));
        }
    }
}
