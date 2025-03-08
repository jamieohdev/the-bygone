package com.jamiedev.bygone.common.entity;

import com.jamiedev.bygone.core.registry.BGEntityTypes;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.DirectionalPlaceContext;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;

public class RisingBlockEntity extends FallingBlockEntity
{
    public interface FallingBlockEntityAccess {
        void setBlockState(BlockState block);
    }

    private Level world;
    public RisingBlockEntity(EntityType<? extends FallingBlockEntity> entityType, Level world) {
        super(entityType, world);
    }

    private RisingBlockEntity(Level world, double x, double y, double z, BlockState block) {
        this(BGEntityTypes.RISING_BLOCK.get(), world);
        ((FallingBlockEntityAccess)this).setBlockState(block);
        this.blocksBuilding = true;
        this.setPos(x, y, z);
        this.setDeltaMovement(Vec3.ZERO);
        this.xo = x;
        this.yo = y;
        this.zo = z;
        this.setStartPos(this.blockPosition());
    }

    @Override
    public void move(MoverType movementType, Vec3 movement) {
        super.move(movementType, movementType == MoverType.SELF ? movement.scale(-1) : movement);
    }

    @Override
    public void tick() {
        super.tick();

        BlockPos blockPos = blockPosition();
        boolean collisionOverhead = !world.getBlockState(blockPos.above()).getCollisionShape(world, blockPos.above()).isEmpty();

        if (!world.isClientSide && world.isOutsideBuildHeight(blockPos.getY()) && world.isOutsideBuildHeight(blockPos.getY() - 10)) {

            discard();
            return;
        }

        if (!world.isClientSide && !isRemoved() && collisionOverhead) {

            BlockState state = getBlockState();
            Block block = state.getBlock();
            BlockState currState = world.getBlockState(blockPos);
            this.setDeltaMovement(getDeltaMovement().multiply(.7, .5, .7));
            boolean replaceable = currState.canBeReplaced(new DirectionalPlaceContext(world, blockPos, Direction.UP, ItemStack.EMPTY, Direction.DOWN));
            boolean shouldPlace = state.canSurvive(world, blockPos) && !FallingBlock.isFree(world.getBlockState(blockPos.above()));

            if (replaceable && shouldPlace) {

                if (world.setBlock(blockPos, state, Block.UPDATE_ALL)) {
                    ((ServerLevel)this.world).getChunkSource().chunkMap.broadcast(this, new ClientboundBlockUpdatePacket(blockPos, this.world.getBlockState(blockPos)));
                    this.discard();
                } else if (dropItem && world.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS)) {
                    this.discard();
                    this.callOnBrokenAfterFall(block, blockPos);
                    this.spawnAtLocation(block);
                }
            } else {
                discard();
                if (dropItem && world.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS)) {
                    this.callOnBrokenAfterFall(block, blockPos);
                    this.spawnAtLocation(block);
                }
            }
        }
    }

    public static RisingBlockEntity fall(Level world, BlockPos pos, BlockState state) {
        RisingBlockEntity fallingBlockEntity = new RisingBlockEntity(world, (double)pos.getX() + 0.5, pos.getY(), (double)pos.getZ() + 0.5, state.hasProperty(BlockStateProperties.WATERLOGGED) ? state.setValue(BlockStateProperties.WATERLOGGED, false) : state);
        fallingBlockEntity.time -= 1200;
        world.setBlock(pos, state.getFluidState().createLegacyBlock(), Block.UPDATE_ALL);
        world.addFreshEntity(fallingBlockEntity);
        return fallingBlockEntity;
    }
}
