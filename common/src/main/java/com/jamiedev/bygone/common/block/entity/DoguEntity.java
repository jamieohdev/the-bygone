package com.jamiedev.bygone.common.block.entity;

import com.jamiedev.bygone.core.registry.BGBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class DoguEntity extends BlockEntity
{
    public DoguEntity(BlockPos pos, BlockState blockState) {
        super(BGBlockEntities.DOGU.get(), pos, blockState);
    }
}
