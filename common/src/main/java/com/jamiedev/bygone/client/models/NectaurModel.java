package com.jamiedev.bygone.client.models;
import com.google.common.collect.ImmutableList;
import com.jamiedev.bygone.client.models.animations.GlareAnimations;
import com.jamiedev.bygone.common.entity.GlareEntity;
import com.jamiedev.bygone.common.entity.NectaurEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
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
import net.minecraft.world.phys.Vec3;

public class NectaurModel<T extends NectaurEntity> extends EntityModel<T>  {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	private final ModelPart root;
	private final ModelPart body;
	private final ModelPart head;
	private final ModelPart frill3;
	private final ModelPart frill2;
	private final ModelPart frill4;
	private final ModelPart leg1;
	private final ModelPart leg2;
	private final ModelPart tail;

	GuardianModel ref;

	public NectaurModel(ModelPart root) {
		this.root = root.getChild("root");
		this.body = this.root.getChild("body");
		this.head = this.body.getChild("head");
		this.frill3 = this.head.getChild("frill3");
		this.frill2 = this.head.getChild("frill2");
		this.frill4 = this.head.getChild("frill4");
		this.tail = this.body.getChild("tail");
		this.leg1 = this.body.getChild("leg1");
		this.leg2 = this.body.getChild("leg2");
	}

	public static LayerDefinition getTexturedModelData() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 56.0F, 0.0F));

		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 13).addBox(-4.0F, -4.0F, -5.0F, 7.0F, 8.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, -13.0F, 1.0F));

		PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(32, 13).addBox(-1.5F, -9.0F, -2.0F, 3.0F, 11.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(24, 30).addBox(-1.5F, -17.0F, 2.0F, 3.0F, 11.0F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(0, 30).addBox(-0.5F, -17.0F, -9.0F, 1.0F, 1.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 0.0F, -6.0F));

		PartDefinition frill3 = head.addOrReplaceChild("frill3", CubeListBuilder.create().texOffs(40, 44).addBox(-2.0F, 0.0F, 0.0F, 3.0F, 0.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, -17.0F, 7.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition frill2 = head.addOrReplaceChild("frill2", CubeListBuilder.create(), PartPose.offset(-1.5F, -12.0F, 7.0F));

		PartDefinition frill_r1 = frill2.addOrReplaceChild("frill_r1", CubeListBuilder.create().texOffs(54, 28).mirror().addBox(-1.0F, -13.0F, 0.0F, 0.0F, 11.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(1.0F, 8.0F, 0.0F, 0.0F, -0.1309F, 0.0F));

		PartDefinition frill4 = head.addOrReplaceChild("frill4", CubeListBuilder.create(), PartPose.offset(1.5F, -12.0F, 7.0F));

		PartDefinition frill_r2 = frill4.addOrReplaceChild("frill_r2", CubeListBuilder.create().texOffs(54, 28).addBox(1.0F, -13.0F, 0.0F, 0.0F, 11.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 8.0F, 0.0F, 0.0F, 0.1309F, 0.0F));

		PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(22, 34).addBox(-0.5F, -9.0F, 0.0F, 1.0F, 10.0F, 20.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, -4.0F, 4.0F, -0.9599F, 0.0F, 0.0F));

		PartDefinition leg1 = body.addOrReplaceChild("leg1", CubeListBuilder.create().texOffs(0, 42).addBox(-1.0F, 6.0F, -2.0F, 3.0F, 7.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(38, 0).addBox(-1.0F, -2.0F, -2.0F, 3.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(2.5F, 0.0F, 0.0F));

		PartDefinition leg2 = body.addOrReplaceChild("leg2", CubeListBuilder.create().texOffs(0, 42).mirror().addBox(-2.0F, 6.0F, -2.0F, 3.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(38, 0).mirror().addBox(-2.0F, -2.0F, -2.0F, 3.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-3.5F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(NectaurEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		//this.head.xRot = headPitch * ((float)Math.PI / 180F);
		//this.head.yRot = netHeadYaw * ((float)Math.PI / 180F);
		this.frill2.xRot = this.head.xRot;
		this.frill2.yRot = this.head.yRot;
		this.frill3.xRot = this.head.xRot;
		this.frill3.yRot = this.head.yRot;
		this.frill4.xRot = this.head.xRot;
		this.frill4.yRot = this.head.yRot;
		this.leg2.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
		this.leg1.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;

		Entity player = Minecraft.getInstance().getCameraEntity();

		/*if (entity.hasActiveAttackTarget()) {
			player = entity.getActiveAttackTarget();
		}*/

		if (player != null) {
			Vec3 vec3 = player.getEyePosition(0.0F);
			Vec3 vec31 = entity.getEyePosition(0.0F);
			double d0 = vec3.y - vec31.y;
			if (d0 > (double)0.0F) {
				this.head.y = 0.0F;
			} else {
				this.head.y = 1.0F;
			}

			Vec3 vec32 = entity.getViewVector(0.0F);
			vec32 = new Vec3(vec32.x, (double)0.0F, vec32.z);
			Vec3 vec33 = (new Vec3(vec31.x - vec3.x, (double)0.0F, vec31.z - vec3.z)).normalize().yRot(((float)Math.PI / 2F));
			double d1 = vec32.dot(vec33);
			this.head.x = Mth.sqrt((float)Math.abs(d1)) * 2.0F * (float)Math.signum(d1);
		}
	}


	protected Iterable<ModelPart> getHeadParts() {
		return ImmutableList.of(this.head, this.frill3,this.frill2,  this.frill4);
	}

	protected Iterable<ModelPart> getBodyParts() {
		return ImmutableList.of(this.tail, this.leg1, this.leg2);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {

		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);

	}

}