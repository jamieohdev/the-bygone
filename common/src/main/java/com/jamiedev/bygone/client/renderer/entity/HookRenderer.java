package com.jamiedev.bygone.client.renderer.entity;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.common.entity.projectile.HookEntity;
import com.jamiedev.bygone.core.registry.BGItems;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import dev.ryanhcode.sable.companion.ClientSubLevelAccess;
import dev.ryanhcode.sable.companion.SableCompanion;
import dev.ryanhcode.sable.companion.math.Pose3dc;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import org.joml.Quaternionf;
import org.joml.Vector3f;

public class HookRenderer extends EntityRenderer<HookEntity> {
    private static final ResourceLocation TEXTURE = Bygone.id("textures/entity/hook.png");
    private static final RenderType LAYER = RenderType.entityCutout(TEXTURE);
    private static final BlockState CHAIN = Blocks.CHAIN.defaultBlockState();

    private final BlockRenderDispatcher blockRenderer;
    public HookRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
        this.blockRenderer = ctx.getBlockRenderDispatcher();
    }



    @Override
    public void render(HookEntity hook, float yaw, float tickDelta, PoseStack poses, MultiBufferSource buffers, int light){
        super.render(hook, yaw, tickDelta, poses, buffers, light);

        poses.pushPose();
        poses.translate(0F, hook.getEyeHeight(), 0F);
        // unrotate on rotated sublevels
        ClientSubLevelAccess sublevel = SableCompanion.INSTANCE.getContainingClient(hook);
        if(sublevel != null) poses.mulPose(new Quaternionf(sublevel.renderPose(tickDelta).orientation()).conjugate());
        // then we, primarily, construct a rotation from (0,1,0) to chain
        // which, is ((0,1,0) × chain) + (|0,1,0||chain|) + ((0,1,0) ⋅ chain)
        // but! this uhh. frongles our rotations. so we counterspin it too. ig.
        // a non-removable singularity exists when chain is (0,-1,0), so uhh. try our best!
        Vec3 chain = this.chain(hook, sublevel, tickDelta);
        Vec3 direction = chain.normalize();
        if(direction.y() < Mth.EPSILON - 1) poses.mulPose(new Quaternionf(1F, 0F, 0F, 0F));
        else poses.mulPose(new Quaternionf(direction.z(), 0, -direction.x(), 1 + direction.y())
                .rotateY((float)Math.atan2(direction.x(), direction.z()))
                .normalize());

        this.renderHook(hook, poses, buffers, light);
        poses.mulPose(new Quaternionf().rotateY(0.25F * Mth.PI));
        this.renderChain(hook, (float)chain.length(), poses, buffers);
        poses.popPose();
    }

    private void renderHook(HookEntity hook, PoseStack poses, MultiBufferSource buffers, int light){
        poses.pushPose();
        poses.scale(0.25F, 0.25F, 0.25F);

        PoseStack.Pose pose = poses.last();
        VertexConsumer consumer = buffers.getBuffer(LAYER);
        this.vertex(consumer, pose, light, -1F, -1F, 0F, 1F, 1F);
        this.vertex(consumer, pose, light,  1F, -1F, 1F, 1F, 1F);
        this.vertex(consumer, pose, light,  1F,  1F, 1F, 0F, 1F);
        this.vertex(consumer, pose, light, -1F,  1F, 0F, 0F, 1F);
        this.vertex(consumer, pose, light, -1F,  1F, 0F, 0F, -1F);
        this.vertex(consumer, pose, light,  1F,  1F, 1F, 0F, -1F);
        this.vertex(consumer, pose, light,  1F, -1F, 1F, 1F, -1F);
        this.vertex(consumer, pose, light, -1F, -1F, 0F, 1F, -1F);
        poses.popPose();
    }
    private static void vertex(VertexConsumer consumer, PoseStack.Pose pose, int light, float x, float z, float u, float v, float normal){
        consumer.addVertex(pose, x, 0.0F, z)
                .setColor(-1)
                .setUv(u, v)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(light)
                .setNormal(pose, 0F, normal, 0F);
    }

    private void renderChain(HookEntity hook, float length, PoseStack poses, MultiBufferSource buffers){
        if(length > HookEntity.MAX_RANGE) return;

        poses.pushPose();

        float equilibrium = hook.equilibrium() > 0F ? hook.equilibrium() : length;
        int segments = (int)Math.floor(equilibrium);
        poses.translate(-0.5F, 0F, -0.5F);
        poses.scale(1F, length / equilibrium, 1F);

        float extra = equilibrium - (float)segments;
        poses.pushPose();
        poses.scale(1F, extra, 1F);
        this.blockRenderer.renderSingleBlock(CHAIN, poses, buffers, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);
        poses.popPose();
        poses.translate(0F, extra, 0F);

        for(int i = 0; i < segments; i++){
            this.blockRenderer.renderSingleBlock(CHAIN, poses, buffers, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);
            poses.translate(0F, 1F, 0F);
        }
        poses.popPose();
    }



    private Vec3 chain(HookEntity hook, ClientSubLevelAccess sublevel, float tickDelta){
        Entity owner = hook.getOwner();
        if(owner == null) return Vec3.ZERO;

        Vec3 offset = Vec3.ZERO;
        if(owner instanceof Player player){
            float bob = Mth.sin(Mth.sqrt(player.getAttackAnim(tickDelta)) * Mth.PI);
            offset = this.getRelativeHandPos(player, bob, tickDelta);
        }
        // this part is similar to HookEntity#chain, but we need to pre-add offset, and use the interpolating functions
        Pose3dc pose = sublevel != null ? sublevel.renderPose(tickDelta) : HookEntity.GLOBAL_POSE;
        Vec3 origin = pose.transformPositionInverse(SableCompanion.INSTANCE.projectOutOfSubLevel(hook.level(), hook.getEyePosition(tickDelta)));
        Vec3 dest = pose.transformPositionInverse(SableCompanion.INSTANCE.projectOutOfSubLevel(owner.level(), owner.getEyePosition(tickDelta).add(offset)));
        return dest.subtract(origin);
    }

    // modified slightly from FishingHookRenderer
    private Vec3 getRelativeHandPos(Player player, float handBob, float tickDelta){
        float side = player.getMainArm() == HumanoidArm.RIGHT ? -1 : 1;
        if (player.getMainHandItem().is(BGItems.ANCIENT_HOOK.get())) side = -side;
        else if (!player.getOffhandItem().is(BGItems.ANCIENT_HOOK.get())) return new Vec3(0.0, -player.getEyeHeight() / 16.2, 0.0); // equiv to height - 0.1

        if (this.entityRenderDispatcher.options.getCameraType().isFirstPerson() && player == Minecraft.getInstance().player) {
            double m = 960.0 / this.entityRenderDispatcher.options.fov().get();
            return this.entityRenderDispatcher.camera.getNearPlane().getPointOnPlane(side * 0.525F, -0.1F)
                    .scale(m).yRot(handBob * 0.5F).xRot(-handBob * 0.7F);
        } else {
            double lerpBodyYaw = Mth.lerp(tickDelta, player.yBodyRotO, player.yBodyRot) * Mth.DEG_TO_RAD;
            double d = Math.sin(lerpBodyYaw);
            double e = Math.cos(lerpBodyYaw);
            float playerScale = player.getScale();
            double j = side * 0.35 * playerScale;
            double k = 0.8 * playerScale;
            double yOffset = player.isCrouching() ? -0.1875 : 0.0;
            return new Vec3(-e * j - d * k, yOffset - 0.45F * playerScale, -d * j + e * k);
        }
    }

    @Override public ResourceLocation getTextureLocation(HookEntity entity){ return TEXTURE; }
}