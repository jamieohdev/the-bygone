package com.jamiedev.bygone.common.entity;

import com.jamiedev.bygone.common.entity.ai.AvoidBlockGoal;
import com.jamiedev.bygone.common.entity.ai.goal.SpectralWanderGoal;
import com.jamiedev.bygone.common.weather.weather_types.HauntingsCategoryHolder;
import com.jamiedev.bygone.core.extension.LivingEntityExtension;
import com.jamiedev.bygone.core.init.JamiesModTag;
import com.jamiedev.bygone.core.registry.BGBlocks;
import com.jamiedev.bygone.core.registry.BGDamageTypes;
import com.jamiedev.bygone.core.registry.BGSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

public class ReaverEntity extends Monster implements FlyingAnimal {
    public static final int WINDUP_TICKS = 14;
    public static final int AIM_LOCK_TICKS = 4;
    public static final int LUNGE_TICKS = 8;
    public static final int FLURRY_TICKS = 9;
    public static final int CLAW_HIT_INTERVAL = 3;
    public static final int RECOVERY_DROP_START = 6;
    public static final int RECOVERY_LAND_TICK = 12;
    public static final int RECOVERY_SPIN_START = 17;
    public static final int RECOVERY_AIM_START = 27;
    public static final int RECOVERY_SPIN_END = 35;
    public static final int RECOVERY_TICKS = 42;
    public static final int HIT_RECOVERY_TICKS = 18;
    public static final int CHOMP_TICKS = 15;
    public static final int CHOMP_HIT_TICK = 5;
    private static final double HOVER_HEIGHT = 0.6;
    private static final double LUNGE_SPEED = 0.9;
    private static final float CLAW_DAMAGE_MULTIPLIER = 0.4F;
    private static final AttackPhase[] ATTACK_PHASES = AttackPhase.values();
    private static final EntityDataAccessor<Byte> DATA_ATTACK_PHASE =
            SynchedEntityData.defineId(ReaverEntity.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Integer> DATA_PHASE_START =
            SynchedEntityData.defineId(ReaverEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Byte> DATA_CLAW_PATTERN =
            SynchedEntityData.defineId(ReaverEntity.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Byte> DATA_RECOVERY_SOURCE =
            SynchedEntityData.defineId(ReaverEntity.class, EntityDataSerializers.BYTE);

    public final AnimationState floatAnimationState = new AnimationState();
    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState meleeAnimationState = new AnimationState();
    private ReaverAttackGoal attackGoal;

    public ReaverEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 5;
        this.moveControl = new FlyingMoveControl(this, 35, true) {
            @Override
            public void tick() {
                if (getAttackPhase() == AttackPhase.IDLE) {
                    super.tick();
                } else {
                    setYya(0);
                    setZza(0);
                }
            }
        };
        this.setNoGravity(true);
        this.setPathfindingMalus(PathType.DANGER_FIRE, -1.0F);
        this.setPathfindingMalus(PathType.DAMAGE_FIRE, -1.0F);
        this.setPathfindingMalus(PathType.WATER, -1.0F);
        this.setPathfindingMalus(PathType.FENCE, -1.0F);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.2)
                .add(Attributes.FLYING_SPEED, 0.9)
                .add(Attributes.FOLLOW_RANGE, 18.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.8)
                .add(Attributes.ATTACK_DAMAGE, 7.0)
                .add(Attributes.MAX_HEALTH, 32.0);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new AvoidBlockGoal(this, 16, 1.4, 1.6,
                pos -> this.level().getBlockState(pos).is(JamiesModTag.HURT_SPECTRAL_BLOCKS)));
        this.goalSelector.addGoal(2, new AvoidBlockGoal(this, 16, 1.4, 1.6,
                pos -> this.level().getBlockState(pos).is(BGBlocks.LITHOPLASMIC_POWDER.get())));
        this.attackGoal = new ReaverAttackGoal();
        this.goalSelector.addGoal(3, this.attackGoal);
        this.goalSelector.addGoal(8, new SpectralWanderGoal(this, 0.6));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
        this.targetSelector.addGoal(1,
                new NearestAttackableTargetGoal<>(this, Player.class, 0, true, false, null)
                        .setUnseenMemoryTicks(300));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this, ReaverEntity.class));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Mob.class, 5, true, false,
                mob -> !(mob instanceof ReaverEntity)));
    }

    @Override
    public boolean canAttackType(EntityType<?> type) {
        return type != this.getType();
    }

    @Override
    public boolean canAttack(LivingEntity target) {
        return !(target instanceof ReaverEntity) && super.canAttack(target);
    }

    @Override
    protected AABB getAttackBoundingBox() {
        return super.getAttackBoundingBox().expandTowards(0, -HOVER_HEIGHT, 0);
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level level) {
        FlyingPathNavigation flyingPathNavigation = new FlyingPathNavigation(this, level) {
            @Override
            public boolean isStableDestination(@NotNull BlockPos pos) {
                return this.level.getBlockState(pos).isAir();
            }
        };
        flyingPathNavigation.setCanOpenDoors(false);
        flyingPathNavigation.setCanFloat(true);
        flyingPathNavigation.setCanPassDoors(true);
        return flyingPathNavigation;
    }

    public static boolean canSpawn(EntityType<? extends Mob> type, LevelAccessor level, MobSpawnType reason,
                                   BlockPos pos, RandomSource random) {
        return (reason != MobSpawnType.NATURAL || (level instanceof Level actualLevel
                && HauntingsCategoryHolder.checkHauntingsActive(actualLevel)))
                && level.getBlockState(pos.below()).is(JamiesModTag.REAVER_SPAWNABLE_ON);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_ATTACK_PHASE, (byte) AttackPhase.IDLE.ordinal());
        builder.define(DATA_PHASE_START, 0);
        builder.define(DATA_CLAW_PATTERN, (byte) 0);
        builder.define(DATA_RECOVERY_SOURCE, (byte) AttackPhase.IDLE.ordinal());
    }

    public AttackPhase getAttackPhase() {
        return ATTACK_PHASES[this.entityData.get(DATA_ATTACK_PHASE)];
    }

    public int getClawPattern() {
        return this.entityData.get(DATA_CLAW_PATTERN);
    }

    public AttackPhase getRecoverySource() {
        return ATTACK_PHASES[this.entityData.get(DATA_RECOVERY_SOURCE)];
    }

    public boolean isTantrumRecovery() {
        return this.getRecoverySource() == AttackPhase.LUNGE;
    }

    public int getRecoveryDuration() {
        return this.isTantrumRecovery() ? RECOVERY_TICKS : HIT_RECOVERY_TICKS;
    }

    private double getGroundClearance() {
        if (this.getAttackPhase() != AttackPhase.RECOVERY || !this.isTantrumRecovery()) {
            return HOVER_HEIGHT;
        }
        float elapsed = this.getAttackPhaseTicks(0);
        float drop = Mth.clamp((elapsed - RECOVERY_DROP_START)
                / (RECOVERY_LAND_TICK - RECOVERY_DROP_START), 0, 1);
        float rise = Mth.clamp((elapsed - RECOVERY_SPIN_START)
                / (RECOVERY_SPIN_END - RECOVERY_SPIN_START), 0, 1);
        return Mth.lerp(drop * (1.0F - rise), HOVER_HEIGHT, 0.1);
    }

    private static AABB getFloorSearchArea(AABB body, Vec3 movement) {
        return body.move(movement.x, 0, movement.z)
                .expandTowards(0, Math.min(0, movement.y) - HOVER_HEIGHT, 0);
    }

    private static double limitVerticalMovement(AABB body, Vec3 movement, Iterable<VoxelShape> collisions, double clearance) {
        AABB destination = body.move(movement.x, 0, movement.z);
        double vertical = movement.y;
        for (VoxelShape shape : collisions) {
            for (AABB block : shape.toAabbs()) {
                if (block.maxX <= destination.minX || block.minX >= destination.maxX
                        || block.maxZ <= destination.minZ || block.minZ >= destination.maxZ) {
                    continue;
                }
                if (block.maxY <= (body.minY + body.maxY) * 0.5) {
                    double floor = block.maxY + clearance;
                    if (floor > body.minY + vertical) {
                        vertical = floor - body.minY;
                    }
                }
            }
        }
        return vertical;
    }

    public float getAttackPhaseTicks(float partialTicks) {
        return Math.max(0.0F, (int) this.level().getGameTime() - this.entityData.get(DATA_PHASE_START) + partialTicks);
    }

    private void setAttackPhase(AttackPhase phase) {
        this.entityData.set(DATA_PHASE_START, (int) this.level().getGameTime());
        this.entityData.set(DATA_ATTACK_PHASE, (byte) phase.ordinal());
    }

    @Override
    public void tick() {
        Vec3 previousPosition = this.position();
        this.setNoGravity(true);
        if (!this.level().isClientSide() && (!this.isAlive() || this.isNoAi())
                && this.getAttackPhase() != AttackPhase.IDLE) {
            this.attackGoal.stop();
        }
        super.tick();
        if (this.level().isClientSide()) {
            this.setupAnimationStates();
        } else {
            if (this.isAlive() && !this.isNoAi()) {
                this.attackGoal.checkLungeContact(previousPosition);
            }
            if (this.collidingBlocks(JamiesModTag.HURT_SPECTRAL_BLOCKS)) {
                this.hurt(BGDamageTypes.source(this.level(), BGDamageTypes.HAUNTED, this, this.getLastAttacker()), 1);
            }
        }
        this.noPhysics = !this.collidingBlocks(JamiesModTag.SPECTRAL_BLOCKS);
    }

    private void setupAnimationStates() {
        AttackPhase phase = this.getAttackPhase();
        this.idleAnimationState.startIfStopped(this.tickCount);
        this.floatAnimationState.animateWhen(phase == AttackPhase.IDLE
                && this.getDeltaMovement().horizontalDistanceSqr() > 2.5000003E-7F, this.tickCount);
        this.meleeAnimationState.animateWhen(phase == AttackPhase.CHOMP, this.tickCount);
    }

    @Override
    protected float tickHeadTurn(float yaw, float animationStep) {
        AttackPhase phase = this.getAttackPhase();
        if (phase != AttackPhase.IDLE && phase != AttackPhase.CHOMP) {
            this.setYBodyRot(this.getYRot());
            return animationStep;
        }
        return super.tickHeadTurn(yaw, animationStep);
    }

    @Override
    public void travel(@NotNull Vec3 travelVector) {
        if (!this.isControlledByLocalInstance()) {
            return;
        }
        if (this.getAttackPhase() != AttackPhase.IDLE) {
            this.move(MoverType.SELF, this.getDeltaMovement());
        } else {
            super.travel(travelVector);
        }
    }

    @Override
    public void move(@NotNull MoverType type, @NotNull Vec3 movement) {
        if (!((LivingEntityExtension) this).bygone$isHauntingsRising()) {
            AABB floorSearch = getFloorSearchArea(this.getBoundingBox(), movement);
            double vertical = limitVerticalMovement(this.getBoundingBox(), movement,
                    this.level().getBlockCollisions(this, floorSearch), this.getGroundClearance());
            if (vertical != movement.y) {
                movement = new Vec3(movement.x, vertical, movement.z);
                Vec3 velocity = this.getDeltaMovement();
                this.setDeltaMovement(velocity.x, Math.max(0, velocity.y), velocity.z);
            }
        }
        super.move(type, movement);
    }

    @Override
    public boolean canBeAffected(MobEffectInstance effect) {
        return !(effect.is(MobEffects.POISON) || effect.is(MobEffects.HARM) || effect.is(MobEffects.WITHER))
                && super.canBeAffected(effect);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return BGSoundEvents.WRAITH_AMBIENT_ADDITIONS_EVENT;
    }

    @Override
    public void playAttackSound() {
        this.playSound(BGSoundEvents.WRAITH_ATTACK_ADDITIONS_EVENT, 1.0F, 1.0F);
    }

    @Override
    protected @NotNull SoundEvent getHurtSound(@NotNull DamageSource source) {
        return BGSoundEvents.WRAITH_HURT_ADDITIONS_EVENT;
    }

    @Override
    protected @NotNull SoundEvent getDeathSound() {
        return BGSoundEvents.WRAITH_DEATH_ADDITIONS_EVENT;
    }

    @Override
    public boolean isFlying() {
        return !this.onGround();
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    public boolean isFreezing() {
        return false;
    }

    @Override
    public boolean canFreeze() {
        return false;
    }

    @Override
    protected double getDefaultGravity() {
        return 0.0;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, @NotNull BlockState state, @NotNull BlockPos pos) { }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.is(DamageTypeTags.IS_FREEZING)) {
            return false;
        }
        return super.hurt(source, amount);
    }

    private boolean collidingBlocks(TagKey<Block> tag) {
        return BlockPos.betweenClosedStream(this.getBoundingBox().inflate(1.0))
                .anyMatch(pos -> this.level().getBlockState(pos).is(tag));
    }

    public enum AttackPhase {
        IDLE,
        WINDUP,
        LUNGE,
        FLURRY,
        RECOVERY,
        CHOMP
    }

    private class ReaverAttackGoal extends Goal {
        private LivingEntity attackTarget;
        private Vec3 direction = Vec3.ZERO;
        private int nextLungeTick;
        private int nextChompTick;
        private boolean landedClawHit;
        private int lastStrikeTick = -1;
        private double recoveryStartY;
        private double recoveryLandY;

        private ReaverAttackGoal() {
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            LivingEntity target = getTarget();
            return getAttackPhase() != AttackPhase.IDLE || (target != null && canAttack(target));
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
        public boolean isInterruptable() {
            return getAttackPhase() == AttackPhase.IDLE;
        }

        @Override
        public void stop() {
            setAttackPhase(AttackPhase.IDLE);
            setDeltaMovement(Vec3.ZERO);
            getNavigation().stop();
            this.attackTarget = null;
            setAggressive(false);
        }

        @Override
        public void tick() {
            setAggressive(true);
            AttackPhase phase = getAttackPhase();
            int elapsed = (int) getAttackPhaseTicks(0);
            if (phase != AttackPhase.IDLE && phase != AttackPhase.RECOVERY
                    && (this.attackTarget == null || !canAttack(this.attackTarget) || getTarget() != this.attackTarget)) {
                this.recover();
                return;
            }
            if (phase == AttackPhase.IDLE) {
                this.tickIdle();
                return;
            }

            getNavigation().stop();
            setDeltaMovement(Vec3.ZERO);
            if (phase != AttackPhase.CHOMP && phase != AttackPhase.RECOVERY) {
                this.faceDirection();
            }
            switch (phase) {
                case WINDUP -> {
                    if (elapsed < WINDUP_TICKS - AIM_LOCK_TICKS) {
                        this.aim();
                    }
                    if (elapsed >= WINDUP_TICKS) {
                        this.begin(AttackPhase.LUNGE);
                        this.nextLungeTick = tickCount + 70;
                        setDeltaMovement(this.direction.scale(LUNGE_SPEED));
                    } else {
                        setDeltaMovement(this.direction.scale(-0.035));
                    }
                }
                case LUNGE -> {
                    if (elapsed >= LUNGE_TICKS || collidingBlocks(JamiesModTag.SPECTRAL_BLOCKS)
                            || collidingBlocks(JamiesModTag.HURT_SPECTRAL_BLOCKS)) {
                        this.recover();
                    } else {
                        setDeltaMovement(this.direction.scale(LUNGE_SPEED));
                    }
                }
                case FLURRY -> {
                    if (elapsed >= FLURRY_TICKS) {
                        this.recover();
                    } else if (elapsed % CLAW_HIT_INTERVAL == 0) {
                        this.strike(elapsed);
                    }
                }
                case RECOVERY -> {
                    if (elapsed >= getRecoveryDuration()) {
                        this.begin(AttackPhase.IDLE);
                        this.nextChompTick = tickCount + 8;
                    } else {
                        this.tickRecovery(elapsed);
                    }
                }
                case CHOMP -> {
                    getLookControl().setLookAt(this.attackTarget, 30, 30);
                    if (elapsed == CHOMP_HIT_TICK && isWithinMeleeAttackRange(this.attackTarget)
                            && hasLineOfSight(this.attackTarget)) {
                        doHurtTarget(this.attackTarget);
                    }
                    if (elapsed >= CHOMP_TICKS) {
                        this.begin(AttackPhase.IDLE);
                    }
                }
                default -> { }
            }
        }

        private void tickIdle() {
            LivingEntity target = getTarget();
            if (target == null) {
                return;
            }
            getLookControl().setLookAt(target, 30, 30);
            double distance = distanceToSqr(target);
            if (tickCount >= this.nextChompTick && isWithinMeleeAttackRange(target) && hasLineOfSight(target)) {
                this.attackTarget = target;
                this.begin(AttackPhase.CHOMP);
                this.nextChompTick = tickCount + 30;
            } else if (tickCount >= this.nextLungeTick && distance >= 4.0 && distance <= 42.25
                    && hasLineOfSight(target)) {
                this.attackTarget = target;
                this.aim();
                this.begin(AttackPhase.WINDUP);
                playSound(SoundEvents.PHANTOM_FLAP, 0.8F, 0.6F);
            } else {
                getNavigation().moveTo(target, 0.8);
            }
        }

        private void begin(AttackPhase phase) {
            getNavigation().stop();
            setDeltaMovement(Vec3.ZERO);
            setAttackPhase(phase);
        }

        private void recover() {
            entityData.set(DATA_RECOVERY_SOURCE, (byte) getAttackPhase().ordinal());
            this.recoveryStartY = getY();
            this.recoveryLandY = this.recoveryStartY;
            if (isTantrumRecovery()) {
                Vec3 drop = new Vec3(0, -1.5, 0);
                double landing = limitVerticalMovement(getBoundingBox(), drop,
                        level().getBlockCollisions(ReaverEntity.this,
                                getFloorSearchArea(getBoundingBox(), drop)), 0.1);
                this.recoveryLandY += landing > drop.y ? landing : -0.35;
            }
            this.begin(AttackPhase.RECOVERY);
        }

        private void tickRecovery(int elapsed) {
            double height;
            float nextTick = elapsed + 1;
            boolean tantrum = isTantrumRecovery();
            if (!tantrum) {
                height = this.recoveryStartY;
            } else if (nextTick < RECOVERY_SPIN_START) {
                float drop = Mth.clamp((nextTick - RECOVERY_DROP_START)
                        / (RECOVERY_LAND_TICK - RECOVERY_DROP_START), 0, 1);
                height = Mth.lerp(Mth.smoothstep(drop), this.recoveryStartY, this.recoveryLandY);
            } else if (nextTick < RECOVERY_SPIN_END) {
                float rise = (nextTick - RECOVERY_SPIN_START) / (RECOVERY_SPIN_END - RECOVERY_SPIN_START);
                height = Mth.lerp(1 - (1 - rise) * (1 - rise) * (1 - rise),
                        this.recoveryLandY, this.recoveryStartY + 0.25);
            } else {
                float settle = Mth.clamp((nextTick - RECOVERY_SPIN_END)
                        / (RECOVERY_TICKS - RECOVERY_SPIN_END), 0, 1);
                height = Mth.lerp(Mth.smoothstep(settle), this.recoveryStartY + 0.25, this.recoveryStartY);
            }
            setDeltaMovement(0, Mth.clamp(height - getY(), -0.3, 0.3), 0);
            LivingEntity target = getTarget();
            if ((!tantrum || elapsed >= RECOVERY_AIM_START) && target != null && canAttack(target)) {
                Vec3 offset = target.position().subtract(position());
                float yaw = (float) (Mth.atan2(-offset.x, offset.z) * Mth.RAD_TO_DEG);
                float facing = Mth.approachDegrees(getYRot(), yaw, 45);
                this.setFacing(facing);
                getLookControl().setLookAt(target, 180, 180);
            } else {
                this.faceDirection();
            }
            if (tantrum && elapsed == RECOVERY_SPIN_START) {
                playSound(SoundEvents.PLAYER_ATTACK_STRONG, 0.5F, 0.6F);
                ((ServerLevel) level()).sendParticles(ParticleTypes.POOF,
                        getX(), getY() + 0.1, getZ(), 6, 0.45, 0.05, 0.45, 0.01);
                playSound(SoundEvents.PHANTOM_FLAP, 0.65F, 1.2F);
            }
        }

        private void aim() {
            this.direction = this.attackTarget.getBoundingBox().getCenter()
                    .subtract(getBoundingBox().getCenter()).normalize();
            this.faceDirection();
        }

        private void faceDirection() {
            float yaw = (float) (Mth.atan2(-this.direction.x, this.direction.z) * Mth.RAD_TO_DEG);
            this.setFacing(yaw);
            Vec3 lookPosition = position().add(this.direction.scale(4));
            getLookControl().setLookAt(lookPosition.x, lookPosition.y, lookPosition.z, 180, 180);
        }

        private void setFacing(float yaw) {
            setYRot(yaw);
            setYBodyRot(yaw);
            setYHeadRot(yaw);
        }

        private void checkLungeContact(Vec3 previousPosition) {
            if (getAttackPhase() != AttackPhase.LUNGE || this.attackTarget == null || !canAttack(this.attackTarget)) {
                return;
            }
            Vec3 from = previousPosition.add(0, getBbHeight() * 0.5, 0);
            Vec3 to = getBoundingBox().getCenter();
            AABB contactBox = this.attackTarget.getBoundingBox()
                    .inflate(getBbWidth() * 0.5 + 0.25, getBbHeight() * 0.5 + 0.15, getBbWidth() * 0.5 + 0.25);
            if (hasLineOfSight(this.attackTarget) && (contactBox.contains(to) || contactBox.clip(from, to).isPresent())) {
                this.landedClawHit = false;
                this.lastStrikeTick = -1;
                int leftPattern = random.nextInt(3);
                int rightPattern = (leftPattern + 1 + random.nextInt(2)) % 3;
                entityData.set(DATA_CLAW_PATTERN, (byte) (leftPattern + rightPattern * 3));
                this.begin(AttackPhase.FLURRY);
                this.strike(0);
            }
        }

        private void strike(int elapsed) {
            if (this.lastStrikeTick == elapsed) {
                return;
            }
            this.lastStrikeTick = elapsed;
            Vec3 strikePos = getBoundingBox().getCenter().add(this.direction.scale(0.9));
            playSound(SoundEvents.PLAYER_ATTACK_SWEEP, 0.75F, 1.2F + elapsed * 0.04F);
            ((ServerLevel) level()).sendParticles(ParticleTypes.SWEEP_ATTACK,
                    strikePos.x, strikePos.y, strikePos.z, 1, 0, 0, 0, 0);
            if (!canAttack(this.attackTarget) || !hasLineOfSight(this.attackTarget)
                    || !getBoundingBox().inflate(0.9, 0.5, 0.9).intersects(this.attackTarget.getBoundingBox())) {
                return;
            }
            Vec3 toTarget = this.attackTarget.getBoundingBox().getCenter().subtract(getBoundingBox().getCenter());
            if (toTarget.normalize().dot(this.direction) < 0.0) {
                return;
            }

            int previousImmunity = this.attackTarget.invulnerableTime;
            DamageSource lastDamage = this.attackTarget.getLastDamageSource();
            DamageSource clawDamage = damageSources().mobAttack(ReaverEntity.this);
            float damage = EnchantmentHelper.modifyDamage((ServerLevel) level(), getWeaponItem(),
                    this.attackTarget, clawDamage,
                    (float) getAttributeValue(Attributes.ATTACK_DAMAGE) * CLAW_DAMAGE_MULTIPLIER);
            if (this.landedClawHit && lastDamage != null && lastDamage.getEntity() == ReaverEntity.this) {
                this.attackTarget.invulnerableTime = 0;
            }
            Vec3 previousMotion = this.attackTarget.getDeltaMovement();
            boolean hit;
            try {
                hit = this.attackTarget.hurt(clawDamage, damage);
            } finally {
                this.attackTarget.invulnerableTime = Math.max(previousImmunity, this.attackTarget.invulnerableTime);
            }
            if (hit) {
                this.landedClawHit = true;
                setLastHurtMob(this.attackTarget);
                if (elapsed + CLAW_HIT_INTERVAL < FLURRY_TICKS) {
                    this.attackTarget.setDeltaMovement(previousMotion);
                }
                EnchantmentHelper.doPostAttackEffects((ServerLevel) level(), this.attackTarget, clawDamage);
            }
        }
    }
}
