package com.jamiedev.bygone.common.item;

import com.jamiedev.bygone.common.component.entity.HookData;
import com.jamiedev.bygone.common.entity.projectile.HookEntity;
import com.jamiedev.bygone.core.registry.BGAttachmentTypes;
import com.jamiedev.bygone.core.registry.BGSoundEvents;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.GameEvent;

public class HookItem extends Item {
    public HookItem(Item.Properties settings) {
        super(settings);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if(HookData.getHook(player) != null){
            player.startUsingItem(hand);
            player.gameEvent(GameEvent.ITEM_INTERACT_START);
        } else if(!level.isClientSide){
            // you need the hook anyway or else the entity will retract
            // so take durability first
            // ...it'd be a bit annoying for your hook to break immediately when you want to use it but
            // hey, the trope's there for a reason
            stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
            if(stack.isEmpty()) return InteractionResultHolder.consume(stack);

            HookEntity hook = new HookEntity(player);
            if(level.addFreshEntity(hook)){
                player.set(BGAttachmentTypes.HOOK, new HookData(hook.getId()));
                level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1F, (1F / (level.getRandom().nextFloat() * 0.4F + 1.2F)) + 0.33F);
                player.awardStat(Stats.ITEM_USED.get(this));
            }
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    @Override
    public void onUseTick(Level level, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        HookEntity hook = HookData.getHook(user);
        if(hook != null){
            hook.pull();
            if(remainingUseTicks % 5 == 0) level.playSound(null, user.getX(), user.getY(), user.getZ(), BGSoundEvents.HOOK_RETRIEVE_ADDITIONS_EVENT, SoundSource.PLAYERS, 1.0F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
        }
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity user) {
        return Integer.MAX_VALUE;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public boolean isValidRepairItem(ItemStack hook, ItemStack repair){
        return repair.is(Blocks.CHAIN.asItem());
    }
}