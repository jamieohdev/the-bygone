package com.jamiedev.bygone.client.models;

import com.jamiedev.bygone.client.models.animations.SabeastAnimations;
import com.jamiedev.bygone.common.entity.SabeastEntity;
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
import net.minecraft.world.entity.Entity;
public class SabeastModel<T extends Entity> extends HierarchicalModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	private final ModelPart root;
	private final ModelPart body_default;
	private final ModelPart body;
	private final ModelPart body2;
	private final ModelPart head;
	private final ModelPart head2;
	private final ModelPart tail_default;
	private final ModelPart tail;
	private final ModelPart tail2;
	private final ModelPart left_arm_default;
	private final ModelPart left_arm;
	private final ModelPart right_arm_default;
	private final ModelPart right_arm;
	private final ModelPart left_leg;
	private final ModelPart right_leg;

	public SabeastModel(ModelPart root) {
		this.root = root.getChild("root");
		this.body_default = this.root.getChild("body_default");
		this.body = this.body_default.getChild("body");
		this.body2 = this.body.getChild("body2");
		this.head = this.body2.getChild("head");
		this.head2 = this.head.getChild("head2");
		this.tail_default = this.body2.getChild("tail_default");
		this.tail = this.tail_default.getChild("tail");
		this.tail2 = this.tail.getChild("tail2");
		this.left_arm_default = this.body.getChild("left_arm_default");
		this.left_arm = this.left_arm_default.getChild("left_arm");
		this.right_arm_default = this.body.getChild("right_arm_default");
		this.right_arm = this.right_arm_default.getChild("right_arm");
		this.left_leg = this.root.getChild("left_leg");
		this.right_leg = this.root.getChild("right_leg");
	}

	public static LayerDefinition getTexturedModelData() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 11.0F));

		PartDefinition body_default = root.addOrReplaceChild("body_default", CubeListBuilder.create(), PartPose.offset(0.0F, -7.0F, 3.0F));

		PartDefinition body = body_default.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition body2 = body.addOrReplaceChild("body2", CubeListBuilder.create().texOffs(54, 42).addBox(-5.0F, -30.0F, -5.0F, 10.0F, 30.0F, 10.0F, new CubeDeformation(0.01F))
		.texOffs(0, 0).addBox(-6.0F, -38.0F, -24.0F, 12.0F, 12.0F, 30.0F, new CubeDeformation(0.0F))
		.texOffs(0, 42).addBox(0.0F, -41.0F, -17.0F, 0.0F, 10.0F, 27.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition head = body2.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, -32.5F, -24.0F));

		PartDefinition head2 = head.addOrReplaceChild("head2", CubeListBuilder.create().texOffs(40, 82).addBox(-5.0F, -4.5F, -7.0F, 10.0F, 9.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(84, 19).addBox(-3.0F, -1.5F, -12.0F, 6.0F, 4.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(84, 0).addBox(-8.0F, -1.5F, -3.0F, 16.0F, 6.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(94, 36).addBox(3.0F, -6.5F, -3.0F, 3.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(94, 36).mirror().addBox(-6.0F, -6.5F, -3.0F, 3.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition tail_default = body2.addOrReplaceChild("tail_default", CubeListBuilder.create(), PartPose.offset(0.0F, -1.0F, 4.0F));

		PartDefinition tail = tail_default.addOrReplaceChild("tail", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition tail2 = tail.addOrReplaceChild("tail2", CubeListBuilder.create().texOffs(0, 79).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(84, 28).addBox(-2.0F, -2.0F, 14.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition left_arm_default = body.addOrReplaceChild("left_arm_default", CubeListBuilder.create(), PartPose.offset(6.0F, -29.0F, -12.0F));

		PartDefinition left_arm = left_arm_default.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(74, 82).addBox(-2.0F, -3.0F, -3.0F, 4.0F, 20.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition right_arm_default = body.addOrReplaceChild("right_arm_default", CubeListBuilder.create(), PartPose.offset(-6.0F, -29.0F, -12.0F));

		PartDefinition right_arm = right_arm_default.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(74, 82).mirror().addBox(-2.0F, -3.0F, -3.0F, 4.0F, 20.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition left_leg = root.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(84, 6).addBox(-1.9F, 0.0F, -2.5F, 4.0F, 8.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(2.9F, -8.0F, 2.5F));

		PartDefinition right_leg = root.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(84, 6).mirror().addBox(-2.0F, 0.0F, -2.5F, 4.0F, 8.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-3.0F, -8.0F, 2.5F));


		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
		root.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
	}

	@Override
	public ModelPart root() {
		return root;
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);

		if (entity instanceof SabeastEntity sabeast)
		{
			this.animate(sabeast.idleAnimationState, SabeastAnimations.IDLE, ageInTicks, 1.0f);
			this.animate(sabeast.walkAnimationState, SabeastAnimations.WALK, ageInTicks, 1.0f);
			this.animate(sabeast.meleeAnimationState, SabeastAnimations.ATTACK, ageInTicks, 1.0f);
		}
	}
}