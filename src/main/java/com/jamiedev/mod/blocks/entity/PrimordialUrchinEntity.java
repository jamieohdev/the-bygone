package com.jamiedev.mod.blocks.entity;

import com.jamiedev.mod.init.JamiesModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.Clearable;
import net.minecraft.util.math.BlockPos;

public class PrimordialUrchinEntity  extends BlockEntity implements Clearable
{
    public PrimordialUrchinEntity(BlockPos pos, BlockState state) {
        super(JamiesModBlockEntities.PRIMORDIAL_URCHIN, pos, state);
    }

    @Override
    public void clear() {

    }

}
