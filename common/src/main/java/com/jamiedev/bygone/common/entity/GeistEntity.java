package com.jamiedev.bygone.common.entity;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.common.entity.ai.AvoidBlockGoal;
import com.jamiedev.bygone.common.entity.ai.goal.GeistGotoLightGoal;
import com.jamiedev.bygone.common.entity.ai.goal.GeistSwoopAttackGoal;
import com.jamiedev.bygone.common.entity.ai.goal.SpectralWanderGoal;
import com.jamiedev.bygone.common.entity.ai.navigation.GeistPathNavigation;
import com.jamiedev.bygone.core.init.JamiesModTag;
import com.jamiedev.bygone.core.registry.BGBlocks;
import com.jamiedev.bygone.core.registry.BGDamageTypes;
import com.jamiedev.bygone.core.registry.BGSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class GeistEntity extends Monster implements FlyingAnimal {

    public AnimationState floatAnimationState = new AnimationState();
    public AnimationState idleAnimationState = new AnimationState();
    public AnimationState meleeAnimationState = new AnimationState();

    public static final int DEFAULT_LIGHT_THRESHOLD = 1;
    public static final double AGGRO_SPEED_MULTIPLIER = 1.0;
    private static final ResourceLocation AGGRO_SPEED_MODIFIER_ID = Bygone.id("geist_aggro_speed");

    public static final EntityDataAccessor<Integer> LIGHT_THRESHOLD = SynchedEntityData.defineId(GeistEntity.class, EntityDataSerializers.INT);

    public GeistEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 10;
        this.moveControl = new FlyingMoveControl(this, 35, false);
        this.setNoGravity(true);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
            .add(Attributes.MOVEMENT_SPEED, 0.2F)
            .add(Attributes.FLYING_SPEED, 0.4F)
            .add(Attributes.FOLLOW_RANGE, 18)
            .add(Attributes.ATTACK_DAMAGE, 4)
            .add(Attributes.KNOCKBACK_RESISTANCE, 1)
            .add(Attributes.MAX_HEALTH, 16);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, HauntEntity.class, 16, 1, 1.5F));
        this.goalSelector.addGoal(2, new GeistGotoLightGoal(this, 2, 8));

        this.goalSelector.addGoal(3, new AvoidBlockGoal(this, 16, 1.4, 1.6, (pos) -> {
            BlockState state = this.level().getBlockState(pos);
            return state.is(JamiesModTag.HURT_SPECTRAL_BLOCKS);
        }));
        this.goalSelector.addGoal(3, new AvoidBlockGoal(this, 16, 1.4, 1.6, (pos) -> {
            BlockState state = this.level().getBlockState(pos);
            return state.is(BGBlocks.LITHOPLASMIC_POWDER.get());
        }));

        this.goalSelector.addGoal(4, new GeistSwoopAttackGoal(this, 1.75F, true));
        this.goalSelector.addGoal(8, new SpectralWanderGoal(this, 0.6F));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3, 1));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, WraithEntity.class).setAlertOthers());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true, this::targetTooClose));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Mob.class, 10, true, false,
                (mob) -> this.targetTooClose(mob) && !mob.getType().is(JamiesModTag.SPECTRAL)));
    }

    @Override
    public void setTarget(@Nullable LivingEntity target) {
        if (target != null && this.getTarget() == null && !this.level().isClientSide()) {
            this.playSound(BGSoundEvents.GEIST_AMBIENT_ANGRY_EVENT, 2.0F, 1.2F);
        }
        super.setTarget(target);
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        boolean aggro = this.getTarget() != null;
        this.setAggressive(aggro);
        this.updateAggroSpeed(this.getAttribute(Attributes.FLYING_SPEED), aggro);
        this.updateAggroSpeed(this.getAttribute(Attributes.MOVEMENT_SPEED), aggro);
    }

    private void updateAggroSpeed(@Nullable AttributeInstance attribute, boolean aggro) {
        if (attribute == null) {
            return;
        }
        if (aggro) {
            if (!attribute.hasModifier(AGGRO_SPEED_MODIFIER_ID)) {
                attribute.addTransientModifier(new AttributeModifier(
                        AGGRO_SPEED_MODIFIER_ID,
                        AGGRO_SPEED_MULTIPLIER,
                        AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                ));
            }
        } else {
            attribute.removeModifier(AGGRO_SPEED_MODIFIER_ID);
        }
    }


    public boolean targetTooClose(LivingEntity entity) {
        return this.distanceTo(entity) <= 3;
    }

    @Override
    public boolean isWithinMeleeAttackRange(LivingEntity entity) {
        return super.isWithinMeleeAttackRange(entity);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(LIGHT_THRESHOLD, DEFAULT_LIGHT_THRESHOLD);
        builder.define(DATA_REPEL_RUN, false);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putInt("LightThreshold", this.getLightThreshold());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.setLightThreshold(compoundTag.getInt("LightThreshold"));
    }

    public int getLightThreshold() {
        return this.entityData.get(LIGHT_THRESHOLD);
    }

    public void setLightThreshold(int threshold) {
        this.entityData.set(LIGHT_THRESHOLD, threshold);
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return this.getTarget() == null ? BGSoundEvents.GEIST_AMBIENT_EVENT : BGSoundEvents.GEIST_AMBIENT_ANGRY_EVENT;
    }

    @Override
    public int getAmbientSoundInterval() {
        int multiplier = this.getTarget() == null ? 1 : 2;
        return super.getAmbientSoundInterval() * multiplier;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return BGSoundEvents.GEIST_HURT_EVENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return BGSoundEvents.GEIST_DEATH_EVENT;
    }

    @Override
    public void playAttackSound() {
        this.playSound(BGSoundEvents.GEIST_ATTACK_EVENT, 1, 1);
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {}

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {}

    @Override
    public boolean isFlapping() {
        return !this.onGround();
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level level) {
		return new GeistPathNavigation(this, level);
    }

    private boolean collidingHurtSpectralBlocks() {
        AABB aabb = this.getBoundingBox().inflate(1.0F, 1.0F, 1.0F);
        return BlockPos.betweenClosedStream(aabb).anyMatch((collisionShape) -> {
            BlockState blockstate = this.level().getBlockState(collisionShape);
            return blockstate.is(JamiesModTag.HURT_SPECTRAL_BLOCKS);
        });
    }

    private boolean collidingSpectralBlocks() {
        AABB aabb = this.getBoundingBox().inflate(1.0F, 1.0F, 1.0F);
        return BlockPos.betweenClosedStream(aabb).anyMatch((collisionShape) -> {
            BlockState blockstate = this.level().getBlockState(collisionShape);
            return blockstate.is(JamiesModTag.SPECTRAL_BLOCKS);
        });
    }

    private void setupAnimationStates() {
        this.idleAnimationState.startIfStopped(this.tickCount);
        if (this.getDeltaMovement().horizontalDistanceSqr() > 2.5000003E-7F) {
            this.floatAnimationState.startIfStopped(this.tickCount);
        } else {
            this.floatAnimationState.stop();
        }

        this.meleeAnimationState.animateWhen(this.attackAnim > 0, this.tickCount);
    }

    private static final EntityDataAccessor<Boolean> DATA_REPEL_RUN;
    private int checkRepelTicks = 0;

    static {
        DATA_REPEL_RUN = SynchedEntityData.defineId(GeistEntity.class, EntityDataSerializers.BOOLEAN);
    }

    public void tick() {
        this.setNoGravity(true);
        super.tick();

        Level level = this.level();

        if (!level.isClientSide()) {
            if (this.checkRepelTicks <= 0) {
                Optional<BlockPos> repelPos = BlockPos.findClosestMatch(this.blockPosition(), 8, 4, pos -> level.getBlockState(pos).is(JamiesModTag.HURT_SPECTRAL_BLOCKS));
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

        if (this.level().isClientSide()) {
            this.setupAnimationStates();
        }

        if  (collidingHurtSpectralBlocks())
        {
            this.hurt(BGDamageTypes.source(this.level(), BGDamageTypes.HAUNTED, this, this.getLastAttacker()), 1);
 
        }

        //noPhysics = !collidingSpectralBlocks();

    }


    @Override
    public void aiStep() {
//        BlockPos blockPos = this.blockPosition();
//        RandomSource randomSource = this.getRandom();
//        BlockPos pos = blockPos.offset(randomSource.nextInt(20) - 10, randomSource.nextInt(6) - 3, randomSource.nextInt(20) - 10);

//        if (this.level().getBrightness(LightLayer.BLOCK, this.blockPosition()) > this.getLightThreshold() || this.isOnFire()) {
//            this.gotoPosition = Vec3.atBottomCenterOf(pos);
//        }

        super.aiStep();
    }

    @Override
    public boolean isFlying() {
        return true;
    }

    public static boolean canSpawn(EntityType<? extends Mob> type, LevelAccessor level, MobSpawnType reason, BlockPos blockPos, RandomSource random) {
        return level.getBlockState(blockPos.below()).is(JamiesModTag.WRAITH_SPAWNABLE_ON);
    }

}
