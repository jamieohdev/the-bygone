package com.jamiedev.bygone.common.entity;

import com.google.common.collect.ImmutableList;
import com.jamiedev.bygone.common.entity.ai.AvoidBlockGoal;
import com.jamiedev.bygone.common.entity.ai.SabeastAI;
import com.jamiedev.bygone.core.init.JamiesModTag;
import com.jamiedev.bygone.core.registry.BGSoundEvents;
import com.mojang.serialization.Dynamic;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Optional;
import java.util.function.Predicate;

public class SabeastEntity extends Monster {

    protected static final ImmutableList<SensorType<? extends Sensor<? super SabeastEntity>>> SENSOR_TYPES = ImmutableList.of(
            SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS, SensorType.HURT_BY
    );
    protected static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(
            MemoryModuleType.LOOK_TARGET,
            MemoryModuleType.NEAREST_LIVING_ENTITIES,
            MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES,
            MemoryModuleType.NEAREST_VISIBLE_PLAYER,
            MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM,
            MemoryModuleType.ITEM_PICKUP_COOLDOWN_TICKS,
            MemoryModuleType.HURT_BY,
            MemoryModuleType.HURT_BY_ENTITY,
            MemoryModuleType.WALK_TARGET,
            MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE,
            MemoryModuleType.ATTACK_TARGET,
            MemoryModuleType.ATTACK_COOLING_DOWN,
            MemoryModuleType.INTERACTION_TARGET,
            MemoryModuleType.PATH,
            MemoryModuleType.ANGRY_AT,
            MemoryModuleType.UNIVERSAL_ANGER,
            MemoryModuleType.NEAREST_ATTACKABLE,
            MemoryModuleType.AVOID_TARGET,
            MemoryModuleType.NEAREST_VISIBLE_NEMESIS,
            MemoryModuleType.NEAREST_PLAYER_HOLDING_WANTED_ITEM
    );
    private static final EntityDataAccessor<Boolean> DATA_STANDING_ID;
    private static final EntityDataAccessor<Boolean> DATA_IS_ATTACKING;
    private static final EntityDataAccessor<Boolean> DATA_REPEL_RUN;

    static {
        DATA_STANDING_ID = SynchedEntityData.defineId(SabeastEntity.class, EntityDataSerializers.BOOLEAN);
        DATA_IS_ATTACKING = SynchedEntityData.defineId(SabeastEntity.class, EntityDataSerializers.BOOLEAN);
        DATA_REPEL_RUN = SynchedEntityData.defineId(SabeastEntity.class, EntityDataSerializers.BOOLEAN);
    }

    public AnimationState walkAnimationState = new AnimationState();
    public AnimationState idleAnimationState = new AnimationState();
    public AnimationState meleeAnimationState = new AnimationState();
    public int meleeAttackInterval = 0;
    private int warningSoundTicks;
    private int checkRepelTicks = 0;

    public SabeastEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 50.0F)
                .add(Attributes.MOVEMENT_SPEED, 0.22F)
                .add(Attributes.ATTACK_DAMAGE, 8.0F)
                .add(Attributes.STEP_HEIGHT, 1.6F);
    }

    @Override
    protected void customServerAiStep() {
        this.level().getProfiler().push("sabeastBrain");
        this.getBrain().tick((ServerLevel) this.level(), this);
        this.level().getProfiler().pop();
        SabeastAI.updateActivity(this);
        super.customServerAiStep();
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new SabeastFreezeWhenLookedAt(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 2.0F, (p_350292_) -> p_350292_.isBaby() ? DamageTypeTags.PANIC_CAUSES : DamageTypeTags.PANIC_ENVIRONMENTAL_CAUSES));

        this.goalSelector.addGoal(3, new AvoidBlockGoal(this, 16, 1.4, 1.6, (pos) -> {
            BlockState state = this.level().getBlockState(pos);
            return state.is(JamiesModTag.SABEAST_REPELLENTS);
        }));

        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 1.0F));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new SabeastEntityMeleeAttackGoal());
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, (Predicate) null));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, MoobooEntity.class, 10, true, true, (Predicate) null));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_STANDING_ID, false);
        builder.define(DATA_IS_ATTACKING, false);
        builder.define(DATA_REPEL_RUN, false);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return BGSoundEvents.SABEAST_AMBIENT_ADDITIONS_EVENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return BGSoundEvents.SABEAST_HURT_ADDITIONS_EVENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return BGSoundEvents.SABEAST_DEATH_ADDITIONS_EVENT;
    }

    @Override
    public void playAttackSound() {
        this.playSound(BGSoundEvents.SABEAST_ATTACK_ADDITIONS_EVENT, 1.0F, 1.0F);
    }
    
    @Override
    protected Brain.Provider<SabeastEntity> brainProvider() {
        return Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
    }

    @Override
    protected Brain<?> makeBrain(Dynamic<?> dynamic) {
        return SabeastAI.makeBrain(this, this.brainProvider().makeBrain(dynamic));
    }

    @Override
    public Brain<SabeastEntity> getBrain() {
        return (Brain<SabeastEntity>) super.getBrain();
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
    }

    boolean isLookingAtMe(Player player) {
        ItemStack itemstack = player.getInventory().armor.get(3);
        if (itemstack.is(Blocks.CARVED_PUMPKIN.asItem())) {
            return false;
        } else {
            Vec3 vec3 = player.getViewVector(1.0F).normalize();
            Vec3 vec31 = new Vec3(this.getX() - player.getX(), this.getEyeY() - player.getEyeY(), this.getZ() - player.getZ());
            double d0 = vec31.length();
            vec31 = vec31.normalize();
            double d1 = vec3.dot(vec31);

            return d1 > (double) 1.0F - 0.025 / d0 && player.hasLineOfSight(this);
        }
    }

    private void setupAnimationStates() {
        this.idleAnimationState.startIfStopped(this.tickCount);

        if (this.entityData.get(DATA_IS_ATTACKING)) {
            this.walkAnimationState.stop();
            this.meleeAnimationState.startIfStopped(this.tickCount);
        } else {
            this.meleeAnimationState.stop();
        }

        if (!this.entityData.get(DATA_IS_ATTACKING)) {
            if (this.getDeltaMovement().horizontalDistanceSqr() > 2.5E-7F) {
                this.walkAnimationState.startIfStopped(this.tickCount);
            } else {
                this.walkAnimationState.stop();
            }
        }
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState block) {
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.is(DamageTypes.IN_FIRE) || source.is(DamageTypes.ON_FIRE)) {
            return super.hurt(source, amount * 4);
        }

        return super.hurt(source, (float) (amount * 0.8));
    }

    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            this.setupAnimationStates();
        }

        if (this.warningSoundTicks > 0) {
            --this.warningSoundTicks;
        }

        Level level = this.level();

        if (!level.isClientSide()) {
            if (this.checkRepelTicks <= 0) {
                Optional<BlockPos> repelPos = BlockPos.findClosestMatch(this.blockPosition(), 8, 4, pos -> level.getBlockState(pos).is(JamiesModTag.SABEAST_REPELLENTS));
                if (repelPos.isPresent()) {
                    this.entityData.set(DATA_REPEL_RUN, true);
                } else {
                    this.entityData.set(DATA_REPEL_RUN, false);
                }
                this.checkRepelTicks = 20;
            }
        }
        if (this.checkRepelTicks > 0) {
            this.checkRepelTicks--;
        }

        if (!this.level().isClientSide) {
            if (this.entityData.get(DATA_IS_ATTACKING)) {
                this.meleeAttackInterval++;
                if (this.meleeAttackInterval >= 40) {
                    this.entityData.set(DATA_IS_ATTACKING, false);
                    this.meleeAttackInterval = 0;
                    this.entityData.set(DATA_STANDING_ID, true);
                }
            }

            if (this.meleeAttackInterval == 10) {
                if (this.getTarget() != null) {
                    this.doHurtTarget(this.getTarget());
                }
            }

        }

    }

    public int getMaxSpawnClusterSize() {
        return 1;
    }

    protected static boolean isBrightEnoughToSpawn(BlockAndTintGetter level, BlockPos pos) {
        return level.getRawBrightness(pos, 0) > 1;
    }

    public boolean getDataRepelRun() {
        return this.entityData.get(DATA_REPEL_RUN);
    }

    public boolean isStanding() {
        return this.entityData.get(DATA_STANDING_ID);
    }

    public void setStanding(boolean standing) {
        this.entityData.set(DATA_STANDING_ID, standing);
    }

    public boolean getDataIsAttacking() {
        return this.entityData.get(DATA_IS_ATTACKING);
    }

    public void setDataIsAttacking(boolean attacking) {
        this.entityData.set(DATA_IS_ATTACKING, attacking);
    }

    protected void playWarningSound() {
        if (this.warningSoundTicks <= 0) {
            this.makeSound(SoundEvents.POLAR_BEAR_WARNING);
            this.warningSoundTicks = 40;
        }

    }

    static class SabeastFreezeWhenLookedAt extends Goal {
        private final SabeastEntity sabeast;
        @Nullable
        private LivingEntity target;

        public SabeastFreezeWhenLookedAt(SabeastEntity sabeast) {
            this.sabeast = sabeast;
            this.setFlags(EnumSet.of(Flag.JUMP, Flag.MOVE));
        }

        public boolean canUse() {
            this.target = this.sabeast.getTarget();
            if (!(this.target instanceof Player)) {
                return false;
            } else {
                double d0 = this.target.distanceToSqr(this.sabeast);

                if (this.target instanceof Player) {
                    this.target.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 200, 0, true, true), this.sabeast);
                }

                return !(d0 > (double) 256.0F) && this.sabeast.isLookingAtMe((Player) this.target);
            }
        }

        public void start() {
            this.sabeast.getNavigation().stop();
        }

        public void tick() {
            assert this.target != null;
            this.sabeast.getLookControl().setLookAt(this.target.getX(), this.target.getEyeY(), this.target.getZ());


        }
    }

    class SabeastEntityMeleeAttackGoal extends Goal {
        private static final long COOLDOWN_BETWEEN_CAN_USE_CHECKS = 20L;
        protected final SabeastEntity mob;
        private final double speedModifier;
        private final boolean followingTargetEvenIfNotSeen;
        private final int attackInterval = 40;
        private Path path;
        private double pathedTargetX;
        private double pathedTargetY;
        private double pathedTargetZ;
        private int ticksUntilNextPathRecalculation;
        private int ticksUntilNextAttack;
        private long lastCanUseCheck;

        public SabeastEntityMeleeAttackGoal() {
            this.mob = SabeastEntity.this;
            this.speedModifier = 1.25F;
            this.followingTargetEvenIfNotSeen = true;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            long i = this.mob.level().getGameTime();

            if (this.mob instanceof SabeastEntity sabeast) {
                if (sabeast.getDataRepelRun()) {
                    return false;
                }
            }

            if (i - this.lastCanUseCheck < 20L) {
                return false;
            } else {
                this.lastCanUseCheck = i;
                LivingEntity livingentity = this.mob.getTarget();
                if (livingentity == null) {
                    return false;
                } else if (!livingentity.isAlive()) {
                    return false;
                } else {
                    this.path = this.mob.getNavigation().createPath(livingentity, 0);
                    return this.path != null || this.mob.isWithinMeleeAttackRange(livingentity);
                }
            }
        }

        @Override
        public boolean canContinueToUse() {
            LivingEntity livingentity = this.mob.getTarget();
            if (this.mob instanceof SabeastEntity sabeast) {
                if (sabeast.getDataRepelRun()) {
                    return false;
                }
            }
            if (livingentity == null) {
                return false;
            } else if (!livingentity.isAlive()) {
                return false;
            } else if (!this.followingTargetEvenIfNotSeen) {
                return !this.mob.getNavigation().isDone();
            } else {
                return this.mob.isWithinRestriction(livingentity.blockPosition()) && (!(livingentity instanceof Player) || !livingentity.isSpectator() && !((Player) livingentity).isCreative());
            }
        }

        @Override
        public void start() {
            this.mob.getNavigation().moveTo(this.path, this.speedModifier);
            this.mob.setAggressive(true);
            this.ticksUntilNextPathRecalculation = 0;
            this.ticksUntilNextAttack = 0;
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            LivingEntity livingentity = this.mob.getTarget();
            if (livingentity != null) {
                this.mob.getLookControl().setLookAt(livingentity, 30.0F, 30.0F);
                this.ticksUntilNextPathRecalculation = Math.max(this.ticksUntilNextPathRecalculation - 1, 0);
                if ((this.followingTargetEvenIfNotSeen || this.mob.getSensing().hasLineOfSight(livingentity))
                        && this.ticksUntilNextPathRecalculation <= 0
                        && (
                        this.pathedTargetX == 0.0 && this.pathedTargetY == 0.0 && this.pathedTargetZ == 0.0
                                || livingentity.distanceToSqr(this.pathedTargetX, this.pathedTargetY, this.pathedTargetZ) >= 1.0
                                || this.mob.getRandom().nextFloat() < 0.05F
                )) {
                    this.pathedTargetX = livingentity.getX();
                    this.pathedTargetY = livingentity.getY();
                    this.pathedTargetZ = livingentity.getZ();
                    this.ticksUntilNextPathRecalculation = 4 + this.mob.getRandom().nextInt(7);
                    double d0 = this.mob.distanceToSqr(livingentity);
                    if (d0 > 1024.0) {
                        this.ticksUntilNextPathRecalculation += 10;
                    } else if (d0 > 256.0) {
                        this.ticksUntilNextPathRecalculation += 5;
                    }

                    if (!this.mob.getNavigation().moveTo(livingentity, this.speedModifier)) {
                        this.ticksUntilNextPathRecalculation += 15;
                    }

                    this.ticksUntilNextPathRecalculation = this.adjustedTickDelay(this.ticksUntilNextPathRecalculation);
                }

                this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
                this.checkAndPerformAttack(livingentity);
            }
        }

        protected void checkAndPerformAttack(LivingEntity target) {
            if (this.canPerformAttack(target) && !this.mob.getDataIsAttacking()) {
                this.mob.setDataIsAttacking(true);
                SabeastEntity.this.setStanding(false);
                this.resetAttackCooldown();
            }

        }

        public void stop() {
            SabeastEntity.this.setStanding(false);
            LivingEntity livingentity = this.mob.getTarget();
            if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(livingentity)) {
                this.mob.setTarget(null);
            }
            this.mob.setDataIsAttacking(false);
            this.mob.setAggressive(false);
            this.mob.getNavigation().stop();
        }

        protected void resetAttackCooldown() {
            this.ticksUntilNextAttack = this.adjustedTickDelay(40);
        }

        protected boolean isTimeToAttack() {
            return this.ticksUntilNextAttack <= 0;
        }

        protected boolean canPerformAttack(LivingEntity entity) {
            return this.isTimeToAttack() && this.mob.isWithinMeleeAttackRange(entity) && this.mob.getSensing().hasLineOfSight(entity);
        }

        protected int getTicksUntilNextAttack() {
            return this.ticksUntilNextAttack;
        }

        protected int getAttackInterval() {
            return this.adjustedTickDelay(20);
        }

    }
}
