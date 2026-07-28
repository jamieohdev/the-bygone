package com.jamiedev.bygone.common.entity;

import com.jamiedev.bygone.common.entity.ai.AvoidBlockGoal;
import com.jamiedev.bygone.common.entity.ai.goal.SpectralWanderGoal;
import com.jamiedev.bygone.core.init.JamiesModTag;
import com.jamiedev.bygone.core.registry.BGBlocks;
import com.jamiedev.bygone.core.registry.BGDamageTypes;
import com.jamiedev.bygone.core.registry.BGSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class WhisperEntity extends Monster implements FlyingAnimal {

    private static final EntityDataAccessor<Boolean> DATA_REVEALED =
            SynchedEntityData.defineId(WhisperEntity.class, EntityDataSerializers.BOOLEAN);

    public static final int REVEAL_CHECK_INTERVAL = 5;
    public static final float REVEAL_FADE_SPEED = 0.06F;
    public static final int DRAIN_INTERVAL = 30;
    public static final double DRAIN_RANGE = 5.0;
    public static final float DRAIN_AMOUNT = 1.0F;
    public static final float DRAIN_MINIMUM_HEALTH = 1.0F;

    public AnimationState idleAnimationState = new AnimationState();
    public AnimationState floatAnimationState = new AnimationState();

    private final TargetingConditions drainTargeting = TargetingConditions.forCombat().range(DRAIN_RANGE);
    private int revealCheckTicks;
    private int drainCooldown = DRAIN_INTERVAL;
    private float revealProgress;
    private float revealProgressO;

    public WhisperEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 8;
        this.moveControl = new FlyingMoveControl(this, 20, true);
        this.setNoGravity(true);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.MOVEMENT_SPEED, 0.16)
                .add(Attributes.FLYING_SPEED, 0.3)
                .add(Attributes.FOLLOW_RANGE, 20.0);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, HauntEntity.class, 16, 1, 1.5F));
        this.goalSelector.addGoal(3, new AvoidBlockGoal(this, 16, 1.4, 1.6, (pos) -> {
            BlockState state = this.level().getBlockState(pos);
            return state.is(JamiesModTag.HURT_SPECTRAL_BLOCKS);
        }));
        this.goalSelector.addGoal(3, new AvoidBlockGoal(this, 16, 1.4, 1.6, (pos) -> {
            BlockState state = this.level().getBlockState(pos);
            return state.is(BGBlocks.LITHOPLASMIC_POWDER.get());
        }));
        this.goalSelector.addGoal(4, new WhisperDriftToPlayerGoal(this, 0.9));
        this.goalSelector.addGoal(8, new SpectralWanderGoal(this, 0.5));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers(WhisperEntity.class));
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level level) {
        FlyingPathNavigation navigation = new FlyingPathNavigation(this, level) {
            @Override
            public boolean isStableDestination(@NotNull BlockPos pos) {
                return this.level.getBlockState(pos).isAir();
            }
        };
        navigation.setCanOpenDoors(false);
        navigation.setCanFloat(true);
        navigation.setCanPassDoors(true);
        return navigation;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_REVEALED, false);
    }

    public boolean isRevealed() {
        return this.entityData.get(DATA_REVEALED);
    }

    public void setRevealed(boolean revealed) {
        this.entityData.set(DATA_REVEALED, revealed);
    }

    public float getRevealProgress(float partialTick) {
        return Mth.lerp(partialTick, this.revealProgressO, this.revealProgress);
    }

    private void tickRevealProgress() {
        this.revealProgressO = this.revealProgress;
        float target = this.isRevealed() ? 1.0F : 0.0F;
        if (this.revealProgress < target) {
            this.revealProgress = Math.min(target, this.revealProgress + REVEAL_FADE_SPEED);
        } else if (this.revealProgress > target) {
            this.revealProgress = Math.max(target, this.revealProgress - REVEAL_FADE_SPEED);
        }
    }

    @Override
    public boolean isInvisible() {
        return super.isInvisible() || this.revealProgress <= 0.0F;
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();

        if (--this.revealCheckTicks <= 0) {
            this.revealCheckTicks = REVEAL_CHECK_INTERVAL;
            boolean lit = BlockPos.findClosestMatch(this.blockPosition(), 8, 4,
                    pos -> this.level().getBlockState(pos).is(JamiesModTag.LITHOPLASM_LIGHT_BLOCKS)).isPresent();
            this.setRevealed(lit);
        }

        if (--this.drainCooldown <= 0) {
            this.drainCooldown = DRAIN_INTERVAL;
            Player victim = this.level().getNearestPlayer(this.drainTargeting, this);
            if (victim != null && victim.getHealth() > DRAIN_MINIMUM_HEALTH) {
                victim.setHealth(Math.max(DRAIN_MINIMUM_HEALTH, victim.getHealth() - DRAIN_AMOUNT));
            }
        }
    }

    @Override
    public boolean canBeAffected(@NotNull MobEffectInstance potioneffect) {
        return !(potioneffect.is(MobEffects.POISON) || potioneffect.is(MobEffects.HARM) || potioneffect.is(MobEffects.WITHER)) && super.canBeAffected(potioneffect);
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return BGSoundEvents.WHISPER_AMBIENT_EVENT;
    }

    @Override
    public int getAmbientSoundInterval() {
        return 60;
    }

    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return BGSoundEvents.WHISPER_HURT_EVENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return BGSoundEvents.WHISPER_DEATH_EVENT;
    }

    @Override
    protected void playStepSound(@NotNull BlockPos pos, @NotNull BlockState state) {
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, @NotNull BlockState state, @NotNull BlockPos pos) {
    }

    @Override
    public boolean isFlying() {
        return !this.onGround();
    }

    @Override
    public boolean isFlapping() {
        return this.isFlying();
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    public boolean canFreeze() {
        return false;
    }

    @Override
    protected double getDefaultGravity() {
        return 0.0;
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

    @Override
    public void tick() {
        this.setNoGravity(true);
        super.tick();

        this.tickRevealProgress();

        if (this.level().isClientSide()) {
            this.setupAnimationStates();
        }

        if (collidingHurtSpectralBlocks()) {
            this.hurt(BGDamageTypes.source(this.level(), BGDamageTypes.HAUNTED, this, this.getLastAttacker()), 1);
        }
    }

    public static boolean canSpawn(EntityType<? extends Mob> type, LevelAccessor level, MobSpawnType reason, BlockPos blockPos, RandomSource random) {
        return level.getBlockState(blockPos.below()).is(JamiesModTag.WRAITH_SPAWNABLE_ON);
    }

    static class WhisperDriftToPlayerGoal extends Goal {
        private final WhisperEntity whisper;
        private final double speedModifier;
        private final TargetingConditions targeting = TargetingConditions.forCombat().range(12.0);
        @Nullable
        private Player followed;
        private int recalcTicks;

        public WhisperDriftToPlayerGoal(WhisperEntity whisper, double speedModifier) {
            this.whisper = whisper;
            this.speedModifier = speedModifier;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            this.followed = this.findFollowTarget();
            return this.followed != null && this.whisper.distanceTo(this.followed) > DRAIN_RANGE - 1;
        }

        @Override
        public boolean canContinueToUse() {
            if (this.followed == null || !this.followed.isAlive()) {
                return false;
            }
            float maxDistance = this.followed == this.whisper.getTarget() ? 32.0F : 16.0F;
            float distance = this.whisper.distanceTo(this.followed);
            return distance > DRAIN_RANGE - 1 && distance < maxDistance;
        }

        @Nullable
        private Player findFollowTarget() {
            if (this.whisper.getTarget() instanceof Player player && player.isAlive() && !player.isCreative() && !player.isSpectator()) {
                return player;
            }
            return this.whisper.level().getNearestPlayer(this.targeting, this.whisper);
        }

        @Override
        public void start() {
            this.recalcTicks = 0;
        }

        @Override
        public void stop() {
            this.followed = null;
            this.whisper.getNavigation().stop();
        }

        @Override
        public void tick() {
            if (this.followed == null) {
                return;
            }
            if (--this.recalcTicks <= 0) {
                this.recalcTicks = this.adjustedTickDelay(10);
                this.whisper.getNavigation().moveTo(this.followed.getX(), this.followed.getEyeY(), this.followed.getZ(), this.speedModifier);
            }
        }
    }
}
