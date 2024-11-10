package com.jamiedev.mod.common.client.models;// Made with Blockbench 4.11.1
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.SalmonEntityModel;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class CoelacanthModel<E extends Entity> extends SinglePartEntityModel<E> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	private final ModelPart Fish;
	private final ModelPart midbody;
	private final ModelPart midfin;
	private final ModelPart tail;
	private final ModelPart head;
	private final ModelPart lowerbody;

	SalmonEntityModel ref;

	public CoelacanthModel(ModelPart root) {
		this.Fish = root.getChild("Fish");
		this.midbody = root.getChild("midbody");
		this.midfin = this.midbody.getChild("midfin");
		this.tail = root.getChild("tail");
		this.head = root.getChild("head");
		this.lowerbody = root.getChild("lowerbody");
	}

	public static TexturedModelData getTexturedModelData() {
		 ModelData meshdefinition = new ModelData();
		 ModelPartData partdefinition = meshdefinition.getRoot();

		 ModelPartData Fish = partdefinition.addChild("Fish", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 27.0F, -6.0F));

		 ModelPartData midbody = partdefinition.addChild("midbody", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -7.0F, -5.0F, 8.0F, 12.0F, 11.0F, new Dilation(0.0F))
		.uv(0, 33).cuboid(0.0F, -11.0F, -4.0F, 0.0F, 5.0F, 8.0F, new Dilation(0.0F))
		.uv(20, 17).cuboid(0.0F, 5.0F, -1.0F, 0.0F, 3.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 20.0F, -5.0F));

		 ModelPartData midfin = midbody.addChild("midfin", ModelPartBuilder.create(), ModelTransform.pivot(3.0F, 3.0F, -2.0F));

		 ModelPartData cube_r1 = midfin.addChild("cube_r1", ModelPartBuilder.create().uv(8, 41).mirrored().cuboid(-6.7373F, -5.0F, -1.6756F, 7.0F, 5.0F, 0.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-6.0F, 0.0F, 0.0F, 0.0F, 0.7418F, 0.0F));

		 ModelPartData cube_r2 = midfin.addChild("cube_r2", ModelPartBuilder.create().uv(8, 41).cuboid(-0.2627F, -5.0F, -1.6756F, 7.0F, 5.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -0.7418F, 0.0F));

		 ModelPartData tail = partdefinition.addChild("tail", ModelPartBuilder.create().uv(38, 0).cuboid(-1.0F, -9.0F, 2.0F, 2.0F, 8.0F, 7.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 6.0F));

		 ModelPartData cube_r3 = tail.addChild("cube_r3", ModelPartBuilder.create().uv(28, 32).cuboid(0.0F, -8.0F, -1.0F, 0.0F, 9.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -4.0F, 5.0F, -0.7854F, 0.0F, 0.0F));

		 ModelPartData head = partdefinition.addChild("head", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		 ModelPartData cube_r4 = head.addChild("cube_r4", ModelPartBuilder.create().uv(0, 23).cuboid(-3.0F, -8.1736F, -7.9848F, 6.0F, 10.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -2.0F, -8.0F, 0.1745F, 0.0F, 0.0F));

		 ModelPartData lowerbody = partdefinition.addChild("lowerbody", ModelPartBuilder.create().uv(28, 23).cuboid(-3.0F, -10.0F, -6.0F, 6.0F, 10.0F, 7.0F, new Dilation(0.0F))
		.uv(0, 0).cuboid(0.0F, -14.0F, -5.0F, 0.0F, 4.0F, 5.0F, new Dilation(0.0F))
		.uv(0, 19).cuboid(0.0F, 0.0F, -2.0F, 0.0F, 4.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 7.0F));

		return TexturedModelData.of(meshdefinition, 64, 64);
	}



	@Override
	public void render(MatrixStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
		Fish.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
		midbody.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
		tail.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
		head.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
		lowerbody.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
	}

	public ModelPart getPart() {
		return this.Fish;
	}
	@Override
	public void setAngles(E entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		float f = 1.0F;
		float g = 1.0F;
		if (!entity.isTouchingWater()) {
			f = 1.1F;
			g = 1.3F;
		}
		this.lowerbody.yaw = -f * 0.25F * MathHelper.sin(g * 0.9F * animationProgress);
		this.tail.yaw = -f * 0.25F * MathHelper.sin(g * 1.4F * animationProgress);
	}
}