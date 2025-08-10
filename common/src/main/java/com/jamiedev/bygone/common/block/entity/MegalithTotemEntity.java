package com.jamiedev.bygone.common.block.entity;

import com.jamiedev.bygone.core.registry.BGBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Clearable;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class MegalithTotemEntity extends BlockEntity implements Clearable
{
    public  MegalithTotemEntity(BlockPos pos, BlockState state) {
        super(BGBlockEntities.MEGALITH_TOTEM.get(), pos, state);
    }

    @Override
    public void clearContent() {

    }

}