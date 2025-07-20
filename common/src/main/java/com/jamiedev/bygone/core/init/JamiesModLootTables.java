package com.jamiedev.bygone.core.init;

import com.jamiedev.bygone.Bygone;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;

public class JamiesModLootTables
{
    private static final Set<ResourceKey<LootTable>> LOOT_TABLES = new HashSet<>();
    private static final Set<ResourceKey<LootTable>> LOOT_TABLES_READ_ONLY;
    public static final ResourceKey<LootTable> EMPTY;

    public static final ResourceKey<LootTable> ABANDONED_MINESHAFT_CHEST;
    public static final ResourceKey<LootTable> LITHY_TRIP_LOOT_TABLE;

    public JamiesModLootTables() {
    }

    private static ResourceKey<LootTable> register(String id) {
        return registerLootTable(ResourceKey.create(Registries.LOOT_TABLE, Bygone.id(id)));
    }

    private static ResourceKey<LootTable> registerLootTable(ResourceKey<LootTable> key) {
        if (LOOT_TABLES.add(key)) {
            return key;
        } else {
            throw new IllegalArgumentException(key.location() + " is already a registered built-in loot table");
        }
    }

    public static Set<ResourceKey<LootTable>> getAll() {
        return LOOT_TABLES_READ_ONLY;
    }

    static {
        LOOT_TABLES_READ_ONLY = Collections.unmodifiableSet(LOOT_TABLES);
        EMPTY = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.withDefaultNamespace("empty"));
        ABANDONED_MINESHAFT_CHEST = register("chests/abandoned_mineshaft");
        LITHY_TRIP_LOOT_TABLE = register("gameplay/lithy_trip");
    }
}
