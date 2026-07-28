package com.jamiedev.bygone.common.entity;

import com.jamiedev.bygone.common.entity.ai.AvoidBlockGoal;
import com.jamiedev.bygone.core.init.JamiesModTag;
import com.jamiedev.bygone.core.registry.BGBlocks;
import com.jamiedev.bygone.core.registry.BGDamageTypes;
import com.jamiedev.bygone.core.registry.BGMobEffects;
import com.jamiedev.bygone.core.registry.BGSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class MournEntity extends Monster {

    public static final byte STATE_HIDDEN = 0;
    public static final byte STATE_RISING = 1;
    public static final byte STATE_EXTENDED = 2;
    public static final byte STATE_SINKING = 3;

    private static final EntityDataAccessor<Byte> DATA_STATE =
            SynchedEntityData.defineId(MournEntity.class, EntityDataSerializers.BYTE);

    public static final int RISE_TICKS = 18;
    public static final int SINK_TICKS = 14;
    public static final int STRIKE_COOLDOWN = 24;
    public static final int PATIENCE_TICKS = 60;
    public static final int PARALYSIS_DURATION = 70;
    public static final double AMBUSH_RANGE = 20.0;
    public static final double EMERGE_RANGE = 2.5;
    public static final double STRIKE_RANGE = 3.0;

    private static final EntityDimensions HIDDEN_DIMENSIONS = EntityDimensions.scalable(1.2F, 0.3F);

    private int stateTicks;
    private int strikeCooldown;
    private int patience;
    private float emergeProgress;
    private float emergeProgressO;

    public MournEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 12;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 24.0)
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.ATTACK_DAMAGE, 6.0)
                .add(Attributes.FOLLOW_RANGE, 24.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MournStrikeGoal(this));
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, HauntEntity.class, 16, 1, 1.5F));
        this.goalSelector.addGoal(3, new AvoidBlockGoal(this, 16, 1.4, 1.6, (pos) -> {
            BlockState state = this.level().getBlockState(pos);
            return state.is(JamiesModTag.HURT_SPECTRAL_BLOCKS);
        }));
        this.goalSelector.addGoal(3, new AvoidBlockGoal(this, 16, 1.4, 1.6, (pos) -> {
            BlockState state = this.level().getBlockState(pos);
            return state.is(BGBlocks.LITHOPLASMIC_POWDER.get());
        }));
        this.goalSelector.addGoal(4, new MournStalkGoal(this, 1.0));
        this.goalSelector.addGoal(7, new MournGotoDarkGoal(this, 0.8, 10));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomStrollGoal(this, 0.6) {
            @Override
            public boolean canUse() {
                return MournEntity.this.isHidden() && super.canUse();
            }
        });
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_STATE, STATE_HIDDEN);
    }

    public byte getMournState() {
        return this.entityData.get(DATA_STATE);
    }

    private void setMournState(byte state) {
        if (this.getMournState() == state) {
            return;
        }
        this.entityData.set(DATA_STATE, state);
        this.refreshDimensions();
    }

    public boolean isHidden() {
        return this.getMournState() == STATE_HIDDEN;
    }

    public boolean isExtended() {
        return this.getMournState() == STATE_EXTENDED;
    }

    public boolean isRooted() {
        return !this.isHidden();
    }

    public float getEmergeProgress(float partialTick) {
        return Mth.lerp(partialTick, this.emergeProgressO, this.emergeProgress);
    }

    public float getStrikeProgress(float partialTick) {
        return this.getAttackAnim(partialTick);
    }

    @Override
    protected @NotNull EntityDimensions getDefaultDimensions(@NotNull Pose pose) {
        return this.isHidden() ? HIDDEN_DIMENSIONS : super.getDefaultDimensions(pose);
    }

    @Override
    public void onSyncedDataUpdated(@NotNull EntityDataAccessor<?> key) {
        if (DATA_STATE.equals(key)) {
            this.refreshDimensions();
        }
        super.onSyncedDataUpdated(key);
    }

    @Override
    public void travel(@NotNull Vec3 travelVector) {
        if (this.isRooted()) {
            super.travel(Vec3.ZERO);
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.0, 1.0, 0.0));
        } else {
            super.travel(travelVector);
        }
    }

    @Override
    public void knockback(double strength, double x, double z) {
        if (this.isRooted()) {
            return;
        }
        super.knockback(strength, x, z);
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        boolean hurt = super.hurt(source, amount);
        if (hurt && !this.level().isClientSide() && this.isHidden() && source.getEntity() instanceof LivingEntity) {
            this.beginRising();
        }
        return hurt;
    }

    public void beginRising() {
        if (!this.isHidden()) {
            return;
        }
        this.setMournState(STATE_RISING);
        this.stateTicks = RISE_TICKS;
        this.patience = PATIENCE_TICKS;
        this.getNavigation().stop();
        this.playSound(BGSoundEvents.MOURN_SCREAM_EVENT, 2.0F, 1.0F);
    }

    public void ambush(Player player) {
        this.beginRising();
        player.addEffect(new MobEffectInstance(BGMobEffects.PARALYZED.get(), PARALYSIS_DURATION, 0), this);
        this.setTarget(player);
    }

    private void beginSinking() {
        if (this.getMournState() != STATE_EXTENDED) {
            return;
        }
        this.setMournState(STATE_SINKING);
        this.stateTicks = SINK_TICKS;
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        this.dropInvalidTarget();

        if (this.strikeCooldown > 0) {
            this.strikeCooldown--;
        }

        switch (this.getMournState()) {
            case STATE_RISING -> {
                this.getNavigation().stop();
                if (--this.stateTicks <= 0) {
                    this.setMournState(STATE_EXTENDED);
                    this.patience = PATIENCE_TICKS;
                }
            }
            case STATE_EXTENDED -> {
                this.getNavigation().stop();
                LivingEntity target = this.getTarget();
                if (target != null && this.distanceTo(target) <= STRIKE_RANGE) {
                    this.patience = PATIENCE_TICKS;
                } else if (--this.patience <= 0) {
                    this.beginSinking();
                }
            }
            case STATE_SINKING -> {
                this.getNavigation().stop();
                if (--this.stateTicks <= 0) {
                    this.setMournState(STATE_HIDDEN);
                }
            }
            default -> {
            }
        }
    }

    private void dropInvalidTarget() {
        LivingEntity target = this.getTarget();
        if (target == null) {
            return;
        }
        if (!target.isAlive() || target.isRemoved()
                || (target instanceof Player player && (player.isCreative() || player.isSpectator()))) {
            this.setTarget(null);
            this.setLastHurtByMob(null);
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        this.emergeProgressO = this.emergeProgress;
        float target = switch (this.getMournState()) {
            case STATE_RISING, STATE_EXTENDED -> 1.0F;
            default -> 0.0F;
        };
        float speed = this.getMournState() == STATE_SINKING ? 1.0F / SINK_TICKS : 1.0F / RISE_TICKS;
        if (this.emergeProgress < target) {
            this.emergeProgress = Math.min(target, this.emergeProgress + speed);
        } else if (this.emergeProgress > target) {
            this.emergeProgress = Math.max(target, this.emergeProgress - speed);
        }
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putByte("MournState", this.getMournState());
        compound.putInt("MournStateTicks", this.stateTicks);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.entityData.set(DATA_STATE, compound.getByte("MournState"));
        this.stateTicks = compound.getInt("MournStateTicks");
        this.emergeProgress = this.isHidden() || this.getMournState() == STATE_SINKING ? 0.0F : 1.0F;
        this.emergeProgressO = this.emergeProgress;
        this.refreshDimensions();
    }

    @Override
    public boolean canBeAffected(@NotNull MobEffectInstance potioneffect) {
        return !(potioneffect.is(MobEffects.POISON) || potioneffect.is(MobEffects.HARM) || potioneffect.is(MobEffects.WITHER)) && super.canBeAffected(potioneffect);
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return BGSoundEvents.MOURN_HURT_EVENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return BGSoundEvents.MOURN_DEATH_EVENT;
    }

    @Override
    protected void playStepSound(@NotNull BlockPos pos, @NotNull BlockState state) {
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
    public boolean isInWall() {
        return !this.isRooted() && super.isInWall();
    }

    private boolean collidingHurtSpectralBlocks() {
        AABB aabb = this.getBoundingBox().inflate(1.0F, 1.0F, 1.0F);
        return BlockPos.betweenClosedStream(aabb).anyMatch((collisionShape) -> {
            BlockState blockstate = this.level().getBlockState(collisionShape);
            return blockstate.is(JamiesModTag.HURT_SPECTRAL_BLOCKS);
        });
    }

    @Override
    public void tick() {
        super.tick();

        if (collidingHurtSpectralBlocks()) {
            this.hurt(BGDamageTypes.source(this.level(), BGDamageTypes.HAUNTED, this, this.getLastAttacker()), 1);
        }
    }

    public static boolean canSpawn(EntityType<? extends Mob> type, LevelAccessor level, MobSpawnType reason, BlockPos blockPos, RandomSource random) {
        return level.getBlockState(blockPos.below()).is(JamiesModTag.WRAITH_SPAWNABLE_ON);
    }

    static class MournStrikeGoal extends Goal {
        private final MournEntity mourn;

        public MournStrikeGoal(MournEntity mourn) {
            this.mourn = mourn;
            this.setFlags(EnumSet.of(Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return this.mourn.isExtended() && this.mourn.getTarget() != null;
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            LivingEntity target = this.mourn.getTarget();
            if (target == null) {
                return;
            }
            this.mourn.getLookControl().setLookAt(target, 30.0F, 30.0F);
            if (this.mourn.strikeCooldown <= 0
                    && this.mourn.distanceTo(target) <= STRIKE_RANGE
                    && this.mourn.hasLineOfSight(target)) {
                this.mourn.strikeCooldown = STRIKE_COOLDOWN;
                this.mourn.swing(InteractionHand.MAIN_HAND);
                this.mourn.doHurtTarget(target);
            }
        }
    }

    static class MournStalkGoal extends Goal {
        private final MournEntity mourn;
        private final double speedModifier;
        private final TargetingConditions targeting = TargetingConditions.forCombat().range(AMBUSH_RANGE);
        @Nullable
        private Player prey;

        public MournStalkGoal(MournEntity mourn, double speedModifier) {
            this.mourn = mourn;
            this.speedModifier = speedModifier;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            if (!this.mourn.isHidden()) {
                return false;
            }
            if (this.mourn.getTarget() instanceof Player player && player.isAlive()) {
                this.prey = player;
                return true;
            }
            this.prey = this.mourn.level().getNearestPlayer(this.targeting, this.mourn);
            return this.prey != null;
        }

        @Override
        public boolean canContinueToUse() {
            return this.prey != null && this.prey.isAlive() && this.mourn.isHidden()
                    && this.mourn.distanceTo(this.prey) < AMBUSH_RANGE + 6;
        }

        @Override
        public void stop() {
            this.prey = null;
            this.mourn.getNavigation().stop();
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            if (this.prey == null) {
                return;
            }
            if (this.mourn.distanceTo(this.prey) <= EMERGE_RANGE) {
                this.mourn.ambush(this.prey);
                return;
            }
            this.mourn.getNavigation().moveTo(this.prey, this.speedModifier);
        }
    }

    static class MournGotoDarkGoal extends MoveToBlockGoal {
        private final MournEntity mourn;

        public MournGotoDarkGoal(MournEntity mourn, double speedModifier, int searchRange) {
            super(mourn, speedModifier, searchRange);
            this.mourn = mourn;
        }

        @Override
        public boolean canUse() {
            return this.mourn.isHidden() && this.mourn.getTarget() == null && super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            return this.mourn.isHidden() && super.canContinueToUse();
        }

        @Override
        protected boolean isValidTarget(@NotNull LevelReader level, @NotNull BlockPos pos) {
            return level.isEmptyBlock(pos.above()) && level.getMaxLocalRawBrightness(pos.above()) <= 4;
        }
    }
}
