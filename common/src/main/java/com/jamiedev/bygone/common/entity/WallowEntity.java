package com.jamiedev.bygone.common.entity;

import com.jamiedev.bygone.common.entity.ai.AvoidBlockGoal;
import com.jamiedev.bygone.core.init.JamiesModTag;
import com.jamiedev.bygone.core.registry.BGDamageTypes;
import com.jamiedev.bygone.core.registry.BGSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.ai.util.AirRandomPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class WallowEntity extends PathfinderMob
{
    public AnimationState floatAnimationState = new AnimationState();
    public AnimationState idleAnimationState = new AnimationState();

    public WallowEntity(EntityType<? extends WallowEntity> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new WallowEntityMoveControl(this);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new AvoidEntityGoal<>(this, HauntEntity.class, 16, 1, 1.5F));
        this.goalSelector.addGoal(1, new WallowEntity.WallowChaseTargetGoal(this));
        this.goalSelector.addGoal(1, new WallowEntity.RandomFloatAroundGoal(this));
        this.goalSelector.addGoal(2, new WallowEntity.WallowEntityLookGoal(this));
        this.goalSelector.addGoal(3, new FollowMobGoal(this, 1.0, 3.0F, 7.0F));

        this.goalSelector.addGoal(3, new AvoidBlockGoal(this, 16, 1.4, 1.6, (pos) -> {
            BlockState state = this.level().getBlockState(pos);
            return state.is(JamiesModTag.HURT_SPECTRAL_BLOCKS);
        }));

        this.goalSelector.addGoal(6, new WallowFollowPlayerGoal(this, 1.4F, 3.0F, 10.0F));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers(WallowEntity.class));
  }

    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {
    }

    public void travel(Vec3 travelVector) {
        if (this.isControlledByLocalInstance()) {
            if (this.isInWater()) {
                this.moveRelative(0.02F, travelVector);
                this.move(MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().scale((double)0.8F));
            } else if (this.isInLava()) {
                this.moveRelative(0.02F, travelVector);
                this.move(MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().scale((double)0.5F));
            } else {
                float f = 0.91F;
                if (this.onGround()) {
                    f = this.level().getBlockState(this.getBlockPosBelowThatAffectsMyMovement()).getBlock().getFriction() * 0.91F;
                }

                float f1 = 0.16277137F / (f * f * f);
                f = 0.91F;
                if (this.onGround()) {
                    f = this.level().getBlockState(this.getBlockPosBelowThatAffectsMyMovement()).getBlock().getFriction() * 0.91F;
                }

                this.moveRelative(this.onGround() ? 0.1F * f1 : 0.02F, travelVector);
                this.move(MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().scale((double)f));
            }
        }

        this.calculateEntityAnimation(false);
    }

    public boolean onClimbable() {
        return false;
    }
    
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10.0)
                .add(Attributes.MOVEMENT_SPEED, 0.01)
                .add(Attributes.FOLLOW_RANGE, 10.0);
    }

    public static final int FREEZE_LINGER = 200;

    private void freezeTouchingEntities() {
        List<LivingEntity> touching = this.level().getEntitiesOfClass(
                LivingEntity.class,
                this.getBoundingBox().inflate(0.2),
                (entity) -> entity != this
                        && entity.canFreeze()
                        && !entity.getType().is(JamiesModTag.NOT_FREEZE_WALLOW)
                        && !entity.getType().is(JamiesModTag.SPECTRAL)
        );
        for (LivingEntity target : touching) {
            boolean wasFullyFrozen = target.isFullyFrozen();
            int fullFreeze = target.getTicksRequiredToFreeze() + FREEZE_LINGER;
            if (target.getTicksFrozen() < fullFreeze) {
                target.setTicksFrozen(fullFreeze);
            }
            if (!wasFullyFrozen) {
                this.playSound(BGSoundEvents.WALLOW_FREEZE_ADDITIONS_EVENT, 0.7F, 1.2F + this.random.nextFloat() * 0.2F);
            }
        }
    }

    private boolean collidingSpectralBlocks() {
        AABB aabb = this.getBoundingBox().inflate(1.0F, 1.0F, 1.0F);
        return BlockPos.betweenClosedStream(aabb).anyMatch((collisionShape) -> {
            BlockState blockstate = this.level().getBlockState(collisionShape);
            return blockstate.is(JamiesModTag.SPECTRAL_BLOCKS);
        });
    }

    private boolean collidingHurtSpectralBlocks() {
        AABB aabb = this.getBoundingBox().inflate(1.0F, 1.0F, 1.0F);
        return BlockPos.betweenClosedStream(aabb).anyMatch((collisionShape) -> {
            BlockState blockstate = this.level().getBlockState(collisionShape);
            return blockstate.is(JamiesModTag.HURT_SPECTRAL_BLOCKS);
        });
    }


    private void setupAnimationStates() {
        this.idleAnimationState.startIfStopped(this.tickCount);
        if (this.getDeltaMovement().horizontalDistanceSqr() > 2.5000003E-7F) {
            this.floatAnimationState.startIfStopped(this.tickCount);
        } else {
            this.floatAnimationState.stop();
        }
   }

    public void tick() {
        this.setNoGravity(true);
        super.tick();

        if (this.level().isClientSide()) {
            this.setupAnimationStates();
        }
        if  (collidingHurtSpectralBlocks())
        {
            this.hurt(BGDamageTypes.source(this.level(), BGDamageTypes.HAUNTED, this, this.getLastAttacker()), 1);

        }

        if (!this.level().isClientSide()) {
            this.freezeTouchingEntities();
        }

        noPhysics = !collidingSpectralBlocks();

    }

    @Override
    protected SoundEvent getAmbientSound() {
        return BGSoundEvents.WALLOW_AMBIENT_ADDITIONS_EVENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return BGSoundEvents.WALLOW_HURT_ADDITIONS_EVENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return BGSoundEvents.WALLOW_DEATH_ADDITIONS_EVENT;
    }

    @Override
    public int getAmbientSoundInterval() {
        return 60;
    }

    @Override
    public boolean canFreeze() {
        return false;
    }


    static class WallowEntityLookGoal extends Goal {
        private final WallowEntity wallow;

        public WallowEntityLookGoal(WallowEntity wallow) {
            this.wallow = wallow;
            this.setFlags(EnumSet.of(Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return true;
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            if (this.wallow.getTarget() == null) {
                Vec3 vec3 = this.wallow.getDeltaMovement();
                this.wallow.setYRot(-((float)Mth.atan2(vec3.x, vec3.z)) * (180.0F / (float)Math.PI));
                this.wallow.yBodyRot = this.wallow.getYRot();
            } else {
                LivingEntity livingentity = this.wallow.getTarget();
                double d0 = 64.0;
                if (livingentity.distanceToSqr(this.wallow) < 4096.0) {
                    double d1 = livingentity.getX() - this.wallow.getX();
                    double d2 = livingentity.getZ() - this.wallow.getZ();
                    this.wallow.setYRot(-((float)Mth.atan2(d1, d2)) * (180.0F / (float)Math.PI));
                    this.wallow.yBodyRot = this.wallow.getYRot();
                }
            }
        }
    }

    static class WallowEntityMoveControl extends MoveControl {
        private final WallowEntity wallow;
        private int floatDuration;

        public WallowEntityMoveControl(WallowEntity wallow) {
            super(wallow);
            this.wallow = wallow;
        }

        @Override
        public void tick() {
            if (this.operation == MoveControl.Operation.MOVE_TO) {
                if (this.floatDuration-- <= 0) {
                    this.floatDuration = this.floatDuration + this.wallow.getRandom().nextInt(5) + 2;
                    Vec3 vec3 = new Vec3(this.wantedX - this.wallow.getX(), this.wantedY - this.wallow.getY(), this.wantedZ - this.wallow.getZ());
                    double d0 = vec3.length();
                    vec3 = vec3.normalize();
                    if (this.canReach(vec3, Mth.ceil(d0))) {
                        this.wallow.setDeltaMovement(this.wallow.getDeltaMovement().add(vec3.scale(0.1)));
                    } else {
                        this.operation = MoveControl.Operation.WAIT;
                    }
                }
            }
        }

        private boolean canReach(Vec3 pos, int length) {
            AABB aabb = this.wallow.getBoundingBox();

            for (int i = 1; i < length; i++) {
                aabb = aabb.move(pos);
                if (!this.wallow.level().noCollision(this.wallow, aabb)) {
                    return false;
                }
            }

            return true;
        }
    }

    static class WallowChaseTargetGoal extends Goal {
        private static final double CHASE_SPEED = 1.6;
        private final WallowEntity wallow;

        public WallowChaseTargetGoal(WallowEntity wallow) {
            this.wallow = wallow;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            LivingEntity target = this.wallow.getTarget();
            return target != null && target.isAlive() && !target.getType().is(JamiesModTag.SPECTRAL);
        }

        @Override
        public boolean canContinueToUse() {
            return this.canUse();
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            LivingEntity target = this.wallow.getTarget();
            if (target == null) {
                return;
            }
            this.wallow.getMoveControl().setWantedPosition(target.getX(), target.getY(0.5), target.getZ(), CHASE_SPEED);
        }
    }

    static class RandomFloatAroundGoal extends Goal {
        private final WallowEntity wallow;

        public RandomFloatAroundGoal(WallowEntity wallow) {
            this.wallow = wallow;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            MoveControl movecontrol = this.wallow.getMoveControl();
            if (!movecontrol.hasWanted()) {
                return true;
            } else {
                double d0 = movecontrol.getWantedX() - this.wallow.getX();
                double d1 = movecontrol.getWantedY() - this.wallow.getY();
                double d2 = movecontrol.getWantedZ() - this.wallow.getZ();
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                return d3 < 1.0 || d3 > 3600.0;
            }
        }

        @Override
        public boolean canContinueToUse() {
            return false;
        }

        @Override
        public void start() {
            RandomSource randomsource = this.wallow.getRandom();
            double d0 = this.wallow.getX() + (double)((randomsource.nextFloat() * 2.0F - 1.0F) * 16.0F);
            double d1 = this.wallow.getY() + (double)((randomsource.nextFloat() * 2.0F - 1.0F) * 16.0F);
            double d2 = this.wallow.getZ() + (double)((randomsource.nextFloat() * 2.0F - 1.0F) * 16.0F);
            this.wallow.getMoveControl().setWantedPosition(d0, d1, d2, 1.0);
        }
    }

    static class WallowFollowPlayerGoal extends Goal {
        private final WallowEntity mob;
        private final Predicate<Player> followPredicate;
        private final double speedModifier;
        private final PathNavigation navigation;
        private final float stopDistance;
        private final float areaSize;
        @javax.annotation.Nullable
        private Player followingMob;
        private int timeToRecalcPath;
        private float oldWaterCost;

        public WallowFollowPlayerGoal(WallowEntity mob, double speedModifier, float stopDistance, float areaSize) {
            this.mob = mob;
            this.followPredicate = Objects::nonNull;
            this.speedModifier = speedModifier;
            this.navigation = mob.getNavigation();
            this.stopDistance = stopDistance;
            this.areaSize = areaSize;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
            if (!(mob.getNavigation() instanceof GroundPathNavigation) && !(mob.getNavigation() instanceof FlyingPathNavigation)) {
                throw new IllegalArgumentException("Unsupported mob type for FollowPlayerGoal");
            }
        }

        public boolean canUse() {

                List<Player> list = this.mob.level().getEntitiesOfClass(Player.class, this.mob.getBoundingBox().inflate(this.areaSize), this.followPredicate);
                if (!list.isEmpty()) {
                    for (Player mob : list) {
                        if (!mob.isInvisible()) {
                            this.followingMob = mob;
                            return true;
                        }
                    }
                }


            return false;
        }

        public boolean canContinueToUse() {
            return this.followingMob != null && !this.navigation.isDone() && this.mob.distanceToSqr(this.followingMob) > (double) (this.stopDistance * this.stopDistance);
        }

        public void start() {
            this.timeToRecalcPath = 0;
            this.oldWaterCost = this.mob.getPathfindingMalus(PathType.WATER);
            this.mob.setPathfindingMalus(PathType.WATER, 0.0F);
        }

        public void stop() {
            this.followingMob = null;
            this.navigation.stop();
            this.mob.setPathfindingMalus(PathType.WATER, this.oldWaterCost);
        }

        public void tick() {
            if (this.followingMob != null && !this.mob.isLeashed()) {
                this.mob.getLookControl().setLookAt(this.followingMob, 10.0F, (float) this.mob.getMaxHeadXRot());
                if (--this.timeToRecalcPath <= 0) {
                    this.timeToRecalcPath = this.adjustedTickDelay(10);
                    double d0 = this.mob.getX() - this.followingMob.getX();
                    double d1 = this.mob.getY() - this.followingMob.getY();
                    double d2 = this.mob.getZ() - this.followingMob.getZ();
                    double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                    if (!(d3 <= (double) (this.stopDistance * this.stopDistance))) {
                        this.navigation.moveTo(this.followingMob, this.speedModifier);
                    } else {
                        this.navigation.stop();

                    }
                }
            }

        }
    }

}
