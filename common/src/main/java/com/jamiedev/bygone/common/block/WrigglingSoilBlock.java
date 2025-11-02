package com.jamiedev.bygone.common.block;

import com.jamiedev.bygone.core.registry.BGBlocks;
import com.jamiedev.bygone.core.registry.BGItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class WrigglingSoilBlock extends Block {
    public WrigglingSoilBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (stack.is(Items.BUCKET)) {
            if (!level.isClientSide) {
                ItemStack baitwormBucket = new ItemStack(BGItems.BUCKET_O_BAITWORMS.get());

                level.setBlock(pos, BGBlocks.UMBRAL_SOIL.get().defaultBlockState(), 3);

                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }

                if (stack.isEmpty()) {
                    player.setItemInHand(hand, baitwormBucket);
                } else if (!player.getInventory().add(baitwormBucket)) {
                    player.drop(baitwormBucket, false);
                }

                level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                BlockPos farmlandPos = pos.offset(x, 0, z);
                BlockState farmlandState = level.getBlockState(farmlandPos);
                Block farmlandBlock = farmlandState.getBlock();

                if (farmlandBlock instanceof FarmBlock ||
                        farmlandBlock instanceof ClaystoneFarmlandBlock) {
                    BlockPos cropPos = farmlandPos.above();
                    BlockState cropState = level.getBlockState(cropPos);

                    if (cropState.getBlock() instanceof BonemealableBlock bonemealable) {
                        if (bonemealable.isValidBonemealTarget(level, cropPos, cropState) && random.nextInt(3) == 0) {
                            if (bonemealable.isBonemealSuccess(level, random, cropPos, cropState)) {
                                bonemealable.performBonemeal(level, random, cropPos, cropState);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return true;
    }
}
