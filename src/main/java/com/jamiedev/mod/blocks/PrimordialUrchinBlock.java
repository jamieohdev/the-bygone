package com.jamiedev.mod.blocks;

import com.jamiedev.mod.blocks.entity.PrimordialUrchinEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;

public class PrimordialUrchinBlock extends BlockWithEntity implements Waterloggable
{
    public static final MapCodec<PrimordialUrchinBlock> CODEC = createCodec(PrimordialUrchinBlock::new);
    public static final IntProperty ACTIVATED;
    public static BooleanProperty ACTIVATEDBOOL;
    public static final VoxelShape SHAPE;
    public static final BooleanProperty WATERLOGGED;

    public static boolean test = false;

    FlowerBlock ref;


    public MapCodec<PrimordialUrchinBlock> getCodec() { return CODEC; }

    public PrimordialUrchinBlock(Settings settings) {
        super(settings);
        this.setDefaultState((BlockState)((BlockState)((BlockState)this.stateManager.getDefaultState()).with(ACTIVATED, 0)).with(ACTIVATEDBOOL, false).with(WATERLOGGED, false));
    }

    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    protected static boolean isInWater(BlockState state, BlockView world, BlockPos pos) {
        if ((Boolean)state.get(WATERLOGGED)) {
            return true;
        } else {
            Direction[] var3 = Direction.values();
            int var4 = var3.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                Direction direction = var3[var5];
                if (world.getFluidState(pos.offset(direction)).isIn(FluidTags.WATER)) {
                    return true;
                }
            }

            return false;
        }
    }
    protected BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if ((Boolean)state.get(WATERLOGGED)) {
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }

        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    protected void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (!isInWater(state, world, pos)) {
            world.setBlockState(pos, (BlockState)this.getDefaultState().with(WATERLOGGED, false), 2);
        }

    }

    private static void updateState(BlockState state, World world, BlockPos pos) {

        boolean bl = (Boolean)state.get(ACTIVATEDBOOL);



    }

    protected boolean canPathfindThrough(BlockState state, NavigationType type) {
        return false;
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
        BlockState blockState = (BlockState)((BlockState)this.getDefaultState()).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
        return blockState;
    }

    private void updatePowered(World world, BlockPos pos) {
        BlockState blockState = world.getBlockState(pos);
        boolean bl = (Boolean)blockState.get(ACTIVATEDBOOL);
        boolean bl2 = false;
        List<? extends Entity> list = world.getOtherEntities((Entity)null, blockState.getOutlineShape(world, pos).getBoundingBox().offset(pos));
        if (!list.isEmpty()) {
            Iterator var7 = list.iterator();

            while(var7.hasNext()) {
                Entity entity = (Entity)var7.next();
                if (!entity.canAvoidTraps()) {
                    bl2 = true;
                    break;
                }
            }
        }

        if (bl2 != bl) {
            blockState = (BlockState)blockState.with(ACTIVATEDBOOL, bl2);
            world.setBlockState(pos, blockState, 3);
        }

        if (bl2) {
            world.scheduleBlockTick(new BlockPos(pos), this, 10);
        }

    }

    protected void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (!world.isClient && world.getDifficulty() != Difficulty.PEACEFUL) {
            if (entity instanceof LivingEntity livingEntity && !(Boolean)state.get(ACTIVATEDBOOL)) {
                if (!livingEntity.isInvulnerableTo(world.getDamageSources().cactus()) && !livingEntity.isInCreativeMode()) {
                    world.playSound((double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, SoundEvents.ENTITY_PUFFER_FISH_BLOW_UP,
                            SoundCategory.BLOCKS, 0.5F + world.random.nextFloat(), world.random.nextFloat() * 0.7F + 0.6F, false);
                    BlockState blockState = (BlockState)state.cycle(ACTIVATEDBOOL);
                    world.setBlockState(pos, blockState, 2);
                    world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(entity, blockState));
                    updateState(blockState, world, pos);
                    test = true;
                    entity.damage(world.getDamageSources().cactus(), 1.0F);
                    livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 10));

                }
            }

        }
        super.onEntityCollision(state, world, pos, entity);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new PrimordialUrchinEntity(pos, state);
    }

    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{ACTIVATEDBOOL, ACTIVATED, WATERLOGGED});
    }

    protected FluidState getFluidState(BlockState state) {
        return (Boolean)state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    static {
        WATERLOGGED = Properties.WATERLOGGED;
        ACTIVATED = Properties.POWER;
        ACTIVATEDBOOL = Properties.INVERTED;
        SHAPE = Block.createCuboidShape(4.0, 0.0, 4.0, 12.0, 8.0, 12.0);
    }
}
