package com.jamiedev.mod.common.client.models;

import com.jamiedev.mod.fabric.JamiesModFabric;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

// Made with Blockbench 4.11.1
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports
public class ScuttleSpikeModel extends Model
{
	public final ModelPart body;

	public static final Identifier TEXTURE = JamiesModFabric.getModId("textures/entity/scuttlespike.png");

	public ScuttleSpikeModel(ModelPart root) {
		super(RenderLayer::getEntitySolid);
		this.body = root.getChild("body");
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData body = modelPartData.addChild("body", ModelPartBuilder.create().uv(0, 0).cuboid(-0.5F, 19.0F, -0.5F, 1.0F, 8.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -3.0F, 0.0F));
		return TexturedModelData.of(modelData, 16, 16);
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, int color) {
		this.body.render(matrices, vertices, light, overlay, color);
	}
}