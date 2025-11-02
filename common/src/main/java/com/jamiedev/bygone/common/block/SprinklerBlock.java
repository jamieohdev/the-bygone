package com.jamiedev.bygone.common.block;

import com.jamiedev.bygone.common.block.entity.SprinklerEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;

public class SprinklerBlock extends BaseEntityBlock {
    public static final VoxelShape SHAPE = Block.box(5.0F, 0.0F, 5.0F, 11.0F, 6.0F, 11.0F);
    // public static final IntProperty FERTILIZERS;
    public static final IntegerProperty AGE;
    public static final MapCodec<SprinklerBlock> CODEC = simpleCodec(SprinklerBlock::new);

    static {
        AGE = BlockStateProperties.AGE_7;
        //  FERTILIZERS = Properties.AGE_2;
    }

    RandomSource random;

    public SprinklerBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any().setValue(this.getAgeProperty(), 0));
    }

    private static boolean isCropsNearby(LevelReader world, BlockPos pos) {
        Iterator<BlockPos> var2 = BlockPos.betweenClosed(pos.offset(-15, 0, -15), pos.offset(15, 1, 15)).iterator();

        BlockPos blockPos;
        do {
            if (!var2.hasNext()) {
                return false;
            }

            blockPos = var2.next();
        } while (world.getBlockState(blockPos).is(BlockTags.CROPS));

        return true;
    }

    private static boolean canFertilize(BlockState state) {
        return state.getValue(AGE) < 2;
    }

    private static boolean isFertilizerItem(ItemStack stack) {
        return stack.is(Items.BONE_MEAL);
    }

    protected IntegerProperty getAgeProperty() {
        return AGE;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected MapCodec<SprinklerBlock> codec() {
        return CODEC;
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SprinklerEntity(pos, state);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (isFertilizerItem(stack)) {
            stack.consume(1, player);
            if (isCropsNearby(world, pos)) {
                if (world.random.nextInt(4) == 1) {
                    for (int i = 1; i <= 2; ++i) {

                        for (BlockPos blockPos : BlockPos.betweenClosed(pos.offset(-15, 0, -15), pos.offset(15, 1, 15))) {
                            BlockState blockState = world.getBlockState(blockPos);
                            Block block = blockState.getBlock();
                            if (block instanceof AmaranthCropBlock cropBlock) {
                                if (world.random.nextFloat() <= 0.3 && !cropBlock.isMaxAge(blockState)) {
                                    if (world instanceof ServerLevel) {
                                        if (cropBlock.isBonemealSuccess(world, world.random, blockPos, blockState)) {
                                            cropBlock.performBonemeal((ServerLevel) world, world.random, blockPos, blockState);
                                            world.levelEvent(1505, blockPos, 15);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            return ItemInteractionResult.sidedSuccess(world.isClientSide);
        } else {
            return hand == InteractionHand.MAIN_HAND && canFertilize(state) ? ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION : ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }
}
