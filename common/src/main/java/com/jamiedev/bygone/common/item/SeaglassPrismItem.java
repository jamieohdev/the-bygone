package com.jamiedev.bygone.common.item;

import com.jamiedev.bygone.core.registry.BGItems;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class SeaglassPrismItem extends Item implements Equipable
{
    public SeaglassPrismItem(Properties properties) {
        super(properties);
    }

    public void inventoryTick(ItemStack stack, Level level, Entity entity, int itemSlot, boolean isSelected) {
        if (level instanceof ServerLevel serverlevel || !level.isClientSide) {
            if (entity instanceof Player player) {
                ItemStack itemstack = player.getItemBySlot(EquipmentSlot.OFFHAND);
                if (itemstack.is(BGItems.SEAGLASS_PRISM.get())) {
                    while(player.experienceProgress >= 0.1F) {
                        float range = level.random.nextIntBetweenInclusive(1, 5);
                        float i = player.experienceProgress + range;
                        player.giveExperienceLevels((int)i);
                    }
                }
            }
        }
    }

    @Override
    public EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.OFFHAND;
    }
}
