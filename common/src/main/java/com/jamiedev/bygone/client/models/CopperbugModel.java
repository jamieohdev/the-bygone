package com.jamiedev.bygone.client.models;

import com.jamiedev.bygone.common.entity.CopperbugEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.Animal;

public class CopperbugModel<C extends Animal> extends EntityModel<CopperbugEntity> {
    private final ModelPart body1;
    private final ModelPart body2;
    private final ModelPart body3;
    private final ModelPart body4;
    private final ModelPart pincerRight;
    private final ModelPart pincerLeft;


    public CopperbugModel(ModelPart root) {
        this.body1 = root.getChild("body1");
        this.body2 = this.body1.getChild("body2");
        this.body3 = this.body2.getChild("body3");
        this.body4 = this.body3.getChild("body4");
        this.pincerRight = this.body1.getChild("pincerRight");
        this.pincerLeft = this.body1.getChild("pincerLeft");
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();
        PartDefinition body1 = modelPartData.addOrReplaceChild("body1", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -2.0F, -5.0F, 6.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition body2 = body1.addOrReplaceChild("body2", CubeListBuilder.create().texOffs(0, 8).addBox(-2.5F, -2.0F, 0.0F, 5.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.ZERO);

        PartDefinition body3 = body2.addOrReplaceChild("body3", CubeListBuilder.create().texOffs(0, 15).addBox(-2.0F, -2.0F, 0.0F, 4.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 4.0F));

        PartDefinition body4 = body3.addOrReplaceChild("body4", CubeListBuilder.create().texOffs(0, 21).addBox(-1.5F, -1.0F, 0.0F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 3.0F));

        PartDefinition pincerRight = body1.addOrReplaceChild("pincerRight", CubeListBuilder.create(), PartPose.offset(-2.0F, 0.0F, -5.0F));

        PartDefinition pincerRight_r1 = pincerRight.addOrReplaceChild("pincerRight_r1", CubeListBuilder.create().texOffs(15, 15).addBox(-1.0F, -1.0F, -5.0F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.rotation(0.0F, -0.2618F, 0.0F));

        PartDefinition pincerLeft = body1.addOrReplaceChild("pincerLeft", CubeListBuilder.create(), PartPose.offset(2.0F, -0.5F, -5.0F));

        PartDefinition pincerLeft_r1 = pincerLeft.addOrReplaceChild("pincerLeft_r1", CubeListBuilder.create().texOffs(19, 8).addBox(0.0F, -0.5F, -5.0F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.rotation(0.0F, 0.2618F, 0.0F));
        return LayerDefinition.create(modelData, 32, 32);
    }

    @Override
    public void setupAnim(CopperbugEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        float partialTick = Mth.clamp(animationProgress - entity.tickCount, 0.0F, 1.0F);
        float warningProgress = entity.getWarningAnimationProgress(partialTick);
        float raisedCleaningProgress = entity.getRaisedCleaningAnimationProgress(partialTick);
        float raisedProgress = Math.max(warningProgress, raisedCleaningProgress);
        float puffProgress = entity.getPuffAnimationProgress(partialTick);
        float headWiggle = Mth.sin(animationProgress * 0.75F) * 0.12F * raisedProgress;
        float walkingProgress = limbAngle * 1.4F;
        float walkingAmount = Mth.clamp(limbDistance, 0.0F, 1.0F);
        this.body1.xRot = -raisedProgress * 0.45F;
        this.body2.xRot = raisedProgress * 0.45F;
        this.body2.yScale = 1.0F + puffProgress * 0.75F;
        this.body1.yRot = Mth.cos(walkingProgress) * 0.2F * walkingAmount + headWiggle;
        this.body1.x = Mth.sin(walkingProgress) * Mth.PI * 0.5F * walkingAmount;
        this.body2.yRot = Mth.cos(walkingProgress + Mth.PI * 0.15F) * 0.18F * walkingAmount - headWiggle;
        this.body2.x = Mth.sin(walkingProgress + Mth.PI * 0.15F) * Mth.PI * 0.25F * walkingAmount;
        this.body3.yRot = Mth.cos(walkingProgress + Mth.PI * 0.3F) * 0.18F * walkingAmount;
        this.body3.x = 0.0F;
        this.body4.yRot = Mth.cos(walkingProgress + Mth.PI * 0.45F) * 0.18F * walkingAmount;
        this.body4.x = Mth.sin(walkingProgress + Mth.PI * 0.45F) * Mth.PI * 0.25F * walkingAmount;

        this.pincerRight.yRot = getPincerRotation(entity, limbAngle, animationProgress);
        this.pincerLeft.yRot = -this.pincerRight.yRot;
    }

    private static float getPincerRotation(CopperbugEntity entity, float limbAngle, float animationProgress) {
        float partialTick = Mth.clamp(animationProgress - entity.tickCount, 0.0F, 1.0F);
        float pincerRotation = Mth.sin(animationProgress * 0.08F + limbAngle * 0.7F) * 0.12F;
        float cleaningProgress = entity.getCleaningAnimationTicks(partialTick) % CopperbugEntity.ATTACK_ANIMATION_TICKS;
        float cleaningRotation = getChompRotation(cleaningProgress, pincerRotation);
        pincerRotation = Mth.lerp(entity.getCleaningAnimationProgress(partialTick), pincerRotation, cleaningRotation);
        pincerRotation = Mth.lerp(entity.getWarningAnimationProgress(partialTick), pincerRotation, 0.45F);
        if (entity.getAttackAnimationTicks() > 0) {
            float attackProgress = CopperbugEntity.ATTACK_ANIMATION_TICKS - entity.getAttackAnimationTicks() + partialTick;
            pincerRotation = getChompRotation(attackProgress, pincerRotation);
        }
        return pincerRotation;
    }

    private static float getChompRotation(float chompProgress, float restingRotation) {
        if (chompProgress < CopperbugEntity.ATTACK_OPEN_TICKS) {
            return Mth.lerp(chompProgress / CopperbugEntity.ATTACK_OPEN_TICKS, 0.0F, 0.6F);
        }

        if (chompProgress < CopperbugEntity.ATTACK_OPEN_TICKS + CopperbugEntity.ATTACK_CLOSE_TICKS) {
            float closingProgress = (chompProgress - CopperbugEntity.ATTACK_OPEN_TICKS) / CopperbugEntity.ATTACK_CLOSE_TICKS;
            return Mth.lerp(closingProgress, 0.6F, -0.14F);
        }

        float recoveryProgress = (chompProgress - CopperbugEntity.ATTACK_OPEN_TICKS - CopperbugEntity.ATTACK_CLOSE_TICKS) / CopperbugEntity.ATTACK_RECOVERY_TICKS;
        return Mth.lerp(recoveryProgress, -0.14F, restingRotation);
    }

    @Override
    public void renderToBuffer(PoseStack matrices, VertexConsumer vertexConsumer, int light, int overlay, int color) {
        this.body1.render(matrices, vertexConsumer, light, overlay, color);
    }
}
