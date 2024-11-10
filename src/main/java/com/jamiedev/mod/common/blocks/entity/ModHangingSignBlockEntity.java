package com.jamiedev.mod.common.blocks.entity;

import com.jamiedev.mod.common.init.JamiesModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

public class ModHangingSignBlockEntity extends SignBlockEntity

{
    private static final int MAX_TEXT_WIDTH = 60;
    private static final int TEXT_LINE_HEIGHT = 9;


    public ModHangingSignBlockEntity(BlockPos two, BlockState three)
    {
        super(JamiesModBlockEntities.MOD_HANGING_SIGN_BLOCK_ENTITY, two, three);

    }

    @Override
    public @NotNull BlockEntityType<?> getType() {
        return JamiesModBlockEntities.MOD_HANGING_SIGN_BLOCK_ENTITY;
    }

    public int getTextLineHeight() {
        return 9;
    }

    public int getMaxTextWidth() {
        return 60;
    }

    public SoundEvent getInteractionFailSound() {
        return SoundEvents.BLOCK_HANGING_SIGN_WAXED_INTERACT_FAIL;
    }
}
