package com.jamiedev.bygone.core.mixin;

import com.jamiedev.bygone.core.registry.BGDataComponents;
import com.jamiedev.bygone.core.registry.BGItems;
import com.jamiedev.bygone.common.item.MaliciousWarHornItem;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Vex.class)
public class VexMixin {
    
    @Inject(method = "registerGoals", at = @At("TAIL"))
    private void modifyGoals(CallbackInfo ci) {
        Vex vex = (Vex)(Object)this;
        
        if (isWarHornVex(vex)) {
            vex.targetSelector.removeAllGoals(goal -> goal instanceof NearestAttackableTargetGoal);
            
            vex.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(vex, Monster.class, 10, true, false, (target) -> 
                target != vex && !(target instanceof Vex) && !isWarHornVexOfSamePlayer(vex, target)));
        }
    }
    
    private boolean isWarHornVex(Vex vex) {
        if (!(vex.level() instanceof ServerLevel serverLevel)) return false;
        
        for (ServerPlayer player : serverLevel.getServer().getPlayerList().getPlayers()) {
            if (isVexFromPlayer(vex, player)) {
                return true;
            }
        }
        return false;
    }
    
    private boolean isVexFromPlayer(Vex vex, Player player) {
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
    
    private boolean isWarHornVexOfSamePlayer(Vex vex1, LivingEntity target) {
        if (!(target instanceof Vex vex2)) return false;
        if (!(vex1.level() instanceof ServerLevel serverLevel)) return false;
        
        for (ServerPlayer player : serverLevel.getServer().getPlayerList().getPlayers()) {
            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                ItemStack stack = player.getInventory().getItem(i);
                if (stack.getItem() == BGItems.MALICIOUS_WAR_HORN.get()) {
                    MaliciousWarHornItem.WarHornData data = stack.getOrDefault(BGDataComponents.WAR_HORN_DATA.value(), MaliciousWarHornItem.WarHornData.EMPTY);
                    if (data.activeVexes().contains(vex1.getUUID()) && data.activeVexes().contains(vex2.getUUID())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}