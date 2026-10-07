package com.jamiedev.bygone.common.item;

import com.jamiedev.bygone.common.entity.AquifawnEntity;
import com.jamiedev.bygone.common.entity.SkipperEntity;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ItemSteerable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class AmoebaGelOnAStickItem extends Item {
    private static final int BOOST_DURABILITY_COST = 7;

    public AmoebaGelOnAStickItem(Properties itemProperties) {
        super(itemProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack itemStack = player.getItemInHand(interactionHand);
        if (!level.isClientSide) {
            Entity controlledVehicle = player.getControlledVehicle();
            if ((controlledVehicle instanceof AquifawnEntity || controlledVehicle instanceof SkipperEntity)
                && controlledVehicle instanceof ItemSteerable itemSteerable && itemSteerable.boost()) {
                ItemStack result = itemStack.hurtAndConvertOnBreak(BOOST_DURABILITY_COST, Items.FISHING_ROD, player, LivingEntity.getSlotForHand(interactionHand));
                return InteractionResultHolder.success(result);
            }
            player.awardStat(Stats.ITEM_USED.get(this));
        }
        return InteractionResultHolder.pass(itemStack);
    }
}
