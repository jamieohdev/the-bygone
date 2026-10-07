package com.jamiedev.bygone.client.models;

import com.jamiedev.bygone.common.entity.SkipperEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

public class SkipperModel extends EntityModel<SkipperEntity> {
    private static final float POWER_STROKE_PROGRESS = 1.0F / 4.0F;
    private static final float PADDLE_SWEEP_RADIANS = 0.9F;
    private static final float SCUTTLE_FREQUENCY = 1.5F;
    private static final float SCUTTLE_SWEEP_RADIANS = 0.35F;
    private static final float SCUTTLE_LIFT_RADIANS = 0.15F;
    private static final float HEAD_YAW_FACTOR = 0.35F;
    private static final float HEAD_PITCH_FACTOR = 0.25F;
    private final ModelPart basePart;
    private final ModelPart leftLegPart;
    private final ModelPart rightLegPart;
    private final ModelPart leftFrontPart;
    private final ModelPart leftBackPart;
    private final ModelPart rightFrontPart;
    private final ModelPart rightBackPart;
    private final ModelPart headPart;

    public SkipperModel(ModelPart rootPart) {
        this.basePart = rootPart.getChild("Base");
        this.leftLegPart = this.basePart.getChild("Left Leg");
        this.rightLegPart = this.basePart.getChild("Right Leg");
        this.leftFrontPart = this.leftLegPart.getChild("left_front");
        this.leftBackPart = this.leftLegPart.getChild("left_back");
        this.rightFrontPart = this.rightLegPart.getChild("right_front");
        this.rightBackPart = this.rightLegPart.getChild("right_back");
        this.headPart = this.basePart.getChild("Head");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition rootDefinition = meshDefinition.getRoot();

        PartDefinition baseDefinition = rootDefinition.addOrReplaceChild("Base",
            CubeListBuilder.create().texOffs(0, 0).addBox(-6.5F, -11.0F, -12.0F, 13.0F, 7.0F, 32.0F, new CubeDeformation(0.0F)),
            PartPose.offset(0.0F, 24.0F, 3.0F));

        PartDefinition leftLegDefinition = baseDefinition.addOrReplaceChild("Left Leg", CubeListBuilder.create(), PartPose.offset(6.4142F, -7.0F, -0.4142F));

        leftLegDefinition.addOrReplaceChild("cube_r1",
            CubeListBuilder.create().texOffs(0, 13).addBox(-4.0F, -2.5F, -4.0F, 8.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)),
            PartPose.offsetAndRotation(-0.5F, -0.5F, 0.0F, 0.0F, 0.7854F, 0.0F));

        PartDefinition leftBackDefinition = leftLegDefinition.addOrReplaceChild("left_back", CubeListBuilder.create(), PartPose.offset(1.5F, 0.0F, 3.5F));

        leftBackDefinition.addOrReplaceChild("left_back_leg_r1",
            CubeListBuilder.create().texOffs(-18, 87).addBox(-12.9393F, 0.0F, 0.1109F, 18.0F, 0.0F, 18.0F, new CubeDeformation(0.0F)),
            PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3491F, 0.7854F, 0.0F));

        PartDefinition leftFrontDefinition = leftLegDefinition.addOrReplaceChild("left_front", CubeListBuilder.create(), PartPose.offset(2.25F, 0.0F, -3.0F));

        leftFrontDefinition.addOrReplaceChild("left_front_leg_r1",
            CubeListBuilder.create().texOffs(-24, 39).addBox(-0.1109F, 0.0F, -12.6464F, 26.0F, 0.0F, 24.0F, new CubeDeformation(0.0F)),
            PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.2533F, 0.7519F, 0.3622F));

        PartDefinition rightLegDefinition = baseDefinition.addOrReplaceChild("Right Leg", CubeListBuilder.create(), PartPose.offset(-6.4142F, -7.0F, -0.4142F));

        rightLegDefinition.addOrReplaceChild("cube_r2",
            CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -2.5F, -4.0F, 8.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)),
            PartPose.offsetAndRotation(0.5F, -0.5F, 0.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition rightBackDefinition = rightLegDefinition.addOrReplaceChild("right_back", CubeListBuilder.create(), PartPose.offset(-1.5F, 0.0F, 3.5F));

        rightBackDefinition.addOrReplaceChild("right_back_leg_r1",
            CubeListBuilder.create().texOffs(18, 87).addBox(-5.0607F, 0.0F, 0.1109F, 18.0F, 0.0F, 18.0F, new CubeDeformation(0.0F)),
            PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3491F, -0.7854F, 0.0F));

        PartDefinition rightFrontDefinition = rightLegDefinition.addOrReplaceChild("right_front", CubeListBuilder.create(), PartPose.offset(-1.5F, 0.0F, 3.5F));

        rightFrontDefinition.addOrReplaceChild("right_front_leg_r1",
            CubeListBuilder.create().texOffs(-24, 63).addBox(-25.8891F, 0.0F, -12.6464F, 26.0F, 0.0F, 24.0F, new CubeDeformation(0.0F)),
            PartPose.offsetAndRotation(-0.75F, 0.0F, -6.5F, 0.2533F, -0.7519F, -0.3622F));

        PartDefinition headDefinition = baseDefinition.addOrReplaceChild("Head", CubeListBuilder.create(), PartPose.offset(0.0F, -7.0F, -10.0F));

        headDefinition.addOrReplaceChild("cube_r3", CubeListBuilder.create()
            .texOffs(52, 25).addBox(-4.0F, -14.0F, -22.0F, 0.0F, 18.0F, 14.0F, new CubeDeformation(0.0F))
            .texOffs(52, 43).addBox(4.0F, -14.0F, -22.0F, 0.0F, 18.0F, 14.0F, new CubeDeformation(0.0F))
            .texOffs(58, 0).addBox(-9.0F, -3.0F, -13.0F, 18.0F, 5.0F, 13.0F, new CubeDeformation(0.0F)),
            PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.4363F, 0.0F, 0.0F));

        return LayerDefinition.create(meshDefinition, 128, 128);
    }

    public static LayerDefinition createSaddleLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();
        meshDefinition.getRoot().addOrReplaceChild("Base", CubeListBuilder.create()
            .texOffs(0, 109).addBox(-8.0F, -11.5F, -14.0F, 16.0F, 3.0F, 16.0F), PartPose.offset(0.0F, 24.0F, 3.0F));
        return LayerDefinition.create(meshDefinition, 128, 128);
    }

    public ModelPart getBasePart() {
        return this.basePart;
    }

    @Override
    public void setupAnim(SkipperEntity skipper, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYawDegrees, float headPitchDegrees) {
        this.basePart.getAllParts().forEach(ModelPart::resetPose);
        float partialTick = ageInTicks - skipper.tickCount;
        float paddleAnimationAmount = skipper.getPaddleAnimationAmount(partialTick);
        if (paddleAnimationAmount > 0.0F) {
            float progress = skipper.getPaddleAnimationProgress(partialTick);
            float sweepProgress = progress < POWER_STROKE_PROGRESS ? progress / POWER_STROKE_PROGRESS
                : 1.0F - (progress - POWER_STROKE_PROGRESS) / (1.0F - POWER_STROKE_PROGRESS);
            sweepProgress = 0.5F - Mth.cos(sweepProgress * Mth.PI) * 0.5F;
            float angleRadians = (sweepProgress - 0.5F) * PADDLE_SWEEP_RADIANS * paddleAnimationAmount;
            this.leftLegPart.yRot = -angleRadians;
            this.rightLegPart.yRot = angleRadians;
            this.basePart.y -= skipper.getSaddleBobOffset(partialTick) * 16.0F;
        }
        if (!skipper.isOverWater() && skipper.onGround()) {
            float scuttlePhase = limbSwing * SCUTTLE_FREQUENCY;
            float scuttleAmount = limbSwingAmount * (1.0F - paddleAnimationAmount);
            float sweepRadians = Mth.sin(scuttlePhase) * SCUTTLE_SWEEP_RADIANS * scuttleAmount;
            float liftRadians = Mth.cos(scuttlePhase) * SCUTTLE_LIFT_RADIANS * scuttleAmount;
            this.leftFrontPart.yRot = sweepRadians;
            this.rightBackPart.yRot = -sweepRadians;
            this.leftBackPart.yRot = -sweepRadians;
            this.rightFrontPart.yRot = sweepRadians;
            this.leftFrontPart.zRot = -Math.max(0.0F, liftRadians);
            this.rightBackPart.zRot = Math.max(0.0F, liftRadians);
            this.leftBackPart.zRot = -Math.max(0.0F, -liftRadians);
            this.rightFrontPart.zRot = Math.max(0.0F, -liftRadians);
        }
        this.headPart.yRot = netHeadYawDegrees * Mth.DEG_TO_RAD * HEAD_YAW_FACTOR;
        this.headPart.xRot = headPitchDegrees * Mth.DEG_TO_RAD * HEAD_PITCH_FACTOR;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int colorArgb) {
        this.basePart.render(poseStack, vertexConsumer, packedLight, packedOverlay, colorArgb);
    }
}
