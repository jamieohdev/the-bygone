package com.jamiedev.bygone.common.block;

import com.jamiedev.bygone.core.init.JamiesModTag;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;

public class UmbraSoilBlock extends Block
{
    public static final MapCodec<UmbraSoilBlock> CODEC = simpleCodec(UmbraSoilBlock::new);
    public static final BooleanProperty SNOWY;

    protected MapCodec<? extends UmbraSoilBlock> codec() {
        return CODEC;
    }

    public UmbraSoilBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue(SNOWY, false));
    }

    protected BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        return facing == Direction.UP ? (BlockState)state.setValue(SNOWY, isSnowySetting(facingState))
                : super.updateShape(state, facing, facingState, level, currentPos, facingPos);
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState blockstate = context.getLevel().getBlockState(context.getClickedPos().above());
        return (BlockState)this.defaultBlockState().setValue(SNOWY, isSnowySetting(blockstate));
    }

    private static boolean isSnowySetting(BlockState state) {
        return state.is(JamiesModTag.MOSSY);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{SNOWY});
    }

    static {
        SNOWY = BlockStateProperties.SNOWY;
    }
}
