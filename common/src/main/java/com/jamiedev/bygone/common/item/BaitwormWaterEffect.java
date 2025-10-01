package com.jamiedev.bygone.common.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
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
            int y = level.random.nextInt(radius * 2 + 1) - radius;
            int z = level.random.nextInt(radius * 2 + 1) - radius;
            
            BlockPos pos = centerPos.offset(x, y, z);
            if (level.getFluidState(pos).is(FluidTags.WATER)) {
                level.sendParticles(
                    ParticleTypes.FISHING,
                    pos.getX() + 0.5,
                    pos.getY() + 0.5,
                    pos.getZ() + 0.5,
                    1,
                    level.random.nextDouble() * 0.5 - 0.25,
                    level.random.nextDouble() * 0.5 - 0.25,
                    level.random.nextDouble() * 0.5 - 0.25,
                    0.1
                );
            }
        }
    }
    
    private static class EffectData {
        final int radius;
        final int duration;
        final long startTime;
        
        EffectData(int radius, int duration, long startTime) {
            this.radius = radius;
            this.duration = duration;
            this.startTime = startTime;
        }
    }
}