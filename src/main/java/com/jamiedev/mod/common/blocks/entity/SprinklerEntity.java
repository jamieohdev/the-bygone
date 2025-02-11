package com.jamiedev.mod.common.blocks.entity;

import com.jamiedev.mod.fabric.init.JamiesModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Clearable;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class SprinklerEntity  extends BlockEntity implements Clearable
{
    public SprinklerEntity(BlockPos pos, BlockState state) {
        super(JamiesModBlockEntities.SPRINKLER, pos, state);
    }

    @Override
    public void clearContent() {

    }

}