package com.jamiedev.bygone.common.entity;

import com.jamiedev.bygone.common.entity.ai.AvoidBlockGoal;
import com.jamiedev.bygone.core.init.JamiesModTag;
import com.jamiedev.bygone.core.registry.*;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nullable;

public class MoobooEntity extends Cow {

    private static final EntityDimensions BABY_DIMENSIONS;
    public AnimationState idleAnimationState = new AnimationState();
    public MoobooEntity(EntityType<? extends Cow> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, SabeastEntity.class, 16.0F, (double)1.0F,
                1.75));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, HauntEntity.class, 16.0F, (double)1.0F,
                1.5));
        this.goalSelector.addGoal(3, new AvoidBlockGoal(this, 16, 1.4, 1.6, (pos) -> {
            BlockState state = this.level().getBlockState(pos);
            return state.is(BGBlocks.LITHOPLASMIC_POWDER.get());
        }));
        this.goalSelector.addGoal(3, new AvoidBlockGoal(this, 16, 1.4, 1.6, (pos) -> {
            BlockState state = this.level().getBlockState(pos);
            return state.is(JamiesModTag.HURT_SPECTRAL_BLOCKS);
        }));
    }

    public static boolean checkAnimalSpawnRules(
            EntityType<? extends Animal> animal, LevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random
    ) {
        boolean flag = MobSpawnType.ignoresLightRequirements(spawnType) || isBrightEnoughToSpawn(level, pos);
        return level.getBlockState(pos.below()).is(BlockTags.ANIMALS_SPAWNABLE_ON) && flag;
    }

    public float getWalkTargetValue(BlockPos pos, LevelReader level) {
        return level.getBlockState(pos.below()).is(BGBlocks.SABLE_MOSS_BLOCK.get()) ? 10.0F
                : level.getPathfindingCostFromLightLevels(pos);
    }

    @Override
    public boolean canBeAffected(MobEffectInstance potioneffect) {
        return !(potioneffect.is(MobEffects.POISON) || potioneffect.is(MobEffects.HARM)|| potioneffect.is(MobEffects.WITHER)) && super.canBeAffected(potioneffect);
    }

    protected SoundEvent getAmbientSound() {
        return BGSoundEvents.MOOBOO_AMBIENT_ADDITIONS_EVENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return BGSoundEvents.MOOBOO_HURT_ADDITIONS_EVENT;
    }

    protected SoundEvent getDeathSound() {
        return BGSoundEvents.MOOBOO_DEATH_ADDITIONS_EVENT;
    }

    protected void playStepSound(BlockPos pos, BlockState block) {
        this.playSound(SoundEvents.COW_STEP, 0.15F, 1.0F);
    }

    protected float getSoundVolume() {
        return 0.35F;
    }

    public int getMaxSpawnClusterSize() {
        return 1;
    }

    private boolean collidingSpectralBlocks() {
        AABB aabb = this.getBoundingBox().inflate(1.0F, 1.0F, 1.0F);
        return BlockPos.betweenClosedStream(aabb).anyMatch((collisionShape) -> {
            BlockState blockstate = this.level().getBlockState(collisionShape);
            return blockstate.is(JamiesModTag.SPECTRAL_BLOCKS);
        });
    }

    @Nullable
    public MoobooEntity getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        return BGEntityTypes.MOOBOO.get().create(level);
    }
    private boolean collidingHurtSpectralBlocks() {
        AABB aabb = this.getBoundingBox().inflate(1.0F, 1.0F, 1.0F);
        return BlockPos.betweenClosedStream(aabb).anyMatch((collisionShape) -> {
            BlockState blockstate = this.level().getBlockState(collisionShape);
            return blockstate.is(JamiesModTag.HURT_SPECTRAL_BLOCKS);
        });
    }

    public void tick()
    {
        super.tick();

        if (this.level().isClientSide()) {
            this.setupAnimationStates();
        }
        if  (collidingHurtSpectralBlocks())
        {
            this.hurt(BGDamageTypes.source(this.level(), BGDamageTypes.HAUNTED, this, this.getLastAttacker()), 1);
 
        }
    }

    @Override
    public float getLightLevelDependentMagicValue() {
        return 1.0F;
    }

    protected static boolean isBrightEnoughToSpawn(BlockAndTintGetter level, BlockPos pos) {
        return level.getRawBrightness(pos, 0) > 1;
    }

    private void setupAnimationStates() {
        this.idleAnimationState.startIfStopped(this.tickCount);
    }

    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (itemstack.is(Items.BUCKET) && !this.isBaby()) {
            player.playSound(BGSoundEvents.MOOBOO_MILK_ADDITIONS_EVENT, 1.0F, 1.0F);
            ItemStack itemstack1 = ItemUtils.createFilledResult(itemstack, player, BGItems.PLASMILK_BUCKET.get().getDefaultInstance());
            player.setItemInHand(hand, itemstack1);
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        } else {
            return super.mobInteract(player, hand);
        }
    }

    public static boolean canSpawn(
            EntityType<? extends Mob> type,
            LevelAccessor serverWorldAccess,
            MobSpawnType spawnReason,
            BlockPos blockPos,
            RandomSource random
    ) {
        return true;
    }

    public EntityDimensions getDefaultDimensions(Pose pose) {
        return this.isBaby() ? BABY_DIMENSIONS : super.getDefaultDimensions(pose);
    }

    static {
        BABY_DIMENSIONS = BGEntityTypes.MOOBOO.get().getDimensions().scale(0.5F).withEyeHeight(0.665F);
    }
}
