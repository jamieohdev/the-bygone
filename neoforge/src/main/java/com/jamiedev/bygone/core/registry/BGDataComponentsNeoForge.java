package com.jamiedev.bygone.core.registry;

import com.jamiedev.bygone.Bygone;
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
    
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENTS = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, Bygone.MOD_ID);
    
    private static final Supplier<DataComponentType<MaliciousWarHornItem.WarHornData>> WAR_HORN_DATA_SUPPLIER = DATA_COMPONENTS.register("war_horn_data", () -> {
        Codec<MaliciousWarHornItem.WarHornData> codec = RecordCodecBuilder.create(instance ->
            instance.group(
                UUIDUtil.CODEC.listOf().fieldOf("active_vexes").forGetter(MaliciousWarHornItem.WarHornData::activeVexes),
                Codec.INT.fieldOf("cooldown_seconds").forGetter(MaliciousWarHornItem.WarHornData::cooldownSeconds),
                Codec.INT.fieldOf("vex_time_left").forGetter(MaliciousWarHornItem.WarHornData::vexTimeLeft)
            ).apply(instance, MaliciousWarHornItem.WarHornData::new)
        );
        
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
    });
    
    public static void init() {
        DataComponentType<MaliciousWarHornItem.WarHornData> dataComponentType = WAR_HORN_DATA_SUPPLIER.get();
        BGDataComponents.WAR_HORN_DATA = (Holder<DataComponentType<MaliciousWarHornItem.WarHornData>>) (Object) BuiltInRegistries.DATA_COMPONENT_TYPE.wrapAsHolder(dataComponentType);
    }
}