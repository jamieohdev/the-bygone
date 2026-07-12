package com.jamiedev.bygone.common.entity;

import com.jamiedev.bygone.common.entity.ai.AvoidBlockGoal;
import com.jamiedev.bygone.common.entity.ai.goal.SpectralWanderGoal;
import com.jamiedev.bygone.common.entity.projectile.ScareBoltEntity;
import com.jamiedev.bygone.core.init.JamiesModTag;
import com.jamiedev.bygone.core.registry.BGBlocks;
import com.jamiedev.bygone.core.registry.BGDamageTypes;
import com.jamiedev.bygone.core.registry.BGSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ScareEntity extends Monster implements RangedAttackMob, FlyingAnimal {

    public static final int VOLLEY_SIZE = 5;
    public static final float VOLLEY_SPREAD = 0.12F;
    public static final float BOLT_SPEED = 0.35F;
    public static final int ZIGZAG_INTERVAL = 10;
    public static final double ZIGZAG_STRENGTH = 0.45;

    public AnimationState idleAnimationState = new AnimationState();
    public AnimationState floatAnimationState = new AnimationState();

    private int zigzagDirection = 1;

    public ScareEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 10;
        this.moveControl = new FlyingMoveControl(this, 20, true);
        this.setNoGravity(true);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.MOVEMENT_SPEED, 0.2)
                .add(Attributes.FLYING_SPEED, 0.6)
                .add(Attributes.FOLLOW_RANGE, 24.0);
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
        this.goalSelector.addGoal(4, new RangedAttackGoal(this, 1.0, 60, 16.0F));
        this.goalSelector.addGoal(8, new SpectralWanderGoal(this, 0.6));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, ScareEntity.class).setAlertOthers());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true).setUnseenMemoryTicks(300));
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
    public void performRangedAttack(@NotNull LivingEntity target, float velocity) {
        this.playSound(BGSoundEvents.SCARE_SHOOT_EVENT, 1.5F, 1.0F);
        Vec3 origin = new Vec3(this.getX(), this.getEyeY() - 0.1, this.getZ());
        Vec3 aim = new Vec3(
                target.getX() - origin.x,
                target.getY(0.5) - origin.y,
                target.getZ() - origin.z
        ).normalize();

        for (int i = 0; i < VOLLEY_SIZE; i++) {
            Vec3 spread = aim.add(
                    this.random.triangle(0, VOLLEY_SPREAD),
                    this.random.triangle(0, VOLLEY_SPREAD),
                    this.random.triangle(0, VOLLEY_SPREAD)
            ).normalize().scale(BOLT_SPEED);
            ScareBoltEntity bolt = new ScareBoltEntity(this.level(), this);
            bolt.setPos(origin.x, origin.y, origin.z);
            bolt.setDeltaMovement(spread);
            this.level().addFreshEntity(bolt);
        }
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        LivingEntity target = this.getTarget();
        if (target != null && this.tickCount % ZIGZAG_INTERVAL == 0) {
            Vec3 toTarget = new Vec3(target.getX() - this.getX(), 0, target.getZ() - this.getZ());
            if (toTarget.lengthSqr() > 1.0E-4) {
                Vec3 perpendicular = new Vec3(-toTarget.z, 0, toTarget.x).normalize();
                this.zigzagDirection = -this.zigzagDirection;
                this.addDeltaMovement(perpendicular.scale(ZIGZAG_STRENGTH * this.zigzagDirection));
            }
        }
    }

    @Override
    public boolean canBeAffected(@NotNull MobEffectInstance potioneffect) {
        return !(potioneffect.is(MobEffects.POISON) || potioneffect.is(MobEffects.HARM) || potioneffect.is(MobEffects.WITHER)) && super.canBeAffected(potioneffect);
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return BGSoundEvents.SCARE_AMBIENT_EVENT;
    }

    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return BGSoundEvents.SCARE_HURT_EVENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return BGSoundEvents.SCARE_DEATH_EVENT;
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
}
