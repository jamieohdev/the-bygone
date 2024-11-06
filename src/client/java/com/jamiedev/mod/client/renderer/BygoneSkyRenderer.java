package com.jamiedev.mod.client.renderer;

import com.jamiedev.mod.JamiesMod;
import com.jamiedev.mod.client.JamiesModClient;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.DimensionRenderingRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.block.enums.CameraSubmersionType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector3d;
import org.joml.Vector4f;

@Environment(EnvType.CLIENT)
public enum BygoneSkyRenderer implements DimensionRenderingRegistry.SkyRenderer
{
    INSTANCE;

    @FunctionalInterface
    interface BufferFunction {
        void make(BufferBuilder bufferBuilder, double minSize, double maxSize, int count, long seed);
    }
    private static Tessellator tessellator;
    private static final Identifier END_SKY = JamiesMod.getModId("textures/environment/bygone_sky.png");

 //   public BygoneSkyRenderer() {}
 @Nullable
 private VertexBuffer lightSkyBuffer;
    @Override
    public void render(WorldRenderContext context)
    {
        Matrix4f matrix4f = new Matrix4f();
        MatrixStack matrixStack = new MatrixStack();
        matrixStack.multiplyPositionMatrix(matrix4f);
        renderLightSky();
        //renderSkybox(matrixStack);
    }
    private static BuiltBuffer buildSkyBuffer(Tessellator tessellator, float f) {
        float g = Math.signum(f) * 512.0F;
        float h = 512.0F;
        BufferBuilder bufferBuilder = tessellator.begin(VertexFormat.DrawMode.TRIANGLE_FAN, VertexFormats.POSITION);
        bufferBuilder.vertex(0.0F, f, 0.0F);

        for(int i = -180; i <= 180; i += 45) {
            bufferBuilder.vertex(g * MathHelper.cos((float)i * 0.017453292F), f, 512.0F * MathHelper.sin((float)i * 0.017453292F));
        }

        return bufferBuilder.end();
    }

    private void renderLightSky() {
        if (this.lightSkyBuffer != null) {
            this.lightSkyBuffer.close();
        }

        this.lightSkyBuffer = new VertexBuffer(VertexBuffer.Usage.STATIC);
        this.lightSkyBuffer.bind();
        this.lightSkyBuffer.upload(buildSkyBuffer(Tessellator.getInstance(), 16.0F));
        VertexBuffer.unbind();
    }

    public void renderSkybox(MatrixStack matrices) {
        RenderSystem.enableBlend();
        RenderSystem.depthMask(false);
        RenderSystem.setShader(GameRenderer::getPositionTexColorProgram);
        RenderSystem.setShaderTexture(0, END_SKY);
        Tessellator tessellator = Tessellator.getInstance();

        for(int i = 0; i < 6; ++i) {
            assert matrices != null;
            matrices.push();
            if (i == 1) {
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90.0F));
            }

            if (i == 2) {
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-90.0F));
            }

            if (i == 3) {
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180.0F));
            }

            if (i == 4) {
                matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(90.0F));
            }

            if (i == 5) {
                matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-90.0F));
            }

            Matrix4f matrix4f = matrices.peek().getPositionMatrix();
            BufferBuilder bufferBuilder = tessellator.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
            bufferBuilder.vertex(matrix4f, -100.0F, -100.0F, -100.0F).texture(0.0F, 0.0F).color(-14145496);
            bufferBuilder.vertex(matrix4f, -100.0F, -100.0F, 100.0F).texture(0.0F, 16.0F).color(-14145496);
            bufferBuilder.vertex(matrix4f, 100.0F, -100.0F, 100.0F).texture(16.0F, 16.0F).color(-14145496);
            bufferBuilder.vertex(matrix4f, 100.0F, -100.0F, -100.0F).texture(16.0F, 0.0F).color(-14145496);
            BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
            matrices.pop();
        }

        RenderSystem.depthMask(true);
        RenderSystem.disableBlend();
    }
}
