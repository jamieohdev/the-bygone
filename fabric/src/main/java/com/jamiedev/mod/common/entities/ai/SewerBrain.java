package com.jamiedev.mod.common.entities.ai;

import com.jamiedev.mod.fabric.datagen.JamiesModTags;
import com.jamiedev.mod.fabric.init.JamiesModTag;
import net.minecraft.world.item.ItemStack;

public class SewerBrain
{

    protected static boolean isCopperOrVerdigris(ItemStack stack) {
        return stack.is(JamiesModTag.VERDAGRIS_ITEMS) || stack.is(JamiesModTag.COPPER_BLOCKS);
    }
}
