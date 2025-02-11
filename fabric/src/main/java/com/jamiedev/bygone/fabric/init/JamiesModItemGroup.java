package com.jamiedev.bygone.fabric.init;

import com.jamiedev.bygone.Bygone;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
public class JamiesModItemGroup
{
   // public static final RegistryKey<ItemGroup> JAMIES_MOD = RegistryKey.of(RegistryKeys.ITEM_GROUP, BygoneFabric.getModId("bygone"));
    public static final ResourceKey<CreativeModeTab> JAMIES_MOD = ResourceKey.create(Registries.CREATIVE_MODE_TAB, Bygone.getModId("test"));

    public static void registerItemgroups() {
      //  Registry.register(Registries.ITEM_GROUP, JAMIES_MOD, FabricItemGroup.builder()
       ////         .icon(() -> new ItemStack(JamiesModBlocks.AMBERSTONE))
       //         .displayName(Text.translatable("itemgroup.bygone"))
      //          .build());
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, JAMIES_MOD, FabricItemGroup.builder()
                .icon(() -> new ItemStack(JamiesModBlocks.SHELF_FUNGUS))
                .title(Component.translatable("itemGroup.test"))
                .build());
    }
}
