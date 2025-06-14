package com.jamiedev.bygone.common.entity.ai.behavior;

import com.jamiedev.bygone.common.entity.NectaurEntity;
import com.jamiedev.bygone.core.registry.BGMemoryModuleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class NectaurJoinGroup extends Behavior<NectaurEntity> {
    public NectaurJoinGroup() {
        super(Map.of());
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, NectaurEntity owner) {
        List<NectaurEntity> nectaurs = level.getEntitiesOfClass(
                NectaurEntity.class,
                owner.getBoundingBox().inflate(16)
        ).stream()
                .filter(entity -> entity != owner)
                .toList();

        return !nectaurs.isEmpty();
    }

    @Override
    protected void start(ServerLevel level, NectaurEntity owner, long gameTime) {
        List<NectaurEntity> nectaurs = level.getEntitiesOfClass(
                NectaurEntity.class,
                owner.getBoundingBox().inflate(16)
        ).stream()
                .filter(entity -> entity != owner)
                .toList();

        if (nectaurs.isEmpty()) return;

        for (NectaurEntity nectaur : nectaurs) {
            if (nectaur.getBrain().hasMemoryValue(BGMemoryModuleTypes.GROUP_LEADER)) {
                owner.getBrain().setMemory(BGMemoryModuleTypes.GROUP_LEADER, nectaur.getBrain().getMemory(BGMemoryModuleTypes.GROUP_LEADER).get());
                return;
            }
        }

        NectaurEntity leader = level.random.nextBoolean() ? owner : nectaurs.get(level.random.nextInt(nectaurs.size()));
        UUID leaderId = leader.getUUID();

        for (NectaurEntity member : nectaurs) {
            member.getBrain().setMemory(BGMemoryModuleTypes.GROUP_LEADER, leaderId);
        }

        owner.getBrain().setMemory(BGMemoryModuleTypes.GROUP_LEADER, leaderId);
    }

}
