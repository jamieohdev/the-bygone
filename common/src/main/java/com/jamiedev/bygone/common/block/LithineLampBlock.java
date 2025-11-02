package com.jamiedev.bygone.common.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RedstoneTorchBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;

public class LithineLampBlock extends Block {
    public static final BooleanProperty LIT;
    public static final MapCodec<LithineLampBlock> CODEC = simpleCodec(LithineLampBlock::new);

    static {
        LIT = RedstoneTorchBlock.LIT;
    }

    LithineLampBlock ref;

    public LithineLampBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(LIT, false));
    }

    protected MapCodec<? extends LithineLampBlock> codec() {
        return CODEC;
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(LIT, context.getLevel().hasNeighborSignal(context.getClickedPos()));
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (player.mayBuild()) {
            if (level.isClientSide) {
                return InteractionResult.SUCCESS;
            } else {
                BlockState blockstate = state.cycle(LIT);
                level.setBlock(pos, blockstate, 2);
                level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, blockstate));

                return InteractionResult.CONSUME;
            }
        } else {
            return super.useWithoutItem(state, level, pos, player, hitResult);
        }
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT);
    }
}
