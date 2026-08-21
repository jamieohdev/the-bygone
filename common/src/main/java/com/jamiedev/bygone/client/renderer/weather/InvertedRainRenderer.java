package com.jamiedev.bygone.client.renderer.weather;

import com.jamiedev.bygone.common.weather.BygoneWeather;
import com.jamiedev.bygone.common.weather.InvertedHeightmap;
import com.jamiedev.bygone.common.weather.weather_types.InvertedRain;
import com.jamiedev.bygone.core.extension.LevelChunkExtension;
import com.jamiedev.bygone.core.registry.BGSoundEvents;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.ParticleStatus;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.client.Camera;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.joml.Matrix4f;

import static net.minecraft.client.renderer.LevelRenderer.getLightColor;

public class InvertedRainRenderer extends WeatherRenderer<InvertedRain> {

    private static final ResourceLocation RAIN_LOCATION = ResourceLocation.withDefaultNamespace("textures/environment/rain.png");

    // From LevelRenderer
    private final float[] rainSizeX;
    private final float[] rainSizeZ;
    public InvertedRainRenderer(InvertedRain weatherInstance) {
        super(weatherInstance);

        this.rainSizeX = new float[1024];
        this.rainSizeZ = new float[1024];

        for(int i = 0; i < 32; ++i) {
            for(int j = 0; j < 32; ++j) {
                float f = (float)(j - 16);
                float f1 = (float)(i - 16);
                float f2 = Mth.sqrt(f * f + f1 * f1);
                this.rainSizeX[i << 5 | j] = -f1 / f2;
                this.rainSizeZ[i << 5 | j] = f / f2;
            }
        }
    }

    private float getRainLevel() {
        return this.instance.getRainAmount();
    }

    private boolean isRainParticle(FluidState fluidState, BlockState blockState) {
        return !fluidState.is(FluidTags.LAVA) && !blockState.is(Blocks.MAGMA_BLOCK) && !CampfireBlock.isLitCampfire(blockState);
    }

    private int time = 0;
    private int rainSoundTime = 0;

    @Override public void tick(Level level) {
        time++;

        Minecraft minecraft = Minecraft.getInstance();
        Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
        float f = getRainLevel() / (Minecraft.useFancyGraphics() ? 1.0F : 2.0F);
        if (f <= 0.0F) return;

        RandomSource randomsource = RandomSource.create((long) this.time * 312987231L);
        BlockPos blockpos = BlockPos.containing(camera.getPosition());
        BlockPos blockpos1 = null;
        int i = (int) (100.0F * f * f) / (minecraft.options.particles().get() == ParticleStatus.DECREASED ? 2 : 1);

        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        for (int j = 0; j < i; ++j) {
            int k = randomsource.nextInt(21) - 10;
            int l = randomsource.nextInt(21) - 10;

            BlockPos offsetBlockPos = blockpos.offset(k, 0, l);
            ChunkAccess chunkAccess = level.getChunk(offsetBlockPos);
            if (chunkAccess instanceof LevelChunk levelChunk) {
                InvertedHeightmap invertedHeightmap = ((LevelChunkExtension) levelChunk).bygone$getInvertedHeightmap();
                if (invertedHeightmap == null) return;

                mutableBlockPos.set(
                    offsetBlockPos.getX(),
                    invertedHeightmap.getHeight(offsetBlockPos.getX(), offsetBlockPos.getZ()) + 1,
                    offsetBlockPos.getZ()
                );

                if (!level.getBiome(mutableBlockPos).value().hasPrecipitation())
                    continue;

                if (mutableBlockPos.getY() < level.getMaxBuildHeight()
                && (mutableBlockPos.getY() <= blockpos.getY() + 30
                && mutableBlockPos.getY() >= blockpos.getY() - 5)) {
                    blockpos1 = mutableBlockPos;
                    if (minecraft.options.particles().get() == ParticleStatus.MINIMAL)
                        break;

                    double d0 = randomsource.nextDouble();
                    double d1 = randomsource.nextDouble();

                    BlockState blockstate = level.getBlockState(blockpos1);
                    if (blockstate.is(Blocks.AIR)) continue;

                    FluidState fluidstate = level.getFluidState(blockpos1);
                    ParticleOptions particleoptions = isRainParticle(fluidstate, blockstate) ? ParticleTypes.RAIN : ParticleTypes.SMOKE;
                    level.addParticle(particleoptions,
                        blockpos1.getX() + d0,
                        blockpos1.getY() - 0.1,
                        blockpos1.getZ() + d1,
                        0.0F, 0.0F, 0.0F
                    );
                }
            }
        }
        if (blockpos1 != null && randomsource.nextInt(3) < this.rainSoundTime++) {
            this.rainSoundTime = 0;
            level.playLocalSound(blockpos1, BGSoundEvents.WEATHER_INVERTED_RAIN_EVENT, SoundSource.WEATHER, 0.2F, 1.0F, false);
        }
    }

    @Override public void render(Level level, LightTexture lightTexture, float partialTick, double camX, double camY, double camZ) {
        if (instance == null) return;

        lightTexture.turnOnLightLayer();

        RenderSystem.disableCull();
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        RenderSystem.depthMask(Minecraft.useShaderTransparency());

        RenderSystem.setShader(GameRenderer::getParticleShader);

        iterateAndRender(
            level,
            (int) Math.floor(camX),
            (int) Math.floor(camY),
            (int) Math.floor(camZ),
            camX, camY, camZ,
            partialTick
        );

        RenderSystem.enableCull();
        RenderSystem.disableBlend();
        lightTexture.turnOffLightLayer();
    }

    public static void debugLineRender(Vec3 start, Vec3 end) {
        Minecraft mc = Minecraft.getInstance();
        Camera camera = mc.gameRenderer.getMainCamera();

        PoseStack pose = new PoseStack();
        Vec3 cam = camera.getPosition();

        pose.pushPose();
        pose.translate(-cam.x, -cam.y, -cam.z);

        Matrix4f tempPose = pose.last().pose();

        VertexConsumer vertexConsumer = mc.renderBuffers().bufferSource()
            .getBuffer(RenderType.lines());

        vertexConsumer.addVertex(tempPose,
                (float)(start.x),
                (float)(start.y),
                (float)(start.z))
            .setColor(255, 0, 0, 255)
            .setNormal(0, 1, 0);

        vertexConsumer.addVertex(tempPose,
                (float)(end.x),
                (float)(end.y),
                (float)(end.z))
            .setColor(255, 0, 0, 255)
            .setNormal(0, 1, 0);

        pose.popPose();
    }

    private void iterateAndRender(
        Level level, int i, int j, int k,
        double camX, double camY, double camZ,
        float partialTick
    ) {
        float rainLevel = getRainLevel();
        if (rainLevel <= .0f) return;

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferBuilder = null;

        int rainDensity = 5;
        if (Minecraft.useFancyGraphics()) rainDensity = 10;

        boolean render = false;
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();

        for(int j1 = k - rainDensity; j1 <= k + rainDensity; ++j1) {
            for (int k1 = i - rainDensity; k1 <= i + rainDensity; ++k1) {
                int l1 = (j1 - k + 16) * 32 + k1 - i + 16;
                double d0 = this.rainSizeX[l1] * 0.5D;
                double d1 = this.rainSizeZ[l1] * 0.5D;
                mutableBlockPos.set(k1, camY, j1);

                Biome biome = level.getBiome(mutableBlockPos).value();
                if (!biome.hasPrecipitation()) continue;

                LevelChunk levelChunk = level.getChunkAt(mutableBlockPos);
                InvertedHeightmap invertedHeightMap = ((LevelChunkExtension) levelChunk).bygone$getInvertedHeightmap();

//                debugLineRender(new Vec3(k1, -64, j1), new Vec3(k1, invertedHeightMap.getHeight(k1, j1), j1));
                if (invertedHeightMap.dirty) invertedHeightMap.primeSelf();
                int i2 = invertedHeightMap.getHeight(k1, j1) + 1;

                int j2 = j - rainDensity;
                int k2 = j + rainDensity;
                if (j2 > i2) j2 = i2;

                if (k2 > i2) k2 = i2;

                int l2 = i2;
                if (i2 > j) l2 = j;

                if (j2 != k2) {
                    RandomSource randomsource = RandomSource.create((k1 * k1 * 3121L + k1 * 45238971L ^ j1 * j1 * 418711L + j1 * 13761L));
                    mutableBlockPos.set(k1, j2, j1);

                    int i3 = (time & 131071);
                    int j3 = k1 * k1 * 3121 + k1 * 45238971 + j1 * j1 * 418711 + j1 * 13761 & 255;
                    float f2 = 3.0F + randomsource.nextFloat();
                    float f3 = ((float)(i3 + j3) + partialTick) / 32.0F * f2;
                    float f4 = f3 % 32.0F;
                    double d2 = (double) k1 + (double) 0.5F - camX;
                    double d3 = (double) j1 + (double) 0.5F - camZ;
                    float f6 = (float) Math.sqrt(d2 * d2 + d3 * d3) / (float) rainDensity;
                    float f7 = ((1.0F - f6 * f6) * 0.5F + 0.5F) * rainLevel;
                    mutableBlockPos.set(k1, l2, j1);
                    int k3 = getLightColor(level, mutableBlockPos);

                    if (!render) {
                        RenderSystem.setShaderTexture(0, RAIN_LOCATION);
                        bufferBuilder = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
                    }

                    bufferBuilder.addVertex((float)(k1 - camX - d0 + 0.5D), (float)(k2 - camY), (float)(j1 - camZ - d1 + 0.5D))
                        .setUv(0.0F, j2 * 0.25F + f4).setColor(1.0F, 1.0F, 1.0F, f7).setLight(k3);
                    bufferBuilder.addVertex((float)(k1 - camX + d0 + 0.5D), (float)(k2 - camY), (float)(j1 - camZ + d1 + 0.5D))
                        .setUv(1.0F, j2 * 0.25F + f4).setColor(1.0F, 1.0F, 1.0F, f7).setLight(k3);
                    bufferBuilder.addVertex((float)(k1 - camX + d0 + 0.5D), (float)(j2 - camY), (float)(j1 - camZ + d1 + 0.5D))
                        .setUv(1.0F, k2 * 0.25F + f4).setColor(1.0F, 1.0F, 1.0F, f7).setLight(k3);
                    bufferBuilder.addVertex((float)(k1 - camX - d0 + 0.5D), (float)(j2 - camY), (float)(j1 - camZ - d1 + 0.5D))
                        .setUv(0.0F, k2 * 0.25F + f4).setColor(1.0F, 1.0F, 1.0F, f7).setLight(k3);
                    render = true;
                }
            }
        }

        if (render) BufferUploader.drawWithShader(bufferBuilder.buildOrThrow());
    }
}
