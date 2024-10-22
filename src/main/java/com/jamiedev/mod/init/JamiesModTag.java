package com.jamiedev.mod.init;
import com.jamiedev.mod.JamiesMod;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class JamiesModTag
{
    public static final TagKey<Block> BIG_BEAK_SPAWNABLE_ON = Blocks.createTag("big_beak_spawnable_on");
    public static final TagKey<Block> GLARE_SPAWNABLE_ON = Blocks.createTag("glare_spawnable_on");

    public static final TagKey<Block>  CORALS = Blocks.createTag("corals");
    public static final TagKey<Block>  CORAL_BLOCKS = Blocks.createTag("coral_blocks");
    public static final TagKey<Block>  WALL_CORALS = Blocks.createTag("wall_coral");

    public static final TagKey<Block>  CORAL_PLANTS = Blocks.createTag("coral_plants");
    public static final TagKey<Block>  BLEMISH_REPLACEABLE = Blocks.createTag("blemish_replaceable");
    public static final TagKey<Block>  BLEMISH_REPLACEABLE_WORLD_GEN = Blocks.createTag("blemish_replaceable_world_gen");

    public static class Blocks {

        private static TagKey<Block> createTag(String name) {
            return TagKey.of(RegistryKeys.BLOCK, JamiesMod.getModId(name));
        }
    }

    public static class Items {
        public static final TagKey<Item> TRANSFORMABLE_ITEMS = createTag("transformable_items");

        private static TagKey<Item> createTag(String name) {
            return TagKey.of(RegistryKeys.ITEM, JamiesMod.getModId(name));
        }
    }
}
