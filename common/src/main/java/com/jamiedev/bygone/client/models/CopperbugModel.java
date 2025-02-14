package com.jamiedev.bygone.client.models;

import com.jamiedev.bygone.entities.CopperbugEntity;
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
import net.minecraft.world.entity.animal.Animal;

public class CopperbugModel<C extends Animal> extends EntityModel<CopperbugEntity> {
	private final ModelPart body1;
	private final ModelPart body2;
	private final ModelPart body3;
	private final ModelPart body4;
	private final ModelPart pincerRight;
	private final ModelPart pincerLeft;


	public CopperbugModel(ModelPart root)
	{
		this.body1 = root.getChild("body1");
		this.body2 = root.getChild("body2");
		this.body3 = root.getChild("body3");
		this.body4 = root.getChild("body4");
		this.pincerRight = root.getChild("pincerRight");
		this.pincerLeft = root.getChild("pincerLeft");
	}

	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();
		PartDefinition body1 = modelPartData.addOrReplaceChild("body1", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -2.0F, 2.0F, 6.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 24.0F, -7.0F));

		PartDefinition body2 = modelPartData.addOrReplaceChild("body2", CubeListBuilder.create().texOffs(0, 8).addBox(-2.5F, -2.0F, 0.0F, 5.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition body3 = modelPartData.addOrReplaceChild("body3", CubeListBuilder.create().texOffs(0, 15).addBox(-2.0F, -2.0F, 4.0F, 4.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition body4 = modelPartData.addOrReplaceChild("body4", CubeListBuilder.create().texOffs(0, 21).addBox(-1.5F, -1.0F, 7.0F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition pincerRight = modelPartData.addOrReplaceChild("pincerRight", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition pincerRight_r1 = pincerRight.addOrReplaceChild("pincerRight_r1", CubeListBuilder.create().texOffs(15, 15).addBox(-1.0F, -1.0F, -5.0F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 0.0F, -5.0F, 0.0F, -0.2618F, 0.0F));

		PartDefinition pincerLeft = modelPartData.addOrReplaceChild("pincerLeft", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition pincerLeft_r1 = pincerLeft.addOrReplaceChild("pincerLeft_r1", CubeListBuilder.create().texOffs(19, 8).addBox(0.0F, -0.5F, -5.0F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, -0.5F, -5.0F, 0.0F, 0.2618F, 0.0F));
		return LayerDefinition.create(modelData, 32, 32);
	}
	
	@Override
	public void setupAnim(CopperbugEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch)
	{
		for(int i = 0; i < this.body1.xRot; ++i) {
			this.body1.yRot = Mth.cos(animationProgress * 0.9F + (float)i * 0.15F * 3.1415927F) * 3.1415927F * 0.05F * (float)(1 + Math.abs(i - 2));
			this.body1.x = Mth.sin(animationProgress * 0.9F + (float)i * 0.15F * 3.1415927F) * 3.1415927F * 0.2F * (float)Math.abs(i - 2);
		}

		for(int i = 0; i < this.body2.xRot; ++i) {
			this.body2.yRot = Mth.cos(animationProgress * 0.9F + (float)i * 0.15F * 3.1415927F) * 3.1415927F * 0.05F * (float)(1 + Math.abs(i - 2));
			this.body2.x = Mth.sin(animationProgress * 0.9F + (float)i * 0.15F * 3.1415927F) * 3.1415927F * 0.2F * (float)Math.abs(i - 2);
		}

		for(int i = 0; i < this.body3.xRot; ++i) {
			this.body3.yRot = Mth.cos(animationProgress * 0.9F + (float)i * 0.15F * 3.1415927F) * 3.1415927F * 0.05F * (float)(1 + Math.abs(i - 2));
			this.body3.x = Mth.sin(animationProgress * 0.9F + (float)i * 0.15F * 3.1415927F) * 3.1415927F * 0.2F * (float)Math.abs(i - 2);
		}

		for(int i = 0; i < this.body4.xRot; ++i) {
			this.body4.yRot = Mth.cos(animationProgress * 0.9F + (float)i * 0.15F * 3.1415927F) * 3.1415927F * 0.05F * (float)(1 + Math.abs(i - 2));
			this.body4.x = Mth.sin(animationProgress * 0.9F + (float)i * 0.15F * 3.1415927F) * 3.1415927F * 0.2F * (float)Math.abs(i - 2);
		}
	}
	
	@Override
	public void renderToBuffer(PoseStack matrices, VertexConsumer vertexConsumer, int light, int overlay, int color) {
		body1.render(matrices, vertexConsumer, light, overlay, color);
		body2.render(matrices, vertexConsumer, light, overlay, color);
		body3.render(matrices, vertexConsumer, light, overlay, color);
		body4.render(matrices, vertexConsumer, light, overlay, color);
		pincerRight.render(matrices, vertexConsumer, light, overlay, color);
		pincerLeft.render(matrices, vertexConsumer, light, overlay, color);
	}
}