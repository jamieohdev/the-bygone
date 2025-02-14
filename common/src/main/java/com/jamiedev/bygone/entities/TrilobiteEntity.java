package com.jamiedev.bygone.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.GlowSquid;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;

public class TrilobiteEntity extends AbstractFish
{
    GlowSquid ref;
    private static final EntityDataAccessor<Integer> DARK_TICKS_REMAINING1 = SynchedEntityData.defineId(TrilobiteEntity.class, EntityDataSerializers.INT);
    public TrilobiteEntity(EntityType<? extends TrilobiteEntity> entityType, Level world) {
        super(entityType, world);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 5.0)
                .add(Attributes.MOVEMENT_SPEED, 0.35);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DARK_TICKS_REMAINING1, 0);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putInt("DarkTicksRemaining", this.getDarkTicksRemaining());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        this.setDarkTicksRemaining(nbt.getInt("DarkTicksRemaining"));
    }

    public static boolean checkSurfaceWaterAnimalSpawnRule(EntityType<? extends WaterAnimal> type, LevelAccessor world, MobSpawnType reason, BlockPos pos, RandomSource random) {
        int i = world.getSeaLevel();
        int j = i - 13;

        return  world.getBlockState(pos).getFluidState().is(FluidTags.WATER)
                    && world.getBlockState(pos.above()).is(Blocks.WATER);
    }



    @Override
    protected SoundEvent getFlopSound() {
        return SoundEvents.TADPOLE_FLOP;
    }

    @Override
    public ItemStack getBucketItemStack() {
        return Items.WATER_BUCKET.getDefaultInstance();
    }

    private void setDarkTicksRemaining(int ticks) {
        this.entityData.set(DARK_TICKS_REMAINING1, ticks);
    }

    public int getDarkTicksRemaining() {
        return this.entityData.get(DARK_TICKS_REMAINING1);
    }


}
