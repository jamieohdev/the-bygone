package com.jamiedev.bygone.common.entity;

import com.jamiedev.bygone.common.block.gourds.GourdLanternBlock;
import com.jamiedev.bygone.common.entity.ai.BGRemoveBlockGoal;
import com.jamiedev.bygone.common.entity.ai.WhiskbillEatGourdGoal;
import com.jamiedev.bygone.core.init.JamiesModTag;
import com.jamiedev.bygone.core.registry.BGBlocks;
import com.jamiedev.bygone.core.registry.BGEntityTypes;
import com.jamiedev.bygone.core.registry.BGItems;
import com.jamiedev.bygone.core.registry.BGSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.function.Predicate;

public class WhiskbillEntity extends Animal
{

    private static final EntityDimensions BABY_BASE_DIMENSIONS;
    public AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;
    int moreCarrotTicks;
    private static final Predicate<Entity> AVOID_PLAYERS;

    private static final EntityDataAccessor<Byte> DATA_FLAGS_ID;

    public WhiskbillEntity(EntityType<? extends WhiskbillEntity> entityType, Level level) {
        super(entityType, level);
    }

    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_FLAGS_ID, (byte)0);
    }

    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("MoreGourdTicks", this.moreCarrotTicks);
    }

    protected static boolean isBrightEnoughToSpawn(BlockAndTintGetter world, BlockPos pos) {
        return world.getRawBrightness(pos, 0) > 1;
    }

    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.moreCarrotTicks = compound.getInt("MoreGourdTicks");
    }

    public void customServerAiStep() {
        if (this.moreCarrotTicks > 0) {
            this.moreCarrotTicks -= this.random.nextInt(3);
            if (this.moreCarrotTicks < 0) {
                this.moreCarrotTicks = 0;
            }
        }

    }

    private void setupAnimationStates() {
        if (this.idleAnimationTimeout <= 0) {
            this.idleAnimationTimeout = this.random.nextInt(100) + 100;
            this.idleAnimationState.start(this.tickCount);
        } else {
            --this.idleAnimationTimeout;
        }

    }

    boolean isLookingAtMe(Player player) {
        ItemStack itemstack = (ItemStack)player.getInventory().armor.get(3);
        if (itemstack.is(Blocks.CARVED_PUMPKIN.asItem())) {
            return false;
        } else {
            Vec3 vec3 = player.getViewVector(1.0F).normalize();
            Vec3 vec31 = new Vec3(this.getX() - player.getX(), this.getEyeY() - player.getEyeY(), this.getZ() - player.getZ());
            double d0 = vec31.length();
            vec31 = vec31.normalize();
            double d1 = vec3.dot(vec31);
            return d1 > (double) 1.0F - 0.025 / d0 && player.hasLineOfSight(this);
        }
    }

    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.SNIFFER_STEP, 0.15F, 1.0F);
    }

    public SoundEvent getEatingSound(ItemStack stack) {
        return SoundEvents.SNIFFER_EAT;
    }

    protected SoundEvent getAmbientSound() {
        return BGSoundEvents.WHISKBILL_AMBIENT_ADDITIONS_EVENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return BGSoundEvents.WHISKBILL_HURT_ADDITIONS_EVENT;
    }

    protected SoundEvent getDeathSound() {
        return BGSoundEvents.WHISKBILL_DEATH_ADDITIONS_EVENT;
    }

    public boolean isClimbing() {
        return ((Byte)this.entityData.get(DATA_FLAGS_ID) & 1) != 0;
    }

    public boolean hurt(DamageSource source, float amount) {
        if (this.level().isClientSide) {
            return false;
        } else {
            if (source.is(DamageTypeTags.IS_PLAYER_ATTACK)) {
                Entity var4 = source.getDirectEntity();
                if (var4 instanceof LivingEntity) {
                    LivingEntity livingentity = (LivingEntity)var4;
                    livingentity.hurt(this.damageSources().thorns(this), 2.0F);
                    livingentity.addEffect(new MobEffectInstance(MobEffects.POISON, 60 * 1, 0), this);
                }
            }

            return super.hurt(source, amount);
        }
    }


    public void setClimbing(boolean climbing) {
        byte b0 = (Byte)this.entityData.get(DATA_FLAGS_ID);
        if (climbing) {
            b0 = (byte)(b0 | 1);
        } else {
            b0 = (byte)(b0 & -2);
        }

        this.entityData.set(DATA_FLAGS_ID, b0);
    }

    public boolean onClimbable() {
        return this.isClimbing();
    }

    public void tick() {
        super.tick();
        if (!this.level().isClientSide) {
            this.setClimbing(this.horizontalCollision);
        }
        if (this.level().isClientSide) {
            setupAnimationStates();
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED, (double)0.20F).add(Attributes.MAX_HEALTH, (double)10.0F);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(1, new ClimbOnTopOfPowderSnowGoal(this, this.level()));
        this.goalSelector.addGoal(2, new BreedGoal(this, 0.8));
        this.goalSelector.addGoal(3, new TemptGoal(this, (double)1.0F, (p_335873_) -> p_335873_.is(JamiesModTag.WHISKBILL_FOOD), false));
        this.goalSelector.addGoal(4, new AvoidEntityGoal<>(this, Player.class, 16.0F, 1.3, 1.4, (p_352798_) -> AVOID_PLAYERS.test((Entity) p_352798_) && !this.isBaby()));

        this.goalSelector.addGoal(4, new WhiskbillEntity.EatGourdGoal(this, 1.5, 11, 9));
        /*this.goalSelector.addGoal(4, new WhiskbillEntity.EatVerdantGourdGoal(this, 1.5, 9, 9));
        this.goalSelector.addGoal(4, new WhiskbillEntity.EatBeigeGourdGoal(this, 1.5, 9, 9));
        this.goalSelector.addGoal(4, new WhiskbillEntity.EatMuaveGourdGoal(this, 1.5, 9, 9));*/

        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 0.6));
        this.goalSelector.addGoal(11, new LookAtPlayerGoal(this, Player.class, 10.0F));
    }


    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(JamiesModTag.WHISKBILL_FOOD);
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return BGEntityTypes.WHISKBILL.get().create(serverLevel);
    }

    boolean wantsMoreFood() {
        return random.nextInt(700) == 1;
    }

    public boolean isSteppingCarefully() {
        return this.isCrouching() || super.isSteppingCarefully();
    }

    class EatGourdGoal extends WhiskbillEatGourdGoal {
        EatGourdGoal(PathfinderMob mob, double speedModifier, int searchRange, int verticalSearchRange) {
            super(mob, speedModifier, searchRange, verticalSearchRange);
        }

        @Override
        public void playDestroyProgressSound(LevelAccessor level, BlockPos pos) {
            level.playSound(null, pos, SoundEvents.CAMEL_EAT, SoundSource.HOSTILE, 0.5F, 0.9F + WhiskbillEntity.this.random.nextFloat() * 0.2F);
        }

        @Override
        public void playBreakSound(Level level, BlockPos pos) {
            level.playSound(null, pos, SoundEvents.PLAYER_BURP, SoundSource.BLOCKS, 0.7F, 0.9F + level.random.nextFloat() * 0.2F);
        }

        @Override
        public double acceptedDistance() {
            return 4.0;
        }
    }

    @Override
    public EntityDimensions getDefaultDimensions(Pose pose) {
        return this.isBaby() ? BABY_BASE_DIMENSIONS : super.getDefaultDimensions(pose);
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 2;
    }

    static {
        BABY_BASE_DIMENSIONS = BGEntityTypes.WHISKBILL.get().getDimensions().withAttachments(EntityAttachments.builder().attach(EntityAttachment.PASSENGER, 0.0F,
                BGEntityTypes.WHISKBILL.get().getHeight() + 0.125F, 0.0F)).scale(0.45F);
        DATA_FLAGS_ID = SynchedEntityData.defineId(WhiskbillEntity.class, EntityDataSerializers.BYTE);
        AVOID_PLAYERS = (p_28463_) -> !p_28463_.isDiscrete() && EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(p_28463_);
    }
}
