package com.jamiedev.bygone.common.entity.projectile;

import com.jamiedev.bygone.common.util.PlayerWithHook;
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
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class HookEntity extends AbstractArrow {
    private static final EntityDataAccessor<Boolean> DATA_RETRACTING =
            SynchedEntityData.defineId(HookEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Float> DATA_INITIAL_EQUILIBRIUM =
            SynchedEntityData.defineId(HookEntity.class, EntityDataSerializers.FLOAT);

    private static final float CHAIN_SPEED = 0.1F;
    private static final float REVERSE_SPEED = 0.055F;

    // all of these are only relevant clientside
    private float chainProgress = 0F;
    private float prevChainProgress = 0F;
    private float equilibrium = -1F;
    private float pullspeed = 0F;
    private boolean pulledThisTick = false;

    public HookEntity(EntityType<? extends HookEntity> entityType, Level level) {
        super(entityType, level);
        this.noCulling = true;
    }
    public HookEntity(Level level, Player player) {
        this(BGEntityTypes.HOOK.get(), level);
        this.setOwner(player);
        this.setPos(player.getX(), player.getEyeY() - 0.1F, player.getZ());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_RETRACTING, false);
        builder.define(DATA_INITIAL_EQUILIBRIUM, -1F);
    }

    public boolean isRetracting(){ return this.entityData.get(DATA_RETRACTING); }
    public void startRetracting(){ this.entityData.set(DATA_RETRACTING, true); }
    public float getInitialEquilibrium(){ return this.entityData.get(DATA_INITIAL_EQUILIBRIUM); }
    public void setInitialEquilibrium(float dist){ this.entityData.set(DATA_INITIAL_EQUILIBRIUM, dist); }

    public boolean isAttached(){ return this.inGround; }
    public float getEquilibrium(){ return this.equilibrium < 0F ? this.getInitialEquilibrium() : this.equilibrium; }
    public float getChainProgress(float partialTick){ return Mth.lerp(partialTick, prevChainProgress, chainProgress); }

    @Nullable
    public Player getPlayerOwner() {
        Entity entity = this.getOwner();
        return entity instanceof Player ? (Player) entity : null;
    }
    public void pull(float accel){
        if(this.getEquilibrium() < 0F) return;
        this.equilibrium = Math.max(this.getEquilibrium() - (this.pullspeed += accel), 0F);
        this.pulledThisTick = true;
    }



    @Override
    public void tick() {
        super.tick();

        this.prevChainProgress = this.chainProgress;
        if (this.isRetracting()) this.chainProgress = Math.max(0F, this.chainProgress - REVERSE_SPEED);
        else this.chainProgress = Math.min(1F, this.chainProgress + CHAIN_SPEED);

        if(!this.pulledThisTick) this.pullspeed = 0F;
        else this.pulledThisTick = false;

        if(!this.level().isClientSide){
            Player player = this.getPlayerOwner();
            if(this.chainProgress == 0){
                PlayerWithHook hooked = (PlayerWithHook)player;
                if(player != null && hooked.bygone$getHook() == this) hooked.bygone$setHook(null);
                this.discard();
                return;
            }

            if(player != null && this.isAttached() && this.getInitialEquilibrium() < 0) this.setInitialEquilibrium(this.distanceTo(player));

            if(!this.isRetracting()){
                if(player == null || this.shouldRetract(player) || !this.level().getFluidState(this.blockPosition()).isEmpty()) this.startRetracting();
            }
        }
    }

    private boolean shouldRetract(Player player) {
        return player.isRemoved()
            || !player.isAlive()
            || player.isSpectator()
            || !player.isHolding(BGItems.ANCIENT_HOOK.get())
            || ((PlayerWithHook)player).bygone$getHook() != this
            || this.distanceTo(player) > 92F
            || player.isShiftKeyDown();
    }

    @Override public boolean canUsePortal(boolean allowVehicles){ return false; }
    @Override protected @NotNull SoundEvent getDefaultHitGroundSoundEvent(){ return BGSoundEvents.HOOK_HIT_ADDITIONS_EVENT; }
    @Override protected ItemStack getDefaultPickupItem(){ return BGItems.ANCIENT_HOOK.get().getDefaultInstance(); }
    @Override protected void onHitEntity(EntityHitResult result){ this.discard(); }

    @Override
    public void setOwner(@Nullable Entity entity) {
        super.setOwner(entity); // sets pickupability, so we gotta reset it
        this.pickup = Pickup.DISALLOWED;
    }
}