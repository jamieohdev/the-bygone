package com.jamiedev.mod.blocks;

import com.google.common.base.Predicates;
import com.jamiedev.mod.blocks.entity.CasterBlockEntity;
import com.jamiedev.mod.init.*;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CasterBlock extends BlockWithEntity implements BlockEntityTicker<CasterBlockEntity>
{
    public static final MapCodec<CasterBlock> CODEC = createCodec(CasterBlock::new);
    public static final DirectionProperty FACING;
    public static final BooleanProperty TRIGGERED;
    public static final EnumProperty<CasterType> TYPE;

    public CasterBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH).with(TRIGGERED, false).with(TYPE, CasterType.NONE));
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayerLookDirection().getOpposite());
    }

    protected void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        ItemScatterer.onStateReplaced(state, newState, world, pos);
        super.onStateReplaced(state, world, pos, newState, moved);
    }

    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        } else {
            Item item = player.getMainHandStack().getItem();
            if (item == Items.BLAZE_ROD) {
                world.setBlockState(pos, state.with(TYPE, CasterType.BLAZE));
            }
            else if (item == Items.BREEZE_ROD) {
                world.setBlockState(pos, state.with(TYPE, CasterType.BREEZE));
            }
            else if (item == JamiesModItems.SCUTTLE_SPIKE) {
                world.setBlockState(pos, state.with(TYPE, CasterType.GUARDIAN));
            }
            return super.onUse(state, world, pos, player, hit);
        }
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CasterBlockEntity(pos, state);
    }

    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    protected BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    protected BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, TRIGGERED, TYPE);
    }

    protected void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        boolean bl = world.isReceivingRedstonePower(pos) || world.isReceivingRedstonePower(pos.up());
        boolean bl2 = state.get(TRIGGERED);
        if (bl && !bl2) {
            world.setBlockState(pos, state.with(TRIGGERED, true));
        } else if (!bl && bl2) {
            world.setBlockState(pos, state.with(TRIGGERED, false));
        }
    }

    protected boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    protected int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        BlockEntity entity = world.getBlockEntity(pos);
        if (entity instanceof CasterBlockEntity && ((CasterBlockEntity) entity).cooldownTicks > 0) {
            return (int) Math.max(Math.min(Math.round((double) ((CasterBlockEntity) entity).cooldownTicks / 6), 15), 1);
        }
        return 0;
    }

    static {
        FACING = FacingBlock.FACING;
        TRIGGERED = Properties.POWERED;
        TYPE = EnumProperty.of("caster_type", CasterType.class);
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World level, BlockState state, BlockEntityType<T> blockEntityType) {
        return blockEntityType == JamiesModBlockEntities.CASTER ? (var1, var2, var3, var4) -> tick(var1, var2, var3, (CasterBlockEntity) var4) : null;
    }

    @Override
    public void tick(World world, BlockPos pos, BlockState state, CasterBlockEntity blockEntity) {
        blockEntity.renderSpike = false;
        if (state.get(TRIGGERED)) {
            if (!world.isClient) {
                if (blockEntity.cooldownTicks > 0) {
                    blockEntity.cooldownTicks -= 1;
                    world.updateComparators(pos, JamiesModBlocks.CASTER);
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
            Direction direction = state.get(FACING);
            Box box = new Box(pos.offset(direction));
            switch (state.get(TYPE)) {
                case BREEZE -> {
                    int i = 1;
                    for (int i1 = 2; i < 4; i++) {
                        if (world.getBlockState(pos.offset(direction, i1)).isAir()) i += 1;
                    }
                    List<Entity> entities = world.getEntitiesByType(TypeFilter.instanceOf(Entity.class), box.union(new Box(pos.offset(direction, i))), Predicates.alwaysTrue());
                    for (Entity entity : entities) {
                        entity.addVelocity((double) direction.getOffsetX()/10, (double) direction.getOffsetY()/10, (double) direction.getOffsetZ()/10);
                    }
                    if (blockEntity.ticks % 40 == 0) {
                        Vec3d blockPos = pos.offset(direction).toCenterPos();
                        world.addImportantParticle(ParticleTypes.POOF, true, blockPos.x, blockPos.y, blockPos.z, direction.getOffsetX()/0.5, direction.getOffsetY()/0.5, direction.getOffsetZ()/0.5);
                    }
                }
                case BLAZE -> {
                    List<Entity> entities = world.getEntitiesByType(TypeFilter.instanceOf(Entity.class), box, Predicates.alwaysTrue());
                    for (Entity entity : entities) {
                        entity.setOnFireFor(15);
                    }
                    Vec3d blockPos = pos.offset(direction).toCenterPos();
                    for (int i = 0; i < 5; i++) {
                        world.addImportantParticle(ParticleTypes.FLAME, true, blockPos.x + (Math.random()-0.5), blockPos.y + (Math.random()-0.5), blockPos.z + (Math.random()-0.5), 0, 0, 0);
                    }
                }
                case GUARDIAN -> {
                    List<Entity> entities = world.getEntitiesByType(TypeFilter.instanceOf(Entity.class), box, Predicates.alwaysTrue());
                    for (Entity entity : entities) {
                        entity.damage(world.getDamageSources().create(JamiesModDamageTypes.SPIKE), 1);
                    }
                    blockEntity.renderSpike = true;
                    Vec3d blockPos = pos.offset(direction).toCenterPos();
                    for (int i = 0; i < 5; i++) {
                        world.addImportantParticle(ParticleTypes.SOUL_FIRE_FLAME, true, blockPos.x + (Math.random()-0.5), blockPos.y + (Math.random()-0.5), blockPos.z + (Math.random()-0.5), 0, 0, 0);
                    }
                }
            }
        }
        else if (blockEntity.cooldownTicks > 0 && !world.isClient)
            blockEntity.cooldownTicks -= 1;
            world.updateComparators(pos, JamiesModBlocks.CASTER);
        if (!blockEntity.isRemoved() && !world.isClient && ((blockEntity.onCooldown && blockEntity.cooldownTicks <=0) ||
                (!blockEntity.onCooldown && blockEntity.cooldownTicks > 0))) {
            blockEntity.onCooldown = blockEntity.cooldownTicks > 0;
            JamiesModComponents.CASTER.sync(blockEntity);
        }
    }

    public enum CasterType implements StringIdentifiable {
        BLAZE,
        BREEZE,
        GUARDIAN,
        NONE;

        @Override
        public String asString() {
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
