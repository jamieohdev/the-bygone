package com.jamiedev.bygone.core.registry;

import com.google.common.base.Suppliers;
import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.common.effect.CarapaceEffect;
import com.jamiedev.bygone.common.effect.UpdraftEffect;
import com.kekecreations.jinxedlib.core.util.JinxedRegistryHelper;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;

import java.util.function.Supplier;

public class BGMobEffects {

    public static Supplier<Holder<MobEffect>> UPDRAFT = register("updraft", UpdraftEffect::new);

    public static Supplier<Holder<MobEffect>> CARAPACE = register("carapace", CarapaceEffect::new);

    private static <T extends MobEffect> Supplier<Holder<MobEffect>> register(String name, Supplier<T> supplier) {
        JinxedRegistryHelper.register(BuiltInRegistries.MOB_EFFECT, Bygone.MOD_ID, name, supplier);
        return Suppliers.memoize(
                () -> BuiltInRegistries.MOB_EFFECT.getHolder(ResourceLocation.fromNamespaceAndPath(Bygone.MOD_ID, name))
                        .orElseThrow()
        );
    }

    public static void init() {
    }
}