package com.jamiedev.bygone.client.renderer;

import com.jamiedev.bygone.Bygone;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexBuffer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.DimensionRenderingRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;

import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

@Environment(EnvType.CLIENT)
public enum BygoneSkyRenderer implements DimensionRenderingRegistry.SkyRenderer
{
    INSTANCE;

    @FunctionalInterface
    interface BufferFunction {
        void make(BufferBuilder bufferBuilder, double minSize, double maxSize, int count, long seed);
    }
    private static Tesselator tessellator;
    private static final ResourceLocation END_SKY = Bygone.id("textures/environment/bygone_sky.png");

 //   public BygoneSkyRenderer() {}
 @Nullable
 private VertexBuffer lightSkyBuffer;
    @Override
    public void render(WorldRenderContext context)
    {
        Matrix4f matrix4f = new Matrix4f();
        PoseStack matrixStack = new PoseStack();
        matrixStack.mulPose(matrix4f);
        renderLightSky();
        //renderSkybox(matrixStack);
    }
    private static MeshData buildSkyBuffer(Tesselator tessellator, float f) {
        float g = Math.signum(f) * 512.0F;
        float h = 512.0F;
        BufferBuilder bufferBuilder = tessellator.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION);
        bufferBuilder.addVertex(0.0F, f, 0.0F);

        for(int i = -180; i <= 180; i += 45) {
            bufferBuilder.addVertex(g * Mth.cos((float)i * 0.017453292F), f, 512.0F * Mth.sin((float)i * 0.017453292F));
        }

        return bufferBuilder.buildOrThrow();
    }

    private void renderLightSky() {



        PoseStack matrices = new PoseStack();
        Matrix4f matrix4f = matrices.last().pose();
        matrices.mulPose(matrix4f);
        RenderSystem.enableBlend();
        RenderSystem.depthMask(false);
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        RenderSystem.setShaderTexture(0, END_SKY);
        Tesselator tessellator = Tesselator.getInstance();

        for(int i = 0; i < 6; ++i) {
            matrices.pushPose();
            if (i == 1) {
                matrices.mulPose(Axis.XP.rotationDegrees(90.0F));
            }

            if (i == 2) {
                matrices.mulPose(Axis.XP.rotationDegrees(-90.0F));
            }

            if (i == 3) {
                matrices.mulPose(Axis.XP.rotationDegrees(180.0F));
            }

            if (i == 4) {
                matrices.mulPose(Axis.ZP.rotationDegrees(90.0F));
            }

            if (i == 5) {
                matrices.mulPose(Axis.ZP.rotationDegrees(-90.0F));
            }


            BufferBuilder bufferBuilder = tessellator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
            bufferBuilder.addVertex(matrix4f, -100.0F, -100.0F, -100.0F).setUv(0.0F, 0.0F).setColor(-14145496);
            bufferBuilder.addVertex(matrix4f, -100.0F, -100.0F, 100.0F).setUv(0.0F, 16.0F).setColor(-14145496);
            bufferBuilder.addVertex(matrix4f, 100.0F, -100.0F, 100.0F).setUv(16.0F, 16.0F).setColor(-14145496);
            bufferBuilder.addVertex(matrix4f, 100.0F, -100.0F, -100.0F).setUv(16.0F, 0.0F).setColor(-14145496);
            BufferUploader.drawWithShader(bufferBuilder.buildOrThrow());
            matrices.popPose();
        }

        RenderSystem.depthMask(true);
        RenderSystem.disableBlend();
    }

    public void renderSkybox(PoseStack matrices) {
        RenderSystem.enableBlend();
        RenderSystem.depthMask(false);
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        RenderSystem.setShaderTexture(0, END_SKY);
        Tesselator tessellator = Tesselator.getInstance();

        for(int i = 0; i < 6; ++i) {
            assert matrices != null;
            matrices.pushPose();
            if (i == 1) {
                matrices.mulPose(Axis.XP.rotationDegrees(90.0F));
            }

            if (i == 2) {
                matrices.mulPose(Axis.XP.rotationDegrees(-90.0F));
            }

            if (i == 3) {
                matrices.mulPose(Axis.XP.rotationDegrees(180.0F));
            }

            if (i == 4) {
                matrices.mulPose(Axis.ZP.rotationDegrees(90.0F));
            }

            if (i == 5) {
                matrices.mulPose(Axis.ZP.rotationDegrees(-90.0F));
            }

            Matrix4f matrix4f = matrices.last().pose();
            BufferBuilder bufferBuilder = tessellator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
            bufferBuilder.addVertex(matrix4f, -100.0F, -100.0F, -100.0F).setUv(0.0F, 0.0F).setColor(-14145496);
            bufferBuilder.addVertex(matrix4f, -100.0F, -100.0F, 100.0F).setUv(0.0F, 16.0F).setColor(-14145496);
            bufferBuilder.addVertex(matrix4f, 100.0F, -100.0F, 100.0F).setUv(16.0F, 16.0F).setColor(-14145496);
            bufferBuilder.addVertex(matrix4f, 100.0F, -100.0F, -100.0F).setUv(16.0F, 0.0F).setColor(-14145496);
            BufferUploader.drawWithShader(bufferBuilder.buildOrThrow());
            matrices.popPose();
        }

        RenderSystem.depthMask(true);
        RenderSystem.disableBlend();
    }
}
