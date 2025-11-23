package com.jamiedev.bygone.common.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.ZombieAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class MurklingEntity extends Monster implements RangedAttackMob
{
    protected final WaterBoundPathNavigation waterNavigation;
    
    public MurklingEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new MurklingEntityMoveControl(this);
        this.waterNavigation = new WaterBoundPathNavigation(this, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Zombie.createAttributes().add(Attributes.STEP_HEIGHT, 1.0);
    }


    protected void registerGoals()  {
        super.registerGoals();
        this.goalSelector.addGoal(1, new MurklingEntity.MurklingEntityAttackGoal(this, 1.0, false));
        this.goalSelector.addGoal(2, new RandomStrollGoal(this, 1.0));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, MurklingEntity.class).setAlertOthers(MurklingEntity.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, this::okTarget));
    }

    @Override
    public void performRangedAttack(LivingEntity livingEntity, float v) {
        
    }

    public boolean okTarget(@Nullable LivingEntity target) {
        return target != null && (!this.level().isDay() || target.isInWater());
    }

    @Override
    public boolean isPushedByFluid() {
        return !this.isSwimming();
    }

    boolean wantsToSwim() {
        LivingEntity livingentity = this.getTarget();
        return livingentity != null && livingentity.isInWater();
    }

    @Override
    public void travel(@NotNull Vec3 travelVector) {
        if (this.isControlledByLocalInstance() && this.isInWater() && this.wantsToSwim()) {
            this.moveRelative(0.01F, travelVector);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9));
        } else {
            super.travel(travelVector);
        }
    }

    @Override
    public void updateSwimming() {
        if (!this.level().isClientSide) {
            if (this.isEffectiveAi() && this.isInWater() && this.wantsToSwim()) {
                this.navigation = this.waterNavigation;
                this.setSwimming(true);
            } else {
                this.setSwimming(false);
            }
        }
    }

    @Override
    public boolean isVisuallySwimming() {
        return this.isSwimming();
    }

    protected boolean closeToNextPos() {
        Path path = this.getNavigation().getPath();
        if (path != null) {
            BlockPos blockpos = path.getTarget();
            if (blockpos != null) {
                double d0 = this.distanceToSqr((double)blockpos.getX(), (double)blockpos.getY(), (double)blockpos.getZ());
                if (d0 < 4.0) {
                    return true;
                }
            }
        }

        return false;
    }

    static class MurklingEntityAttackGoal extends MeleeAttackGoal {
        private final MurklingEntity murkling;

        public MurklingEntityAttackGoal(MurklingEntity murkling, double speedModifier, boolean followingTargetEvenIfNotSeen) {
            super(murkling, speedModifier, followingTargetEvenIfNotSeen);
            this.murkling = murkling;
        }

        @Override
        public boolean canUse() {
            return super.canUse() && this.murkling.okTarget(this.murkling.getTarget());
        }

        @Override
        public boolean canContinueToUse() {
            return super.canContinueToUse() && this.murkling.okTarget(this.murkling.getTarget());
        }
    }

    static class MurklingEntityMoveControl extends MoveControl {
        private final MurklingEntity murkling;

        public MurklingEntityMoveControl(MurklingEntity murkling) {
            super(murkling);
            this.murkling = murkling;
        }

        @Override
        public void tick() {
            LivingEntity livingentity = this.murkling.getTarget();
            if (this.murkling.wantsToSwim() && this.murkling.isInWater()) {
                if (livingentity != null && livingentity.getY() > this.murkling.getY()) {
                    this.murkling.setDeltaMovement(this.murkling.getDeltaMovement().add(0.0, 0.002, 0.0));
                }

                if (this.operation != MoveControl.Operation.MOVE_TO || this.murkling.getNavigation().isDone()) {
                    this.murkling.setSpeed(0.0F);
                    return;
                }

                double d0 = this.wantedX - this.murkling.getX();
                double d1 = this.wantedY - this.murkling.getY();
                double d2 = this.wantedZ - this.murkling.getZ();
                double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                d1 /= d3;
                float f = (float)(Mth.atan2(d2, d0) * 180.0F / (float)Math.PI) - 90.0F;
                this.murkling.setYRot(this.rotlerp(this.murkling.getYRot(), f, 90.0F));
                this.murkling.yBodyRot = this.murkling.getYRot();
                float f1 = (float)(this.speedModifier * this.murkling.getAttributeValue(Attributes.MOVEMENT_SPEED));
                float f2 = Mth.lerp(0.125F, this.murkling.getSpeed(), f1);
                this.murkling.setSpeed(f2);
                this.murkling.setDeltaMovement(this.murkling.getDeltaMovement().add((double)f2 * d0 * 0.005, (double)f2 * d1 * 0.1, (double)f2 * d2 * 0.005));
            } else {
                if (!this.murkling.onGround()) {
                    this.murkling.setDeltaMovement(this.murkling.getDeltaMovement().add(0.0, -0.008, 0.0));
                }

                super.tick();
            }
        }
    }

}
