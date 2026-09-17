package com.jamiedev.bygone.common.entity.ai.goal;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

public class GeistMeleeAttackGoal extends MeleeAttackGoal {

	public GeistMeleeAttackGoal(PathfinderMob mob, double speedModifier, boolean followingTargetEvenIfNotSeen) {
		super(mob, speedModifier, followingTargetEvenIfNotSeen);
	}

}
