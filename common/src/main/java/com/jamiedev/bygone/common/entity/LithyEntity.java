package com.jamiedev.bygone.common.entity;

import com.jamiedev.bygone.common.entity.ai.FollowPlayerGoal;
import com.jamiedev.bygone.core.init.JamiesModLootTables;
import com.jamiedev.bygone.core.registry.BGBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
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
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
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
    public boolean jumpUp = false;

    public LithyEntity(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_FLAGS_ID, (byte)0);
        builder.define(DATA_TRIPPED, false);
        builder.define(DATA_TRIPPED_TICK, 0);
        builder.define(DATA_TRIP_COOLDOWN, 1200 + this.random.nextInt(0, 200));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        this.entityData.set(DATA_TRIPPED, compound.getBoolean("DataTripped"));
        this.entityData.set(DATA_TRIPPED_TICK, compound.getInt("DataTrippedTick"));
        this.entityData.set(DATA_TRIP_COOLDOWN, compound.getInt("DataTripCooldown"));

        super.readAdditionalSaveData(compound);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        compound.putBoolean("DataTripped", this.entityData.get(DATA_TRIPPED));
        compound.putInt("DataTrippedTick", this.entityData.get(DATA_TRIPPED_TICK));
        compound.putInt("DataTripCooldown", this.entityData.get(DATA_TRIP_COOLDOWN));

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
        this.goalSelector.addGoal(6, new FollowMobGoal(this, (double)1.0F, 10.0F, 2.0F));
        this.goalSelector.addGoal(6, new FollowPlayerGoal(this, (double)1.0F, 10.0F, 4.0F));

        this.goalSelector.addGoal(8, new WaterAvoidingRandomStrollGoal(this, (double)1.0F));

        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(10, new RandomLookAroundGoal(this));
    }

    @Override
    public void tick() {
        int trippedCooldown = this.entityData.get(DATA_TRIP_COOLDOWN);
        int tripTick = this.entityData.get(DATA_TRIPPED_TICK);

        if (this.jumpUp) {
            this.jumpUp = false;
            this.push(new Vec3(0.0, 0.4, 0.0));
        }
        if (!this.entityData.get(DATA_TRIPPED)) {
            if (this.getDeltaMovement().horizontalDistanceSqr() > 2.5000003E-7F) {
                this.entityData.set(DATA_TRIP_COOLDOWN, trippedCooldown - 1);
                if (this.entityData.get(DATA_TRIP_COOLDOWN) <= 0 && this.random.nextFloat() < 0.1) {
                    this.push(this.getDeltaMovement().add(0.0, 0.2, 0.0));
                    this.entityData.set(DATA_TRIPPED, true);
                    this.entityData.set(DATA_TRIP_COOLDOWN, 1200 + this.random.nextInt(0, 200));
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
    public void travel(Vec3 travelVector) {
        Vec3 delta = this.getDeltaMovement();
        if (this.entityData.get(DATA_TRIPPED) && this.onGround()) {
            this.setDeltaMovement(delta.x * 0.1, delta.y, delta.z * 0.1);
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
    }
}
