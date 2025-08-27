package com.jamiedev.bygone.common.util;

import com.jamiedev.bygone.common.item.MaliciousWarHornItem;
import com.jamiedev.bygone.core.registry.BGItems;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.item.ItemStack;

public class VexDeathTracker {
    
    public static void onVexDeath(Vex vex, ServerLevel level) {
        for (ServerPlayer player : level.getServer().getPlayerList().getPlayers()) {
            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                ItemStack stack = player.getInventory().getItem(i);
                
                if (stack.getItem() == BGItems.MALICIOUS_WAR_HORN.get()) {
                    MaliciousWarHornItem.onVexDeath(vex, stack);
                    return;
                }
            }
        }
    }
}