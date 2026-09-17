package com.jamiedev.bygone.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.CarpetBlock;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;

public class HauntedGroundBlock extends CarpetBlock {

	public HauntedGroundBlock(Properties properties) {
		super(properties);
	}

	protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		if (level.getBrightness(LightLayer.BLOCK, pos) > 11) {
			dropResources(state, level, pos);
			level.removeBlock(pos, false);
		}

	}
}
