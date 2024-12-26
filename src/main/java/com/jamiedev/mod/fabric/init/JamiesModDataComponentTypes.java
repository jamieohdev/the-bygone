package com.jamiedev.mod.fabric.init;

import com.jamiedev.mod.common.blocks.entity.CopperbugNestBlockEntity;
import com.jamiedev.mod.fabric.JamiesModFabric;
import net.minecraft.block.entity.BeehiveBlockEntity;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentType;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.component.type.ContainerLootComponent;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.inventory.ContainerLock;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Rarity;

import java.util.List;
import java.util.function.UnaryOperator;

public class JamiesModDataComponentTypes
{
    public static final ComponentType<List<CopperbugNestBlockEntity.CopperbugData>> COPPERBUGS = register("copperbugs", (builder) -> {
        return builder.codec(CopperbugNestBlockEntity.CopperbugData.LIST_CODEC).packetCodec(CopperbugNestBlockEntity.CopperbugData.PACKET_CODEC.collect(PacketCodecs.toList())).cache();
    });
    public static final ComponentType<List<BeehiveBlockEntity.BeeData>> BEES = register("bees", (builder) -> {
        return builder.codec(BeehiveBlockEntity.BeeData.LIST_CODEC).packetCodec(BeehiveBlockEntity.BeeData.PACKET_CODEC.collect(PacketCodecs.toList())).cache();
    });

  //  public static final ComponentMap DEFAULT_ITEM_COMPONENTS;

    public JamiesModDataComponentTypes() {
    }


    private static <T> ComponentType<T> register(String id, UnaryOperator<ComponentType.Builder<T>> builderOperator) {

        return (ComponentType)Registry.register(Registries.DATA_COMPONENT_TYPE, JamiesModFabric.getModId(id), ((ComponentType.Builder)builderOperator.apply(ComponentType.builder())).build());
    }

    static {
  //      DEFAULT_ITEM_COMPONENTS = ComponentMap.builder().add(MAX_STACK_SIZE, 64).add(LORE, LoreComponent.DEFAULT).add(ENCHANTMENTS, ItemEnchantmentsComponent.DEFAULT).add(REPAIR_COST, 0).add(ATTRIBUTE_MODIFIERS, AttributeModifiersComponent.DEFAULT).add(RARITY, Rarity.COMMON).build();
    }
}
