package com.jamiedev.mod.common.items;

import com.jamiedev.mod.common.entities.projectile.BygoneItemEntity;
import com.jamiedev.mod.fabric.init.JamiesModTag;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.EnderEyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;

public class BygoneItem extends Item
{
    public BygoneItem(Item.Properties settings) {
        super(settings);
    }

EnderEyeItem ref;

    public int getUseDuration(ItemStack stack, LivingEntity user) {
        return 0;
    }

    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        ItemStack itemStack = user.getItemInHand(hand);
        BlockHitResult blockHitResult = getPlayerPOVHitResult(world, user, ClipContext.Fluid.NONE);

            user.startUsingItem(hand);
            if (world instanceof ServerLevel serverWorld) {
                BlockPos blockPos = serverWorld.findNearestMapStructure(JamiesModTag.BYGONE_ITEM_LOCATED, user.blockPosition(), 100, false);
                if (blockPos != null) {
                    BygoneItemEntity eyeOfEnderEntity = new BygoneItemEntity(world, user.getX(), user.getY(0.5), user.getZ());
                    eyeOfEnderEntity.setItem(itemStack);
                    eyeOfEnderEntity.initTargetPos(blockPos);
                    world.gameEvent(GameEvent.PROJECTILE_SHOOT, eyeOfEnderEntity.position(), GameEvent.Context.of(user));
                    world.addFreshEntity(eyeOfEnderEntity);
                    if (user instanceof ServerPlayer serverPlayerEntity) {
                        CriteriaTriggers.USED_ENDER_EYE.trigger(serverPlayerEntity, blockPos);
                    }

                    float f = Mth.lerp(world.random.nextFloat(), 0.33F, 0.5F);
                    world.playSound((Player)null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENDER_EYE_LAUNCH, SoundSource.NEUTRAL, 1.0F, f);
                    itemStack.consume(1, user);
                    user.awardStat(Stats.ITEM_USED.get(this));
                    user.swing(hand, true);
                    return InteractionResultHolder.success(itemStack);
                }
            }

            return InteractionResultHolder.consume(itemStack);
    }

}
