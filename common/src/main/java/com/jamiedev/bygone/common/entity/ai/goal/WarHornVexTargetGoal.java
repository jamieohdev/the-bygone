package com.jamiedev.bygone.common.entity.ai.goal;

import com.jamiedev.bygone.core.registry.BGDataComponents;
import com.jamiedev.bygone.core.registry.BGItems;
import com.jamiedev.bygone.common.item.MaliciousWarHornItem;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class WarHornVexTargetGoal extends NearestAttackableTargetGoal<Monster> {
    private final Vex vex;
    private boolean warHornVexChecked = false;
    private boolean isWarHornVexCached = false;
    
    public WarHornVexTargetGoal(Vex vex) {
        super(vex, Monster.class, 10, true, false, (target) -> 
            target != vex && !(target instanceof Vex));
        this.vex = vex;
    }
    
    @Override
    public boolean canUse() {
        if (!isWarHornVex()) {
            return false;
        }
        
        if (vex.getTarget() instanceof Player) {
            vex.setTarget(null);
            return false;
        }
        
        return super.canUse();
    }
    
    public boolean isWarHornVex() {
        if (warHornVexChecked) {
            return isWarHornVexCached;
        }
        
        if (!(vex.level() instanceof ServerLevel serverLevel)) {
            warHornVexChecked = true;
            isWarHornVexCached = false;
            return false;
        }
        
        for (ServerPlayer player : serverLevel.getServer().getPlayerList().getPlayers()) {
            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                ItemStack stack = player.getInventory().getItem(i);
                if (stack.getItem() == BGItems.MALICIOUS_WAR_HORN.get()) {
                    MaliciousWarHornItem.WarHornData data = stack.getOrDefault(BGDataComponents.WAR_HORN_DATA.value(), MaliciousWarHornItem.WarHornData.EMPTY);
                    if (data.activeVexes().contains(vex.getUUID())) {
                        warHornVexChecked = true;
                        isWarHornVexCached = true;
                        return true;
                    }
                }
            }
        }
        
        warHornVexChecked = true;
        isWarHornVexCached = false;
        return false;
    }
}