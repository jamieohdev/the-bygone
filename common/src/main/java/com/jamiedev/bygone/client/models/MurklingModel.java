package com.jamiedev.bygone.client.models;

import com.jamiedev.bygone.client.models.animations.LithyAnimations;
import com.jamiedev.bygone.common.entity.LithyEntity;
import com.jamiedev.bygone.common.entity.MurklingEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.ZombieModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;

public class MurklingModel<T extends MurklingEntity> extends HierarchicalModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	private final ModelPart root;
	private final ModelPart head;
	private final ModelPart arm0;
	private final ModelPart arm1;
	private final ModelPart arm2;
	private final ModelPart arm3;
	private final ModelPart fin;
	private final ModelPart fin2;

	public MurklingModel(ModelPart root) {
		this.root = root;
		this.head = root.getChild("head");
		this.arm0 = root.getChild("arm0");
		this.arm1 = root.getChild("arm1");
		this.arm2 = root.getChild("arm2");
		this.arm3 = root.getChild("arm3");
		this.fin =  root.getChild("fin");
		this.fin2 = this.fin.getChild("fin2");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -6.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(20, 34).addBox(0.0F, -6.0F, 2.0F, 0.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 18.0F, 0.0F));

		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 16).addBox(-3.0F, -10.0F, -3.0F, 6.0F, 10.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(24, 0).addBox(0.0F, -12.0F, -3.0F, 0.0F, 12.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -6.0F, 0.0F));

		PartDefinition cube_r1 = head.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(40, 0).addBox(0.0F, -3.0F, 0.0F, 6.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -5.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

		PartDefinition cube_r2 = head.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(40, 0).addBox(-6.0F, -3.0F, 0.0F, 6.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, -5.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

		PartDefinition arm0 = partdefinition.addOrReplaceChild("arm0", CubeListBuilder.create().texOffs(28, 34).addBox(-4.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, -3.0F, 0.0F));

		PartDefinition arm1 = partdefinition.addOrReplaceChild("arm1", CubeListBuilder.create().texOffs(28, 34).addBox(-4.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, 3.0F, 0.0F));

		PartDefinition arm2 = partdefinition.addOrReplaceChild("arm2", CubeListBuilder.create().texOffs(28, 34).addBox(0.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, -3.0F, 0.0F));

		PartDefinition arm3 = partdefinition.addOrReplaceChild("arm3", CubeListBuilder.create().texOffs(28, 34).addBox(0.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, 3.0F, 0.0F));

		PartDefinition fin = partdefinition.addOrReplaceChild("fin", CubeListBuilder.create().texOffs(24, 20).addBox(-3.0F, 0.0F, -2.0F, 6.0F, 10.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, 0.0F));

		PartDefinition fin2 = fin.addOrReplaceChild("fin2", CubeListBuilder.create().texOffs(0, 32).addBox(-5.0F, 0.0F, 0.0F, 10.0F, 10.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 10.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(MurklingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

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