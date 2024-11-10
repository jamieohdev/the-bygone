package com.jamiedev.mod.common.client.models;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;

public class DuckieModel<T extends Entity> extends AnimalModel<T> {
	private final ModelPart head;
	private final ModelPart body;
	private final ModelPart wing_left;
	private final ModelPart wing_right;
	private final ModelPart tail;
	public DuckieModel(ModelPart root) {
		this.head = root.getChild("head");
		this.body = root.getChild("body");
		this.wing_left = root.getChild("wing_left");
		this.wing_right = root.getChild("wing_right");
		this.tail = root.getChild("tail");
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData head = modelPartData.addChild("head", ModelPartBuilder.create().uv(0, 19).cuboid(-4.0F, -19.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.0F))
		.uv(0, 35).cuboid(-3.0F, -13.5F, -8.0F, 6.0F, 2.0F, 4.0F, new Dilation(0.0F))
		.uv(0, 0).cuboid(-1.0F, -14.5F, -5.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		ModelPartData body = modelPartData.addChild("body", ModelPartBuilder.create().uv(0, 0).cuboid(-5.0F, -11.0F, -3.0F, 10.0F, 7.0F, 12.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		ModelPartData wing_left = modelPartData.addChild("wing_left", ModelPartBuilder.create().uv(34, 19).cuboid(5.0F, -10.0F, -1.0F, 1.0F, 5.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		ModelPartData wing_right = modelPartData.addChild("wing_right", ModelPartBuilder.create().uv(24, 27).cuboid(-6.0F, -10.0F, -1.0F, 1.0F, 5.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		ModelPartData tail = modelPartData.addChild("tail", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		ModelPartData tail_r1 = tail.addChild("tail_r1", ModelPartBuilder.create().uv(32, 0).cuboid(-9.0F, -9.0F, -1.0F, 10.0F, 9.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(4.0F, -4.0F, 9.0F, -0.3491F, 0.0F, 0.0F));
		return TexturedModelData.of(modelData, 64, 64);
	}
	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, int color) {
		head.render(matrices, vertexConsumer, light, overlay, color);
		body.render(matrices, vertexConsumer, light, overlay, color);
		wing_left.render(matrices, vertexConsumer, light, overlay, color);
		wing_right.render(matrices, vertexConsumer, light, overlay, color);
		tail.render(matrices, vertexConsumer, light, overlay, color);
	}

	protected Iterable<ModelPart> getHeadParts() {
		return ImmutableList.of(this.head);
	}

	protected Iterable<ModelPart> getBodyParts() {
		return ImmutableList.of(this.body,  this.wing_right, this.wing_left, this.tail);
	}

	@Override
	public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		this.head.pitch = headPitch * 0.017453292F;
		this.head.yaw = headYaw * 0.017453292F;

	}
}