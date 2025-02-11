package com.jamiedev.mod.common.client.models;

import com.jamiedev.mod.common.entities.FungalParentEntity;
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
import net.minecraft.client.render.entity.model.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

public class FungalParentModel<T extends LivingEntity> extends HumanoidModel<FungalParentEntity> {
	private final ModelPart right_leg;
	
	EndermanModel ref;
	private final ModelPart right_arm;
	private final ModelPart left_leg;
	private final ModelPart left_arm;
	private final ModelPart hat;
	private final ModelPart body;
	private final ModelPart head;
	private final ModelPart head_child;
	public FungalParentModel(ModelPart root) {
		super(root);
		this.right_leg = root.getChild("right_leg");
		this.right_arm = root.getChild("right_arm");
		this.left_leg = root.getChild("left_leg");
		this.left_arm = root.getChild("left_arm");
		this.hat = root.getChild("hat");
		this.body = root.getChild("body");
		this.head = root.getChild("head");
		this.head_child = root.getChild("head_child");
	}
	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();
		PartDefinition right_leg = modelPartData.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(47, 36).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 15.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 9.0F, 0.0F));

		PartDefinition right_arm = modelPartData.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(38, 36).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 23.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(7.0F, -2.0F, 0.0F));

		PartDefinition left_leg = modelPartData.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 52).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 15.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 9.0F, 0.0F));

		PartDefinition left_arm = modelPartData.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(29, 36).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 23.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-7.0F, -3.0F, 0.0F));

		PartDefinition hat = modelPartData.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(9, 52).addBox(-9.0F, 0.0F, -9.0F, 2.0F, 13.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(19, 52).addBox(7.0F, 0.0F, -9.0F, 2.0F, 9.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(14, 52).addBox(-9.0F, 0.0F, 9.0F, 2.0F, 10.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(49, 21).addBox(7.0F, 0.0F, 9.0F, 2.0F, 14.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(0, 21).addBox(-6.0F, -4.0F, -6.0F, 12.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.0F, 0.0F));

		PartDefinition body = modelPartData.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 36).addBox(-5.0F, -27.0F, -2.0F, 10.0F, 11.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition head = modelPartData.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-9.0F, -2.0F, -9.0F, 18.0F, 2.0F, 18.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.0F, 0.0F));

		PartDefinition head_child = modelPartData.addOrReplaceChild("head_child", CubeListBuilder.create().texOffs(92, 1).addBox(-5.0F, -5.5F, -4.0F, 10.0F, 6.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.0F, 0.0F));
		return LayerDefinition.create(modelData, 128, 128);
	}
	@Override
	public void setAngles(FungalParentEntity livingEntity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.hat.visible = !livingEntity.isBaby();
		this.head_child.visible = livingEntity.isBaby();
		this.head.visible = !livingEntity.isBaby();

		// thanks to @swirtzly for the baby rendering stuff :)
		this.left_arm.visible = !livingEntity.isBaby();
		this.right_arm.visible = !livingEntity.isBaby();

		this.head_child.x = this.head.x;
		this.head_child.y = this.head.y;
		this.head_child.z = this.head.z;
		this.head_child.xRot = this.head.xRot;
		this.head_child.yRot = this.head.yRot;
		this.head_child.zRot = this.head.zRot;

		this.hat.x = this.head.x;
		this.hat.y = this.head.y;
		this.hat.z = this.head.z;
		this.hat.xRot = this.head.xRot;
		this.hat.yRot = this.head.yRot;
		this.hat.zRot = this.head.zRot;

		ModelPart var10000 = this.right_leg;
		var10000.xRot -= 0.0F;
		var10000 = this.left_leg;
		var10000.xRot -= 0.0F;
		var10000 = this.right_arm;
		var10000.xRot *= 0.5F;
		var10000 = this.left_arm;
		var10000.xRot *= 0.5F;
		var10000 = this.right_leg;
		var10000.xRot *= 0.5F;
		var10000 = this.left_leg;
		var10000.xRot *= 0.5F;

		if (this.right_arm.xRot > 0.4F) {
			this.right_arm.xRot = 0.4F;
		}

		if (this.left_arm.xRot > 0.4F) {
			this.left_arm.xRot = 0.4F;
		}

		if (this.right_arm.xRot < -0.4F) {
			this.right_arm.xRot = -0.4F;
		}

		if (this.left_arm.xRot < -0.4F) {
			this.left_arm.xRot = -0.4F;
		}

		if (this.right_leg.xRot > 0.4F) {
			this.right_leg.xRot = 0.4F;
		}

		if (this.left_leg.xRot > 0.4F) {
			this.left_leg.xRot = 0.4F;
		}

		this.right_leg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
		this.left_leg.xRot = Mth.cos(limbSwing * 0.6662F + 3.1415927F) * 1.4F * limbSwingAmount;
	}
	@Override
	public void renderToBuffer(PoseStack matrices, VertexConsumer vertexConsumer, int light, int overlay, int color) {
		right_leg.render(matrices, vertexConsumer, light, overlay, color);
		right_arm.render(matrices, vertexConsumer, light, overlay, color);
		left_leg.render(matrices, vertexConsumer, light, overlay, color);
		left_arm.render(matrices, vertexConsumer, light, overlay, color);
		hat.render(matrices, vertexConsumer, light, overlay, color);
		body.render(matrices, vertexConsumer, light, overlay, color);
		head.render(matrices, vertexConsumer, light, overlay, color);
		head_child.render(matrices, vertexConsumer, light, overlay, color);
	}
}