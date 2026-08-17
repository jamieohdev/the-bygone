package com.jamiedev.bygone.client.models;

import com.jamiedev.bygone.client.models.animations.*;
import com.jamiedev.bygone.common.entity.WallowEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;

public class WallowModel<T extends Entity> extends HauntingsMobModel<T> {

	private final ModelPart all;
	private final ModelPart body;
	private final ModelPart backFlap;
	private final ModelPart frontFlap;
	private final ModelPart leftFlap;
	private final ModelPart rightFlap;
	private final ModelPart orb;

	public WallowModel(ModelPart root) {
		this.all = root.getChild("all");
		this.body = this.all.getChild("body");
		this.backFlap = this.body.getChild("backFlap");
		this.frontFlap = this.body.getChild("frontFlap");
		this.leftFlap = this.body.getChild("leftFlap");
		this.rightFlap = this.body.getChild("rightFlap");
		this.orb = this.all.getChild("orb");
	}

	public static LayerDefinition getTexturedModelData() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition all = partdefinition.addOrReplaceChild("all", CubeListBuilder.create(), PartPose.offset(0.0F, 20.0F, 0.0F));

		PartDefinition body = all.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, 0.0F, -6.0F, 12.0F, 12.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(0, 24).addBox(-4.0F, -3.0F, -4.0F, 8.0F, 3.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -13.0F, 0.0F));

		PartDefinition backFlap = body.addOrReplaceChild("backFlap", CubeListBuilder.create(), PartPose.offset(0.0F, 12.0F, 6.0F));

		PartDefinition BottomFlap1_r1 = backFlap.addOrReplaceChild("BottomFlap1_r1", CubeListBuilder.create().texOffs(0, 35).addBox(-6.0F, 0.0F, 0.0F, 12.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.5236F, 0.0F, 0.0F));

		PartDefinition frontFlap = body.addOrReplaceChild("frontFlap", CubeListBuilder.create(), PartPose.offset(0.0F, 12.0F, -6.0F));

		PartDefinition BottomFlap2_r1 = frontFlap.addOrReplaceChild("BottomFlap2_r1", CubeListBuilder.create().texOffs(24, 36).addBox(-6.0F, 0.0F, 0.0F, 12.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.5236F, 0.0F, 0.0F));

		PartDefinition leftFlap = body.addOrReplaceChild("leftFlap", CubeListBuilder.create(), PartPose.offset(-6.0F, 12.0F, 0.0F));

		PartDefinition BottomFlap2_r2 = leftFlap.addOrReplaceChild("BottomFlap2_r2", CubeListBuilder.create().texOffs(24, 40).addBox(-6.0F, 0.0F, 0.0F, 12.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.5236F));

		PartDefinition rightFlap = body.addOrReplaceChild("rightFlap", CubeListBuilder.create(), PartPose.offset(6.0F, 12.0F, 0.0F));

		PartDefinition BottomFlap3_r1 = rightFlap.addOrReplaceChild("BottomFlap3_r1", CubeListBuilder.create().texOffs(0, 39).addBox(-6.0F, 0.0F, 0.0F, 12.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, -0.5236F));

		PartDefinition orb = all.addOrReplaceChild("orb", CubeListBuilder.create().texOffs(32, 24).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -17.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
		all.render(poseStack, vertexConsumer, packedLight, packedOverlay, this.modifyColor(color));
	}

	@Override
	public ModelPart root() {
		return all;
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
	{
		this.root().getAllParts().forEach(ModelPart::resetPose);

		if (entity instanceof WallowEntity wraith) {
			this.animate(wraith.idleAnimationState, WallowAnimations.IDLE, ageInTicks, 1.0f);
			this.animate(wraith.floatAnimationState, WallowAnimations.FLOAT, ageInTicks, 1.0f);
		}
	}
}