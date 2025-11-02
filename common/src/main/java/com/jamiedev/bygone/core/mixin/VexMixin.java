package com.jamiedev.bygone.core.mixin;

import com.jamiedev.bygone.common.entity.ai.goal.WarHornVexTargetGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Vex.class)
public class VexMixin {

    @Inject(method = "registerGoals", at = @At("TAIL"))
    private void modifyGoals(CallbackInfo ci) {
        Vex vex = (Vex) (Object) this;

        vex.targetSelector.addGoal(0, new WarHornVexTargetGoal(vex));
        vex.targetSelector.addGoal(1, new HurtByTargetGoal(vex) {
            @Override
            public boolean canUse() {
                WarHornVexTargetGoal warHornGoal = null;
                for (var wrappedGoal : vex.targetSelector.getAvailableGoals()) {
                    if (wrappedGoal.getGoal() instanceof WarHornVexTargetGoal) {
                        warHornGoal = (WarHornVexTargetGoal) wrappedGoal.getGoal();
                        break;
                    }
                }

                if (warHornGoal != null && warHornGoal.isWarHornVex()) {
                    return super.canUse() && !(vex.getLastHurtByMob() instanceof Player);
                }

                return super.canUse();
            }
        });
    }
}