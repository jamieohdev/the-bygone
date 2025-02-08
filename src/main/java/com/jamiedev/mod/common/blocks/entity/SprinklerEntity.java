package com.jamiedev.mod.common.blocks.entity;

import com.jamiedev.mod.fabric.init.JamiesModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.Clearable;
import net.minecraft.util.math.BlockPos;

public class SprinklerEntity  extends BlockEntity implements Clearable
{
    public SprinklerEntity(BlockPos pos, BlockState state) {
        super(JamiesModBlockEntities.SPRINKLER, pos, state);
    }

    @Override
    public void clear() {

    }

}