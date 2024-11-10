package com.jamiedev.mod.common.init;

import com.jamiedev.mod.common.JamiesMod;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.decoration.painting.PaintingVariant;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKeys;

public class JamiesModPaintings
{
    public static final RegistryKey<PaintingVariant> HALLUCIGENIA = of("hallucigenia");

    public static void bootstrap(Registerable<PaintingVariant> registry) {
        register(registry, HALLUCIGENIA, 4, 2);
    }

    private static void register(Registerable<PaintingVariant> registry, RegistryKey<PaintingVariant> key, int width, int height) {
        registry.register(key, new PaintingVariant(width, height, key.getValue()));
    }

    private static RegistryKey<PaintingVariant> of(String id) {
        return RegistryKey.of(RegistryKeys.PAINTING_VARIANT, JamiesMod.getModId( id));
    }
}
