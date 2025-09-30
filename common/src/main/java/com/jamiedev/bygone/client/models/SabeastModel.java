package com.jamiedev.bygone.client.models;

import com.jamiedev.bygone.client.models.animations.WraithAnimations;
import com.jamiedev.bygone.common.entity.SabeastEntity;
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
import net.minecraft.client.renderer.entity.PolarBearRenderer;
import net.minecraft.world.entity.Entity;
public class SabeastModel<T extends Entity> extends HierarchicalModel<T>
{
	PolarBearModel ref;

	PolarBearRenderer ref2;

	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	private final ModelPart base;
	private final ModelPart lower_body;
	private final ModelPart tail;
	private final ModelPart torso;
	private final ModelPart upper_body;
	private final ModelPart head;
	private final ModelPart left_ear;
	private final ModelPart right_ear;
	private final ModelPart snout;
	private final ModelPart right_arm;
	private final ModelPart left_arm;
	private final ModelPart left_leg;
	private final ModelPart right_leg;

	public SabeastModel(ModelPart root) {
		this.base = root.getChild("base");
		this.lower_body = this.base.getChild("lower_body");
		this.tail = this.lower_body.getChild("tail");
		this.torso = this.lower_body.getChild("torso");
		this.upper_body = this.torso.getChild("upper_body");
		this.head = this.upper_body.getChild("head");
		this.left_ear = this.head.getChild("left_ear");
		this.right_ear = this.head.getChild("right_ear");
		this.snout = this.head.getChild("snout");
		this.right_arm = this.upper_body.getChild("right_arm");
		this.left_arm = this.upper_body.getChild("left_arm");
		this.left_leg = this.base.getChild("left_leg");
		this.right_leg = this.base.getChild("right_leg");
	}

	public static LayerDefinition getTexturedModelData() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition base = partdefinition.addOrReplaceChild("base", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition lower_body = base.addOrReplaceChild("lower_body", CubeListBuilder.create().texOffs(0, 42).addBox(-5.0F, -30.0F, -5.0F, 10.0F, 30.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -7.0F, 3.0F, 0.2618F, 0.0F, 0.0F));

		PartDefinition tail = lower_body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(64, 68).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(84, 88).addBox(-2.0F, -2.0F, 14.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, 4.0F, -0.6109F, 0.0F, 0.0F));

		PartDefinition torso = lower_body.addOrReplaceChild("torso", CubeListBuilder.create(), PartPose.offset(0.0F, -29.0F, -0.05F));

		PartDefinition upper_body = torso.addOrReplaceChild("upper_body", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -16.0F, -8.0F, 12.0F, 30.0F, 12.0F, new CubeDeformation(0.01F))
		.texOffs(40, 68).addBox(0.0F, -9.0F, -3.0F, 0.0F, 30.0F, 12.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(0.0F, -4.0F, -8.0F, 1.5708F, 0.0F, 0.0F));

		PartDefinition head = upper_body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 82).addBox(-5.0F, -7.0F, -4.0F, 10.0F, 7.0F, 9.0F, new CubeDeformation(0.0F))
		.texOffs(48, 26).addBox(-8.0F, -3.0F, -4.0F, 16.0F, 0.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -16.0F, -2.0F, -0.1745F, 0.0F, 0.0F));

		PartDefinition left_ear = head.addOrReplaceChild("left_ear", CubeListBuilder.create().texOffs(48, 35).addBox(-1.0F, -1.0F, -2.0F, 3.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, -2.0F, 4.0F));

		PartDefinition right_ear = head.addOrReplaceChild("right_ear", CubeListBuilder.create().texOffs(64, 35).addBox(-2.0F, -1.0F, -2.0F, 3.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, -2.0F, 4.0F));

		PartDefinition snout = head.addOrReplaceChild("snout", CubeListBuilder.create().texOffs(64, 88).addBox(-3.0F, -5.0F, -2.0F, 6.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -7.0F, 0.0F));

		PartDefinition right_arm = upper_body.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(48, 0).mirror().addBox(-2.0F, -3.0F, -20.0F, 4.0F, 6.0F, 20.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-6.0F, -4.0F, -2.0F, -0.2618F, 0.0F, 0.0F));

		PartDefinition left_arm = upper_body.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(48, 0).addBox(-2.0F, -3.0F, -20.0F, 4.0F, 6.0F, 20.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.0F, -4.0F, -2.0F, -0.2618F, 0.0F, 0.0F));

		PartDefinition left_leg = base.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(88, 35).addBox(-1.91F, 0.0F, -2.5F, 4.0F, 8.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(2.9F, -8.0F, 2.5F));

		PartDefinition right_leg = base.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(88, 35).mirror().addBox(-1.99F, 0.0F, -2.5F, 4.0F, 8.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-3.0F, -8.0F, 2.5F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
		base.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
	}

	@Override
	public ModelPart root() {
		return base;
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);

		if (entity instanceof SabeastEntity sabeast)
		{
			float f = ageInTicks - (float)entity.tickCount;
			float f1 = ((SabeastEntity) entity).getStandingAnimationScale(f);
			f1 *= f1;

		}
	}
}