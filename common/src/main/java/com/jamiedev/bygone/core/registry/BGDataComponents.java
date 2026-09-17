package com.jamiedev.bygone.core.registry;

import com.jamiedev.bygone.common.block.entity.GumboPotBlockEntity;
import com.jamiedev.bygone.common.item.MaliciousWarHornItem;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class BGDataComponents {

    public static Holder<DataComponentType<MaliciousWarHornItem.WarHornData>> WAR_HORN_DATA;
    public static Holder<DataComponentType<BGDataComponentTypes.EchoGongData>> ECHO_GONG_DATA;

    public static Holder<DataComponentType<GumboPotBlockEntity.GumboIngredientComponent>> GUMBO_INGREDIENT_DATA;

    static GumboPotBlockEntity.GumboIngredientComponent gumboComponent(FoodProperties foodProperties) {
        return new GumboPotBlockEntity.GumboIngredientComponent(foodProperties);
    }

    /*
    * Should probably be data driven in the future
    */
    public static void gumboBootstrap(BiConsumer<Item, GumboPotBlockEntity.GumboIngredientComponent> consumer) {
        consumer.accept(
            Items.MOSS_BLOCK,
            gumboComponent(new FoodProperties.Builder().nutrition(2)
                .saturationModifier(0.05f)
                .build()
            )
        );
        consumer.accept(
            Items.MOSS_CARPET,
            gumboComponent(new FoodProperties.Builder().nutrition(1)
                .saturationModifier(0.05f)
                .build()
            )
        );
        consumer.accept(
            BGBlocks.ALPHA_MOSS_BLOCK.get().asItem(),
            gumboComponent(new FoodProperties.Builder().nutrition(2)
                .saturationModifier(0.10f)
                .build()
            )
        );
        consumer.accept(
            BGBlocks.ALPHA_MOSS_CARPET.get().asItem(),
            gumboComponent(new FoodProperties.Builder().nutrition(1)
                .saturationModifier(0.10f)
                .build()
            )
        );
        consumer.accept(
            BGBlocks.SABLE_MOSS_BLOCK.get().asItem(),
            gumboComponent(new FoodProperties.Builder().nutrition(2)
                .saturationModifier(0.15f)
                .build()
            )
        );
        consumer.accept(
            BGBlocks.SABLE_MOSS_CARPET.get().asItem(),
            gumboComponent(new FoodProperties.Builder().nutrition(1)
                .saturationModifier(0.15f)
                .build()
            )
        );
        consumer.accept(
            Items.STICK,
            gumboComponent(new FoodProperties.Builder().nutrition(1)
                .saturationModifier(0.02f)
                .build()
            )
        );
        consumer.accept(
            Items.BAMBOO,
            gumboComponent(new FoodProperties.Builder().nutrition(4)
                .saturationModifier(0.3f)
                .build()
            )
        );
        consumer.accept(
            Items.BONE,
            gumboComponent(new FoodProperties.Builder().nutrition(4)
                .saturationModifier(0.6f)
                .build()
            )
        );
        consumer.accept(
            Items.SLIME_BALL,
            gumboComponent(new FoodProperties.Builder().nutrition(2)
                .saturationModifier(0.2f)
                .effect(new MobEffectInstance(MobEffects.CONFUSION, 100, 0), 0.3f)
                .build()
            )
        );
        consumer.accept(
            Items.LEATHER,
            gumboComponent(new FoodProperties.Builder().nutrition(2)
                .saturationModifier(0.2f)
                .build()
            )
        );
        consumer.accept(
            Items.LEATHER_BOOTS,
            gumboComponent(new FoodProperties.Builder().nutrition(6)
                .saturationModifier(0.2f)
                .build()
            )
        );
        consumer.accept(
            Items.LEATHER_CHESTPLATE,
            gumboComponent(new FoodProperties.Builder().nutrition(8)
                .saturationModifier(0.2f)
                .build()
            )
        );
        consumer.accept(
            Items.LEATHER_HELMET,
            gumboComponent(new FoodProperties.Builder().nutrition(6)
                .saturationModifier(0.2f)
                .build()
            )
        );
        consumer.accept(
            Items.LEATHER_LEGGINGS,
            gumboComponent(new FoodProperties.Builder().nutrition(6)
                .saturationModifier(0.2f)
                .build()
            )
        );
        consumer.accept(
            BGBlocks.BLEMISH.get().asItem(),
            gumboComponent(new FoodProperties.Builder().nutrition(2)
                .saturationModifier(0.05f)
                .build()
            )
        );
        consumer.accept(
            BGBlocks.BLEMISH_VEIN.get().asItem(),
            gumboComponent(new FoodProperties.Builder().nutrition(2)
                .saturationModifier(0.05f)
                .build()
            )
        );
        consumer.accept(
            BGBlocks.ORANGE_MUSHROOM_BLOCK.get().asItem(),
            gumboComponent(new FoodProperties.Builder().nutrition(3)
                .saturationModifier(0.1f)
                .effect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 200, 0), 0.05f)
                .build()
            )
        );
        consumer.accept(
            BGBlocks.PINK_MUSHROOM_BLOCK.get().asItem(),
            gumboComponent(new FoodProperties.Builder().nutrition(3)
                .saturationModifier(0.1f)
                .effect(new MobEffectInstance(MobEffects.REGENERATION, 100, 0), 0.05f)
                .build()
            )
        );
        consumer.accept(
            BGBlocks.PURPLE_MUSHROOM_BLOCK.get().asItem(),
            gumboComponent(new FoodProperties.Builder().nutrition(3)
                .saturationModifier(0.1f)
                .effect(new MobEffectInstance(MobEffects.WEAKNESS, 200, 0), 0.05f)
                .build()
            )
        );
        consumer.accept(
            BGItems.ORANGE_FUNGI.get(),
            gumboComponent(new FoodProperties.Builder().nutrition(3)
                .saturationModifier(0.1f)
                .effect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 400, 0), 0.05f)
                .effect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 400, 0), 0.025f)
                .build()
            )
        );
        consumer.accept(
            BGItems.PINK_FUNGI.get(),
            gumboComponent(new FoodProperties.Builder().nutrition(3)
                    .saturationModifier(0.1f)
                    .effect(new MobEffectInstance(MobEffects.REGENERATION, 200, 0), 0.05f)
                    .effect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 400, 0), 0.025f)
                    .build()
            )
        );
        consumer.accept(
            BGItems.PURPLE_FUNGI.get(),
            gumboComponent(new FoodProperties.Builder().nutrition(3)
                    .saturationModifier(0.1f)
                    .effect(new MobEffectInstance(MobEffects.WEAKNESS, 400, 0), 0.05f)
                    .effect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 400, 0), 0.025f)
                    .build()
            )
        );
        consumer.accept(
            Items.CRIMSON_FUNGUS,
            gumboComponent(new FoodProperties.Builder().nutrition(3)
                    .saturationModifier(0.1f)
                    .effect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 400, 0), 0.05f)
                    .effect(new MobEffectInstance(MobEffects.WEAKNESS, 400, 0), 0.025f)
                    .build()
            )
        );
        consumer.accept(
            Items.CRIMSON_ROOTS,
            gumboComponent(new FoodProperties.Builder().nutrition(1)
                .saturationModifier(0.1f)
                .effect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 400, 0), 0.05f)
                .effect(new MobEffectInstance(MobEffects.WEAKNESS, 400, 0), 0.025f)
                .build()
            )
        );
        consumer.accept(
            Items.NETHER_SPROUTS,
            gumboComponent(new FoodProperties.Builder().nutrition(1)
                .saturationModifier(0.1f)
                .effect(new MobEffectInstance(MobEffects.CONFUSION, 100, 0), 0.05f)
                .build()
            )
        );
        consumer.accept(
            Items.WARPED_FUNGUS,
            gumboComponent(new FoodProperties.Builder().nutrition(3)
                .saturationModifier(0.1f)
                .effect(new MobEffectInstance(MobEffects.INVISIBILITY, 400, 0), 0.05f)
                .effect(new MobEffectInstance(MobEffects.POISON, 400, 0), 0.025f)
                .build()
            )
        );
        consumer.accept(
            Items.WARPED_ROOTS,
            gumboComponent(new FoodProperties.Builder().nutrition(1)
                .saturationModifier(0.1f)
                .effect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 400, 0), 0.05f)
                .effect(new MobEffectInstance(MobEffects.WEAKNESS, 400, 0), 0.025f)
                .build()
            )
        );
        consumer.accept(
            Items.BROWN_MUSHROOM,
            gumboComponent(new FoodProperties.Builder().nutrition(3)
                .saturationModifier(0.1f)
                .effect(new MobEffectInstance(MobEffects.DIG_SPEED, 100, 0), 0.05f)
                .build()
            )
        );
        consumer.accept(
            Items.RED_MUSHROOM,
            gumboComponent(new FoodProperties.Builder().nutrition(3)
                .saturationModifier(0.1f)
                .effect(new MobEffectInstance(MobEffects.JUMP, 100, 1), 0.05f)
                .build()
            )
        );
        consumer.accept(
            Items.BROWN_MUSHROOM_BLOCK,
            gumboComponent(new FoodProperties.Builder().nutrition(3)
                .saturationModifier(0.1f)
                .build()
            )
        );
        consumer.accept(
            Items.RED_MUSHROOM_BLOCK,
            gumboComponent(new FoodProperties.Builder().nutrition(3)
                .saturationModifier(0.1f)
                .build()
            )
        );
        consumer.accept(
            BGBlocks.SHELF_FUNGUS.get().asItem(),
            gumboComponent(new FoodProperties.Builder().nutrition(3)
                .saturationModifier(0.1f)
                .build()
            )
        );
        consumer.accept(
            BGBlocks.SHELF_MOLD.get().asItem(),
            gumboComponent(new FoodProperties.Builder().nutrition(3)
                .saturationModifier(0.1f)
                .build()
            )
        );
        consumer.accept(
            BGBlocks.SHELF_MOLD_MOSS.get().asItem(),
            gumboComponent(new FoodProperties.Builder().nutrition(2)
                .saturationModifier(0.1f)
                .build()
            )
        );
        consumer.accept(
            BGBlocks.SHELF_ROOTS.get().asItem(),
            gumboComponent(new FoodProperties.Builder().nutrition(1)
                .saturationModifier(0.05f)
                .build()
            )
        );
        consumer.accept(
            BGBlocks.SHELF_SPROUTS.get().asItem(),
            gumboComponent(new FoodProperties.Builder().nutrition(1)
                .saturationModifier(0.05f)
                .build()
            )
        );
        consumer.accept(
            BGBlocks.ORANGE_FUNGI_VINES.get().asItem(),
            gumboComponent(new FoodProperties.Builder().nutrition(1)
                .saturationModifier(0.05f)
                .build()
            )
        );
        consumer.accept(
            BGBlocks.PINK_FUNGI_VINES.get().asItem(),
            gumboComponent(new FoodProperties.Builder().nutrition(1)
                .saturationModifier(0.05f)
                .build()
            )
        );
        consumer.accept(
            BGBlocks.PURPLE_FUNGI_VINES.get().asItem(),
            gumboComponent(new FoodProperties.Builder().nutrition(1)
                .saturationModifier(0.05f)
                .build()
            )
        );
        consumer.accept(
            BGBlocks.BELLADONNA.get().asItem(),
            gumboComponent(new FoodProperties.Builder().nutrition(1)
                .saturationModifier(0.02f)
                .effect(new MobEffectInstance(MobEffects.WITHER, 400, 2), 1.0f)
                .build()
            )
        );
        consumer.accept(
            BGItems.AMOEBA_GEL.get().asItem(),
            gumboComponent(new FoodProperties.Builder().nutrition(5)
                .saturationModifier(1.0f)
                .build()
            )
        );
    }
}