package com.jamiedev.bygone.common.entity.ai.goal;

import com.jamiedev.bygone.common.entity.WallowEntity;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class WallowDriftGoal extends Goal {
    private final WallowEntity wallow;

    private int cooldownTicks = 0;

    public WallowDriftGoal(WallowEntity wallow) {
        this.wallow = wallow;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        return true;
    }

    @Override
    public void tick() {
        wallow.noPhysics = true;
        wallow.setNoGravity(true);

        if (wallow.level().isClientSide) {
            return;
        }

        if (cooldownTicks-- <= 0) {
            cooldownTicks = 10 + wallow.getRandom().nextInt(20);
            Vec3 v = wallow.getDeltaMovement();

            double impulseX = (wallow.getRandom().nextDouble() - 0.5D) * 0.06D;
            double impulseY = (wallow.getRandom().nextDouble() - 0.5D) * 0.03D;
            double impulseZ = (wallow.getRandom().nextDouble() - 0.5D) * 0.06D;

            double nx = Mth.clamp(v.x + impulseX, -0.18D, 0.18D);
            double ny = Mth.clamp(v.y + impulseY, -0.10D, 0.10D);
            double nz = Mth.clamp(v.z + impulseZ, -0.18D, 0.18D);

            wallow.setDeltaMovement(nx, ny, nz);
        } else {
            Vec3 v = wallow.getDeltaMovement();
            wallow.setDeltaMovement(v.x * 0.92D, v.y * 0.92D, v.z * 0.92D);
        }
    }
}