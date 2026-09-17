package com.jamiedev.bygone.client.models;

import com.jamiedev.bygone.client.models.animations.HauntAnimations;
import com.jamiedev.bygone.common.entity.HauntEntity;
import com.jamiedev.bygone.common.entity.MoobooEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import com.mojang.blaze3d.vertex.VertexConsumer;


public class HauntModel<T extends Entity> extends HauntingsMobModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	private final ModelPart haunt;
	private final ModelPart head;
	private final ModelPart orb;
	private final ModelPart tears;
	private final ModelPart torso;
	private final ModelPart arm1;
	private final ModelPart arm2;

	public HauntModel(ModelPart root) {
		this.haunt = root.getChild("haunt");
		this.head = this.haunt.getChild("head");
		this.orb = this.head.getChild("orb");
		this.tears = this.head.getChild("tears");
		this.torso = this.haunt.getChild("torso");
		this.arm1 = this.torso.getChild("arm1");
		this.arm2 = this.torso.getChild("arm2");
	}

	public static LayerDefinition getTexturedModelData() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition haunt = partdefinition.addOrReplaceChild("haunt", CubeListBuilder.create(), PartPose.offset(0.0F, 14.5F, -2.55F));

		PartDefinition head = haunt.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 12).addBox(-2.5F, -4.5F, -4.5F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 4.55F, 0.1745F, 0.0F, 0.0F));

		PartDefinition orb = head.addOrReplaceChild("orb", CubeListBuilder.create().texOffs(20, 12).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.0F, -2.0F, -0.1745F, 0.0F, 0.0F));

		PartDefinition tears = head.addOrReplaceChild("tears", CubeListBuilder.create().texOffs(8, 22).addBox(-2.5F, -0.5F, 0.0485F, 5.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0174F, -4.6485F, -0.1745F, 0.0F, 0.0F));

		PartDefinition torso = haunt.addOrReplaceChild("torso", CubeListBuilder.create(), PartPose.offset(0.0F, 2.0761F, 4.6673F));

		PartDefinition cube_r1 = torso.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(20, 20).addBox(-2.0F, -1.0F, -2.5F, 4.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(0, 22).addBox(-1.0F, -0.5F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition arm1 = torso.addOrReplaceChild("arm1", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -1.8827F, 0.0761F, 5.0F, 5.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.25F, 0.9239F, 1.3827F, 0.3927F, 0.0F, 0.0F));

		PartDefinition arm2 = torso.addOrReplaceChild("arm2", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-5.0F, -2.3827F, 0.0761F, 5.0F, 5.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.25F, 1.3858F, 1.574F, 0.3927F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public ModelPart root() {
		return haunt;
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);

		if (entity instanceof HauntEntity haunt)
		{
			this.animate(haunt.idleAnimationState, com.jamiedev.bygone.client.models.animations.HauntAnimations.IDLE, ageInTicks, 1.0f);
			this.animate(haunt.floatAnimationState, HauntAnimations.MOVING, ageInTicks, 1.0f);
		}
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
		haunt.render(poseStack, vertexConsumer, packedLight, packedOverlay, this.modifyColor(color));
	}
}