package com.jamiedev.bygone.common.block;

import com.google.common.collect.ImmutableMap;
import com.jamiedev.bygone.common.entity.WraithEntity;
import com.jamiedev.bygone.core.registry.BGBlocks;
import com.mojang.serialization.MapCodec;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.portal.PortalShape;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class IceBouquetBlock extends Block {
    FireBlock ref;
    public static final int MAX_AGE = 15;
    public static final IntegerProperty AGE = BlockStateProperties.AGE_15;
    public static final BooleanProperty NORTH = PipeBlock.NORTH;
    public static final BooleanProperty EAST = PipeBlock.EAST;
    public static final BooleanProperty SOUTH = PipeBlock.SOUTH;
    public static final BooleanProperty WEST = PipeBlock.WEST;
    public static final BooleanProperty UP = PipeBlock.UP;
    private static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION = PipeBlock.PROPERTY_BY_DIRECTION
            .entrySet()
            .stream()
            .filter(p_53467_ -> p_53467_.getKey() != Direction.DOWN)
            .collect(Util.toMap());
    private static final int SECONDS_ON_FIRE = 8;
    private final float fireDamage;
    protected static final float AABB_OFFSET = 1.0F;
    protected static final VoxelShape DOWN_AABB = Block.box(0.0, 0.0, 0.0, 16.0, 1.0, 16.0);
    private static final VoxelShape UP_AABB = Block.box(0.0, 15.0, 0.0, 16.0, 16.0, 16.0);
    private static final VoxelShape WEST_AABB = Block.box(0.0, 0.0, 0.0, 1.0, 16.0, 16.0);
    private static final VoxelShape EAST_AABB = Block.box(15.0, 0.0, 0.0, 16.0, 16.0, 16.0);
    private static final VoxelShape NORTH_AABB = Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 1.0);
    private static final VoxelShape SOUTH_AABB = Block.box(0.0, 0.0, 15.0, 16.0, 16.0, 16.0);
    private final Map<BlockState, VoxelShape> shapesCache;

    BaseFireBlock ref1;

    public IceBouquetBlock(BlockBehaviour.Properties properties, float fireDamage) {
        super(properties);
        this.fireDamage = fireDamage;
        this.registerDefaultState(
                this.stateDefinition
                        .any()
                        .setValue(AGE, Integer.valueOf(0))
                        .setValue(NORTH, Boolean.valueOf(false))
                        .setValue(EAST, Boolean.valueOf(false))
                        .setValue(SOUTH, Boolean.valueOf(false))
                        .setValue(WEST, Boolean.valueOf(false))
                        .setValue(UP, Boolean.valueOf(false))
        );
        this.shapesCache = ImmutableMap.copyOf(
                this.stateDefinition
                        .getPossibleStates()
                        .stream()
                        .filter(p_53497_ -> p_53497_.getValue(AGE) == 0)
                        .collect(Collectors.toMap(Function.identity(), IceBouquetBlock::calculateShape))
        );
    }

    private static VoxelShape calculateShape(BlockState state) {
        VoxelShape voxelshape = Shapes.empty();
        if (state.getValue(UP)) {
            voxelshape = UP_AABB;
        }

        if (state.getValue(NORTH)) {
            voxelshape = Shapes.or(voxelshape, NORTH_AABB);
        }

        if (state.getValue(SOUTH)) {
            voxelshape = Shapes.or(voxelshape, SOUTH_AABB);
        }

        if (state.getValue(EAST)) {
            voxelshape = Shapes.or(voxelshape, EAST_AABB);
        }

        if (state.getValue(WEST)) {
            voxelshape = Shapes.or(voxelshape, WEST_AABB);
        }

        return voxelshape.isEmpty() ? DOWN_AABB : voxelshape;
    }

    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos blockpos = pos.below();
        return level.getBlockState(blockpos).isFaceSturdy(level, blockpos, Direction.UP) || this.isValidFireLocation(level, pos);
    }

    public static boolean canBePlacedAt(Level level, BlockPos pos, Direction direction) {
        BlockState blockstate = level.getBlockState(pos);
        return blockstate.isAir() && getState(level, pos).canSurvive(level, pos);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.getStateForPlacement(context.getLevel(), context.getClickedPos());
    }

    protected BlockState getStateForPlacement(BlockGetter level, BlockPos pos) {
        BlockPos blockpos = pos.below();
        BlockState blockstate = level.getBlockState(blockpos);
        if (!blockstate.isFaceSturdy(level, blockpos, Direction.UP)) {
            BlockState blockstate1 = this.defaultBlockState();

            for (Direction direction : Direction.values()) {
                BooleanProperty booleanproperty = PROPERTY_BY_DIRECTION.get(direction);
            }

            return blockstate1;
        } else {
            return this.defaultBlockState();
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE, NORTH, EAST, SOUTH, WEST, UP);
    }

    public static BlockState getState(BlockGetter reader, BlockPos pos) {
        BlockPos blockpos = pos.below();
        BlockState blockstate = reader.getBlockState(blockpos);
        return BGBlocks.ICE_BOUQUET.get().defaultBlockState();
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return this.shapesCache.get(state.setValue(AGE, Integer.valueOf(0)));
    }

    /**
     * Called periodically clientside on blocks near the player to show effects (like furnace fire particles).
     */
    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (random.nextInt(24) == 0) {
            level.playLocalSound(
                    (double)pos.getX() + 0.5,
                    (double)pos.getY() + 0.5,
                    (double)pos.getZ() + 0.5,
                    SoundEvents.FIRE_AMBIENT,
                    SoundSource.BLOCKS,
                    1.0F + random.nextFloat(),
                    random.nextFloat() * 0.7F + 0.3F,
                    false
            );
        }
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        level.scheduleTick(pos, this, getFireTickDelay(level.random));
        if (level.random.nextFloat() < 0.1) {
            level.removeBlock(pos, false);
        }

        if (!state.canSurvive(level, pos)) {
            level.removeBlock(pos, false);
        }

        BlockState blockstate = level.getBlockState(pos.below());
        boolean flag = blockstate.is(level.dimensionType().infiniburn());
        int i = state.getValue(AGE);
        int j = Math.min(15, i + random.nextInt(3) / 2);
        if (i != j) {
            state = state.setValue(AGE, Integer.valueOf(j));
            level.setBlock(pos, state, 4);
        }

        if (!flag) {
            if (!this.isValidFireLocation(level, pos)) {
                BlockPos blockpos = pos.below();
                if (!level.getBlockState(blockpos).isFaceSturdy(level, blockpos, Direction.UP) || i > 3) {
                    level.removeBlock(pos, false);
                }

                return;
            }
        }

        boolean flag1 = level.getBiome(pos).is(BiomeTags.INCREASED_FIRE_BURNOUT);
        int k = flag1 ? -50 : 0;
        this.checkBurnOut(level, pos.east(), 300 + k, random, i);
        this.checkBurnOut(level, pos.west(), 300 + k, random, i);
        this.checkBurnOut(level, pos.below(), 250 + k, random, i);
        this.checkBurnOut(level, pos.above(), 250 + k, random, i);
        this.checkBurnOut(level, pos.north(), 300 + k, random, i);
        this.checkBurnOut(level, pos.south(), 300 + k, random, i);
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
    }

    private static int getFireTickDelay(RandomSource random) {
        return 30 + random.nextInt(10);
    }

    private void checkBurnOut(Level level, BlockPos pos, int chance, RandomSource random, int age) {
        int i = this.getBurnOdds(level.getBlockState(pos));
        if (random.nextInt(chance) < i) {
            BlockState blockstate = level.getBlockState(pos);
            if (random.nextInt(age + 10) < 5 && !level.isRainingAt(pos)) {
                int j = Math.min(age + random.nextInt(5) / 4, 15);
                level.setBlock(pos, this.getStateWithAge(level, pos, j), 3);
            } else {
                level.removeBlock(pos, false);
            }
        }
    }

    private BlockState getStateWithAge(LevelAccessor level, BlockPos pos, int age) {
        BlockState blockstate = getState(level, pos);
        return blockstate.is(Blocks.FIRE) ? blockstate.setValue(AGE, Integer.valueOf(age)) : blockstate;
    }

    private boolean isValidFireLocation(BlockGetter level, BlockPos pos) {
        for (Direction direction : Direction.Plane.HORIZONTAL ) {
            BlockState blockstate1 = level.getBlockState(pos.below());
            if (blockstate1.isSolid() || !blockstate1.isAir()) {
                return true;
            }
        }

        return false;
    }

    private int getBurnOdds(BlockState state) {
        return 0;
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        //entity.hurt(level.damageSources().inFire(), this.fireDamage);

        if (entity instanceof Player)
        {
            entity.hurt(level.damageSources().freeze(), this.fireDamage);
        }

        if (entity instanceof WraithEntity)
        {
            ((WraithEntity) entity).heal(0.05F);
        }

        super.entityInside(state, level, pos, entity);
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!oldState.is(state.getBlock())) {
            if (!state.canSurvive(level, pos)) {
                level.removeBlock(pos, false);
            }
        }
    }


    @Override
    protected void spawnDestroyParticles(Level level, Player player, BlockPos pos, BlockState state) {
    }

    @Override
    public @NotNull BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide()) {
            level.levelEvent(null, 1009, pos, 0);
        }

        return super.playerWillDestroy(level, pos, state, player);
    }

}
