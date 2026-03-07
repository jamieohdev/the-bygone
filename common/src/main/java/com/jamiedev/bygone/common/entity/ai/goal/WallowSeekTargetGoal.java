package com.jamiedev.bygone.common.entity.ai.goal;

import com.jamiedev.bygone.common.entity.WallowEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

public class WallowSeekTargetGoal extends Goal {
    private static final double EPS = 1.0E-6;

    private final WallowEntity wallow;
    private final double speed;
    private final double stopDistance;
    private final Predicate<LivingEntity> selector;

    private LivingEntity target;

    public WallowSeekTargetGoal(WallowEntity wallow, double speed, double stopDistance, Predicate<LivingEntity> selector) {
        this.wallow = wallow;
        this.speed = speed;
        this.stopDistance = stopDistance;
        this.selector = selector;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        target = findPreferredTarget();
        return target != null;
    }

    @Override
    public boolean canContinueToUse() {
        return target != null && target.isAlive() && wallow.distanceToSqr(target) > stopDistance();
    }

    @Override
    public void stop() {
        target = null;
        wallow.getNavigation().stop();
    }

    @Override
    public void tick() {
        wallow.noPhysics = true;
        wallow.setNoGravity(true);

        if (target == null) return;

        wallow.getLookControl().setLookAt(target, 30.0F, 30.0F);

        if (wallow.distanceToSqr(target) <= stopDistance()) {
            wallow.setDeltaMovement(wallow.getDeltaMovement().scale(0.85));
            wallow.getNavigation().stop();
            return;
        }

        flowyyTowardTarget();
    }

    private void flowyyTowardTarget() {
        Vec3 dir = directionToTargetCenter();
        if (dir.lengthSqr() < EPS) return;

        Vec3 desired = dir.normalize().scale(speed);
        Vec3 v = wallow.getDeltaMovement();

        wallow.setDeltaMovement(v.scale(0.80D).add(desired.scale(0.20D)));
    }

    private Vec3 directionToTargetCenter() {
        Vec3 targetCenter = target.position().add(0.0D, target.getBbHeight() * 0.5D, 0.0D);
        return targetCenter.subtract(wallow.position());
    }

    private double stopDistance() {
        return stopDistance * stopDistance;
    }

    private LivingEntity findPreferredTarget() {
        double range = wallow.getAttributeValue(Attributes.FOLLOW_RANGE);

        LivingEntity p = findClosestPlayer(range);
        return (p != null) ? p : findClosestNonPlayerLiving(range);
    }

    private LivingEntity findClosestPlayer(double range) {
        List<Player> players = wallow.level().getEntitiesOfClass(
                Player.class,
                wallow.getBoundingBox().inflate(range),
                p -> p.isAlive() && !p.isSpectator() && selector.test(p)
        );
        return closestByDistance(players);
    }

    private LivingEntity findClosestNonPlayerLiving(double range) {
        List<LivingEntity> mobs = wallow.level().getEntitiesOfClass(
                LivingEntity.class,
                wallow.getBoundingBox().inflate(range),
                e -> e.isAlive() && e != wallow && !(e instanceof Player) && selector.test(e)
        );
        return closestByDistance(mobs);
    }

    private LivingEntity closestByDistance(List<? extends LivingEntity> list) {
        LivingEntity best = null;
        double bestDist2 = Double.MAX_VALUE;

        for (LivingEntity e : list) {
            double d2 = wallow.distanceToSqr(e);
            if (d2 < bestDist2) {
                bestDist2 = d2;
                best = e;
            }
        }

        return best;
    }
}