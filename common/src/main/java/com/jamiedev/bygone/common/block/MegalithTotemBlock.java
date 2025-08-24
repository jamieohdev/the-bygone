    package com.jamiedev.bygone.common.block;

    import com.jamiedev.bygone.common.block.entity.MegalithTotemEntity;
    import com.jamiedev.bygone.core.registry.BGBlockEntities;
    import com.jamiedev.bygone.core.registry.BGItems;
    import com.mojang.serialization.MapCodec;
    import net.minecraft.core.BlockPos;
    import net.minecraft.core.Direction;
    import net.minecraft.world.InteractionHand;
    import net.minecraft.world.ItemInteractionResult;
    import net.minecraft.world.SimpleContainer;
    import net.minecraft.world.entity.player.Player;
    import net.minecraft.world.item.ItemStack;
    import net.minecraft.world.item.context.BlockPlaceContext;
    import net.minecraft.world.level.BlockGetter;
    import net.minecraft.world.level.Level;
    import net.minecraft.world.level.block.*;
    import net.minecraft.world.level.block.entity.BlockEntity;
    import net.minecraft.world.level.block.entity.BlockEntityTicker;
    import net.minecraft.world.level.block.entity.BlockEntityType;
    import net.minecraft.world.level.block.state.BlockState;
    import net.minecraft.world.level.block.state.StateDefinition;
    import net.minecraft.world.level.block.state.properties.DirectionProperty;
    import net.minecraft.world.phys.BlockHitResult;
    import net.minecraft.world.phys.shapes.CollisionContext;
    import net.minecraft.world.phys.shapes.VoxelShape;
    import org.jetbrains.annotations.Nullable;

    public class MegalithTotemBlock extends BaseEntityBlock {
        public static final MapCodec<MegalithTotemBlock> CODEC = simpleCodec(MegalithTotemBlock::new);
        public static final VoxelShape SHAPE;

        public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.Plane.HORIZONTAL);

        public MegalithTotemBlock(Properties properties) {
            super(properties);
        }

        @Override
        protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
            return SHAPE;
        }

        @Override
        protected MapCodec<? extends BaseEntityBlock> codec() {
            return CODEC;
        }

        @Override
        protected RenderShape getRenderShape(BlockState state) {
            return RenderShape.MODEL;
        }

        @Override
        public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
            return new MegalithTotemEntity(blockPos, blockState);
        }

        @Override
        protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
            super.createBlockStateDefinition(builder);
            builder.add(FACING);
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
        protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
            if (!level.isClientSide && stack.is(BGItems.LITHOPLASM.get())) {
                BlockEntity be = level.getBlockEntity(pos);
                if (be instanceof MegalithTotemEntity totem) {
                    SimpleContainer inv = totem.getInventory();
                    ItemStack slotStack = inv.getItem(0);
                    if (slotStack.isEmpty() || slotStack.is(BGItems.LITHOPLASM.get())) {
                        if (slotStack.isEmpty()) {
                            inv.setItem(0, stack.copyWithCount(1));
                            stack.shrink(1);
                        } else if (slotStack.getCount() < slotStack.getMaxStackSize()) {
                            slotStack.grow(1);
                            stack.shrink(1);
                        }
                        MegalithTotemEntity.tick(level, pos, state, totem);
                    }
                }


                return ItemInteractionResult.sidedSuccess(false);
            }
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        @Override
        public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(
                Level level, BlockState state, BlockEntityType<T> blockEntityType) {

            if (level.isClientSide) {return null;}

            if (blockEntityType == BGBlockEntities.MEGALITH_TOTEM.get()) {
                return (lvl, pos, st, be) -> {
                    if (be instanceof MegalithTotemEntity totem) {
                        MegalithTotemEntity.tick(lvl, pos, st, totem);
                    }
                };
            }
            return null;
        }

        static {
            SHAPE = Block.box(4.0, 0.0, 4.0, 12.0, 24.0, 12.0);
        }
    }
