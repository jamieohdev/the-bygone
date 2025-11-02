package com.jamiedev.bygone.common.entity.ai;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.jamiedev.bygone.common.entity.PestEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class PestSensor extends Sensor<LivingEntity> {
    private static Optional<BlockPos> findNearestRepellent(ServerLevel level, LivingEntity livingEntity) {
        return BlockPos.findClosestMatch(livingEntity.blockPosition(), 8, 4, p_186160_ -> isValidRepellent(level, p_186160_));
    }

    private static boolean isValidRepellent(ServerLevel level, BlockPos pos) {
        BlockState blockstate = level.getBlockState(pos);
        boolean flag = blockstate.is(BlockTags.DOORS);
        return flag && blockstate.is(Blocks.GLOWSTONE) ? CampfireBlock.isLitCampfire(blockstate) : flag;
    }

    @Override
    protected void doTick(ServerLevel level, LivingEntity entity) {
        Brain<?> brain = entity.getBrain();
        brain.setMemory(MemoryModuleType.NEAREST_REPELLENT, findNearestRepellent(level, entity));
        Optional<Mob> optional = Optional.empty();
        Optional<PestEntity> optional3 = Optional.empty();
        Optional<LivingEntity> optional4 = Optional.empty();
        Optional<Player> optional5 = Optional.empty();
        Optional<Player> optional6 = Optional.empty();

        List<PestEntity> list = Lists.newArrayList();
        List<PestEntity> list1 = Lists.newArrayList();
        NearestVisibleLivingEntities nearestvisiblelivingentities = brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES)
                .orElse(NearestVisibleLivingEntities.empty());

        for (LivingEntity livingentity : nearestvisiblelivingentities.findAll(p_186157_ -> true)) {
            if (livingentity instanceof PestEntity PestEntity) {
                if (PestEntity.isBaby() && optional3.isEmpty()) {
                    optional3 = Optional.of(PestEntity);
                } else if (!PestEntity.isBaby()) {
                    list.add(PestEntity);
                }
            } else if (livingentity instanceof Player player) {
                if (optional5.isEmpty() && entity.canAttack(livingentity)) {
                    optional5 = Optional.of(player);
                }

            } else {
                optional = Optional.of((Mob) livingentity);
            }
        }

        for (LivingEntity livingentity1 : brain.getMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES).orElse(ImmutableList.of())) {
            if (livingentity1 instanceof PestEntity abstractPestEntity) {
                if (!abstractPestEntity.isBaby()) {
                    list1.add(abstractPestEntity);
                }
            }
        }
    }

    @Override
    public Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.of(
                MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES,
                MemoryModuleType.NEAREST_LIVING_ENTITIES,
                MemoryModuleType.NEAREST_REPELLENT
        );
    }
}
