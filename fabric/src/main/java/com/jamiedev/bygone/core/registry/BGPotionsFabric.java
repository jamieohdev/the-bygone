package com.jamiedev.bygone.core.registry;

import com.jamiedev.bygone.Bygone;
import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistryBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;

public class BGPotionsFabric {

    public static final Holder<Potion> HAUNTED = register("haunted_potion", new Potion(new MobEffectInstance(
            BGMobEffects.HAUNTED.get(), 3600)));
    public static final Holder<Potion> LONG_HAUNTED = register("long_haunted_potion", new Potion(new MobEffectInstance(
            BGMobEffects.HAUNTED.get(), 9600)));

    private static Holder<Potion> register(String id, Potion potion) {
        return Registry.registerForHolder(BuiltInRegistries.POTION, Bygone.id(id), potion);
    }

    public static void init() {
        FabricBrewingRecipeRegistryBuilder.BUILD.register(builder -> builder.addMix(Potions.AWKWARD, BGItems.LITHOPLASM.get(), HAUNTED));
        FabricBrewingRecipeRegistryBuilder.BUILD.register(builder -> builder.addMix(HAUNTED, Items.REDSTONE, LONG_HAUNTED));
    }
    
}
