package com.jamiedev.bygone.common.entity;

import com.jamiedev.bygone.common.entity.ai.AvoidBlockGoal;
import com.jamiedev.bygone.core.init.JamiesModTag;
import com.jamiedev.bygone.core.registry.BGBlocks;
import com.jamiedev.bygone.core.registry.BGDamageTypes;
import com.jamiedev.bygone.core.registry.BGMobEffects;
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
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
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

    private static final EntityDataAccessor<Boolean> DATA_HIDING =
            SynchedEntityData.defineId(MournEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_SCREAMING =
            SynchedEntityData.defineId(MournEntity.class, EntityDataSerializers.BOOLEAN);

    public static final int SCREAM_DURATION = 30;
    public static final int PARALYSIS_DURATION = 70;
    public static final double AMBUSH_RANGE = 16.0;
    public static final double POUNCE_RANGE = 2.5;

    public AnimationState idleAnimationState = new AnimationState();

    private float stretchProgress = 1.0F;
    private float stretchProgressO = 1.0F;
    private int screamTicks;

    public MournEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 12;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 24.0)
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.ATTACK_DAMAGE, 5.0)
                .add(Attributes.FOLLOW_RANGE, 24.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.6);
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
        this.goalSelector.addGoal(4, new MournAmbushGoal(this, 1.0));
        this.goalSelector.addGoal(5, new MeleeAttackGoal(this, 1.25, true));
        this.goalSelector.addGoal(7, new MournGotoDarkGoal(this, 0.8, 10));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomStrollGoal(this, 0.6));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_HIDING, true);
        builder.define(DATA_SCREAMING, false);
    }

    public boolean isHiding() {
        return this.entityData.get(DATA_HIDING);
    }

    public void setHiding(boolean hiding) {
        this.entityData.set(DATA_HIDING, hiding);
    }

    public boolean isScreaming() {
        return this.entityData.get(DATA_SCREAMING);
    }

    public void setScreaming(boolean screaming) {
        this.entityData.set(DATA_SCREAMING, screaming);
    }

    public float getStretchProgress(float partialTick) {
        return Mth.lerp(partialTick, this.stretchProgressO, this.stretchProgress);
    }

    public void ambush(Player player) {
        this.setHiding(false);
        this.setScreaming(true);
        this.screamTicks = SCREAM_DURATION;
        this.playSound(BGSoundEvents.MOURN_SCREAM_EVENT, 2.0F, 1.0F);
        player.addEffect(new MobEffectInstance(BGMobEffects.PARALYZED.get(), PARALYSIS_DURATION, 0), this);
        this.setTarget(player);
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.screamTicks > 0) {
            this.screamTicks--;
            if (this.screamTicks == 0) {
                this.setScreaming(false);
            }
        }
        this.setHiding(this.getTarget() == null && this.screamTicks <= 0);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        this.stretchProgressO = this.stretchProgress;
        float targetStretch = this.isHiding() ? 0.0F : 1.0F;
        if (this.stretchProgress < targetStretch) {
            this.stretchProgress = Math.min(targetStretch, this.stretchProgress + 0.25F);
        } else if (this.stretchProgress > targetStretch) {
            this.stretchProgress = Math.max(targetStretch, this.stretchProgress - 0.15F);
        }
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

        if (this.level().isClientSide()) {
            this.idleAnimationState.startIfStopped(this.tickCount);
        }

        if (collidingHurtSpectralBlocks()) {
            this.hurt(BGDamageTypes.source(this.level(), BGDamageTypes.HAUNTED, this, this.getLastAttacker()), 1);
        }
    }

    public static boolean canSpawn(EntityType<? extends Mob> type, LevelAccessor level, MobSpawnType reason, BlockPos blockPos, RandomSource random) {
        return level.getBlockState(blockPos.below()).is(JamiesModTag.WRAITH_SPAWNABLE_ON);
    }

    static class MournAmbushGoal extends Goal {
        private final MournEntity mourn;
        private final double speedModifier;
        private final TargetingConditions targeting = TargetingConditions.forCombat().range(AMBUSH_RANGE);
        @Nullable
        private Player prey;

        public MournAmbushGoal(MournEntity mourn, double speedModifier) {
            this.mourn = mourn;
            this.speedModifier = speedModifier;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            if (!this.mourn.isHiding() || this.mourn.getTarget() != null) {
                return false;
            }
            this.prey = this.mourn.level().getNearestPlayer(this.targeting, this.mourn);
            return this.prey != null;
        }

        @Override
        public boolean canContinueToUse() {
            return this.prey != null && this.prey.isAlive() && this.mourn.isHiding()
                    && this.mourn.getTarget() == null
                    && this.mourn.distanceTo(this.prey) < AMBUSH_RANGE + 4;
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
            double distance = this.mourn.distanceTo(this.prey);
            if (distance <= POUNCE_RANGE) {
                this.mourn.ambush(this.prey);
                return;
            }
            if (distance < 8 && this.isPlayerLookingAtMourn(this.prey)) {
                this.mourn.setTarget(this.prey);
                return;
            }
            this.mourn.getNavigation().moveTo(this.prey, this.speedModifier);
        }

        private boolean isPlayerLookingAtMourn(Player player) {
            Vec3 viewVector = player.getViewVector(1.0F).normalize();
            Vec3 toMourn = new Vec3(
                    this.mourn.getX() - player.getX(),
                    this.mourn.getY(0.5) - player.getEyeY(),
                    this.mourn.getZ() - player.getZ()
            );
            double length = toMourn.length();
            toMourn = toMourn.normalize();
            double dot = viewVector.dot(toMourn);
            return dot > 1.0 - 0.025 / length && player.hasLineOfSight(this.mourn);
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
            return this.mourn.isHiding() && this.mourn.getTarget() == null && super.canUse();
        }

        @Override
        protected boolean isValidTarget(@NotNull LevelReader level, @NotNull BlockPos pos) {
            return level.isEmptyBlock(pos.above()) && level.getMaxLocalRawBrightness(pos.above()) <= 4;
        }
    }
}
