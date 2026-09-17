package com.jamiedev.bygone.common.block;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.jamiedev.bygone.core.init.JamiesModTag;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class LithoplasmicPowderBlock extends Block {

	public static final MapCodec<LithoplasmicPowderBlock> CODEC = simpleCodec(LithoplasmicPowderBlock::new);
	public static final EnumProperty<RedstoneSide> NORTH = BlockStateProperties.NORTH_REDSTONE;
	public static final EnumProperty<RedstoneSide> EAST = BlockStateProperties.EAST_REDSTONE;
	public static final EnumProperty<RedstoneSide> SOUTH = BlockStateProperties.SOUTH_REDSTONE;
	public static final EnumProperty<RedstoneSide> WEST = BlockStateProperties.WEST_REDSTONE;

	public static final Map<Direction, EnumProperty<RedstoneSide>> PROPERTY_BY_DIRECTION = Maps.newEnumMap(ImmutableMap.of(
			Direction.NORTH, NORTH,
			Direction.EAST, EAST,
			Direction.SOUTH, SOUTH,
			Direction.WEST, WEST
	));

	protected static final int W = 3;
	private static final VoxelShape SHAPE_DOT = Block.box(3, 0, 3, 13, 1, 13);
	private static final Map<Direction, VoxelShape> SHAPES_FLOOR = Maps.newEnumMap(ImmutableMap.of(
			Direction.NORTH, Block.box(3, 0, 0, 13, 1, 13),
			Direction.SOUTH, Block.box(3, 0, 3, 13, 1, 16),
			Direction.EAST, Block.box(3, 0, 3, 16, 1, 13),
			Direction.WEST, Block.box(0, 0, 3, 13, 1, 13)
	));
	private static final Map<Direction, VoxelShape> SHAPES_UP  = Maps.newEnumMap(ImmutableMap.of(
			Direction.NORTH, Shapes.or(SHAPES_FLOOR.get(Direction.NORTH), Block.box(3, 0, 0, 13, 16, 1)),
			Direction.SOUTH, Shapes.or(SHAPES_FLOOR.get(Direction.SOUTH), Block.box(3, 0, 15, 13, 16, 16)),
			Direction.EAST, Shapes.or(SHAPES_FLOOR.get(Direction.EAST), Block.box(15, 0, 3, 16, 16, 13)),
			Direction.WEST, Shapes.or(SHAPES_FLOOR.get(Direction.WEST), Block.box(0, 0, 3, 1, 16, 13))
	));

	private static final Map<BlockState, VoxelShape> SHAPES_CACHE = Maps.newHashMap();

	private final BlockState crossState;

	@Override
	public @NotNull MapCodec<LithoplasmicPowderBlock> codec() {
		return CODEC;
	}

	public LithoplasmicPowderBlock(BlockBehaviour.Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any()
				.setValue(NORTH, RedstoneSide.NONE)
				.setValue(EAST, RedstoneSide.NONE)
				.setValue(SOUTH, RedstoneSide.NONE)
				.setValue(WEST, RedstoneSide.NONE)
		);
		this.crossState = this.defaultBlockState()
				.setValue(NORTH, RedstoneSide.SIDE)
				.setValue(EAST, RedstoneSide.SIDE)
				.setValue(SOUTH, RedstoneSide.SIDE)
				.setValue(WEST, RedstoneSide.SIDE);
		for (BlockState state : this.getStateDefinition().getPossibleStates()) {
			SHAPES_CACHE.put(state, this.calculateShape(state));
		}
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(NORTH, EAST, SOUTH, WEST);
	}

	private VoxelShape calculateShape(BlockState state) {
		VoxelShape shape = SHAPE_DOT;

		for (Direction direction : Direction.Plane.HORIZONTAL) {
			RedstoneSide side = state.getValue(PROPERTY_BY_DIRECTION.get(direction));
			if (side == RedstoneSide.SIDE) {
				shape = Shapes.or(shape, SHAPES_FLOOR.get(direction));
			} else if (side == RedstoneSide.UP) {
				shape = Shapes.or(shape, SHAPES_UP.get(direction));
			}
		}

		return shape;
	}

	@Override
	protected @NotNull VoxelShape getCollisionShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
		if (context instanceof EntityCollisionContext entityContext) {
			if (entityContext.getEntity() != null && entityContext.getEntity().getType().is(JamiesModTag.SPECTRAL)) {
				return Shapes.block();
			}
		}
		return super.getCollisionShape(state, level, pos, context);
	}

	@Override
	protected @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
		return SHAPES_CACHE.get(state);
	}

	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.getConnectionState(context.getLevel(), this.crossState, context.getClickedPos());
	}

	private BlockState getConnectionState(BlockGetter level, BlockState state, BlockPos pos) {
		boolean isDot = isDot(state);
		state = this.getMissingConnections(level, this.defaultBlockState(), pos);
		if (isDot && isDot(state)) return state;

		boolean north = state.getValue(NORTH).isConnected();
		boolean south = state.getValue(SOUTH).isConnected();
		boolean east = state.getValue(EAST).isConnected();
		boolean west = state.getValue(WEST).isConnected();
		boolean notX = !north && !south;
		boolean notZ = !east && !west;

		if (!west && notX) {
			state = state.setValue(WEST, RedstoneSide.SIDE);
		}
		if (!east && notX) {
			state = state.setValue(EAST, RedstoneSide.SIDE);
		}
		if (!north && notZ) {
			state = state.setValue(NORTH, RedstoneSide.SIDE);
		}
		if (!south && notZ) {
			state = state.setValue(SOUTH, RedstoneSide.SIDE);
		}

		return state;
	}

	private BlockState getMissingConnections(BlockGetter level, BlockState state, BlockPos pos) {
		boolean flag = !level.getBlockState(pos.above()).isRedstoneConductor(level, pos);

		for (Direction direction : Direction.Plane.HORIZONTAL) {
			if (!state.getValue(PROPERTY_BY_DIRECTION.get(direction)).isConnected()) {
				RedstoneSide side = this.getConnectingSide(level, pos, direction, flag);
				state = state.setValue(PROPERTY_BY_DIRECTION.get(direction), side);
			}
		}

		return state;
	}

	protected BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
		if (facing == Direction.DOWN) {
			return !this.canSurviveOn(level, facingPos, facingState) ? Blocks.AIR.defaultBlockState() : state;
		} else if (facing == Direction.UP) {
			return this.getConnectionState(level, state, currentPos);
		} else {
			RedstoneSide side = this.getConnectingSide(level, currentPos, facing);
			return side.isConnected() == state.getValue(PROPERTY_BY_DIRECTION.get(facing)).isConnected() && !isCross(state) ? state.setValue(PROPERTY_BY_DIRECTION.get(facing), side) : this.getConnectionState(level, this.crossState.setValue(PROPERTY_BY_DIRECTION.get(facing), side), currentPos);
		}
	}

	private static boolean isCross(BlockState state) {
		return state.getValue(NORTH).isConnected()
				&& state.getValue(SOUTH).isConnected()
				&& state.getValue(EAST).isConnected()
				&& state.getValue(WEST).isConnected();
	}

	private static boolean isDot(BlockState state) {
		return !state.getValue(NORTH).isConnected()
				&& !state.getValue(SOUTH).isConnected()
				&& !state.getValue(EAST).isConnected()
				&& !state.getValue(WEST).isConnected();
	}

	@Override
	protected void updateIndirectNeighbourShapes(BlockState state, LevelAccessor level, BlockPos pos, int flags, int recursionLeft) {
		BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

		for (Direction direction : Direction.Plane.HORIZONTAL) {
			RedstoneSide side = state.getValue(PROPERTY_BY_DIRECTION.get(direction));
			if (side != RedstoneSide.NONE && !level.getBlockState(mutablePos.setWithOffset(pos, direction)).is(this)) {
				mutablePos.move(Direction.DOWN);
				BlockState belowState = level.getBlockState(mutablePos);
				if (belowState.is(this)) {
					BlockPos oppositePos = mutablePos.relative(direction.getOpposite());
					level.neighborShapeChanged(direction.getOpposite(), level.getBlockState(oppositePos), mutablePos, oppositePos, flags, recursionLeft);
				}

				mutablePos.setWithOffset(pos, direction).move(Direction.UP);
				BlockState aboveState = level.getBlockState(mutablePos);
				if (aboveState.is(this)) {
					BlockPos oppositePos = mutablePos.relative(direction.getOpposite());
					level.neighborShapeChanged(direction.getOpposite(), level.getBlockState(oppositePos), mutablePos, oppositePos, flags, recursionLeft);
				}
			}
		}
	}

	private RedstoneSide getConnectingSide(BlockGetter level, BlockPos pos, Direction face) {
		return this.getConnectingSide(level, pos, face, !level.getBlockState(pos.above()).isRedstoneConductor(level, pos));
	}

	private RedstoneSide getConnectingSide(BlockGetter level, BlockPos pos, Direction direction, boolean nonNormalCubeAbove) {
		BlockPos offsetPos = pos.relative(direction);
		BlockState offsetState = level.getBlockState(offsetPos);
		if (nonNormalCubeAbove) {
			if (this.canSurviveOn(level, offsetPos, offsetState) && level.getBlockState(offsetPos.above()).is(this)) {
				if (offsetState.isFaceSturdy(level, offsetPos, direction.getOpposite())) {
					return RedstoneSide.UP;
				}
				return RedstoneSide.SIDE;
			}
		}
		return offsetState.is(this) || !offsetState.isRedstoneConductor(level, offsetPos) && level.getBlockState(offsetPos.below()).is(this) ? RedstoneSide.SIDE : RedstoneSide.NONE;
	}

	protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		BlockPos belowPos = pos.below();
		BlockState belowState = level.getBlockState(belowPos);
		return this.canSurviveOn(level, belowPos, belowState);
	}

	private boolean canSurviveOn(BlockGetter level, BlockPos pos, BlockState state) {
		return state.isFaceSturdy(level, pos, Direction.UP);
	}

	private void checkCornerChangeAt(Level level, BlockPos pos) {
		if (level.getBlockState(pos).is(this)) {
			level.updateNeighborsAt(pos, this);
			for (Direction direction : Direction.values()) {
				level.updateNeighborsAt(pos.relative(direction), this);
			}
		}
	}

	@Override
	protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
		if (!oldState.is(state.getBlock()) && !level.isClientSide) {
			for (Direction direction : Direction.Plane.VERTICAL) {
				level.updateNeighborsAt(pos.relative(direction), this);
			}
			this.updateNeighborsOfNeighboringWires(level, pos);
		}
	}

	@Override
	protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
		if (!isMoving && !state.is(newState.getBlock())) {
			super.onRemove(state, level, pos, newState, false);
			if (!level.isClientSide) {
				for (Direction direction : Direction.values()) {
					level.updateNeighborsAt(pos.relative(direction), this);
				}
				this.updateNeighborsOfNeighboringWires(level, pos);
			}
		}
	}

	private void updateNeighborsOfNeighboringWires(Level level, BlockPos pos) {
		for (Direction direction : Direction.Plane.HORIZONTAL) {
			this.checkCornerChangeAt(level, pos.relative(direction));

			BlockPos offsetPos = pos.relative(direction);
			if (level.getBlockState(offsetPos).isRedstoneConductor(level, offsetPos)) {
				this.checkCornerChangeAt(level, offsetPos.above());
			} else {
				this.checkCornerChangeAt(level, offsetPos.below());
			}
		}
	}

	protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
		if (!level.isClientSide && !state.canSurvive(level, pos)) {
			dropResources(state, level, pos);
			level.removeBlock(pos, false);
		}
	}

	@Override
	protected @NotNull BlockState rotate(@NotNull BlockState state, Rotation rotation) {
		return switch (rotation) {
			case CLOCKWISE_180 -> state.setValue(NORTH, state.getValue(SOUTH)).setValue(EAST, state.getValue(WEST)).setValue(SOUTH, state.getValue(NORTH)).setValue(WEST, state.getValue(EAST));
			case COUNTERCLOCKWISE_90 -> state.setValue(NORTH, state.getValue(EAST)).setValue(EAST, state.getValue(SOUTH)).setValue(SOUTH, state.getValue(WEST)).setValue(WEST, state.getValue(NORTH));
			case CLOCKWISE_90 -> state.setValue(NORTH, state.getValue(WEST)).setValue(EAST, state.getValue(NORTH)).setValue(SOUTH, state.getValue(EAST)).setValue(WEST, state.getValue(SOUTH));
			default -> state;
		};
	}

	@Override
	protected @NotNull BlockState mirror(@NotNull BlockState state, Mirror mirror) {
		return switch (mirror) {
			case LEFT_RIGHT -> state.setValue(NORTH, state.getValue(SOUTH)).setValue(SOUTH, state.getValue(NORTH));
			case FRONT_BACK -> state.setValue(EAST, state.getValue(WEST)).setValue(WEST, state.getValue(EAST));
			default -> super.mirror(state, mirror);
		};
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
		if (!player.getAbilities().mayBuild) return InteractionResult.PASS;

		if (isCross(state)) {
			state = this.defaultBlockState();
		} else if (isDot(state)) {
			state = this.crossState;
		} else {
			return InteractionResult.PASS;
		}

		level.setBlock(pos, this.crossState, 3);
		this.updatesOnShapeChange(level, pos, state, state);
		return InteractionResult.SUCCESS;
	}

	private void updatesOnShapeChange(Level level, BlockPos pos, BlockState oldState, BlockState newState) {
		for (Direction direction : Direction.Plane.HORIZONTAL) {
			BlockPos offsetPos = pos.relative(direction);
			if (oldState.getValue(PROPERTY_BY_DIRECTION.get(direction)).isConnected() != newState.getValue(PROPERTY_BY_DIRECTION.get(direction)).isConnected() && level.getBlockState(offsetPos).isRedstoneConductor(level, offsetPos)) {
				level.updateNeighborsAtExceptFromFacing(offsetPos, newState.getBlock(), direction.getOpposite());
			}
		}
	}

}