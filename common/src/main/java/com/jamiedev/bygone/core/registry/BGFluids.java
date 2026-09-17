package com.jamiedev.bygone.core.registry;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.common.block.fluids.LithoFluid;
import com.kekecreations.jinxedlib.core.util.JinxedRegistryHelper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.material.Fluid;

import java.util.function.Supplier;

public class BGFluids {

    public static Supplier<Fluid> LITHO_STILL = registerFluid("litho", LithoFluid.Source::new);
    public static Supplier<Fluid> LITHO_FLOWING = registerFluid("flowing_litho", LithoFluid.Flowing::new);

    public static Supplier<Fluid> registerFluid(String name, Supplier<Fluid> supplier) {
        return JinxedRegistryHelper.register(BuiltInRegistries.FLUID, Bygone.MOD_ID, name, supplier);
    }

    public static void init() {}
}
