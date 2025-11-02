package com.jamiedev.bygone.common.item;

import com.jamiedev.bygone.core.registry.BGBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class BucketOBaitwormsItem extends Item {

    private static final int EFFECT_RADIUS = 5;
    private static final int EFFECT_DURATION = 1200;

    public BucketOBaitwormsItem(Properties properties) {
//        super(properties.component(BGDataComponents.GUMBO_INGREDIENT_DATA.value(),
//                new GumboPotBlockEntity.GumboIngredientComponent(
//                        new FoodProperties.Builder()
//                                .nutrition(2)
//                                .saturationModifier(0.3F)
//                                .build()
//                )
//        ));
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        HitResult hitResult = getPlayerPOVHitResult(level, player, net.minecraft.world.level.ClipContext.Fluid.SOURCE_ONLY);

        if (hitResult.getType() == HitResult.Type.BLOCK) {
            BlockHitResult blockHitResult = (BlockHitResult) hitResult;
            BlockPos pos = blockHitResult.getBlockPos().immutable();
            BlockState state = level.getBlockState(pos);

            if (level.getFluidState(pos).is(FluidTags.WATER)) {
                if (!level.isClientSide) {
                    applyBaitwormEffect((ServerLevel) level, pos);
                    level.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);

                    if (!player.getAbilities().instabuild) {
                        return InteractionResultHolder.sidedSuccess(new ItemStack(Items.BUCKET), level.isClientSide);
                    }
                }
                return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
            }

            if (state.is(BGBlocks.UMBRAL_SOIL.get())) {
                if (!level.isClientSide) {
                    level.setBlock(pos, BGBlocks.WRIGGLING_SOIL.get().defaultBlockState(), 3);
                    level.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);

                    if (!player.getAbilities().instabuild) {
                        return InteractionResultHolder.sidedSuccess(new ItemStack(Items.BUCKET), level.isClientSide);
                    }
                }
                return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
            }
        }

        return InteractionResultHolder.pass(stack);
    }

    private void applyBaitwormEffect(ServerLevel level, BlockPos centerPos) {
        for (int x = -EFFECT_RADIUS; x <= EFFECT_RADIUS; x++) {
            for (int y = -EFFECT_RADIUS; y <= EFFECT_RADIUS; y++) {
                for (int z = -EFFECT_RADIUS; z <= EFFECT_RADIUS; z++) {
                    BlockPos pos = centerPos.offset(x, y, z);
                    if (level.getFluidState(pos).is(FluidTags.WATER)) {
                        if (level.random.nextFloat() < 0.3F) {
                            level.sendParticles(
                                    ParticleTypes.FISHING,
                                    pos.getX() + 0.5,
                                    pos.getY() + 0.5,
                                    pos.getZ() + 0.5,
                                    1,
                                    level.random.nextDouble() * 0.5 - 0.25,
                                    level.random.nextDouble() * 0.5 - 0.25,
                                    level.random.nextDouble() * 0.5 - 0.25,
                                    0.1
                            );
                        }
                    }
                }
            }
        }

        BaitwormWaterEffect.addEffect(level, centerPos, EFFECT_RADIUS, EFFECT_DURATION);
    }
}