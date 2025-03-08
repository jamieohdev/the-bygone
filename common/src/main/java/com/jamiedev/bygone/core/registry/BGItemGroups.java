package com.jamiedev.bygone.core.registry;

import com.jamiedev.bygone.Bygone;
import com.kekecreations.jinxedlib.core.util.JinxedRegistryHelper;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public class BGItemGroups {

    public static final Supplier<CreativeModeTab> EXAMPLE_TAB = registerTab("test", () -> CreativeModeTab.builder(null, -1)
            .icon(() -> new ItemStack(BGBlocks.SHELF_FUNGUS.get()))
            .displayItems((itemDisplayParameters, output) -> {

                output.accept(BGItems.ANCIENT_SIGN.get());
                            /*
                            output.accept(BGItems.ANCIENT_HANGING_SIGN);
                            output.accept(BGItems.ORANGE_FUNGI);
                            output.accept(BGItems.PINK_FUNGI);
                            output.accept(BGItems.PURPLE_FUNGI);
                            output.accept(BGItems.EXOTIC_ARROW);
                            output.accept(BGItems.EXOTIC_PLUMAGE);
                            output.accept(BGItems.COELECANTH);
                            output.accept(BGItems.COELECANTH_COOKED);
                            output.accept(BGItems.BEIGE_SLICE);
                            output.accept(BGItems.MUAVE_SLICE);
                            output.accept(BGItems.VERDANT_SLICE);
                            output.accept(BGItems.GOURD_DANGO);
                            output.accept(BGItems.GOURD_SOUP);
                            output.accept(BGItems.AMARANTH_SEEDS);
                            output.accept(BGItems.AMARANTH_GRAIN);
                            output.accept(BGItems.AMARANTH_LOAF);
                            output.accept(BGItems.CHANTRELLE_SEEDS);
                            output.accept(BGItems.CHANTRELLE);
                            output.accept(BGItems.PLAGA_SEEDS);
                            output.accept(BGItems.PLAGA);
                            output.accept(BGItems.BIG_BEAK_SPAWN_EGG);
                            output.accept(BGItems.COELACANTH_SPAWN_EGG);
                            output.accept(BGItems.COPPERBUG_SPAWN_EGG);
                            output.accept(BGItems.FUNGALPARENT_SPAWN_EGG);
                            output.accept(BGItems.MOOBOO_SPAWN_EGG);
                            output.accept(BGItems.PEST_SPAWN_EGG);
                            output.accept(BGItems.SCUTTLE_SPAWN_EGG);
                            output.accept(BGItems.TRILOBITE_SPAWN_EGG);
                            output.accept(BGItems.ARCANE_CORE);
                            output.accept(BGItems.HOOK);
                            output.accept(BGItems.GOLD_BIG_BEAK_ARMOR);
                            output.accept(BGItems.IRON_BIG_BEAK_ARMOR);
                            output.accept(BGItems.DIAMOND_BIG_BEAK_ARMOR);
                            output.accept(BGItems.VERDIGRIS_SCRAP);
                            output.accept(BGItems.VERDIGRIS_INGOT);
                            output.accept(BGItems.VERDIGRIS_BLADE);
                            output.accept(BGItems.VERDIGRIS_BOW);


                            output.accept(BGBlocks.BYGONE_PORTAL_FRAME_BLOCK);
                            output.accept(BGBlocks.BYGONESTONE_IRON_ORE);
                            output.accept(BGBlocks.BYGONESLATE_IRON_ORE);
                            output.accept(BGBlocks.BYGONESTONE_COAL_ORE);
                            output.accept(BGBlocks.BYGONESLATE_COAL_ORE);
                            output.accept(BGBlocks.BYGONESTONE_COPPER_ORE);
                            output.accept(BGBlocks.BYGONESLATE_COPPER_ORE);
                            
                            output.accept(BGBlocks.BYSTONE);
                            output.accept(BGBlocks.COBBLED_BYSTONE);
                            output.accept(BGBlocks.COBBLED_BYSTONE_STAIRS);
                            output.accept(BGBlocks.COBBLED_BYSTONE_SLAB);
                            output.accept(BGBlocks.COBBLED_BYSTONE_WALL);
                            output.accept(BGBlocks.POLISHED_BYSTONE.get());
                            output.accept(BGBlocks.POLISHED_BYSTONE_STAIRS);
                            output.accept(BGBlocks.POLISHED_BYSTONE_SLAB);
                            output.accept(BGBlocks.POLISHED_BYSTONE_WALL);
                            output.accept(BGBlocks.POLISHED_BYSTONE_BRICK);
                            output.accept(BGBlocks.POLISHED_BYSTONE_BRICK_STAIRS);
                            output.accept(BGBlocks.POLISHED_BYSTONE_BRICK_SLAB);
                            output.accept(BGBlocks.POLISHED_BYSTONE_BRICK_WALL);
                            output.accept(BGBlocks.POLISHED_BYSTONE_SHINGLES);
                            output.accept(BGBlocks.CHISELED_POLISHED_BYSTONE);
                            
                            
                            output.accept(BGBlocks.BYSLATE);
                            output.accept(BGBlocks.COBBLED_BYSLATE);
                            output.accept(BGBlocks.COBBLED_BYSLATE_STAIRS);
                            output.accept(BGBlocks.COBBLED_BYSLATE_SLAB);
                            output.accept(BGBlocks.COBBLED_BYSLATE_WALL);
                            output.accept(BGBlocks.POLISHED_BYSLATE);
                            output.accept(BGBlocks.POLISHED_BYSLATE_STAIRS);
                            output.accept(BGBlocks.POLISHED_BYSLATE_SLAB);
                            output.accept(BGBlocks.POLISHED_BYSLATE_WALL);
                            output.accept(BGBlocks.POLISHED_BYSLATE_BRICK);
                            output.accept(BGBlocks.POLISHED_BYSLATE_BRICK_STAIRS);
                            output.accept(BGBlocks.POLISHED_BYSLATE_BRICK_SLAB);
                            output.accept(BGBlocks.POLISHED_BYSLATE_BRICK_WALL);
                            output.accept(BGBlocks.POLISHED_BYSLATE_SHINGLES);
                            output.accept(BGBlocks.CHISELED_POLISHED_BYSLATE);
                            
                            output.accept(BGBlocks.CLOUD);

                            // Start of the Ancient Forests unique Blocks

                            output.accept(BGBlocks.SHORT_GRASS);

                            output.accept(BGBlocks.MONTSECHIA);
                            output.accept(BGBlocks.SAGARIA);
                            output.accept(BGBlocks.RAFFLESIA);
                            output.accept(BGBlocks.GOURD_LANTERN_VERDANT);
                            output.accept(BGBlocks.GOURD_LANTERN_BEIGE);
                            output.accept(BGBlocks.GOURD_LANTERN_MUAVE);
                            output.accept(BGBlocks.BIG_WHIRLIWEED);
                            output.accept(BGBlocks.WHIRLIWEED);
                            output.accept(BGBlocks.CLAYSTONE);
                            output.accept(BGBlocks.COARSE_CLAYSTONE);
                            output.accept(BGBlocks.CLAYSTONE_BRICKS);

                            output.accept(BGBlocks.CLAYSTONE_BRICKS_STAIRS);

                            output.accept(BGBlocks.CLAYSTONE_BRICKS_SLAB);
                            output.accept(BGBlocks.CLAYSTONE_BRICKS_WALL);
                            output.accept(BGBlocks.MOSSY_CLAYSTONE);
                            output.accept(BGBlocks.ANCIENT_ROOTS);
                            output.accept(BGBlocks.ANCIENT_VINE);
                            output.accept(BGBlocks.ANCIENT_SAPLING);
                            output.accept(BGBlocks.ANCIENT_LOG);
                            output.accept(BGBlocks.ANCIENT_LEAVES);
                            output.accept(BGBlocks.ANCIENT_WOOD);
                            output.accept(BGBlocks.STRIPPED_ANCIENT_LOG);
                            output.accept(BGBlocks.STRIPPED_ANCIENT_WOOD);
                            output.accept(BGBlocks.ANCIENT_PLANKS);
                            output.accept(BGBlocks.ANCIENT_STAIRS);
                            output.accept(BGBlocks.ANCIENT_SLAB);
                            output.accept(BGBlocks.ANCIENT_FENCE);
                            output.accept(BGBlocks.ANCIENT_FENCE_GATE);
                            output.accept(BGBlocks.ANCIENT_DOOR);
                            output.accept(BGBlocks.ANCIENT_TRAPDOOR);
                            output.accept(BGBlocks.ANCIENT_PRESSURE_PLATE);
                            output.accept(BGBlocks.ANCIENT_BUTTON);
                            output.accept(BGBlocks.ALPHA_MOSS_CARPET);
                            output.accept(BGBlocks.ALPHA_MOSS_BLOCK);
                            output.accept(BGBlocks.ALPHA_MOSSY_CLAYSTONE);

                            output.accept(BGBlocks.CASTER);

// Start of the Primordial Ocean blockset

                            output.accept(BGBlocks.BLUE_ALGAE);

                            output.accept(BGBlocks.OCEANSTONE);

                            output.accept(BGBlocks.PRIMORDIAL_SAND);

                            output.accept(BGBlocks.GLOW_GRAVEL);

                            output.accept(BGBlocks.MALACHITE);
                            output.accept(BGBlocks.MALACHITE_CHISELED);
                            output.accept(BGBlocks.MALACHITE_PILLAR);
                            output.accept(BGBlocks.MALACHITE_TILE);
                            output.accept(BGBlocks.MALACHITE_DOOR);
                            output.accept(BGBlocks.MALACHITE_STAIRS);
                            output.accept(BGBlocks.MALACHITE_SLAB);
                            output.accept(BGBlocks.MALACHITE_WALL);
                            output.accept(BGBlocks.MALACHITE_TILE_STAIRS);
                            output.accept(BGBlocks.MALACHITE_TILE_SLAB);
                            output.accept(BGBlocks.MALACHITE_TILE_WALL);
                            output.accept(BGBlocks.PRIMORDIAL_VENT);
                            output.accept(BGBlocks.PRIMORDIAL_VENTSTONE);
                            output.accept(BGBlocks.CRINOID);
                            output.accept(BGBlocks.PRIMORDIAL_URCHIN);
                            output.accept(BGBlocks.DEAD_ORANGE_CORAL_BLOCK);
                            output.accept(BGBlocks.ORANGE_CORAL_BLOCK);
                            output.accept(BGBlocks.DEAD_ORANGE_CORAL);
                            output.accept(BGBlocks.ORANGE_CORAL);
                            output.accept(BGBlocks.DEAD_ORANGE_CORAL_FAN);
                            output.accept(BGBlocks.ORANGE_CORAL_FAN);
                            output.accept(BGBlocks.DEAD_BLUE_CORAL_BLOCK);
                            output.accept(BGBlocks.BLUE_CORAL_BLOCK);
                            output.accept(BGBlocks.DEAD_BLUE_CORAL);
                            output.accept(BGBlocks.BLUE_CORAL);
                            output.accept(BGBlocks.DEAD_BLUE_CORAL_FAN);
                            output.accept(BGBlocks.BLUE_CORAL_FAN);
                            output.accept(BGBlocks.CHARNIA);


// Start of Blemish biome blocksets!
                            output.accept(BGBlocks.BLEMISH);
                            output.accept(BGBlocks.BLEMISH_CATALYST);
                            output.accept(BGBlocks.BLEMISH_VEIN);


// Start of Amber Desert blocksets!
                            output.accept(BGBlocks.UMBER);
                            output.accept(BGBlocks.UMBER_BRICKS);
                            output.accept(BGBlocks.AMBER);

                            output.accept(BGBlocks.AMBER_BRICKS);
                            output.accept(BGBlocks.FLOWING_AMBER);
                            output.accept(BGBlocks.COBBLED_AMBER);

                            output.accept(BGBlocks.AMBERSTONE);
                            output.accept(BGBlocks.COBBLED_AMBERSTONE);
                            output.accept(BGBlocks.POINTED_AMBER);
                            output.accept(BGBlocks.CREOSOTE);
                            output.accept(BGBlocks.CREOSOTE_SPROUTS);

// Farming / Agriculture blocksets
                            output.accept(BGBlocks.AMARANTH_BLOCK);
                            output.accept(BGBlocks.SPRINKER);
                            output.accept(BGBlocks.BELLADONNA);
                            output.accept(BGBlocks.COLEUS);


// Shelf Hollows blocksets

                            output.accept(BGBlocks.PROTOTAXITE_STEM);
                            output.accept(BGBlocks.SHELF_MYCELIUM);
                            output.accept(BGBlocks.SHELF_MOLD_BLOCK);
                            output.accept(BGBlocks.SHELF_MOLD);
                            output.accept(BGBlocks.SHELF_MOLD_MOSS);
                            output.accept(BGBlocks.ORANGE_MUSHROOM_BLOCK);
                            output.accept(BGBlocks.PINK_MUSHROOM_BLOCK);
                            output.accept(BGBlocks.PURPLE_MUSHROOM_BLOCK);
                            output.accept(BGBlocks.ORANGE_FUNGAL_BRICKS);
                            output.accept(BGBlocks.ORANGE_FUNGAL_STAIRS);
                            output.accept(BGBlocks.ORANGE_FUNGAL_SLAB);
                            output.accept(BGBlocks.ORANGE_FUNGAL_WALL);
                            output.accept(BGBlocks.PINK_FUNGAL_BRICKS);
                            output.accept(BGBlocks.PINK_FUNGAL_STAIRS);
                            output.accept(BGBlocks.PINK_FUNGAL_SLAB);
                            output.accept(BGBlocks.PINK_FUNGAL_WALL);
                            output.accept(BGBlocks.PURPLE_FUNGAL_BRICKS);
                            output.accept(BGBlocks.PURPLE_FUNGAL_STAIRS);
                            output.accept(BGBlocks.PURPLE_FUNGAL_SLAB);
                            output.accept(BGBlocks.PURPLE_FUNGAL_WALL);
                            output.accept(BGBlocks.SHELF_ROOTS);
                            output.accept(BGBlocks.SHELF_SPROUTS);

                             */

            })
            .title(Component.translatable("itemGroup.test"))
            .build());

    public static Supplier<CreativeModeTab> registerTab(String name, Supplier<CreativeModeTab> supplier) {
        return JinxedRegistryHelper.register(BuiltInRegistries.CREATIVE_MODE_TAB, Bygone.MOD_ID, name, supplier);
    }

    public static void register() {}

}
