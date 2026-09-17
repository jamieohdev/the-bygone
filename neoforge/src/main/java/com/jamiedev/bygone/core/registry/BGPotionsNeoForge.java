package com.jamiedev.bygone.core.registry;
import com.jamiedev.bygone.Bygone;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
public class BGPotionsNeoForge
{
    public static final DeferredRegister<Potion> POTIONS =
            DeferredRegister.create(BuiltInRegistries.POTION, Bygone.MOD_ID);

    public static final Holder<Potion> HAUNTED_POTION = POTIONS.register("haunted_potion",
            () -> new Potion("haunted_potion", new MobEffectInstance(BGMobEffects.HAUNTED.get(), 3600, 0)));

    public static final Holder<Potion> LONG_HAUNTED_POTION = POTIONS.register("long_haunted_potion",
            () -> new Potion("haunted_potion", new MobEffectInstance(BGMobEffects.HAUNTED.get(), 9600, 0)));

    public static void register(IEventBus eventBus) {
        POTIONS.register(eventBus);
    }
}
