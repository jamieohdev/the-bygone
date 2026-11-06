package com.jamiedev.bygone.common.entity;

import com.jamiedev.bygone.common.util.Animatable;
import com.jamiedev.bygone.core.init.JamiesModLootTables;
import com.jamiedev.bygone.core.init.JamiesModTag;
import com.jamiedev.bygone.core.registry.BGBlocks;
import com.jamiedev.bygone.core.registry.BGSoundEvents;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class LithyEntity extends PathfinderMob implements Animatable<LithyEntity,LithyEntity.State>{
    private static final EntityDataAccessor<Integer> DATA_STATE = SynchedEntityData.defineId(LithyEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> DATA_TARGETING = SynchedEntityData.defineId(LithyEntity.class, EntityDataSerializers.BOOLEAN);

    private final Animatable.Holder<LithyEntity,State> animations = new Animatable.Holder<>(this, State.values());
    private int randomTripCooldown = this.random.nextBoolean() ? genRandomTripTime() : 0;
    private int tripwireTripCooldown = 0;

    private static final EntityDimensions TRIPPED_DIMENSIONS = EntityDimensions.scalable(0.8F, 0.8F).withEyeHeight(0.4F);

    public LithyEntity(EntityType<? extends PathfinderMob> entityType, Level level){
        super(entityType, level);
    }
    public static AttributeSupplier.Builder createAttributes(){
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 5.0F)
                .add(Attributes.MOVEMENT_SPEED, 0.25F)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0F)
                .add(Attributes.ATTACK_DAMAGE, 4.0F)
                .add(Attributes.STEP_HEIGHT, 1.0F);
    }
    public static boolean canSpawn(EntityType<LithyEntity> type, LevelAccessor level, MobSpawnType reason, BlockPos pos, RandomSource random){
        return level.getBlockState(pos.below()).is(JamiesModTag.LITHY_SPAWNABLE_ON);
    }

    @Override
    protected void registerGoals(){
        this.goalSelector.addGoal(1, new LithyTripGoal());
        this.goalSelector.addGoal(2, new FloatGoal(this));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, WraithEntity.class, 6.0F, 1.0F, 1.2));
        this.goalSelector.addGoal(4, new LeapAtTargetGoal(this, 0.4F));
        this.goalSelector.addGoal(5, new MeleeAttackGoal(this, 1.0F, true));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new FollowMobGoal(this, 0.5F, 3.0F, 10.0F));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 0.5));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, false));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
    }



    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder){
        super.defineSynchedData(builder);
        builder.define(DATA_STATE, 0);
        builder.define(DATA_TARGETING, false);
    }
    @Override
    public void readAdditionalSaveData(CompoundTag tag){
        super.readAdditionalSaveData(tag);
        // check contains due to migration to new tag names
        if(tag.contains("RandomTripCooldown")) this.randomTripCooldown = tag.getInt("RandomTripCooldown");
        if(tag.contains("TripwireTripCooldown")) this.tripwireTripCooldown = tag.getInt("TripwireTripCooldown");
    }
    @Override
    public void addAdditionalSaveData(CompoundTag tag){
        super.addAdditionalSaveData(tag);
        tag.putInt("RandomTripCooldown", this.randomTripCooldown);
        tag.putInt("TripwireTripCooldown", this.tripwireTripCooldown);
    }
    private int genRandomTripTime(){ return 1200 + this.random.nextInt(0, 200); }

    @Override public Animatable.Holder<LithyEntity,State> animations(){ return animations; }
    @Override public EntityDataAccessor<Integer> getStateData(){ return DATA_STATE; }



    @Override
    public void tick(){
        super.tick();
        if(this.tripwireTripCooldown > 0) this.tripwireTripCooldown--;
        if(this.randomTripCooldown > 0) this.randomTripCooldown--;

        // don't check equality beforehand, set() does that for us
        if(!this.level().isClientSide) this.getEntityData().set(DATA_TARGETING, this.getTarget() != null);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key){
        super.onSyncedDataUpdated(key);
        if(this.animations != null) this.animations.onSyncedDataUpdated(key);

        // funky trip effects
        if(DATA_STATE.equals(key) && this.getState() == State.TRIPPED){
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
    }

    private boolean isMoving(){
        if(this.isPassenger()) return false;
        return this.getDeltaMovement().horizontalDistanceSqr() > Mth.EPSILON * Mth.EPSILON;
    }



    @Override protected SoundEvent getAmbientSound() { return BGSoundEvents.LITHY_AMBIENT_ADDITIONS_EVENT; }
    @Override protected SoundEvent getHurtSound(DamageSource damageSource) { return BGSoundEvents.LITHY_HURT_ADDITIONS_EVENT; }
    @Override protected SoundEvent getDeathSound() { return BGSoundEvents.LITHY_DEATH_ADDITIONS_EVENT; }
    @Override protected int decreaseAirSupply(int air){ return air; }
    @Override public boolean canHoldItem(ItemStack stack){ return false; }
    @Override
    public boolean canSpawnSprintParticle(){
        return this.isMoving() && this.onGround() && this.getEntityData().get(DATA_TARGETING) && this.random.nextInt(5) == 0;
    }
    @Override
    public EntityDimensions getDefaultDimensions(Pose pose){
        if(pose == Pose.FALL_FLYING) return TRIPPED_DIMENSIONS;
        else return super.getDefaultDimensions(pose);
    }



    public static enum State implements Animatable.State<LithyEntity>{
        IDLE, TRIPPED, RISING;
    }



    private class LithyTripGoal extends Goal{
        private int ticks = 0;
        private int threshold = 0;
        private boolean tripwire = false;
        private LithyTripGoal(){
            this.setFlags(EnumSet.allOf(Goal.Flag.class));
        }

        private void progress(State state, int threshold){
            this.ticks = 0;
            this.threshold = this.adjustedTickDelay(threshold);
            LithyEntity.this.setState(state);
        }
        private boolean completed(State state){
            return ticks >= threshold && state == LithyEntity.this.getState();
        }



        @Override
        public void start(){
            this.progress(State.TRIPPED, 60);
            LithyEntity.this.setPose(Pose.FALL_FLYING); // idk it's the closest
            LithyEntity.this.getLookControl().setLookAt(LithyEntity.this.getEyePosition().add(LithyEntity.this.getDeltaMovement()));
            LithyEntity.this.setTarget(null);
            LithyEntity.this.push(LithyEntity.this.getDeltaMovement().multiply(0.5, 0.25, 0.5));

            if(LithyEntity.this.level() instanceof ServerLevel server){
                Vec3 expansion = LithyEntity.this.getDeltaMovement().scale(8);
                AABB aabb = LithyEntity.this.getBoundingBox().expandTowards(expansion);
                for(Entity entity : LithyEntity.this.level().getEntities(LithyEntity.this, aabb, Entity::canBeHitByProjectile)){
                    if(!(entity instanceof LithyEntity)){
                        double damage = LithyEntity.this.getAttributeValue(Attributes.ATTACK_DAMAGE);
                        entity.hurt(LithyEntity.this.damageSources().cramming(), (float)(damage * 2));
                    }
                    if(entity instanceof LivingEntity living){
                        Vec3 knockback = LithyEntity.this.getDeltaMovement().multiply(-1, 0, -1).normalize();
                        living.knockback(LithyEntity.this.getDeltaMovement().length(), knockback.x, knockback.z);
                    }
                }

                LootTable table = server.getServer().reloadableRegistries().getLootTable(JamiesModLootTables.LITHY_TRIP_LOOT_TABLE);
                LootParams params = new LootParams.Builder(server)
                        .withParameter(LootContextParams.ORIGIN, LithyEntity.this.position())
                        .withParameter(LootContextParams.THIS_ENTITY, LithyEntity.this)
                        .create(LootContextParamSets.GIFT);
                for(ItemStack stack : table.getRandomItems(params)){
                    LithyEntity.this.spawnAtLocation(stack, LithyEntity.this.getEyeHeight()).push(LithyEntity.this.getDeltaMovement());
                }
            }
        }
        @Override
        public void tick(){
            this.ticks++;
            if(this.completed(State.TRIPPED) && LithyEntity.this.random.nextFloat() < 0.02){
                this.progress(State.RISING, 25);
            }
        }
        @Override
        public void stop(){
            LithyEntity.this.setState(State.IDLE);
            LithyEntity.this.setPose(Pose.STANDING);
            if(tripwire) LithyEntity.this.tripwireTripCooldown = 600;
            else LithyEntity.this.randomTripCooldown = LithyEntity.this.genRandomTripTime();
        }



        @Override
        public boolean canUse(){
            if(!LithyEntity.this.isMoving()) return false;
            BlockState state = LithyEntity.this.level().getBlockState(LithyEntity.this.getOnPos().above());
            tripwire = (state.is(Blocks.TRIPWIRE) || state.is(Blocks.TRIPWIRE_HOOK)) && state.getValue(BlockStateProperties.ATTACHED);
            if(tripwire) return LithyEntity.this.tripwireTripCooldown <= 0;
            else return LithyEntity.this.randomTripCooldown <= 0 && !LithyEntity.this.onGround();
        }
        @Override public boolean canContinueToUse(){ return !this.completed(State.RISING); }
        @Override public boolean isInterruptable(){ return false; }
    }
}
