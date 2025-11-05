package com.jamiedev.bygone.common.item;

import com.google.gson.JsonSerializationContext;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

public class BreathPodItem extends Item
{
    public BreathPodItem(Item.Properties properties)
    {
        super(properties);
    }

    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entityLiving) {
        super.finishUsingItem(stack, level, entityLiving);
        if (entityLiving instanceof ServerPlayer serverplayer && serverplayer.getAirSupply() < 300) {
            CriteriaTriggers.CONSUME_ITEM.trigger(serverplayer, stack);
            serverplayer.awardStat(Stats.ITEM_USED.get(this));
            serverplayer.setAirSupply(serverplayer.getAirSupply() + 150);
            stack.consume(1, serverplayer);
        }

        return stack;
    }

    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 40;
    }

    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.EAT;
    }

    public SoundEvent getDrinkingSound() {
        return SoundEvents.CAMEL_EAT;
    }

    public SoundEvent getEatingSound() {
        return SoundEvents.CAMEL_EAT;
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        return ItemUtils.startUsingInstantly(level, player, hand);
    }
}
