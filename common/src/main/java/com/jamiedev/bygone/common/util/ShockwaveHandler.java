package com.jamiedev.bygone.common.util;

import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ShockwaveHandler {
    private static final ConcurrentHashMap<UUID, List<ActiveShockwave>> ACTIVE_SHOCKWAVES = new ConcurrentHashMap<>();

    public static void createShockwave(ServerLevel level, Player player, SimpleParticleType particleType,
                                       float maxRadius, int durationTicks, int particleCount) {
        UUID playerId = player.getUUID();
        ActiveShockwave shockwave = new ActiveShockwave(level, player.position(), particleType,
                maxRadius, durationTicks, particleCount);

        ACTIVE_SHOCKWAVES.computeIfAbsent(playerId, k -> new ArrayList<>()).add(shockwave);
    }

    public static void tickAll() {
        ACTIVE_SHOCKWAVES.entrySet().removeIf(entry -> {
            List<ActiveShockwave> shockwaves = entry.getValue();
            shockwaves.removeIf(shockwave -> !shockwave.tick());
            return shockwaves.isEmpty();
        });
    }

    public static void clearPlayer(UUID playerId) {
        ACTIVE_SHOCKWAVES.remove(playerId);
    }

    public static class ActiveShockwave {
        private final ServerLevel level;
        private final Vec3 center;
        private final SimpleParticleType particleType;
        private final float maxRadius;
        private final int duration;
        private final int particleCount;
        private float currentRadius;
        private int ticksAlive;

        public ActiveShockwave(ServerLevel level, Vec3 center, SimpleParticleType particleType,
                               float maxRadius, int duration, int particleCount) {
            this.level = level;
            this.center = center;
            this.particleType = particleType;
            this.maxRadius = maxRadius;
            this.duration = duration;
            this.particleCount = particleCount;
            this.currentRadius = 0.5F;
            this.ticksAlive = 0;
        }

        public boolean tick() {
            ticksAlive++;

            float progress = (float) ticksAlive / duration;
            if (progress > 1.0F) {
                return false;
            }

            float easedProgress = 1.0F - (1.0F - progress) * (1.0F - progress);
            currentRadius = 0.5F + easedProgress * (maxRadius - 0.5F);

            for (int i = 0; i < particleCount; i++) {
                float angle = (float) (i * 2 * Math.PI / particleCount);
                double x = center.x + Mth.cos(angle) * currentRadius;
                double z = center.z + Mth.sin(angle) * currentRadius;
                double y = center.y + 0.5;

                x += (level.random.nextDouble() - 0.5) * 0.1;
                z += (level.random.nextDouble() - 0.5) * 0.1;
                y += (level.random.nextDouble() - 0.5) * 0.2;

                level.sendParticles(particleType, x, y, z, 1, 0, 0, 0, 0);
            }

            return true;
        }
    }
}