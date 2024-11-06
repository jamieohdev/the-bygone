package com.jamiedev.mod.init;

import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.fabricmc.fabric.api.registry.FuelRegistry;
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
        compostingRegistry.add(JamiesModBlocks.SHORT_GRASS, 0.2F);
        compostingRegistry.add(JamiesModBlocks.TALL_GRASS, 0.4F);
    }

    private static void addFuels() {
        FuelRegistry fuelRegistry = FuelRegistry.INSTANCE;

        fuelRegistry.add(JamiesModBlocks.ANCIENT_SAPLING, 300);
        fuelRegistry.add(JamiesModBlocks.CAVE_VINES_PLANT, 200);
        fuelRegistry.add(JamiesModBlocks.CAVE_VINES, 200);
        fuelRegistry.add(JamiesModBlocks.CHARNIA, 50);
        fuelRegistry.add(JamiesModBlocks.MONTSECHIA, 50);
        fuelRegistry.add(JamiesModBlocks.RAFFLESIA, 500);
        fuelRegistry.add(JamiesModBlocks.SHORT_GRASS, 300);
        fuelRegistry.add(JamiesModBlocks.TALL_GRASS, 200);
    }

    public static void init()
    {
        addCompostables();
        addFuels();
    }

}
