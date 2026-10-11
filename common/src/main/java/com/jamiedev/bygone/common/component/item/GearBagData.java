package com.jamiedev.bygone.common.component.item;

import com.jamiedev.bygone.core.registry.BGDataComponentTypes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BundleContents;

import org.apache.commons.lang3.math.Fraction;

import org.jetbrains.annotations.Nullable;

public class GearBagData extends BundleContents{
    public static final Codec<GearBagData> CODEC = RecordCodecBuilder.create(i -> i.group(
            ItemStack.CODEC.listOf().fieldOf("items").forGetter(x -> x.items)
    ).apply(i, GearBagData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf,GearBagData> STREAM_CODEC = StreamCodec.composite(
            ItemStack.STREAM_CODEC.apply(ByteBufCodecs.list()), x -> x.items,
            GearBagData::new
    );

    public static final GearBagData EMPTY = new GearBagData(List.of());

    public static final int MAX_ITEMS = 9;
    private static boolean allowed(ItemStack stack){
        return !stack.isEmpty() && stack.getMaxStackSize() == 1 && stack.getItem().canFitInsideContainerItems();
    }



    public GearBagData(List<ItemStack> items){
        super(items, Fraction.getFraction(items.size(), MAX_ITEMS));
    }
    public static GearBagData from(ItemStack stack){
        return stack.getOrDefault(BGDataComponentTypes.GEAR_BAG.get(), GearBagData.EMPTY);
    }

    // inherited equals and hashCode is sufficient for us



    public static class Mutable{
        private final List<ItemStack> items;
        public Mutable(GearBagData bag){
            this.items = new ArrayList<>(bag.items);
        }
        public static Mutable from(ItemStack stack){
            return new Mutable(GearBagData.from(stack));
        }


        public boolean insert(ItemStack stack){
            if(this.items.size() >= MAX_ITEMS || !GearBagData.allowed(stack)) return false;
            this.items.add(0, stack.split(1));
            return true;
        }
        public boolean insert(Slot slot, Player player){
            if(this.items.size() >= MAX_ITEMS || !GearBagData.allowed(slot.getItem())) return false;

            ItemStack stack = slot.safeTake(1, 1, player);
            if(stack.isEmpty()) return false;
            this.items.add(0, stack);
            return true;
        }

        @Nullable
        public ItemStack remove(){
            return this.items.isEmpty() ? null : this.items.remove(0);
        }
        public boolean remove(Slot slot){
            if(this.items.isEmpty()) return false;

            ItemStack stack = slot.safeInsert(this.items.get(0).copy());
            if(stack.equals(this.items.get(0))) return false;
            else if(stack.isEmpty()) this.items.remove(0);
            else this.items.set(0, stack);
            return true;
        }

        public void clear(){ this.items.clear(); }

        public GearBagData toImmutable(){
            return new GearBagData(items);
        }
    }
}
