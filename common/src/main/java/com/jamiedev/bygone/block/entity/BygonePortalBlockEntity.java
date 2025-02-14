package com.jamiedev.bygone.block.entity;

import com.jamiedev.bygone.init.JamiesModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

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
