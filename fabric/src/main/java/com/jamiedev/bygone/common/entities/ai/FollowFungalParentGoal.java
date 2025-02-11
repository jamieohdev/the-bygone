package com.jamiedev.bygone.common.entities.ai;

import com.jamiedev.bygone.common.entities.FungalParentEntity;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;
import net.minecraft.world.entity.ai.goal.Goal;

public class FollowFungalParentGoal extends Goal
{
    public static final int HORIZONTAL_CHECK_RANGE = 8;
    public static final int VERTICAL_CHECK_RANGE = 4;
    public static final int MIN_DISTANCE = 3;
    private final FungalParentEntity animal;
    @Nullable
    private FungalParentEntity parent;
    private final double speed;
    private int delay;

    public FollowFungalParentGoal(FungalParentEntity animal, double speed) {
        this.animal = animal;
        this.speed = speed;
    }

    public boolean canUse() {
        if (this.animal.getAge() >= 0) {
            return false;
        } else {
            List<? extends FungalParentEntity> list = this.animal.level().getEntitiesOfClass(this.animal.getClass(), this.animal.getBoundingBox().inflate(8.0, 4.0, 8.0));
            FungalParentEntity animalEntity = null;
            double d = Double.MAX_VALUE;
            Iterator var5 = list.iterator();

            while(var5.hasNext()) {
                FungalParentEntity animalEntity2 = (FungalParentEntity)var5.next();
                if (animalEntity2.getAge() >= 0) {
                    double e = this.animal.distanceToSqr(animalEntity2);
                    if (!(e > d)) {
                        d = e;
                        animalEntity = animalEntity2;
                    }
                }
            }

            if (animalEntity == null) {
                return false;
            } else if (d < 9.0) {
                return false;
            } else {
                this.parent = animalEntity;
                return true;
            }
        }
    }

    public boolean canContinueToUse() {
        if (this.animal.getAge() >= 0) {
            return false;
        } else if (!this.parent.isAlive()) {
            return false;
        } else {
            double d = this.animal.distanceToSqr(this.parent);
            return !(d < 9.0) && !(d > 256.0);
        }
    }

    public void start() {
        this.delay = 0;
    }

    public void stop() {
        this.parent = null;
    }

    public void tick() {
        if (--this.delay <= 0) {
            this.delay = this.adjustedTickDelay(10);
            this.animal.getNavigation().moveTo(this.parent, this.speed);
        }
    }
}
