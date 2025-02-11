package com.jamiedev.mod.common.blocks.entity;

import com.jamiedev.mod.fabric.init.JamiesModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
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

    public int getMaxTextLineWidth() {
        return 60;
    }

    public SoundEvent getSignInteractionFailedSoundEvent() {
        return SoundEvents.WAXED_HANGING_SIGN_INTERACT_FAIL;
    }
}
