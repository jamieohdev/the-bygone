package com.jamiedev.bygone.common.item;

import com.jamiedev.bygone.core.registry.BGMobEffects;
import com.jamiedev.bygone.core.registry.BGSoundEvents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

public class WhirliweedBundleItem extends Item {
    
    public WhirliweedBundleItem(Properties properties) {
        super(properties);
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        
        if (!level.isClientSide) {
            player.addEffect(new MobEffectInstance(BGMobEffects.UPDRAFT.get(), 600, 0));
            
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    BGSoundEvents.WHIRLIWEED_BUNDLE_USE.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
            level.gameEvent(GameEvent.INSTRUMENT_PLAY, player.position(), GameEvent.Context.of(player));
            
            player.awardStat(Stats.ITEM_USED.get(this));
            
            if (!player.getAbilities().instabuild) {
                itemStack.shrink(1);
            }
        }
        
        return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
    }
    
    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.NONE;
    }
    
    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }
}