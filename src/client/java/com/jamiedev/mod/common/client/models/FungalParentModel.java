package com.jamiedev.mod.common.client.models;

import com.jamiedev.mod.common.entities.FungalParentEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;

public class FungalParentModel<T extends LivingEntity> extends BipedEntityModel<FungalParentEntity> {
	private final ModelPart right_leg;
	
	EndermanEntityModel ref;
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
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData right_leg = modelPartData.addChild("right_leg", ModelPartBuilder.create().uv(47, 36).cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 15.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(2.0F, 9.0F, 0.0F));

		ModelPartData right_arm = modelPartData.addChild("right_arm", ModelPartBuilder.create().uv(38, 36).cuboid(-1.0F, -1.0F, -1.0F, 2.0F, 23.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(7.0F, -2.0F, 0.0F));

		ModelPartData left_leg = modelPartData.addChild("left_leg", ModelPartBuilder.create().uv(0, 52).cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 15.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(-2.0F, 9.0F, 0.0F));

		ModelPartData left_arm = modelPartData.addChild("left_arm", ModelPartBuilder.create().uv(29, 36).cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 23.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(-7.0F, -3.0F, 0.0F));

		ModelPartData hat = modelPartData.addChild("hat", ModelPartBuilder.create().uv(9, 52).cuboid(-9.0F, 0.0F, -9.0F, 2.0F, 13.0F, 0.0F, new Dilation(0.0F))
				.uv(19, 52).cuboid(7.0F, 0.0F, -9.0F, 2.0F, 9.0F, 0.0F, new Dilation(0.0F))
				.uv(14, 52).cuboid(-9.0F, 0.0F, 9.0F, 2.0F, 10.0F, 0.0F, new Dilation(0.0F))
				.uv(49, 21).cuboid(7.0F, 0.0F, 9.0F, 2.0F, 14.0F, 0.0F, new Dilation(0.0F))
				.uv(0, 21).cuboid(-6.0F, -4.0F, -6.0F, 12.0F, 2.0F, 12.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -4.0F, 0.0F));

		ModelPartData body = modelPartData.addChild("body", ModelPartBuilder.create().uv(0, 36).cuboid(-5.0F, -27.0F, -2.0F, 10.0F, 11.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		ModelPartData head = modelPartData.addChild("head", ModelPartBuilder.create().uv(0, 0).cuboid(-9.0F, -2.0F, -9.0F, 18.0F, 2.0F, 18.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -4.0F, 0.0F));

		ModelPartData head_child = modelPartData.addChild("head_child", ModelPartBuilder.create().uv(92, 1).cuboid(-5.0F, -5.5F, -4.0F, 10.0F, 6.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -4.0F, 0.0F));
		return TexturedModelData.of(modelData, 128, 128);
	}
	@Override
	public void setAngles(FungalParentEntity livingEntity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.hat.visible = !livingEntity.isBaby();
		this.head_child.visible = livingEntity.isBaby();
		this.head.visible = !livingEntity.isBaby();

		// thanks to @swirtzly for the baby rendering stuff :)
		this.left_arm.visible = !livingEntity.isBaby();
		this.right_arm.visible = !livingEntity.isBaby();

		this.head_child.pivotX = this.head.pivotX;
		this.head_child.pivotY = this.head.pivotY;
		this.head_child.pivotZ = this.head.pivotZ;
		this.head_child.pitch = this.head.pitch;
		this.head_child.yaw = this.head.yaw;
		this.head_child.roll = this.head.roll;

		this.hat.pivotX = this.head.pivotX;
		this.hat.pivotY = this.head.pivotY;
		this.hat.pivotZ = this.head.pivotZ;
		this.hat.pitch = this.head.pitch;
		this.hat.yaw = this.head.yaw;
		this.hat.roll = this.head.roll;

		ModelPart var10000 = this.right_leg;
		var10000.pitch -= 0.0F;
		var10000 = this.left_leg;
		var10000.pitch -= 0.0F;
		var10000 = this.right_arm;
		var10000.pitch *= 0.5F;
		var10000 = this.left_arm;
		var10000.pitch *= 0.5F;
		var10000 = this.right_leg;
		var10000.pitch *= 0.5F;
		var10000 = this.left_leg;
		var10000.pitch *= 0.5F;

		if (this.right_arm.pitch > 0.4F) {
			this.right_arm.pitch = 0.4F;
		}

		if (this.left_arm.pitch > 0.4F) {
			this.left_arm.pitch = 0.4F;
		}

		if (this.right_arm.pitch < -0.4F) {
			this.right_arm.pitch = -0.4F;
		}

		if (this.left_arm.pitch < -0.4F) {
			this.left_arm.pitch = -0.4F;
		}

		if (this.right_leg.pitch > 0.4F) {
			this.right_leg.pitch = 0.4F;
		}

		if (this.left_leg.pitch > 0.4F) {
			this.left_leg.pitch = 0.4F;
		}

		this.right_leg.pitch = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
		this.left_leg.pitch = MathHelper.cos(limbSwing * 0.6662F + 3.1415927F) * 1.4F * limbSwingAmount;
	}
	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, int color) {
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