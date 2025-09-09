package com.jamiedev.bygone.core.registry;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.common.block.entity.CopperbugNestBlockEntity;
import com.jamiedev.bygone.common.item.MaliciousWarHornItem;

import java.util.List;
import java.util.UUID;
import java.util.function.UnaryOperator;
import java.util.function.Supplier;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;


public class BGDataComponentTypes
{
   public static final DataComponentType<List<CopperbugNestBlockEntity.CopperbugData>> COPPERBUGS = register("copperbugs", builder -> {
     return builder.persistent(CopperbugNestBlockEntity.CopperbugData.LIST_CODEC).networkSynchronized(
               CopperbugNestBlockEntity.CopperbugData.PACKET_CODEC.apply(ByteBufCodecs.list())).cacheEncoding();
   });
    public static final DataComponentType<List<BeehiveBlockEntity.Occupant>> BEES = register("bees", builder -> {
        return builder.persistent(BeehiveBlockEntity.Occupant.LIST_CODEC).networkSynchronized(BeehiveBlockEntity.Occupant.STREAM_CODEC.apply(ByteBufCodecs.list())).cacheEncoding();
    });


    public static final DataComponentType<Integer> BYGONE_MAP_HEIGHT = register("bygone_map_height", (p_335177_) -> p_335177_.persistent(ExtraCodecs.POSITIVE_INT).networkSynchronized(ByteBufCodecs.VAR_INT));
    

    public static final DataComponentType<CopperbugNestBlockEntity.CopperbugData> COPPERBUG_1 = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            Bygone.id("copperbugs"),
            DataComponentType.<CopperbugNestBlockEntity.CopperbugData>builder().persistent(null).build()
    );

  //  public static final ComponentMap DEFAULT_ITEM_COMPONENTS;

    public BGDataComponentTypes() {
    }


    private static <T> DataComponentType<T> register(String id, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, Bygone.id(id), builderOperator.apply(DataComponentType.builder()).build());
    }

    static {
  //      DEFAULT_ITEM_COMPONENTS = ComponentMap.builder().add(MAX_STACK_SIZE, 64).add(LORE, LoreComponent.DEFAULT).add(ENCHANTMENTS, ItemEnchantmentsComponent.DEFAULT).add(REPAIR_COST, 0).add(ATTRIBUTE_MODIFIERS, AttributeModifiersComponent.DEFAULT).add(RARITY, Rarity.COMMON).build();
    }

    protected static void init() {
        Bygone.LOGGER.info("Registering {} components", Bygone.MOD_ID);

    }
    
    public record EchoGongData(int charge) {
        public static final EchoGongData EMPTY = new EchoGongData(0);
    }
}
