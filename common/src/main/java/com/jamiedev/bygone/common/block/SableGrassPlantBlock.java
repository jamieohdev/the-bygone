package com.jamiedev.bygone.common.block;

import com.jamiedev.bygone.core.registry.BGBlocks;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.GrowingPlantBodyBlock;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SableGrassPlantBlock extends GrowingPlantBodyBlock {
    public static final VoxelShape SHAPE = Block.box(4.0F, 0.0F, 4.0F, 12.0F, 16.0F, 12.0F);
    public static final MapCodec<SableGrassPlantBlock> CODEC = simpleCodec(SableGrassPlantBlock::new);
    IntegerProperty AGE;

    public SableGrassPlantBlock(BlockBehaviour.Properties p_154873_) {

        super(p_154873_, Direction.UP, SHAPE, false);

    }

    public MapCodec<SableGrassPlantBlock> codec() {
        return CODEC;
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (level.isEmptyBlock(pos.above())) {
            int i = 1;

            while (level.getBlockState(pos.below(i)).is(this)) {
                i++;
            }

            if (i < 3) {
                int j = state.getValue(AGE);
                if (j == 15) {
                    level.setBlockAndUpdate(pos.above(), this.defaultBlockState());
                    level.setBlock(pos, state.setValue(AGE, 0), 4);
                } else {
                    level.setBlock(pos, state.setValue(AGE, j + 1), 4);
                }
            }
        }
    }

    @Override
    protected GrowingPlantHeadBlock getHeadBlock() {
        return (GrowingPlantHeadBlock) BGBlocks.SABLE_GRASS.get();
    }
}
