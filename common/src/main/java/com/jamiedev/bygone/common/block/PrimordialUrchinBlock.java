package com.jamiedev.bygone.common.block;

import org.jetbrains.annotations.NotNull;
import com.jamiedev.bygone.common.block.entity.PrimordialUrchinEntity;
import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;

public class PrimordialUrchinBlock extends BaseEntityBlock implements SimpleWaterloggedBlock
{
    public static final MapCodec<PrimordialUrchinBlock> CODEC = simpleCodec(PrimordialUrchinBlock::new);
    public static final IntegerProperty ACTIVATED;
    public static BooleanProperty ACTIVATEDBOOL;
    public static final VoxelShape SHAPE;
    public static final BooleanProperty WATERLOGGED;

    public static boolean test = false;


    @Override
    public MapCodec<PrimordialUrchinBlock> codec() { return CODEC; }

    public PrimordialUrchinBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any().setValue(ACTIVATED, 0).setValue(ACTIVATEDBOOL, false).setValue(WATERLOGGED, false));
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    protected static boolean isInWater(BlockState state, BlockGetter world, BlockPos pos) {
        if (state.getValue(WATERLOGGED)) {
            return true;
        } else {
            Direction[] var3 = Direction.values();
            int var4 = var3.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                Direction direction = var3[var5];
                if (world.getFluidState(pos.relative(direction)).is(FluidTags.WATER)) {
                    return true;
                }
            }

            return false;
        }
    }
    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
        }

        return super.updateShape(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    protected void tick(BlockState state, ServerLevel world, BlockPos pos, @NotNull RandomSource random) {
        if (!isInWater(state, world, pos)) {
            world.setBlock(pos, this.defaultBlockState().setValue(WATERLOGGED, false), 2);
        }

    }

    private static void updateState(BlockState state, Level world, BlockPos pos) {

        boolean bl = state.getValue(ACTIVATEDBOOL);



    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        FluidState fluidState = ctx.getLevel().getFluidState(ctx.getClickedPos());
        BlockState blockState = this.defaultBlockState().setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
        return blockState;
    }

    private void updatePowered(Level world, BlockPos pos) {
        BlockState blockState = world.getBlockState(pos);
        boolean bl = blockState.getValue(ACTIVATEDBOOL);
        boolean bl2 = false;
        List<? extends Entity> list = world.getEntities(null, blockState.getShape(world, pos).bounds().move(pos));
        if (!list.isEmpty()) {
            Iterator<? extends Entity> var7 = list.iterator();

            while(var7.hasNext()) {
                Entity entity = (Entity)var7.next();
                if (!entity.isIgnoringBlockTriggers()) {
                    bl2 = true;
                    break;
                }
            }
        }

        if (bl2 != bl) {
            blockState = blockState.setValue(ACTIVATEDBOOL, bl2);
            world.setBlock(pos, blockState, 3);
        }

        if (bl2) {
            world.scheduleTick(new BlockPos(pos), this, 10);
        }

    }

    @Override
    protected void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
        if (!world.isClientSide && world.getDifficulty() != Difficulty.PEACEFUL) {
            if (entity instanceof LivingEntity livingEntity && !(Boolean)state.getValue(ACTIVATEDBOOL)) {
                if (!livingEntity.isInvulnerableTo(world.damageSources().cactus()) && !livingEntity.hasInfiniteMaterials()) {
                    world.playLocalSound((double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, SoundEvents.PUFFER_FISH_BLOW_UP,
                            SoundSource.BLOCKS, 0.5F + world.random.nextFloat(), world.random.nextFloat() * 0.7F + 0.6F, false);
                    BlockState blockState = state.cycle(ACTIVATEDBOOL);
                    world.setBlock(pos, blockState, 2);
                    world.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(entity, blockState));
                    updateState(blockState, world, pos);
                    test = true;
                    entity.hurt(world.damageSources().cactus(), 1.0F);
                    livingEntity.addEffect(new MobEffectInstance(MobEffects.POISON, 10));

                }
            }

        }
        super.entityInside(state, world, pos, entity);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new PrimordialUrchinEntity(pos, state);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ACTIVATEDBOOL, ACTIVATED, WATERLOGGED);
    }

    @Override
    protected FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    static {
        WATERLOGGED = BlockStateProperties.WATERLOGGED;
        ACTIVATED = BlockStateProperties.POWER;
        ACTIVATEDBOOL = BlockStateProperties.INVERTED;
        SHAPE = Block.box(4.0, 0.0, 4.0, 12.0, 8.0, 12.0);
    }
}
