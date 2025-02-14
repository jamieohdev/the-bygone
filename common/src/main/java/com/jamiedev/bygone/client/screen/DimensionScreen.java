package com.jamiedev.bygone.client.screen;

import com.jamiedev.bygone.Bygone;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.ReceivingLevelScreen;

import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class DimensionScreen {
    private static final Component DOWNLOADING_BYGONE_TERRAIN_TEXT  = Component.translatable("system.bygone.entering_into_bygone");
    private static final ResourceLocation BACKGROUND_LOCATION = Bygone.id( "bg.png");

    public static void renderScreenAndText(ReceivingLevelScreen screen, GuiGraphics guiGraphics) {
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);

        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        RenderSystem.setShaderTexture(0, BACKGROUND_LOCATION);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        bufferbuilder.addVertex(0.0F, screen.height, 0.0F).setUv(0.0F, (float)screen.height / 32.0F).setColor(64, 64, 64, 255);
        bufferbuilder.addVertex(screen.width, screen.height, 0.0F).setUv((float)screen.width / 32.0F, (float)screen.height / 32.0F).setColor(64, 64, 64, 255);
        bufferbuilder.addVertex(screen.width, 0.0F, 0.0F).setUv((float)screen.width / 32.0F, 0.0f).setColor(64, 64, 64, 255);
        bufferbuilder.addVertex(0.0F, 0.0F, 0.0F).setUv(0.0F, 0.0f).setColor(64, 64, 64, 255);
        BufferUploader.draw(bufferbuilder.buildOrThrow());

        guiGraphics.drawCenteredString(Minecraft.getInstance().font, DOWNLOADING_BYGONE_TERRAIN_TEXT, screen.width / 2 + 1, screen.height / 2 - 9, 0);
        guiGraphics.drawCenteredString(Minecraft.getInstance().font, DOWNLOADING_BYGONE_TERRAIN_TEXT, screen.width / 2, screen.height / 2 - 10, 0xfff3e8);
    }
}
