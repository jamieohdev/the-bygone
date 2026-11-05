package com.jamiedev.bygone.client.renderer.entity;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.client.JamiesModModelLayers;
import com.jamiedev.bygone.client.models.WraithModel;
import com.jamiedev.bygone.client.renderer.entity.layers.WraithEyesLayer;
import com.jamiedev.bygone.common.entity.WraithEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class WraithRenderer extends HauntingsMobRenderer<WraithEntity, WraithModel<WraithEntity>> {
    private static final ResourceLocation TEXTURE = Bygone.id("textures/entity/wraith.png");
    private final Matrix4f entityPoseInverse = new Matrix4f();

    public WraithRenderer(EntityRendererProvider.Context context) {
        super(context, new WraithModel<>(context.bakeLayer(JamiesModModelLayers.WRAITH)), 0.5F);
        this.addLayer(new WraithEyesLayer<>(this));
        this.addLayer(new RenderLayer<>(this) {
            @Override
            public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, WraithEntity entity,
                               float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
                if (entity.canEmitSpellParticles()) {
                    entity.emitSpellParticles(getSpellParticlePosition(entity, HumanoidArm.LEFT, poseStack, partialTick),
                            getSpellParticlePosition(entity, HumanoidArm.RIGHT, poseStack, partialTick));
                }
            }
        });
    }

    @Override
    public void render(WraithEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        this.entityPoseInverse.set(poseStack.last().pose()).invert();
        super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);
    }

    @Nullable
    private Vec3 getSpellParticlePosition(WraithEntity entity, HumanoidArm arm, PoseStack poseStack, float partialTick) {
        poseStack.pushPose();
        this.model.translateToHand(arm, poseStack);
        Matrix4f transform = new Matrix4f(this.entityPoseInverse).mul(poseStack.last().pose());
        poseStack.popPose();
        Vector3f shoulder = transform.transformPosition(0.0F, 0.0F, 0.0F, new Vector3f());
        Vector3f hand = transform.transformPosition(0.0F, 7.0F / 16.0F, 0.0F, new Vector3f());
        if (hand.y <= shoulder.y) {
            return null;
        }
        return new Vec3(Mth.lerp(partialTick, entity.xOld, entity.getX()) + hand.x,
                Mth.lerp(partialTick, entity.yOld, entity.getY()) + hand.y,
                Mth.lerp(partialTick, entity.zOld, entity.getZ()) + hand.z);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull WraithEntity WraithEntity) {
        return TEXTURE;
    }

    @Nullable
    @Override
    protected RenderType getRenderType(@NotNull WraithEntity entity, boolean bodyVisible, boolean translucent, boolean glowing) {
        if (bodyVisible || translucent) {
            return RenderType.entityTranslucent(this.getTextureLocation(entity));
        }
        return glowing ? RenderType.outline(this.getTextureLocation(entity)) : null;
    }
}
