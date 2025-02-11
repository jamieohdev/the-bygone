package com.jamiedev.bygone.client.renderer;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.common.entities.projectile.HookEntity;
import com.jamiedev.bygone.fabric.init.JamiesModItems;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class HookRenderer extends EntityRenderer<HookEntity>
{
    private static final ResourceLocation TEXTURE = Bygone.getModId("textures/entity/hook.png");
    private static final RenderType LAYER = RenderType.entityCutout(TEXTURE);
    public HookRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(HookEntity hook, float yaw, float tickDelta, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int light) {
        Player playerOwner = hook.getPlayerOwner();
        if (playerOwner != null) {
            matrixStack.pushPose();
            matrixStack.pushPose();
            matrixStack.scale(0.5F, 0.5F, 0.5F);
            matrixStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
            PoseStack.Pose hookEntry = matrixStack.last();
            VertexConsumer hookBuffer = vertexConsumerProvider.getBuffer(LAYER);
            vertex(hookBuffer, hookEntry, light, 0.0F, 0, 0, 1);
            vertex(hookBuffer, hookEntry, light, 1.0F, 0, 1, 1);
            vertex(hookBuffer, hookEntry, light, 1.0F, 1, 1, 0);
            vertex(hookBuffer, hookEntry, light, 0.0F, 1, 0, 0);
            matrixStack.popPose();
            float handSwingProgress = playerOwner.getAttackAnim(tickDelta);
            float handBob = Mth.sin(Mth.sqrt(handSwingProgress) * Mth.PI);
            Vec3 handPos = this.getHandPos(playerOwner, handBob, tickDelta, JamiesModItems.HOOK);
            Vec3 lerpedPos = hook.getPosition(tickDelta).add(0.0, 0.25, 0.0);
            float xDiff = (float)(handPos.x - lerpedPos.x);
            float yDiff = (float)(handPos.y - lerpedPos.y);
            float zDiff = (float)(handPos.z - lerpedPos.z);
            VertexConsumer lineStripBuffer = vertexConsumerProvider.getBuffer(RenderType.lineStrip());
            PoseStack.Pose lineEntry = matrixStack.last();

            for(int o = 0; o <= 16; ++o) {
                renderFishingLine(xDiff, yDiff, zDiff, lineStripBuffer, lineEntry, percentage(o, 16), percentage(o + 1, 16), DyeColor.BROWN.getTextureDiffuseColor());
            }

            matrixStack.popPose();
            super.render(hook, yaw, tickDelta, matrixStack, vertexConsumerProvider, light);
        }
    }

    private Vec3 getHandPos(Player player, float handBob, float tickDelta, Item item) {
        int sideOffset = player.getMainArm() == HumanoidArm.RIGHT ? 1 : -1;
        ItemStack itemStack = player.getMainHandItem();
        if (!itemStack.is(item)) {
            sideOffset = -sideOffset;
        }

        if (this.entityRenderDispatcher.options.getCameraType().isFirstPerson() && player == Minecraft.getInstance().player) {
            double m = 960.0 / (double) this.entityRenderDispatcher.options.fov().get();
            Vec3 vec3d = this.entityRenderDispatcher.camera.getNearPlane().getPointOnPlane((float) sideOffset * 0.525F, -0.1F).scale(m).yRot(handBob * 0.5F).xRot(-handBob * 0.7F);
            return player.getEyePosition(tickDelta).add(vec3d);
        } else {
            float lerpBodyYaw = Mth.lerp(tickDelta, player.yBodyRotO, player.yBodyRot) * Mth.DEG_TO_RAD;
            double d = Mth.sin(lerpBodyYaw);
            double e = Mth.cos(lerpBodyYaw);
            float playerScale = player.getScale();
            double j = (double) sideOffset * 0.35 * (double) playerScale;
            double k = 0.8 * (double) playerScale;
            float yOffset = player.isCrouching() ? -0.1875F : 0.0F;
            return player.getEyePosition(tickDelta).add(-e * j - d * k, (double) yOffset - 0.45 * (double) playerScale, -d * j + e * k);
        }
    }

    private static float percentage(int value, int max) {
        return (float)value / (float)max;
    }

    private static void vertex(VertexConsumer buffer, PoseStack.Pose matrix, int light, float x, int y, int u, int v) {
        buffer.addVertex(matrix, x - 0.5F, (float)y - 0.5F, 0.0F)
                .setColor(-1)
                .setUv((float)u, (float)v)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(light)
                .setNormal(matrix, 0.0F, 1.0F, 0.0F);
    }

    private static void renderFishingLine(float xDist, float yDist, float zDist, VertexConsumer buffer, PoseStack.Pose matrices, float segmentStart, float segmentEnd, int lineARGBColor) {
        float xStart = xDist * segmentStart;
        float yStart = yDist * (segmentStart * segmentStart + segmentStart) * 0.5F + 0.25F;
        float zStart = zDist * segmentStart;
        float xStep = xDist * segmentEnd - xStart;
        float yStep = yDist * (segmentEnd * segmentEnd + segmentEnd) * 0.5F + 0.25F - yStart;
        float zStep = zDist * segmentEnd - zStart;
        float step = Mth.sqrt(xStep * xStep + yStep * yStep + zStep * zStep);
        xStep /= step;
        yStep /= step;
        zStep /= step;
        buffer.addVertex(matrices, xStart, yStart, zStart).setColor(lineARGBColor).setNormal(matrices, xStep, yStep, zStep);
    }


    @Override
    public ResourceLocation getTextureLocation(HookEntity entity) {
        return TEXTURE;
    }
}
