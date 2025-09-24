package com.jamiedev.bygone.core.registry;

import com.jamiedev.bygone.common.block.entity.GumboPotBlockEntity;
import com.jamiedev.bygone.common.item.MaliciousWarHornItem;
import com.jamiedev.bygone.core.registry.BGDataComponentTypes;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;

public class BGDataComponents {
    
    public static Holder<DataComponentType<MaliciousWarHornItem.WarHornData>> WAR_HORN_DATA;
    public static Holder<DataComponentType<BGDataComponentTypes.EchoGongData>> ECHO_GONG_DATA;

    public static Holder<DataComponentType<GumboPotBlockEntity.GumboIngredientComponent>> GUMBO_INGREDIENT_DATA;

    public static void init() {
        
    }
}