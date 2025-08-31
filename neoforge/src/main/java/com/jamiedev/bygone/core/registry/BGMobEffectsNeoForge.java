package com.jamiedev.bygone.core.registry;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.common.effect.UpdraftEffect;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

public class BGMobEffectsNeoForge {
    
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = 
            DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, Bygone.MOD_ID);
    
    public static final DeferredHolder<MobEffect, MobEffect> UPDRAFT = 
            MOB_EFFECTS.register("updraft", UpdraftEffect::new);
    
    public static void init(net.neoforged.bus.api.IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
}