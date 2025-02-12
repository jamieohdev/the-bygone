package com.jamiedev.bygone.entities;

import com.jamiedev.bygone.init.JamiesModBlocks;
import com.jamiedev.bygone.init.JamiesModEntityTypes;
import com.jamiedev.bygone.init.JamiesModSoundEvents;
import com.jamiedev.bygone.init.JamiesModTag;
import com.jamiedev.bygone.items.CustomAnimalArmorItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;


import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

import net.minecraft.world.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityAttachment;
import net.minecraft.world.entity.EntityAttachments;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
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
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AnimalArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.ticks.ContainerSingleItem;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.IntUnaryOperator;

public class BigBeakEntity  extends AbstractHorse
{

    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;

    public final AnimationState walkingAnimationState = new AnimationState();
    public int walkingAnimationTimeout = 0;

    public final AnimationState flappingAnimationState = new AnimationState();
    public int flappingAnimationTimeout = 0;

    Horse ref;
    Pig ref3;
    private static final float MIN_HEALTH_BONUS = generateMaxHealth((max) -> {
        return 0;
    });
    private static final float MAX_HEALTH_BONUS = generateMaxHealth((max) -> {
        return max;
    });
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

    protected void randomizeReinforcementsChance(RandomSource random) {
        AttributeInstance var10000 = this.getAttribute(Attributes.MAX_HEALTH);
        Objects.requireNonNull(random);
        assert var10000 != null;
        var10000.setBaseValue(generateMaxHealth(random::nextInt));
        var10000 = this.getAttribute(Attributes.MOVEMENT_SPEED);
        Objects.requireNonNull(random);
        assert var10000 != null;
        var10000.setBaseValue(generateSpeed(random::nextDouble));
        var10000 = this.getAttribute(Attributes.JUMP_STRENGTH);
        Objects.requireNonNull(random);
        assert var10000 != null;
        var10000.setBaseValue(generateJumpStrength(random::nextDouble));
    }


    public static AttributeSupplier.Builder createBigBeakAttributes() {
        return Mob.createMobAttributes().add(Attributes.JUMP_STRENGTH, 2.0)
                .add(Attributes.MAX_HEALTH, 50.0)
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
        this.goalSelector.addGoal(4, new TemptGoal(this, 1.2, (stack) -> {
            return stack.is(JamiesModTag.BIGBEAK_FOOD);
        }, false));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 0.7));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        if (this.canPerformRearing()) {
            this.goalSelector.addGoal(9, new RandomStandGoal(this));
        }

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
        return 5.0F + (float)randomIntGetter.applyAsInt(2) + (float)randomIntGetter.applyAsInt(3);
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
                    this.playSound(JamiesModSoundEvents.BIGBEAK_STEP_WOOD_ADDITIONS_EVENT, blockSoundGroup.getVolume() * 0.15F, blockSoundGroup.getPitch());
                }
            } else if (this.isWoodSoundType(blockSoundGroup)) {
                this.playSound(JamiesModSoundEvents.BIGBEAK_STEP_WOOD_ADDITIONS_EVENT, blockSoundGroup.getVolume() * 0.15F, blockSoundGroup.getPitch());
            } else {
                this.playSound(JamiesModSoundEvents.BIGBEAK_STEP_ADDITIONS_EVENT, blockSoundGroup.getVolume() * 0.15F, blockSoundGroup.getPitch());
            }

        }
    }

    @Override
    protected void playGallopSound(SoundType group) {
        this.playSound(JamiesModSoundEvents.BIGBEAK_GALLOP_ADDITIONS_EVENT, group.getVolume() * 0.15F, group.getPitch());
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
        return JamiesModSoundEvents.BIGBEAK_AMBIENT_ADDITIONS_EVENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return JamiesModSoundEvents.BIGBEAK_DEATH_ADDITIONS_EVENT;
    }

    @Override
    @Nullable
    protected SoundEvent getEatingSound() {
        return SoundEvents.PARROT_EAT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return JamiesModSoundEvents.BIGBEAK_HURT_ADDITIONS_EVENT;
    }
    @Override
    protected void playJumpSound() {
        this.playSound(JamiesModSoundEvents.BIGBEAK_JUMP_ADDITIONS_EVENT, 0.4F, 1.0F);
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
    @Nullable
    public AgeableMob getBreedOffspring(ServerLevel world, AgeableMob entity) {

            BigBeakEntity BigBeakEntity = (BigBeakEntity)entity;
            BigBeakEntity BigBeakEntity2 = JamiesModEntityTypes.BIG_BEAK.create(world);
            if (BigBeakEntity2 != null) {
                int i = this.random.nextInt(9);
                this.setOffspringAttributes(entity, BigBeakEntity2);
            }

            return BigBeakEntity2;

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

    static double createOffspringAttribute(double parentBase, double otherParentBase, double min, double max, RandomSource random) {
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

    public static boolean checkAnimalSpawnRules(EntityType<? extends Animal> type, LevelAccessor serverWorldAccess, MobSpawnType spawnReason, BlockPos blockPos, RandomSource random) {
        boolean bl = MobSpawnType.ignoresLightRequirements(spawnReason) || isBrightEnoughToSpawn(serverWorldAccess, blockPos);
        return serverWorldAccess.getBlockState(blockPos.below()).is(Blocks.MOSS_BLOCK) || serverWorldAccess.getBlockState(blockPos.below()).is(JamiesModBlocks.MOSSY_CLAYSTONE) && bl;
    }

    protected static boolean isBrightEnoughToSpawn(BlockAndTintGetter world, BlockPos pos) {
        return world.getRawBrightness(pos, 0) > 1;
    }

    @Override
    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType spawnReason, @Nullable SpawnGroupData entityData) {
        RandomSource random = world.getRandom();

        return super.finalizeSpawn(world, difficulty, spawnReason, entityData);
    }

    @Override
    public EntityDimensions getDefaultDimensions(Pose pose) {
        return this.isBaby() ? BABY_BASE_DIMENSIONS : super.getDefaultDimensions(pose);
    }

    static {
        BABY_BASE_DIMENSIONS = JamiesModEntityTypes.BIG_BEAK.getDimensions().withAttachments(EntityAttachments.builder().attach(EntityAttachment.PASSENGER, 0.0F,
                JamiesModEntityTypes.BIG_BEAK.getHeight() + 0.125F, 0.0F)).scale(0.5F);
    }
    public static boolean canSpawn(
            EntityType<BigBeakEntity> moobloomEntityType,
            LevelAccessor serverWorldAccess,
            MobSpawnType spawnReason,
            BlockPos blockPos,
            RandomSource random
    ) {
        return serverWorldAccess.getBlockState(blockPos.below()).is(Blocks.MOSS_BLOCK) ||
                serverWorldAccess.getBlockState(blockPos.below()).is(JamiesModBlocks.MOSSY_CLAYSTONE);
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
