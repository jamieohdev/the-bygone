package com.jamiedev.bygone.common.entity;

import com.jamiedev.bygone.core.init.JamiesModTag;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.animal.horse.Markings;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class MurklingEntity extends Monster implements RangedAttackMob
{
    public AnimationState swimmingAnimationState = new AnimationState();
    public AnimationState idleAnimationState = new AnimationState();
    public AnimationState attackAnimationState = new AnimationState();

    private static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT;
    protected final WaterBoundPathNavigation waterNavigation;
    
    public MurklingEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.setPathfindingMalus(PathType.WATER, 0.0F);
        this.moveControl = new MurklingEntityMoveControl(this);
        this.lookControl = new SmoothSwimmingLookControl(this, 10);
        this.waterNavigation = new WaterBoundPathNavigation(this, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 30.0)
                .add(Attributes.FOLLOW_RANGE, 20.0)
                .add(Attributes.MOVEMENT_SPEED, 0.10)
                .add(Attributes.WATER_MOVEMENT_EFFICIENCY, 0.33)
                .add(Attributes.ATTACK_DAMAGE, 6.0)
                .add(Attributes.STEP_HEIGHT, 2.0);
    }


    protected void registerGoals()  {
        this.goalSelector.addGoal(0, new TryFindWaterGoal(this));
        this.goalSelector.addGoal(1, new MurklingEntity.MurklingEntityAttackGoal(this, 4.55, false));
        this.goalSelector.addGoal(2, new RandomSwimmingGoal(this, 1.0, 10));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, Player.class).setAlertOthers(MurklingEntity.class));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, PrimordialFishEntity.class).setAlertOthers(MurklingEntity.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, this::okTarget));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PrimordialFishEntity.class, 10, true, false, this::okTarget));
    }

    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_ID_TYPE_VARIANT, 0);
    }

    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Variant", this.getTypeVariant());
    }

    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setTypeVariant(compound.getInt("Variant"));
    }

    protected void handleAirSupply(int airSupply) {
        if (this.isAlive() && !this.isInWaterOrBubble()) {
            this.setAirSupply(airSupply - 1);
            if (this.getAirSupply() == -20) {
                this.setAirSupply(0);
                this.hurt(this.damageSources().drown(), 2.0F);
            }
        } else {
            this.setAirSupply(300);
        }

    }

    public void tick()
    {
        super.tick();

        if (this.level().isClientSide()) {
            this.setupAnimationStates();
        }
    }

    public void baseTick() {
        int i = this.getAirSupply();
        super.baseTick();
        this.handleAirSupply(i);
    }

    @Override
    public void performRangedAttack(LivingEntity livingEntity, float v) {
        
    }

    public boolean okTarget(@Nullable LivingEntity target) {
        return target != null && (!this.level().isDay() || target.isInWater());
    }

    @Override
    public int getMaxHeadXRot() {
        return 1;
    }

    @Override
    public int getMaxHeadYRot() {
        return 1;
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new WaterBoundPathNavigation(this, level);
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    boolean wantsToSwim() {
        LivingEntity livingentity = this.getTarget();
        return livingentity != null && livingentity.isInWater();
    }

    protected SoundEvent getFlopSound() {
        return SoundEvents.GUARDIAN_FLOP;
    }

    @Override
    public void aiStep() {
        if (!this.isInWater() && this.onGround() && this.verticalCollision) {
            this.setDeltaMovement(
                    this.getDeltaMovement()
                            .add((double)((this.random.nextFloat() * 2.0F - 1.0F) * 0.05F), 0.4F, (double)((this.random.nextFloat() * 2.0F - 1.0F) * 0.05F))
            );
            this.setOnGround(false);
            this.hasImpulse = true;
            this.makeSound(this.getFlopSound());
        }

        super.aiStep();
    }

    @Override
    public void travel(@NotNull Vec3 travelVector) {
        if (this.isEffectiveAi() && this.isInWater()) {
            this.moveRelative(this.getSpeed(), travelVector);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9));
            if (this.getTarget() == null) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.005, 0.0));
            }   } else {
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

    private static MurklingVariants getRandomVariant(LevelAccessor level, BlockPos pos) {
        Holder<Biome> holder = level.getBiome(pos);
        int i = level.getRandom().nextInt(100);
        return i < 10 ? MurklingVariants.FUSCHIA : i < 15 ? MurklingVariants.TEAL : MurklingVariants.OLIVE;
    }
    
        
    private int getTypeVariant() {
        return this.entityData.get(DATA_ID_TYPE_VARIANT);
    }

    private void setTypeVariant(int typeVariant) {
        this.entityData.set(DATA_ID_TYPE_VARIANT, typeVariant);
    }

    private void setVariantAndMarkings(MurklingVariants variant, Markings marking) {
        this.setTypeVariant(variant.getId() & 255 | marking.getId() << 8 & '\uff00');
    }

    public MurklingVariants getVariant() {
        return MurklingVariants.byId(this.getTypeVariant() & 255);
    }

    public void setVariant(MurklingVariants variant) {
        this.setTypeVariant(variant.getId() & 255 | this.getTypeVariant() & -256);
    }

    public Markings getMarkings() {
        return Markings.byId((this.getTypeVariant() & '\uff00') >> 8);
    }

    private void setupAnimationStates() {
        this.idleAnimationState.startIfStopped(this.tickCount);
        if (this.getDeltaMovement().horizontalDistanceSqr() > 2.5000003E-7F) {
            this.swimmingAnimationState.startIfStopped(this.tickCount);
        } else {
            this.swimmingAnimationState.stop();
        }

        if (this.attackAnim > 0) {
            this.attackAnimationState.start(this.tickCount);
        } else if (this.attackAnim == 0) {
            this.attackAnimationState.stop();
        }
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
        private final int maxTurnX;
        private final int maxTurnY;
        private final float inWaterSpeedModifier;
        private final float outsideWaterSpeedModifier;
        private final boolean applyGravity;
        private final MurklingEntity murkling;

        public MurklingEntityMoveControl(MurklingEntity murkling) {
            super(murkling);
            this.murkling = murkling;
            this.maxTurnX = 85;
            this.maxTurnY = 10;
            this.inWaterSpeedModifier = 0.02F;
            this.outsideWaterSpeedModifier = 0.1F;
            this.applyGravity = true;
        }

        @Override
        public void tick() {

            LivingEntity livingentity = this.murkling.getTarget();
            if (this.murkling.wantsToSwim() && this.murkling.isInWater()) {


                if (this.operation != MoveControl.Operation.MOVE_TO || this.murkling.getNavigation().isDone()) {
                    this.murkling.setSpeed(0.0F);
                    this.murkling.setXxa(0.0F);
                    this.murkling.setYya(0.0F);
                    this.murkling.setZza(0.0F);
                    return;
                }

                double d0 = this.wantedX - this.murkling.getX();
                double d1 = this.wantedY - this.murkling.getY();
                double d2 = this.wantedZ - this.murkling.getZ();
                double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                d1 /= d3;

                float f = (float)(Mth.atan2(d2, d0) * (double)180.0F / (double)(float)Math.PI) - 90.0F;
                this.mob.setYRot(this.rotlerp(this.mob.getYRot(), f, (float)this.maxTurnY));
                this.mob.yBodyRot = this.mob.getYRot();
                this.mob.yHeadRot = this.mob.getYRot();
                float f1 = (float)(this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED));
                if (this.mob.isInWater()) {
                    this.mob.setSpeed(f1 * this.inWaterSpeedModifier);
                    double d4 = Math.sqrt(d0 * d0 + d2 * d2);
                    if (Math.abs(d1) > (double)1.0E-5F || Math.abs(d4) > (double)1.0E-5F) {
                        float f3 = -((float)(Mth.atan2(d1, d4) * (double)180.0F / (double)(float)Math.PI));
                        f3 = Mth.clamp(Mth.wrapDegrees(f3), (float)(-this.maxTurnX), (float)this.maxTurnX);
                        this.mob.setXRot(this.rotlerp(this.mob.getXRot(), f3, 5.0F));
                    }

                    float f6 = Mth.cos(this.mob.getXRot() * ((float)Math.PI / 180F));
                    float f4 = Mth.sin(this.mob.getXRot() * ((float)Math.PI / 180F));
                    this.mob.zza = f6 * f1;
                    this.mob.yya = -f4 * f1;
                } else {
                    float f5 = Math.abs(Mth.wrapDegrees(this.mob.getYRot() - f));
                    float f2 = getTurningSpeedFactor(f5);
                    this.mob.setSpeed(f1 * this.outsideWaterSpeedModifier * f2);
                }

            } else {
                if (!this.murkling.onGround()) {
                    this.murkling.setDeltaMovement(this.murkling.getDeltaMovement().add(0.0, -0.008, 0.0));
                }

                super.tick();
            }
        }

        private static float getTurningSpeedFactor(float p_249853_) {
            return 1.0F - Mth.clamp((p_249853_ - 10.0F) / 50.0F, 0.0F, 1.0F);
        }
    }

    public static class MurklingGroupData extends AgeableMob.AgeableMobGroupData {
        public final MurklingVariants variant;

        public MurklingGroupData(MurklingVariants variant) {
            super(true);
            this.variant = variant;
        }
    }

    public boolean checkSpawnObstruction(LevelReader level) {
        return level.isUnobstructed(this);
    }

    @javax.annotation.Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @javax.annotation.Nullable SpawnGroupData spawnGroupData) {
        RandomSource randomsource = level.getRandom();
        MurklingVariants murkling$variant = getRandomVariant(level, this.blockPosition());
        MurklingVariants variant;
        if (spawnGroupData instanceof MurklingEntity.MurklingGroupData) {
            murkling$variant = ((MurklingEntity.MurklingGroupData) spawnGroupData).variant;
        } else {
            murkling$variant = Util.getRandom(MurklingVariants.values(), randomsource);
            spawnGroupData = new MurklingEntity.MurklingGroupData(murkling$variant);
        }

        this.setVariantAndMarkings(murkling$variant, Util.getRandom(Markings.values(), randomsource));
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }
    
    static {
        DATA_ID_TYPE_VARIANT = SynchedEntityData.defineId(MurklingEntity.class, EntityDataSerializers.INT);
    }
}
