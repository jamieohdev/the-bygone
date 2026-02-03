package com.jamiedev.bygone.core.datagen;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.core.registry.BGBlocks;
import com.jamiedev.bygone.core.registry.BGItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

@SuppressWarnings("all")
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
                .pattern(" I ")
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

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS,BGBlocks.AMARANTH_BALE_BLOCK.get(),1)
                .requires(BGItems.AMARANTH_GRAIN.get(),9)
                .unlockedBy(getHasName(BGBlocks.AMARANTH_CROP.get()), has(BGBlocks.AMARANTH_CROP.get()))
                .save(exporter, ResourceLocation.parse(getSimpleRecipeName(BGBlocks.AMARANTH_BALE_BLOCK.get())));

        oreSmelting(exporter, List.of(BGBlocks.COBBLED_BYSLATE.get()),RecipeCategory.BUILDING_BLOCKS,BGBlocks.BYSLATE.get(),0.1f,200,"byslate");

        stonecutterResultFromBase(exporter,RecipeCategory.BUILDING_BLOCKS,BGBlocks.POLISHED_BYSLATE.get(),BGBlocks.BYSLATE.get(),1);

        oreSmelting(exporter, List.of(BGBlocks.COBBLED_BYSTONE.get()),RecipeCategory.BUILDING_BLOCKS,BGBlocks.BYSTONE.get(),0.1f,200,"bystone");

        stonecutterResultFromBase(exporter,RecipeCategory.BUILDING_BLOCKS,BGBlocks.POLISHED_BYSTONE.get(),BGBlocks.BYSTONE.get(),1);

        chiseledRecipe(exporter,RecipeCategory.BUILDING_BLOCKS,BGBlocks.AMBER_SANDSTONE_SLAB.get(),BGBlocks.CHISELED_AMBER_SANDSTONE.get());

        stonecutterResultFromBase(exporter,RecipeCategory.BUILDING_BLOCKS,BGBlocks.CHISELED_AMBER_SANDSTONE.get(),BGBlocks.AMBER_SANDSTONE.get(),1);

        chiseledRecipe(exporter,RecipeCategory.BUILDING_BLOCKS,BGBlocks.POLISHED_BYSLATE_BRICK_SLAB.get(),BGBlocks.CHISELED_POLISHED_BYSLATE.get());

        stonecutterResultFromBase(exporter,RecipeCategory.BUILDING_BLOCKS,BGBlocks.CHISELED_POLISHED_BYSLATE.get(),BGBlocks.POLISHED_BYSLATE.get(),1);

        chiseledRecipe(exporter,RecipeCategory.BUILDING_BLOCKS,BGBlocks.POLISHED_BYSTONE_BRICK_SLAB.get(),BGBlocks.CHISELED_POLISHED_BYSTONE.get());

        stonecutterResultFromBase(exporter,RecipeCategory.BUILDING_BLOCKS,BGBlocks.CHISELED_POLISHED_BYSTONE.get(),BGBlocks.POLISHED_BYSTONE.get(),1);

        chiseledRecipe(exporter,RecipeCategory.BUILDING_BLOCKS,BGBlocks.SHELLSTONE_BRICKS_SLAB.get(),BGBlocks.CHISELED_SHELLSTONE_BRICKS.get());

        stonecutterResultFromBase(exporter,RecipeCategory.BUILDING_BLOCKS,BGBlocks.CHISELED_SHELLSTONE_BRICKS.get(),BGBlocks.SHELLSTONE_BRICKS.get(),1);

        bricksRecipe(exporter,BGBlocks.CLAYSTONE.get(),BGBlocks.CLAYSTONE_BRICKS.get());

        stonecutterResultFromBase(exporter,RecipeCategory.BUILDING_BLOCKS,BGBlocks.CLAYSTONE_BRICKS_SLAB.get(),BGBlocks.CLAYSTONE_BRICKS.get(),2);

        stonecutterResultFromBase(exporter,RecipeCategory.BUILDING_BLOCKS,BGBlocks.CLAYSTONE_BRICKS_STAIRS.get(),BGBlocks.CLAYSTONE_BRICKS.get(),1);

        stonecutterResultFromBase(exporter,RecipeCategory.BUILDING_BLOCKS,BGBlocks.CLAYSTONE_BRICKS_WALL.get(),BGBlocks.CLAYSTONE_BRICKS.get(),1);

        slabRecipe(exporter,BGBlocks.CLAYSTONE_BRICKS.get(),BGBlocks.CLAYSTONE_BRICKS_SLAB.get());

        stairsRecipe(exporter,BGBlocks.CLAYSTONE_BRICKS.get(),BGBlocks.CLAYSTONE_BRICKS_STAIRS.get());

        wallsRecipe(exporter,BGBlocks.CLAYSTONE_BRICKS.get(),BGBlocks.CLAYSTONE_BRICKS_WALL.get());

        oreBlasting(exporter,List.of(BGBlocks.BYSTONE_COAL_ORE.get(), BGBlocks.BYSLATE_COAL_ORE.get()),RecipeCategory.MISC,Items.COAL,0.1f,100,"coal_from_bygone_ores");
        oreSmelting(exporter,List.of(BGBlocks.BYSTONE_COAL_ORE.get(), BGBlocks.BYSLATE_COAL_ORE.get()),RecipeCategory.MISC,Items.COAL,0.1f,200,"coal_from_bygone_ores");


        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS,BGBlocks.COARSE_CLAYSTONE.get(),1)
                .pattern("SC")
                .pattern("CS")
                .define('S', BGBlocks.CLAYSTONE.get())
                .define('C', Items.GRAVEL)
                .unlockedBy(getHasName(BGBlocks.CLAYSTONE.get()), has(BGBlocks.CLAYSTONE.get()))
                .save(exporter, ResourceLocation.parse(getSimpleRecipeName(BGBlocks.COARSE_CLAYSTONE.get())));

        slabRecipe(exporter,BGBlocks.COBBLED_BYSLATE.get(),BGBlocks.COBBLED_BYSLATE_SLAB.get());

        stonecutterResultFromBase(exporter,RecipeCategory.BUILDING_BLOCKS,BGBlocks.COBBLED_BYSLATE_SLAB.get(),BGBlocks.COBBLED_BYSLATE.get(),1);

        stairsRecipe(exporter,BGBlocks.COBBLED_BYSLATE.get(),BGBlocks.COBBLED_BYSLATE_STAIRS.get());

        stonecutterResultFromBase(exporter,RecipeCategory.BUILDING_BLOCKS,BGBlocks.COBBLED_BYSLATE_STAIRS.get(),BGBlocks.COBBLED_BYSLATE.get(),1);

        wallsRecipe(exporter,BGBlocks.COBBLED_BYSLATE.get(),BGBlocks.COBBLED_BYSLATE_WALL.get());

        stonecutterResultFromBase(exporter,RecipeCategory.BUILDING_BLOCKS,BGBlocks.COBBLED_BYSLATE_WALL.get(),BGBlocks.COBBLED_BYSLATE.get(),1);

        slabRecipe(exporter,BGBlocks.COBBLED_BYSTONE.get(),BGBlocks.COBBLED_BYSTONE_SLAB.get());

        stonecutterResultFromBase(exporter,RecipeCategory.BUILDING_BLOCKS,BGBlocks.COBBLED_BYSTONE_SLAB.get(),BGBlocks.COBBLED_BYSTONE.get(),1);

        stairsRecipe(exporter,BGBlocks.COBBLED_BYSTONE.get(),BGBlocks.COBBLED_BYSTONE_STAIRS.get());

        stonecutterResultFromBase(exporter,RecipeCategory.BUILDING_BLOCKS,BGBlocks.COBBLED_BYSTONE_STAIRS.get(),BGBlocks.COBBLED_BYSTONE.get(),1);

        wallsRecipe(exporter,BGBlocks.COBBLED_BYSTONE.get(),BGBlocks.COBBLED_BYSTONE_WALL.get());

        stonecutterResultFromBase(exporter,RecipeCategory.BUILDING_BLOCKS,BGBlocks.COBBLED_BYSTONE_WALL.get(),BGBlocks.COBBLED_BYSTONE.get(),1);

        oreSmelting(exporter,List.of(BGItems.COELECANTH.get()),RecipeCategory.FOOD,BGItems.COELECANTH_COOKED.get(),0.35f,200,"cooked_coelecanth");

        itemCampfire(exporter,List.of(BGItems.COELECANTH.get()),RecipeCategory.FOOD,BGItems.COELECANTH_COOKED.get(),0.35f,600,"cooked_coelecanth");

        itemSmoking(exporter,List.of(BGItems.COELECANTH.get()),RecipeCategory.FOOD,BGItems.COELECANTH_COOKED.get(),0.35f,100,"cooked_coelecanth");

        oreSmelting(exporter,List.of(BGItems.POULTRY.get()),RecipeCategory.FOOD,BGItems.COOKED_POULTRY.get(),0.35f,200,"cooked_poultry");

        itemCampfire(exporter,List.of(BGItems.POULTRY.get()),RecipeCategory.FOOD,BGItems.COOKED_POULTRY.get(),0.35f,600,"cooked_poultry");

        itemSmoking(exporter,List.of(BGItems.POULTRY.get()),RecipeCategory.FOOD,BGItems.COOKED_POULTRY.get(),0.35f,100,"cooked_poultry");

        oreSmelting(exporter,List.of(BGItems.PRIMORDIAL_FISH.get()),RecipeCategory.FOOD,BGItems.COOKED_PRIMORDIAL_FISH.get(),0.35f,200,"cooked_primordial_fish");

        itemCampfire(exporter,List.of(BGItems.PRIMORDIAL_FISH.get()),RecipeCategory.FOOD,BGItems.COOKED_PRIMORDIAL_FISH.get(),0.35f,600,"cooked_primordial_fish");

        itemSmoking(exporter,List.of(BGItems.PRIMORDIAL_FISH.get()),RecipeCategory.FOOD,BGItems.COOKED_PRIMORDIAL_FISH.get(),0.35f,100,"cooked_primordial_fish");

        oreBlasting(exporter,List.of(BGBlocks.BYSLATE_COPPER_ORE.get(), BGBlocks.BYSTONE_COPPER_ORE.get()),RecipeCategory.MISC,Items.COPPER_INGOT,0.1f,100,"copper_ingot_from_bygone_stones");
        oreSmelting(exporter,List.of(BGBlocks.BYSLATE_COPPER_ORE.get(), BGBlocks.BYSTONE_COPPER_ORE.get()),RecipeCategory.MISC,Items.COPPER_INGOT,0.1f,200,"copper_ingot_from_bygone_stones");

        oreSmelting(exporter,List.of(BGBlocks.SHELLSTONE_BRICKS.get()),RecipeCategory.BUILDING_BLOCKS,BGBlocks.CRACKED_SHELLSTONE_BRICKS.get(),0.1f,200,"cracked_shellstone_bricks_from_shelstone_bricks");
        stonecutterResultFromBase(exporter,RecipeCategory.BUILDING_BLOCKS,BGBlocks.CRACKED_SHELLSTONE_BRICKS.get(),BGBlocks.SHELLSTONE_BRICKS.get(),1);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS,BGBlocks.CRYPTIC_MEGALITH_LANTERN.get(),1)
                .requires(BGBlocks.CRYPTIC_MEGALITH_FACE.get())
                .requires(BGItems.LITHOPLASM.get())
                .unlockedBy(getHasName(BGItems.LITHOPLASM.get()), has(BGItems.LITHOPLASM.get()))
                .save(exporter, ResourceLocation.parse(getSimpleRecipeName(BGBlocks.CRYPTIC_MEGALITH_LANTERN.get())));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS,BGBlocks.CUT_AMBER_SANDSTONE.get(),1)
                .pattern("SS")
                .pattern("SS")
                .define('S', BGBlocks.AMBER_SANDSTONE.get())
                .unlockedBy(getHasName(BGBlocks.AMBER_SANDSTONE.get()), has(BGBlocks.AMBER_SANDSTONE.get()))
                .save(exporter, ResourceLocation.parse(getSimpleRecipeName(BGBlocks.CUT_AMBER_SANDSTONE.get())));

        stonecutterResultFromBase(exporter,RecipeCategory.BUILDING_BLOCKS,BGBlocks.CUT_AMBER_SANDSTONE.get(),BGBlocks.AMBER_SANDSTONE.get(),1);
        slabRecipe(exporter,BGBlocks.CUT_AMBER_SANDSTONE.get(),BGBlocks.CUT_AMBER_SANDSTONE_SLAB.get());

        stonecutterResultFromBase(exporter,RecipeCategory.BUILDING_BLOCKS,BGBlocks.CUT_AMBER_SANDSTONE_SLAB.get(),BGBlocks.CUT_AMBER_SANDSTONE.get(),1);

        for (var entry : COLOR_TO_AMPHORA.entrySet()) {
            DyeColor color = entry.getKey();
            Block result = entry.getValue().get();

            Item dye = DyeItem.byColor(color);

            ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, result, 1)
                    .requires(Ingredient.of(ALL_AMPHORAS))
                    .requires(dye, 1)
                    .unlockedBy(getHasName(BGBlocks.AMPHORA.get()), has(BGBlocks.AMPHORA.get()))
                    .save(exporter, ResourceLocation.fromNamespaceAndPath(
                            "bygone",
                            "dye_" + color.getName() + "_amphora"
                    ));
        }



        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS,BGItems.ECHO_GONG.get(),1)
                .pattern(" S ")
                .pattern("SXS")
                .pattern(" S ")
                .define('S', Items.ECHO_SHARD)
                .define('X', BGItems.VERDIGRIS_INGOT.get())
                .unlockedBy(getHasName(BGItems.VERDIGRIS_INGOT.get()), has(BGItems.VERDIGRIS_INGOT.get()));


        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT,BGItems.EXOTIC_ARROW.get(),4)
                .pattern(" X ")
                .pattern(" # ")
                .pattern(" Y ")
                .define('X', Items.STICK)
                .define('#', Items.FLINT)
                .define('Y', BGItems.EXOTIC_PLUMAGE.get())
                .unlockedBy(getHasName(BGItems.EXOTIC_PLUMAGE.get()), has(BGItems.EXOTIC_PLUMAGE.get()))
                .save(exporter, ResourceLocation.parse(getSimpleRecipeName(BGItems.EXOTIC_ARROW.get())));

        oreSmelting(exporter,List.of(BGBlocks.AMBER_SAND.get()),RecipeCategory.BUILDING_BLOCKS,Blocks.GLASS,0.1f,200,"glass_from_amber_sand");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD,BGItems.GOURD_DANGO.get(),3)
                        .requires(BGItems.BEIGE_GOURD_SEEDS.get(),1)
                                .requires(BGItems.MUAVE_GOURD_SEEDS.get(),1)
                                        .requires(BGItems.VERDANT_GOURD_SEEDS.get(),1)
                                                .unlockedBy(getHasName(BGItems.BEIGE_GOURD_SEEDS.get()), has(BGItems.BEIGE_GOURD_SEEDS.get()))
                                                        .save(exporter, ResourceLocation.parse(getSimpleRecipeName(BGItems.GOURD_DANGO.get())));


        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD,BGItems.GOURD_SOUP.get(),1)
                        .requires(BGItems.BEIGE_SLICE.get(),1)
                                .requires(BGItems.MUAVE_SLICE.get(),1)
                                        .requires(BGItems.VERDANT_SLICE.get(),1)
                                                .requires(Items.BOWL,1)
                                                        .unlockedBy(getHasName(BGItems.BEIGE_SLICE.get()), has(BGItems.BEIGE_SLICE.get()))
                                                                .save(exporter, ResourceLocation.parse(getSimpleRecipeName(BGItems.GOURD_SOUP.get())));


        oreSmelting(exporter,List.of(BGItems.GOLD_BIG_BEAK_ARMOR.get()),RecipeCategory.MISC,Items.GOLD_NUGGET,0.1f,200,"gold_nugget_from_gold_big_beak_armor");
        oreBlasting(exporter,List.of(BGItems.GOLD_BIG_BEAK_ARMOR.get()),RecipeCategory.MISC,Items.GOLD_NUGGET,0.1f,100,"gold_nugget_from_gold_big_beak_armor");

        for (var entry : COLOR_TO_GLOW_CONCRETE.entrySet()) {
            DyeColor color = entry.getKey();
            ItemLike result = entry.getValue().get();

            Item dye = DyeItem.byColor(color);

            glowConcrete(exporter, result, dye);
        }

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS,BGBlocks.HOLLOW_MEGALITH_LANTERN.get(),1)
                .requires(BGBlocks.HOLLOW_MEGALITH_FACE.get())
                .requires(BGItems.LITHOPLASM.get())
                .unlockedBy(getHasName(BGItems.LITHOPLASM.get()), has(BGItems.LITHOPLASM.get()))
                .save(exporter, ResourceLocation.parse(getSimpleRecipeName(BGBlocks.HOLLOW_MEGALITH_LANTERN.get())));

        oreSmelting(exporter,List.of(BGBlocks.BYSLATE_IRON_ORE.get(),BGBlocks.BYSTONE_IRON_ORE.get()),RecipeCategory.MISC,Items.IRON_INGOT,0.1f,200,"iron_ingot_from_bygone_stones");
        oreBlasting(exporter,List.of(BGBlocks.BYSLATE_IRON_ORE.get(),BGBlocks.BYSTONE_IRON_ORE.get()),RecipeCategory.MISC,Items.IRON_INGOT,0.1f,100,"iron_ingot_from_bygone_stones");

        oreSmelting(exporter,List.of(BGItems.IRON_BIG_BEAK_ARMOR.get()),RecipeCategory.MISC,Items.IRON_NUGGET,0.1f,200,"iron_nugget_from_iron_big_beak_armor");
        oreBlasting(exporter,List.of(BGItems.IRON_BIG_BEAK_ARMOR.get()),RecipeCategory.MISC,Items.IRON_NUGGET,0.1f,100,"iron_nugget_from_iron_big_beak_armor");

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE,Blocks.LEVER,1)
                .pattern("X")
                .pattern("#")
                .define('X', Ingredient.of(ItemTags.STONE_CRAFTING_MATERIALS))
                .define('#', Items.STICK)
                .unlockedBy(getHasName(Items.STICK), has(Items.STICK))
                .save(exporter, ResourceLocation.parse(getSimpleRecipeName(Blocks.LEVER.asItem())));



        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,Items.WHITE_DYE,1)
                .requires(BGBlocks.MONTSECHIA.get(),1)
                .unlockedBy(getHasName(BGBlocks.MONTSECHIA.get()), has(BGBlocks.MONTSECHIA.get()))
                .save(exporter, ResourceLocation.fromNamespaceAndPath(Bygone.MOD_ID, "white_dye_from_montsechia"));


        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,Items.LIGHT_GRAY_DYE,1)
                .requires(BGBlocks.MOON_BLOSSOM.get(),1)
                .unlockedBy(getHasName(BGBlocks.MOON_BLOSSOM.get()), has(BGBlocks.MOON_BLOSSOM.get()))
                .save(exporter, ResourceLocation.fromNamespaceAndPath(Bygone.MOD_ID, "light_gray_dye_from_moon_blossom"));


        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS,BGBlocks.LIT_CRACKED_MEGALITH_BLOCK.get(),1)
                .requires(BGBlocks.CRACKED_MEGALITH_BLOCK.get(),1)
                .requires(BGItems.LITHOPLASM.get(),1)
                .unlockedBy(getHasName(BGBlocks.CRACKED_MEGALITH_BLOCK.get()), has(BGBlocks.CRACKED_MEGALITH_BLOCK.get()))
                .save(exporter, ResourceLocation.parse(getSimpleRecipeName(BGBlocks.LIT_CRACKED_MEGALITH_BLOCK.get())));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE,BGBlocks.LITHINE_LAMP.get(),1)
                .pattern(" R ")
                .pattern("RGR")
                .pattern(" R ")
                .define('G', Items.GLOWSTONE_DUST)
                .define('R', BGItems.LITHOPLASM.get())
                .unlockedBy(getHasName(BGItems.LITHOPLASM.get()), has(BGItems.LITHOPLASM.get()))
                .save(exporter, ResourceLocation.parse(getSimpleRecipeName(BGBlocks.LITHINE_LAMP.get())));


        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS,BGBlocks.MALACHITE_CHISELED.get(),1)
                .pattern("X")
                .pattern("X")
                .define('X', BGBlocks.MALACHITE_SLAB.get())
                .unlockedBy(getHasName(BGBlocks.MALACHITE_SLAB.get()), has(BGBlocks.MALACHITE_SLAB.get()))
                .save(exporter, ResourceLocation.parse(getSimpleRecipeName(BGBlocks.MALACHITE_CHISELED.get())));

        stonecutterResultFromBase(exporter,RecipeCategory.BUILDING_BLOCKS, BGBlocks.MALACHITE_CHISELED.get(), BGBlocks.MALACHITE.get(),2);

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE,BGBlocks.MALACHITE_DOOR.get(),1)
                .pattern("XX")
                .pattern("XX")
                .pattern("XX")
                .define('X', BGBlocks.MALACHITE.get())
                .unlockedBy(getHasName(BGBlocks.MALACHITE.get()), has(BGBlocks.MALACHITE.get()))
                .save(exporter, ResourceLocation.parse(getSimpleRecipeName(BGBlocks.MALACHITE_DOOR.get())));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS,BGBlocks.MALACHITE_PILLAR.get(),2)
                .pattern("X")
                .pattern("X")
                .define('X', BGBlocks.MALACHITE.get())
                .unlockedBy(getHasName(BGBlocks.MALACHITE.get()), has(BGBlocks.MALACHITE.get()))
                .save(exporter, ResourceLocation.parse(getSimpleRecipeName(BGBlocks.MALACHITE_PILLAR.get())));

        stonecutterResultFromBase(exporter,RecipeCategory.BUILDING_BLOCKS,BGBlocks.MALACHITE_PILLAR.get(),BGBlocks.MALACHITE.get(),1);
        slabRecipe(exporter,BGBlocks.MALACHITE.get(),BGBlocks.MALACHITE_SLAB.get());
        stonecutterResultFromBase(exporter,RecipeCategory.BUILDING_BLOCKS,BGBlocks.MALACHITE_SLAB.get(),BGBlocks.MALACHITE.get(),1);


        stairsRecipe(exporter,BGBlocks.MALACHITE.get(),BGBlocks.MALACHITE_STAIRS.get());
        stonecutterResultFromBase(exporter,RecipeCategory.BUILDING_BLOCKS,BGBlocks.MALACHITE_STAIRS.get(),BGBlocks.MALACHITE.get(),1);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS,BGBlocks.MALACHITE_TILE.get(),4)
                        .pattern("XX")
                                .pattern("XX")
                                        .define('X', BGBlocks.MALACHITE.get())
                                                .unlockedBy(getHasName(BGBlocks.MALACHITE.get()), has(BGBlocks.MALACHITE.get()))
                                                        .save(exporter, ResourceLocation.parse(getSimpleRecipeName(BGBlocks.MALACHITE_TILE.get())));
        stonecutterResultFromBase(exporter,RecipeCategory.BUILDING_BLOCKS,BGBlocks.MALACHITE_TILE.get(),BGBlocks.MALACHITE.get(),1);

        slabRecipe(exporter,BGBlocks.MALACHITE_TILE.get(),BGBlocks.MALACHITE_TILE_SLAB.get());
        stonecutterResultFromBase(exporter,RecipeCategory.BUILDING_BLOCKS,BGBlocks.MALACHITE_TILE_SLAB.get(),BGBlocks.MALACHITE_TILE.get(),1);

        stairsRecipe(exporter,BGBlocks.MALACHITE_TILE.get(),BGBlocks.MALACHITE_TILE_STAIRS.get());
        stonecutterResultFromBase(exporter,RecipeCategory.BUILDING_BLOCKS,BGBlocks.MALACHITE_TILE_STAIRS.get(),BGBlocks.MALACHITE_TILE.get(),1);

        wallsRecipe(exporter,BGBlocks.MALACHITE.get(),BGBlocks.MALACHITE_WALL.get());
        stonecutterResultFromBase(exporter,RecipeCategory.BUILDING_BLOCKS,BGBlocks.MALACHITE_WALL.get(),BGBlocks.MALACHITE.get(),1);


        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS,BGBlocks.MEGALITH_LANTERN.get(),1)
                .requires(BGBlocks.MEGALITH_FACE.get())
                .requires(BGItems.LITHOPLASM.get())
                .unlockedBy(getHasName(BGItems.LITHOPLASM.get()), has(BGItems.LITHOPLASM.get()))
                .save(exporter, ResourceLocation.parse(getSimpleRecipeName(BGBlocks.MEGALITH_LANTERN.get())));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,BGBlocks.MEGALITH_LANTERN.get(),1)
                .pattern(" L ")
                .pattern("LBL")
                .pattern(" L ")
                .define('L', BGBlocks.MEGALITH_BLOCK.get())
                .define('B', BGItems.LITHOPLASM.get())
                .unlockedBy(getHasName(BGItems.LITHOPLASM.get()), has(BGItems.LITHOPLASM.get()))
                .save(exporter, ResourceLocation.parse(getSimpleRecipeName(BGBlocks.MEGALITH_LANTERN.get()) + "_alt"));

        bricksRecipe(exporter,BGBlocks.ORANGE_MUSHROOM_BLOCK.get(),BGBlocks.ORANGE_FUNGAL_BRICKS.get());
        stonecutterResultFromBase(exporter,RecipeCategory.BUILDING_BLOCKS,BGBlocks.ORANGE_FUNGAL_SLAB.get(),BGBlocks.ORANGE_FUNGAL_BRICKS.get(),2);
        stonecutterResultFromBase(exporter,RecipeCategory.BUILDING_BLOCKS,BGBlocks.ORANGE_FUNGAL_STAIRS.get(),BGBlocks.ORANGE_FUNGAL_BRICKS.get(),1);
        stonecutterResultFromBase(exporter,RecipeCategory.BUILDING_BLOCKS,BGBlocks.ORANGE_FUNGAL_WALL.get(),BGBlocks.ORANGE_FUNGAL_BRICKS.get(),1);

        slabRecipe(exporter,BGBlocks.ORANGE_FUNGAL_BRICKS.get(),BGBlocks.ORANGE_FUNGAL_SLAB.get());
        stairsRecipe(exporter,BGBlocks.ORANGE_FUNGAL_BRICKS.get(),BGBlocks.ORANGE_FUNGAL_STAIRS.get());
        wallsRecipe(exporter,BGBlocks.ORANGE_FUNGAL_BRICKS.get(),BGBlocks.ORANGE_FUNGAL_WALL.get());

        bricksRecipe(exporter,BGBlocks.PINK_MUSHROOM_BLOCK.get(),BGBlocks.PINK_FUNGAL_BRICKS.get());
        stonecutterResultFromBase(exporter,RecipeCategory.BUILDING_BLOCKS,BGBlocks.PINK_FUNGAL_SLAB.get(),BGBlocks.PINK_FUNGAL_BRICKS.get(),2);
        stonecutterResultFromBase(exporter,RecipeCategory.BUILDING_BLOCKS,BGBlocks.PINK_FUNGAL_STAIRS.get(),BGBlocks.PINK_FUNGAL_BRICKS.get(),1);
        stonecutterResultFromBase(exporter,RecipeCategory.BUILDING_BLOCKS,BGBlocks.PINK_FUNGAL_WALL.get(),BGBlocks.PINK_FUNGAL_BRICKS.get(),1);

        slabRecipe(exporter,BGBlocks.PINK_FUNGAL_BRICKS.get(),BGBlocks.PINK_FUNGAL_SLAB.get());
        stairsRecipe(exporter,BGBlocks.PINK_FUNGAL_BRICKS.get(),BGBlocks.PINK_FUNGAL_STAIRS.get());
        wallsRecipe(exporter,BGBlocks.PINK_FUNGAL_BRICKS.get(),BGBlocks.PINK_FUNGAL_WALL.get());

        polished(exporter,RecipeCategory.BUILDING_BLOCKS,BGBlocks.BYSLATE.get(),BGBlocks.POLISHED_BYSLATE.get());

        bricksRecipe(exporter,BGBlocks.BYSLATE.get(),BGBlocks.POLISHED_BYSLATE_BRICK.get());

        stonecutterResultFromBase(exporter,RecipeCategory.BUILDING_BLOCKS,BGBlocks.POLISHED_BYSLATE_BRICK_SLAB.get(),BGBlocks.POLISHED_BYSLATE_BRICK.get(),2);
        stonecutterResultFromBase(exporter,RecipeCategory.BUILDING_BLOCKS,BGBlocks.POLISHED_BYSLATE_BRICK_STAIRS.get(),BGBlocks.POLISHED_BYSLATE_BRICK.get(),1);
        stonecutterResultFromBase(exporter,RecipeCategory.BUILDING_BLOCKS,BGBlocks.POLISHED_BYSLATE_BRICK_WALL.get(),BGBlocks.POLISHED_BYSLATE_BRICK.get(),1);

        slabRecipe(exporter,BGBlocks.POLISHED_BYSLATE_BRICK.get(),BGBlocks.POLISHED_BYSLATE_BRICK_SLAB.get());
        stairsRecipe(exporter,BGBlocks.POLISHED_BYSLATE_BRICK.get(),BGBlocks.POLISHED_BYSLATE_BRICK_STAIRS.get());
        wallsRecipe(exporter,BGBlocks.POLISHED_BYSLATE_BRICK.get(),BGBlocks.POLISHED_BYSLATE_BRICK_WALL.get());

        stonecutterResultFromBase(exporter,RecipeCategory.BUILDING_BLOCKS,BGBlocks.CHISELED_POLISHED_BYSLATE.get(),BGBlocks.POLISHED_BYSLATE.get(),1);
        stonecutterResultFromBase(exporter,RecipeCategory.BUILDING_BLOCKS,BGBlocks.POLISHED_BYSLATE_BRICK.get(),BGBlocks.POLISHED_BYSLATE.get(),1);
        stonecutterResultFromBase(exporter,RecipeCategory.BUILDING_BLOCKS,BGBlocks.POLISHED_BYSLATE_SHINGLES.get(),BGBlocks.POLISHED_BYSLATE.get(),1);
        stonecutterResultFromBase(exporter,RecipeCategory.BUILDING_BLOCKS,BGBlocks.POLISHED_BYSLATE_SLAB.get(),BGBlocks.POLISHED_BYSLATE.get(),2);
        stonecutterResultFromBase(exporter,RecipeCategory.BUILDING_BLOCKS,BGBlocks.POLISHED_BYSLATE_STAIRS.get(),BGBlocks.POLISHED_BYSLATE.get(),1);
        stonecutterResultFromBase(exporter,RecipeCategory.BUILDING_BLOCKS,BGBlocks.POLISHED_BYSLATE_WALL.get(),BGBlocks.POLISHED_BYSLATE.get(),1);
        polished(exporter,RecipeCategory.BUILDING_BLOCKS,BGBlocks.BYSTONE.get(),BGBlocks.POLISHED_BYSTONE.get());
        bricksRecipe(exporter,BGBlocks.POLISHED_BYSLATE_BRICK.get(),BGBlocks.POLISHED_BYSLATE.get());

        slabRecipe(exporter, BGBlocks.POLISHED_BYSLATE.get(), BGBlocks.POLISHED_BYSLATE_SLAB.get());
        stairsRecipe(exporter, BGBlocks.POLISHED_BYSLATE.get(), BGBlocks.POLISHED_BYSLATE_STAIRS.get());
        wallsRecipe(exporter, BGBlocks.POLISHED_BYSLATE.get(), BGBlocks.POLISHED_BYSLATE_WALL.get());

        polished(exporter,RecipeCategory.BUILDING_BLOCKS,BGBlocks.POLISHED_BYSTONE.get(),BGBlocks.BYSTONE.get());
        bricksRecipe(exporter,BGBlocks.POLISHED_BYSTONE.get(),BGBlocks.POLISHED_BYSTONE_BRICK.get());

        stonecutterResultFromBase(exporter,RecipeCategory.BUILDING_BLOCKS,BGBlocks.POLISHED_BYSTONE_BRICK_SLAB.get(),BGBlocks.POLISHED_BYSTONE_BRICK.get(),2);





        stonecutterResultFromBase(exporter,RecipeCategory.BUILDING_BLOCKS,BGBlocks.CHISELED_POLISHED_BYSTONE.get(),BGBlocks.POLISHED_BYSTONE.get(),1);
        stonecutterResultFromBase(exporter,RecipeCategory.BUILDING_BLOCKS,BGBlocks.POLISHED_BYSTONE_BRICK.get(),BGBlocks.POLISHED_BYSTONE.get(),1);
        stonecutterResultFromBase(exporter,RecipeCategory.BUILDING_BLOCKS,BGBlocks.POLISHED_BYSTONE_SHINGLES.get(),BGBlocks.POLISHED_BYSTONE.get(),1);
        stonecutterResultFromBase(exporter,RecipeCategory.BUILDING_BLOCKS,BGBlocks.POLISHED_BYSTONE_SLAB.get(),BGBlocks.POLISHED_BYSTONE.get(),1);
        stonecutterResultFromBase(exporter,RecipeCategory.BUILDING_BLOCKS,BGBlocks.POLISHED_BYSTONE_STAIRS.get(),BGBlocks.POLISHED_BYSTONE.get(),1);
        stonecutterResultFromBase(exporter,RecipeCategory.BUILDING_BLOCKS,BGBlocks.POLISHED_BYSTONE_WALL.get(),BGBlocks.POLISHED_BYSTONE.get(),1);





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
        */
    }

    private static final Map<DyeColor, Supplier<Block>> COLOR_TO_AMPHORA =
            Map.ofEntries(
                    Map.entry(DyeColor.WHITE, BGBlocks.WHITE_AMPHORA),
                    Map.entry(DyeColor.ORANGE, BGBlocks.ORANGE_AMPHORA),
                    Map.entry(DyeColor.MAGENTA, BGBlocks.MAGENTA_AMPHORA),
                    Map.entry(DyeColor.LIGHT_BLUE, BGBlocks.LIGHT_BLUE_AMPHORA),
                    Map.entry(DyeColor.YELLOW, BGBlocks.YELLOW_AMPHORA),
                    Map.entry(DyeColor.LIME, BGBlocks.LIME_AMPHORA),
                    Map.entry(DyeColor.PINK, BGBlocks.PINK_AMPHORA),
                    Map.entry(DyeColor.GRAY, BGBlocks.GRAY_AMPHORA),
                    Map.entry(DyeColor.LIGHT_GRAY, BGBlocks.LIGHT_GRAY_AMPHORA),
                    Map.entry(DyeColor.CYAN, BGBlocks.CYAN_AMPHORA),
                    Map.entry(DyeColor.PURPLE, BGBlocks.PURPLE_AMPHORA),
                    Map.entry(DyeColor.BLUE, BGBlocks.BLUE_AMPHORA),
                    Map.entry(DyeColor.BROWN, BGBlocks.BROWN_AMPHORA),
                    Map.entry(DyeColor.GREEN, BGBlocks.GREEN_AMPHORA),
                    Map.entry(DyeColor.RED, BGBlocks.RED_AMPHORA),
                    Map.entry(DyeColor.BLACK, BGBlocks.BLACK_AMPHORA)
            );


    private static final Block[] ALL_AMPHORAS = new Block[] {
            BGBlocks.AMPHORA.get(),
            BGBlocks.WHITE_AMPHORA.get(),
            BGBlocks.ORANGE_AMPHORA.get(),
            BGBlocks.MAGENTA_AMPHORA.get(),
            BGBlocks.LIGHT_BLUE_AMPHORA.get(),
            BGBlocks.YELLOW_AMPHORA.get(),
            BGBlocks.LIME_AMPHORA.get(),
            BGBlocks.PINK_AMPHORA.get(),
            BGBlocks.GRAY_AMPHORA.get(),
            BGBlocks.LIGHT_GRAY_AMPHORA.get(),
            BGBlocks.CYAN_AMPHORA.get(),
            BGBlocks.PURPLE_AMPHORA.get(),
            BGBlocks.BLUE_AMPHORA.get(),
            BGBlocks.BROWN_AMPHORA.get(),
            BGBlocks.GREEN_AMPHORA.get(),
            BGBlocks.RED_AMPHORA.get(),
            BGBlocks.BLACK_AMPHORA.get()
    };

    private static final Map<DyeColor, Supplier<Block>> COLOR_TO_GLOW_CONCRETE =
            Map.ofEntries(
                    Map.entry(DyeColor.BROWN, BGBlocks.BROWN_GLOW_CONCRETE_POWDER),
                    Map.entry(DyeColor.CYAN, BGBlocks.CYAN_GLOW_CONCRETE_POWDER),
                    Map.entry(DyeColor.GRAY, BGBlocks.GRAY_GLOW_CONCRETE_POWDER),
                    Map.entry(DyeColor.GREEN, BGBlocks.GREEN_GLOW_CONCRETE_POWDER),
                    Map.entry(DyeColor.LIGHT_BLUE, BGBlocks.LIGHT_BLUE_GLOW_CONCRETE_POWDER),
                    Map.entry(DyeColor.LIGHT_GRAY, BGBlocks.LIGHT_GRAY_GLOW_CONCRETE_POWDER),
                    Map.entry(DyeColor.LIME, BGBlocks.LIME_GLOW_CONCRETE_POWDER),
                    Map.entry(DyeColor.MAGENTA, BGBlocks.MAGENTA_GLOW_CONCRETE_POWDER),
                    Map.entry(DyeColor.ORANGE, BGBlocks.ORANGE_GLOW_CONCRETE_POWDER),
                    Map.entry(DyeColor.PINK, BGBlocks.PINK_GLOW_CONCRETE_POWDER),
                    Map.entry(DyeColor.PURPLE, BGBlocks.PURPLE_GLOW_CONCRETE_POWDER),
                    Map.entry(DyeColor.RED, BGBlocks.RED_GLOW_CONCRETE_POWDER),
                    Map.entry(DyeColor.WHITE, BGBlocks.WHITE_GLOW_CONCRETE_POWDER),
                    Map.entry(DyeColor.YELLOW, BGBlocks.YELLOW_GLOW_CONCRETE_POWDER)
            );



    protected static void glowConcrete(RecipeOutput recipeOutput, ItemLike glowConcrete, ItemLike dye) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, glowConcrete, 8).requires(dye).requires(BGBlocks.SHELLSTONE.get(), 4).requires(BGBlocks.GLOW_GRAVEL.get(), 4).group("glow_concrete").unlockedBy(getHasName(BGBlocks.SHELLSTONE.get()), has(BGBlocks.SHELLSTONE.get())).unlockedBy(getHasName(BGBlocks.GLOW_GRAVEL.get()), has(BGBlocks.GLOW_GRAVEL.get())).save(recipeOutput);
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

    void chiseledRecipe(RecipeOutput exporter, RecipeCategory recipeCategory ,Block input, Block output) {
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


    protected static void itemCampfire(RecipeOutput recipeOutput, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult,
                                      float pExperience, int pCookingTime, String pGroup) {
        oreCooking(recipeOutput, RecipeSerializer.CAMPFIRE_COOKING_RECIPE, CampfireCookingRecipe::new, pIngredients, pCategory, pResult,
                pExperience, pCookingTime, pGroup, "_from_campfire_cooking");
    }


    protected static void itemSmoking(RecipeOutput recipeOutput, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult,
                                      float pExperience, int pCookingTime, String pGroup) {
        oreCooking(recipeOutput, RecipeSerializer.SMOKING_RECIPE, SmokingRecipe::new, pIngredients, pCategory, pResult,
                pExperience, pCookingTime, pGroup, "_from_smoking");
    }

    protected static <T extends AbstractCookingRecipe> void oreCooking(RecipeOutput recipeOutput, RecipeSerializer<T> pCookingSerializer, AbstractCookingRecipe.Factory<T> factory,
                                                                       List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup, String pRecipeName) {
        for(ItemLike itemlike : pIngredients) {
            SimpleCookingRecipeBuilder.generic(Ingredient.of(itemlike), pCategory, pResult, pExperience, pCookingTime, pCookingSerializer, factory).group(pGroup).unlockedBy(getHasName(itemlike), has(itemlike))
                    .save(recipeOutput, Bygone.MOD_ID + ":" + getItemName(pResult) + pRecipeName + "_" + getItemName(itemlike));
        }
    }
}
