package com.jamiedev.bygone.common.block;

import com.jamiedev.bygone.common.entity.BigBeakEntity;
import com.jamiedev.bygone.common.entity.BigBeakVariants;
import com.jamiedev.bygone.core.registry.BGEntityTypes;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SnifferEggBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class BigBeakEggBlock extends SnifferEggBlock {
    public static final MapCodec<SnifferEggBlock> CODEC = simpleCodec(BigBeakEggBlock::new);
    public static final EnumProperty<BigBeakVariants> VARIANT = EnumProperty.create("variant", BigBeakVariants.class);

    public BigBeakEggBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(VARIANT, BigBeakVariants.NORMAL));
    }

    @Override
    public MapCodec<SnifferEggBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(VARIANT);
    }

    @Override
    public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
        if (this.getHatchLevel(blockState) < MAX_HATCH_LEVEL) {
            super.tick(blockState, serverLevel, blockPos, randomSource);
            return;
        }

        BigBeakEntity bigBeakBaby = BGEntityTypes.BIG_BEAK.get().create(serverLevel);
        if (bigBeakBaby == null) {
            serverLevel.scheduleTick(blockPos, this, 200);
            return;
        }

        bigBeakBaby.setBaby(true);
        bigBeakBaby.setVariant(blockState.getValue(VARIANT));
        bigBeakBaby.moveTo(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5, randomSource.nextFloat() * 360.0F, 0.0F);
        if (!serverLevel.addFreshEntity(bigBeakBaby)) {
            serverLevel.scheduleTick(blockPos, this, 200);
            return;
        }

        serverLevel.playSound(null, blockPos, SoundEvents.SNIFFER_EGG_HATCH, SoundSource.BLOCKS, 0.7F, 0.9F + randomSource.nextFloat() * 0.2F);
        serverLevel.destroyBlock(blockPos, false);
    }
}
