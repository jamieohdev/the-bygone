package com.jamiedev.bygone.core.registry;

import com.jamiedev.bygone.Bygone;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;

import java.util.Optional;
import java.util.UUID;

public class BGMemoryModuleTypes<U> {
    public static final MemoryModuleType<UUID> GROUP_LEADER = register("group_leader", UUIDUtil.CODEC);
    public static final MemoryModuleType<Boolean> IS_LEADER = register("is_leader");
    public static final MemoryModuleType<Boolean> IS_IN_GROUP = register("is_in_group");
    public static final MemoryModuleType<LivingEntity> NEAREST_NECTAUR_ALLY = register("nearest_nectaur_ally");
    public static final MemoryModuleType<Unit> NECTAUR_RANGED_COOLDOWN = register("nectaur_ranged_cooldown", Unit.CODEC);


    private static <U> MemoryModuleType<U> register(String identifier, Codec<U> codec) {
        return Registry.register(
                BuiltInRegistries.MEMORY_MODULE_TYPE, Bygone.id(identifier), new MemoryModuleType<>(Optional.of(codec))
        );
    }

    private static <U> MemoryModuleType<U> register(String identifier) {
        return Registry.register(
                BuiltInRegistries.MEMORY_MODULE_TYPE, Bygone.id(identifier), new MemoryModuleType<>(Optional.empty())
        );
    }

    protected static void init() {
        Bygone.LOGGER.info("Registering {} memory module types", Bygone.MOD_ID);
    }
}
