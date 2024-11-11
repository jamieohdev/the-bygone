package com.jamiedev.mod.common.entities;

import com.jamiedev.mod.fabric.JamiesModFabric;
import com.jamiedev.mod.fabric.init.JamiesModBlocks;
import com.jamiedev.mod.fabric.init.JamiesModEntityTypes;
import com.jamiedev.mod.fabric.init.JamiesModSoundEvents;
import com.jamiedev.mod.fabric.init.JamiesModTag;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SingleStackInventory;
import net.minecraft.item.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.*;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.IntUnaryOperator;

public class BigBeakEntity  extends AbstractHorseEntity
{

    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;

    public final AnimationState walkingAnimationState = new AnimationState();
    public int walkingAnimationTimeout = 0;

    public final AnimationState flappingAnimationState = new AnimationState();
    public int flappingAnimationTimeout = 0;

    HorseEntity ref;
    PigEntity ref3;
    private static final float MIN_HEALTH_BONUS = getChildHealthBonus((max) -> {
        return 0;
    });
    private static final float MAX_HEALTH_BONUS = getChildHealthBonus((max) -> {
        return max;
    });
    private static final EntityDimensions BABY_BASE_DIMENSIONS;
    public float flapProgress;
    public float maxWingDeviation;
    public float prevMaxWingDeviation;
    public float prevFlapProgress;
    public float flapSpeed = 1.0F;
    private float field_28639 = 1.0F;

    private final Inventory inventory = new SingleStackInventory() {
        public ItemStack getStack() {
            return BigBeakEntity.this.getBodyArmor();
        }

        public void setStack(ItemStack stack) {
            BigBeakEntity.this.equipBodyArmor(stack);
        }

        public void markDirty() {
        }

        public boolean canPlayerUse(PlayerEntity player) {
            return player.getVehicle() == BigBeakEntity.this || player.canInteractWithEntity(
                    BigBeakEntity.this, 4.0);
        }
    };

    public BigBeakEntity(EntityType<? extends BigBeakEntity> entityType, World world) {
        super(entityType, world);
    }

    protected void initAttributes(Random random) {
        EntityAttributeInstance var10000 = this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
        Objects.requireNonNull(random);
        assert var10000 != null;
        var10000.setBaseValue((double)getChildHealthBonus(random::nextInt));
        var10000 = this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
        Objects.requireNonNull(random);
        assert var10000 != null;
        var10000.setBaseValue(getChildMovementSpeedBonus(random::nextDouble));
        var10000 = this.getAttributeInstance(EntityAttributes.GENERIC_JUMP_STRENGTH);
        Objects.requireNonNull(random);
        assert var10000 != null;
        var10000.setBaseValue(getChildJumpStrengthBonus(random::nextDouble));
    }


    public static DefaultAttributeContainer.Builder createBigBeakAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_JUMP_STRENGTH, 2.0)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 50.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.4)
                .add(EntityAttributes.GENERIC_STEP_HEIGHT, 2.0)
                .add(EntityAttributes.GENERIC_SAFE_FALL_DISTANCE, 100.0)
                .add(EntityAttributes.GENERIC_FALL_DAMAGE_MULTIPLIER, 0.5);
    }

    protected void initGoals() {
        this.goalSelector.add(1, new EscapeDangerGoal(this, 1.2));
        this.goalSelector.add(1, new HorseBondWithPlayerGoal(this, 1.2));
        this.goalSelector.add(2, new AnimalMateGoal(this, 1.0, BigBeakEntity.class));
        this.goalSelector.add(4, new FollowParentGoal(this, 1.0));
        this.goalSelector.add(4, new TemptGoal(this, 1.2, (stack) -> {
            return stack.isIn(JamiesModTag.BIGBEAK_FOOD);
        }, false));
        this.goalSelector.add(6, new WanderAroundFarGoal(this, 0.7));
        this.goalSelector.add(7, new LookAtEntityGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.add(8, new LookAroundGoal(this));
        if (this.shouldAmbientStand()) {
            this.goalSelector.add(9, new AmbientStandGoal(this));
        }

        this.initCustomGoals();
    }

    public void tickMovement() {
        this.prevFlapProgress = this.flapProgress;
        this.prevMaxWingDeviation = this.maxWingDeviation;
        this.maxWingDeviation += (this.isOnGround() ? -1.0F : 4.0F) * 0.3F;
        this.maxWingDeviation = MathHelper.clamp(this.maxWingDeviation, 0.0F, 1.0F);
        if (!this.isOnGround() && this.flapSpeed < 1.0F) {
            this.flapSpeed = 1.0F;
            this.flappingAnimationTimeout = this.random.nextInt(100) + 100;
            this.flappingAnimationState.start(this.age);
        }else {
            --this.flappingAnimationTimeout;
        }

        this.flapSpeed *= 0.9F;
        Vec3d vec3d = this.getVelocity();
        if (!this.isOnGround() && vec3d.y < 0.0) {
            this.setVelocity(vec3d.multiply(1.0, 0.6, 1.0));
        }

        this.flapProgress += this.flapSpeed * 2.0F;

        super.tickMovement();
        if (!this.getWorld().isClient && this.isAlive()) {
            if (this.random.nextInt(900) == 0 && this.deathTime == 0) {
                this.heal(1.0F);
            }

            if (this.eatsGrass()) {
                if (!this.isEatingGrass() && !this.hasPassengers() && this.random.nextInt(300) == 0 && this.getWorld().getBlockState(this.getBlockPos().down()).isOf(Blocks.GRASS_BLOCK)) {
                    this.setEatingGrass(true);
                }
            }

            this.walkToParent();
        }
    }

    protected static float getChildHealthBonus(IntUnaryOperator randomIntGetter) {
        return 5.0F + (float)randomIntGetter.applyAsInt(2) + (float)randomIntGetter.applyAsInt(3);
    }


    public float getBrightnessAtEyes() {
        return 1.0F;
    }

    protected boolean isFlappingWings() {
        return this.speed > this.field_28639;
    }

    protected void addFlapEffects() {
        this.field_28639 = this.speed + this.maxWingDeviation / 2.0F;
    }


    public void onInventoryChanged(Inventory sender) {
        ItemStack itemStack = this.getBodyArmor();
        super.onInventoryChanged(sender);
        ItemStack itemStack2 = this.getBodyArmor();
        if (this.age > 20 && this.isHorseArmor(itemStack2) && itemStack != itemStack2) {
            this.playSound(SoundEvents.ENTITY_HORSE_ARMOR, 0.5F, 1.0F);
        }

    }

    @Override
    public void tick() {
        super.tick();
        if (this.getWorld().isClient) {
            setupAnimationStates();
        }
    }

    protected void playStepSound(BlockPos pos, BlockState state) {
        if (!state.isLiquid()) {
            BlockState blockState = this.getWorld().getBlockState(pos.up());
            BlockSoundGroup blockSoundGroup = state.getSoundGroup();
            if (blockState.isOf(Blocks.SNOW)) {
                blockSoundGroup = blockState.getSoundGroup();
            }

            if (this.hasPassengers() && this.playExtraHorseSounds) {
                ++this.soundTicks;
                if (this.soundTicks > 5 && this.soundTicks % 3 == 0) {
                    this.playWalkSound(blockSoundGroup);
                } else if (this.soundTicks <= 5) {
                    this.playSound(JamiesModSoundEvents.BIGBEAK_STEP_WOOD_ADDITIONS_EVENT, blockSoundGroup.getVolume() * 0.15F, blockSoundGroup.getPitch());
                }
            } else if (this.isWooden(blockSoundGroup)) {
                this.playSound(JamiesModSoundEvents.BIGBEAK_STEP_WOOD_ADDITIONS_EVENT, blockSoundGroup.getVolume() * 0.15F, blockSoundGroup.getPitch());
            } else {
                this.playSound(JamiesModSoundEvents.BIGBEAK_STEP_ADDITIONS_EVENT, blockSoundGroup.getVolume() * 0.15F, blockSoundGroup.getPitch());
            }

        }
    }

    @Override
    protected void playWalkSound(BlockSoundGroup group) {
        this.playSound(JamiesModSoundEvents.BIGBEAK_GALLOP_ADDITIONS_EVENT, group.getVolume() * 0.15F, group.getPitch());
    }

    private boolean isWooden(BlockSoundGroup soundGroup) {
        return soundGroup == BlockSoundGroup.WOOD || soundGroup == BlockSoundGroup.NETHER_WOOD || soundGroup == BlockSoundGroup.NETHER_STEM || soundGroup == BlockSoundGroup.CHERRY_WOOD || soundGroup == BlockSoundGroup.BAMBOO_WOOD;
    }


    public boolean canImmediatelyDespawn(double distanceSquared) {
        return this.hasVehicle();
    }


    protected SoundEvent getAmbientSound() {
        return JamiesModSoundEvents.BIGBEAK_AMBIENT_ADDITIONS_EVENT;
    }

    protected SoundEvent getDeathSound() {
        return JamiesModSoundEvents.BIGBEAK_DEATH_ADDITIONS_EVENT;
    }

    @Nullable
    protected SoundEvent getEatSound() {
        return SoundEvents.ENTITY_PARROT_EAT;
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return JamiesModSoundEvents.BIGBEAK_HURT_ADDITIONS_EVENT;
    }
    @Override
    protected void playJumpSound() {
        this.playSound(JamiesModSoundEvents.BIGBEAK_JUMP_ADDITIONS_EVENT, 0.4F, 1.0F);
    }


    public ActionResult interactBigBeak(PlayerEntity player, ItemStack stack) {
        boolean bl = this.receiveFood(player, stack);
        if (bl) {
            stack.decrementUnlessCreative(1, player);
        }

        if (this.getWorld().isClient) {
            return ActionResult.CONSUME;
        } else {
            return bl ? ActionResult.SUCCESS : ActionResult.PASS;
        }
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.isIn(ItemTags.CHICKEN_FOOD);
    }

    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        boolean bl = !this.isBaby() && this.isTame() && player.shouldCancelInteraction();
        if (!this.hasPassengers() && !bl) {
            ItemStack itemStack = player.getStackInHand(hand);
            if (!itemStack.isEmpty()) {
                if (this.isBreedingItem(itemStack)) {
                    return this.interactBigBeak(player, itemStack);
                }

                if (!this.isTame()) {
                    this.playAngrySound();
                    return ActionResult.success(this.getWorld().isClient);
                }

                if (this.canUseSlot(EquipmentSlot.BODY) && this.isHorseArmor(itemStack) && !this.isWearingBodyArmor()) {
                    this.equipBigBeakArmor(player, itemStack);
                    return ActionResult.success(this.getWorld().isClient);
                }
            }

            return super.interactMob(player, hand);
        } else {
            return super.interactMob(player, hand);
        }
    }

    public void equipBigBeakArmor(PlayerEntity player, ItemStack stack) {
        if (this.isHorseArmor(stack)) {
            this.equipBodyArmor(stack.copyWithCount(1));
            stack.decrementUnlessCreative(1, player);
        }

    }


    public boolean canBreedWith(AnimalEntity other) {
        return false;
    }

    @Nullable
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {

            BigBeakEntity BigBeakEntity = (BigBeakEntity)entity;
            BigBeakEntity BigBeakEntity2 = (BigBeakEntity)JamiesModEntityTypes.BIG_BEAK.create(world);
            if (BigBeakEntity2 != null) {
                int i = this.random.nextInt(9);
                this.setChildAttributes(entity, BigBeakEntity2);
            }

            return BigBeakEntity2;

    }

    public boolean canUseSlot(EquipmentSlot slot) {
        return true;
    }

    @Override
    public boolean isHorseArmor(ItemStack stack) {
        Item var3 = stack.getItem();
        boolean var10000;
        if (var3 instanceof AnimalArmorItem animalArmorItem) {
            if (animalArmorItem.getType() == JamiesModFabric.BIG_BEAK_ARMOR) {
                var10000 = true;
                return var10000;
            }
        }

        var10000 = false;
        return var10000;
    }

    static double calculateAttributeBaseValue(double parentBase, double otherParentBase, double min, double max, Random random) {
        if (max <= min) {
            throw new IllegalArgumentException("Incorrect range for an attribute");
        } else {
            parentBase = MathHelper.clamp(parentBase, min, max);
            otherParentBase = MathHelper.clamp(otherParentBase, min, max);
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


    protected void setChildAttributes(PassiveEntity other, AbstractHorseEntity child) {
        this.setChildAttribute(other, child, EntityAttributes.GENERIC_MAX_HEALTH, (double)MIN_HEALTH_BONUS, (double)MAX_HEALTH_BONUS);
    }

    private void setChildAttribute(PassiveEntity other, AbstractHorseEntity child, RegistryEntry<EntityAttribute> attribute, double min, double max) {
        double d = calculateAttributeBaseValue(this.getAttributeBaseValue(attribute), other.getAttributeBaseValue(attribute), min, max, this.random);
        Objects.requireNonNull(child.getAttributeInstance(attribute)).setBaseValue(d);
    }

    public static boolean isValidNaturalSpawn(EntityType<? extends AnimalEntity> type, WorldAccess serverWorldAccess, SpawnReason spawnReason, BlockPos blockPos, Random random) {
        boolean bl = SpawnReason.isTrialSpawner(spawnReason) || isLightLevelValidForNaturalSpawn(serverWorldAccess, blockPos);
        return serverWorldAccess.getBlockState(blockPos.down()).isOf(Blocks.MOSS_BLOCK) || serverWorldAccess.getBlockState(blockPos.down()).isOf(JamiesModBlocks.MOSSY_CLAYSTONE) && bl;
    }

    protected static boolean isLightLevelValidForNaturalSpawn(BlockRenderView world, BlockPos pos) {
        return world.getBaseLightLevel(pos, 0) > 1;
    }

    @Nullable
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {
        Random random = world.getRandom();

        return super.initialize(world, difficulty, spawnReason, (EntityData)entityData);
    }

    public EntityDimensions getBaseDimensions(EntityPose pose) {
        return this.isBaby() ? BABY_BASE_DIMENSIONS : super.getBaseDimensions(pose);
    }

    static {
        BABY_BASE_DIMENSIONS = JamiesModEntityTypes.BIG_BEAK.getDimensions().withAttachments(EntityAttachments.builder().add(EntityAttachmentType.PASSENGER, 0.0F,
                JamiesModEntityTypes.BIG_BEAK.getHeight() + 0.125F, 0.0F)).scaled(0.5F);
    }
    public static boolean canSpawn(
            EntityType<BigBeakEntity> moobloomEntityType,
            WorldAccess serverWorldAccess,
            SpawnReason spawnReason,
            BlockPos blockPos,
            Random random
    ) {
        return serverWorldAccess.getBlockState(blockPos.down()).isOf(Blocks.MOSS_BLOCK) ||
                serverWorldAccess.getBlockState(blockPos.down()).isOf(JamiesModBlocks.MOSSY_CLAYSTONE);
    }

    public boolean canSpawn(WorldView world) {
        return world.doesNotIntersectEntities(this);
    }

    private void setupAnimationStates() {
        if (this.idleAnimationTimeout <= 0) {
            this.idleAnimationTimeout = this.random.nextInt(100) + 100;
            this.idleAnimationState.start(this.age);
        } else {
            --this.idleAnimationTimeout;
        }

    }

    @Override
    protected void updateLimbs(float posDelta) {
        float f = this.getPose() == EntityPose.STANDING ? Math.min(posDelta * 6.0f, 1.0f) : 0.0f;
        this.limbAnimator.updateLimbs(f, 0.2f);
    }




    public enum State {
        FLAPPING, IDLE;
    }
}
