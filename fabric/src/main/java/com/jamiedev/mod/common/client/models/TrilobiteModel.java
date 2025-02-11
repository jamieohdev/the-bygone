package com.jamiedev.mod.common.client.models;// Made with Blockbench 4.11.1
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

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
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

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

	public static LayerDefinition getTexturedModelData() {
		MeshDefinition meshdefinition = new MeshDefinition();
		 PartDefinition partdefinition = meshdefinition.getRoot();

		 PartDefinition Head = partdefinition.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(0, 13).addBox(-4.0F, -4.25F, -1.0F, 8.0F, 0.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-5.0F, -4.0F, -1.0F, 10.0F, 4.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, -1.0F));

		 PartDefinition cube_r1 = Head.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(14, 23).addBox(0.0F, -2.0F, -3.0F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)),PartPose.offsetAndRotation(4.0F, -0.25F, -2.0F, 0.0F, -0.3927F, 0.0F));

		 PartDefinition cube_r2 = Head.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(14, 23).mirror().addBox(-2.0F, -2.0F, -3.0F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false),PartPose.offsetAndRotation(-4.0F, -0.25F, -2.0F, 0.0F, 0.3927F, 0.0F));

		 PartDefinition cube_r3 = Head.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.0F, -2.0F, -1.0F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false),PartPose.offsetAndRotation(2.0F, -3.0F, 1.0F, 0.0F, 0.0F, 0.5672F));

		 PartDefinition cube_r4 = Head.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 0).addBox(1.0F, -2.0F, -1.0F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)),PartPose.offsetAndRotation(-2.0F, -3.0F, 1.0F, 0.0F, 0.0F, -0.5672F));

		 PartDefinition Midbody = partdefinition.addOrReplaceChild("Midbody", CubeListBuilder.create(), PartPose.offset(0.0F, 23.0F, -1.0F));

		 PartDefinition cube_r5 = Midbody.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 21).addBox(-3.0F, -2.4226F, -3.9063F, 6.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)),PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3491F, 0.0F, 0.0F));

		 PartDefinition Tail = partdefinition.addOrReplaceChild("Tail", CubeListBuilder.create().texOffs(17, 14).addBox(-1.0F, -2.0F, -5.0F, 2.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, -6.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(E entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		float f = 1.0F;
		float g = 1.0F;
		if (!entity.isInWater()) {
			f = 1.1F;
			g = 1.3F;
		}

		this.Tail.yRot = -f * 0.25F * Mth.sin(g * 1.9F * animationProgress);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
		Head.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
		Midbody.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
		Tail.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
	}
}