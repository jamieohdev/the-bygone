package com.jamiedev.bygone.client.models;

import com.jamiedev.bygone.client.models.animations.WhiskbillAnimations;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.KeyframeAnimations;
import net.minecraft.client.animation.definitions.SnifferAnimation;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import org.joml.Vector3f;

import java.util.Random;

public class WhiskbillModel<T extends Entity> extends AgeableHierarchicalModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	//public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "whiskbill"), "main");
	private final ModelPart tail;
	private final ModelPart head;
	private final ModelPart whisker2;
	private final ModelPart whisker3;
	private final ModelPart whisker1;
	private final ModelPart whisker4;
	private final ModelPart leg2;
	private final ModelPart leg4;
	private final ModelPart leg1;
	private final ModelPart leg3;
	private final ModelPart bb_main;


	SnifferModel ref;

	public WhiskbillModel(ModelPart root) {
        super(0.5F, 14.0F);
        this.tail = root.getChild("tail");
		this.head = root.getChild("head");
		this.whisker2 = this.head.getChild("whisker2");
		this.whisker3 = this.head.getChild("whisker3");
		this.whisker1 = this.head.getChild("whisker1");
		this.whisker4 = this.head.getChild("whisker4");
		this.leg2 = root.getChild("leg2");
		this.leg4 = root.getChild("leg4");
		this.leg1 = root.getChild("leg1");
		this.leg3 = root.getChild("leg3");
		this.bb_main = root.getChild("bb_main");
	}

	public static LayerDefinition getTexturedModelData() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition tail = partdefinition.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(0, 0).addBox(-10.5F, -3.0F, -5.0F, 21.0F, 13.0F, 41.0F, new CubeDeformation(0.0F))
		.texOffs(131, 7).addBox(-3.5F, -3.0F, -6.0F, 7.0F, 6.0F, 35.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 8.0F, 20.0F));

		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(126, 48).addBox(-9.5F, -4.0F, -19.0F, 19.0F, 8.0F, 22.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 9.0F, -19.0F));

		PartDefinition whisker2 = head.addOrReplaceChild("whisker2", CubeListBuilder.create().texOffs(126, 78).addBox(-2.0F, 0.0F, -10.0F, 17.0F, 0.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.5F, 4.0F, -6.0F, 0.0F, 0.0F, 0.4363F));

		PartDefinition whisker3 = head.addOrReplaceChild("whisker3", CubeListBuilder.create().texOffs(126, 78).mirror().addBox(-15.0F, 0.0F, -10.0F, 17.0F, 0.0F, 14.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-9.5F, 4.0F, -6.0F, 0.0F, 0.0F, -0.4363F));

		PartDefinition whisker1 = head.addOrReplaceChild("whisker1", CubeListBuilder.create().texOffs(28, 141).addBox(-3.0F, -11.0F, 3.0F, 7.0F, 15.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(9.5F, -4.0F, -8.0F));

		PartDefinition whisker4 = head.addOrReplaceChild("whisker4", CubeListBuilder.create().texOffs(28, 141).mirror().addBox(-4.0F, -11.0F, 3.0F, 7.0F, 15.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-9.5F, -4.0F, -8.0F));

		PartDefinition leg2 = partdefinition.addOrReplaceChild("leg2", CubeListBuilder.create().texOffs(42, 141).addBox(-4.5F, 11.0F, 6.0F, 8.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(126, 121).addBox(-4.5F, -3.0F, -4.0F, 8.0F, 16.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(13.0F, 11.0F, 12.0F));

		PartDefinition leg4 = partdefinition.addOrReplaceChild("leg4", CubeListBuilder.create().texOffs(42, 141).mirror().addBox(-3.5F, 11.0F, 6.0F, 8.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(126, 121).mirror().addBox(-3.5F, -3.0F, -4.0F, 8.0F, 16.0F, 10.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-13.0F, 11.0F, 12.0F));

		PartDefinition leg1 = partdefinition.addOrReplaceChild("leg1", CubeListBuilder.create().texOffs(126, 92).addBox(-3.0F, -4.0F, -4.0F, 8.0F, 19.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(0, 141).addBox(-7.0F, 13.0F, -4.0F, 4.0F, 2.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(13.0F, 9.0F, -8.0F));

		PartDefinition leg3 = partdefinition.addOrReplaceChild("leg3", CubeListBuilder.create().texOffs(126, 92).mirror().addBox(-5.0F, -4.0F, -4.0F, 8.0F, 19.0F, 10.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 141).mirror().addBox(3.0F, 13.0F, -4.0F, 4.0F, 2.0F, 10.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-13.0F, 9.0F, -8.0F));

		PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(0, 54).addBox(-13.5F, -20.0F, -16.0F, 27.0F, 15.0F, 36.0F, new CubeDeformation(0.0F))
		.texOffs(0, 105).addBox(-13.5F, -8.0F, -16.0F, 27.0F, 0.0F, 36.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 256, 256);
	}



	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.head.xRot = headPitch * ((float) Math.PI / 180F);
		this.head.yRot = netHeadYaw * ((float) Math.PI / 180F);
		//this.animateWalk(WhiskbillAnimations.WHISKBILL_WALK, limbSwing, limbSwingAmount, 9.0F, 100.0F);

		Random rand = new Random();
		boolean flag = rand.nextInt(1000) > 1;

		this.leg4.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
		this.leg2.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
		this.leg3.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
		this.leg1.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;

		this.tail.yRot = Mth.cos(limbSwing * 0.3331F) * 1.4F * limbSwingAmount;

	}

	public ModelPart root() {
		return this.bb_main;
	}

	@Override
    public void renderToBuffer(PoseStack matrices, VertexConsumer vertexConsumer, int light, int overlay, int color) {
		tail.render(matrices, vertexConsumer, light, overlay, color);
		head.render(matrices, vertexConsumer, light, overlay, color);
		leg2.render(matrices, vertexConsumer, light, overlay, color);
		leg4.render(matrices, vertexConsumer, light, overlay, color);
		leg1.render(matrices, vertexConsumer, light, overlay, color);
		leg3.render(matrices, vertexConsumer, light, overlay, color);
		bb_main.render(matrices, vertexConsumer, light, overlay, color);
	}
}