package com.jamiedev.bygone.common.entity.ai;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.pathfinder.PathType;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class FollowPlayerGoal extends Goal {
    private final Mob mob;
    private final Predicate<Player> followPredicate;
    private final double speedModifier;
    private final PathNavigation navigation;
    private final float stopDistance;
    private final float areaSize;
    @Nullable
    private Player followingMob;
    private int timeToRecalcPath;
    private float oldWaterCost;

    public FollowPlayerGoal(Mob mob, double speedModifier, float stopDistance, float areaSize) {
        this.mob = mob;
        this.followPredicate = Objects::nonNull;//&& mob.getClass() != p_25278_.getClass();
        this.speedModifier = speedModifier;
        this.navigation = mob.getNavigation();
        this.stopDistance = stopDistance;
        this.areaSize = areaSize;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        if (!(mob.getNavigation() instanceof GroundPathNavigation) && !(mob.getNavigation() instanceof FlyingPathNavigation)) {
            throw new IllegalArgumentException("Unsupported mob type for FollowPlayerGoal");
        }
    }

    public boolean canUse() {
        List<Player> list = this.mob.level().getEntitiesOfClass(Player.class, this.mob.getBoundingBox().inflate(this.areaSize), this.followPredicate);
        if (!list.isEmpty()) {
            for (Player mob : list) {
                if (!mob.isInvisible()) {
                    this.followingMob = mob;
                    return true;
                }
            }
        }

        return false;
    }

    public boolean canContinueToUse() {
        return this.followingMob != null && !this.navigation.isDone() && this.mob.distanceToSqr(this.followingMob) > (double) (this.stopDistance * this.stopDistance);
    }

    public void start() {
        this.timeToRecalcPath = 0;
        this.oldWaterCost = this.mob.getPathfindingMalus(PathType.WATER);
        this.mob.setPathfindingMalus(PathType.WATER, 0.0F);
    }

    public void stop() {
        this.followingMob = null;
        this.navigation.stop();
        this.mob.setPathfindingMalus(PathType.WATER, this.oldWaterCost);
    }

    public void tick() {
        if (this.followingMob != null && !this.mob.isLeashed()) {
            this.mob.getLookControl().setLookAt(this.followingMob, 10.0F, (float) this.mob.getMaxHeadXRot());
            if (--this.timeToRecalcPath <= 0) {
                this.timeToRecalcPath = this.adjustedTickDelay(10);
                double d0 = this.mob.getX() - this.followingMob.getX();
                double d1 = this.mob.getY() - this.followingMob.getY();
                double d2 = this.mob.getZ() - this.followingMob.getZ();
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                if (!(d3 <= (double) (this.stopDistance * this.stopDistance))) {
                    this.navigation.moveTo(this.followingMob, this.speedModifier);
                } else {
                    this.navigation.stop();

                }
            }
        }

    }
}
