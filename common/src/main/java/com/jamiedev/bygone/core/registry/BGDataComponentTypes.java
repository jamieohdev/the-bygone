package com.jamiedev.bygone.core.registry;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.common.component.item.*;

import com.kekecreations.jinxedlib.core.util.JinxedRegistryHelper;

import com.mojang.serialization.Codec;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;

import java.util.List;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;


public class BGDataComponentTypes {
    public static final Supplier<DataComponentType<EchoGongData>> ECHO_GONG = register("echo_gong_data",
            b -> b.persistent(EchoGongData.CODEC).networkSynchronized(EchoGongData.STREAM_CODEC));

    public static final Supplier<DataComponentType<GearBagData>> GEAR_BAG = register("gear_bag",
            b -> b.persistent(GearBagData.CODEC).networkSynchronized(GearBagData.STREAM_CODEC));

    public static final Supplier<DataComponentType<GumboIngredientData>> GUMBO_INGREDIENT = register("gumbo_ingredient_data",
            b -> b.persistent(GumboIngredientData.CODEC).networkSynchronized(GumboIngredientData.STREAM_CODEC));

    public static final Supplier<DataComponentType<Integer>> MAP_HEIGHT = register("map_height",
            b -> b.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.VAR_INT));

    public static final Supplier<DataComponentType<WarHornData>> WAR_HORN = register("war_horn_data",
            b -> b.persistent(WarHornData.CODEC).networkSynchronized(WarHornData.STREAM_CODEC));



    private static <T> Supplier<DataComponentType<T>> register(String id, UnaryOperator<DataComponentType.Builder<T>> builder) {
        return JinxedRegistryHelper.register(
                BuiltInRegistries.DATA_COMPONENT_TYPE,
                Bygone.MOD_ID, id,
                () -> builder.apply(DataComponentType.builder()).build()
        );
    }
    public static void init(){}
}
