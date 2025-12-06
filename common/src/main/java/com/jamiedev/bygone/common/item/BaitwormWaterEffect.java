package com.jamiedev.bygone.common.item;

import com.jamiedev.bygone.core.registry.BGParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class BaitwormWaterEffect {

    private static final Map<ServerLevel, Map<BlockPos, EffectData>> ACTIVE_EFFECTS = new HashMap<>();

    public static void addEffect(ServerLevel level, BlockPos pos, int radius, int duration) {
        ACTIVE_EFFECTS.computeIfAbsent(level, k -> new HashMap<>())
                .put(pos, new EffectData(radius, duration, level.getGameTime()));
    }

    public static boolean isInBaitwormWater(ServerLevel level, BlockPos pos) {
        Map<BlockPos, EffectData> levelEffects = ACTIVE_EFFECTS.get(level);
        if (levelEffects == null) return false;

        long currentTime = level.getGameTime();

        for (Map.Entry<BlockPos, EffectData> entry : levelEffects.entrySet()) {
            BlockPos centerPos = entry.getKey();
            EffectData data = entry.getValue();

            if (currentTime - data.startTime > data.duration) {
                continue;
            }

            if (pos.distSqr(centerPos) <= data.radius * data.radius) {
                if (level.getFluidState(pos).is(FluidTags.WATER)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static void tick(ServerLevel level) {
        Map<BlockPos, EffectData> levelEffects = ACTIVE_EFFECTS.get(level);
        if (levelEffects == null) return;

        long currentTime = level.getGameTime();
        Iterator<Map.Entry<BlockPos, EffectData>> iterator = levelEffects.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<BlockPos, EffectData> entry = iterator.next();
            BlockPos centerPos = entry.getKey();
            EffectData data = entry.getValue();

            if (currentTime - data.startTime > data.duration) {
                iterator.remove();
            } else {
                if (currentTime % 5 == 0) {
                    spawnParticles(level, centerPos, data.radius);
                }
            }
        }

        if (levelEffects.isEmpty()) {
            ACTIVE_EFFECTS.remove(level);
        }
    }

    private static void spawnParticles(ServerLevel level, BlockPos centerPos, int radius) {
        for (int i = 0; i < 3; i++) {
            int x = level.random.nextInt(radius * 2 + 1) - radius;
            int z = level.random.nextInt(radius * 2 + 1) - radius;

            BlockPos surfacePos = centerPos.offset(x, 0, z);

            if (level.getFluidState(surfacePos).is(FluidTags.WATER) && !level.getFluidState(surfacePos.above()).is(FluidTags.WATER)) {
                level.sendParticles(
                        (SimpleParticleType) BGParticleTypes.WORM,
                        surfacePos.getX() + 0.5,
                        surfacePos.getY() + 0.5,
                        surfacePos.getZ() + 0.5,
                        2,
                        level.random.nextDouble() * 0.3 - 0.15,
                        0.05,
                        level.random.nextDouble() * 0.3 - 0.15,
                        0.05
                );
            }
        }
    }

    private record EffectData(int radius, int duration, long startTime) {
    }
}