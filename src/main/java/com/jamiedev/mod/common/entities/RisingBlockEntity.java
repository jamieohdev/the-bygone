package com.jamiedev.mod.common.entities;

import com.jamiedev.mod.common.blocks.LandingBlock2;
import com.jamiedev.mod.fabric.init.JamiesModEntityTypes;
import com.mojang.logging.LogUtils;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.AutomaticItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.network.EntityTrackerEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.crash.CrashReportSection;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.Iterator;
import java.util.function.Predicate;

public class RisingBlockEntity extends FallingBlockEntity
{
    public interface FallingBlockEntityAccess {
        void setBlockState(BlockState block);
    }

    private World world;
    public RisingBlockEntity(EntityType<? extends FallingBlockEntity> entityType, World world) {
        super(entityType, world);
    }

    private RisingBlockEntity(World world, double x, double y, double z, BlockState block) {
        this(JamiesModEntityTypes.RISING_BLOCK, world);
        ((FallingBlockEntityAccess)this).setBlockState(block);
        this.intersectionChecked = true;
        this.setPosition(x, y, z);
        this.setVelocity(Vec3d.ZERO);
        this.prevX = x;
        this.prevY = y;
        this.prevZ = z;
        this.setFallingBlockPos(this.getBlockPos());
    }

    @Override
    public void move(MovementType movementType, Vec3d movement) {
        super.move(movementType, movementType == MovementType.SELF ? movement.multiply(-1) : movement);
    }

    @Override
    public void tick() {
        super.tick();

        BlockPos blockPos = getBlockPos();
        boolean collisionOverhead = !world.getBlockState(blockPos.up()).getCollisionShape(world, blockPos.up()).isEmpty();

        if (!world.isClient && world.isOutOfHeightLimit(blockPos.getY()) && world.isOutOfHeightLimit(blockPos.getY() - 10)) {

            discard();
            return;
        }

        if (!world.isClient && !isRemoved() && collisionOverhead) {

            BlockState state = getBlockState();
            Block block = state.getBlock();
            BlockState currState = world.getBlockState(blockPos);
            this.setVelocity(getVelocity().multiply(.7, .5, .7));
            boolean replaceable = currState.canReplace(new AutomaticItemPlacementContext(world, blockPos, Direction.UP, ItemStack.EMPTY, Direction.DOWN));
            boolean shouldPlace = state.canPlaceAt(world, blockPos) && !FallingBlock.canFallThrough(world.getBlockState(blockPos.up()));

            if (replaceable && shouldPlace) {

                if (world.setBlockState(blockPos, state, Block.NOTIFY_ALL)) {
                    ((ServerWorld)this.world).getChunkManager().chunkLoadingManager.sendToOtherNearbyPlayers(this, new BlockUpdateS2CPacket(blockPos, this.world.getBlockState(blockPos)));
                    this.discard();
                } else if (dropItem && world.getGameRules().getBoolean(GameRules.DO_TILE_DROPS)) {
                    this.discard();
                    this.onDestroyedOnLanding(block, blockPos);
                    this.dropItem(block);
                }
            } else {
                discard();
                if (dropItem && world.getGameRules().getBoolean(GameRules.DO_TILE_DROPS)) {
                    this.onDestroyedOnLanding(block, blockPos);
                    this.dropItem(block);
                }
            }
        }
    }

    public static RisingBlockEntity spawnFromBlock(World world, BlockPos pos, BlockState state) {
        RisingBlockEntity fallingBlockEntity = new RisingBlockEntity(world, (double)pos.getX() + 0.5, pos.getY(), (double)pos.getZ() + 0.5, state.contains(Properties.WATERLOGGED) ? state.with(Properties.WATERLOGGED, false) : state);
        fallingBlockEntity.timeFalling -= 1200;
        world.setBlockState(pos, state.getFluidState().getBlockState(), Block.NOTIFY_ALL);
        world.spawnEntity(fallingBlockEntity);
        return fallingBlockEntity;
    }
}
