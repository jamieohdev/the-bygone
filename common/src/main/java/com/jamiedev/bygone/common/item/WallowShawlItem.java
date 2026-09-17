package com.jamiedev.bygone.common.item;

import com.jamiedev.bygone.core.registry.BGItems;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;

public class WallowShawlItem extends Item implements Equipable {

	public WallowShawlItem(Item.Properties properties) {
		super(properties);
		DispenserBlock.registerBehavior(this, ArmorItem.DISPENSE_ITEM_BEHAVIOR);
	}

	@Override
	public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
		return repair.is(BGItems.WALLOW_SHAWL_SCRAP.get());
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		return this.swapWithEquipmentSlot(this, level, player, hand);
	}

	@Override
	public Holder<SoundEvent> getEquipSound() {
		return SoundEvents.ARMOR_EQUIP_ELYTRA;
	}

	@Override
	public EquipmentSlot getEquipmentSlot() {
		return EquipmentSlot.CHEST;
	}

}
