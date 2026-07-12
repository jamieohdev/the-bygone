package com.jamiedev.bygone.client.models;

import com.jamiedev.bygone.client.models.animations.*;
import com.jamiedev.bygone.client.renderer.SpectralRenderUtil;
import com.jamiedev.bygone.common.entity.*;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class ReaverModel<T extends Entity> extends HierarchicalModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	private final ModelPart Body;
	private final ModelPart LowerJaw;
	private final ModelPart eye;
	private final ModelPart UpperJaw;
	private final ModelPart LeftArm;
	private final ModelPart LowerLeftArm;
	private final ModelPart RightArm;
	private final ModelPart LowerRightArm;

	public ReaverModel(ModelPart root) {
		this.Body = root.getChild("Body");
		this.LowerJaw = this.Body.getChild("LowerJaw");
		this.eye = this.Body.getChild("eye");
		this.UpperJaw = this.Body.getChild("UpperJaw");
		this.LeftArm = this.Body.getChild("LeftArm");
		this.LowerLeftArm = this.LeftArm.getChild("LowerLeftArm");
		this.RightArm = this.Body.getChild("RightArm");
		this.LowerRightArm = this.RightArm.getChild("LowerRightArm");
	}

	public static LayerDefinition getTexturedModelData() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Body = partdefinition.addOrReplaceChild("Body", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition LowerJaw = Body.addOrReplaceChild("LowerJaw", CubeListBuilder.create().texOffs(0, 0).addBox(-9.0F, 0.0F, -18.0F, 18.0F, 11.0F, 18.0F, new CubeDeformation(0.0F))
		.texOffs(0, 88).addBox(-8.5F, -0.5F, -17.5F, 17.0F, 11.0F, 17.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -11.0F, 9.0F));

		PartDefinition eye = Body.addOrReplaceChild("eye", CubeListBuilder.create().texOffs(80, 12).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -9.0F, 2.0F));

		PartDefinition UpperJaw = Body.addOrReplaceChild("UpperJaw", CubeListBuilder.create().texOffs(0, 29).addBox(-9.0F, -3.0F, -18.0F, 18.0F, 7.0F, 18.0F, new CubeDeformation(0.02F))
		.texOffs(56, 82).addBox(-8.5F, -2.5F, -17.5F, 17.0F, 7.0F, 17.0F, new CubeDeformation(0.02F)), PartPose.offsetAndRotation(0.0F, -11.0F, 9.0F, -0.6545F, 0.0F, 0.0F));

		PartDefinition LeftArm = Body.addOrReplaceChild("LeftArm", CubeListBuilder.create().texOffs(0, 54).addBox(-8.0F, -4.0F, -12.0F, 8.0F, 8.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.0F, -6.0F, 4.0F, 1.5708F, 0.0436F, 0.0F));

		PartDefinition LowerLeftArm = LeftArm.addOrReplaceChild("LowerLeftArm", CubeListBuilder.create().texOffs(0, 78).addBox(-2.0F, -2.0F, -6.0F, 4.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, 0.0F, -12.0F));

		PartDefinition Hand_r1 = LowerLeftArm.addOrReplaceChild("Hand_r1", CubeListBuilder.create().texOffs(72, 14).addBox(0.0F, -3.0F, -14.0F, 0.0F, 9.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -6.0F, 0.0F, 0.0F, -1.5708F));

		PartDefinition RightArm = Body.addOrReplaceChild("RightArm", CubeListBuilder.create().texOffs(48, 54).addBox(0.0F, -4.0F, -12.0F, 8.0F, 8.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.0F, -6.0F, 4.0F, 1.5708F, -0.0436F, 0.0F));

		PartDefinition LowerRightArm = RightArm.addOrReplaceChild("LowerRightArm", CubeListBuilder.create().texOffs(20, 78).addBox(-2.0F, -2.0F, -6.0F, 4.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, 0.0F, -12.0F));

		PartDefinition Hand_r2 = LowerRightArm.addOrReplaceChild("Hand_r2", CubeListBuilder.create().texOffs(72, 23).addBox(0.0F, -3.0F, -14.0F, 0.0F, 9.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -6.0F, 0.0F, 0.0F, 1.5708F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public @NotNull ModelPart root() {
		return Body;
	}

	private float spectralAlpha = 1.0F;

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.spectralAlpha = SpectralRenderUtil.healthAlpha(entity);

		if (entity instanceof ReaverEntity wraith) {
			this.animate(wraith.idleAnimationState, ReaverAnimations.IDLE, ageInTicks, 1.0f);
			this.animate(wraith.floatAnimationState, ReaverAnimations.FLOAT, ageInTicks, 1.0f);
			this.animate(wraith.meleeAnimationState, ReaverAnimations.ATTACK, ageInTicks, 1.0f);
		}
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int light, int overlay, int color)
	{
		Body.render(poseStack, vertexConsumer, light, overlay, SpectralRenderUtil.applyAlpha(color, this.spectralAlpha));
	}
}