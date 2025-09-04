package com.jamiedev.bygone.client.models;

import com.jamiedev.bygone.client.models.animations.LithyAnimations;
import com.jamiedev.bygone.client.models.animations.WraithAnimations;
import com.jamiedev.bygone.common.entity.WraithEntity;
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
public class WraithModel<T extends Entity> extends HierarchicalModel<T>{
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	private final ModelPart root;
	private final ModelPart body;
	private final ModelPart head;
	private final ModelPart left_arm;
	private final ModelPart left_arm2;
	private final ModelPart right_arm;
	private final ModelPart right_arm2;
//	private final ModelPart camera;

	public WraithModel(ModelPart root) {
		this.root = root.getChild("root");
		this.body = this.root.getChild("body");
		this.head = this.body.getChild("head");
		this.left_arm = this.body.getChild("left_arm");
		this.left_arm2 = this.left_arm.getChild("left_arm2");
		this.right_arm = this.body.getChild("right_arm");
		this.right_arm2 = this.right_arm.getChild("right_arm2");
	//	this.camera = root.getChild("camera");
	}

	public static LayerDefinition getTexturedModelData() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 15.0F, 0.5F));

		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(34, 0).addBox(-4.0F, -7.0F, -2.5F, 8.0F, 9.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(0, 21).addBox(-4.0F, -7.0F, -2.5F, 8.0F, 16.0F, 7.0F, new CubeDeformation(0.125F)), PartPose.offset(0.0F, -6.0F, -0.5F));

		PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -7.0F, -4.5F, 8.0F, 12.0F, 9.0F, new CubeDeformation(0.25F))
		.texOffs(30, 21).addBox(-3.0F, -7.0F, -4.5F, 6.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -7.0F, 0.0F));

		PartDefinition left_arm = body.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offset(6.0F, -3.0F, 1.0F));

		PartDefinition left_arm2 = left_arm.addOrReplaceChild("left_arm2", CubeListBuilder.create().texOffs(30, 37).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 9.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 44).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 11.0F, 1.0F, new CubeDeformation(0.125F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition right_arm = body.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(-6.0F, -3.0F, 1.0F));

		PartDefinition right_arm2 = right_arm.addOrReplaceChild("right_arm2", CubeListBuilder.create().texOffs(30, 37).mirror().addBox(-2.0F, -2.0F, -2.0F, 4.0F, 9.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 44).mirror().addBox(-0.5F, -1.0F, -0.5F, 1.0F, 11.0F, 1.0F, new CubeDeformation(0.125F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition camera = partdefinition.addOrReplaceChild("camera", CubeListBuilder.create(), PartPose.offset(0.0F, 8.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);

		if (entity instanceof WraithEntity wraith)
		{
			this.animate(wraith.idleAnimationState, WraithAnimations.idle, ageInTicks, 1.0f);
			this.animate(wraith.floatAnimationState, WraithAnimations.floating, ageInTicks, 1.0f);
			this.animate(wraith.meleeAnimationState, WraithAnimations.attack, ageInTicks, 1.0f);
			this.animate(wraith.spellAnimationState, WraithAnimations.cast, ageInTicks, 1.0f);
		}
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
		root.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
	//	camera.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
	}

	@Override
	public ModelPart root() {
		return root;
	}
}