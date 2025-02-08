package com.jamiedev.mod.common.blocks;

import com.jamiedev.mod.common.blocks.entity.SprinklerEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;

public class SprinklerBlock extends BlockWithEntity implements Fertilizable
{
    public static final VoxelShape SHAPE= Block.createCuboidShape((double)5.0F, (double)0.0F, (double)5.0F, (double)11.0F, (double)6.0F, (double)11.0F);
    public static final MapCodec<SprinklerBlock> CODEC = createCodec(SprinklerBlock::new);
   // public static final IntProperty FERTILIZERS;
    public static final IntProperty AGE;
    Random random;

    public SprinklerBlock(Settings settings) {
        super(settings);
        this.setDefaultState((BlockState)((BlockState)this.stateManager.getDefaultState()).with(this.getAgeProperty(), 0));
    }
    protected IntProperty getAgeProperty() {
        return AGE;
    }


    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    protected MapCodec<SprinklerBlock> getCodec() {
        return CODEC;
    }

    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new SprinklerEntity(pos, state);
    }


    private static boolean isCropsNearby(WorldView world, BlockPos pos) {
        Iterator<BlockPos> var2 = BlockPos.iterate(pos.add(-15, 0, -15), pos.add(15, 1, 15)).iterator();

        BlockPos blockPos;
        do {
            if (!var2.hasNext()) {
                return false;
            }

            blockPos = (BlockPos)var2.next();
        } while(world.getBlockState(blockPos).isIn(BlockTags.CROPS));

        return true;
    }

    private static boolean canFertilize(BlockState state) {
        return (Integer)state.get(AGE) < 2;
    }


    private static boolean isFertilizerItem(ItemStack stack) {
        return stack.isOf(Items.BONE_MEAL);
    }

    protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (isFertilizerItem(stack)) {
            stack.decrementUnlessCreative(1, player);
            if (isCropsNearby(world, pos))
            {
                if (world.random.nextInt(2) == 1) {
                    for(int i = 1; i <= 2; ++i) {

                        for(BlockPos blockPos : BlockPos.iterate(pos.add(-15, 0, -15), pos.add(15, 1, 15))) {
                            BlockState blockState = world.getBlockState(blockPos);
                            Block block = blockState.getBlock();
                            BlockState blockState2 = null;
                            if (block instanceof AmaranthCropBlock cropBlock) {
                                if (world.random.nextFloat() <= 0.3 && !cropBlock.isMature(blockState)) {
                                    if (world instanceof ServerWorld) {
                                        if (cropBlock.canGrow(world, world.random, blockPos, blockState)) {
                                            cropBlock.grow((ServerWorld)world, world.random, blockPos, blockState);
                                            world.syncWorldEvent(1505, blockPos, 15);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            return ItemActionResult.success(world.isClient);
        } else {
            return hand == Hand.MAIN_HAND && canFertilize(state) ? ItemActionResult.SKIP_DEFAULT_BLOCK_INTERACTION : ItemActionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{AGE});
    }

    @Override
    public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state) {
        return false;
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return false;
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {

    }

    static
    {
        AGE = Properties.AGE_7;
      //  FERTILIZERS = Properties.AGE_2;
    }
}
