package com.jamiedev.bygone.client.renderer.effect;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.client.renderer.weather.HauntingsRenderer;
import com.jamiedev.bygone.common.weather.BygoneWeather;
import com.jamiedev.bygone.common.weather.weather_types.HauntingsEvent;
import com.jamiedev.bygone.core.registry.BGDimensions;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.shaders.Uniform;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceProvider;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.io.IOException;
import java.util.Optional;

public class FogEffectRenderer implements AutoCloseable {
    private static FogEffectRenderer INSTANCE;
    public static FogEffectRenderer getInstance() {
        if (INSTANCE == null) INSTANCE = new FogEffectRenderer();
        return INSTANCE;
    }

    public static ResourceLocation FOG_SHADER = Bygone.id("void_fog");
    private static ShaderInstance shaderInstance;

    public static float getDefaultFogMultiplier() { return 2.75f; }

    private boolean failed = false;
    public void accept(ShaderInstance loadedShader) {
        RenderSystem.assertOnRenderThread();
        try {
            shaderInstance = loadedShader;
            this.initUniforms();
        } catch (RuntimeException e) {
            if (!failed) Bygone.LOGGER.warn("Failed to load shader: {}", FOG_SHADER, e);
            failed = true;
        }
    }

    @Override
    public void close() throws Exception {
        if (fullscreenQuad != null) {
            fullscreenQuad.close();
            fullscreenQuad = null;
        }
        if (depthTarget != null) {
            depthTarget.destroyBuffers();
            depthTarget = null;
        }
        INSTANCE = null;
    }

    private float intensity = 1.0f;
    public void updateFogIntensity(float intensity, boolean forced) {
        this.intensity = intensity;
        if (forced) progress = intensity;
    }

    // to intensity
    private float progress = 0.0f;
    private static final float PROGRESS_SPEED = 0.01f;

    private VertexBuffer fullscreenQuad;
    public void bindFullscreenQuad() {
        if (fullscreenQuad == null) {
            fullscreenQuad = new VertexBuffer(VertexBuffer.Usage.STATIC);
            BufferBuilder builder = Tesselator.getInstance().begin(
                VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION
            );

            builder.addVertex(0.0F, 0.0F, 0.0F);
            builder.addVertex(1.0F, 0.0f, 0.0F);
            builder.addVertex(1.0F, 1.0F, 0.0F);
            builder.addVertex(0.0F, 1.0F, 0.0F);

            fullscreenQuad.bind();
            fullscreenQuad.upload(builder.buildOrThrow());
            VertexBuffer.unbind();
        } else fullscreenQuad.bind();
    }

    private TextureTarget depthTarget;
    private TextureTarget getDepthTarget(RenderTarget renderTarget) {
        // why is everything a neoforge thing
//        boolean stencil = renderTarget.isStencilEnabled();
        if (depthTarget == null)
            depthTarget = new TextureTarget(renderTarget.width, renderTarget.height, true, Minecraft.ON_OSX);
        else if (depthTarget.width != renderTarget.width || depthTarget.height != renderTarget.height)
            depthTarget.resize(renderTarget.width, renderTarget.height, Minecraft.ON_OSX);
        depthTarget.copyDepthFrom(renderTarget);
        renderTarget.bindWrite(true);
        return depthTarget;
    }

    private Uniform screenToView;
    private Uniform viewToWorld;

    private Uniform noiseStart;
    private Uniform renderDistance;

    private Uniform cameraY;
    private Uniform voidY;

    private Uniform intensityValue;
    private Uniform anisotropyValue;

    private Uniform fadeFactor;
    private Uniform fadeHeight;

    private void initUniforms() {
        screenToView = shaderInstance.getUniform("ScreenToView");
        viewToWorld = shaderInstance.getUniform("ViewToWorld");

        noiseStart = shaderInstance.getUniform("NoiseStart");
        renderDistance = shaderInstance.getUniform("RenderDistance");

        cameraY = shaderInstance.getUniform("CameraPosition");
        voidY = shaderInstance.getUniform("VoidPosition");

        intensityValue = shaderInstance.getUniform("Intensity");
        anisotropyValue = shaderInstance.getUniform("Anisotropy");

        fadeFactor = shaderInstance.getUniform("FadeFactor");
        fadeHeight = shaderInstance.getUniform("FadeHeight");
    }

    private int lastDepthTexture = -1;
    public void render(
        Minecraft minecraft, Matrix4f projectionMatrix,
        Camera camera, float partialTicks
    ) {
        assert minecraft.level != null;
        progress = Mth.approach(progress, intensity, (partialTicks * PROGRESS_SPEED));

        RenderTarget target = minecraft.getMainRenderTarget();
        RenderTarget depthTarget = getDepthTarget(target);
        int depthTexture = depthTarget.getDepthTextureId();
        if (lastDepthTexture != depthTexture) {
            shaderInstance.setSampler("DepthSampler", depthTexture);
            lastDepthTexture = depthTexture;
        }

        screenToView.set(new Matrix4f(projectionMatrix).invert());
        viewToWorld.set(new Matrix4f().rotation(camera.rotation()));
        renderDistance.set(minecraft.options.getEffectiveRenderDistance() * 16f);

        // noise
        Vec3 cameraPosition = camera.getPosition();

        double scale = 0.01d;
        double timeSeconds = (minecraft.level.getGameTime() + (double) partialTicks) * 2f;
        double baseX = scale * (cameraPosition.x + 0.37 * cameraPosition.y - 0.37 * timeSeconds);
        double baseY = scale * (cameraPosition.z - 0.23 * cameraPosition.y + 0.23 * timeSeconds);

        noiseStart.set((float) baseX, (float) baseY);

        //
        cameraY.set((float) cameraPosition.y);

        voidY.set((float) minecraft.level.getMinBuildHeight() - 32f);
        fadeHeight.set(minecraft.level.getHeight() * (2f / 3f));
        fadeFactor.set(0.018f);

        intensityValue.set(progress);
        anisotropyValue.set(0.6f);

        // rendering
        target.bindWrite(true);

        RenderSystem.disableScissor();
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.disableCull();

        this.bindFullscreenQuad();
        shaderInstance.apply();

        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(
            GlStateManager.SourceFactor.SRC_ALPHA,
            GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
            GlStateManager.SourceFactor.ZERO,
            GlStateManager.DestFactor.ONE
        );
        fullscreenQuad.draw();
        RenderSystem.defaultBlendFunc();

        shaderInstance.clear();

        VertexBuffer.unbind();
        target.bindWrite(true);

    }

}
