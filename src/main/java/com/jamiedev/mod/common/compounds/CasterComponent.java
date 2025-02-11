package com.jamiedev.mod.common.compounds;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import org.ladysnake.cca.api.v3.component.Component;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

public interface CasterComponent extends Component, AutoSyncedComponent {
    @Override
    default void readFromNbt(CompoundTag tag, HolderLookup.Provider registryLookup) {}

    @Override
    default void writeToNbt(CompoundTag tag, HolderLookup.Provider registryLookup) {}
}
