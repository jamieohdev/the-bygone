package com.jamiedev.bygone.common.item;

import com.jamiedev.bygone.core.registry.BGArmorMaterials;
import com.jamiedev.bygone.core.registry.BGMobEffects;

import net.minecraft.tags.FluidTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class CarapaceArmorItem extends ArmorItem{
    public CarapaceArmorItem(ArmorItem.Type type, Item.Properties properties){
        super(BGArmorMaterials.CARAPACE, type, properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected){
        // we check player, instead of livingentity, because the base armor slots are different for non-players
        if(!(entity instanceof Player player)) return;
        if(slot != this.getType().getSlot().getIndex(Inventory.INVENTORY_SIZE)) return;

        if(player.isEyeInFluid(FluidTags.WATER))
            player.addEffect(new MobEffectInstance(BGMobEffects.CARAPACE.get(), 200, 0));
    }
}
