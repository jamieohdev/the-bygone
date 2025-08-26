package com.jamiedev.bygone.core.mixin;

import com.jamiedev.bygone.core.registry.BGDataComponents;
import com.jamiedev.bygone.core.registry.BGItems;
import com.jamiedev.bygone.common.item.MaliciousWarHornItem;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixinVex {
    
    @Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
    private void preventWarHornVexHurtingPlayer(DamageSource damageSource, float amount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity entity = (LivingEntity)(Object)this;
        
        if (entity instanceof Player player && damageSource.getEntity() instanceof Vex vex) {
            if (isWarHornVex(vex, player)) {
                cir.setReturnValue(false);
            }
        }
    }
    
    @Inject(method = "doHurtTarget", at = @At("HEAD"), cancellable = true)
    private void preventWarHornVexTargeting(Entity target, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity entity = (LivingEntity)(Object)this;
        
        if (entity instanceof Vex vex && target instanceof Player player) {
            if (isWarHornVex(vex, player)) {
                cir.setReturnValue(false);
            }
        }
    }
    
    private boolean isWarHornVex(Vex vex, Player player) {
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.getItem() == BGItems.MALICIOUS_WAR_HORN.get()) {
                MaliciousWarHornItem.WarHornData data = stack.getOrDefault(BGDataComponents.WAR_HORN_DATA.value(), MaliciousWarHornItem.WarHornData.EMPTY);
                if (data.activeVexes().contains(vex.getUUID())) {
                    return true;
                }
            }
        }
        return false;
    }
}