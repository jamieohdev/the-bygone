package com.jamiedev.bygone.block;

import com.google.common.base.Predicates;
import com.jamiedev.bygone.block.entity.CasterBlockEntity;
import com.jamiedev.bygone.init.JamiesModDamageTypes;
import com.jamiedev.bygone.init.JamiesModItems;
import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.*;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CasterBlock extends BaseEntityBlock implements BlockEntityTicker<CasterBlockEntity>
{
    public static final MapCodec<CasterBlock> CODEC = simpleCodec(CasterBlock::new);
    public static final DirectionProperty FACING;
    public static final BooleanProperty TRIGGERED;
    public static final EnumProperty<CasterType> TYPE;

    public CasterBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(TRIGGERED, false).setValue(TYPE, CasterType.NONE));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(FACING, ctx.getNearestLookingDirection().getOpposite());
    }

    @Override
    protected void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
        Containers.dropContentsOnDestroy(state, newState, world, pos);
        super.onRemove(state, world, pos, newState, moved);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        if (world.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            Item item = player.getMainHandItem().getItem();
            if (item == Items.BLAZE_ROD) {
                world.setBlockAndUpdate(pos, state.setValue(TYPE, CasterType.BLAZE));
            }
            else if (item == Items.BREEZE_ROD) {
                world.setBlockAndUpdate(pos, state.setValue(TYPE, CasterType.BREEZE));
            }
            else if (item == JamiesModItems.SCUTTLE_SPIKE) {
                world.setBlockAndUpdate(pos, state.setValue(TYPE, CasterType.GUARDIAN));
            }
            return super.useWithoutItem(state, world, pos, player, hit);
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CasterBlockEntity(pos, state);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, TRIGGERED, TYPE);
    }

    @Override
    protected void neighborChanged(BlockState state, Level world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        boolean bl = world.hasNeighborSignal(pos) || world.hasNeighborSignal(pos.above());
        boolean bl2 = state.getValue(TRIGGERED);
        if (bl && !bl2) {
            world.setBlockAndUpdate(pos, state.setValue(TRIGGERED, true));
        } else if (!bl && bl2) {
            world.setBlockAndUpdate(pos, state.setValue(TRIGGERED, false));
        }
    }

    @Override
    protected boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    protected int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
        BlockEntity entity = world.getBlockEntity(pos);
        if (entity instanceof CasterBlockEntity && ((CasterBlockEntity) entity).cooldownTicks > 0) {
            return (int) Math.max(Math.min(Math.round((double) ((CasterBlockEntity) entity).cooldownTicks / 6), 15), 1);
        }
        return 0;
    }

    static {
        FACING = DirectionalBlock.FACING;
        TRIGGERED = BlockStateProperties.POWERED;
        TYPE = EnumProperty.create("caster_type", CasterType.class);
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return blockEntityType == JamiesModBlockEntities.CASTER ? (var1, var2, var3, var4) -> tick(var1, var2, var3, (CasterBlockEntity) var4) : null;
    }

    @Override
    public void tick(Level world, BlockPos pos, BlockState state, CasterBlockEntity blockEntity) {
        blockEntity.renderSpike = false;
        if (state.getValue(TRIGGERED)) {
            if (!world.isClientSide) {
                if (blockEntity.cooldownTicks > 0) {
                    blockEntity.cooldownTicks -= 1;
                    world.updateNeighbourForOutputSignal(pos, JamiesModBlocks.CASTER);
                    return;
                }
                else
                    blockEntity.ticks += 1;
                if (blockEntity.ticks >= 100) {
                    blockEntity.ticks = 0;
                    blockEntity.cooldownTicks = 100;
                }
            }
            else if (blockEntity.onCooldown) return;
            Direction direction = state.getValue(FACING);
            AABB box = new AABB(pos.relative(direction));
            switch (state.getValue(TYPE)) {
                case BREEZE -> {
                    int i = 1;
                    for (int i1 = 2; i < 4; i++) {
                        if (world.getBlockState(pos.relative(direction, i1)).isAir()) i += 1;
                    }
                    List<Entity> entities = world.getEntities(EntityTypeTest.forClass(Entity.class), box.minmax(new AABB(pos.relative(direction, i))), Predicates.alwaysTrue());
                    for (Entity entity : entities) {
                        entity.push((double) direction.getStepX()/10, (double) direction.getStepY()/10, (double) direction.getStepZ()/10);
                    }
                    if (blockEntity.ticks % 40 == 0) {
                        Vec3 blockPos = pos.relative(direction).getCenter();
                        world.addAlwaysVisibleParticle(ParticleTypes.POOF, true, blockPos.x, blockPos.y, blockPos.z, direction.getStepX()/0.5, direction.getStepY()/0.5, direction.getStepZ()/0.5);
                    }
                }
                case BLAZE -> {
                    List<Entity> entities = world.getEntities(EntityTypeTest.forClass(Entity.class), box, Predicates.alwaysTrue());
                    for (Entity entity : entities) {
                        entity.igniteForSeconds(15);
                    }
                    Vec3 blockPos = pos.relative(direction).getCenter();
                    for (int i = 0; i < 5; i++) {
                        world.addAlwaysVisibleParticle(ParticleTypes.FLAME, true, blockPos.x + (Math.random()-0.5), blockPos.y + (Math.random()-0.5), blockPos.z + (Math.random()-0.5), 0, 0, 0);
                    }
                }
                case GUARDIAN -> {
                    List<Entity> entities = world.getEntities(EntityTypeTest.forClass(Entity.class), box, Predicates.alwaysTrue());
                    for (Entity entity : entities) {
                        entity.hurt(world.damageSources().source(JamiesModDamageTypes.SPIKE), 1);
                    }
                    blockEntity.renderSpike = true;
                    Vec3 blockPos = pos.relative(direction).getCenter();
                    for (int i = 0; i < 5; i++) {
                        world.addAlwaysVisibleParticle(ParticleTypes.SOUL_FIRE_FLAME, true, blockPos.x + (Math.random()-0.5), blockPos.y + (Math.random()-0.5), blockPos.z + (Math.random()-0.5), 0, 0, 0);
                    }
                }
            }
        }
        else if (blockEntity.cooldownTicks > 0 && !world.isClientSide)
            blockEntity.cooldownTicks -= 1;
            world.updateNeighbourForOutputSignal(pos, JamiesModBlocks.CASTER);
        if (!blockEntity.isRemoved() && !world.isClientSide && ((blockEntity.onCooldown && blockEntity.cooldownTicks <=0) ||
                (!blockEntity.onCooldown && blockEntity.cooldownTicks > 0))) {
            blockEntity.onCooldown = blockEntity.cooldownTicks > 0;
            //todo JamiesModComponents.CASTER.sync(blockEntity);
        }
    }

    public enum CasterType implements StringRepresentable {
        BLAZE,
        BREEZE,
        GUARDIAN,
        NONE;

        @Override
        public String getSerializedName() {
            switch (this) {
                case BLAZE -> {
                    return "blaze";
                }
                case BREEZE -> {
                    return "breeze";
                }
                case GUARDIAN -> {
                    return "guardian";
                }
                default -> {
                    return "none";
                }
            }
        }
    }
}
