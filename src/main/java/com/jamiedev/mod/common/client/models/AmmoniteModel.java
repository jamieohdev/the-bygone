package com.jamiedev.mod.common.client.models;

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

// Made with Blockbench 4.11.1
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports
public class AmmoniteModel<E extends Entity> extends EntityModel<E>
{
	private final ModelPart shell;
	private final ModelPart head;
	private final ModelPart bone;
	private final ModelPart bone2;
	private final ModelPart bone3;
	private final ModelPart bone4;
	public AmmoniteModel(ModelPart root) {
		this.shell = root.getChild("shell");
		this.head = root.getChild("head");
		this.bone = root.getChild("bone");
		this.bone2 = root.getChild("bone2");
		this.bone3 = root.getChild("bone3");
		this.bone4 = root.getChild("bone4");
	}
	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();
		PartDefinition shell = modelPartData.addOrReplaceChild("shell", CubeListBuilder.create(), PartPose.offset(1.0F, 23.0F, -2.0F));

		PartDefinition cube_r1 = shell.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(18, 16).addBox(1.0F, -1.5F, -1.5F, 0.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, -7.0F, -6.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition cube_r2 = shell.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(28, 26).addBox(1.0F, -8.0F, -1.0F, 0.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, -6.0F, 5.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition cube_r3 = shell.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -8.0F, -1.0F, 4.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -5.0F, -6.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition head = modelPartData.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 16).addBox(-4.5F, -4.0F, -1.0F, 5.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, 23.0F, -2.0F));

		PartDefinition bone = modelPartData.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(0, 24).addBox(-1.0F, -3.0F, 3.0F, 1.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, 23.0F, -2.0F));

		PartDefinition bone2 = modelPartData.addOrReplaceChild("bone2", CubeListBuilder.create().texOffs(24, 0).addBox(-4.0F, -3.0F, 3.0F, 1.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, 23.0F, -2.0F));

		PartDefinition bone3 = modelPartData.addOrReplaceChild("bone3", CubeListBuilder.create().texOffs(24, 7).addBox(0.0F, -2.0F, 1.0F, 1.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition bone4 = modelPartData.addOrReplaceChild("bone4", CubeListBuilder.create().texOffs(14, 26).addBox(-3.0F, -2.0F, 1.0F, 1.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));
		return LayerDefinition.create(modelData, 64, 64);
	}
	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
	}
	@Override
	public void renderToBuffer(PoseStack matrices, VertexConsumer vertexConsumer, int light, int overlay, int color) {
		shell.render(matrices, vertexConsumer, light, overlay, color);
		head.render(matrices, vertexConsumer, light, overlay, color);
		bone.render(matrices, vertexConsumer, light, overlay, color);
		bone2.render(matrices, vertexConsumer, light, overlay, color);
		bone3.render(matrices, vertexConsumer, light, overlay, color);
		bone4.render(matrices, vertexConsumer, light, overlay, color);
	}
}