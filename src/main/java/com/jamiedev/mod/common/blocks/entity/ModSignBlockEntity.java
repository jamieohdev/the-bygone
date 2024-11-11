package com.jamiedev.mod.common.blocks.entity;

import com.jamiedev.mod.fabric.init.JamiesModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

public class ModSignBlockEntity extends SignBlockEntity
{
    public ModSignBlockEntity(BlockPos pos, BlockState state) {
        super(JamiesModBlockEntities.MOD_SIGN_BLOCK_ENTITY, pos, state);
    }

    @Override
    public @NotNull BlockEntityType<?> getType() {
        return JamiesModBlockEntities.MOD_SIGN_BLOCK_ENTITY;
    }
}
