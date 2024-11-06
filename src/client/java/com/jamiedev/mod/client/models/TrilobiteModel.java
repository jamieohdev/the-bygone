package com.jamiedev.mod.client.models;// Made with Blockbench 4.11.1
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class TrilobiteModel<E extends Entity> extends EntityModel<E> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	private final ModelPart Head;
	private final ModelPart Midbody;
	private final ModelPart Tail;

	public TrilobiteModel(ModelPart root) {
		this.Head = root.getChild("Head");
		this.Midbody = root.getChild("Midbody");
		this.Tail = root.getChild("Tail");
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData meshdefinition = new ModelData();
		 ModelPartData partdefinition = meshdefinition.getRoot();

		 ModelPartData Head = partdefinition.addChild("Head", ModelPartBuilder.create().uv(0, 13).cuboid(-4.0F, -4.25F, -1.0F, 8.0F, 0.0F, 8.0F, new Dilation(0.0F))
		.uv(0, 0).cuboid(-5.0F, -4.0F, -1.0F, 10.0F, 4.0F, 9.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, -1.0F));

		 ModelPartData cube_r1 = Head.addChild("cube_r1", ModelPartBuilder.create().uv(14, 23).cuboid(0.0F, -2.0F, -3.0F, 2.0F, 2.0F, 6.0F, new Dilation(0.0F)),ModelTransform.of(4.0F, -0.25F, -2.0F, 0.0F, -0.3927F, 0.0F));

		 ModelPartData cube_r2 = Head.addChild("cube_r2", ModelPartBuilder.create().uv(14, 23).mirrored().cuboid(-2.0F, -2.0F, -3.0F, 2.0F, 2.0F, 6.0F, new Dilation(0.0F)).mirrored(false),ModelTransform.of(-4.0F, -0.25F, -2.0F, 0.0F, 0.3927F, 0.0F));

		 ModelPartData cube_r3 = Head.addChild("cube_r3", ModelPartBuilder.create().uv(0, 0).mirrored().cuboid(-1.0F, -2.0F, -1.0F, 0.0F, 2.0F, 2.0F, new Dilation(0.0F)).mirrored(false),ModelTransform.of(2.0F, -3.0F, 1.0F, 0.0F, 0.0F, 0.5672F));

		 ModelPartData cube_r4 = Head.addChild("cube_r4", ModelPartBuilder.create().uv(0, 0).cuboid(1.0F, -2.0F, -1.0F, 0.0F, 2.0F, 2.0F, new Dilation(0.0F)),ModelTransform.of(-2.0F, -3.0F, 1.0F, 0.0F, 0.0F, -0.5672F));

		 ModelPartData Midbody = partdefinition.addChild("Midbody", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 23.0F, -1.0F));

		 ModelPartData cube_r5 = Midbody.addChild("cube_r5", ModelPartBuilder.create().uv(0, 21).cuboid(-3.0F, -2.4226F, -3.9063F, 6.0F, 2.0F, 4.0F, new Dilation(0.0F)),ModelTransform.of(0.0F, 0.0F, 0.0F, 0.3491F, 0.0F, 0.0F));

		 ModelPartData Tail = partdefinition.addChild("Tail", ModelPartBuilder.create().uv(17, 14).cuboid(-1.0F, -2.0F, -5.0F, 2.0F, 2.0F, 7.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, -6.0F));

		return TexturedModelData.of(meshdefinition, 64, 64);
	}

	@Override
	public void setAngles(E entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		float f = 1.0F;
		float g = 1.0F;
		if (!entity.isTouchingWater()) {
			f = 1.1F;
			g = 1.3F;
		}

		this.Tail.yaw = -f * 0.25F * MathHelper.sin(g * 1.9F * animationProgress);
	}

	@Override
	public void render(MatrixStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
		Head.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
		Midbody.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
		Tail.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
	}
}