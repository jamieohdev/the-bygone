package com.jamiedev.bygone.client.models;

import com.jamiedev.bygone.client.models.animations.*;
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

public class GeistModel<T extends Entity> extends HauntingsMobModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	private final ModelPart all;
	private final ModelPart body;
	private final ModelPart eye;
	private final ModelPart Mouth;
	private final ModelPart ArmRight;
	private final ModelPart ArmLeft;

	public GeistModel(ModelPart root) {
		this.all = root.getChild("all");
		this.body = this.all.getChild("body");
		this.eye = this.body.getChild("eye");
		this.Mouth = this.body.getChild("Mouth");
		this.ArmRight = this.all.getChild("ArmRight");
		this.ArmLeft = this.all.getChild("ArmLeft");
	}

	public static LayerDefinition getTexturedModelData() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition all = partdefinition.addOrReplaceChild("all", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 12.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

		PartDefinition body = all.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 22).addBox(-2.0F, -18.0F, -4.0F, 8.0F, 18.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, -2.0F, 0.0F));

		PartDefinition LowerBody_r1 = body.addOrReplaceChild("LowerBody_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-15.04F, -6.95F, -4.0F, 18.0F, 14.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.6F, 6.1F, 0.0F, 0.0F, 0.0F, 0.2182F));

		PartDefinition Back_r1 = body.addOrReplaceChild("Back_r1", CubeListBuilder.create().texOffs(32, 22).addBox(-4.0F, -14.0F, -4.0F, 7.0F, 13.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.0F, 3.0F, 0.0F, 0.0F, 0.0F, 0.5672F));

		PartDefinition eye = body.addOrReplaceChild("eye", CubeListBuilder.create().texOffs(0, 48).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, -13.0F, 0.0F));

		PartDefinition Mouth = body.addOrReplaceChild("Mouth", CubeListBuilder.create().texOffs(16, 44).addBox(0.0F, -5.0F, -2.0F, 0.0F, 10.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, -5.0F, 0.0F));

		PartDefinition ArmRight = all.addOrReplaceChild("ArmRight", CubeListBuilder.create(), PartPose.offset(0.0F, -1.0F, -4.0F));

		PartDefinition cube_r1 = ArmRight.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(28, 44).addBox(-2.0F, -2.0F, -3.0F, 13.0F, 13.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -2.0F, -1.1F, -0.0094F, 0.0426F, 0.3489F));

		PartDefinition ArmLeft = all.addOrReplaceChild("ArmLeft", CubeListBuilder.create(), PartPose.offset(0.0F, -1.0F, 4.0F));

		PartDefinition cube_r2 = ArmLeft.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(28, 44).addBox(-2.0F, -2.0F, -3.0F, 13.0F, 13.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -2.0F, 3.1F, 0.0094F, -0.0426F, 0.3489F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public @NotNull ModelPart root() {
		return all;
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);

		if (entity instanceof GeistEntity wraith) {
			this.animate(wraith.idleAnimationState, GeistAnimations.IDLE, ageInTicks, 1.0f);
			this.animate(wraith.floatAnimationState, GeistAnimations.FLOAT, ageInTicks, 1.0f);
			this.animate(wraith.meleeAnimationState, GeistAnimations.ATTACK, ageInTicks, 1.0f);
		}
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int light, int overlay, int color)
	{
		all.render(poseStack, vertexConsumer, light, overlay, this.modifyColor(color));
	}
}