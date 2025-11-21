package com.jamiedev.bygone.core.registry;

import com.jamiedev.bygone.Bygone;
import com.kekecreations.jinxedlib.core.util.JinxedRegistryHelper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public class BGItemGroups {

    public static final Supplier<CreativeModeTab> EXAMPLE_TAB = registerTab(
            "test", () -> CreativeModeTab.builder(null, -1)
                    .icon(() -> new ItemStack(BGBlocks.SABLE_LOG.get()))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(BGItems.ANCIENT_SIGN.get());
                        output.accept(BGItems.ANCIENT_HANGING_SIGN.get());
                        output.accept(BGItems.SABLE_SIGN.get());
                        output.accept(BGItems.SABLE_HANGING_SIGN.get());

                        output.accept(BGItems.EXOTIC_ARROW.get());
                        output.accept(BGItems.EXOTIC_PLUMAGE.get());
                        output.accept(BGItems.COELECANTH.get());
                        output.accept(BGItems.COELECANTH_COOKED.get());
                        output.accept(BGItems.COELECANTH_BUCKET.get());
                        output.accept(BGItems.AMOEBA_GEL.get());
                        output.accept(BGItems.AMOEBA_BUCKET.get());
                        output.accept(BGItems.BEIGE_SLICE.get());
                        output.accept(BGItems.MUAVE_SLICE.get());
                        output.accept(BGItems.VERDANT_SLICE.get());
                        output.accept(BGItems.GOURD_DANGO.get());
                        output.accept(BGItems.GOURD_SOUP.get());
                        output.accept(BGItems.AMARANTH_SEEDS.get());
                        output.accept(BGItems.AMARANTH_GRAIN.get());
                        output.accept(BGItems.AMARANTH_LOAF.get());
                        output.accept(BGBlocks.AMARANTH_BLOCK.get());
                        output.accept(BGItems.CHANTRELLE_SEEDS.get());
                        output.accept(BGItems.CHANTRELLE.get());
                        output.accept(BGItems.PLAGA_SEEDS.get());
                        output.accept(BGItems.PLAGA.get());
                        output.accept(BGItems.SPEED_WHEAT.get());
                        output.accept(BGBlocks.SPRINKER.get());
                        output.accept(BGItems.ECTOPLASM_BUCKET.get());
                        output.accept(BGBlocks.GUMBO_POT.get());
                        output.accept(BGItems.AMOEBA_SPAWN_EGG.get());
                        output.accept(BGItems.AQUIFAWN_SPAWN_EGG.get());
                        output.accept(BGItems.BIG_BEAK_SPAWN_EGG.get());
                        output.accept(BGItems.COELACANTH_SPAWN_EGG.get());
                        output.accept(BGItems.COPPERBUG_SPAWN_EGG.get());
                        output.accept(BGItems.FUNGALPARENT_SPAWN_EGG.get());
                        output.accept(BGItems.LITHY_SPAWN_EGG.get());
                        output.accept(BGItems.MOOBOO_SPAWN_EGG.get());
                        output.accept(BGItems.NECTAUR_SPAWN_EGG.get());
                        output.accept(BGItems.PESKY_SPAWN_EGG.get());
                        output.accept(BGItems.PEST_SPAWN_EGG.get());
                        output.accept(BGItems.SABEAST_SPAWN_EGG.get());
                        output.accept(BGItems.SCUTTLE_SPAWN_EGG.get());
                        output.accept(BGItems.TRILOBITE_SPAWN_EGG.get());
                        output.accept(BGItems.WHISKBILL_SPAWN_EGG.get());
                        output.accept(BGItems.WRAITH_SPAWN_EGG.get());
                        output.accept(BGItems.ARCANE_CORE.get());
                        output.accept(BGItems.HOOK.get());
                        output.accept(BGItems.MALICIOUS_WAR_HORN.get());
                        output.accept(BGItems.ECHO_GONG.get());
                        output.accept(BGItems.GOLD_BIG_BEAK_ARMOR.get());
                        output.accept(BGItems.IRON_BIG_BEAK_ARMOR.get());
                        output.accept(BGItems.DIAMOND_BIG_BEAK_ARMOR.get());

                        output.accept(BGItems.VERDIGRIS_SCRAP.get());
                        output.accept(BGItems.VERDIGRIS_INGOT.get());
                        output.accept(BGBlocks.VERDIGRIS_SCRAP_BLOCK.get());
                        output.accept(BGBlocks.VERDIGRIS_BLOCK.get());
                        output.accept(BGItems.VERDIGRIS_BLADE.get());
                        output.accept(BGItems.VERDIGRIS_BOW.get());
                        output.accept(BGBlocks.BROKEN_VERDIGRIS_COG.get());
                        output.accept(BGBlocks.RAMSHACKLED_VERDIGRIS_COG.get());
                        output.accept(BGBlocks.TARNISHED_VERDIGRIS_COG.get());
                        output.accept(BGBlocks.PRISTINE_VERDIGRIS_COG.get());


                        output.accept(BGBlocks.BYGONE_PORTAL_FRAME_BLOCK.get());
                        output.accept(BGBlocks.BYGONESTONE_IRON_ORE.get());
                        output.accept(BGBlocks.BYGONESLATE_IRON_ORE.get());
                        output.accept(BGBlocks.BYGONESTONE_COAL_ORE.get());
                        output.accept(BGBlocks.BYGONESLATE_COAL_ORE.get());
                        output.accept(BGBlocks.BYGONESTONE_COPPER_ORE.get());
                        output.accept(BGBlocks.BYGONESLATE_COPPER_ORE.get());

                        output.accept(BGBlocks.BYSTONE.get());
                        output.accept(BGBlocks.COBBLED_BYSTONE.get());
                        output.accept(BGBlocks.COBBLED_BYSTONE_STAIRS.get());
                        output.accept(BGBlocks.COBBLED_BYSTONE_SLAB.get());
                        output.accept(BGBlocks.COBBLED_BYSTONE_WALL.get());
                        output.accept(BGBlocks.POLISHED_BYSTONE.get());
                        output.accept(BGBlocks.POLISHED_BYSTONE_STAIRS.get());
                        output.accept(BGBlocks.POLISHED_BYSTONE_SLAB.get());
                        output.accept(BGBlocks.POLISHED_BYSTONE_WALL.get());
                        output.accept(BGBlocks.POLISHED_BYSTONE_BRICK.get());
                        output.accept(BGBlocks.POLISHED_BYSTONE_BRICK_STAIRS.get());
                        output.accept(BGBlocks.POLISHED_BYSTONE_BRICK_SLAB.get());
                        output.accept(BGBlocks.POLISHED_BYSTONE_BRICK_WALL.get());
                        output.accept(BGBlocks.POLISHED_BYSTONE_SHINGLES.get());
                        output.accept(BGBlocks.CHISELED_POLISHED_BYSTONE.get());


                        output.accept(BGBlocks.BYSLATE.get());
                        output.accept(BGBlocks.COBBLED_BYSLATE.get());
                        output.accept(BGBlocks.COBBLED_BYSLATE_STAIRS.get());
                        output.accept(BGBlocks.COBBLED_BYSLATE_SLAB.get());
                        output.accept(BGBlocks.COBBLED_BYSLATE_WALL.get());
                        output.accept(BGBlocks.POLISHED_BYSLATE.get());
                        output.accept(BGBlocks.POLISHED_BYSLATE_STAIRS.get());
                        output.accept(BGBlocks.POLISHED_BYSLATE_SLAB.get());
                        output.accept(BGBlocks.POLISHED_BYSLATE_WALL.get());
                        output.accept(BGBlocks.POLISHED_BYSLATE_BRICK.get());
                        output.accept(BGBlocks.POLISHED_BYSLATE_BRICK_STAIRS.get());
                        output.accept(BGBlocks.POLISHED_BYSLATE_BRICK_SLAB.get());
                        output.accept(BGBlocks.POLISHED_BYSLATE_BRICK_WALL.get());
                        output.accept(BGBlocks.POLISHED_BYSLATE_SHINGLES.get());
                        output.accept(BGBlocks.CHISELED_POLISHED_BYSLATE.get());

                        output.accept(BGBlocks.CLOUD.get());

                        // Start of the Ancient Forests unique Blocks

                        output.accept(BGBlocks.SHORT_GRASS.get());

                        output.accept(BGBlocks.MONTSECHIA.get());
                        output.accept(BGBlocks.SAGARIA.get());
                        output.accept(BGBlocks.RAFFLESIA.get());
                        output.accept(BGBlocks.GOURD_VINE.get());
                        output.accept(BGBlocks.GOURD_LANTERN_VERDANT.get());
                        output.accept(BGBlocks.GOURD_LANTERN_BEIGE.get());
                        output.accept(BGBlocks.GOURD_LANTERN_MUAVE.get());
                        output.accept(BGBlocks.BIG_WHIRLIWEED.get());
                        output.accept(BGBlocks.WHIRLIWEED.get());
                        output.accept(BGItems.WHIRLIWEED_BUNDLE.get());
                        output.accept(BGBlocks.CLAYSTONE.get());
                        output.accept(BGBlocks.SUSPICIOUS_CLAYSTONE.get());
                        output.accept(BGBlocks.CLAYSTONE_FARMLAND.get());
                        output.accept(BGBlocks.COARSE_CLAYSTONE.get());
                        output.accept(BGBlocks.CLAYSTONE_BRICKS.get());

                        output.accept(BGBlocks.CLAYSTONE_BRICKS_STAIRS.get());

                        output.accept(BGBlocks.CLAYSTONE_BRICKS_SLAB.get());
                        output.accept(BGBlocks.CLAYSTONE_BRICKS_WALL.get());
                        output.accept(BGBlocks.MOSSY_CLAYSTONE.get());
                        output.accept(BGBlocks.ANCIENT_ROOTS.get());
                        output.accept(BGBlocks.ANCIENT_VINE.get());
                        output.accept(BGBlocks.ANCIENT_SAPLING.get());
                        output.accept(BGBlocks.ANCIENT_LOG.get());
                        output.accept(BGBlocks.ANCIENT_LEAVES.get());
                        output.accept(BGBlocks.ANCIENT_WOOD.get());
                        output.accept(BGBlocks.STRIPPED_ANCIENT_LOG.get());
                        output.accept(BGBlocks.STRIPPED_ANCIENT_WOOD.get());
                        output.accept(BGBlocks.ANCIENT_PLANKS.get());
                        output.accept(BGBlocks.ANCIENT_STAIRS.get());
                        output.accept(BGBlocks.ANCIENT_SLAB.get());
                        output.accept(BGBlocks.ANCIENT_FENCE.get());
                        output.accept(BGBlocks.ANCIENT_FENCE_GATE.get());
                        output.accept(BGBlocks.ANCIENT_DOOR.get());
                        output.accept(BGBlocks.ANCIENT_TRAPDOOR.get());
                        output.accept(BGBlocks.ANCIENT_PRESSURE_PLATE.get());
                        output.accept(BGBlocks.ANCIENT_BUTTON.get());
                        output.accept(BGBlocks.ALPHA_MOSS_CARPET.get());
                        output.accept(BGBlocks.ALPHA_MOSS_BLOCK.get());
                        output.accept(BGBlocks.ALPHA_MOSSY_CLAYSTONE.get());


                        // Start of the Primordial Ocean blockset

                        output.accept(BGBlocks.BLUE_ALGAE.get());
                        output.accept(BGBlocks.CREOSOTE.get());
                        output.accept(BGBlocks.CREOSOTE_SPROUTS.get());
                        output.accept(BGBlocks.BELLADONNA.get());
                        output.accept(BGBlocks.COLEUS.get());

                        output.accept(BGBlocks.SHELLSAND.get());
                        output.accept(BGBlocks.SUSPICIOUS_SHELLSAND.get());
                        output.accept(BGBlocks.SEAGLASS.get());
                        output.accept(BGBlocks.SEAGLASS_PANE.get());
                        output.accept(BGBlocks.COBBLED_SEAGLASS.get());
                        output.accept(BGBlocks.SHELLSTONE.get());
                        output.accept(BGBlocks.SHELLSTONE_STAIRS.get());
                        output.accept(BGBlocks.SHELLSTONE_SLAB.get());
                        output.accept(BGBlocks.SHELLSTONE_WALL.get());
                        output.accept(BGBlocks.SHELLSTONE_BRICKS.get());
                        output.accept(BGBlocks.SHELLSTONE_PILLAR.get());
                        output.accept(BGBlocks.CRACKED_SHELLSTONE_BRICKS.get());
                        output.accept(BGBlocks.CHISELED_SHELLSTONE_BRICKS.get());
                        output.accept(BGBlocks.SHELLSTONE_BRICKS_STAIRS.get());
                        output.accept(BGBlocks.SHELLSTONE_BRICKS_SLAB.get());
                        output.accept(BGBlocks.SHELLSTONE_BRICKS_WALL.get());

                        output.accept(BGBlocks.GLOW_GRAVEL.get());

                        output.accept(BGBlocks.MALACHITE.get());
                        output.accept(BGBlocks.MALACHITE_CHISELED.get());
                        output.accept(BGBlocks.MALACHITE_PILLAR.get());
                        output.accept(BGBlocks.MALACHITE_TILE.get());
                        output.accept(BGBlocks.MALACHITE_DOOR.get());
                        output.accept(BGBlocks.MALACHITE_STAIRS.get());
                        output.accept(BGBlocks.MALACHITE_SLAB.get());
                        output.accept(BGBlocks.MALACHITE_WALL.get());
                        output.accept(BGBlocks.MALACHITE_TILE_STAIRS.get());
                        output.accept(BGBlocks.MALACHITE_TILE_SLAB.get());
                        output.accept(BGBlocks.MALACHITE_TILE_WALL.get());
                        output.accept(BGBlocks.PRIMORDIAL_VENT.get());
                        output.accept(BGBlocks.PRIMORDIAL_VENTSTONE.get());
                        output.accept(BGBlocks.CRINOID.get());
                        output.accept(BGBlocks.PRIMORDIAL_URCHIN.get());
                        output.accept(BGBlocks.DEAD_RUGOSA_CORAL_BLOCK.get());
                        output.accept(BGBlocks.RUGOSA_CORAL_BLOCK.get());
                        output.accept(BGBlocks.DEAD_RUGOSA_CORAL.get());
                        output.accept(BGBlocks.RUGOSA_CORAL.get());
                        output.accept(BGItems.DEAD_RUGOSA_CORAL_FAN.get());
                        output.accept(BGItems.RUGOSA_CORAL_FAN.get());
                        output.accept(BGBlocks.DEAD_TABULATA_CORAL_BLOCK.get());
                        output.accept(BGBlocks.TABULATA_CORAL_BLOCK.get());
                        output.accept(BGBlocks.DEAD_TABULATA_CORAL.get());
                        output.accept(BGBlocks.TABULATA_CORAL.get());
                        output.accept(BGItems.DEAD_TABULATA_CORAL_FAN.get());
                        output.accept(BGItems.TABULATA_CORAL_FAN.get());
                        output.accept(BGBlocks.DEAD_PILLAR_CORAL_BLOCK.get());
                        output.accept(BGBlocks.PILLAR_CORAL_BLOCK.get());
                        output.accept(BGBlocks.DEAD_PILLAR_CORAL.get());
                        output.accept(BGBlocks.PILLAR_CORAL.get());
                        output.accept(BGItems.DEAD_PILLAR_CORAL_FAN.get());
                        output.accept(BGItems.PILLAR_CORAL_FAN.get());
                        output.accept(BGBlocks.DEAD_THAMNOPORA_CORAL_BLOCK.get());
                        output.accept(BGBlocks.THAMNOPORA_CORAL_BLOCK.get());
                        output.accept(BGBlocks.DEAD_THAMNOPORA_CORAL.get());
                        output.accept(BGBlocks.THAMNOPORA_CORAL.get());
                        output.accept(BGItems.DEAD_THAMNOPORA_CORAL_FAN.get());
                        output.accept(BGItems.THAMNOPORA_CORAL_FAN.get());
                        output.accept(BGBlocks.CHARNIA.get());


                        // Start of Blemish biome blocksets!
                        output.accept(BGBlocks.BLEMISH.get());
                        output.accept(BGBlocks.BLEMISH_CATALYST.get());
                        output.accept(BGBlocks.BLEMISH_VEIN.get());


                        // Start of Amber Desert blocksets!
                        output.accept(BGBlocks.AMBER_SAND.get());
                        output.accept(BGBlocks.AMBER_SANDSTONE.get());
                        output.accept(BGBlocks.AMBER_SANDSTONE_STAIRS.get());
                        output.accept(BGBlocks.AMBER_SANDSTONE_SLAB.get());
                        output.accept(BGBlocks.AMBER_SANDSTONE_WALL.get());
                        output.accept(BGBlocks.CUT_AMBER_SANDSTONE.get());
                        output.accept(BGBlocks.CUT_AMBER_SANDSTONE_SLAB.get());
                        output.accept(BGBlocks.SMOOTH_AMBER_SANDSTONE.get());
                        output.accept(BGBlocks.SMOOTH_AMBER_SANDSTONE_STAIRS.get());
                        output.accept(BGBlocks.SMOOTH_AMBER_SANDSTONE_SLAB.get());
                        output.accept(BGBlocks.CHISELED_AMBER_SANDSTONE.get());
                        output.accept(BGBlocks.AMBER.get());
                        output.accept(BGBlocks.AMBER_CLUMP.get());

                        output.accept(BGBlocks.AMBER_BRICKS.get());
                        output.accept(BGBlocks.FLOWING_AMBER.get());
                        output.accept(BGBlocks.COBBLED_AMBER.get());

                        output.accept(BGBlocks.AMBERSTONE.get());
                        output.accept(BGBlocks.COBBLED_AMBERSTONE.get());
                        output.accept(BGBlocks.POINTED_AMBER.get());

                        // Farming / Agriculture blocksets


                        // Shelf Hollows blocksets

                        output.accept(BGBlocks.PROTOTAXITE_STEM.get());
                        output.accept(BGBlocks.SHELF_MYCELIUM.get());
                        output.accept(BGBlocks.SHELF_MOLD_BLOCK.get());
                        output.accept(BGBlocks.SHELF_MOLD.get());
                        output.accept(BGBlocks.SHELF_MOLD_MOSS.get());
                        output.accept(BGBlocks.ORANGE_MUSHROOM_BLOCK.get());
                        output.accept(BGBlocks.ORANGE_FUNGAL_BRICKS.get());
                        output.accept(BGBlocks.ORANGE_FUNGAL_STAIRS.get());
                        output.accept(BGBlocks.ORANGE_FUNGAL_SLAB.get());
                        output.accept(BGBlocks.ORANGE_FUNGAL_WALL.get());
                        output.accept(BGItems.ORANGE_FUNGI.get());
                        output.accept(BGBlocks.PINK_MUSHROOM_BLOCK.get());
                        output.accept(BGBlocks.PINK_FUNGAL_BRICKS.get());
                        output.accept(BGBlocks.PINK_FUNGAL_STAIRS.get());
                        output.accept(BGBlocks.PINK_FUNGAL_SLAB.get());
                        output.accept(BGBlocks.PINK_FUNGAL_WALL.get());
                        output.accept(BGItems.PINK_FUNGI.get());
                        output.accept(BGBlocks.PURPLE_MUSHROOM_BLOCK.get());
                        output.accept(BGBlocks.PURPLE_FUNGAL_BRICKS.get());
                        output.accept(BGBlocks.PURPLE_FUNGAL_STAIRS.get());
                        output.accept(BGBlocks.PURPLE_FUNGAL_SLAB.get());
                        output.accept(BGBlocks.PURPLE_FUNGAL_WALL.get());
                        output.accept(BGItems.PURPLE_FUNGI.get());
                        output.accept(BGBlocks.SHELF_ROOTS.get());
                        output.accept(BGBlocks.SHELF_SPROUTS.get());

                        output.accept(BGBlocks.ICE_BOUQUET.get());
                        output.accept(BGItems.LITHOPLASM.get());
                        output.accept(BGBlocks.MEGALITH_BLOCK.get());
                        output.accept(BGBlocks.CRACKED_MEGALITH_BLOCK.get());
                        output.accept(BGBlocks.LIT_CRACKED_MEGALITH_BLOCK.get());
                        output.accept(BGBlocks.ANGRY_MEGALITH_FACE.get());
                        output.accept(BGBlocks.ANGRY_MEGALITH_LANTERN.get());
                        output.accept(BGBlocks.CRYPTIC_MEGALITH_FACE.get());
                        output.accept(BGBlocks.CRYPTIC_MEGALITH_LANTERN.get());
                        output.accept(BGBlocks.HOLLOW_MEGALITH_FACE.get());
                        output.accept(BGBlocks.HOLLOW_MEGALITH_LANTERN.get());
                        output.accept(BGBlocks.MEGALITH_FACE.get());
                        output.accept(BGBlocks.MEGALITH_LANTERN.get());
                        output.accept(BGBlocks.LITHINE_LAMP.get());
                        output.accept(BGBlocks.MEGALITH_TOTEM.get());
                        output.accept(BGBlocks.LITHINE_ORE.get());

                        output.accept(BGBlocks.SABLE_SAPLING.get());
                        output.accept(BGBlocks.SABLE_LOG.get());
                        output.accept(BGBlocks.SABLE_LEAVES.get());
                        output.accept(BGBlocks.SABLE_WOOD.get());
                        output.accept(BGBlocks.STRIPPED_SABLE_LOG.get());
                        output.accept(BGBlocks.STRIPPED_SABLE_WOOD.get());
                        output.accept(BGBlocks.SABLE_PLANKS.get());
                        output.accept(BGBlocks.SABLE_STAIRS.get());
                        output.accept(BGBlocks.SABLE_SLAB.get());
                        output.accept(BGBlocks.SABLE_FENCE.get());
                        output.accept(BGBlocks.SABLE_FENCE_GATE.get());
                        output.accept(BGBlocks.SABLE_DOOR.get());
                        output.accept(BGBlocks.SABLE_TRAPDOOR.get());
                        output.accept(BGBlocks.SABLE_PRESSURE_PLATE.get());
                        output.accept(BGBlocks.SABLE_BUTTON.get());

                        output.accept(BGBlocks.UMBRAL_SOIL.get());
                        output.accept(BGBlocks.WRIGGLING_SOIL.get());
                        output.accept(BGItems.BUCKET_O_BAITWORMS.get());
                        output.accept(BGItems.SABLE_BRANCH.get());
                        output.accept(BGItems.THORNY_SABLE_BRANCH.get());
                        output.accept(BGItems.SABLE_GRASS.get());
                        output.accept(BGItems.SABLOSSOM.get());
                        output.accept(BGBlocks.SABLENUT.get());
                        output.accept(BGItems.ROASTED_SABLE_NUT.get());
                        output.accept(BGBlocks.SABLE_MOSS_BLOCK.get());
                        output.accept(BGBlocks.SABLE_MOSS_CARPET.get());
                        output.accept(BGBlocks.UMBRAL_GRASS.get());
                        output.accept(BGBlocks.TALL_UMBRAL_GRASS.get());
                        output.accept(BGBlocks.MOON_BLOSSOM.get());
                        output.accept(BGBlocks.SABLE_MOSSY_BYSLATE.get());
                        output.accept(BGBlocks.SABLE_MOSSY_BYSLATE_STAIRS.get());
                        output.accept(BGBlocks.SABLE_MOSSY_BYSLATE_SLAB.get());
                        output.accept(BGBlocks.SABLE_MOSSY_BYSLATE_WALL.get());

                        output.accept(BGBlocks.AMPHORA.get());
                        output.accept(BGBlocks.GILDED_AMPHORA.get());
                        output.accept(BGBlocks.WHITE_AMPHORA.get());
                        output.accept(BGBlocks.LIGHT_GRAY_AMPHORA.get());
                        output.accept(BGBlocks.GRAY_AMPHORA.get());
                        output.accept(BGBlocks.BLACK_AMPHORA.get());
                        output.accept(BGBlocks.BROWN_AMPHORA.get());
                        output.accept(BGBlocks.RED_AMPHORA.get());
                        output.accept(BGBlocks.ORANGE_AMPHORA.get());
                        output.accept(BGBlocks.YELLOW_AMPHORA.get());
                        output.accept(BGBlocks.LIME_AMPHORA.get());
                        output.accept(BGBlocks.GREEN_AMPHORA.get());
                        output.accept(BGBlocks.CYAN_AMPHORA.get());
                        output.accept(BGBlocks.LIGHT_BLUE_AMPHORA.get());
                        output.accept(BGBlocks.BLUE_AMPHORA.get());
                        output.accept(BGBlocks.PURPLE_AMPHORA.get());
                        output.accept(BGBlocks.MAGENTA_AMPHORA.get());
                        output.accept(BGBlocks.PINK_AMPHORA.get());

                        output.accept(BGItems.MUSIC_DISC_SHUFFLE.get());
                    })
                    .title(Component.translatable("itemGroup.test"))
                    .build()
    );

    public static Supplier<CreativeModeTab> registerTab(String name, Supplier<CreativeModeTab> supplier) {
        return JinxedRegistryHelper.register(BuiltInRegistries.CREATIVE_MODE_TAB, Bygone.MOD_ID, name, supplier);
    }

    public static void register() {
    }

}
