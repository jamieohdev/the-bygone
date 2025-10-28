package com.jamiedev.bygone.common.entity.ai.sensing;

import com.google.common.collect.ImmutableSet;
import com.jamiedev.bygone.common.entity.SabeastEntity;
import com.jamiedev.bygone.core.init.JamiesModTag;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import com.jamiedev.bygone.common.entity.AmoebaEntity;

import java.util.Optional;
import java.util.Set;

public class AmoebaSensor extends Sensor<AmoebaEntity>
{
    @Override
    public Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.of(
                MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES,
                MemoryModuleType.NEAREST_REPELLENT
        );
    }

    protected void doTick(ServerLevel level, AmoebaEntity entity) {
        Brain<?> brain = entity.getBrain();
        brain.setMemory(MemoryModuleType.NEAREST_REPELLENT, this.findNearestRepellent(level, entity));


    }

    private Optional<BlockPos> findNearestRepellent(ServerLevel level, AmoebaEntity hoglin) {
        return BlockPos.findClosestMatch(hoglin.blockPosition(), 8, 4, p_186148_ -> level.getBlockState(p_186148_).is(JamiesModTag.AMOEBA_REPELLENTS));
    }
}
