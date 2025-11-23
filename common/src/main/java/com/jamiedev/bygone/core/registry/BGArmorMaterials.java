package com.jamiedev.bygone.core.registry;

import com.jamiedev.bygone.Bygone;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.AnimalArmorItem;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.EnumMap;
import java.util.List;
import java.util.function.Supplier;

public class BGArmorMaterials {
    public static Holder<ArmorMaterial> SCALE = register("scale", Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
        map.put(ArmorItem.Type.BOOTS, 3);
        map.put(ArmorItem.Type.LEGGINGS, 6);
        map.put(ArmorItem.Type.CHESTPLATE, 8);
        map.put(ArmorItem.Type.HELMET, 3);
        map.put(ArmorItem.Type.BODY, 11);
    }), 9, SoundEvents.ARMOR_EQUIP_DIAMOND, 2.0F, 0.3F, () -> Ingredient.of(BGItems.SCALE.get()));


    public static Holder<ArmorMaterial> CARAPACE = register("carapace", Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
        map.put(ArmorItem.Type.BOOTS, 2);
        map.put(ArmorItem.Type.LEGGINGS, 5);
        map.put(ArmorItem.Type.CHESTPLATE, 6);
        map.put(ArmorItem.Type.HELMET, 2);
        map.put(ArmorItem.Type.BODY, 5);
    }), 1, SoundEvents.ARMOR_EQUIP_IRON, 0.0F, 0.0F, () -> Ingredient.of(BGItems.GLOW_CHITIN.get()));

    public static Holder<ArmorMaterial> BIG_BEAK = register("big_beak", Util.make(new EnumMap(AnimalArmorItem.BodyType.class), map -> {
        map.put(AnimalArmorItem.BodyType.EQUESTRIAN, 1);
    }), 9, SoundEvents.ARMOR_EQUIP_DIAMOND, 2.0F, 0.3F, () -> Ingredient.of(BGItems.SCALE.get()));


    private static Holder<ArmorMaterial> register(
            String id,
            EnumMap<ArmorItem.Type, Integer> defense,
            int enchantability,
            Holder<SoundEvent> equipSound,
            float toughness,
            float knockbackResistance,
            Supplier<Ingredient> repairIngredient
    ) {
        List<ArmorMaterial.Layer> list = List.of(new ArmorMaterial.Layer(ResourceLocation.tryParse(Bygone.MOD_ID + ":" + id)));
        return register(id, defense, enchantability, equipSound, toughness, knockbackResistance, repairIngredient, list);
    }

    private static Holder<ArmorMaterial> register(
            String id,
            EnumMap<ArmorItem.Type, Integer> defense,
            int enchantability,
            Holder<SoundEvent> equipSound,
            float toughness,
            float knockbackResistance,
            Supplier<Ingredient> repairIngredient,
            List<ArmorMaterial.Layer> layers
    ) {
        EnumMap<ArmorItem.Type, Integer> enumMap = new EnumMap<>(ArmorItem.Type.class);

        for (ArmorItem.Type type : ArmorItem.Type.values()) {
            enumMap.put(type, defense.get(type));
        }

        return Registry.registerForHolder(
                BuiltInRegistries.ARMOR_MATERIAL,
                ResourceLocation.tryParse(Bygone.MOD_ID + ":" + id),
                new ArmorMaterial(enumMap, enchantability, equipSound, repairIngredient, layers, toughness, knockbackResistance)
        );
    }


}
