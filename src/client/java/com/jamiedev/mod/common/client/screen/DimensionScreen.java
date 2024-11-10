package com.jamiedev.mod.common.client.screen;

import com.jamiedev.mod.common.JamiesMod;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import net.minecraft.client.render.*;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class DimensionScreen {
    private static final Text DOWNLOADING_BYGONE_TERRAIN_TEXT  = Text.translatable("system.bygone.entering_into_bygone");
    private static final Identifier BACKGROUND_LOCATION = JamiesMod.getModId( "bg.png");

    public static void renderScreenAndText(DownloadingTerrainScreen screen, DrawContext guiGraphics) {
        Tessellator tesselator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tesselator.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);

        RenderSystem.setShader(GameRenderer::getPositionTexColorProgram);
        RenderSystem.setShaderTexture(0, BACKGROUND_LOCATION);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        bufferbuilder.vertex(0.0F, screen.height, 0.0F).texture(0.0F, (float)screen.height / 32.0F).color(64, 64, 64, 255);
        bufferbuilder.vertex(screen.width, screen.height, 0.0F).texture((float)screen.width / 32.0F, (float)screen.height / 32.0F).color(64, 64, 64, 255);
        bufferbuilder.vertex(screen.width, 0.0F, 0.0F).texture((float)screen.width / 32.0F, 0.0f).color(64, 64, 64, 255);
        bufferbuilder.vertex(0.0F, 0.0F, 0.0F).texture(0.0F, 0.0f).color(64, 64, 64, 255);
        BufferRenderer.draw(bufferbuilder.end());

        guiGraphics.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer, DOWNLOADING_BYGONE_TERRAIN_TEXT, screen.width / 2 + 1, screen.height / 2 - 9, 0);
        guiGraphics.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer, DOWNLOADING_BYGONE_TERRAIN_TEXT, screen.width / 2, screen.height / 2 - 10, 16774120);
    }
}
