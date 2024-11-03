package com.jamiedev.mod.entities;

import com.jamiedev.mod.init.JamiesModBlocks;
import com.jamiedev.mod.init.JamiesModEntityTypes;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.AnimalArmorItem;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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

public class BigBeakEntity  extends AbstractHorseEntity
{

    HorseEntity ref;

    private static final EntityDimensions BABY_BASE_DIMENSIONS;
    public float flapProgress;
    public float maxWingDeviation;
    public float prevMaxWingDeviation;
    public float prevFlapProgress;
    public float flapSpeed = 1.0F;
    private float field_28639 = 1.0F;

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
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 10.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.4)
                .add(EntityAttributes.GENERIC_STEP_HEIGHT, 2.0)
                .add(EntityAttributes.GENERIC_SAFE_FALL_DISTANCE, 100.0)
                .add(EntityAttributes.GENERIC_FALL_DAMAGE_MULTIPLIER, 0.5);
    }


    public void tickMovement() {
        this.prevFlapProgress = this.flapProgress;
        this.prevMaxWingDeviation = this.maxWingDeviation;
        this.maxWingDeviation += (this.isOnGround() ? -1.0F : 4.0F) * 0.3F;
        this.maxWingDeviation = MathHelper.clamp(this.maxWingDeviation, 0.0F, 1.0F);
        if (!this.isOnGround() && this.flapSpeed < 1.0F) {
            this.flapSpeed = 1.0F;
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
        if (this.age > 20 && this.isBigBeakArmor(itemStack2) && itemStack != itemStack2) {
            this.playSound(SoundEvents.ENTITY_HORSE_ARMOR, 0.5F, 1.0F);
        }

    }

    protected void playWalkSound(BlockSoundGroup group) {
        super.playWalkSound(group);
        if (this.random.nextInt(10) == 0) {
        }

    }

    public boolean canImmediatelyDespawn(double distanceSquared) {
        return this.hasVehicle();
    }


    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_PARROT_AMBIENT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_PARROT_DEATH;
    }

    @Nullable
    protected SoundEvent getEatSound() {
        return SoundEvents.ENTITY_PARROT_EAT;
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_PARROT_HURT;
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
            }

            return super.interactMob(player, hand);
        } else {
            return super.interactMob(player, hand);
        }
    }

    public boolean canBreedWith(AnimalEntity other) {
        if (other == this) {
            return false;
        } else if (!(other instanceof DonkeyEntity) && !(other instanceof BigBeakEntity)) {
            return false;
        } else {
            return this.canBreed();
        }
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

    public boolean isBigBeakArmor(ItemStack stack) {
        Item var3 = stack.getItem();
        boolean var10000;
        if (var3 instanceof ArmorItem animalArmorItem) {
            if (animalArmorItem.getType() == ArmorItem.Type.BODY) {
                var10000 = true;
                return var10000;
            }
        }

        var10000 = false;
        return var10000;
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
        BABY_BASE_DIMENSIONS = EntityType.HORSE.getDimensions().withAttachments(EntityAttachments.builder().add(EntityAttachmentType.PASSENGER, 0.0F,
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
}
