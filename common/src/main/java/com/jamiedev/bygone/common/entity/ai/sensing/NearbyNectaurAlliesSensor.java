package com.jamiedev.bygone.common.entity.ai.sensing;

import com.jamiedev.bygone.common.entity.NectaurEntity;
import com.jamiedev.bygone.core.registry.BGMemoryModuleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;

import java.util.List;
import java.util.Set;

public class NearbyNectaurAlliesSensor extends Sensor<NectaurEntity> {
    @Override
    protected void doTick(ServerLevel level, NectaurEntity entity) {
        List<NectaurEntity> allies = level.getEntitiesOfClass(NectaurEntity.class,
                        entity.getBoundingBox().inflate(16)
                ).stream()
                .filter(possible -> entity.closerThan(possible, 16))
                .filter(NectaurEntity::isAlive)
                .toList();

        if (!allies.isEmpty()) {
            Brain<?> brain = entity.getBrain();
            brain.setMemory(BGMemoryModuleTypes.NEAREST_NECTAUR_ALLY, allies.getFirst());
        }
    }

    @Override
    public Set<MemoryModuleType<?>> requires() {
        return Set.of(BGMemoryModuleTypes.NEAREST_NECTAUR_ALLY);
    }
}
