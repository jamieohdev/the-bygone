package com.jamiedev.bygone.common.entity.ai.navigation;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class GeistPathNavigation extends FlyingPathNavigation {

	public GeistPathNavigation(Mob mob, Level level) {
		super(mob, level);
		this.setCanOpenDoors(false);
		this.setCanFloat(true);
		this.setCanPassDoors(true);
	}

	@Override
	public boolean isStableDestination(@NotNull BlockPos pos) {
		return this.level.getBlockState(pos).isAir();
	}

}
