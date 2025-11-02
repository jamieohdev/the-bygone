package com.jamiedev.bygone.common.entity.projectile;

import com.jamiedev.bygone.core.registry.BGEntityTypes;
import com.jamiedev.bygone.core.registry.BGItems;
import com.jamiedev.bygone.core.registry.BGSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
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
        this.soundEvent = this.getDefaultHitGroundSoundEvent();
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return BGItems.HOOK.get().getDefaultInstance();
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
        Player player = this.getPlayerOwner();
        if (!this.level().isClientSide) {
            if ((player == null || this.shouldRetract(player))) {
                this.discard();
            }
            if (!this.level().getFluidState(new BlockPos(this.getBlockX(), this.getBlockY(), this.getBlockZ())).isEmpty()) {
                this.discard();
            }
            if (player != null && player.isShiftKeyDown()) {
                this.discard();
            }
        }
    }

    private boolean shouldRetract(Player player) {
        return player.isRemoved() || !player.isAlive() || !player.isHolding(BGItems.HOOK.get()) || this.distanceTo(player) > 64F;
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
