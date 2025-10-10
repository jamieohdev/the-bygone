package com.jamiedev.bygone.core.registry;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.common.block.DoguBlock;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class BGBlockProperties
{
    public static final EnumProperty<DoguBlock.Pose> DOGU_POSE = EnumProperty.create("dogu_pose", DoguBlock.Pose.class);

    public static void init() {

    }

}
