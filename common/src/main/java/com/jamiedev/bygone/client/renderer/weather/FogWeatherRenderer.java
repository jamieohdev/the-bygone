package com.jamiedev.bygone.client.renderer.weather;

import com.jamiedev.bygone.common.weather.InvertedHeightmap;
import com.jamiedev.bygone.common.weather.weather_types.FogWeatherEvent;
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

public class FogWeatherRenderer extends WeatherRenderer<FogWeatherEvent> {
    public FogWeatherRenderer(FogWeatherEvent instance) { super(instance); }

    @Override public void tick(Level level) {}
    @Override public void render(Level level, LightTexture lightTexture, float partialTick, double camX, double camY, double camZ) {}
}
