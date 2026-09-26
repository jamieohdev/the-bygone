package com.jamiedev.bygone.client;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.client.renderer.LithoFluidFog;
import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class LithoClientExtensions implements IClientFluidTypeExtensions {

    private static final ResourceLocation LITHO_STILL = Bygone.id("block/litho_still");

    private static final ResourceLocation LITHO_FLOW =  Bygone.id("block/litho_flow");

    @Override
    public @NotNull ResourceLocation getStillTexture() {
        return LITHO_STILL;
    }

    @Override
    public @NotNull ResourceLocation getFlowingTexture() {
        return LITHO_FLOW;
    }

    @Override
    public @NotNull Vector3f modifyFogColor(Camera camera, float partialTicks, ClientLevel level, int renderDistance, float darkenWorldAmount, Vector3f fluidFogColor) {
        return LithoFluidFog.lithoFogColor;
    }

    @Override
    public void modifyFogRender(Camera camera, FogRenderer.FogMode mode, float renderDistance, float partialTicks, float nearDistance, float farDistance, FogShape shape) {
        RenderSystem.setShaderFogStart(LithoFluidFog.lithoFogStart);
        RenderSystem.setShaderFogEnd(LithoFluidFog.lithoFogEnd);
    }
}