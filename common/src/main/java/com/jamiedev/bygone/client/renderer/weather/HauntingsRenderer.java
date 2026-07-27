package com.jamiedev.bygone.client.renderer.weather;

import com.jamiedev.bygone.common.weather.InvertedHeightmap;
import com.jamiedev.bygone.common.weather.weather_types.HauntingsEvent;
import com.jamiedev.bygone.core.extension.LevelChunkExtension;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.ParticleStatus;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Optional;

public class HauntingsRenderer extends WeatherRenderer<HauntingsEvent> {
    private static final int PARTICLE_INTERVAL = 2;
    private static final int PARTICLE_HORIZONTAL_RANGE = 24;
    private static final int PARTICLE_VERTICAL_RANGE = 18;

    public HauntingsRenderer(HauntingsEvent instance) { super(instance); }

    private int time = 0;

    @Override
    public void tick(Level level) {
        if (this.instance == null || !this.instance.isActive()) return;

        time++;
        if (time % PARTICLE_INTERVAL != 0) return;

        Minecraft minecraft = Minecraft.getInstance();
        ParticleStatus particleStatus = minecraft.options.particles().get();
        if (particleStatus == ParticleStatus.MINIMAL && level.random.nextInt(3) != 0) return;

        Camera camera = minecraft.gameRenderer.getMainCamera();
        RandomSource randomsource = RandomSource.create((long) this.time * 312987231L);

        BlockPos blockPos = BlockPos.containing(camera.getPosition());
        int particleAmount = Minecraft.useFancyGraphics() ? 12 : 8;
        if (particleStatus == ParticleStatus.DECREASED) particleAmount /= 2;
        if (particleStatus == ParticleStatus.MINIMAL) return;

        for (int i = 0; i < particleAmount; ++i) {
            int x = randomsource.nextInt((PARTICLE_HORIZONTAL_RANGE * 2) + 1) - PARTICLE_HORIZONTAL_RANGE;
            int z = randomsource.nextInt((PARTICLE_HORIZONTAL_RANGE * 2) + 1) - PARTICLE_HORIZONTAL_RANGE;

            BlockPos offsetBlockPos = blockPos.offset(x, 0, z);
            ChunkAccess chunkAccess = level.getChunk(offsetBlockPos);
            if (chunkAccess instanceof LevelChunk levelChunk) {
                InvertedHeightmap invertedHeightmap = ((LevelChunkExtension) levelChunk).bygone$getInvertedHeightmap();
                if (invertedHeightmap == null) continue;
                getParticleGroundPos(level, offsetBlockPos, blockPos.getY(), randomsource)
                    .ifPresent(groundPos -> spawnHauntingParticle(level, groundPos, randomsource));
            }
        }
    }

    @Override public void render(Level level, LightTexture lightTexture, float partialTick, double camX, double camY, double camZ) {}

    private static Optional<BlockPos> getParticleGroundPos(
        Level level, BlockPos pos, int cameraY, RandomSource random
    ) {
        int minY = Math.max(level.getMinBuildHeight(), cameraY - PARTICLE_VERTICAL_RANGE);
        int maxY = Math.min(level.getMaxBuildHeight() - 1, cameraY + PARTICLE_VERTICAL_RANGE);
        if (minY >= maxY) return Optional.empty();

        BlockPos.MutableBlockPos candidate = new BlockPos.MutableBlockPos(pos.getX(), maxY, pos.getZ());
        int startY = minY + random.nextInt(maxY - minY + 1);
        int span = maxY - minY + 1;

        for (int checked = 0; checked < span; checked++) {
            candidate.setY(minY + Math.floorMod(startY - minY - checked, span));
            if (isParticleGroundPos(level, candidate)) return Optional.of(candidate.immutable());
        }
        return Optional.empty();
    }

    private static boolean isParticleGroundPos(Level level, BlockPos pos) {
        BlockPos above = pos.above();
        BlockState ground = level.getBlockState(pos);
        return ground.isFaceSturdy(level, pos, Direction.UP)
            && level.getBlockState(above).getCollisionShape(level, above).isEmpty();
    }

    private static void spawnHauntingParticle(Level level, BlockPos blockPos, RandomSource random) {
        BlockState ground = level.getBlockState(blockPos);
        VoxelShape shape = ground.getCollisionShape(level, blockPos);
        double blockTop = shape.isEmpty() ? 1.0D : shape.max(Direction.Axis.Y);

        level.addAlwaysVisibleParticle(
            ParticleTypes.SOUL,
            blockPos.getX() + random.nextDouble(),
            blockPos.getY() + blockTop + 0.02D,
            blockPos.getZ() + random.nextDouble(),
            0.0D,
            0.02D + random.nextDouble() * 0.04D,
            0.0D
        );
    }
}
