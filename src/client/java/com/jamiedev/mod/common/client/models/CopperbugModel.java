package com.jamiedev.mod.common.client.models;

import com.jamiedev.mod.common.entities.CopperbugEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.SilverfishEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.util.math.MathHelper;

public class CopperbugModel<C extends AnimalEntity> extends EntityModel<CopperbugEntity> {
	private final ModelPart body1;
	private final ModelPart body2;
	private final ModelPart body3;
	private final ModelPart body4;
	private final ModelPart pincerRight;
	private final ModelPart pincerLeft;


	public CopperbugModel(ModelPart root)
	{
		this.body1 = root.getChild("body1");
		this.body2 = root.getChild("body2");
		this.body3 = root.getChild("body3");
		this.body4 = root.getChild("body4");
		this.pincerRight = root.getChild("pincerRight");
		this.pincerLeft = root.getChild("pincerLeft");
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData body1 = modelPartData.addChild("body1", ModelPartBuilder.create().uv(0, 0).cuboid(-5.0F, -2.0F, 2.0F, 6.0F, 2.0F, 5.0F, new Dilation(0.0F)), ModelTransform.pivot(2.0F, 24.0F, -7.0F));

		ModelPartData body2 = modelPartData.addChild("body2", ModelPartBuilder.create().uv(0, 8).cuboid(-2.5F, -2.0F, 0.0F, 5.0F, 2.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		ModelPartData body3 = modelPartData.addChild("body3", ModelPartBuilder.create().uv(0, 15).cuboid(-2.0F, -2.0F, 4.0F, 4.0F, 2.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		ModelPartData body4 = modelPartData.addChild("body4", ModelPartBuilder.create().uv(0, 21).cuboid(-1.5F, -1.0F, 7.0F, 3.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		ModelPartData pincerRight = modelPartData.addChild("pincerRight", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		ModelPartData pincerRight_r1 = pincerRight.addChild("pincerRight_r1", ModelPartBuilder.create().uv(15, 15).cuboid(-1.0F, -1.0F, -5.0F, 1.0F, 1.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(-2.0F, 0.0F, -5.0F, 0.0F, -0.2618F, 0.0F));

		ModelPartData pincerLeft = modelPartData.addChild("pincerLeft", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		ModelPartData pincerLeft_r1 = pincerLeft.addChild("pincerLeft_r1", ModelPartBuilder.create().uv(19, 8).cuboid(0.0F, -0.5F, -5.0F, 1.0F, 1.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(2.0F, -0.5F, -5.0F, 0.0F, 0.2618F, 0.0F));
		return TexturedModelData.of(modelData, 32, 32);
	}
	
	@Override
	public void setAngles(CopperbugEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch)
	{
		for(int i = 0; i < this.body1.pitch; ++i) {
			this.body1.yaw = MathHelper.cos(animationProgress * 0.9F + (float)i * 0.15F * 3.1415927F) * 3.1415927F * 0.05F * (float)(1 + Math.abs(i - 2));
			this.body1.pivotX = MathHelper.sin(animationProgress * 0.9F + (float)i * 0.15F * 3.1415927F) * 3.1415927F * 0.2F * (float)Math.abs(i - 2);
		}

		for(int i = 0; i < this.body2.pitch; ++i) {
			this.body2.yaw = MathHelper.cos(animationProgress * 0.9F + (float)i * 0.15F * 3.1415927F) * 3.1415927F * 0.05F * (float)(1 + Math.abs(i - 2));
			this.body2.pivotX = MathHelper.sin(animationProgress * 0.9F + (float)i * 0.15F * 3.1415927F) * 3.1415927F * 0.2F * (float)Math.abs(i - 2);
		}

		for(int i = 0; i < this.body3.pitch; ++i) {
			this.body3.yaw = MathHelper.cos(animationProgress * 0.9F + (float)i * 0.15F * 3.1415927F) * 3.1415927F * 0.05F * (float)(1 + Math.abs(i - 2));
			this.body3.pivotX = MathHelper.sin(animationProgress * 0.9F + (float)i * 0.15F * 3.1415927F) * 3.1415927F * 0.2F * (float)Math.abs(i - 2);
		}

		for(int i = 0; i < this.body4.pitch; ++i) {
			this.body4.yaw = MathHelper.cos(animationProgress * 0.9F + (float)i * 0.15F * 3.1415927F) * 3.1415927F * 0.05F * (float)(1 + Math.abs(i - 2));
			this.body4.pivotX = MathHelper.sin(animationProgress * 0.9F + (float)i * 0.15F * 3.1415927F) * 3.1415927F * 0.2F * (float)Math.abs(i - 2);
		}
	}
	
	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, int color) {
		body1.render(matrices, vertexConsumer, light, overlay, color);
		body2.render(matrices, vertexConsumer, light, overlay, color);
		body3.render(matrices, vertexConsumer, light, overlay, color);
		body4.render(matrices, vertexConsumer, light, overlay, color);
		pincerRight.render(matrices, vertexConsumer, light, overlay, color);
		pincerLeft.render(matrices, vertexConsumer, light, overlay, color);
	}
}