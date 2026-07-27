package com.jamiedev.bygone.client.renderer.effect;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.client.renderer.weather.HauntingsRenderer;
import com.jamiedev.bygone.common.weather.BygoneWeather;
import com.jamiedev.bygone.common.weather.weather_types.HauntingsEvent;
import com.jamiedev.bygone.core.registry.BGDimensions;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.joml.Vector3f;

import java.io.IOException;
import java.util.Optional;

public class HauntingsEffectRenderer {
    public static HauntingsEffectRenderer INSTANCE = new HauntingsEffectRenderer();
    public static HauntingsEffectRenderer getInstance() { return INSTANCE; }

    public static ResourceLocation HAUNTINGS_SHADER = Bygone.id("shaders/post/hauntings.json");
    public PostChain postChain;
    public void setupPostChain() throws IOException {
        Minecraft minecraft = Minecraft.getInstance();
        postChain = new PostChain(
            minecraft.getTextureManager(), minecraft.getResourceManager(),
            minecraft.getMainRenderTarget(), HAUNTINGS_SHADER
        );
        postChain.resize(minecraft.getWindow().getWidth(), minecraft.getWindow().getHeight());
    }

    public float progress = 0.0f;
    // forgot to write this earlier but thank u  cappin
    public float modifyAmbientLightFactor(float ambientLight) {
        float darkeningAmount = 1 - this.progress;
        return ambientLight * darkeningAmount;
    }
    public void modifySkyLightColor(Vector3f color, int skyLightLevel) {
        color.mul(1 - this.progress);
    }
    private static final int LIGHT_LEVEL = 12;
    public void modifyBlockLightColor(Vector3f color, int blockLightLevel) {
        if (this.progress <= 0) return;
        if (blockLightLevel < LIGHT_LEVEL) {
            float factor = Mth.map(blockLightLevel, 0, LIGHT_LEVEL, 0, 1);
            factor = (float) Math.pow(factor, Mth.lerp(this.progress, 1, 5));
            color.mul(factor);
        }
    }

    private float width = 0.0f;
    private float height = 0.0f;

    // just so the game doesnt spam error logs but hopefully it never fails
    // normally not necessary but since fabric doesnt have a registry
    // point for postchain afaik the code tries running every render frame
    private boolean failed = false;
    void resizePostChain(Window window) {
        postChain.resize(window.getWidth(), window.getHeight());
        width = window.getWidth();
        height = window.getHeight();
    }
    // this might be redundant but also needs to exist to set it up in fabric
    public void resizeOrCreate() {
        Window window = Minecraft.getInstance().getWindow();
        if (postChain == null) {
            try {
                setupPostChain();
                resizePostChain(window);
            } catch (IOException e) {
                if (!failed) Bygone.LOGGER.warn("Failed to load shader: {}", HAUNTINGS_SHADER, e);
                failed = true;
            }
        } else if (window.getWidth() != width || window.getHeight() != height)
            resizePostChain(window);
    }

    public void render(Minecraft minecraft, float partialTicks) {
        // oops lmao
        if (minecraft.level == null || !minecraft.level.dimension().equals(BGDimensions.BYGONE_LEVEL_KEY)) return;
        BygoneWeather.Client clientWeather = BygoneWeather.Client.getInstance();
        // ough lmao
        Optional<HauntingsRenderer> renderer = clientWeather.stream().filter(HauntingsRenderer.class::isInstance)
            .map(HauntingsRenderer.class::cast).findFirst();
        if (renderer.isEmpty()) return;
        HauntingsEvent hauntingsEvent = renderer.get().getWeatherInstance();
        boolean enabled = hauntingsEvent.isActive();

        float tickDivide = 100f;
        float progressAmount = (((enabled ? 1 : -1) * partialTicks) / tickDivide);
        progress = Mth.clamp(progress + progressAmount, 0, 1);

        if (progress <= 0.0f) return;

        // do it after this point so it doesnt idly crash in case things go really wrong
        resizeOrCreate();
        if (postChain == null) return;

        float fakeProgress = Math.max(progress, 0.01f);
        postChain.setUniform("DarknessStrength", fakeProgress * .8f);
        postChain.setUniform("CutThroughStrength", 1f - (fakeProgress * fakeProgress * .8f));
        postChain.setUniform("EdgeStrength", 0.25f * fakeProgress);
        postChain.setUniform("DistanceScale", 0f * fakeProgress);
        postChain.setUniform("MinEdgePixels", 1.75f);

        postChain.process(partialTicks);

        minecraft.getMainRenderTarget().bindWrite(true);
    }
}
