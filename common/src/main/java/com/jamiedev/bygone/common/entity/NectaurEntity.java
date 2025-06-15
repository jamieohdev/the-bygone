package com.jamiedev.bygone.common.entity;

import com.google.common.collect.ImmutableList;
import com.jamiedev.bygone.common.entity.ai.HydropusBrain;
import com.jamiedev.bygone.common.entity.ai.NectaurBrain;
import com.jamiedev.bygone.core.registry.BGEntityTypes;
import com.jamiedev.bygone.core.registry.BGMemoryModuleTypes;
import com.jamiedev.bygone.core.registry.BGSoundEvents;
import com.mojang.serialization.Dynamic;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.monster.piglin.PiglinBruteAi;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.LingeringPotionItem;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class NectaurEntity extends Animal implements NeutralMob, RangedAttackMob {

    ZombifiedPiglin ref;
    private static final UniformInt FIRST_ANGER_SOUND_DELAY;
    private int playFirstAngerSoundIn;

    protected static final ImmutableList<SensorType<? extends Sensor<? super NectaurEntity>>> SENSOR_TYPES = ImmutableList.of(
            SensorType.NEAREST_LIVING_ENTITIES,
            SensorType.NEAREST_PLAYERS,
            SensorType.HURT_BY
    );
    protected static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(
            BGMemoryModuleTypes.NEAREST_NECTAUR_ALLY,
            BGMemoryModuleTypes.IS_LEADER,
            BGMemoryModuleTypes.IS_IN_GROUP,
            BGMemoryModuleTypes.GROUP_LEADER,
            BGMemoryModuleTypes.NECTAUR_RANGED_COOLDOWN,
            MemoryModuleType.PATH,
            MemoryModuleType.LOOK_TARGET,
            MemoryModuleType.NEAREST_LIVING_ENTITIES,
            MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES,
            MemoryModuleType.NEAREST_VISIBLE_PLAYER,
            MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER,
            MemoryModuleType.NEAREST_VISIBLE_NEMESIS,
            MemoryModuleType.HURT_BY,
            MemoryModuleType.HURT_BY_ENTITY,
            MemoryModuleType.WALK_TARGET,
            MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE,
            MemoryModuleType.ATTACK_TARGET,
            MemoryModuleType.ATTACK_COOLING_DOWN,
            MemoryModuleType.INTERACTION_TARGET,
            MemoryModuleType.ANGRY_AT,
            MemoryModuleType.UNIVERSAL_ANGER,
            MemoryModuleType.NEAREST_ATTACKABLE
    );

    public NectaurEntity(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.SPAWN_REINFORCEMENTS_CHANCE, (double)0.1F)
                .add(Attributes.ATTACK_DAMAGE, (double)4.0F)
                .add(Attributes.MAX_HEALTH, 8.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25);
    }

    @Override
    protected Brain.Provider<NectaurEntity> brainProvider() {
        return Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
    }

    @Override
    protected Brain<?> makeBrain(Dynamic<?> dynamic) {
        return NectaurBrain.create(this, this.brainProvider().makeBrain(dynamic));
    }

    @Override
    @SuppressWarnings("all")
    public Brain<NectaurEntity> getBrain() {
        return (Brain<NectaurEntity>) super.getBrain();
    }

    @Override
    protected void customServerAiStep() {

        if (this.isAngry()) {
            this.maybePlayFirstAngerSound();
        }

        this.level().getProfiler().push("nectaurBrain");
        this.getBrain().tick((ServerLevel)this.level(), this);
        this.level().getProfiler().pop();
        NectaurBrain.updateActivity(this);
        super.customServerAiStep();
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.getEntity() instanceof LivingEntity entity && source.isDirect() && entity.getMainHandItem().isEmpty()) {
            entity.hurt(damageSources().generic(), 1.0F);
        }
        else if (source.getEntity() instanceof NectaurEntity) {
            return false;
        }

        return super.hurt(source, amount);
    }

    public boolean canBeAffected(MobEffectInstance potioneffect) {
        return (!potioneffect.is(MobEffects.MOVEMENT_SLOWDOWN) && !potioneffect.is(MobEffects.POISON)) && super.canBeAffected(potioneffect);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return false;
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        return null;
    }

    @Override
    public int getRemainingPersistentAngerTime() {
        return 0;
    }

    @Override
    public void setRemainingPersistentAngerTime(int remainingPersistentAngerTime) {

    }

    @Override
    public @Nullable UUID getPersistentAngerTarget() {
        return null;
    }

    @Override
    public void setPersistentAngerTarget(@Nullable UUID persistentAngerTarget) {

    }

    @Override
    public void startPersistentAngerTimer() {

    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.isAngry() ? BGSoundEvents.NECTAUR_SCREECH_ADDITIONS_EVENT:BGSoundEvents.NECTAUR_AMBIENT_ADDITIONS_EVENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return BGSoundEvents.NECTAUR_HURT_ADDITIONS_EVENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return BGSoundEvents.NECTAUR_DEATH_ADDITIONS_EVENT;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState block) {
        this.playSound(SoundEvents.CHICKEN_STEP, 0.15F, 1.0F);
    }

    private void playAngerSound() {
        this.playSound(BGSoundEvents.NECTAUR_BELLOW_ADDITIONS_EVENT, this.getSoundVolume() * 2.0F, this.getVoicePitch() * 1.8F);
    }

    private void maybePlayFirstAngerSound() {
        if (this.playFirstAngerSoundIn > 0) {
            --this.playFirstAngerSoundIn;
            if (this.playFirstAngerSoundIn == 0) {
                this.playAngerSound();
            }
        }

    }

    @Override
    public void performRangedAttack(LivingEntity target, float velocity) {
        /*Vec3 vec3 = target.getDeltaMovement();
        double d0 = target.getX() + vec3.x - this.getX();
        double d1 = target.getEyeY() - 1.6F - this.getY();
        double d2 = target.getZ() + vec3.z - this.getZ();
        double d3 = Math.sqrt(d0 * d0 + d2 * d2);
        Holder<Potion> holder = Potions.POISON;

        ThrownPotion thrownpotion = new ThrownPotion(this.level(), this);
        thrownpotion.setItem(PotionContents.createItemStack(Items.LINGERING_POTION, holder));
        thrownpotion.setXRot(thrownpotion.getXRot() - -20.0F);
        thrownpotion.shoot(d0, d1 + d3 * 0.2, d2, 0.75F, 8.0F);
        this.level().addFreshEntity(thrownpotion);*/



        if (target.getRandom().nextInt(3) == 1)
        {
            ItemStack itemstack1 = new ItemStack(Items.ARROW);
            itemstack1.set(DataComponents.POTION_CONTENTS, new PotionContents(Potions.SLOWNESS));
            ArrowItem arrowItem = (ArrowItem) itemstack1.getItem();
            AbstractArrow abstractarrow = arrowItem.createArrow(this.level(), itemstack1, this, null);
            double d0 = target.getX() - this.getX();
            double d1 = target.getY(0.3333333333333333) - abstractarrow.getY();
            double d2 = target.getZ() - this.getZ();
            double d3 = Math.sqrt(d0 * d0 + d2 * d2);
            abstractarrow.shoot(d0, d1 + d3 * 0.2F, d2, 1.6F, (float)(14 - this.level().getDifficulty().getId() * 4));
            this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
            this.level().addFreshEntity(abstractarrow);
        }
        if (target.getRandom().nextInt(10) == 1)
        {
            ItemStack itemstack1 = new ItemStack(Items.ARROW);
            itemstack1.set(DataComponents.POTION_CONTENTS, new PotionContents(Potions.STRONG_SLOWNESS));
            ArrowItem arrowItem = (ArrowItem) itemstack1.getItem();
            AbstractArrow abstractarrow = arrowItem.createArrow(this.level(), itemstack1, this, null);
            double d0 = target.getX() - this.getX();
            double d1 = target.getY(0.3333333333333333) - abstractarrow.getY();
            double d2 = target.getZ() - this.getZ();
            double d3 = Math.sqrt(d0 * d0 + d2 * d2);
            abstractarrow.shoot(d0, d1 + d3 * 0.2F, d2, 1.6F, (float)(14 - this.level().getDifficulty().getId() * 4));
            this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
            this.level().addFreshEntity(abstractarrow);
        }
        if (target.getRandom().nextInt(25) == 1)
        {
            ItemStack itemstack1 = new ItemStack(Items.ARROW);
            itemstack1.set(DataComponents.POTION_CONTENTS, new PotionContents(Potions.STRONG_POISON));
            ArrowItem arrowItem = (ArrowItem) itemstack1.getItem();
            AbstractArrow abstractarrow = arrowItem.createArrow(this.level(), itemstack1, this, null);
            double d0 = target.getX() - this.getX();
            double d1 = target.getY(0.3333333333333333) - abstractarrow.getY();
            double d2 = target.getZ() - this.getZ();
            double d3 = Math.sqrt(d0 * d0 + d2 * d2);
            abstractarrow.shoot(d0, d1 + d3 * 0.2F, d2, 1.6F, (float)(14 - this.level().getDifficulty().getId() * 4));
            this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
            this.level().addFreshEntity(abstractarrow);
        }
        else
        {
            ItemStack itemstack1 = new ItemStack(Items.ARROW);
            itemstack1.set(DataComponents.POTION_CONTENTS, new PotionContents(Potions.POISON));
            ArrowItem arrowItem = (ArrowItem) itemstack1.getItem();
            AbstractArrow abstractarrow = arrowItem.createArrow(this.level(), itemstack1, this, null);
            double d0 = target.getX() - this.getX();
            double d1 = target.getY(0.3333333333333333) - abstractarrow.getY();
            double d2 = target.getZ() - this.getZ();
            double d3 = Math.sqrt(d0 * d0 + d2 * d2);
            abstractarrow.shoot(d0, d1 + d3 * 0.2F, d2, 1.6F, (float)(14 - this.level().getDifficulty().getId() * 4));
            this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
            this.level().addFreshEntity(abstractarrow);
        }

    }

    static
    {
        FIRST_ANGER_SOUND_DELAY = TimeUtil.rangeOfSeconds(0, 1);
    }
}