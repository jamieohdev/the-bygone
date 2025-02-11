package com.jamiedev.bygone.fabric.datagen;

import com.jamiedev.bygone.Bygone;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
public class JamiesModTags
{
    public static class Blocks {
        private static TagKey<Block> createTag(String name) {
            return TagKey.create(Registries.BLOCK, Bygone.getModId(name));
        }
    }

    public static class Items {

        public static final TagKey<Item> ANCIENT_LOGS =
                createTag("ancient_logs");


        private static TagKey<Item> createTag(String name) {
            return TagKey.create(Registries.ITEM, Bygone.getModId(name));
        }
    }

    public static class Fluids {
        public static final TagKey<Fluid> EXAMPLE_LIQUID = of("example_liquid");

        private static TagKey<Fluid> of(String name) {
            return TagKey.create(Registries.FLUID, Bygone.getModId(name));
        }
    }
}
