package com.jamiedev.bygone.common.entity;

import com.jamiedev.bygone.core.registry.BGBlocks;
import com.jamiedev.bygone.core.registry.BGEntityTypes;
import com.jamiedev.bygone.core.registry.BGSoundEvents;
import com.jamiedev.bygone.core.init.JamiesModTag;
import com.jamiedev.bygone.common.item.CustomAnimalArmorItem;
import net.minecraft.Util;
import net.minecraft.client.model.HorseModel;
import net.minecraft.client.renderer.entity.HorseRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;


import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

import net.minecraft.world.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStandGoal;
import net.minecraft.world.entity.ai.goal.RunAroundLikeCrazyGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Ocelot;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.animal.horse.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.ticks.ContainerSingleItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.IntUnaryOperator;

public class BigBeakEntity  extends AbstractHorse implements VariantHolder<BigBeakVariants>
{
    Rabbit ref;
    private static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT;

    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;

    public final AnimationState walkingAnimationState = new AnimationState();
    public int walkingAnimationTimeout = 0;

    public final AnimationState flappingAnimationState = new AnimationState();
    public int flappingAnimationTimeout = 0;

    private static final float MIN_HEALTH_BONUS = generateMaxHealth(max -> 0);
    private static final float MAX_HEALTH_BONUS = generateMaxHealth(max -> max);
    private static final EntityDimensions BABY_BASE_DIMENSIONS;
    public float flapProgress;
    public float maxWingDeviation;
    public float prevMaxWingDeviation;
    public float prevFlapProgress;
    public float flapSpeed = 1.0F;
    private float field_28639 = 1.0F;


    private final Container inventory = new ContainerSingleItem() {
        @Override
        public ItemStack getTheItem() {
            return BigBeakEntity.this.getBodyArmorItem();
        }

        @Override
        public void setTheItem(ItemStack stack) {
            BigBeakEntity.this.setBodyArmorItem(stack);
        }

        @Override
        public void setChanged() {
        }

        @Override
        public boolean stillValid(Player player) {
            return player.getVehicle() == BigBeakEntity.this || player.canInteractWithEntity(
                    BigBeakEntity.this, 4.0);
        }
    };

    public BigBeakEntity(EntityType<? extends BigBeakEntity> entityType, Level world) {
        super(entityType, world);
    }

    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_ID_TYPE_VARIANT, 0);
    }

    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Variant", this.getTypeVariant());
    }

    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setTypeVariant(compound.getInt("Variant"));
    }

    private void setTypeVariant(int typeVariant) {
        this.entityData.set(DATA_ID_TYPE_VARIANT, typeVariant);
    }

    private int getTypeVariant() {
        return (Integer)this.entityData.get(DATA_ID_TYPE_VARIANT);
    }

    private void setVariantAndMarkings(BigBeakVariants variant, Markings marking) {
        this.setTypeVariant(variant.getId() & 255 | marking.getId() << 8 & '\uff00');
    }

    public BigBeakVariants getVariant() {
        return BigBeakVariants.byId(this.getTypeVariant() & 255);
    }

    public void setVariant(BigBeakVariants variant) {
        this.setTypeVariant(variant.getId() & 255 | this.getTypeVariant() & -256);
    }

    public Markings getMarkings() {
        return Markings.byId((this.getTypeVariant() & '\uff00') >> 8);
    }


    public static AttributeSupplier.Builder createBigBeakAttributes() {
        return Mob.createMobAttributes().add(Attributes.JUMP_STRENGTH, 2.0)
                .add(Attributes.MAX_HEALTH, 10.0)
                .add(Attributes.MOVEMENT_SPEED, 0.4)
                .add(Attributes.STEP_HEIGHT, 2.0)
                .add(Attributes.SAFE_FALL_DISTANCE, 100.0)
                .add(Attributes.FALL_DAMAGE_MULTIPLIER, 0.5);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.2));
        this.goalSelector.addGoal(1, new RunAroundLikeCrazyGoal(this, 1.2));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0, BigBeakEntity.class));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.0));
        this.goalSelector.addGoal(4, new TemptGoal(this, 1.2, stack -> {
            return stack.is(JamiesModTag.BIGBEAK_FOOD);
        }, false));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 0.7));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        if (this.canPerformRearing()) {
            this.goalSelector.addGoal(9, new RandomStandGoal(this));
        }

        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, PestEntity.class, true));

        this.addBehaviourGoals();
    }

    @Override
    public void aiStep() {
        this.prevFlapProgress = this.flapProgress;
        this.prevMaxWingDeviation = this.maxWingDeviation;
        this.maxWingDeviation += (this.onGround() ? -1.0F : 4.0F) * 0.3F;
        this.maxWingDeviation = Mth.clamp(this.maxWingDeviation, 0.0F, 1.0F);
        if (!this.onGround() && this.flapSpeed < 1.0F) {
            this.flapSpeed = 1.0F;
            this.flappingAnimationTimeout = this.random.nextInt(100) + 100;
            this.flappingAnimationState.start(this.tickCount);
        }else {
            --this.flappingAnimationTimeout;
        }

        this.flapSpeed *= 0.9F;
        Vec3 vec3d = this.getDeltaMovement();
        if (!this.onGround() && vec3d.y < 0.0) {
            this.setDeltaMovement(vec3d.multiply(1.0, 0.6, 1.0));
        }

        this.flapProgress += this.flapSpeed * 2.0F;

        super.aiStep();
        if (!this.level().isClientSide && this.isAlive()) {
            if (this.random.nextInt(900) == 0 && this.deathTime == 0) {
                this.heal(1.0F);
            }

            if (this.canEatGrass()) {
                if (!this.isEating() && !this.isVehicle() && this.random.nextInt(300) == 0 && this.level().getBlockState(this.blockPosition().below()).is(Blocks.GRASS_BLOCK)) {
                    this.setEating(true);
                }
            }

            this.followMommy();
        }
    }

    protected static float generateMaxHealth(IntUnaryOperator randomIntGetter) {
        return 1.0F + (float)randomIntGetter.applyAsInt(2) + (float)randomIntGetter.applyAsInt(3);
    }


    @Override
    public float getLightLevelDependentMagicValue() {
        return 1.0F;
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
    public void containerChanged(Container sender) {
        ItemStack itemStack = this.getBodyArmorItem();
        super.containerChanged(sender);
        ItemStack itemStack2 = this.getBodyArmorItem();
        if (this.tickCount > 20 && this.isBodyArmorItem(itemStack2) && itemStack != itemStack2) {
            this.playSound(SoundEvents.HORSE_ARMOR, 0.5F, 1.0F);
        }

    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            setupAnimationStates();
        }
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        if (!state.liquid()) {
            BlockState blockState = this.level().getBlockState(pos.above());
            SoundType blockSoundGroup = state.getSoundType();
            if (blockState.is(Blocks.SNOW)) {
                blockSoundGroup = blockState.getSoundType();
            }

            if (this.isVehicle() && this.canGallop) {
                ++this.gallopSoundCounter;
                if (this.gallopSoundCounter > 5 && this.gallopSoundCounter % 3 == 0) {
                    this.playGallopSound(blockSoundGroup);
                } else if (this.gallopSoundCounter <= 5) {
                    this.playSound(BGSoundEvents.BIGBEAK_STEP_WOOD_ADDITIONS_EVENT, blockSoundGroup.getVolume() * 0.15F, blockSoundGroup.getPitch());
                }
            } else if (this.isWoodSoundType(blockSoundGroup)) {
                this.playSound(BGSoundEvents.BIGBEAK_STEP_WOOD_ADDITIONS_EVENT, blockSoundGroup.getVolume() * 0.15F, blockSoundGroup.getPitch());
            } else {
                this.playSound(BGSoundEvents.BIGBEAK_STEP_ADDITIONS_EVENT, blockSoundGroup.getVolume() * 0.15F, blockSoundGroup.getPitch());
            }

        }
    }

    @Override
    protected void playGallopSound(SoundType group) {
        this.playSound(BGSoundEvents.BIGBEAK_GALLOP_ADDITIONS_EVENT, group.getVolume() * 0.15F, group.getPitch());
    }

    private boolean isWoodSoundType(SoundType soundGroup) {
        return soundGroup == SoundType.WOOD || soundGroup == SoundType.NETHER_WOOD || soundGroup == SoundType.STEM || soundGroup == SoundType.CHERRY_WOOD || soundGroup == SoundType.BAMBOO_WOOD;
    }


    @Override
    public boolean removeWhenFarAway(double distanceSquared) {
        return this.isPassenger();
    }


    @Override
    protected SoundEvent getAmbientSound() {
        return BGSoundEvents.BIGBEAK_AMBIENT_ADDITIONS_EVENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return BGSoundEvents.BIGBEAK_DEATH_ADDITIONS_EVENT;
    }

    @Override
    @Nullable
    protected SoundEvent getEatingSound() {
        return SoundEvents.PARROT_EAT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return BGSoundEvents.BIGBEAK_HURT_ADDITIONS_EVENT;
    }
    @Override
    protected void playJumpSound() {
        this.playSound(BGSoundEvents.BIGBEAK_JUMP_ADDITIONS_EVENT, 0.4F, 1.0F);
    }


    public InteractionResult interactBigBeak(Player player, ItemStack stack) {
        boolean bl = this.handleEating(player, stack);
        if (bl) {
            stack.consume(1, player);
        }

        if (this.level().isClientSide) {
            return InteractionResult.CONSUME;
        } else {
            return bl ? InteractionResult.SUCCESS : InteractionResult.PASS;
        }
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(ItemTags.CHICKEN_FOOD);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        boolean bl = !this.isBaby() && this.isTamed() && player.isSecondaryUseActive();
        if (!this.isVehicle() && !bl) {
            ItemStack itemStack = player.getItemInHand(hand);
            if (!itemStack.isEmpty()) {
                if (this.isFood(itemStack)) {
                    return this.interactBigBeak(player, itemStack);
                }

                if (!this.isTamed()) {
                    this.makeMad();
                    return InteractionResult.sidedSuccess(this.level().isClientSide);
                }

                if (this.canUseSlot(EquipmentSlot.BODY) && this.isBodyArmorItem(itemStack) && !this.isWearingBodyArmor()) {
                    this.equipBigBeakArmor(player, itemStack);
                    return InteractionResult.sidedSuccess(this.level().isClientSide);
                }
            }

            return super.mobInteract(player, hand);
        } else {
            return super.mobInteract(player, hand);
        }
    }

    public void equipBigBeakArmor(Player player, ItemStack stack) {
        if (this.isBodyArmorItem(stack)) {
            this.setBodyArmorItem(stack.copyWithCount(1));
            stack.consume(1, player);
        }

    }


    @Override
    public boolean canMate(Animal other) {
        return false;
    }

    @Override
    @javax.annotation.Nullable
    public AgeableMob getBreedOffspring(ServerLevel world, AgeableMob otherParent) {

        BigBeakEntity horse = (BigBeakEntity) otherParent;
        BigBeakEntity horse1 = BGEntityTypes.BIG_BEAK.get().create(world);
        if (horse1 != null) {
            int i = this.random.nextInt(9);
            BigBeakVariants variant;
            if (i < 4) {
                variant = this.getVariant();
            } else if (i < 8) {
                variant = horse.getVariant();
            } else {
                variant = (BigBeakVariants) Util.getRandom(BigBeakVariants.values(), this.random);
            }

            int j = this.random.nextInt(5);
            Markings markings;
            if (j < 2) {
                markings = this.getMarkings();
            } else if (j < 4) {
                markings = horse.getMarkings();
            } else {
                markings = (Markings) Util.getRandom(Markings.values(), this.random);
            }

            horse1.setVariantAndMarkings(variant, markings);
            this.setOffspringAttributes(otherParent, horse1);
        }

        return horse1;

    }

    @Override
    public boolean canUseSlot(EquipmentSlot slot) {
        return true;
    }

    @Override
    public boolean isBodyArmorItem(ItemStack stack) {
        Item var3 = stack.getItem();
        if (var3 instanceof CustomAnimalArmorItem animalArmorItem) {
            return animalArmorItem.getBodyType() == CustomAnimalArmorItem.BodyType.BIG_BEAK;
        }

        return false;
    }

    static double createOffspringAttribute(double parentBase, double otherParentBase, double min, double max, @NotNull RandomSource random) {
        if (max <= min) {
            throw new IllegalArgumentException("Incorrect range for an attribute");
        } else {
            parentBase = Mth.clamp(parentBase, min, max);
            otherParentBase = Mth.clamp(otherParentBase, min, max);
            double d = 0.15 * (max - min);
            double e = Math.abs(parentBase - otherParentBase) + d * 2.0;
            double f = (parentBase + otherParentBase) / 2.0;
            double g = (random.nextDouble() + random.nextDouble() + random.nextDouble()) / 3.0 - 0.5;
            double h = f + e * g;
            double i;
            if (h > max) {
                i = h - max;
                return max - i;
            } else if (h < min) {
                i = min - h;
                return min + i;
            } else {
                return h;
            }
        }
    }

    @Override
    protected void setOffspringAttributes(AgeableMob other, AbstractHorse child) {
        this.setOffspringAttribute(other, child, Attributes.MAX_HEALTH, MIN_HEALTH_BONUS, MAX_HEALTH_BONUS);
    }

    private void setOffspringAttribute(AgeableMob other, AbstractHorse child, Holder<Attribute> attribute, double min, double max) {
        double d = createOffspringAttribute(this.getAttributeBaseValue(attribute), other.getAttributeBaseValue(attribute), min, max, this.random);
        Objects.requireNonNull(child.getAttribute(attribute)).setBaseValue(d);
    }

    protected static boolean isBrightEnoughToSpawn(BlockAndTintGetter world, BlockPos pos) {
        return world.getRawBrightness(pos, 0) > 1;
    }

    private static BigBeakVariants getRandomBigBeakVariant(LevelAccessor level, BlockPos pos) {
        Holder<Biome> holder = level.getBiome(pos);
        int i = level.getRandom().nextInt(100);
        if (holder.is(JamiesModTag.SPAWNS_WARM_BIGBEAKS)) {
            return i < 80 ? BigBeakVariants.SAVANNA : (i < 90 ? BigBeakVariants.NOMAD : BigBeakVariants.PEACHY);
        }
        else {
            return i < 10 ? BigBeakVariants.TRANS: i < 15 ? BigBeakVariants.LESBIAN: i < 20 ? BigBeakVariants.MLM:
                    i < 30 ? BigBeakVariants.FROSTY: i < 40 ? BigBeakVariants.BLUEBILL: (i < 50 ? BigBeakVariants.TROPICAL : BigBeakVariants.NORMAL);
        }
    }


    @javax.annotation.Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @javax.annotation.Nullable SpawnGroupData spawnGroupData) {
        RandomSource randomsource = level.getRandom();
        BigBeakVariants bigbeak$variant = getRandomBigBeakVariant(level, this.blockPosition());
        BigBeakVariants variant;
        if (spawnGroupData instanceof BigBeakEntity.BigBeakGroupData) {
            bigbeak$variant = ((BigBeakEntity.BigBeakGroupData)spawnGroupData).variant;
        } else {
            bigbeak$variant = Util.getRandom(BigBeakVariants.values(), randomsource);
            spawnGroupData = new BigBeakEntity.BigBeakGroupData(bigbeak$variant);
        }

        this.setVariantAndMarkings(bigbeak$variant, Util.getRandom(Markings.values(), randomsource));
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }

    @Override
    public EntityDimensions getDefaultDimensions(Pose pose) {
        return this.isBaby() ? BABY_BASE_DIMENSIONS : super.getDefaultDimensions(pose);
    }

    static {
        DATA_ID_TYPE_VARIANT = SynchedEntityData.defineId(BigBeakEntity.class, EntityDataSerializers.INT);
        BABY_BASE_DIMENSIONS = BGEntityTypes.BIG_BEAK.get().getDimensions().withAttachments(EntityAttachments.builder().attach(EntityAttachment.PASSENGER, 0.0F,
                BGEntityTypes.BIG_BEAK.get().getHeight() + 0.125F, 0.0F)).scale(0.5F);
    }

    public static boolean canSpawn(
            EntityType<? extends Mob> moobloomEntityType,
            LevelAccessor serverWorldAccess,
            MobSpawnType spawnReason,
            BlockPos blockPos,
            RandomSource random
    ) {
        return serverWorldAccess.getBlockState(blockPos.below()).is(Blocks.MOSS_BLOCK)
                || serverWorldAccess.getBlockState(blockPos).is(Blocks.MOSS_CARPET)
                || serverWorldAccess.getBlockState(blockPos).is(BGBlocks.AMBER_SAND.get())
                || serverWorldAccess.getBlockState(blockPos.below()).is(BGBlocks.MOSSY_CLAYSTONE.get());
    }

    public static class BigBeakGroupData extends AgeableMob.AgeableMobGroupData {
        public final BigBeakVariants variant;

        public BigBeakGroupData(BigBeakVariants variant) {
            super(true);
            this.variant = variant;
        }
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader world) {
        return world.isUnobstructed(this);
    }

    private void setupAnimationStates() {
        if (this.idleAnimationTimeout <= 0) {
            this.idleAnimationTimeout = this.random.nextInt(100) + 100;
            this.idleAnimationState.start(this.tickCount);
        } else {
            --this.idleAnimationTimeout;
        }

    }

    @Override
    protected void updateWalkAnimation(float posDelta) {
        float f = this.getPose() == Pose.STANDING ? Math.min(posDelta * 6.0f, 1.0f) : 0.0f;
        this.walkAnimation.update(f, 0.2f);
    }

    public enum State {
        FLAPPING, IDLE
    }
}
