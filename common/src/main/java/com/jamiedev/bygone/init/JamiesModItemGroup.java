package com.jamiedev.bygone.init;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.block.*;
import com.jamiedev.bygone.block.gourds.GourdDangoBlock;
import com.jamiedev.bygone.block.gourds.GourdDangoWallBlock;
import com.jamiedev.bygone.block.gourds.GourdLanternBlock;
import com.jamiedev.bygone.block.gourds.GourdVineBlock;
import com.jamiedev.bygone.block.shelf.*;
import net.kyrptonaught.customportalapi.CustomPortalBlock;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

import java.util.Optional;

import static net.minecraft.world.level.block.Blocks.DIRT;

public class JamiesModItemGroup
{
   // public static final RegistryKey<ItemGroup> JAMIES_MOD = RegistryKey.of(RegistryKeys.ITEM_GROUP, BygoneFabric.getModId("bygone"));
    public static final ResourceKey<CreativeModeTab> JAMIES_MOD = ResourceKey.create(Registries.CREATIVE_MODE_TAB, Bygone.id("test"));

    public static void registerItemgroups() {

        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, JAMIES_MOD, CreativeModeTab.builder(null,-1)
                .icon(() -> new ItemStack(JamiesModBlocks.SHELF_FUNGUS))
                        .displayItems((itemDisplayParameters, output) -> {
                            output.accept( JamiesModItems.ANCIENT_SIGN);
                            output.accept(JamiesModItems.ANCIENT_HANGING_SIGN);
                            output.accept(JamiesModItems.ORANGE_FUNGI);
                            output.accept(JamiesModItems.PINK_FUNGI);
                            output.accept(JamiesModItems.PURPLE_FUNGI);
                            output.accept(JamiesModItems.EXOTIC_ARROW);
                            output.accept(JamiesModItems.EXOTIC_PLUMAGE);
                            output.accept(JamiesModItems.COELECANTH);
                            output.accept(JamiesModItems.COELECANTH_COOKED);
                            output.accept(JamiesModItems.BEIGE_SLICE);
                            output.accept(JamiesModItems.MUAVE_SLICE);
                            output.accept(JamiesModItems.VERDANT_SLICE);
                            output.accept(JamiesModItems.GOURD_DANGO);
                            output.accept(JamiesModItems.GOURD_SOUP);
                            output.accept(JamiesModItems.AMARANTH_SEEDS);
                            output.accept(JamiesModItems.AMARANTH_GRAIN);
                            output.accept(JamiesModItems.AMARANTH_LOAF);
                            output.accept(JamiesModItems.CHANTRELLE_SEEDS);
                            output.accept(JamiesModItems.CHANTRELLE);
                            output.accept(JamiesModItems.PLAGA_SEEDS);
                            output.accept(JamiesModItems.PLAGA);
                            output.accept(JamiesModItems.BIG_BEAK_SPAWN_EGG);
                            output.accept(JamiesModItems.COELACANTH_SPAWN_EGG);
                            output.accept(JamiesModItems.COPPERBUG_SPAWN_EGG);
                            output.accept(JamiesModItems.FUNGALPARENT_SPAWN_EGG);
                            output.accept(JamiesModItems.MOOBOO_SPAWN_EGG);
                            output.accept(JamiesModItems.SCUTTLE_SPAWN_EGG);
                            output.accept(JamiesModItems.TRILOBITE_SPAWN_EGG);
                            output.accept(JamiesModItems.ARCANE_CORE);
                            output.accept(JamiesModItems.HOOK);
                            output.accept(JamiesModItems.GOLD_BIG_BEAK_ARMOR);
                            output.accept(JamiesModItems.IRON_BIG_BEAK_ARMOR);
                            output.accept(JamiesModItems.DIAMOND_BIG_BEAK_ARMOR);
                            output.accept(JamiesModItems.VERDIGRIS_SCRAP);
                            output.accept(JamiesModItems.VERDIGRIS_INGOT);
                            output.accept(JamiesModItems.VERDIGRIS_BLADE);
                            output.accept(JamiesModItems.VERDIGRIS_BOW);


                            output.accept(JamiesModBlocks.BYGONE_PORTAL_FRAME_BLOCK);
                            output.accept(JamiesModBlocks.BYGONESTONE_IRON_ORE);
                            output.accept(JamiesModBlocks.BYGONESLATE_IRON_ORE);
                            output.accept(JamiesModBlocks.BYGONESTONE_COAL_ORE);
                            output.accept(JamiesModBlocks.BYGONESLATE_COAL_ORE);
                            output.accept(JamiesModBlocks.BYGONESTONE_COPPER_ORE);
                            output.accept(JamiesModBlocks.BYGONESLATE_COPPER_ORE);
                            output.accept(JamiesModBlocks.LIMBOSTONE);
                            output.accept(JamiesModBlocks.LIMBOSLATE);
                            output.accept(JamiesModBlocks.COBBLED_BYSTONE);
                            output.accept(JamiesModBlocks.COBBLED_BYSTONE_STAIRS);
                            output.accept(JamiesModBlocks.COBBLED_BYSTONE_SLAB);
                            output.accept(JamiesModBlocks.COBBLED_BYSTONE_WALL);
                            output.accept(JamiesModBlocks.COBBLED_BYSLATE);
                            output.accept(JamiesModBlocks.COBBLED_BYSLATE_STAIRS);
                            output.accept(JamiesModBlocks.COBBLED_BYSLATE_SLAB);
                            output.accept(JamiesModBlocks.COBBLED_BYSLATE_WALL);
                            output.accept(JamiesModBlocks.CLOUD);

                            // Start of the Ancient Forests unique Blocks

                            output.accept(JamiesModBlocks.SHORT_GRASS);

                            output.accept(JamiesModBlocks.MONTSECHIA);
                            output.accept(JamiesModBlocks.SAGARIA);
                            output.accept(JamiesModBlocks.RAFFLESIA);
                            output.accept(JamiesModBlocks.GOURD_LANTERN_VERDANT);
                            output.accept(JamiesModBlocks.GOURD_LANTERN_BEIGE);
                            output.accept(JamiesModBlocks.GOURD_LANTERN_MUAVE);
                            output.accept(JamiesModBlocks.BIG_WHIRLIWEED);
                            output.accept(JamiesModBlocks.WHIRLIWEED);
                            output.accept(JamiesModBlocks.CLAYSTONE);
                            output.accept(JamiesModBlocks.COARSE_CLAYSTONE);
                            output.accept(JamiesModBlocks.CLAYSTONE_BRICKS);

                            output.accept(JamiesModBlocks.CLAYSTONE_BRICKS_STAIRS);

                            output.accept(JamiesModBlocks.CLAYSTONE_BRICKS_SLAB);
                            output.accept(JamiesModBlocks.CLAYSTONE_BRICKS_WALL);
                            output.accept(JamiesModBlocks.MOSSY_CLAYSTONE);
                            output.accept(JamiesModBlocks.ANCIENT_ROOTS);
                            output.accept(JamiesModBlocks.ANCIENT_VINE);
                            output.accept(JamiesModBlocks.ANCIENT_SAPLING);
                            output.accept(JamiesModBlocks.ANCIENT_LOG);
                            output.accept(JamiesModBlocks.ANCIENT_LEAVES);
                            output.accept(JamiesModBlocks.ANCIENT_WOOD);
                            output.accept(JamiesModBlocks.STRIPPED_ANCIENT_LOG);
                            output.accept(JamiesModBlocks.STRIPPED_ANCIENT_WOOD);
                            output.accept(JamiesModBlocks.ANCIENT_PLANKS);
                            output.accept(JamiesModBlocks.ANCIENT_STAIRS);
                            output.accept(JamiesModBlocks.ANCIENT_SLAB);
                            output.accept(JamiesModBlocks.ANCIENT_FENCE);
                            output.accept(JamiesModBlocks.ANCIENT_FENCE_GATE);
                            output.accept(JamiesModBlocks.ANCIENT_DOOR);
                            output.accept(JamiesModBlocks.ANCIENT_TRAPDOOR);
                            output.accept(JamiesModBlocks.ANCIENT_PRESSURE_PLATE);
                            output.accept(JamiesModBlocks.ANCIENT_BUTTON);
                            output.accept(JamiesModBlocks.ALPHA_MOSS_CARPET);
                            output.accept(JamiesModBlocks.ALPHA_MOSS_BLOCK);
                            output.accept(JamiesModBlocks.ALPHA_MOSSY_CLAYSTONE);

                            output.accept(JamiesModBlocks.CASTER);

// Start of the Primordial Ocean blockset

                            output.accept(JamiesModBlocks.BLUE_ALGAE);

                            output.accept(JamiesModBlocks.OCEANSTONE);

                            output.accept(JamiesModBlocks.PRIMORDIAL_SAND);

                            output.accept(JamiesModBlocks.GLOW_GRAVEL);

                            output.accept(JamiesModBlocks.MALACHITE);
                            output.accept(JamiesModBlocks.MALACHITE_CHISELED);
                            output.accept(JamiesModBlocks.MALACHITE_PILLAR);
                            output.accept(JamiesModBlocks.MALACHITE_TILE);
                            output.accept(JamiesModBlocks.MALACHITE_DOOR);
                            output.accept(JamiesModBlocks.MALACHITE_STAIRS);
                            output.accept(JamiesModBlocks.MALACHITE_SLAB);
                            output.accept(JamiesModBlocks.MALACHITE_WALL);
                            output.accept(JamiesModBlocks.MALACHITE_TILE_STAIRS);
                            output.accept(JamiesModBlocks.MALACHITE_TILE_SLAB);
                            output.accept(JamiesModBlocks.MALACHITE_TILE_WALL);
                            output.accept(JamiesModBlocks.PRIMORDIAL_VENT);
                            output.accept(JamiesModBlocks.PRIMORDIAL_VENTSTONE);
                            output.accept(JamiesModBlocks.CRINOID);
                            output.accept(JamiesModBlocks.PRIMORDIAL_URCHIN);
                            output.accept(JamiesModBlocks.DEAD_ORANGE_CORAL_BLOCK);
                            output.accept(JamiesModBlocks.ORANGE_CORAL_BLOCK);
                            output.accept(JamiesModBlocks.DEAD_ORANGE_CORAL);
                            output.accept(JamiesModBlocks.ORANGE_CORAL);
                            output.accept(JamiesModBlocks.DEAD_ORANGE_CORAL_FAN);
                            output.accept(JamiesModBlocks.ORANGE_CORAL_FAN);
                            output.accept(JamiesModBlocks.DEAD_BLUE_CORAL_BLOCK);
                            output.accept(JamiesModBlocks.BLUE_CORAL_BLOCK);
                            output.accept(JamiesModBlocks.DEAD_BLUE_CORAL);
                            output.accept(JamiesModBlocks.BLUE_CORAL);
                            output.accept(JamiesModBlocks.DEAD_BLUE_CORAL_FAN);
                            output.accept(JamiesModBlocks.BLUE_CORAL_FAN);
                            output.accept(JamiesModBlocks.CHARNIA);


// Start of Blemish biome blocksets!
                            output.accept(JamiesModBlocks.BLEMISH);
                            output.accept(JamiesModBlocks.BLEMISH_CATALYST);
                            output.accept(JamiesModBlocks.BLEMISH_VEIN);


// Start of Amber Desert blocksets!
                            output.accept(JamiesModBlocks.UMBER);
                            output.accept(JamiesModBlocks.UMBER_BRICKS);
                            output.accept(JamiesModBlocks.AMBER);

                            output.accept(JamiesModBlocks.AMBER_BRICKS);
                            output.accept(JamiesModBlocks.FLOWING_AMBER);
                            output.accept(JamiesModBlocks.COBBLED_AMBER);

                            output.accept(JamiesModBlocks.AMBERSTONE);
                            output.accept(JamiesModBlocks.COBBLED_AMBERSTONE);
                            output.accept(JamiesModBlocks.POINTED_AMBER);
                            output.accept(JamiesModBlocks.CREOSOTE);
                            output.accept(JamiesModBlocks.CREOSOTE_SPROUTS);

// Farming / Agriculture blocksets
                            output.accept(JamiesModBlocks.AMARANTH_BLOCK);
                            output.accept(JamiesModBlocks.SPRINKER);
                            output.accept(JamiesModBlocks.BELLADONNA);
                            output.accept(JamiesModBlocks.COLEUS);


// Shelf Hollows blocksets

                            output.accept(JamiesModBlocks.PROTOTAXITE_STEM);
                            output.accept(JamiesModBlocks.SHELF_MYCELIUM);
                            output.accept(JamiesModBlocks.SHELF_MOLD_BLOCK);
                            output.accept(JamiesModBlocks.SHELF_MOLD);
                            output.accept(JamiesModBlocks.SHELF_MOLD_MOSS);
                            output.accept(JamiesModBlocks.ORANGE_MUSHROOM_BLOCK);
                            output.accept(JamiesModBlocks.PINK_MUSHROOM_BLOCK);
                            output.accept(JamiesModBlocks.PURPLE_MUSHROOM_BLOCK);
                            output.accept(JamiesModBlocks.ORANGE_FUNGAL_BRICKS);
                            output.accept(JamiesModBlocks.ORANGE_FUNGAL_STAIRS);
                            output.accept(JamiesModBlocks.ORANGE_FUNGAL_SLAB);
                            output.accept(JamiesModBlocks.ORANGE_FUNGAL_WALL);
                            output.accept(JamiesModBlocks.PINK_FUNGAL_BRICKS);
                            output.accept(JamiesModBlocks.PINK_FUNGAL_STAIRS);
                            output.accept(JamiesModBlocks.PINK_FUNGAL_SLAB);
                            output.accept(JamiesModBlocks.PINK_FUNGAL_WALL);
                            output.accept(JamiesModBlocks.PURPLE_FUNGAL_BRICKS);
                            output.accept(JamiesModBlocks.PURPLE_FUNGAL_STAIRS);
                            output.accept(JamiesModBlocks.PURPLE_FUNGAL_SLAB);
                            output.accept(JamiesModBlocks.PURPLE_FUNGAL_WALL);
                            output.accept(JamiesModBlocks.SHELF_ROOTS);
                            output.accept(JamiesModBlocks.SHELF_SPROUTS);
                            
                        })
                .title(Component.translatable("itemGroup.test"))
                .build());
    }
}
