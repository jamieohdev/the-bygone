package com.jamiedev.bygone.core.registry;

import com.jamiedev.bygone.Bygone;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import java.util.function.Supplier;

public class BGMobEffects {
    
    public static Supplier<Holder<MobEffect>> UPDRAFT = () -> {
        MobEffect effect = BuiltInRegistries.MOB_EFFECT.get(ResourceLocation.fromNamespaceAndPath(Bygone.MOD_ID, "updraft"));
        return BuiltInRegistries.MOB_EFFECT.wrapAsHolder(effect);
    };
    
    public static void init() {
    }
}