package com.jamiedev.bygone.core.registry;

import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.armortrim.TrimMaterial;

import com.jamiedev.bygone.Bygone;

import java.util.Map;

public class BGTrimMaterials
{
    public static final ResourceKey<TrimMaterial> VERDIGRIS = registerKey("verdigris");

    private static ResourceKey<TrimMaterial> registerKey(String name) {
        return ResourceKey.create(Registries.TRIM_MATERIAL, Bygone.id(name));
    }

    public static void bootstrap(BootstrapContext<TrimMaterial> context) {
        register(context, VERDIGRIS, BGItems.VERDIGRIS_INGOT.get(), Style.EMPTY.withColor(0x70CFA7), 0.55F);
    }

    private static void register(BootstrapContext<TrimMaterial> context, ResourceKey<TrimMaterial> materialKey, Item ingredient, Style style, float itemModelIndex) {
        register(context, materialKey, ingredient, style, itemModelIndex, Map.of());
    }

    private static void register(BootstrapContext<TrimMaterial> context, ResourceKey<TrimMaterial> materialKey, Item ingredient, Style style, float itemModelIndex, Map<Holder<ArmorMaterial>, String> overrideArmorMaterials) {
        TrimMaterial trimMaterial = TrimMaterial.create(materialKey.location().getPath(), ingredient, itemModelIndex, Component.translatable(Util.makeDescriptionId("trim_material", materialKey.location())).withStyle(style), overrideArmorMaterials);
        context.register(materialKey, trimMaterial);
    }
}
