package com.jamiedev.bygone.core.init;

import com.jamiedev.bygone.Bygone;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.material.Fluid;

public class JamiesModTag {

    public static final TagKey<Block> SABLE_LOGS = Blocks.createTag("sable_logs");
    public static final TagKey<Block> BIG_BEAK_SPAWNABLE_ON = Blocks.createTag("big_beak_spawnable_on");
    public static final TagKey<Block> GLARE_SPAWNABLE_ON = Blocks.createTag("glare_spawnable_on");

    public static final TagKey<Block> HEATER_BLOCKS = Blocks.createTag("heater_blocks");

    public static final TagKey<Block> MEGALITH_BLOCKS = Blocks.createTag("megalith_blocks");

    public static final TagKey<Block> SPRINKLERS = Blocks.createTag("sprinklers");
    public static final TagKey<Block> COPPER_BLOCKS_1 = Blocks.createTag("copper_blocks");

    public static final TagKey<Block> PEST_REPELLENTS = Blocks.createTag("pest_repellents");

    public static final TagKey<Block>  MOSSY = Blocks.createTag("mossy");
    public static final TagKey<Block>  CORALS = Blocks.createTag("corals");
    public static final TagKey<Block>  CORAL_BLOCKS = Blocks.createTag("coral_blocks");
    public static final TagKey<Block>  WALL_CORALS = Blocks.createTag("wall_coral");

    public static final TagKey<Block>  WHIRLIWEEDS = Blocks.createTag("whirliweeds");
    public static final TagKey<Item>  WHIRLIWEEDS_ITEM = Items.createTag("whirliweeds");


    public static final TagKey<Block>  CORAL_PLANTS = Blocks.createTag("coral_plants");
    public static final TagKey<Block>  BLEMISH_REPLACEABLE = Blocks.createTag("blemish_replaceable");
    public static final TagKey<Block>  BLEMISH_REPLACEABLE_WORLD_GEN = Blocks.createTag("blemish_replaceable_world_gen");
    public static final TagKey<Block> CREOSOTE_MAY_PLACE_ON = Blocks.createTag("creosote_may_place_on");

    public static final TagKey<Item> COPPER_BLOCKS = Items.createTag("copper_blocks");
    public static final TagKey<Item> VERDAGRIS_ITEMS = Items.createTag("verdigris_items");

    public static final TagKey<Item> BIGBEAK_FOOD = Items.createTag("bigbeak_food");
    public static final TagKey<Item> WHISKBILL_FOOD = Items.createTag("whiskbill_food");

    public static final TagKey<Item> GUMBO_MAKES_SAFE = Items.createTag("gumbo_makes_safe");
    public static final TagKey<Item> CANNOT_ADD_TO_GUMBO = Items.createTag("cannot_add_to_gumbo");

    public static final TagKey<Item> SABEAST_AFRAID_PLAYER_HOLD = Items.createTag("sabeast_afraid_player_hold");

    public static final TagKey<Structure> ON_BYGONE_PORTAL_MAPS = Structures.createTag("on_bygone_portal_maps");

    public static final TagKey<Structure> BYGONE_ITEM_LOCATED = Structures.createTag("bygone_item_located");
    public static final TagKey<Block> COPPERBUGNESTS = Blocks.createTag("copperbug_nests") ;

    public static final TagKey<EntityType<?>> COPPERBUGNEST_INHABITORS = Entities.createTag("copperbugnest_inhabitors");
    public static final TagKey<Block> SHELF_FUNGI = Blocks.createTag("shelf_fungi") ;
    public static final TagKey<Block>  MOLD_REPLACEABLE = Blocks.createTag("mold_replaceable");

    public static final TagKey<Biome> SPAWNS_WARM_BIGBEAKS = Biomes.createTag("spawns_warm_bigbeaks");

    public static class Blocks {

        private static TagKey<Block> createTag(String name) {
            return TagKey.create(Registries.BLOCK, Bygone.id(name));
        }
    }

    public static class Biomes {

        private static TagKey<Biome> createTag(String name) {
            return TagKey.create(Registries.BIOME, Bygone.id(name));
        }
    }

    public static class Fluids {
        private static TagKey<Fluid> create(String name) {
            return TagKey.create(Registries.FLUID, Bygone.id(name));
        }
    }


    public static class Entities {
        private static TagKey<EntityType<?>> createTag(String id) {
            return TagKey.create(Registries.ENTITY_TYPE, Bygone.id(id));
        }
    }

    public static class Items {
        public static final TagKey<Item> TRANSFORMABLE_ITEMS = createTag("transformable_items");

        private static TagKey<Item> createTag(String name) {
            return TagKey.create(Registries.ITEM, Bygone.id(name));
        }
    }

    public static class Structures {

        private static TagKey<Structure> createTag(String id) {
            return TagKey.create(Registries.STRUCTURE, Bygone.id(id));
        }
    }

    public static class PointOfInterests {
        public static final TagKey<PoiType> COPPERBUG_HOME = of("copperbug_home");


        private static TagKey<PoiType> of(String id) {
            return TagKey.create(Registries.POINT_OF_INTEREST_TYPE, Bygone.id(id));
        }
    }
}
