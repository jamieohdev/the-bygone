package com.jamiedev.bygone.entities.ai;

import java.util.EnumSet;
import java.util.Objects;
import java.util.function.Predicate;
import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.Goal.Flag;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

public class AvoidBlockGoal<T extends Block> extends Goal
{
    protected final PathfinderMob mob;
    protected final T toAvoid;
    protected final float maxDist;
    private final double walkSpeedModifier;
    private final double sprintSpeedModifier;

    @Nullable
    protected Path path;

    @Nullable
    public BlockPos blockPos;

    public AvoidBlockGoal(PathfinderMob mob, T toAvoid, float maxDist, double walkSpeedModifier, double sprintSpeedModifier) {
        this.mob = mob;
        this.toAvoid = toAvoid;
        this.maxDist = maxDist;
        this.walkSpeedModifier = walkSpeedModifier;
        this.sprintSpeedModifier = sprintSpeedModifier;
    }

    @Override
    public boolean canUse() {
        if(this.blockPos == null || this.mob.getTarget() != null) {
            return false;
        }
        if(!this.blockPos.closerThan(this.mob.blockPosition(), this.maxDist)) {
            return false;
        }
        Vec3 avoidTarget = Vec3.atCenterOf(this.blockPos);
        Vec3 vec3 = DefaultRandomPos.getPosAway(this.mob, 16, 7, avoidTarget);
        if (vec3 == null) {
            return false;
        }
        if (vec3.distanceToSqr(avoidTarget) < this.mob.distanceToSqr(avoidTarget)) {
            return false;
        }
        this.path = this.mob.getNavigation().createPath(vec3.x, vec3.y, vec3.z, 0);
        return this.path != null;
    }

    @Override
    public boolean canContinueToUse() {
        return !this.mob.getNavigation().isDone();
    }

    @Override
    public void start() {
        this.mob.getNavigation().moveTo(this.path, this.walkSpeedModifier);
    }

    @Override
    public void stop() {
        this.blockPos = null;
    }

    @Override
    public void tick() {
        if (this.blockPos != null && this.mob.distanceToSqr(Vec3.atCenterOf(this.blockPos)) < 49.0D) {
            this.mob.getNavigation().setSpeedModifier(this.sprintSpeedModifier);
        } else {
            this.mob.getNavigation().setSpeedModifier(this.walkSpeedModifier);
        }
    }
}
