package com.jamiedev.bygone.client.renderer.entity;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.common.entity.projectile.HookEntity;
import com.jamiedev.bygone.core.registry.BGItems;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;

public class HookRenderer extends EntityRenderer<HookEntity> {
    private static final ResourceLocation TEXTURE = Bygone.id("textures/entity/hook.png");
    private static final RenderType LAYER = RenderType.entityCutout(TEXTURE);

    public HookRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    private static void vertex(VertexConsumer buffer, PoseStack.Pose matrix, int light, float x, int y, int u, int v) {
        buffer.addVertex(matrix, x - 0.5F, (float) y - 0.5F, 0.0F)
                .setColor(-1)
                .setUv((float) u, (float) v)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(light)
                .setNormal(matrix, 0.0F, 1.0F, 0.0F);
    }


    private void renderChain(PoseStack poseStack, MultiBufferSource buffers, HookEntity hook, Vec3 start, Vec3 end, float partialTick) {
        Minecraft mc = Minecraft.getInstance();

        double dx = end.x - start.x;
        double dy = end.y - start.y;
        double dz = end.z - start.z;

        double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);

        if (distance < 0.05D) return;

        float nx = (float)(dx / distance);
        float ny = (float)(dy / distance);
        float nz = (float)(dz / distance);

        Quaternionf rotation = rotationYDirection(nx, ny, nz);

        ItemStack chainStack = new ItemStack(Blocks.CHAIN.asItem());

        int segments = (int)Math.round(distance * 4.0D);
        if (segments < 1) segments = 1;

        Vec3 hookPos = hook.getPosition(partialTick);

        for (int i = 0; i < segments; i++) {
            double t = ((double)i + 0.5D) / segments;

            double x = start.x + dx * t;
            double y = start.y + dy * t;
            double z = start.z + dz * t;

            poseStack.pushPose();

            poseStack.translate(
                    x - hookPos.x,
                    y - hookPos.y,
                    z - hookPos.z
            );

            poseStack.mulPose(rotation);

            if (((int)Math.floor(distance * t / 0.25D) & 1) == 1) {
                poseStack.mulPose(Axis.YP.rotationDegrees(90F));
            }
            poseStack.scale(0.9F, 0.9F, 0.9F);

            mc.getItemRenderer().renderStatic(chainStack, ItemDisplayContext.GROUND,
                    LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY,
                    poseStack, buffers, hook.level(), 0);

            poseStack.popPose();
        }
    }
    private Quaternionf rotationYDirection(float tx, float ty, float tz) {
        float dot = ty;

        if (dot < -0.9999F) {
            return new Quaternionf().rotationX((float)Math.PI);
        }

        float cx = tz;
        float cy = 0F;
        float cz = -tx;
        float w = 1F + dot;

        Quaternionf q = new Quaternionf(cx, cy, cz, w);
        q.normalize();

        return q;
    }

    @Override
    public void render(HookEntity hook, float yaw, float tickDelta, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int light) {
        Player playerOwner = hook.getPlayerOwner();
        if (playerOwner != null && hook.tickCount > 1) {
            float progress = hook.getChainProgress(tickDelta);

            float handSwingProgress = playerOwner.getAttackAnim(tickDelta);
            float handBob = Mth.sin(Mth.sqrt(handSwingProgress) * Mth.PI);
            Vec3 handPos = this.getHandPos(playerOwner, handBob, tickDelta, BGItems.ANCIENT_HOOK.get());
            Vec3 lerpedPos = hook.getPosition(tickDelta).add(0.0, 0.25, 0.0);
            Vec3 animatedEnd = handPos.lerp(lerpedPos, progress);

            matrixStack.pushPose();
            if (progress > 0.5F) {
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
            }

            renderChain(matrixStack, vertexConsumerProvider, hook, handPos, animatedEnd, tickDelta);
            matrixStack.popPose();

            if (progress > 0.5F) {
                super.render(hook, yaw, tickDelta, matrixStack, vertexConsumerProvider, light);
            }
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
            double j = (double) sideOffset * 0.55 * (double) playerScale;
            double k = 0.55 * (double) playerScale;
            float yOffset = player.isCrouching() ? -0.1875F : 0.0F;
            return player.getEyePosition(tickDelta).add(-e * j - d * k, (double) yOffset - 0.60 * (double) playerScale, -d * j + e * k);
        }
    }

    @Override
    public ResourceLocation getTextureLocation(HookEntity entity) {
        return TEXTURE;
    }
}