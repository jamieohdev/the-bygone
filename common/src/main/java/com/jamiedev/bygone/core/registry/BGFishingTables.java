package com.jamiedev.bygone.core.registry;

import com.jamiedev.bygone.Bygone;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.HashMap;
import java.util.Map;

public class BGFishingTables {
    
    private static final Map<ResourceKey<Biome>, ResourceKey<LootTable>> BIOME_FISHING_TABLES = new HashMap<>();
    
    public static final ResourceKey<LootTable> ALPHAHANG_FISHING = register("gameplay/fishing/alphahang", BGBiomes.ALPHAHANG);
    public static final ResourceKey<LootTable> ANCIENT_FOREST_FISHING = register("gameplay/fishing/ancient_forest", BGBiomes.ANCIENT_FOREST);
    public static final ResourceKey<LootTable> AMBER_DESERT_FISHING = register("gameplay/fishing/amber_desert", BGBiomes.AMBER_DESERT);
    public static final ResourceKey<LootTable> MEGALITH_FIELD_FISHING = register("gameplay/fishing/megalith_field", BGBiomes.MEGALITH_FIELD);
    public static final ResourceKey<LootTable> PRIMORDIAL_BEACH_FISHING = register("gameplay/fishing/primordial_beach", BGBiomes.PRIMORDIAL_BEACH);
    public static final ResourceKey<LootTable> PRIMORDIAL_OCEAN_FISHING = register("gameplay/fishing/primordial_ocean", BGBiomes.PRIMORDIAL_OCEAN);
    public static final ResourceKey<LootTable> SHELFHOLLOW_FISHING = register("gameplay/fishing/shelfhollow", BGBiomes.SHELFHOLLOW);
    
    private static ResourceKey<LootTable> register(String path, ResourceKey<Biome> biome) {
        ResourceKey<LootTable> lootTable = ResourceKey.create(Registries.LOOT_TABLE, Bygone.id(path));
        BIOME_FISHING_TABLES.put(biome, lootTable);
        return lootTable;
    }
    
    public static ResourceKey<LootTable> getFishingTableForBiome(Holder<Biome> biomeHolder) {
        return BIOME_FISHING_TABLES.get(biomeHolder.unwrapKey().orElse(null));
    }
    
    public static void init() {
    }
}