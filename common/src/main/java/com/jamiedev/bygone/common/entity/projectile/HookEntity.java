package com.jamiedev.bygone.common.entity.projectile;

import com.jamiedev.bygone.core.registry.BGEntityTypes;
import com.jamiedev.bygone.core.registry.BGItems;
import com.jamiedev.bygone.core.registry.BGSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class HookEntity extends AbstractArrow {

    private static final EntityDataAccessor<Boolean> DATA_RETRACTING =
            SynchedEntityData.defineId(HookEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Float> DATA_CHAIN_PROGRESS =
            SynchedEntityData.defineId(HookEntity.class, EntityDataSerializers.FLOAT);

    private static final float CHAIN_SPEED = 0.1F;
    private static final float REVERSE_SPEED = 0.055F;

    public float prevChainProgress = 0F;

    private final SoundEvent soundEvent;
    FishingHook ref;
    @javax.annotation.Nullable
    private BlockState lastState;

    public HookEntity(EntityType<? extends HookEntity> entityType, Level pLevel) {
        super(entityType, pLevel);
        this.noCulling = true;
        this.soundEvent = this.getDefaultHitGroundSoundEvent();
    }

    public HookEntity(Level level, Player player) {
        super(BGEntityTypes.HOOK.get(), level);
        setOwner(player);
        setPosRaw(player.getX(), player.getEyeY() - 0.1, player.getZ());
        this.setOldPosAndRot();
        this.soundEvent = this.getDefaultHitGroundSoundEvent();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_RETRACTING, false);
        builder.define(DATA_CHAIN_PROGRESS, 0F);
    }

    public boolean isRetracting() {
        return this.entityData.get(DATA_RETRACTING);
    }

    public void startRetracting() {
        this.entityData.set(DATA_RETRACTING, true);
    }

    public float getChainProgressFloat() {
        return this.entityData.get(DATA_CHAIN_PROGRESS);
    }

    public void setChainProgressFloat(float value) {
        this.entityData.set(DATA_CHAIN_PROGRESS, value);
    }

    public float getChainProgress(float partialTick) {
        return Mth.lerp(partialTick, prevChainProgress, getChainProgressFloat());
    }

    public void bygone$syncOldPos() {
        this.setOldPosAndRot();
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return BGItems.ANCIENT_HOOK.get().getDefaultInstance();
    }

    @Override
    public void setOwner(@Nullable Entity entity) {
        super.setOwner(entity);
        this.pickup = Pickup.DISALLOWED;
    }

    @Nullable
    public Player getPlayerOwner() {
        Entity entity = this.getOwner();
        return entity instanceof Player ? (Player) entity : null;
    }

    @Override
    public void tick() {
        super.tick();

        prevChainProgress = getChainProgressFloat();

        if (this.isRetracting()) {
            float current = getChainProgressFloat();
            setChainProgressFloat(Math.max(0F, current - REVERSE_SPEED));

            if (!this.level().isClientSide && getChainProgressFloat() <= 0F) {
                this.discard();
            }
            return;
        }

        if (getChainProgressFloat() < 1F) {
            setChainProgressFloat(Math.min(1F, getChainProgressFloat() + CHAIN_SPEED));
        }

        Player player = this.getPlayerOwner();
        if (!this.level().isClientSide) {
            boolean inFluid = !this.level().getFluidState(
                    new BlockPos(this.getBlockX(), this.getBlockY(), this.getBlockZ())).isEmpty();

            if (player == null || this.shouldRetract(player) || inFluid || player.isShiftKeyDown()) {
                this.startRetracting();
            }
        }
    }

    private boolean shouldRetract(Player player) {
        return player.isRemoved() || !player.isAlive() || !player.isHolding(BGItems.ANCIENT_HOOK.get()) || this.distanceTo(player) > 64F;
    }

    @Override
    public boolean canUsePortal(boolean allowVehicles) {
        return false;
    }

    @Override
    protected @NotNull SoundEvent getDefaultHitGroundSoundEvent() {
        return BGSoundEvents.HOOK_HIT_ADDITIONS_EVENT;
    }

    @Override
    public boolean isInWall() {
        if (this.noPhysics) {
            return false;
        } else {
            float f = this.getDimensions(this.getPose()).width() * 0.8F;
            AABB box = AABB.ofSize(this.getEyePosition(), f, 1.0E-6, f);
            // this.playSound(BGSoundEvents.HOOK_HIT_ADDITIONS_EVENT, 0.25F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.4F);
            return BlockPos.betweenClosedStream(box).anyMatch((pos) -> {
                BlockState blockState = this.level().getBlockState(pos);
                return !blockState.isAir() && Shapes.joinIsNotEmpty(blockState.getCollisionShape(this.level(), pos)
                        .move(pos.getX(), pos.getY(), pos.getZ()), Shapes.create(box), BooleanOp.AND);
            });
        }
    }
}