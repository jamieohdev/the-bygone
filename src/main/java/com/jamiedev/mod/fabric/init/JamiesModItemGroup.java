package com.jamiedev.mod.fabric.init;

import com.jamiedev.mod.fabric.JamiesModFabric;
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
   // public static final RegistryKey<ItemGroup> JAMIES_MOD = RegistryKey.of(RegistryKeys.ITEM_GROUP, JamiesModFabric.getModId("bygone"));
    public static final RegistryKey<ItemGroup> JAMIES_MOD = RegistryKey.of(RegistryKeys.ITEM_GROUP, JamiesModFabric.getModId("test"));

    public static void registerItemgroups() {
      //  Registry.register(Registries.ITEM_GROUP, JAMIES_MOD, FabricItemGroup.builder()
       ////         .icon(() -> new ItemStack(JamiesModBlocks.AMBERSTONE))
       //         .displayName(Text.translatable("itemgroup.bygone"))
      //          .build());
        Registry.register(Registries.ITEM_GROUP, JAMIES_MOD, FabricItemGroup.builder()
                .icon(() -> new ItemStack(JamiesModItems.AMARANTH_LOAF))
                .displayName(Text.translatable("itemGroup.test"))
                .build());
    }
}
