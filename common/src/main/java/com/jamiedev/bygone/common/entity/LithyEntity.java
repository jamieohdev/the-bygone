package com.jamiedev.bygone.common.entity;

import com.jamiedev.bygone.common.entity.ai.FollowPlayerGoal;
import com.jamiedev.bygone.core.init.JamiesModLootTables;
import com.jamiedev.bygone.core.registry.BGBlocks;
import com.jamiedev.bygone.core.registry.BGSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TripWireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class LithyEntity extends Animal
{
    Wolf test;
    IronGolem ref;

    protected static final EntityDataAccessor<Byte> DATA_FLAGS_ID;
    protected static final EntityDataAccessor<Boolean> DATA_TRIPPED;
    protected static final EntityDataAccessor<Boolean> DATA_JUMP_UP;
    protected static final EntityDataAccessor<Integer> DATA_TRIPPED_TICK;
    protected static final EntityDataAccessor<Integer> DATA_TRIP_COOLDOWN;
    protected static final EntityDataAccessor<Integer> DATA_TRIPWIRE_TRIP_COOLDOWN;
    public boolean tripwireTrip = false;
    
    public AnimationState walkAnimationState = new AnimationState();
    public AnimationState tripBeginAnimationState = new AnimationState();
    public AnimationState tripAnimationState = new AnimationState();
    public AnimationState tripEndAnimationState = new AnimationState();

    public LithyEntity(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_FLAGS_ID, (byte)0);
        builder.define(DATA_TRIPPED, false);
        builder.define(DATA_JUMP_UP, false);
        builder.define(DATA_TRIPPED_TICK, 0);
        builder.define(DATA_TRIP_COOLDOWN, 1200 + this.random.nextInt(0, 200));
        builder.define(DATA_TRIPWIRE_TRIP_COOLDOWN, 0);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        this.entityData.set(DATA_TRIPPED, compound.getBoolean("DataTripped"));
        this.entityData.set(DATA_JUMP_UP, compound.getBoolean("DataJumpUp"));
        this.entityData.set(DATA_TRIPPED_TICK, compound.getInt("DataTrippedTick"));
        this.entityData.set(DATA_TRIP_COOLDOWN, compound.getInt("DataTripCooldown"));
        this.entityData.set(DATA_TRIPWIRE_TRIP_COOLDOWN, compound.getInt("DataTripwireTripCooldown"));

        super.readAdditionalSaveData(compound);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        compound.putBoolean("DataTripped", this.entityData.get(DATA_TRIPPED));
        compound.putBoolean("DataJumpUp", this.entityData.get(DATA_JUMP_UP));
        compound.putInt("DataTrippedTick", this.entityData.get(DATA_TRIPPED_TICK));
        compound.putInt("DataTripCooldown", this.entityData.get(DATA_TRIP_COOLDOWN));
        compound.putInt("DataTripwireTripCooldown", this.entityData.get(DATA_TRIPWIRE_TRIP_COOLDOWN));

        super.addAdditionalSaveData(compound);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, (double)5.0F)
                .add(Attributes.MOVEMENT_SPEED, (double)0.25F)
                .add(Attributes.KNOCKBACK_RESISTANCE, (double)1.0F)
                .add(Attributes.ATTACK_DAMAGE, (double)15.0F)
                .add(Attributes.STEP_HEIGHT, (double)1.0F);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(4, new LeapAtTargetGoal(this, 0.4F));
        this.goalSelector.addGoal(5, new LithyEntity.LithyMeleeAttackGoal(this, (double)1.0F, true));
        this.goalSelector.addGoal(6, new LithyEntity.LithyFollowMobGoal(this, (double)1.4F, 3.0F, 10.0F));
        this.goalSelector.addGoal(6, new LithyEntity.LithyFollowPlayerGoal(this, (double)1.4F, 3.0F, 10.0F));

        this.goalSelector.addGoal(8, new WaterAvoidingRandomStrollGoal(this, (double)1.0F));

        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(10, new RandomLookAroundGoal(this));
    }

    private void setupAnimationStates() {
        if (this.getDeltaMovement().horizontalDistanceSqr() > 2.5000003E-7F &&
                !this.getTripped() &&
                !this.entityData.get(DATA_JUMP_UP)) {
            this.walkAnimationState.startIfStopped(this.tickCount);
        } else {
            this.walkAnimationState.stop();
        }
        
        if (this.getTripped()) {
            if (this.getTrippedTick() == 0) {
                this.tripBeginAnimationState.start(this.tickCount);
                this.tripAnimationState.stop();
                this.tripEndAnimationState.stop();
            } else if (this.getTrippedTick() >= 125 && this.getTrippedTick() < 250) {
                this.tripBeginAnimationState.stop();
                this.tripAnimationState.startIfStopped(this.tickCount);
                this.tripEndAnimationState.stop();
            }
        } else {
            if (this.entityData.get(DATA_JUMP_UP)) {
                this.tripBeginAnimationState.stop();
                this.tripAnimationState.stop();
                this.tripEndAnimationState.start(40);
            }
        }
    }

    @Override
    public void tick() {
        if (this.level().isClientSide()) {
            this.setupAnimationStates();
        }

        if (!this.level().isClientSide()) {
            int trippedCooldown = this.entityData.get(DATA_TRIP_COOLDOWN);
            int tripTick = this.entityData.get(DATA_TRIPPED_TICK);

            if (this.entityData.get(DATA_TRIPPED) && tripTick < 5) {
                Vec3 startPos = this.position();
                Vec3 lookAng = this.getLookAngle().normalize();
                Vec3 endPos = startPos.add(lookAng.scale(2.0));

                AABB rayBox = new AABB(startPos, endPos).inflate(1.0); // Inflating to 1 block radius

                List<LivingEntity> livingEntities = this.level().getEntitiesOfClass(LivingEntity.class, rayBox, e ->
                        e != this && e.isAlive() && e.getBoundingBox().intersects(rayBox) && !(e instanceof LithyEntity));

                for (LivingEntity living : livingEntities) {
                    living.hurt(this.damageSources().cramming(), 30);
                }

            }

            if (this.entityData.get(DATA_JUMP_UP)) {
                this.entityData.set(DATA_JUMP_UP, false);
                this.push(new Vec3(0.0, 0.4, 0.0));
            }
            if (!this.entityData.get(DATA_TRIPPED)) {
                this.entityData.set(DATA_TRIPWIRE_TRIP_COOLDOWN, this.entityData.get(DATA_TRIPWIRE_TRIP_COOLDOWN) - 1);
                if (this.getDeltaMovement().horizontalDistanceSqr() > 2.5000003E-7F) {
                    this.entityData.set(DATA_TRIP_COOLDOWN, trippedCooldown - 1);

                    BlockPos onPos = this.getOnPos().above();
                    BlockState state = this.level().getBlockState(onPos);

                    if (state.is(Blocks.TRIPWIRE) && state.getValue(TripWireBlock.ATTACHED) && this.entityData.get(DATA_TRIPWIRE_TRIP_COOLDOWN) <= 0) {
                        this.push(this.getDeltaMovement().add(0.0, 0.2, 0.0));
                        this.entityData.set(DATA_TRIPPED, true);
                        this.entityData.set(DATA_TRIP_COOLDOWN, 1200 + this.random.nextInt(0, 200));
                        this.tripwireTrip = true;
                        this.playTripEffects();
                    }

                    else if (this.entityData.get(DATA_TRIP_COOLDOWN) <= 0 && this.random.nextFloat() < 0.1) {
                        this.push(this.getDeltaMovement().add(0.0, 0.2, 0.0));
                        this.entityData.set(DATA_TRIPPED, true);
                        this.entityData.set(DATA_TRIP_COOLDOWN, 1200 + this.random.nextInt(0, 200));
                        this.playTripEffects();
                        if (this.level().getServer() != null) {
                            LootTable loottable = this.level().getServer().reloadableRegistries().getLootTable(JamiesModLootTables.LITHY_TRIP_LOOT_TABLE);
                            List<ItemStack> list = loottable.getRandomItems(
                                    new LootParams.Builder((ServerLevel)this.level())
                                            .create(LootContextParamSets.EMPTY)
                            );

                            for (ItemStack stack : list) {
                                this.spawnAtLocation(stack);
                            }
                        }
                    }
                }
            }
            else {
                this.entityData.set(DATA_TRIPPED_TICK, tripTick + 1);

                if (tripTick > 60 && this.random.nextFloat() < 0.01) {
                    this.entityData.set(DATA_TRIPPED, false);
                    this.entityData.set(DATA_TRIPPED_TICK, 0);
                    this.entityData.set(DATA_JUMP_UP, true);
                    if (this.tripwireTrip) {
                        this.entityData.set(DATA_TRIPWIRE_TRIP_COOLDOWN, 600);
                        this.tripwireTrip = false;
                    }
                }
            }
        }

        super.tick();
    }

    @Override
    protected float tickHeadTurn(float yRot, float animStep) {
        if (this.entityData.get(DATA_TRIPPED)) {
            return 0.0F;
        }

        return super.tickHeadTurn(yRot, animStep);
    }

    @Override
    public void aiStep() {
        Vec3 delta = this.getDeltaMovement();
        if (this.entityData.get(DATA_TRIPPED) && this.onGround()) {
            this.setDeltaMovement(0.0, delta.y - 0.1, 0.0);
        }
        else {
            super.aiStep();
        }
    }

    @Override
    public void travel(Vec3 travelVector) {
        Vec3 delta = this.getDeltaMovement();
        if (this.entityData.get(DATA_TRIPPED) && this.onGround()) {
            this.setDeltaMovement(0.0, delta.y - 0.1, 0.0);
        }
        else {
            super.travel(travelVector);
        }
    }

    public boolean getTripped() {
        return this.entityData.get(DATA_TRIPPED);
    }

    public int getTrippedTick() {
        return this.entityData.get(DATA_TRIPPED_TICK);
    }

    protected int decreaseAirSupply(int air) {
        return air;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return BGSoundEvents.LITHY_AMBIENT_ADDITIONS_EVENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return BGSoundEvents.LITHY_HURT_ADDITIONS_EVENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return BGSoundEvents.LITHY_DEATH_ADDITIONS_EVENT;
    }


    protected void doPush(Entity entity) {
        if (entity instanceof Enemy && !(entity instanceof Creeper) && this.getRandom().nextInt(20) == 0) {
            this.setTarget((LivingEntity)entity);
        }

        super.doPush(entity);
    }

    public boolean canSpawnSprintParticle() {
        return this.getDeltaMovement().horizontalDistanceSqr() > (double)2.5000003E-7F && this.random.nextInt(5) == 0;
    }

    public Crackiness.Level getCrackiness() {
        return Crackiness.GOLEM.byFraction(this.getHealth() / this.getMaxHealth());
    }

    private void playTripEffects() {
        this.playSound(BGSoundEvents.LITHY_TRIP_ADDITIONS_EVENT, 0.7F, 1.2F + this.random.nextFloat() * 0.2F);

        if (this.level().isClientSide()) {
            double x = this.getX();
            double y = this.getY();
            double z = this.getZ();

            for (int i = 0; i < 15; i++) {
                double offsetX = (this.random.nextDouble() - 0.5) * 0.8;
                double offsetY = this.random.nextDouble() * 0.3;
                double offsetZ = (this.random.nextDouble() - 0.5) * 0.8;
                
                double velocityX = (this.random.nextDouble() - 0.5) * 0.2;
                double velocityY = this.random.nextDouble() * 0.15 + 0.05;
                double velocityZ = (this.random.nextDouble() - 0.5) * 0.2;
                
                this.level().addParticle(ParticleTypes.POOF, 
                    x + offsetX, y + offsetY, z + offsetZ,
                    velocityX, velocityY, velocityZ);
            }

            BlockState groundState = this.level().getBlockState(this.getOnPos());
            if (!groundState.isAir()) {
                for (int i = 0; i < 10; i++) {
                    double offsetX = (this.random.nextDouble() - 0.5) * 0.6;
                    double offsetZ = (this.random.nextDouble() - 0.5) * 0.6;
                    
                    this.level().addParticle(ParticleTypes.SMOKE,
                        x + offsetX, y + 0.1, z + offsetZ,
                        0.0, 0.05, 0.0);
                }
            }
        }
    }

    public boolean hurt(DamageSource source, float amount) {
        Crackiness.Level crackiness$level = this.getCrackiness();
        boolean flag = super.hurt(source, amount);
        if (flag && this.getCrackiness() != crackiness$level) {
            this.playSound(SoundEvents.DEEPSLATE_BRICKS_BREAK, 1.0F, 1.0F);
        }

        return flag;
    }

    public static boolean canSpawn(
            EntityType<? extends Mob> moobloomEntityType,
            LevelAccessor serverWorldAccess,
            MobSpawnType spawnReason,
            BlockPos blockPos,
            RandomSource random
    ) {
        return serverWorldAccess.getBlockState(blockPos).is(BGBlocks.MEGALITH_LANTERN.get())
                || serverWorldAccess.getBlockState(blockPos).is(BGBlocks.BYSLATE.get())
                || serverWorldAccess.getBlockState(blockPos.below()).is(BGBlocks.ALPHA_MOSS_BLOCK.get());
    }

    @Override
    public boolean isFood(ItemStack itemStack) {
        return false;
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return null;
    }

    static {
        DATA_FLAGS_ID = SynchedEntityData.defineId(LithyEntity.class, EntityDataSerializers.BYTE);
        DATA_TRIPPED = SynchedEntityData.defineId(LithyEntity.class, EntityDataSerializers.BOOLEAN);
        DATA_JUMP_UP = SynchedEntityData.defineId(LithyEntity.class, EntityDataSerializers.BOOLEAN);
        DATA_TRIPPED_TICK = SynchedEntityData.defineId(LithyEntity.class, EntityDataSerializers.INT);
        DATA_TRIP_COOLDOWN = SynchedEntityData.defineId(LithyEntity.class, EntityDataSerializers.INT);
        DATA_TRIPWIRE_TRIP_COOLDOWN = SynchedEntityData.defineId(LithyEntity.class, EntityDataSerializers.INT);
    }

    static class LithyMeleeAttackGoal extends Goal {
        protected final LithyEntity lithy;
        private final double speedModifier;
        private final boolean followingTargetEvenIfNotSeen;
        private Path path;
        private double pathedTargetX;
        private double pathedTargetY;
        private double pathedTargetZ;
        private int ticksUntilNextPathRecalculation;
        private int ticksUntilNextAttack;
        private final int attackInterval = 20;
        private long lastCanUseCheck;
        private static final long COOLDOWN_BETWEEN_CAN_USE_CHECKS = 20L;

        public LithyMeleeAttackGoal(LithyEntity lithy, double speedModifier, boolean followingTargetEvenIfNotSeen) {
            this.lithy = lithy;
            this.speedModifier = speedModifier;
            this.followingTargetEvenIfNotSeen = followingTargetEvenIfNotSeen;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            if (!this.lithy.entityData.get(DATA_TRIPPED)) {
                long i = this.lithy.level().getGameTime();
                if (i - this.lastCanUseCheck < 20L) {
                    return false;
                } else {
                    this.lastCanUseCheck = i;
                    LivingEntity livingentity = this.lithy.getTarget();
                    if (livingentity == null) {
                        return false;
                    } else if (!livingentity.isAlive()) {
                        return false;
                    } else {
                        this.path = this.lithy.getNavigation().createPath(livingentity, 0);
                        return this.path != null ? true : this.lithy.isWithinMeleeAttackRange(livingentity);
                    }
                }
            }
            return false;
        }

        @Override
        public boolean canContinueToUse() {
            if (!this.lithy.entityData.get(DATA_TRIPPED)) {
                LivingEntity livingentity = this.lithy.getTarget();
                if (livingentity == null) {
                    return false;
                } else if (!livingentity.isAlive()) {
                    return false;
                } else if (!this.followingTargetEvenIfNotSeen) {
                    return !this.lithy.getNavigation().isDone();
                } else {
                    return !this.lithy.isWithinRestriction(livingentity.blockPosition())
                            ? false
                            : !(livingentity instanceof Player) || !livingentity.isSpectator() && !((Player)livingentity).isCreative();
                }
            }
            return false;
        }

        @Override
        public void start() {
            this.lithy.getNavigation().moveTo(this.path, this.speedModifier);
            this.lithy.setAggressive(true);
            this.ticksUntilNextPathRecalculation = 0;
            this.ticksUntilNextAttack = 0;
        }

        @Override
        public void stop() {
            LivingEntity livingentity = this.lithy.getTarget();
            if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(livingentity)) {
                this.lithy.setTarget(null);
            }

            this.lithy.setAggressive(false);
            this.lithy.getNavigation().stop();
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            LivingEntity livingentity = this.lithy.getTarget();
            if (livingentity != null) {
                this.lithy.getLookControl().setLookAt(livingentity, 30.0F, 30.0F);
                this.ticksUntilNextPathRecalculation = Math.max(this.ticksUntilNextPathRecalculation - 1, 0);
                if ((this.followingTargetEvenIfNotSeen || this.lithy.getSensing().hasLineOfSight(livingentity))
                        && this.ticksUntilNextPathRecalculation <= 0
                        && (
                        this.pathedTargetX == 0.0 && this.pathedTargetY == 0.0 && this.pathedTargetZ == 0.0
                                || livingentity.distanceToSqr(this.pathedTargetX, this.pathedTargetY, this.pathedTargetZ) >= 1.0
                                || this.lithy.getRandom().nextFloat() < 0.05F
                )) {
                    this.pathedTargetX = livingentity.getX();
                    this.pathedTargetY = livingentity.getY();
                    this.pathedTargetZ = livingentity.getZ();
                    this.ticksUntilNextPathRecalculation = 4 + this.lithy.getRandom().nextInt(7);
                    double d0 = this.lithy.distanceToSqr(livingentity);
                    if (d0 > 1024.0) {
                        this.ticksUntilNextPathRecalculation += 10;
                    } else if (d0 > 256.0) {
                        this.ticksUntilNextPathRecalculation += 5;
                    }

                    if (!this.lithy.getNavigation().moveTo(livingentity, this.speedModifier)) {
                        this.ticksUntilNextPathRecalculation += 15;
                    }

                    this.ticksUntilNextPathRecalculation = this.adjustedTickDelay(this.ticksUntilNextPathRecalculation);
                }

                this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
                this.checkAndPerformAttack(livingentity);
            }
        }

        protected void checkAndPerformAttack(LivingEntity target) {
            if (this.canPerformAttack(target)) {
                this.resetAttackCooldown();
                this.lithy.swing(InteractionHand.MAIN_HAND);
                this.lithy.doHurtTarget(target);
            }
        }

        protected void resetAttackCooldown() {
            this.ticksUntilNextAttack = this.adjustedTickDelay(20);
        }

        protected boolean isTimeToAttack() {
            return this.ticksUntilNextAttack <= 0;
        }

        protected boolean canPerformAttack(LivingEntity entity) {
            return this.isTimeToAttack() && this.lithy.isWithinMeleeAttackRange(entity) && this.lithy.getSensing().hasLineOfSight(entity);
        }

        protected int getTicksUntilNextAttack() {
            return this.ticksUntilNextAttack;
        }

        protected int getAttackInterval() {
            return this.adjustedTickDelay(20);
        }
    }

    static class LithyFollowMobGoal extends Goal {
        private final LithyEntity lithy;
        private final Predicate<Mob> followPredicate;
        @javax.annotation.Nullable
        private Mob followingMob;
        private final double speedModifier;
        private final PathNavigation navigation;
        private int timeToRecalcPath;
        private final float stopDistance;
        private float oldWaterCost;
        private final float areaSize;

        /**
         * Constructs a goal allowing a mob to follow others. The mob must have Ground or Flying navigation.
         */
        public LithyFollowMobGoal(LithyEntity mob, double speedModifier, float stopDistance, float areaSize) {
            this.lithy = mob;
            this.followPredicate = p_25278_ -> p_25278_ != null && mob.getClass() != p_25278_.getClass();
            this.speedModifier = speedModifier;
            this.navigation = mob.getNavigation();
            this.stopDistance = stopDistance;
            this.areaSize = areaSize;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
            if (!(mob.getNavigation() instanceof GroundPathNavigation) && !(mob.getNavigation() instanceof FlyingPathNavigation)) {
                throw new IllegalArgumentException("Unsupported mob type for FollowMobGoal");
            }
        }

        @Override
        public boolean canUse() {
            if (!this.lithy.entityData.get(DATA_TRIPPED)) {
                List<Mob> list = this.lithy.level().getEntitiesOfClass(Mob.class, this.lithy.getBoundingBox().inflate((double)this.areaSize), this.followPredicate);
                if (!list.isEmpty()) {
                    for (Mob mob : list) {
                        if (!mob.isInvisible()) {
                            this.followingMob = mob;
                            return true;
                        }
                    }
                }
            }

            return false;
        }

        @Override
        public boolean canContinueToUse() {
            return this.followingMob != null
                    && !this.lithy.entityData.get(DATA_TRIPPED)
                    && !this.navigation.isDone()
                    && this.lithy.distanceToSqr(this.followingMob) > (double)(this.stopDistance * this.stopDistance);
        }

        @Override
        public void start() {
            this.timeToRecalcPath = 0;
            this.oldWaterCost = this.lithy.getPathfindingMalus(PathType.WATER);
            this.lithy.setPathfindingMalus(PathType.WATER, 0.0F);
        }

        @Override
        public void stop() {
            this.followingMob = null;
            this.navigation.stop();
            this.lithy.setPathfindingMalus(PathType.WATER, this.oldWaterCost);
        }

        @Override
        public void tick() {
            if (this.followingMob != null && !this.lithy.isLeashed()) {
                this.lithy.getLookControl().setLookAt(this.followingMob, 10.0F, (float)this.lithy.getMaxHeadXRot());
                if (--this.timeToRecalcPath <= 0) {
                    this.timeToRecalcPath = this.adjustedTickDelay(10);
                    double d0 = this.lithy.getX() - this.followingMob.getX();
                    double d1 = this.lithy.getY() - this.followingMob.getY();
                    double d2 = this.lithy.getZ() - this.followingMob.getZ();
                    double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                    if (!(d3 <= (double)(this.stopDistance * this.stopDistance))) {
                        this.navigation.moveTo(this.followingMob, this.speedModifier);
                    } else {
                        this.navigation.stop();
                        LookControl lookcontrol = this.followingMob.getLookControl();
                        if (d3 <= (double)this.stopDistance
                                || lookcontrol.getWantedX() == this.lithy.getX()
                                && lookcontrol.getWantedY() == this.lithy.getY()
                                && lookcontrol.getWantedZ() == this.lithy.getZ()) {
                            double d4 = this.followingMob.getX() - this.lithy.getX();
                            double d5 = this.followingMob.getZ() - this.lithy.getZ();
                            this.navigation.moveTo(this.lithy.getX() - d4, this.lithy.getY(), this.lithy.getZ() - d5, this.speedModifier);
                        }
                    }
                }
            }
        }
    }

    static class LithyFollowPlayerGoal extends Goal {
        private final LithyEntity mob;
        private final Predicate<Player> followPredicate;
        @javax.annotation.Nullable
        private Player followingMob;
        private final double speedModifier;
        private final PathNavigation navigation;
        private int timeToRecalcPath;
        private final float stopDistance;
        private float oldWaterCost;
        private final float areaSize;

        public LithyFollowPlayerGoal(LithyEntity mob, double speedModifier, float stopDistance, float areaSize) {
            this.mob = mob;
            this.followPredicate = Objects::nonNull;//&& mob.getClass() != p_25278_.getClass();
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
            if (!this.mob.entityData.get(DATA_TRIPPED)) {
                List<Player> list = this.mob.level().getEntitiesOfClass(Player.class, this.mob.getBoundingBox().inflate((double)this.areaSize), this.followPredicate);
                if (!list.isEmpty()) {
                    for(Player mob : list) {
                        if (!mob.isInvisible()) {
                            this.followingMob = mob;
                            return true;
                        }
                    }
                }
            }

            return false;
        }

        public boolean canContinueToUse() {
            return this.followingMob != null && !this.mob.entityData.get(DATA_TRIPPED) && !this.navigation.isDone() && this.mob.distanceToSqr(this.followingMob) > (double)(this.stopDistance * this.stopDistance);
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
                this.mob.getLookControl().setLookAt(this.followingMob, 10.0F, (float)this.mob.getMaxHeadXRot());
                if (--this.timeToRecalcPath <= 0) {
                    this.timeToRecalcPath = this.adjustedTickDelay(10);
                    double d0 = this.mob.getX() - this.followingMob.getX();
                    double d1 = this.mob.getY() - this.followingMob.getY();
                    double d2 = this.mob.getZ() - this.followingMob.getZ();
                    double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                    if (!(d3 <= (double)(this.stopDistance * this.stopDistance))) {
                        this.navigation.moveTo(this.followingMob, this.speedModifier);
                    } else {
                        this.navigation.stop();

                    }
                }
            }

        }
    }

}
