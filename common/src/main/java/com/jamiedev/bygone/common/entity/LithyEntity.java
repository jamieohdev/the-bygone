package com.jamiedev.bygone.common.entity;

import com.jamiedev.bygone.common.entity.ai.FollowPlayerGoal;
import com.jamiedev.bygone.core.init.JamiesModLootTables;
import com.jamiedev.bygone.core.registry.BGBlockEntities;
import com.jamiedev.bygone.core.registry.BGBlocks;
import com.jamiedev.bygone.core.registry.BGEntityTypes;
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
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
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
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LithyEntity extends Animal
{
    Wolf test;
    IronGolem ref;

    protected static final EntityDataAccessor<Byte> DATA_FLAGS_ID;
    protected static final EntityDataAccessor<Boolean> DATA_TRIPPED;
    protected static final EntityDataAccessor<Integer> DATA_TRIPPED_TICK;
    protected static final EntityDataAccessor<Integer> DATA_TRIP_COOLDOWN;
    protected static final EntityDataAccessor<Integer> DATA_TRIPWIRE_TRIP_COOLDOWN;
    public boolean jumpUp = false;
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
        builder.define(DATA_TRIPPED_TICK, 0);
        builder.define(DATA_TRIP_COOLDOWN, 1200 + this.random.nextInt(0, 200));
        builder.define(DATA_TRIPWIRE_TRIP_COOLDOWN, 0);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        this.entityData.set(DATA_TRIPPED, compound.getBoolean("DataTripped"));
        this.entityData.set(DATA_TRIPPED_TICK, compound.getInt("DataTrippedTick"));
        this.entityData.set(DATA_TRIP_COOLDOWN, compound.getInt("DataTripCooldown"));
        this.entityData.set(DATA_TRIPWIRE_TRIP_COOLDOWN, compound.getInt("DataTripwireTripCooldown"));

        super.readAdditionalSaveData(compound);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        compound.putBoolean("DataTripped", this.entityData.get(DATA_TRIPPED));
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
        this.goalSelector.addGoal(5, new MeleeAttackGoal(this, (double)1.0F, true));
        this.goalSelector.addGoal(6, new FollowMobGoal(this, (double)1.4F, 3.0F, 10.0F));
        this.goalSelector.addGoal(6, new FollowPlayerGoal(this, (double)1.4F, 3.0F, 10.0F));

        this.goalSelector.addGoal(8, new WaterAvoidingRandomStrollGoal(this, (double)1.0F));

        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(10, new RandomLookAroundGoal(this));
    }

    private void setupAnimationStates() {
        if (this.getDeltaMovement().horizontalDistanceSqr() > 2.5000003E-7F && !this.getTripped()) {
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
            if (this.jumpUp) {
                this.tripBeginAnimationState.stop();
                this.tripAnimationState.stop();
                this.tripEndAnimationState.start(this.tickCount);
            }
        }
    }

    @Override
    public void tick() {
        int trippedCooldown = this.entityData.get(DATA_TRIP_COOLDOWN);
        int tripTick = this.entityData.get(DATA_TRIPPED_TICK);
        
        if (this.level().isClientSide()) {
            this.setupAnimationStates();
        }

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

        if (this.jumpUp) {
            this.jumpUp = false;
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
                this.jumpUp = true;
                if (this.tripwireTrip) {
                    this.entityData.set(DATA_TRIPWIRE_TRIP_COOLDOWN, 600);
                    this.tripwireTrip = false;
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
        DATA_TRIPPED_TICK = SynchedEntityData.defineId(LithyEntity.class, EntityDataSerializers.INT);
        DATA_TRIP_COOLDOWN = SynchedEntityData.defineId(LithyEntity.class, EntityDataSerializers.INT);
        DATA_TRIPWIRE_TRIP_COOLDOWN = SynchedEntityData.defineId(LithyEntity.class, EntityDataSerializers.INT);
    }

}
