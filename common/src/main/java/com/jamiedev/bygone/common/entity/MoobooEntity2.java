package com.jamiedev.bygone.common.entity;

import com.jamiedev.bygone.core.registry.BGBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;

public class MoobooEntity2 extends Cow
{

    public MoobooEntity2(EntityType<? extends Cow> entityType, Level level) {
        super(entityType, level);
    }

    public float getWalkTargetValue(BlockPos pos, LevelReader level) {
        return level.getBlockState(pos.below()).is(BGBlocks.SABLE_MOSS_BLOCK.get()) ? 10.0F : level.getPathfindingCostFromLightLevels(pos);
    }

}
