package com.jamiedev.mod.common.blocks.gourds;

import com.jamiedev.mod.common.init.JamiesModBlocks;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;

public class GourdVineBlock extends AbstractPlantStemBlock {
    public static final MapCodec<GourdVineBlock> CODEC = createCodec(GourdVineBlock::new);
    protected static final VoxelShape SHAPE = Block.createCuboidShape(4.0, 0.0, 4.0, 12.0, 16.0, 12.0);

    public MapCodec<GourdVineBlock> getCodec() {
        return CODEC;
    }

    public GourdVineBlock(AbstractBlock.Settings settings) {
        super(settings, Direction.DOWN, SHAPE, false, 0.1);
    }

    protected int getGrowthLength(Random random) {
        return VineLogic.getGrowthLength(random);
    }

    protected Block getPlant() {
        return JamiesModBlocks.GOURD_VINE;
    }

    protected boolean chooseStemState(BlockState state) {
        return VineLogic.isValidForWeepingStem(state);
    }
}
