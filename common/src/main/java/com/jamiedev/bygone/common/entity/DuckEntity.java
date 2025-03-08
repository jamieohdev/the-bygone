package com.jamiedev.bygone.common.entity;

import com.jamiedev.bygone.core.registry.BGEntityTypes;
import net.minecraft.core.BlockPos;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class DuckEntity extends Animal
{
    private static final EntityDimensions BABY_BASE_DIMENSIONS;
    public float flapProgress;
    public float maxWingDeviation;
    public float prevMaxWingDeviation;
    public float prevFlapProgress;
    public float flapSpeed = 1.0F;
    private float field_28639 = 1.0F;
    public int eggLayTime;
    public boolean hasJockey;

    public DuckEntity(EntityType<? extends DuckEntity> entityType, Level world) {
        super(entityType, world);
        this.eggLayTime = this.random.nextInt(6000) + 6000;
        this.setPathfindingMalus(PathType.WATER, 0.0F);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.4));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.0, (stack) -> {
            return stack.is(ItemTags.CHICKEN_FOOD);
        }, false));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.1));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
    }

    @Override
    public EntityDimensions getDefaultDimensions(Pose pose) {
        return this.isBaby() ? BABY_BASE_DIMENSIONS : super.getDefaultDimensions(pose);
    }

    public static AttributeSupplier.Builder createDuckAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 4.0).add(Attributes.MOVEMENT_SPEED, 0.25);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        this.prevFlapProgress = this.flapProgress;
        this.prevMaxWingDeviation = this.maxWingDeviation;
        this.maxWingDeviation += (this.onGround() ? -1.0F : 4.0F) * 0.3F;
        this.maxWingDeviation = Mth.clamp(this.maxWingDeviation, 0.0F, 1.0F);
        if (!this.onGround() && this.flapSpeed < 1.0F) {
            this.flapSpeed = 1.0F;
        }

        this.flapSpeed *= 0.9F;
        Vec3 vec3d = this.getDeltaMovement();
        if (!this.onGround() && vec3d.y < 0.0) {
            this.setDeltaMovement(vec3d.multiply(1.0, 0.6, 1.0));
        }

        this.flapProgress += this.flapSpeed * 2.0F;
        if (!this.level().isClientSide && this.isAlive() && !this.isBaby() && !this.hasJockey() && --this.eggLayTime <= 0) {
            this.playSound(SoundEvents.CHICKEN_EGG, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
            this.spawnAtLocation(Items.EGG);
            this.gameEvent(GameEvent.ENTITY_PLACE);
            this.eggLayTime = this.random.nextInt(6000) + 6000;
        }

    }

    @Override
    protected boolean isFlapping() {
        return this.flyDist > this.field_28639;
    }

    @Override
    protected void onFlap() {
        this.field_28639 = this.flyDist + this.maxWingDeviation / 2.0F;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.CHICKEN_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.CHICKEN_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.CHICKEN_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.CHICKEN_STEP, 0.15F, 1.0F);
    }

    @Override
    @Nullable
    public DuckEntity getBreedOffspring(ServerLevel serverWorld, AgeableMob passiveEntity) {
        return BGEntityTypes.DUCK.create(serverWorld);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(ItemTags.CHICKEN_FOOD);
    }

    @Override
    protected int getBaseExperienceReward() {
        return this.hasJockey() ? 10 : super.getBaseExperienceReward();
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        this.hasJockey = nbt.getBoolean("IsDuckJockey");
        if (nbt.contains("EggLayTime")) {
            this.eggLayTime = nbt.getInt("EggLayTime");
        }

    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putBoolean("IsDuckJockey", this.hasJockey);
        nbt.putInt("EggLayTime", this.eggLayTime);
    }

    @Override
    public boolean removeWhenFarAway(double distanceSquared) {
        return this.hasJockey();
    }

    @Override
    protected void positionRider(Entity passenger, Entity.MoveFunction positionUpdater) {
        super.positionRider(passenger, positionUpdater);
        if (passenger instanceof LivingEntity) {
            ((LivingEntity)passenger).yBodyRot = this.yBodyRot;
        }

    }

    public boolean hasJockey() {
        return this.hasJockey;
    }

    public void setHasJockey(boolean hasJockey) {
        this.hasJockey = hasJockey;
    }

    static {
        BABY_BASE_DIMENSIONS = EntityType.CHICKEN.getDimensions().scale(0.5F).withEyeHeight(0.2975F);
    }
}
