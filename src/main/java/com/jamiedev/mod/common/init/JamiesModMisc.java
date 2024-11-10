package com.jamiedev.mod.common.init;

import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Items;

public class JamiesModMisc {
    protected static void addCompostables() {
        CompostingChanceRegistry compostingRegistry = CompostingChanceRegistry.INSTANCE;
        float LEAVES_CHANCE = compostingRegistry.get(Items.OAK_LEAVES);

        compostingRegistry.add(JamiesModBlocks.ANCIENT_SAPLING, 0.5F);
        compostingRegistry.add(JamiesModBlocks.CAVE_VINES_PLANT, 0.3F);
        compostingRegistry.add(JamiesModBlocks.CAVE_VINES, 0.3F);
        compostingRegistry.add(JamiesModBlocks.CHARNIA, 0.2F);
        compostingRegistry.add(JamiesModBlocks.MONTSECHIA, 0.2F);
        compostingRegistry.add(JamiesModBlocks.RAFFLESIA, 0.8F);
        compostingRegistry.add(JamiesModBlocks.SAGARIA, 0.2F);
        compostingRegistry.add(JamiesModBlocks.SHORT_GRASS, 0.2F);
        compostingRegistry.add(JamiesModBlocks.TALL_GRASS, 0.4F);
        compostingRegistry.add(JamiesModBlocks.ANCIENT_ROOTS, 0.3F);
        compostingRegistry.add(JamiesModBlocks.ANCIENT_VINE, 0.3F);
        compostingRegistry.add(JamiesModBlocks.ANCIENT_LEAVES, 0.3F);
    }

    private static void addFuels() {
        FuelRegistry fuelRegistry = FuelRegistry.INSTANCE;
        final FlammableBlockRegistry flammableBlockRegistry = FlammableBlockRegistry.getDefaultInstance();

        fuelRegistry.add(JamiesModBlocks.ANCIENT_SAPLING, 300);
        fuelRegistry.add(JamiesModBlocks.CAVE_VINES_PLANT, 200);
        fuelRegistry.add(JamiesModBlocks.CAVE_VINES, 200);
        fuelRegistry.add(JamiesModBlocks.CHARNIA, 50);
        fuelRegistry.add(JamiesModBlocks.MONTSECHIA, 50);
        fuelRegistry.add(JamiesModBlocks.RAFFLESIA, 500);
        fuelRegistry.add(JamiesModBlocks.SAGARIA, 50);
        fuelRegistry.add(JamiesModBlocks.SHORT_GRASS, 300);
        fuelRegistry.add(JamiesModBlocks.TALL_GRASS, 200);
        fuelRegistry.add(JamiesModBlocks.ANCIENT_ROOTS, 50);

        fuelRegistry.add(JamiesModBlocks.ANCIENT_WOOD, 300);
        fuelRegistry.add(JamiesModBlocks.ANCIENT_LOG, 300);
        fuelRegistry.add(JamiesModBlocks.ANCIENT_PLANKS, 300);
        fuelRegistry.add(JamiesModBlocks.ANCIENT_SLAB, 150);
        fuelRegistry.add(JamiesModBlocks.ANCIENT_STAIRS, 300);
        fuelRegistry.add(JamiesModBlocks.ANCIENT_PRESSURE_PLATE, 300);
        fuelRegistry.add(JamiesModBlocks.ANCIENT_BUTTON, 100);
        fuelRegistry.add(JamiesModBlocks.ANCIENT_TRAPDOOR, 300);
        fuelRegistry.add(JamiesModBlocks.ANCIENT_FENCE_GATE, 300);
        fuelRegistry.add(JamiesModBlocks.ANCIENT_FENCE, 300);
        fuelRegistry.add(JamiesModBlocks.ANCIENT_DOOR, 200);

        flammableBlockRegistry.add(JamiesModBlocks.ANCIENT_WOOD, 5, 20);
        flammableBlockRegistry.add(JamiesModBlocks.ANCIENT_LOG, 5, 20);
        flammableBlockRegistry.add(JamiesModBlocks.ANCIENT_PLANKS, 5, 20);
        flammableBlockRegistry.add(JamiesModBlocks.ANCIENT_SLAB, 5, 20);
        flammableBlockRegistry.add(JamiesModBlocks.ANCIENT_STAIRS, 5, 20);
        flammableBlockRegistry.add(JamiesModBlocks.ANCIENT_PRESSURE_PLATE, 5, 20);
        flammableBlockRegistry.add(JamiesModBlocks.ANCIENT_BUTTON, 5, 20);
        flammableBlockRegistry.add(JamiesModBlocks.ANCIENT_TRAPDOOR, 5, 20);
        flammableBlockRegistry.add(JamiesModBlocks.ANCIENT_FENCE_GATE, 5, 20);
        flammableBlockRegistry.add(JamiesModBlocks.ANCIENT_FENCE, 5, 20);
        flammableBlockRegistry.add(JamiesModBlocks.ANCIENT_DOOR, 5, 20);

    }

    public static void init()
    {
        addCompostables();
        addFuels();
    }

}
