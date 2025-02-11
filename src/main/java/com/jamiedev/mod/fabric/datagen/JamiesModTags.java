package com.jamiedev.mod.fabric.datagen;

import com.jamiedev.mod.fabric.JamiesModFabric;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
public class JamiesModTags
{
    public static class Blocks {
        private static TagKey<Block> createTag(String name) {
            return TagKey.create(Registries.BLOCK, JamiesModFabric.getModId(name));
        }
    }

    public static class Items {

        public static final TagKey<Item> ANCIENT_LOGS =
                createTag("ancient_logs");


        private static TagKey<Item> createTag(String name) {
            return TagKey.create(Registries.ITEM, JamiesModFabric.getModId(name));
        }
    }

    public static class Fluids {
        public static final TagKey<Fluid> EXAMPLE_LIQUID = of("example_liquid");

        private static TagKey<Fluid> of(String name) {
            return TagKey.create(Registries.FLUID, JamiesModFabric.getModId(name));
        }
    }
}
