package com.jamiedev.bygone.common.block;

import com.jamiedev.bygone.common.block.entity.DoguEntity;
import com.jamiedev.bygone.core.init.JamiesModTag;
import com.jamiedev.bygone.core.registry.BGBlockProperties;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.function.IntFunction;
import net.minecraft.core.Direction;

public class DoguBlock extends BaseEntityBlock implements SimpleWaterloggedBlock
{

    public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.Plane.HORIZONTAL);

    public static final EnumProperty<DoguBlock.Pose> POSE = BGBlockProperties.DOGU_POSE;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    private static final VoxelShape SHAPE;

    public final MapCodec<DoguBlock> CODEC = simpleCodec(DoguBlock::new);

    public DoguBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(
                this.defaultBlockState().setValue(FACING, Direction.NORTH)
                        .setValue(POSE, Pose.STANDING).setValue(WATERLOGGED, false)
        );
    }



    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    protected @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new DoguEntity(blockPos, blockState);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING, POSE, WATERLOGGED);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Deprecated
    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Deprecated
    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected ItemInteractionResult useItemOn(
            ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult
    ) {
            this.updatePose(level, state, pos, player);
            return ItemInteractionResult.SUCCESS;
    }


    void updatePose(Level level, BlockState blockState, BlockPos blockPos, Player player) {
        level.playSound(null, blockPos, SoundEvents.MUD_BRICKS_BREAK, SoundSource.BLOCKS);
        level.setBlock(blockPos, blockState.setValue(POSE, ((DoguBlock.Pose)blockState.getValue(POSE)).getNextPose()), 3);
        level.gameEvent(player, GameEvent.BLOCK_CHANGE, blockPos);
    }

    @Override
    protected boolean hasAnalogOutputSignal(BlockState blockState) {
        return true;
    }

    @Override
    protected int getAnalogOutputSignal(BlockState blockState, Level level, BlockPos pos){
        return ((DoguBlock.Pose)blockState.getValue(POSE)).ordinal() + 1;
    }

    @Override
    protected FluidState getFluidState(BlockState blockState) {
        return blockState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(blockState);
    }

    public BlockState updateShape(Direction direction, BlockState neighborState,
                                  LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        return this.updateShape(this.defaultBlockState(),
                direction, neighborState, level, pos, neighborPos);
    }

    static {
        SHAPE = Block.box(7.0, 0.0, 7.0, 9.0, 16.0, 9.0);
    }

    public static enum Pose implements StringRepresentable {
        STANDING("standing"),
        DANCING("dancing");

        public static final IntFunction<Pose> BY_ID = ByIdMap.continuous(Enum::ordinal, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
        public static final Codec<Pose> CODEC = StringRepresentable.fromEnum(DoguBlock.Pose::values);
        private final String name;

        private Pose(final String string2) {
            this.name = string2;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }

        public DoguBlock.Pose getNextPose() {
            return (DoguBlock.Pose)BY_ID.apply(this.ordinal() + 1);
        }
    }
}
