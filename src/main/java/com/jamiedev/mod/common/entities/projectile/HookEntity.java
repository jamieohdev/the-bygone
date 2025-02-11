package com.jamiedev.mod.common.entities.projectile;

import com.jamiedev.mod.fabric.init.JamiesModEntityTypes;
import com.jamiedev.mod.fabric.init.JamiesModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import org.jetbrains.annotations.Nullable;

public class HookEntity extends AbstractArrow
{



    public HookEntity(EntityType<? extends HookEntity> entityType, Level pLevel) {
        super(entityType, pLevel);
        this.noCulling = true;
    }

    public HookEntity(Level level, Player player) {
        super(JamiesModEntityTypes.HOOK, level);
        setOwner(player);
        setPosRaw(player.getX(), player.getEyeY() - 0.1, player.getZ());
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return JamiesModItems.HOOK.getDefaultInstance();
    }

    @Override
    public void setOwner(@Nullable Entity entity) {
        super.setOwner(entity);
        this.pickup = Pickup.DISALLOWED;
    }

    @Nullable
    public Player getPlayerOwner() {
        Entity entity = this.getOwner();
        return entity instanceof Player ? (Player)entity : null;
    }

    @Override
    public void tick() {
        super.tick();
        Player player = this.getPlayerOwner();
        if((player == null || this.shouldRetract(player)) && !this.level().isClientSide) {
            this.discard();
        }
    }

    private boolean shouldRetract(Player player) {
        return player.isRemoved() || !player.isAlive() || !player.isHolding(JamiesModItems.HOOK) || this.distanceToSqr(player) > 10000.0;
    }

    @Override
    public boolean canUsePortal(boolean allowVehicles) {
        return false;
    }

    @Override
    public boolean isInWall() {
        if (this.noPhysics) {
            return false;
        } else {
            float f = this.getDimensions(this.getPose()).width() * 0.8F;
            AABB box = AABB.ofSize(this.getEyePosition(), (double)f, 1.0E-6, (double)f);
            return BlockPos.betweenClosedStream(box).anyMatch((pos) -> {
                BlockState blockState = this.level().getBlockState(pos);
                return !blockState.isAir() && Shapes.joinIsNotEmpty(blockState.getCollisionShape(this.level(), pos)
                        .move((double)pos.getX(), (double)pos.getY(), (double)pos.getZ()), Shapes.create(box), BooleanOp.AND);
            });
        }
    }
}
