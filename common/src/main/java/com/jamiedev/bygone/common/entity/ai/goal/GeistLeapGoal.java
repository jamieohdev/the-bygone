package com.jamiedev.bygone.common.entity.ai.goal;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;

public class GeistLeapGoal extends LeapAtTargetGoal {

	protected final Mob mob;

	public GeistLeapGoal(Mob mob, float yd) {
		super(mob, yd);
		this.mob = mob;
	}

	@Override
	public boolean canUse() {
		super.canUse();
		if (this.mob.hasControllingPassenger()) return false;

		LivingEntity target = this.mob.getTarget();
		if (target == null) return false;

		double distance = this.mob.distanceToSqr(target);
		if (distance < 2) return false;

		return this.mob.getRandom().nextInt(reducedTickDelay(5)) == 0;
	}

}
