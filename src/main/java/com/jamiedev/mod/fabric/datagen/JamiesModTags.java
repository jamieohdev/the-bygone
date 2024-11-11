package com.jamiedev.mod.fabric.datagen;

import com.jamiedev.mod.fabric.JamiesModFabric;
import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
public class JamiesModTags
{
    public static class Blocks {
        private static TagKey<Block> createTag(String name) {
            return TagKey.of(RegistryKeys.BLOCK, JamiesModFabric.getModId(name));
        }
    }

    public static class Items {

        public static final TagKey<Item> ANCIENT_LOGS =
                createTag("ancient_logs");


        private static TagKey<Item> createTag(String name) {
            return TagKey.of(RegistryKeys.ITEM, JamiesModFabric.getModId(name));
        }
    }

    public static class Fluids {
        public static final TagKey<Fluid> EXAMPLE_LIQUID = of("example_liquid");

        private static TagKey<Fluid> of(String name) {
            return TagKey.of(RegistryKeys.FLUID, JamiesModFabric.getModId(name));
        }
    }
}
