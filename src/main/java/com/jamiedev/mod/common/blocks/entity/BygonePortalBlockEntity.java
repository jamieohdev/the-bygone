package com.jamiedev.mod.common.blocks.entity;

import com.jamiedev.mod.fabric.init.JamiesModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class BygonePortalBlockEntity extends BlockEntity
{
    protected BygonePortalBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    public BygonePortalBlockEntity(BlockPos pos, BlockState state) {
        super(JamiesModBlockEntities.BYGONE_PORTAL, pos, state);
    }



    public boolean shouldDrawSide(Direction direction) {
        return direction.getAxis() == Direction.Axis.Y;
    }
}
