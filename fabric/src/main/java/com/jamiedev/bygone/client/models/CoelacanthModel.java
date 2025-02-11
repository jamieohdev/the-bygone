package com.jamiedev.bygone.client.models;// Made with Blockbench 4.11.1
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

public class CoelacanthModel<E extends Entity> extends HierarchicalModel<E> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	private final ModelPart Fish;
	private final ModelPart midbody;
	private final ModelPart midfin;
	private final ModelPart tail;
	private final ModelPart head;
	private final ModelPart lowerbody;

	SalmonModel ref;

	public CoelacanthModel(ModelPart root) {
		this.Fish = root.getChild("Fish");
		this.midbody = root.getChild("midbody");
		this.midfin = this.midbody.getChild("midfin");
		this.tail = root.getChild("tail");
		this.head = root.getChild("head");
		this.lowerbody = root.getChild("lowerbody");
	}

	public static LayerDefinition getTexturedModelData() {
		 MeshDefinition meshdefinition = new MeshDefinition();
		 PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Fish = partdefinition.addOrReplaceChild("Fish", CubeListBuilder.create(), PartPose.offset(0.0F, 27.0F, -6.0F));

		PartDefinition midbody = partdefinition.addOrReplaceChild("midbody",
				CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -7.0F, 4.0F, 8.0F, 12.0F, 11.0F,
								new CubeDeformation(0.0F))
						.texOffs(0, 33).addBox(0.0F, -11.0F, 5.0F, 0.0F, 5.0F, 8.0F, new CubeDeformation(0.0F))
						.texOffs(20, 17).addBox(0.0F, 5.0F, 8.0F, 0.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 20.0F, -5.0F));

		PartDefinition midfin = midbody.addOrReplaceChild("midfin", CubeListBuilder.create(), PartPose.offset(3.0F, 3.0F, -2.0F));

		PartDefinition cube_r1 = midfin.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(8, 41).mirror().addBox(-6.7373F, -5.0F, -1.6756F, 7.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-6.0F, 0.0F, 9.0F, 0.0F, 0.7418F, 0.0F));

		PartDefinition cube_r2 = midfin.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(8, 41).addBox(-0.2627F, -5.0F, -1.6756F, 7.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 9.0F, 0.0F, -0.7418F, 0.0F));

		PartDefinition tail = partdefinition.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(38, 0).addBox(-1.0F, -9.0F, 11.0F, 2.0F, 8.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 6.0F));

		PartDefinition cube_r3 = tail.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(28, 32).addBox(0.0F, -8.0F, -1.0F, 0.0F, 9.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -4.0F, 14.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition cube_r4 = head.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 23)
						.addBox(-3.0F, -8.1736F, -7.9848F, 6.0F, 10.0F, 8.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -2.0F, 1.0F, 0.1745F, 0.0F, 0.0F));

		PartDefinition lowerbody = partdefinition.addOrReplaceChild("lowerbody", CubeListBuilder.create().texOffs(28, 23).addBox(-3.0F, -10.0F, 3.0F, 6.0F, 10.0F, 7.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(0.0F, -14.0F, 4.0F, 0.0F, 4.0F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(0, 19).addBox(0.0F, 0.0F, 7.0F, 0.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 7.0F));


		return LayerDefinition.create(meshdefinition, 64, 64);
	}



	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
		Fish.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
		midbody.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
		tail.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
		head.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
		lowerbody.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
	}

	public ModelPart root() {
		return this.Fish;
	}
	@Override
	public void setupAnim(E entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		float f = 1.0F;
		float g = 1.0F;
		if (!entity.isInWater()) {
			f = 1.1F;
			g = 1.3F;
		}
		this.lowerbody.yRot = -f * 0.25F * Mth.sin(g * 0.9F * animationProgress);
		this.tail.yRot = -f * 0.25F * Mth.sin(g * 1.4F * animationProgress);
	}
}