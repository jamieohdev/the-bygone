package com.jamiedev.bygone.common.entity;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class MoobooEntity extends Cow
{

    public static final EntityDataAccessor<Boolean> DATA_RENDER;

    public MoobooEntity(EntityType<? extends Cow> entityType, Level world) {
        super(entityType, world);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.MOVEMENT_SPEED, 0.20000000298023224);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 2.0));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_RENDER, false);
    }

    @Override
    public void addAdditionalSaveData(net.minecraft.nbt.CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("DataRender", this.entityData.get(DATA_RENDER));
    }

    @Override
    public void readAdditionalSaveData(net.minecraft.nbt.CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.entityData.set(DATA_RENDER, tag.getBoolean("DataRender"));
    }

    @Override
    public void tick() {
        float randomFloat = random.nextFloat();

        if (this.entityData.get(DATA_RENDER)) {
            if (randomFloat < 0.05) {
                this.entityData.set(DATA_RENDER, false);
            }
        }
        else {
            if (randomFloat < 0.01) {
                this.entityData.set(DATA_RENDER, true);
            }
        }

        super.tick();
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        if (this.entityData.get(DATA_RENDER)) {
            return false;
        }

        return super.shouldRenderAtSqrDistance(distance);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return false;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (itemStack.is(Items.BUCKET) && !this.isBaby()) {
            player.playSound(SoundEvents.COW_MILK, 1.0F, 1.0F);
            ItemStack itemStack2 = ItemUtils.createFilledResult(itemStack, player, Items.MILK_BUCKET.getDefaultInstance());
            player.setItemInHand(hand, itemStack2);
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        } else {
            return super.mobInteract(player, hand);
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.level().isClientSide) {
            return false;
        } else {
            if (source.is(DamageTypes.PLAYER_ATTACK)) {
                Entity var4 = source.getDirectEntity();
                if (var4 instanceof LivingEntity livingEntity) {
                    this.addEffect(new MobEffectInstance(MobEffects.LEVITATION,  20, 0), this);
                    this.addEffect(new MobEffectInstance(MobEffects.HEAL,  20, (int) (Math.max(1*amount, 0))), this);
                    this.addEffect(new MobEffectInstance(MobEffects.REGENERATION, (int) (20*amount), 0), this);
                }
            }
            if (source.is(DamageTypes.FALL)) {
                Entity var4 = source.getDirectEntity();
                if (var4 instanceof LivingEntity livingEntity) {
                    this.addEffect(new MobEffectInstance(MobEffects.HEAL,  20, 20), this);
                }
            }

            return super.hurt(source, amount);
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.COW_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.COW_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.COW_DEATH;
    }

    static {
        DATA_RENDER = SynchedEntityData.defineId(MoobooEntity.class, EntityDataSerializers.BOOLEAN);
    }

}
