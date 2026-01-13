package com.jamiedev.bygone.common.entity;

import com.jamiedev.bygone.core.registry.BGBlocks;
import com.jamiedev.bygone.core.registry.BGItems;
import com.jamiedev.bygone.core.registry.BGSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
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
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class MoobooEntity extends Cow {

    public AnimationState idleAnimationState = new AnimationState();
    public MoobooEntity(EntityType<? extends Cow> entityType, Level level) {
        super(entityType, level);
    }

    public static boolean checkAnimalSpawnRules(
            EntityType<? extends Animal> animal, LevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random
    ) {
        boolean flag = MobSpawnType.ignoresLightRequirements(spawnType) || isBrightEnoughToSpawn(level, pos);
        return level.getBlockState(pos.below()).is(BlockTags.ANIMALS_SPAWNABLE_ON) && flag;
    }

    public float getWalkTargetValue(BlockPos pos, LevelReader level) {
        return level.getBlockState(pos.below()).is(BGBlocks.SABLE_MOSS_BLOCK.get()) ? 10.0F : level.getPathfindingCostFromLightLevels(pos);
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
        return 0.4F;
    }

    public int getMaxSpawnClusterSize() {
        return 1;
    }

    public void tick()
    {
        super.tick();

        if (this.level().isClientSide()) {
            this.setupAnimationStates();
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
            ItemStack itemstack1 = ItemUtils.createFilledResult(itemstack, player, BGItems.ECTOPLASM_BUCKET.get().getDefaultInstance());
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

}
