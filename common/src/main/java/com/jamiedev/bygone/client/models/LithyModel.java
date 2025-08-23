package com.jamiedev.bygone.client.models;

import com.jamiedev.bygone.client.models.animations.LithyAnimations;
import com.jamiedev.bygone.common.entity.LithyEntity;
import com.jamiedev.bygone.core.mixin.client.BipedEntityModelMixin;
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
import net.minecraft.world.entity.monster.Zombie;

public class LithyModel<T extends Entity> extends HierarchicalModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor

	ZombieModel ref;

	private final ModelPart root;
	private final ModelPart right_leg;
	private final ModelPart left_leg;
	private final ModelPart body;
	private final ModelPart neck;

	public LithyModel(ModelPart root) {
		this.root = root.getChild("root");
		this.right_leg = this.root.getChild("right_leg");
		this.left_leg = this.root.getChild("left_leg");
		this.body = this.root.getChild("body");
		this.neck = this.body.getChild("neck");
	}

	public static LayerDefinition getTexturedModelData() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition right_leg = root.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(30, 0).mirror().addBox(-2.0F, -1.0F, -2.0F, 4.0F, 11.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-3.0F, -10.0F, 0.0F));

		PartDefinition left_leg = root.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(30, 0).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 11.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, -10.0F, 0.0F));

		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 23).addBox(-4.0F, -7.0F, -3.0F, 8.0F, 9.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -7.0F, 0.0F));

		PartDefinition neck = body.addOrReplaceChild("neck", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -16.0F, -4.0F, 8.0F, 16.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -7.0F, 1.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);

		if (entity instanceof LithyEntity lithy) {
			this.animate(lithy.walkAnimationState, LithyAnimations.walk, ageInTicks, 1.0f);
			this.animate(lithy.tripBeginAnimationState, LithyAnimations.trip_begin, ageInTicks, 1.0f);
			this.animate(lithy.tripAnimationState, LithyAnimations.trip, ageInTicks, 1.0f);
			this.animate(lithy.tripEndAnimationState, LithyAnimations.trip_end, ageInTicks, 1.0f);
		}
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
		root.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
	}

	@Override
	public ModelPart root() {
		return root;
	}
}