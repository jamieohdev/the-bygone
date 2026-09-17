package com.jamiedev.bygone.common.entity.ai.goal;

import com.jamiedev.bygone.common.entity.GeistEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class GeistSwoopAttackGoal extends MeleeAttackGoal {

	protected static final float HOVER_HEIGHT = 5;
	protected static final float HOVER_DISTANCE = 5;
	protected static final int HOVER_TICKS = 40;

	protected final GeistEntity geist;
	protected final double speedModifier;

	protected Phase phase = Phase.HOVER;
	protected int hoverTimer = 0;
	protected int cooldownTimer = 0;
	protected boolean orbitFlipped = false;

	public GeistSwoopAttackGoal(GeistEntity entity, double speedModifier, boolean followingTargetEvenIfNotSeen) {
		super(entity, speedModifier, followingTargetEvenIfNotSeen);
		this.geist = entity;
		this.speedModifier = speedModifier;
		this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
	}

	@Override
	public void start() {
		super.start();
		this.phase = Phase.HOVER;
		this.hoverTimer = 0;
		this.cooldownTimer = 0;
	}

	@Override
	public void stop() {
		super.stop();
		this.geist.setDeltaMovement(Vec3.ZERO);
	}

	@Override
	public void tick() {
		LivingEntity target = this.geist.getTarget();
		if (target == null) return;

		this.cooldownTimer = Math.max(0, this.cooldownTimer - 1);

		switch (this.phase) {
			case HOVER -> this.tickHover(target);
			case SWOOP -> this.tickSwoop(target);
		}

		this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
		this.checkAndPerformAttack(target);
		this.geist.getLookControl().setLookAt(target, 30, 30);
	}

	private void tickHover(LivingEntity target) {
		double pi = Math.PI + (this.orbitFlipped ? Math.PI : 0);
		double angle = (this.geist.tickCount % 200) / 200F * 2 * pi;
		double orbitX = target.getX() + Math.cos(angle) * HOVER_DISTANCE;
		double orbitY = target.getY() + HOVER_HEIGHT;
		double orbitZ = target.getZ() + Math.sin(angle) * HOVER_DISTANCE;
		Vec3 hoverPos = new Vec3(orbitX, orbitY, orbitZ);

		this.moveTo(hoverPos, 4);

		double verticalDistance = this.geist.getY() - target.getY();
		boolean isLooming = verticalDistance > HOVER_HEIGHT * 0.75F;
		if (isLooming) {
			this.hoverTimer++;
		} else {
			this.hoverTimer = Math.max(0, this.hoverTimer - 1);
		}

		if (this.hoverTimer >= HOVER_TICKS && this.cooldownTimer <= 0) {
			this.startSwoop(target);
		}
	}

	protected void startSwoop(LivingEntity target) {
		Vec3 toTarget = target.position().subtract(this.geist.position());
		this.geist.setDeltaMovement(toTarget.normalize().scale(this.speedModifier));
		this.hoverTimer = -10;
		this.orbitFlipped = !this.orbitFlipped;
		this.phase = Phase.SWOOP;
	}

	protected void tickSwoop(LivingEntity target) {
		this.hoverTimer++;
		if (this.hoverTimer >= 0) {
			this.phase = Phase.HOVER;
		}
	}

	protected void moveTo(Vec3 pos, double speedModifier) {
		this.geist.getMoveControl().setWantedPosition(pos.x, pos.y, pos.z, speedModifier);
	}

	@Override
	protected void checkAndPerformAttack(LivingEntity target) {
		if (this.canPerformAttack(target)) {
			this.resetAttackCooldown();
			this.mob.swing(InteractionHand.MAIN_HAND);
			boolean damage = this.mob.doHurtTarget(target);
			if (!damage) this.hoverTimer -= 20;
		}
	}

	protected enum Phase {
		HOVER,
		SWOOP
	}

}
