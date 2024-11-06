package com.jamiedev.mod.compounds;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import org.ladysnake.cca.api.v3.component.Component;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

public interface CasterComponent extends Component, AutoSyncedComponent {
    @Override
    default void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {}

    @Override
    default void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {}
}
