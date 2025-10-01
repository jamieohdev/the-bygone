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
    private static final Map<ResourceKey<Biome>, ResourceKey<LootTable>> BIOME_FISHING_TABLES_RARE = new HashMap<>();
    
    public static final ResourceKey<LootTable> ALPHAHANG_FISHING = register("gameplay/fishing/alphahang", BGBiomes.ALPHAHANG);
    public static final ResourceKey<LootTable> ANCIENT_FOREST_FISHING = register("gameplay/fishing/ancient_forest", BGBiomes.ANCIENT_FOREST);
    public static final ResourceKey<LootTable> AMBER_DESERT_FISHING = register("gameplay/fishing/amber_desert", BGBiomes.AMBER_DESERT);
    public static final ResourceKey<LootTable> MEGALITH_FIELD_FISHING = register("gameplay/fishing/megalith_field", BGBiomes.MEGALITH_FIELD);
    public static final ResourceKey<LootTable> PRIMORDIAL_BEACH_FISHING = register("gameplay/fishing/primordial_beach", BGBiomes.PRIMORDIAL_BEACH);
    public static final ResourceKey<LootTable> PRIMORDIAL_OCEAN_FISHING = register("gameplay/fishing/primordial_ocean", BGBiomes.PRIMORDIAL_OCEAN);
    public static final ResourceKey<LootTable> SHELFHOLLOW_FISHING = register("gameplay/fishing/shelfhollow", BGBiomes.SHELFHOLLOW);
    
    public static final ResourceKey<LootTable> ALPHAHANG_FISHING_RARE = registerRare("gameplay/fishing/alphahang_rare", BGBiomes.ALPHAHANG);
    public static final ResourceKey<LootTable> ANCIENT_FOREST_FISHING_RARE = registerRare("gameplay/fishing/ancient_forest_rare", BGBiomes.ANCIENT_FOREST);
    public static final ResourceKey<LootTable> AMBER_DESERT_FISHING_RARE = registerRare("gameplay/fishing/amber_desert_rare", BGBiomes.AMBER_DESERT);
    public static final ResourceKey<LootTable> MEGALITH_FIELD_FISHING_RARE = registerRare("gameplay/fishing/megalith_field_rare", BGBiomes.MEGALITH_FIELD);
    public static final ResourceKey<LootTable> PRIMORDIAL_BEACH_FISHING_RARE = registerRare("gameplay/fishing/primordial_beach_rare", BGBiomes.PRIMORDIAL_BEACH);
    public static final ResourceKey<LootTable> PRIMORDIAL_OCEAN_FISHING_RARE = registerRare("gameplay/fishing/primordial_ocean_rare", BGBiomes.PRIMORDIAL_OCEAN);
    public static final ResourceKey<LootTable> SHELFHOLLOW_FISHING_RARE = registerRare("gameplay/fishing/shelfhollow_rare", BGBiomes.SHELFHOLLOW);
    
    private static ResourceKey<LootTable> register(String path, ResourceKey<Biome> biome) {
        ResourceKey<LootTable> lootTable = ResourceKey.create(Registries.LOOT_TABLE, Bygone.id(path));
        BIOME_FISHING_TABLES.put(biome, lootTable);
        return lootTable;
    }
    
    private static ResourceKey<LootTable> registerRare(String path, ResourceKey<Biome> biome) {
        ResourceKey<LootTable> lootTable = ResourceKey.create(Registries.LOOT_TABLE, Bygone.id(path));
        BIOME_FISHING_TABLES_RARE.put(biome, lootTable);
        return lootTable;
    }
    
    public static ResourceKey<LootTable> getFishingTableForBiome(Holder<Biome> biomeHolder) {
        return getFishingTableForBiome(biomeHolder, false);
    }
    
    public static ResourceKey<LootTable> getFishingTableForBiome(Holder<Biome> biomeHolder, boolean useRare) {
        ResourceKey<Biome> biomeKey = biomeHolder.unwrapKey().orElse(null);
        if (useRare) {
            return BIOME_FISHING_TABLES_RARE.get(biomeKey);
        }
        return BIOME_FISHING_TABLES.get(biomeKey);
    }
    
    public static void init() {
    }
}