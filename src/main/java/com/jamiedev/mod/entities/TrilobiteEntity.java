package com.jamiedev.mod.entities;

import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.entity.passive.FishEntity;
import net.minecraft.entity.passive.GlowSquidEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public class TrilobiteEntity extends FishEntity
{
    GlowSquidEntity ref;
    private static final TrackedData<Integer> DARK_TICKS_REMAINING1;
    public TrilobiteEntity(EntityType<? extends TrilobiteEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 5.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.35);
    }

    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(DARK_TICKS_REMAINING1, 0);
    }

    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("DarkTicksRemaining", this.getDarkTicksRemaining());
    }

    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.setDarkTicksRemaining(nbt.getInt("DarkTicksRemaining"));
    }

    public static boolean canSpawn(EntityType<? extends WaterCreatureEntity> type, WorldAccess world, SpawnReason reason, BlockPos pos, Random random) {
        int i = world.getSeaLevel();
        int j = i - 13;

        return  world.getBlockState(pos).getFluidState().isIn(FluidTags.WATER)
                    && world.getBlockState(pos.up()).isOf(Blocks.WATER);
    }



    @Override
    protected SoundEvent getFlopSound() {
        return SoundEvents.ENTITY_TADPOLE_FLOP;
    }

    @Override
    public ItemStack getBucketItem() {
        return Items.WATER_BUCKET.getDefaultStack();
    }

    private void setDarkTicksRemaining(int ticks) {
        this.dataTracker.set(DARK_TICKS_REMAINING1, ticks);
    }

    public int getDarkTicksRemaining() {
        return (Integer)this.dataTracker.get(DARK_TICKS_REMAINING1);
    }



    static {
        DARK_TICKS_REMAINING1 = DataTracker.registerData(TrilobiteEntity.class, TrackedDataHandlerRegistry.INTEGER);
    }
}
