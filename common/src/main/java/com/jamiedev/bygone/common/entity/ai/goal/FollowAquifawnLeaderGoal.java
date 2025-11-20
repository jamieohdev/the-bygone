package com.jamiedev.bygone.common.entity.ai.goal;

import com.jamiedev.bygone.common.entity.AquifawnEntity;
import com.mojang.datafixers.DataFixUtils;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.AbstractSchoolingFish;

import java.util.List;
import java.util.function.Predicate;

public class FollowAquifawnLeaderGoal extends Goal
{


    private static final int INTERVAL_TICKS = 200;
    private final AquifawnEntity mob;
    private int timeToRecalcPath;
    private int nextStartTick;

    public FollowAquifawnLeaderGoal(AquifawnEntity fish) {
        this.mob = fish;
        this.nextStartTick = this.nextStartTick(fish);
    }

    protected int nextStartTick(AquifawnEntity taskOwner) {
        return reducedTickDelay(200 + taskOwner.getRandom().nextInt(200) % 20);
    }

    public boolean canUse() {
        if (this.mob.hasFollowers()) {
            return false;
        } else if (this.mob.isFollower()) {
            return true;
        } else if (this.nextStartTick > 0) {
            --this.nextStartTick;
            return false;
        } else {
            this.nextStartTick = this.nextStartTick(this.mob);
            Predicate<AquifawnEntity> predicate = p_25258_ -> p_25258_.canBeFollowed() || !p_25258_.isFollower();
            List<? extends AquifawnEntity> list = this.mob
                    .level()
                    .getEntitiesOfClass((Class<? extends AquifawnEntity>)this.mob.getClass(),
                            this.mob.getBoundingBox().inflate(8.0, 8.0, 8.0), predicate);
            AquifawnEntity abstractschoolingfish =
                    DataFixUtils.orElse(list.stream()
                            .filter(AquifawnEntity::canBeFollowed).findAny(), this.mob);
            abstractschoolingfish.addFollowers(list.stream().filter(p_25255_ -> !p_25255_.isFollower()));
            return this.mob.isFollower();
        }
    }

    public boolean canContinueToUse() {
        return this.mob.isFollower() && this.mob.inRangeOfLeader();
    }

    public void start() {
        this.timeToRecalcPath = 0;
    }

    public void stop() {
        this.mob.stopFollowing();
    }

    public void tick() {
        if (--this.timeToRecalcPath <= 0) {
            this.timeToRecalcPath = this.adjustedTickDelay(10);
            this.mob.pathToLeader();
        }

    }
}
