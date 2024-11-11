package com.jamiedev.mod.common.entities.projectile;

import com.jamiedev.mod.fabric.init.JamiesModEntityTypes;
import com.jamiedev.mod.fabric.init.JamiesModItems;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class HookEntity extends PersistentProjectileEntity
{



    public HookEntity(EntityType<? extends HookEntity> entityType, World pLevel) {
        super(entityType, pLevel);
        this.ignoreCameraFrustum = true;
    }

    public HookEntity(World level, PlayerEntity player) {
        super(JamiesModEntityTypes.HOOK, level);
        setOwner(player);
        setPos(player.getX(), player.getEyeY() - 0.1, player.getZ());
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return JamiesModItems.HOOK.getDefaultStack();
    }

    @Override
    public void setOwner(@Nullable Entity entity) {
        super.setOwner(entity);
        this.pickupType = PickupPermission.DISALLOWED;
    }

    @Nullable
    public PlayerEntity getPlayerOwner() {
        Entity entity = this.getOwner();
        return entity instanceof PlayerEntity ? (PlayerEntity)entity : null;
    }

    @Override
    public void tick() {
        super.tick();
        PlayerEntity player = this.getPlayerOwner();
        if((player == null || this.shouldRetract(player)) && !this.getWorld().isClient) {
            this.discard();
        }
    }

    private boolean shouldRetract(PlayerEntity player) {
        return player.isRemoved() || !player.isAlive() || !player.isHolding(JamiesModItems.HOOK) || this.squaredDistanceTo(player) > 10000.0;
    }

    @Override
    public boolean canUsePortals(boolean allowVehicles) {
        return false;
    }

    @Override
    public boolean isInsideWall() {
        if (this.noClip) {
            return false;
        } else {
            float f = this.getDimensions(this.getPose()).width() * 0.8F;
            Box box = Box.of(this.getEyePos(), (double)f, 1.0E-6, (double)f);
            return BlockPos.stream(box).anyMatch((pos) -> {
                BlockState blockState = this.getWorld().getBlockState(pos);
                return !blockState.isAir() && VoxelShapes.matchesAnywhere(blockState.getCollisionShape(this.getWorld(), pos)
                        .offset((double)pos.getX(), (double)pos.getY(), (double)pos.getZ()), VoxelShapes.cuboid(box), BooleanBiFunction.AND);
            });
        }
    }
}
