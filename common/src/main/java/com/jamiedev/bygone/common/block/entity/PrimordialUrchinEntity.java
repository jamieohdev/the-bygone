package com.jamiedev.bygone.common.block.entity;

import com.jamiedev.bygone.core.registry.BGBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Clearable;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class PrimordialUrchinEntity  extends BlockEntity implements Clearable
{
    public PrimordialUrchinEntity(BlockPos pos, BlockState state) {
        super(BGBlockEntities.PRIMORDIAL_URCHIN, pos, state);
    }

    @Override
    public void clearContent() {

    }

}
