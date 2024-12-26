package com.jamiedev.mod.fabric.init;

import com.jamiedev.mod.fabric.JamiesModFabric;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.structure.Structure;

public class JamiesModTag
{
    public static final TagKey<Block> BIG_BEAK_SPAWNABLE_ON = Blocks.createTag("big_beak_spawnable_on");
    public static final TagKey<Block> GLARE_SPAWNABLE_ON = Blocks.createTag("glare_spawnable_on");

    public static final TagKey<Block> COPPER_BLOCKS_1 = Blocks.createTag("copper_blocks");

    public static final TagKey<Block>  CORALS = Blocks.createTag("corals");
    public static final TagKey<Block>  CORAL_BLOCKS = Blocks.createTag("coral_blocks");
    public static final TagKey<Block>  WALL_CORALS = Blocks.createTag("wall_coral");

    public static final TagKey<Block>  CORAL_PLANTS = Blocks.createTag("coral_plants");
    public static final TagKey<Block>  BLEMISH_REPLACEABLE = Blocks.createTag("blemish_replaceable");
    public static final TagKey<Block>  BLEMISH_REPLACEABLE_WORLD_GEN = Blocks.createTag("blemish_replaceable_world_gen");
    public static final TagKey<Block> CREOSOTE_MAY_PLACE_ON = Blocks.createTag("creosote_may_place_on");

    public static final TagKey<Item> COPPER_BLOCKS = Items.createTag("copper_blocks");
    public static final TagKey<Item> VERDAGRIS_ITEMS = Items.createTag("verdigris_items");

    public static final TagKey<Item> BIGBEAK_FOOD = Items.createTag("bigbeak_food");

    public static final TagKey<Structure> ON_BYGONE_PORTAL_MAPS = Structures.createTag("on_bygone_portal_maps");

    public static final TagKey<Structure> BYGONE_ITEM_LOCATED = Structures.createTag("bygone_item_located");
    public static final TagKey<Block> COPPERBUGNESTS = Blocks.createTag("copperbug_nests") ;

    public static final TagKey<EntityType<?>> COPPERBUGNEST_INHABITORS = Entities.createTag("copperbugnest_inhabitors");

    public static class Blocks {

        private static TagKey<Block> createTag(String name) {
            return TagKey.of(RegistryKeys.BLOCK, JamiesModFabric.getModId(name));
        }
    }

    public static class Entities {
        private static TagKey<EntityType<?>> createTag(String id) {
            return TagKey.of(RegistryKeys.ENTITY_TYPE, JamiesModFabric.getModId(id));
        }
    }

    public static class Items {
        public static final TagKey<Item> TRANSFORMABLE_ITEMS = createTag("transformable_items");

        private static TagKey<Item> createTag(String name) {
            return TagKey.of(RegistryKeys.ITEM, JamiesModFabric.getModId(name));
        }
    }

    public static class Structures {

        private static TagKey<Structure> createTag(String id) {
            return TagKey.of(RegistryKeys.STRUCTURE, JamiesModFabric.getModId(id));
        }
    }
}
