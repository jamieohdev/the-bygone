package com.jamiedev.bygone.entities;

import com.jamiedev.bygone.entities.ai.LubberNavigation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class LubberEntity  extends Monster implements RangedAttackMob
{
    LubberEntity ref;
    Skeleton ref2;

    private static final EntityDataAccessor<Byte> LUBBER_FLAGS;
    private static final float field_30498 = 0.1F;

    protected LubberEntity(EntityType<? extends LubberEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    public void performRangedAttack(LivingEntity target, float pullProgress) {

    }
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, BigBeakEntity.class, 6.0F, 1.0, 1.2, (entity) -> {
            return !((BigBeakEntity)entity).isFlapping();
        }));
        this.goalSelector.addGoal(3, new LeapAtTargetGoal(this, 0.4F));
        this.goalSelector.addGoal(4, new LubberEntity.AttackGoal(this));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new LubberEntity.TargetGoal<>(this, Player.class));
        this.targetSelector.addGoal(3, new LubberEntity.TargetGoal<>(this, IronGolem.class));
    }

    @Override
    protected PathNavigation createNavigation(Level world) {
        return new LubberNavigation(this, world);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(LUBBER_FLAGS, (byte)0);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide) {
            this.setClimbingWall(this.horizontalCollision);
        }

    }

    public static AttributeSupplier.Builder createLubberAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 16.0).add(Attributes.MOVEMENT_SPEED, 0.30000001192092896);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.SILVERFISH_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.SILVERFISH_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SILVERFISH_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.SILVERFISH_STEP, 0.15F, 1.0F);
    }

    @Override
    public boolean onClimbable() {
        return this.isClimbingWall();
    }

    @Override
    public void makeStuckInBlock(BlockState state, Vec3 multiplier) {
        if (!state.is(Blocks.COBWEB)) {
            super.makeStuckInBlock(state, multiplier);
        }

    }

    @Override
    public boolean canBeAffected(MobEffectInstance effect) {
        return !effect.is(MobEffects.POISON) && super.canBeAffected(effect);
    }

    public boolean isClimbingWall() {
        return (this.entityData.get(LUBBER_FLAGS) & 1) != 0;
    }

    public void setClimbingWall(boolean climbing) {
        byte b = this.entityData.get(LUBBER_FLAGS);
        if (climbing) {
            b = (byte)(b | 1);
        } else {
            b &= -2;
        }

        this.entityData.set(LUBBER_FLAGS, b);
    }

    @Override
    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType spawnReason, @Nullable SpawnGroupData entityData) {
        SpawnGroupData entityData1 = super.finalizeSpawn(world, difficulty, spawnReason, entityData);
        RandomSource random = world.getRandom();
        if (random.nextInt(100) == 0) {
            Skeleton skeletonEntity = EntityType.SKELETON.create(this.level());
            if (skeletonEntity != null) {
                skeletonEntity.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
                skeletonEntity.finalizeSpawn(world, difficulty, spawnReason, null);
                skeletonEntity.startRiding(this);
            }
        }

        if (entityData1 == null) {
            entityData1 = new LubberEntity.LubberData();
            if (world.getDifficulty() == Difficulty.HARD && random.nextFloat() < 0.1F * difficulty.getSpecialMultiplier()) {
                ((LubberEntity.LubberData)entityData1).setEffect(random);
            }
        }

        if (entityData1 instanceof LubberEntity.LubberData spiderData) {
            Holder<MobEffect> registryEntry = spiderData.effect;
            if (registryEntry != null) {
                this.addEffect(new MobEffectInstance(registryEntry, -1));
            }
        }

        return entityData1;
    }

    @Override
    public Vec3 getVehicleAttachmentPoint(Entity vehicle) {
        return vehicle.getBbWidth() <= this.getBbWidth() ? new Vec3(0.0, 0.3125 * (double)this.getScale(), 0.0) : super.getVehicleAttachmentPoint(vehicle);
    }

    static {
        LUBBER_FLAGS = SynchedEntityData.defineId(LubberEntity.class, EntityDataSerializers.BYTE);
    }

    private static class AttackGoal extends MeleeAttackGoal {
        public AttackGoal(LubberEntity spider) {
            super(spider, 1.0, true);
        }

        @Override
        public boolean canUse() {
            return super.canUse() && !this.mob.isVehicle();
        }

        @Override
        public boolean canContinueToUse() {
            float f = this.mob.getLightLevelDependentMagicValue();
            if (f >= 0.5F && this.mob.getRandom().nextInt(100) == 0) {
                this.mob.setTarget(null);
                return false;
            } else {
                return super.canContinueToUse();
            }
        }
    }

    private static class TargetGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
        public TargetGoal(LubberEntity spider, Class<T> targetEntityClass) {
            super(spider, targetEntityClass, true);
        }

        @Override
        public boolean canUse() {
            float f = this.mob.getLightLevelDependentMagicValue();
            return !(f >= 0.5F) && super.canUse();
        }
    }

    public static class LubberData implements SpawnGroupData {
        @Nullable
        public Holder<MobEffect> effect;

        public LubberData() {
        }

        public void setEffect(RandomSource random) {
            int i = random.nextInt(5);
            if (i <= 1) {
                this.effect = MobEffects.MOVEMENT_SPEED;
            } else if (i <= 2) {
                this.effect = MobEffects.DAMAGE_BOOST;
            } else if (i <= 3) {
                this.effect = MobEffects.REGENERATION;
            } else if (i <= 4) {
                this.effect = MobEffects.INVISIBILITY;
            }

        }
    }
}
