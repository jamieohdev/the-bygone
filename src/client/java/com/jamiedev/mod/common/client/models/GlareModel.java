package com.jamiedev.mod.common.client.models;
// Made with Blockbench 4.11.2
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.jamiedev.mod.common.client.models.animations.BigBeakAnimations;
import com.jamiedev.mod.common.client.models.animations.GlareAnimations;
import com.jamiedev.mod.common.entities.GlareEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;

public class GlareModel<T extends GlareEntity> extends SinglePartEntityModel<T>  {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	private final ModelPart root;
	private final ModelPart Head;
	private final ModelPart Face;
	private final ModelPart neutral;
	private final ModelPart tired;
	private final ModelPart closed;
	private final ModelPart angry;
	private final ModelPart Body;

	public GlareModel(ModelPart root) {
		this.root = root.getChild("root");
		this.Head = this.root.getChild("Head");
		this.Face = this.Head.getChild("Face");
		this.neutral = this.Face.getChild("neutral");
		this.tired = this.Face.getChild("tired");
		this.closed = this.Face.getChild("closed");
		this.angry = this.Face.getChild("angry");
		this.Body = this.root.getChild("Body");
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData meshdefinition = new ModelData();
		ModelPartData ModelPartData = meshdefinition.getRoot();

		ModelPartData root = ModelPartData.addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData Head = root.addChild("Head", ModelPartBuilder.create().uv(0, 27).cuboid(-6.0F, 1.0F, -6.0F, 12.0F, 10.0F, 12.0F, new Dilation(0.0F))
		.uv(0, 0).cuboid(-7.0F, 0.0F, -7.0F, 14.0F, 13.0F, 14.0F, new Dilation(0.0F))
		.uv(0, 80).mirrored().cuboid(-7.0F, -2.0F, -7.0F, 14.0F, 2.0F, 14.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(0.0F, 12.0F, 0.0F));

		ModelPartData Face = Head.addChild("Face", ModelPartBuilder.create(), ModelTransform.pivot(5.0F, 1.0F, -5.0F));

		ModelPartData neutral = Face.addChild("neutral", ModelPartBuilder.create().uv(104, 0).cuboid(-11.0F, 0.0F, -1.0F, 12.0F, 10.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData tired = Face.addChild("tired", ModelPartBuilder.create().uv(104, 10).cuboid(-11.0F, 0.0F, -1.0F, 12.0F, 10.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData closed = Face.addChild("closed", ModelPartBuilder.create().uv(104, 20).cuboid(-11.0F, 0.0F, -1.0F, 12.0F, 10.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData angry = Face.addChild("angry", ModelPartBuilder.create().uv(104, 30).cuboid(-11.0F, 0.0F, -1.0F, 12.0F, 10.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData Body = root.addChild("Body", ModelPartBuilder.create().uv(0, 49).mirrored().cuboid(-4.5F, -6.0F, -4.5F, 9.0F, 6.0F, 9.0F, new Dilation(0.0F)).mirrored(false)
				.uv(0, 64).mirrored().cuboid(-4.5F, -7.0F, -4.5F, 9.0F, 7.0F, 9.0F, new Dilation(0.25F)).mirrored(false), ModelTransform.pivot(0.0F, 12.0F, 0.0F));

		return TexturedModelData.of(meshdefinition, 128, 128);
	}


	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}


	@Override
	public ModelPart getPart() {
		return root;
	}

	@Override
	public void render(MatrixStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
		root.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
	}

	@Override
	public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		this.getPart().traverse().forEach(ModelPart::resetTransform);

		this.updateAnimation(entity.idleAnimationState, GlareAnimations.GLARE_FACE_IDLE, animationProgress, 1f);
		this.updateAnimation(entity.idleAnimationState, GlareAnimations.GLARE_BODY_IDLE, animationProgress, 1f);
		this.animateMovement(GlareAnimations.GLARE_BODY_MOVE, limbAngle, limbDistance,2f, 2.5f);

	}

}