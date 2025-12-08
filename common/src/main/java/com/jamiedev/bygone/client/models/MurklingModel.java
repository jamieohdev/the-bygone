package com.jamiedev.bygone.client.models;

import com.jamiedev.bygone.client.models.animations.MurklingAnimations;
import com.jamiedev.bygone.common.entity.MurklingEntity;
import com.jamiedev.bygone.common.entity.MurklingVariants;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;


public class MurklingModel<T extends Entity> extends HierarchicalModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	private final ModelPart root;
	private final ModelPart head;
	private final ModelPart jaw;
	private final ModelPart right_cranial;
	private final ModelPart left_cranial;
	private final ModelPart right_pectoral;
	private final ModelPart left_pectoral;
	private final ModelPart right_pelvic;
	private final ModelPart left_pelvic;
	private final ModelPart tail;
	private final ModelPart caudal_fin;

	public MurklingModel(ModelPart root) {
		this.root = root.getChild("root");
		this.head = this.root.getChild("head");
		this.jaw = this.head.getChild("jaw");
		this.right_cranial = this.head.getChild("right_cranial");
		this.left_cranial = this.head.getChild("left_cranial");
		this.right_pectoral = this.root.getChild("right_pectoral");
		this.left_pectoral = this.root.getChild("left_pectoral");
		this.right_pelvic = this.root.getChild("right_pelvic");
		this.left_pelvic = this.root.getChild("left_pelvic");
		this.tail = this.root.getChild("tail");
		this.caudal_fin = this.tail.getChild("caudal_fin");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -6.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(28, 25).addBox(0.0F, -6.0F, 2.0F, 0.0F, 12.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 18.0F, 0.0F, 0.4363F, 0.0F, 0.0F));

		PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(1, 17).addBox(-3.0F, -11.0F, -2.0F, 6.0F, 11.0F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(50, 0).addBox(-3.0F, -4.0F, -3.0F, 6.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(24, -11).addBox(0.0F, -16.0F, -3.0F, 0.0F, 16.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -6.0F, 0.0F, 1.1781F, 0.0F, 0.0F));

		PartDefinition jaw = head.addOrReplaceChild("jaw", CubeListBuilder.create().texOffs(50, 5).addBox(-3.0F, -7.0F, -1.0F, 6.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.0F, -2.0F));

		PartDefinition right_cranial = head.addOrReplaceChild("right_cranial", CubeListBuilder.create(), PartPose.offset(3.0F, -4.0F, 1.0F));

		PartDefinition cube_r1 = right_cranial.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(38, 37).mirror().addBox(0.0F, -4.0F, 0.0F, 6.0F, 7.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

		PartDefinition left_cranial = head.addOrReplaceChild("left_cranial", CubeListBuilder.create(), PartPose.offset(-3.0F, -4.0F, 1.0F));

		PartDefinition cube_r2 = left_cranial.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(38, 37).addBox(-6.0F, -4.0F, 0.0F, 6.0F, 7.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

		PartDefinition right_pectoral = root.addOrReplaceChild("right_pectoral", CubeListBuilder.create().texOffs(0, 43).addBox(-13.0F, -3.0F, -2.0F, 13.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(44, 21).addBox(-12.0F, -3.0F, -2.0F, 1.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, -3.0F, 0.0F, 0.0F, -0.9599F, 0.0F));

		PartDefinition left_pectoral = root.addOrReplaceChild("left_pectoral", CubeListBuilder.create().texOffs(0, 43).mirror().addBox(0.0F, -3.0F, -2.0F, 13.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(44, 21).mirror().addBox(11.0F, -3.0F, -2.0F, 1.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(4.0F, -3.0F, 0.0F, 0.0F, 0.9599F, 0.0F));

		PartDefinition right_pelvic = root.addOrReplaceChild("right_pelvic", CubeListBuilder.create().texOffs(38, 30).addBox(-4.0F, -1.0F, -2.0F, 4.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, 3.0F, 0.0F));

		PartDefinition left_pelvic = root.addOrReplaceChild("left_pelvic", CubeListBuilder.create().texOffs(38, 30).mirror().addBox(0.0F, -1.0F, -2.0F, 4.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(4.0F, 3.0F, 0.0F));

		PartDefinition tail = root.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(24, 16).addBox(-3.0F, 0.0F, -2.0F, 6.0F, 10.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, 0.0F));

		PartDefinition caudal_fin = tail.addOrReplaceChild("caudal_fin", CubeListBuilder.create().texOffs(0, 33).addBox(-7.0F, 0.0F, 0.0F, 14.0F, 10.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 10.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(@NotNull Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);

		if (entity instanceof MurklingEntity murkling)
		{
			this.animate(murkling.idleAnimationState, MurklingAnimations.IDLE, ageInTicks, 1.0f);
			this.animate(murkling.swimmingAnimationState, MurklingAnimations.SWIMMING, ageInTicks, 1.0f);
			this.animate(murkling.attackAnimationState, MurklingAnimations.ATTACK, ageInTicks, 1.0f);
		}
	}

	@Override
	public ModelPart root() {
		return root;
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
		root.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
	}
}