package com.jamiedev.bygone.core.registry;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.common.block.fluids.BronzeFluid;
import com.kekecreations.jinxedlib.core.util.JinxedRegistryHelper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.material.Fluid;

import java.util.function.Supplier;

public class BGFluids {

    public static Supplier<Fluid> BRONZE_STILL = registerFluid("bronze_still", BronzeFluid.Source::new);
    public static Supplier<Fluid> BRONZE_FLOWING = registerFluid("bronze_flowing", BronzeFluid.Flowing::new);

    public static Supplier<Fluid> registerFluid(String name, Supplier<Fluid> supplier) {
        return JinxedRegistryHelper.register(BuiltInRegistries.FLUID, Bygone.MOD_ID, name, supplier);
    }
}
