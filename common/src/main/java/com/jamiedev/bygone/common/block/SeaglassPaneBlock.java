package com.jamiedev.bygone.common.block;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.BeaconBeamBlock;
import net.minecraft.world.level.block.IronBarsBlock;
import org.jetbrains.annotations.NotNull;

public class SeaglassPaneBlock extends IronBarsBlock implements BeaconBeamBlock {
    public SeaglassPaneBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull DyeColor getColor() {
        return DyeColor.CYAN;
    }
}
