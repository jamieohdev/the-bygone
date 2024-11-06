package com.jamiedev.mod.entities;

import com.jamiedev.mod.entities.ai.LubberNavigation;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Difficulty;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class LubberEntity  extends HostileEntity implements RangedAttackMob
{
    LubberEntity ref;
    SkeletonEntity ref2;

    private static final TrackedData<Byte> LUBBER_FLAGS;
    private static final float field_30498 = 0.1F;

    protected LubberEntity(EntityType<? extends LubberEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void shootAt(LivingEntity target, float pullProgress) {

    }
    protected void initGoals() {
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(2, new FleeEntityGoal(this, BigBeakEntity.class, 6.0F, 1.0, 1.2, (entity) -> {
            return !((BigBeakEntity)entity).isFlappingWings();
        }));
        this.goalSelector.add(3, new PounceAtTargetGoal(this, 0.4F));
        this.goalSelector.add(4, new LubberEntity.AttackGoal(this));
        this.goalSelector.add(5, new WanderAroundFarGoal(this, 0.8));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(6, new LookAroundGoal(this));
        this.targetSelector.add(1, new RevengeGoal(this, new Class[0]));
        this.targetSelector.add(2, new LubberEntity.TargetGoal(this, PlayerEntity.class));
        this.targetSelector.add(3, new LubberEntity.TargetGoal(this, IronGolemEntity.class));
    }

    protected EntityNavigation createNavigation(World world) {
        return new LubberNavigation(this, world);
    }

    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(LUBBER_FLAGS, (byte)0);
    }

    public void tick() {
        super.tick();
        if (!this.getWorld().isClient) {
            this.setClimbingWall(this.horizontalCollision);
        }

    }

    public static DefaultAttributeContainer.Builder createLubberAttributes() {
        return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 16.0).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.30000001192092896);
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_SILVERFISH_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_SILVERFISH_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_SILVERFISH_DEATH;
    }

    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.ENTITY_SILVERFISH_STEP, 0.15F, 1.0F);
    }

    public boolean isClimbing() {
        return this.isClimbingWall();
    }

    public void slowMovement(BlockState state, Vec3d multiplier) {
        if (!state.isOf(Blocks.COBWEB)) {
            super.slowMovement(state, multiplier);
        }

    }

    public boolean canHaveStatusEffect(StatusEffectInstance effect) {
        return effect.equals(StatusEffects.POISON) ? false : super.canHaveStatusEffect(effect);
    }

    public boolean isClimbingWall() {
        return ((Byte)this.dataTracker.get(LUBBER_FLAGS) & 1) != 0;
    }

    public void setClimbingWall(boolean climbing) {
        byte b = (Byte)this.dataTracker.get(LUBBER_FLAGS);
        if (climbing) {
            b = (byte)(b | 1);
        } else {
            b &= -2;
        }

        this.dataTracker.set(LUBBER_FLAGS, b);
    }

    @Nullable
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {
        EntityData entityData1 = super.initialize(world, difficulty, spawnReason, entityData);
        Random random = world.getRandom();
        if (random.nextInt(100) == 0) {
            SkeletonEntity skeletonEntity = (SkeletonEntity)EntityType.SKELETON.create(this.getWorld());
            if (skeletonEntity != null) {
                skeletonEntity.refreshPositionAndAngles(this.getX(), this.getY(), this.getZ(), this.getYaw(), 0.0F);
                skeletonEntity.initialize(world, difficulty, spawnReason, (EntityData)null);
                skeletonEntity.startRiding(this);
            }
        }

        if (entityData1 == null) {
            entityData1 = new LubberEntity.LubberData();
            if (world.getDifficulty() == Difficulty.HARD && random.nextFloat() < 0.1F * difficulty.getClampedLocalDifficulty()) {
                ((LubberEntity.LubberData)entityData1).setEffect(random);
            }
        }

        if (entityData1 instanceof LubberEntity.LubberData spiderData) {
            RegistryEntry<StatusEffect> registryEntry = spiderData.effect;
            if (registryEntry != null) {
                this.addStatusEffect(new StatusEffectInstance(registryEntry, -1));
            }
        }

        return (EntityData)entityData1;
    }

    public Vec3d getVehicleAttachmentPos(Entity vehicle) {
        return vehicle.getWidth() <= this.getWidth() ? new Vec3d(0.0, 0.3125 * (double)this.getScale(), 0.0) : super.getVehicleAttachmentPos(vehicle);
    }

    static {
        LUBBER_FLAGS = DataTracker.registerData(LubberEntity.class, TrackedDataHandlerRegistry.BYTE);
    }

    private static class AttackGoal extends MeleeAttackGoal {
        public AttackGoal(LubberEntity spider) {
            super(spider, 1.0, true);
        }

        public boolean canStart() {
            return super.canStart() && !this.mob.hasPassengers();
        }

        public boolean shouldContinue() {
            float f = this.mob.getBrightnessAtEyes();
            if (f >= 0.5F && this.mob.getRandom().nextInt(100) == 0) {
                this.mob.setTarget((LivingEntity)null);
                return false;
            } else {
                return super.shouldContinue();
            }
        }
    }

    private static class TargetGoal<T extends LivingEntity> extends ActiveTargetGoal<T> {
        public TargetGoal(LubberEntity spider, Class<T> targetEntityClass) {
            super(spider, targetEntityClass, true);
        }

        public boolean canStart() {
            float f = this.mob.getBrightnessAtEyes();
            return f >= 0.5F ? false : super.canStart();
        }
    }

    public static class LubberData implements EntityData {
        @Nullable
        public RegistryEntry<StatusEffect> effect;

        public LubberData() {
        }

        public void setEffect(Random random) {
            int i = random.nextInt(5);
            if (i <= 1) {
                this.effect = StatusEffects.SPEED;
            } else if (i <= 2) {
                this.effect = StatusEffects.STRENGTH;
            } else if (i <= 3) {
                this.effect = StatusEffects.REGENERATION;
            } else if (i <= 4) {
                this.effect = StatusEffects.INVISIBILITY;
            }

        }
    }
}
