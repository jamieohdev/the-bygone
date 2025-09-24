package com.jamiedev.bygone.core.registry;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.common.block.entity.GumboPotBlockEntity;
import com.jamiedev.bygone.common.item.MaliciousWarHornItem;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class BGDataComponentsNeoForge {

    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENTS = DeferredRegister.create(
            Registries.DATA_COMPONENT_TYPE,
            Bygone.MOD_ID
    );

    private static final Supplier<DataComponentType<MaliciousWarHornItem.WarHornData>> WAR_HORN_DATA_SUPPLIER = DATA_COMPONENTS.register(
            "war_horn_data", () -> {
                Codec<MaliciousWarHornItem.WarHornData> codec = RecordCodecBuilder.create(instance -> instance.group(
                        UUIDUtil.CODEC.listOf()
                                .fieldOf("active_vexes")
                                .forGetter(MaliciousWarHornItem.WarHornData::activeVexes),
                        Codec.INT.fieldOf("cooldown_seconds")
                                .forGetter(MaliciousWarHornItem.WarHornData::cooldownSeconds),
                        Codec.INT.fieldOf("vex_time_left").forGetter(MaliciousWarHornItem.WarHornData::vexTimeLeft)
                ).apply(instance, MaliciousWarHornItem.WarHornData::new));

                StreamCodec<RegistryFriendlyByteBuf, MaliciousWarHornItem.WarHornData> streamCodec = StreamCodec.composite(
                        UUIDUtil.STREAM_CODEC.apply(ByteBufCodecs.list()),
                        MaliciousWarHornItem.WarHornData::activeVexes,
                        ByteBufCodecs.VAR_INT,
                        MaliciousWarHornItem.WarHornData::cooldownSeconds,
                        ByteBufCodecs.VAR_INT,
                        MaliciousWarHornItem.WarHornData::vexTimeLeft,
                        MaliciousWarHornItem.WarHornData::new
                );

                return DataComponentType.<MaliciousWarHornItem.WarHornData>builder()
                        .persistent(codec)
                        .networkSynchronized(streamCodec)
                        .build();
            }
    );

    private static final Supplier<DataComponentType<BGDataComponentTypes.EchoGongData>> ECHO_GONG_DATA_SUPPLIER = DATA_COMPONENTS.register(
            "echo_gong_data", () -> {
                Codec<BGDataComponentTypes.EchoGongData> codec = RecordCodecBuilder.create(instance -> instance.group(
                                Codec.INT.fieldOf("charge").forGetter(BGDataComponentTypes.EchoGongData::charge))
                        .apply(instance, BGDataComponentTypes.EchoGongData::new));

                StreamCodec<RegistryFriendlyByteBuf, BGDataComponentTypes.EchoGongData> streamCodec = StreamCodec.composite(
                        ByteBufCodecs.VAR_INT,
                        BGDataComponentTypes.EchoGongData::charge,
                        BGDataComponentTypes.EchoGongData::new
                );

                return DataComponentType.<BGDataComponentTypes.EchoGongData>builder()
                        .persistent(codec)
                        .networkSynchronized(streamCodec)
                        .build();
            }
    );

    private static final Supplier<DataComponentType<GumboPotBlockEntity.GumboIngredientComponent>> GUMBO_INGREDIENT_DATA_SUPPLIER = DATA_COMPONENTS.register(
            "gumbo_ingredient_data",
            () -> DataComponentType.<GumboPotBlockEntity.GumboIngredientComponent>builder()
                    .persistent(GumboPotBlockEntity.GumboIngredientComponent.CODEC)
                    .networkSynchronized(GumboPotBlockEntity.GumboIngredientComponent.STREAM_CODEC)
                    .build()
    );

    public static void init() {
        DataComponentType<MaliciousWarHornItem.WarHornData> dataComponentType = WAR_HORN_DATA_SUPPLIER.get();
        BGDataComponents.WAR_HORN_DATA = (Holder<DataComponentType<MaliciousWarHornItem.WarHornData>>) (Object) BuiltInRegistries.DATA_COMPONENT_TYPE.wrapAsHolder(
                dataComponentType);

        DataComponentType<BGDataComponentTypes.EchoGongData> echoGongDataComponentType = ECHO_GONG_DATA_SUPPLIER.get();
        BGDataComponents.ECHO_GONG_DATA = (Holder<DataComponentType<BGDataComponentTypes.EchoGongData>>) (Object) BuiltInRegistries.DATA_COMPONENT_TYPE.wrapAsHolder(
                echoGongDataComponentType);

        DataComponentType<GumboPotBlockEntity.GumboIngredientComponent> gumboIngredientDataComponentType = GUMBO_INGREDIENT_DATA_SUPPLIER.get();

        BGDataComponents.GUMBO_INGREDIENT_DATA = (Holder<DataComponentType<GumboPotBlockEntity.GumboIngredientComponent>>) (Object) BuiltInRegistries.DATA_COMPONENT_TYPE.wrapAsHolder(
                gumboIngredientDataComponentType);
    }
}