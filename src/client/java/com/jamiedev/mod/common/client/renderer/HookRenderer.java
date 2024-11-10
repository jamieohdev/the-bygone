package com.jamiedev.mod.common.client.renderer;

import com.jamiedev.mod.common.JamiesMod;
import com.jamiedev.mod.common.entities.projectile.HookEntity;
import com.jamiedev.mod.common.init.JamiesModItems;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class HookRenderer extends EntityRenderer<HookEntity>
{
    private static final Identifier TEXTURE = JamiesMod.getModId("textures/entity/hook.png");
    private static final RenderLayer LAYER = RenderLayer.getEntityCutout(TEXTURE);
    public HookRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(HookEntity hook, float yaw, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light) {
        PlayerEntity playerOwner = hook.getPlayerOwner();
        if (playerOwner != null) {
            matrixStack.push();
            matrixStack.push();
            matrixStack.scale(0.5F, 0.5F, 0.5F);
            matrixStack.multiply(this.dispatcher.getRotation());
            MatrixStack.Entry hookEntry = matrixStack.peek();
            VertexConsumer hookBuffer = vertexConsumerProvider.getBuffer(LAYER);
            vertex(hookBuffer, hookEntry, light, 0.0F, 0, 0, 1);
            vertex(hookBuffer, hookEntry, light, 1.0F, 0, 1, 1);
            vertex(hookBuffer, hookEntry, light, 1.0F, 1, 1, 0);
            vertex(hookBuffer, hookEntry, light, 0.0F, 1, 0, 0);
            matrixStack.pop();
            float handSwingProgress = playerOwner.getHandSwingProgress(tickDelta);
            float handBob = MathHelper.sin(MathHelper.sqrt(handSwingProgress) * MathHelper.PI);
            Vec3d handPos = this.getHandPos(playerOwner, handBob, tickDelta, JamiesModItems.HOOK);
            Vec3d lerpedPos = hook.getLerpedPos(tickDelta).add(0.0, 0.25, 0.0);
            float xDiff = (float)(handPos.x - lerpedPos.x);
            float yDiff = (float)(handPos.y - lerpedPos.y);
            float zDiff = (float)(handPos.z - lerpedPos.z);
            VertexConsumer lineStripBuffer = vertexConsumerProvider.getBuffer(RenderLayer.getLineStrip());
            MatrixStack.Entry lineEntry = matrixStack.peek();

            for(int o = 0; o <= 16; ++o) {
                renderFishingLine(xDiff, yDiff, zDiff, lineStripBuffer, lineEntry, percentage(o, 16), percentage(o + 1, 16), DyeColor.BROWN.getEntityColor());
            }

            matrixStack.pop();
            super.render(hook, yaw, tickDelta, matrixStack, vertexConsumerProvider, light);
        }
    }

    private Vec3d getHandPos(PlayerEntity player, float handBob, float tickDelta, Item item) {
        int sideOffset = player.getMainArm() == Arm.RIGHT ? 1 : -1;
        ItemStack itemStack = player.getMainHandStack();
        if (!itemStack.isOf(item)) {
            sideOffset = -sideOffset;
        }

        if (this.dispatcher.gameOptions.getPerspective().isFirstPerson() && player == MinecraftClient.getInstance().player) {
            double m = 960.0 / (double) this.dispatcher.gameOptions.getFov().getValue();
            Vec3d vec3d = this.dispatcher.camera.getProjection().getPosition((float) sideOffset * 0.525F, -0.1F).multiply(m).rotateY(handBob * 0.5F).rotateX(-handBob * 0.7F);
            return player.getCameraPosVec(tickDelta).add(vec3d);
        } else {
            float lerpBodyYaw = MathHelper.lerp(tickDelta, player.prevBodyYaw, player.bodyYaw) * MathHelper.RADIANS_PER_DEGREE;
            double d = MathHelper.sin(lerpBodyYaw);
            double e = MathHelper.cos(lerpBodyYaw);
            float playerScale = player.getScale();
            double j = (double) sideOffset * 0.35 * (double) playerScale;
            double k = 0.8 * (double) playerScale;
            float yOffset = player.isInSneakingPose() ? -0.1875F : 0.0F;
            return player.getCameraPosVec(tickDelta).add(-e * j - d * k, (double) yOffset - 0.45 * (double) playerScale, -d * j + e * k);
        }
    }

    private static float percentage(int value, int max) {
        return (float)value / (float)max;
    }

    private static void vertex(VertexConsumer buffer, MatrixStack.Entry matrix, int light, float x, int y, int u, int v) {
        buffer.vertex(matrix, x - 0.5F, (float)y - 0.5F, 0.0F)
                .color(-1)
                .texture((float)u, (float)v)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(light)
                .normal(matrix, 0.0F, 1.0F, 0.0F);
    }

    private static void renderFishingLine(float xDist, float yDist, float zDist, VertexConsumer buffer, MatrixStack.Entry matrices, float segmentStart, float segmentEnd, int lineARGBColor) {
        float xStart = xDist * segmentStart;
        float yStart = yDist * (segmentStart * segmentStart + segmentStart) * 0.5F + 0.25F;
        float zStart = zDist * segmentStart;
        float xStep = xDist * segmentEnd - xStart;
        float yStep = yDist * (segmentEnd * segmentEnd + segmentEnd) * 0.5F + 0.25F - yStart;
        float zStep = zDist * segmentEnd - zStart;
        float step = MathHelper.sqrt(xStep * xStep + yStep * yStep + zStep * zStep);
        xStep /= step;
        yStep /= step;
        zStep /= step;
        buffer.vertex(matrices, xStart, yStart, zStart).color(lineARGBColor).normal(matrices, xStep, yStep, zStep);
    }


    @Override
    public Identifier getTexture(HookEntity entity) {
        return TEXTURE;
    }
}
