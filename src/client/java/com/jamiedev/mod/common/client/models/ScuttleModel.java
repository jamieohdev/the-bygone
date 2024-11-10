package com.jamiedev.mod.common.client.models;

import com.jamiedev.mod.common.entities.ScuttleEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.GuardianEntityModel;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

// Made with Blockbench 4.11.1
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports
public class ScuttleModel extends SinglePartEntityModel<ScuttleEntity> {
	GuardianEntityModel ref;
	private final ModelPart eye;
	private final ModelPart body;
	private final ModelPart tail3;
	private final ModelPart knub1;
	private final ModelPart knub2;
	private final ModelPart knub3;
	private final ModelPart knub4;
	private final ModelPart spine1;
	private final ModelPart spine2;
	private final ModelPart spine3;
	private final ModelPart spine4;
	private final ModelPart root;

	final ModelPart[] tail;

	private static final String EYE = "eye";

	public ScuttleModel(ModelPart root) {
		this.root = root;
		this.body = root.getChild("body");
		this.tail3 = this.body.getChild("tail3");
		this.knub4 = this.body.getChild("knub4");
		this.knub3 = this.body.getChild("knub3");
		this.knub2 = this.body.getChild("knub2");
		this.knub1 = this.body.getChild("knub1");
		this.eye = this.body.getChild("eye");

		this.tail = new ModelPart[2];


		this.spine1 = this.body.getChild("spine1");
		this.spine2 = this.body.getChild("spine2");
		this.spine3 = this.body.getChild("spine3");
		this.spine4 = this.body.getChild("spine4");
	}

	public ModelPart getPart() {
		return this.root;
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();

		ModelPartData body = modelPartData.addChild("body", ModelPartBuilder.create().uv(0, 0).cuboid(-6.0F, -6.0F, -8.0F, 12.0F, 4.0F, 12.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 20.0F, 0.0F));
		body.addChild("eye", ModelPartBuilder.create().uv(32, 21).cuboid(-1.0F, 15.5F, 0.0F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -19.5F, -8.25F));

		ModelPartData tail3 = body.addChild("tail3", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -5.0F, 0.0F));

		ModelPartData tail3_r1 = tail3.addChild("tail3_r1", ModelPartBuilder.create().uv(0, 16).cuboid(0.0F, -2.5F, 7.0F, 1.0F, 7.0F, 7.0F, new Dilation(0.0F))
				.uv(24, 24).cuboid(-1.0F, 0.0F, 4.0F, 2.0F, 2.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0436F, 0.0F));

		ModelPartData knub4 = body.addChild("knub4", ModelPartBuilder.create(), ModelTransform.pivot(6.0F, 4.0F, 7.0F));

		ModelPartData knub4_r1 = knub4.addChild("knub4_r1", ModelPartBuilder.create().uv(32, 16).cuboid(-1.0F, -3.0F, -1.0F, 2.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-3.0F, -4.0F, -5.0F, 0.2618F, 0.0F, 0.0F));

		ModelPartData knub3 = body.addChild("knub3", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 4.0F, 7.0F));

		ModelPartData knub3_r1 = knub3.addChild("knub3_r1", ModelPartBuilder.create().uv(16, 32).cuboid(-1.0F, -3.0F, -1.0F, 2.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-3.0F, -4.0F, -5.0F, 0.2618F, 0.0F, 0.0F));

		ModelPartData knub2 = body.addChild("knub2", ModelPartBuilder.create(), ModelTransform.pivot(6.0F, 4.0F, 0.0F));

		ModelPartData knub2_r1 = knub2.addChild("knub2_r1", ModelPartBuilder.create().uv(8, 30).cuboid(-1.0F, -3.0F, -1.0F, 2.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-3.0F, -4.0F, -5.0F, 0.2618F, 0.0F, 0.0F));

		ModelPartData knub1 = body.addChild("knub1", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 4.0F, 0.0F));

		ModelPartData knub1_r1 = knub1.addChild("knub1_r1", ModelPartBuilder.create().uv(0, 30).cuboid(-1.0F, -3.0F, -1.0F, 2.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-3.0F, -4.0F, -5.0F, 0.2618F, 0.0F, 0.0F));

		ModelPartData spine1 = body.addChild("spine1", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 4.0F, 0.0F));

		ModelPartData spine1_r1 = spine1.addChild("spine1_r1", ModelPartBuilder.create().uv(24, 16).cuboid(-1.0F, -6.0F, -1.0F, 2.0F, 6.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-4.0F, -10.0F, -4.0F, -0.2618F, 0.0F, 0.0F));

		ModelPartData spine2 = body.addChild("spine2", ModelPartBuilder.create(), ModelTransform.pivot(6.0F, 4.0F, 1.0F));

		ModelPartData spine1_r2 = spine2.addChild("spine1_r2", ModelPartBuilder.create().uv(24, 29).cuboid(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-3.0F, -10.0F, -6.0F, -0.2618F, 0.0F, 0.0F));

		ModelPartData spine3 = body.addChild("spine3", ModelPartBuilder.create(), ModelTransform.pivot(2.0F, 4.0F, 7.0F));

		ModelPartData spine1_r3 = spine3.addChild("spine1_r3", ModelPartBuilder.create().uv(16, 16).cuboid(-1.0F, -7.0F, -1.0F, 2.0F, 7.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-4.0F, -10.0F, -6.0F, -0.2618F, 0.0F, 0.0F));

		ModelPartData spine4 = body.addChild("spine4", ModelPartBuilder.create(), ModelTransform.pivot(7.0F, 4.0F, 6.0F));

		ModelPartData spine1_r4 = spine4.addChild("spine1_r4", ModelPartBuilder.create().uv(16, 25).cuboid(-1.0F, -5.0F, -1.0F, 2.0F, 5.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-4.0F, -10.0F, -6.0F, -0.2618F, 0.0F, 0.0F));
		return TexturedModelData.of(modelData, 64, 64);
	}


	@Override
	public void setAngles(ScuttleEntity guardianEntity, float f, float g, float h, float i, float j) {
		float k = h - (float) guardianEntity.age;
		this.body.yaw = (float) (i * 0.017453292F);
		this.body.pitch = (float) (j * 0.017453292F);

		Entity entity = MinecraftClient.getInstance().getCameraEntity();

		if (guardianEntity.hasProjTarget())
		{
			entity = guardianEntity.getProjTarget();
		}

		if (entity != null) {
			Vec3d vec3d = ((Entity)entity).getCameraPosVec(0.0F);
			Vec3d vec3d2 = guardianEntity.getCameraPosVec(0.0F);
			double d = vec3d.y - vec3d2.y;
			if (d > 0.0) {
				this.eye.pivotY = -19.5F;
			} else {
				this.eye.pivotY = -20.5F;
			}

			Vec3d vec3d3 = guardianEntity.getRotationVec(0.0F);
			vec3d3 = new Vec3d(vec3d3.x, 0.0, vec3d3.z);
			Vec3d vec3d4 = (new Vec3d(vec3d2.x - vec3d.x, 0.0, vec3d2.z - vec3d.z)).normalize().rotateY(1.5707964F);
			double e = vec3d3.dotProduct(vec3d4);
			this.eye.pivotX = MathHelper.sqrt((float)Math.abs(e)) * 2.0F * (float)Math.signum(e);
		}

		this.eye.visible = true;
		float m = guardianEntity.getTailAngle(k);
		this.tail3.yaw = MathHelper.sin(m) * 3.1415927F * 0.15F;
	}

	private static float getAngle(int index, float animationProgress, float magnitude) {
		return 1.0F + MathHelper.cos(animationProgress * 1.5F + (float)index) * 0.01F - magnitude;
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, int color)  {
		body.render(matrices, vertexConsumer, light, overlay, color);
		//tail3.render(matrices, vertexConsumer, light, overlay, color);

	}
}