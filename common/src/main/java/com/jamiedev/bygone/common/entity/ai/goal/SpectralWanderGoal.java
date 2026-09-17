package com.jamiedev.bygone.common.entity.ai.goal;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomFlyingGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class SpectralWanderGoal extends WaterAvoidingRandomFlyingGoal {

	public static final int DESTINATIONS_TO_CHECK = 10;

	public SpectralWanderGoal(PathfinderMob mob, double speed) {
		super(mob, speed);
	}

	@Nullable
	@Override
	protected Vec3 getPosition() {
		RandomSource random = this.mob.getRandom();
		Level level = this.mob.level();
		BlockPos mobPos = this.mob.blockPosition();

		for (int i = 0; i < DESTINATIONS_TO_CHECK; i++) {
			int dx = Mth.nextInt(random, -10, 10);
			int dy = Mth.nextInt(random, -12, 13);
			int dz = Mth.nextInt(random, -10, 10);

			BlockPos candidate = mobPos.offset(dx, dy, dz);
			BlockPos ground = candidate.below();

			BlockState state = level.getBlockState(ground);
			for (int checkGround = 1; checkGround <= 2; checkGround++) {
				BlockState checkState = level.getBlockState(new BlockPos(
						ground.getX(),
						ground.getY() + checkGround,
						ground.getZ()
				));
				if (!checkState.isAir()) {
					break;
				}
			}
			if (state.isFaceSturdy(
					level,
					ground,
					Direction.DOWN
			) && !state.isAir() && level.isEmptyBlock(candidate) && level.isEmptyBlock(candidate.above())) {
				return Vec3.atCenterOf(candidate);
			}
		}

		return null; // No position found
	}

}
