package com.jamiedev.mod.common.client.models;

import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;

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
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData shell = modelPartData.addChild("shell", ModelPartBuilder.create(), ModelTransform.pivot(1.0F, 23.0F, -2.0F));

		ModelPartData cube_r1 = shell.addChild("cube_r1", ModelPartBuilder.create().uv(18, 16).cuboid(1.0F, -1.5F, -1.5F, 0.0F, 2.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(-3.0F, -7.0F, -6.0F, 0.7854F, 0.0F, 0.0F));

		ModelPartData cube_r2 = shell.addChild("cube_r2", ModelPartBuilder.create().uv(28, 26).cuboid(1.0F, -8.0F, -1.0F, 0.0F, 8.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-3.0F, -6.0F, 5.0F, 0.7854F, 0.0F, 0.0F));

		ModelPartData cube_r3 = shell.addChild("cube_r3", ModelPartBuilder.create().uv(0, 0).cuboid(-3.0F, -8.0F, -1.0F, 4.0F, 8.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(-1.0F, -5.0F, -6.0F, -0.7854F, 0.0F, 0.0F));

		ModelPartData head = modelPartData.addChild("head", ModelPartBuilder.create().uv(0, 16).cuboid(-4.5F, -4.0F, -1.0F, 5.0F, 4.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(1.0F, 23.0F, -2.0F));

		ModelPartData bone = modelPartData.addChild("bone", ModelPartBuilder.create().uv(0, 24).cuboid(-1.0F, -3.0F, 3.0F, 1.0F, 1.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(1.0F, 23.0F, -2.0F));

		ModelPartData bone2 = modelPartData.addChild("bone2", ModelPartBuilder.create().uv(24, 0).cuboid(-4.0F, -3.0F, 3.0F, 1.0F, 1.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(1.0F, 23.0F, -2.0F));

		ModelPartData bone3 = modelPartData.addChild("bone3", ModelPartBuilder.create().uv(24, 7).cuboid(0.0F, -2.0F, 1.0F, 1.0F, 1.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		ModelPartData bone4 = modelPartData.addChild("bone4", ModelPartBuilder.create().uv(14, 26).cuboid(-3.0F, -2.0F, 1.0F, 1.0F, 1.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
		return TexturedModelData.of(modelData, 64, 64);
	}
	@Override
	public void setAngles(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
	}
	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, int color) {
		shell.render(matrices, vertexConsumer, light, overlay, color);
		head.render(matrices, vertexConsumer, light, overlay, color);
		bone.render(matrices, vertexConsumer, light, overlay, color);
		bone2.render(matrices, vertexConsumer, light, overlay, color);
		bone3.render(matrices, vertexConsumer, light, overlay, color);
		bone4.render(matrices, vertexConsumer, light, overlay, color);
	}
}