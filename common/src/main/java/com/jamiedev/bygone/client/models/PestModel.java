package com.jamiedev.bygone.client.models;

import com.jamiedev.bygone.common.entity.PestEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;


public class PestModel<T extends Entity> extends EntityModel<T> {
	private final ModelPart head;
	private final ModelPart tube;
	private final ModelPart body;
	private final ModelPart leftFrontLeg;
	private final ModelPart leftMiddleHindLeg;
	private final ModelPart leftHindLeg;
	private final ModelPart rightFrontLeg;
	private final ModelPart rightMiddleHindLeg;
	private final ModelPart rightHindLeg;

	public PestModel(ModelPart root) {
		this.head = root.getChild("head");
		this.tube = this.head.getChild("tube");
		this.body = root.getChild("body");
		this.leftFrontLeg = root.getChild("leftFrontLeg");
		this.leftMiddleHindLeg = root.getChild("leftMiddleHindLeg");
		this.leftHindLeg = root.getChild("leftHindLeg");
		this.rightFrontLeg = root.getChild("rightFrontLeg");
		this.rightMiddleHindLeg = root.getChild("rightMiddleHindLeg");
		this.rightHindLeg = root.getChild("rightHindLeg");
	}

	public static LayerDefinition getTexturedModelData() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 17).addBox(-3.0F, -3.0F, -6.0F, 6.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 18.0F, -5.0F));

		PartDefinition tube = head.addOrReplaceChild("tube", CubeListBuilder.create(), PartPose.offset(0.0F, 3.0F, -6.0F));

		PartDefinition tube_r1 = tube.addOrReplaceChild("tube_r1", CubeListBuilder.create().texOffs(24, 17).addBox(-1.0F, -1.2929F, -8.7782F, 2.0F, 2.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -3.0F, 1.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -4.0F, -5.0F, 8.0F, 7.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 18.0F, 0.0F));

		PartDefinition leftFrontLeg = partdefinition.addOrReplaceChild("leftFrontLeg", CubeListBuilder.create(), PartPose.offset(4.0F, 20.0F, -3.0F));

		PartDefinition leftFrontLeg_r1 = leftFrontLeg.addOrReplaceChild("leftFrontLeg_r1", CubeListBuilder.create().texOffs(0, 28).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.5708F));

		PartDefinition leftMiddleHindLeg = partdefinition.addOrReplaceChild("leftMiddleHindLeg", CubeListBuilder.create(), PartPose.offset(4.0F, 20.0F, 0.0F));

		PartDefinition leftMiddleHindLeg_r1 = leftMiddleHindLeg.addOrReplaceChild("leftMiddleHindLeg_r1", CubeListBuilder.create().texOffs(8, 28).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.5708F));

		PartDefinition leftHindLeg = partdefinition.addOrReplaceChild("leftHindLeg", CubeListBuilder.create(), PartPose.offset(4.0F, 20.0F, 3.0F));

		PartDefinition leftHindLeg_r1 = leftHindLeg.addOrReplaceChild("leftHindLeg_r1", CubeListBuilder.create().texOffs(16, 28).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.5708F));

		PartDefinition rightFrontLeg = partdefinition.addOrReplaceChild("rightFrontLeg", CubeListBuilder.create(), PartPose.offset(-4.0F, 20.0F, -3.0F));

		PartDefinition rightFrontLeg_r1 = rightFrontLeg.addOrReplaceChild("rightFrontLeg_r1", CubeListBuilder.create().texOffs(0, 36).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.5708F));

		PartDefinition rightMiddleHindLeg = partdefinition.addOrReplaceChild("rightMiddleHindLeg", CubeListBuilder.create(), PartPose.offset(-4.0F, 20.0F, 0.0F));

		PartDefinition rightMiddleHindLeg_r1 = rightMiddleHindLeg.addOrReplaceChild("rightMiddleHindLeg_r1", CubeListBuilder.create().texOffs(32, 28).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.5708F));

		PartDefinition rightHindLeg = partdefinition.addOrReplaceChild("rightHindLeg", CubeListBuilder.create(), PartPose.offset(-4.0F, 20.0F, 3.0F));

		PartDefinition rightHindLeg_r1 = rightHindLeg.addOrReplaceChild("rightHindLeg_r1", CubeListBuilder.create().texOffs(24, 28).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.5708F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
		head.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
		leftFrontLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
		leftMiddleHindLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
		leftHindLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
		rightFrontLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
		rightMiddleHindLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
		rightHindLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
	}
	
	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.tube.yRot = netHeadYaw * ((float)Math.PI / 180F);
		this.head.yRot = netHeadYaw * ((float)Math.PI / 180F);
		this.tube.xRot = headPitch * ((float)Math.PI / 180F);
		this.head.xRot = headPitch * ((float)Math.PI / 180F);
		float f = ((float)Math.PI / 4F);
		this.rightHindLeg.zRot = (-(float)Math.PI / 4F);
		this.leftHindLeg.zRot = ((float)Math.PI / 4F);
		this.rightMiddleHindLeg.zRot = -0.58119464F;
		this.leftMiddleHindLeg.zRot = 0.58119464F;

		this.rightFrontLeg.zRot = (-(float)Math.PI / 4F);
		this.leftFrontLeg.zRot = ((float)Math.PI / 4F);
		float f1 = -0.0F;
		float f2 = ((float)Math.PI / 8F);
		this.rightHindLeg.yRot = ((float)Math.PI / 4F);
		this.leftHindLeg.yRot = (-(float)Math.PI / 4F);
		this.rightMiddleHindLeg.yRot = ((float)Math.PI / 8F);
		this.leftMiddleHindLeg.yRot = (-(float)Math.PI / 8F);

		this.rightFrontLeg.yRot = (-(float)Math.PI / 4F);
		this.leftFrontLeg.yRot = ((float)Math.PI / 4F);
		float f3 = -(Mth.cos(limbSwing * 0.6662F * 2.0F + 0.0F) * 0.4F) * limbSwingAmount;
		float f4 = -(Mth.cos(limbSwing * 0.6662F * 2.0F + (float)Math.PI) * 0.4F) * limbSwingAmount;
		float f5 = -(Mth.cos(limbSwing * 0.6662F * 2.0F + ((float)Math.PI / 2F)) * 0.4F) * limbSwingAmount;
		float f6 = -(Mth.cos(limbSwing * 0.6662F * 2.0F + ((float)Math.PI * 1.5F)) * 0.4F) * limbSwingAmount;
		float f7 = Math.abs(Mth.sin(limbSwing * 0.6662F + 0.0F) * 0.4F) * limbSwingAmount;
		float f8 = Math.abs(Mth.sin(limbSwing * 0.6662F + (float)Math.PI) * 0.4F) * limbSwingAmount;
		float f9 = Math.abs(Mth.sin(limbSwing * 0.6662F + ((float)Math.PI / 2F)) * 0.4F) * limbSwingAmount;
		float f10 = Math.abs(Mth.sin(limbSwing * 0.6662F + ((float)Math.PI * 1.5F)) * 0.4F) * limbSwingAmount;
		ModelPart var10000 = this.rightHindLeg;
		var10000.yRot += f3;
		var10000 = this.leftHindLeg;
		var10000.yRot += -f3;
		var10000 = this.rightMiddleHindLeg;
		var10000.yRot += f4;
		var10000 = this.leftMiddleHindLeg;
		var10000.yRot += -f4;

		var10000 = this.rightFrontLeg;
		var10000.yRot += f6;
		var10000 = this.leftFrontLeg;
		var10000.yRot += -f6;
		var10000 = this.rightHindLeg;
		var10000.zRot += f7;
		var10000 = this.leftHindLeg;
		var10000.zRot += -f7;
		var10000 = this.rightMiddleHindLeg;
		var10000.zRot += f8;
		var10000 = this.leftMiddleHindLeg;
		var10000.zRot += -f8;

		var10000 = this.rightFrontLeg;
		var10000.zRot += f10;
		var10000 = this.leftFrontLeg;
		var10000.zRot += -f10;
	}
}