package com.jamiedev.bygone.common.entity.ai.behavior;

import com.jamiedev.bygone.common.entity.NectaurEntity;
import com.jamiedev.bygone.core.registry.BGMemoryModuleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

import java.util.Map;
import java.util.UUID;

public class NectaurFollowLeader extends Behavior<NectaurEntity> {
    public NectaurFollowLeader() {
        super(Map.of(BGMemoryModuleTypes.GROUP_LEADER, MemoryStatus.VALUE_PRESENT));
    }

    @Override
    protected void tick(ServerLevel level, NectaurEntity entity, long gameTime) {
        UUID leaderId = entity.getBrain().getMemory(BGMemoryModuleTypes.GROUP_LEADER).get();
        Entity leaderEntity = ((ServerLevel) entity.level()).getEntity(leaderId);

        if (leaderEntity instanceof NectaurEntity leader && !entity.getUUID().equals(leaderId)) {
            double distSq = entity.distanceTo(leader);

            if (distSq > 10.0D) {
                entity.getNavigation().moveTo(leader.getX(), leader.getY(), leader.getZ(), 8, 1.25D);
            }
        }
    }

    @Override
    protected boolean canStillUse(ServerLevel level, NectaurEntity entity, long gameTime) {
        return entity.getBrain().hasMemoryValue(BGMemoryModuleTypes.GROUP_LEADER) &&
                level.getEntity(entity.getBrain().getMemory(BGMemoryModuleTypes.GROUP_LEADER).get()) != null;
    }


}
