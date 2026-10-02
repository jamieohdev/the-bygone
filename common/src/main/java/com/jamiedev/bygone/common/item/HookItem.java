package com.jamiedev.bygone.common.item;

import com.jamiedev.bygone.common.entity.projectile.HookEntity;
import com.jamiedev.bygone.common.util.PlayerWithHook;
import com.jamiedev.bygone.core.registry.BGSoundEvents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

public class HookItem extends Item {
    public HookItem(Properties settings) {
        super(settings);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        HookEntity hook = ((PlayerWithHook)player).bygone$getHook();
        ItemStack stack = player.getItemInHand(hand);
        if(hook == null || hook.isRetracting()){
            if(!level.isClientSide){
                stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
                if(stack.isEmpty()) return InteractionResultHolder.consume(stack);
                hook = new HookEntity(level, player);
                hook.shootFromRotation(player, player.getXRot(), player.getYRot(), 0F, 10F, 0F);
                if(level.addFreshEntity(hook)) ((PlayerWithHook)player).bygone$setHook(hook);
                level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1F, 1F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + 0.33F);
            }
        } else if(hook.isAttached()){
            player.startUsingItem(hand);
            player.gameEvent(GameEvent.ITEM_INTERACT_START);
        } else return InteractionResultHolder.fail(stack);
        player.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    @Override
    public void onUseTick(Level level, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (user instanceof Player player) {
            HookEntity hook = ((PlayerWithHook) player).bygone$getHook();
            if (hook != null && hook.isAttached()) {
                if (remainingUseTicks % 5 == 0) level.playSound(null, user.getX(), user.getY(), user.getZ(), BGSoundEvents.HOOK_RETRIEVE_ADDITIONS_EVENT, SoundSource.NEUTRAL, 1.0F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
                if (!player.isShiftKeyDown()) hook.pull(0.07F);
            }
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity user, int slot, boolean selected){
        if(!selected || !(user instanceof Player player)) return;
        HookEntity hook = ((PlayerWithHook)player).bygone$getHook();
        if(hook == null || hook.getEquilibrium() < 0 || hook.isRetracting()) return; // equilibrium set on impact
        Vec3 vector = hook.position().subtract(player.position());
        if(vector.lengthSqr() <= Mth.EPSILON) return;

        // ... apparently player movement is handled on client. fall damage is server though! so. that's. something.
        if(level.isClientSide){
            // XXX: no fucking clue how to stop a player from getting kicked for flying :/
            double rebound = vector.length() - hook.getEquilibrium();
            if(rebound <= 0) return;
            Vec3 direction = vector.normalize();
            Vec3 tension = direction.scale(-0.5 * player.getDeltaMovement().dot(direction));
            player.push(vector.normalize().scale(user.getGravity() * rebound).add(tension));
            if(!player.onGround()) player.push(player.getDeltaMovement().multiply(0.075, 0, 0.075)); // counteract air friction
        }
        else{
            player.currentImpulseImpactPos = player.position();
            player.setIgnoreFallDamageFromCurrentImpulse(true);
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


}