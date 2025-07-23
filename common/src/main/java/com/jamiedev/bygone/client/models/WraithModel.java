package com.jamiedev.bygone.client.models;

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

public class WraithModel<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	private final ModelPart wraith;
	private final ModelPart Head;
	private final ModelPart Body;
	private final ModelPart LeftArm;
	private final ModelPart RightArm;

	public WraithModel(ModelPart root) {
		this.wraith = root.getChild("wraith");
		this.Head = this.wraith.getChild("Head");
		this.Body = this.wraith.getChild("Body");
		this.LeftArm = this.wraith.getChild("LeftArm");
		this.RightArm = this.wraith.getChild("RightArm");
	}

	public static LayerDefinition getTexturedModelData() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition wraith = partdefinition.addOrReplaceChild("wraith", CubeListBuilder.create(), PartPose.offset(0.0F, 15.0F, 0.0F));

		PartDefinition Head = wraith.addOrReplaceChild("Head", CubeListBuilder.create(), PartPose.offset(0.0F, -14.0F, -2.0F));

		PartDefinition scream_r1 = Head.addOrReplaceChild("scream_r1", CubeListBuilder.create().texOffs(35, 47).addBox(-3.0F, -5.0F, -3.0F, 6.0F, 8.0F, 8.0F, new CubeDeformation(0.01F))
		.texOffs(30, 13).addBox(-3.0F, -5.0F, -3.0F, 6.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-4.0F, -5.0F, -3.0F, 8.0F, 12.0F, 9.0F, new CubeDeformation(0.11F)), PartPose.offsetAndRotation(0.0F, -1.0F, 0.0F, 0.2182F, 0.0F, 0.0F));

		PartDefinition Body = wraith.addOrReplaceChild("Body", CubeListBuilder.create().texOffs(30, 29).addBox(-4.0F, 2.0F, -3.0F, 8.0F, 9.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(0, 21).addBox(-4.0F, 2.0F, -3.0F, 8.0F, 16.0F, 7.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(0.0F, -15.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition LeftArm = wraith.addOrReplaceChild("LeftArm", CubeListBuilder.create().texOffs(34, 0).addBox(-0.8F, 0.0F, -2.0F, 4.0F, 9.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(29, 43).addBox(0.7F, 1.0F, -0.5F, 1.0F, 11.0F, 1.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(5.0F, -11.0F, 1.0F, -0.8727F, 0.0F, 0.0F));

		PartDefinition RightArm = wraith.addOrReplaceChild("RightArm", CubeListBuilder.create().texOffs(34, 0).mirror().addBox(-3.2F, 0.0F, -2.0F, 4.0F, 9.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(29, 43).mirror().addBox(-1.7F, 1.0F, -0.5F, 1.0F, 11.0F, 1.0F, new CubeDeformation(0.1F)).mirror(false), PartPose.offsetAndRotation(-5.0F, -11.0F, 1.0F, -0.8727F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
		wraith.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
	}
}