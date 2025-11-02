package com.jamiedev.bygone.common.block;

import com.jamiedev.bygone.Bygone;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.BlockSetType;

public class JamiesModBlockSetType {

    public static final BlockSetType ANCIENT = BlockSetType.register(
            new BlockSetType(
                    Bygone.id("ancient").toString(),
                    true,
                    true,
                    true,
                    BlockSetType.PressurePlateSensitivity.EVERYTHING,
                    SoundType.NETHER_WOOD,
                    SoundEvents.NETHER_WOOD_DOOR_CLOSE,
                    SoundEvents.NETHER_WOOD_DOOR_OPEN,
                    SoundEvents.NETHER_WOOD_TRAPDOOR_CLOSE,
                    SoundEvents.NETHER_WOOD_TRAPDOOR_OPEN,
                    SoundEvents.NETHER_WOOD_PRESSURE_PLATE_CLICK_OFF,
                    SoundEvents.NETHER_WOOD_PRESSURE_PLATE_CLICK_ON,
                    SoundEvents.NETHER_WOOD_BUTTON_CLICK_OFF,
                    SoundEvents.NETHER_WOOD_BUTTON_CLICK_ON
            )
    );

    public static final BlockSetType SABLE = BlockSetType.register(
            new BlockSetType(
                    Bygone.id("sable").toString(),
                    true,
                    true,
                    true,
                    BlockSetType.PressurePlateSensitivity.EVERYTHING,
                    SoundType.NETHER_WOOD,
                    SoundEvents.NETHER_WOOD_DOOR_CLOSE,
                    SoundEvents.NETHER_WOOD_DOOR_OPEN,
                    SoundEvents.NETHER_WOOD_TRAPDOOR_CLOSE,
                    SoundEvents.NETHER_WOOD_TRAPDOOR_OPEN,
                    SoundEvents.NETHER_WOOD_PRESSURE_PLATE_CLICK_OFF,
                    SoundEvents.NETHER_WOOD_PRESSURE_PLATE_CLICK_ON,
                    SoundEvents.NETHER_WOOD_BUTTON_CLICK_OFF,
                    SoundEvents.NETHER_WOOD_BUTTON_CLICK_ON
            )
    );

    public void init() {
    }
}
