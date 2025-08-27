package com.jamiedev.bygone.core.registry;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.common.effect.UpdraftEffect;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;

public class BGMobEffectsFabric {
    
    public static final MobEffect UPDRAFT = Registry.register(
            BuiltInRegistries.MOB_EFFECT,
            ResourceLocation.fromNamespaceAndPath(Bygone.MOD_ID, "updraft"),
            new UpdraftEffect()
    );
    
    public static void init() {
    }
}