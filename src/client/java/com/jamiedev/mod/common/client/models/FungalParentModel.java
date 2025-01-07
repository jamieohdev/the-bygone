package com.jamiedev.mod.common.client.models;

import com.google.common.collect.ImmutableList;
import com.jamiedev.mod.common.entities.FungalParentEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EndermanEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;

public class FungalParentModel<T extends LivingEntity> extends BipedEntityModel<T> {
	private final ModelPart body;
	private final ModelPart head;
	private final ModelPart hat;
	public final ModelPart rightArm;
	public final ModelPart leftArm;
	private final ModelPart rightLeg;
	private final ModelPart leftLeg;

	EndermanEntityModel ref;
	
	public FungalParentModel(ModelPart root) {
		super(root);
		this.body = root.getChild("body");
		this.head = root.getChild("head");
		this.rightArm = root.getChild("right_arm");
		this.leftArm = root.getChild("left_arm");
		this.rightLeg = root.getChild("right_leg");
		this.leftLeg = root.getChild("left_leg");
		this.hat = root.getChild("hat");
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData body = modelPartData.addChild("body", ModelPartBuilder.create().uv(0, 16).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -15.0F, 0.0F));

		ModelPartData head = modelPartData.addChild("head", ModelPartBuilder.create().uv(0, 0).cuboid(-6.0F, -11.5F, -4.0F, 12.0F, 8.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -12.0F, 0.0F));
		ModelPartData hat = modelPartData.addChild("hat", ModelPartBuilder.create().uv(0, 0).cuboid(-6.0F, -11.5F, -4.0F, 12.0F, 8.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -12.0F, 0.0F));

		ModelPartData rightArm = modelPartData.addChild("right_arm", ModelPartBuilder.create().uv(24, 16).cuboid(-2.0F, -2.0F, -1.0F, 2.0F, 30.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(-5.0F, -13.0F, 0.0F));

		ModelPartData leftArm = modelPartData.addChild("left_arm", ModelPartBuilder.create().uv(0, 32).cuboid(0.0F, -2.0F, -1.0F, 2.0F, 30.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(5.0F, -13.0F, 0.0F));

		ModelPartData rightLeg = modelPartData.addChild("right_leg", ModelPartBuilder.create().uv(8, 32).cuboid(-1.0F, 4.0F, -1.0F, 2.0F, 26.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(-2.0F, -6.0F, 0.0F));

		ModelPartData leftLeg = modelPartData.addChild("left_leg", ModelPartBuilder.create().uv(16, 32).cuboid(-1.0F, 4.0F, -1.0F, 2.0F, 26.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(2.0F, -6.0F, 0.0F));
		return TexturedModelData.of(modelData, 64, 64);
	}

	@Override
	public void setAngles(T livingEntity, float f, float g, float h, float i, float j) {
		super.setAngles(livingEntity, f, g, h, i, j);
		this.hat.visible = false;
		this.head.visible = true;

		// thanks to @swirtzly for the baby rendering stuff :)
		this.leftArm.visible = !livingEntity.isBaby();
		this.rightArm.visible = !livingEntity.isBaby();

		this.body.pitch = 0.0F;
		this.body.pivotY = -14.0F;
		this.body.pivotZ = -0.0F;
		ModelPart var10000 = this.rightLeg;
		var10000.pitch -= 0.0F;
		var10000 = this.leftLeg;
		var10000.pitch -= 0.0F;
		var10000 = this.rightArm;
		var10000.pitch *= 0.5F;
		var10000 = this.leftArm;
		var10000.pitch *= 0.5F;
		var10000 = this.rightLeg;
		var10000.pitch *= 0.5F;
		var10000 = this.leftLeg;
		var10000.pitch *= 0.5F;
		float l = 0.4F;
		if (this.rightArm.pitch > 0.4F) {
			this.rightArm.pitch = 0.4F;
		}

		if (this.leftArm.pitch > 0.4F) {
			this.leftArm.pitch = 0.4F;
		}

		if (this.rightArm.pitch < -0.4F) {
			this.rightArm.pitch = -0.4F;
		}

		if (this.leftArm.pitch < -0.4F) {
			this.leftArm.pitch = -0.4F;
		}

		if (this.rightLeg.pitch > 0.4F) {
			this.rightLeg.pitch = 0.4F;
		}

		if (this.leftLeg.pitch > 0.4F) {
			this.leftLeg.pitch = 0.4F;
		}

		if (this.rightLeg.pitch < -0.4F) {
			this.rightLeg.pitch = -0.4F;
		}

		if (this.leftLeg.pitch < -0.4F) {
			this.leftLeg.pitch = -0.4F;
		}

		this.rightLeg.pivotZ = 0.0F;
		this.leftLeg.pivotZ = 0.0F;
		this.rightLeg.pivotY = -5.0F;
		this.leftLeg.pivotY = -5.0F;
		this.head.pivotZ = -0.0F;
		this.head.pivotY = -13.0F;
		this.hat.pivotX = this.head.pivotX;
		this.hat.pivotY = this.head.pivotY;
		this.hat.pivotZ = this.head.pivotZ;
		this.hat.pitch = this.head.pitch;
		this.hat.yaw = this.head.yaw;
		this.hat.roll = this.head.roll;


		this.rightArm.setPivot(-5.0F, -12.0F, 0.0F);
		this.leftArm.setPivot(5.0F, -12.0F, 0.0F);

		FungalParentEntity livingEntity1 = (FungalParentEntity) livingEntity;

	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, int color)  {
		body.render(matrices, vertexConsumer, light, overlay, color);
		head.render(matrices, vertexConsumer, light, overlay, color);
		hat.render(matrices, vertexConsumer, light, overlay, color);
		rightArm.render(matrices, vertexConsumer, light, overlay, color);
		leftArm.render(matrices, vertexConsumer, light, overlay, color);
		rightLeg.render(matrices, vertexConsumer, light, overlay, color);
		leftLeg.render(matrices, vertexConsumer, light, overlay, color);
	}
}