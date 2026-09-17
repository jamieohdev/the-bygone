package com.jamiedev.bygone.common.block;

import com.jamiedev.bygone.common.block.entity.LithineLampBlockEntity;
import com.jamiedev.bygone.core.registry.BGBlockEntities;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import javax.annotation.Nullable;
import java.util.function.ToIntFunction;

public class LithineLampBlock extends BaseEntityBlock {
    public static final IntegerProperty POWER = BlockStateProperties.POWER;
    public static ToIntFunction<BlockState> lithineLampValue() {
        return (state) -> state.getValue(BlockStateProperties.POWER).intValue();
    }

    public LithineLampBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(POWER, 0));
    }

    public static final MapCodec<LithineLampBlock> CODEC = simpleCodec(LithineLampBlock::new);
    @Override protected MapCodec<? extends BaseEntityBlock> codec() { return CODEC; }

    @Nullable @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return level.isClientSide ? null : createTickerHelper(blockEntityType, BGBlockEntities.LITHINE_LAMP.get(), LithineLampBlockEntity::tick);
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(POWER, 0);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POWER);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) { return RenderShape.MODEL; }

    @Override public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new LithineLampBlockEntity(blockPos, blockState);
    }
}
