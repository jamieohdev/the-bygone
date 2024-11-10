package com.jamiedev.mod.common.items;

import com.jamiedev.mod.common.JamiesMod;
import com.jamiedev.mod.common.init.JamiesModBlocks;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
public class JamiesModItemGroup
{
    public static final RegistryKey<ItemGroup> JAMIES_MOD = RegistryKey.of(RegistryKeys.ITEM_GROUP, JamiesMod.getModId("bygone"));

    public static void registerItemgroups() {
        Registry.register(Registries.ITEM_GROUP, JAMIES_MOD, FabricItemGroup.builder()
                .icon(() -> new ItemStack(JamiesModBlocks.AMBER))
                .displayName(Text.translatable("itemgroup.bygone"))
                .build());
    }
}
