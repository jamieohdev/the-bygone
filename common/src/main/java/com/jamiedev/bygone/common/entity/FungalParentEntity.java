package com.jamiedev.bygone.common.entity;

import com.jamiedev.bygone.core.registry.BGBlocks;
import com.jamiedev.bygone.core.registry.BGEntityTypes;
import com.jamiedev.bygone.core.registry.BGSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class FungalParentEntity extends Animal implements NeutralMob {

    private static final EntityDataAccessor<Boolean> WARNING;
    private static final float field_30352 = 6.0F;
    private static final UniformInt ANGER_TIME_RANGE;

    static {
        WARNING = SynchedEntityData.defineId(FungalParentEntity.class, EntityDataSerializers.BOOLEAN);
        ANGER_TIME_RANGE = TimeUtil.rangeOfSeconds(20, 39);
    }

    EnderMan ref;
    private float lastWarningAnimationProgress;
    private float warningAnimationProgress;
    private int warningSoundCooldown;
    private int angerTime;
    @Nullable
    private UUID angryAt;

    public FungalParentEntity(EntityType<? extends FungalParentEntity> entityType, Level world) {
        super(entityType, world);
    }

    public static AttributeSupplier.Builder createFungieAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 30.0).add(Attributes.FOLLOW_RANGE, 20.0).add(Attributes.MOVEMENT_SPEED, 0.25).add(Attributes.ATTACK_DAMAGE, 8.0);
    }

    public static boolean canSpawn(EntityType<FungalParentEntity> type,
                                   LevelAccessor serverWorldAccess,
                                   MobSpawnType spawnReason,
                                   BlockPos blockPos,
                                   RandomSource random
    ) {
        return serverWorldAccess.getBlockState(blockPos.below()).is(BGBlocks.SHELF_FUNGUS.get())
                || serverWorldAccess.getBlockState(blockPos).is(BGBlocks.SHELF_MOLD_MOSS.get())
                || serverWorldAccess.getBlockState(blockPos).is(BGBlocks.SHELF_MOLD_BLOCK.get())
                || serverWorldAccess.getBlockState(blockPos.below()).is(BGBlocks.SHELF_MYCELIUM.get());
    }

    public static <T extends Mob> boolean canSpawn(EntityType<T> tEntityType, ServerLevelAccessor serverWorldAccess, MobSpawnType mobSpawnType, BlockPos blockPos, RandomSource randomSource) {
        return serverWorldAccess.getBlockState(blockPos.below()).is(BGBlocks.SHELF_FUNGUS.get())
                || serverWorldAccess.getBlockState(blockPos).is(BGBlocks.SHELF_MOLD_MOSS.get())
                || serverWorldAccess.getBlockState(blockPos).is(BGBlocks.SHELF_MOLD_BLOCK.get())
                || serverWorldAccess.getBlockState(blockPos.below()).is(BGBlocks.SHELF_MYCELIUM.get());
    }

    @Override
    @Nullable
    public AgeableMob getBreedOffspring(ServerLevel world, AgeableMob entity) {
        return BGEntityTypes.FUNGAL_PARENT.get().create(world);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return false;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new FungalParentEntity.AttackGoal());
        this.goalSelector.addGoal(1, new PanicGoal(this, 4.0, (polarBear) -> {
            return polarBear.isBaby() ? DamageTypeTags.PANIC_CAUSES : DamageTypeTags.PANIC_ENVIRONMENTAL_CAUSES;
        }));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.25));
        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 0.8));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 63.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new FungalParentEntity.FungalParentRevengeGoal());
        this.targetSelector.addGoal(2, new FungalParentEntity.ProtectBabiesGoal());
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, this::isAngryAt));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, GlareEntity.class, 10, true, true, null));
        this.targetSelector.addGoal(5, new ResetUniversalAngerTargetGoal<>(this, true));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        this.readPersistentAngerSaveData(this.level(), nbt);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        this.addPersistentAngerSaveData(nbt);
    }

    @Override
    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(ANGER_TIME_RANGE.sample(this.random));
    }

    @Override
    public int getRemainingPersistentAngerTime() {
        return this.angerTime;
    }

    @Override
    public void setRemainingPersistentAngerTime(int angerTime) {
        this.angerTime = angerTime;
    }

    @Override
    @Nullable
    public UUID getPersistentAngerTarget() {
        return this.angryAt;
    }

    @Override
    public void setPersistentAngerTarget(@Nullable UUID angryAt) {
        this.angryAt = angryAt;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.isBaby() ? BGSoundEvents.FUNGUSPARENT_AMBIENT_BABY_ADDITIONS_EVENT : BGSoundEvents.FUNGUSPARENT_AMBIENT_ADDITIONS_EVENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return BGSoundEvents.FUNGUSPARENT_HURT_ADDITIONS_EVENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return BGSoundEvents.FUNGUSPARENT_DEATH_ADDITIONS_EVENT;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.CHICKEN_STEP, 0.15F, 1.0F);
    }

    protected void playWarningSound() {
        if (this.warningSoundCooldown <= 0) {
            this.makeSound(SoundEvents.CAMPFIRE_CRACKLE);
            this.warningSoundCooldown = 40;
        }

    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(WARNING, false);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            if (this.warningAnimationProgress != this.lastWarningAnimationProgress) {
                this.refreshDimensions();
            }

            this.lastWarningAnimationProgress = this.warningAnimationProgress;
            if (this.isWarning()) {
                this.warningAnimationProgress = Mth.clamp(this.warningAnimationProgress + 1.0F, 0.0F, 6.0F);
            } else {
                this.warningAnimationProgress = Mth.clamp(this.warningAnimationProgress - 1.0F, 0.0F, 6.0F);
            }
        }

        if (this.warningSoundCooldown > 0) {
            --this.warningSoundCooldown;
        }

        if (!this.level().isClientSide) {
            this.updatePersistentAnger((ServerLevel) this.level(), true);
        }

    }

    @Override
    public EntityDimensions getDefaultDimensions(Pose pose) {
        if (this.warningAnimationProgress > 0.0F) {
            float f = this.warningAnimationProgress / 6.0F;
            float g = 1.0F + f;
            return super.getDefaultDimensions(pose).scale(1.0F, g);
        } else {
            return super.getDefaultDimensions(pose);
        }
    }

    public boolean isWarning() {
        return this.entityData.get(WARNING);
    }

    public void setWarning(boolean warning) {
        this.entityData.set(WARNING, warning);
    }

    public float getWarningAnimationProgress(float tickDelta) {
        return Mth.lerp(tickDelta, this.lastWarningAnimationProgress, this.warningAnimationProgress) / 6.0F;
    }

    @Override
    protected float getWaterSlowDown() {
        return 0.98F;
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType spawnReason, @Nullable SpawnGroupData entityData) {
        if (entityData == null) {
            entityData = new AgeableMob.AgeableMobGroupData(1.0F);
        }

        return super.finalizeSpawn(world, difficulty, spawnReason, entityData);
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        boolean bl = super.doHurtTarget(target);
        if (bl && this.getMainHandItem().isEmpty() && target instanceof LivingEntity) {
            float f = this.level().getCurrentDifficultyAt(this.blockPosition()).getEffectiveDifficulty();
            ((LivingEntity) target).addEffect(new MobEffectInstance(MobEffects.POISON, 20 * (int) f), this);
        }

        return bl;
    }

    private class AttackGoal extends MeleeAttackGoal {
        public AttackGoal() {
            super(FungalParentEntity.this, 1.25, true);
        }

        @Override
        protected void checkAndPerformAttack(LivingEntity target) {
            if (this.canPerformAttack(target)) {
                this.resetAttackCooldown();
                this.mob.doHurtTarget(target);
                FungalParentEntity.this.setWarning(false);
            } else if (this.mob.distanceToSqr(target) < (double) ((target.getBbWidth() + 3.0F) * (target.getBbWidth() + 3.0F))) {
                if (this.isTimeToAttack()) {
                    FungalParentEntity.this.setWarning(false);
                    this.resetAttackCooldown();
                }

                if (this.getTicksUntilNextAttack() <= 10) {
                    FungalParentEntity.this.setWarning(true);
                    FungalParentEntity.this.playWarningSound();
                }
            } else {
                this.resetAttackCooldown();
                FungalParentEntity.this.setWarning(false);
            }

        }

        @Override
        public void stop() {
            FungalParentEntity.this.setWarning(false);
            super.stop();
        }
    }

    class FungalParentRevengeGoal extends HurtByTargetGoal {
        public FungalParentRevengeGoal() {
            super(FungalParentEntity.this);
        }

        @Override
        public void start() {
            super.start();
            if (FungalParentEntity.this.isBaby()) {
                this.alertOthers();
                this.stop();
            }

        }

        @Override
        protected void alertOther(Mob mob, LivingEntity target) {
            if (mob instanceof FungalParentEntity && !mob.isBaby()) {
                super.alertOther(mob, target);
            }

        }
    }

    class ProtectBabiesGoal extends NearestAttackableTargetGoal<Player> {
        public ProtectBabiesGoal() {
            super(FungalParentEntity.this, Player.class, 20, true, true, null);
        }

        @Override
        public boolean canUse() {
            if (FungalParentEntity.this.isBaby()) {
                return false;
            } else {
                if (super.canUse()) {
                    List<FungalParentEntity> list =
                            FungalParentEntity.this.level().getEntitiesOfClass(FungalParentEntity.class,
                                    FungalParentEntity.this.getBoundingBox().inflate(8.0, 4.0, 8.0));

                    Iterator<FungalParentEntity> var2 = list.iterator();

                    while (var2.hasNext()) {
                        FungalParentEntity polarBearEntity = var2.next();


                        if (polarBearEntity.isBaby()) {
                            return true;
                        }
                    }
                }

                return false;
            }
        }

        @Override
        protected double getFollowDistance() {
            return super.getFollowDistance() * 0.5;
        }
    }
}
