package com.jamiedev.mod.fabric.init;

import com.jamiedev.mod.fabric.JamiesModFabric;
import net.minecraft.loot.LootTable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class JamiesModLootTables
{
    private static final Set<RegistryKey<LootTable>> LOOT_TABLES = new HashSet();
    private static final Set<RegistryKey<LootTable>> LOOT_TABLES_READ_ONLY;
    public static final RegistryKey<LootTable> EMPTY;

    public static final RegistryKey<LootTable> ABANDONED_MINESHAFT_CHEST;

    public JamiesModLootTables() {
    }

    private static RegistryKey<LootTable> register(String id) {
        return registerLootTable(RegistryKey.of(RegistryKeys.LOOT_TABLE, JamiesModFabric.getModId(id)));
    }

    private static RegistryKey<LootTable> registerLootTable(RegistryKey<LootTable> key) {
        if (LOOT_TABLES.add(key)) {
            return key;
        } else {
            throw new IllegalArgumentException(String.valueOf(key.getValue()) + " is already a registered built-in loot table");
        }
    }

    public static Set<RegistryKey<LootTable>> getAll() {
        return LOOT_TABLES_READ_ONLY;
    }

    static {
        LOOT_TABLES_READ_ONLY = Collections.unmodifiableSet(LOOT_TABLES);
        EMPTY = RegistryKey.of(RegistryKeys.LOOT_TABLE, Identifier.ofVanilla("empty"));
        ABANDONED_MINESHAFT_CHEST = register("chests/abandoned_mineshaft");
    }
}
