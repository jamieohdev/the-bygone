package com.jamiedev.mod.common.entities.ai;

import com.jamiedev.mod.fabric.datagen.JamiesModTags;
import com.jamiedev.mod.fabric.init.JamiesModTag;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;

public class SewerBrain
{

    protected static boolean isCopperOrVerdigris(ItemStack stack) {
        return stack.isIn(JamiesModTag.VERDAGRIS_ITEMS) || stack.isIn(JamiesModTag.COPPER_BLOCKS);
    }
}
