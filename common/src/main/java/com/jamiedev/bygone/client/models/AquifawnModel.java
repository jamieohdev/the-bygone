package com.jamiedev.bygone.client.models;

import com.jamiedev.bygone.common.entity.AquifawnEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.GuardianModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class AquifawnModel<T extends AquifawnEntity> extends HierarchicalModel<T> {
	private final ModelPart root;

	private final ModelPart leg1;
	private final ModelPart leg0;
	private final ModelPart neck;
	private final ModelPart head;
	private final ModelPart noose;
	private final ModelPart ear1;
	private final ModelPart ear2;
	private final ModelPart horn;
	private final ModelPart horn2;
	private final ModelPart cape;
	private final ModelPart body;

	private final ModelPart tail0;
	private final ModelPart tail1;
	private final ModelPart tail2;

	public AquifawnModel(ModelPart root) {
		this.root  = root;
		this.tail0 = root.getChild("tail0");
		this.tail1 = root.getChild("tail1");
		this.tail2 = root.getChild("tail2");

		this.leg1 = root.getChild("leg1");
		this.leg0 = root.getChild("leg0");
		this.neck = root.getChild("neck");
		this.head = this.neck.getChild("head");
		this.noose = this.head.getChild("noose");
		this.ear1 = this.head.getChild("ear1");
		this.ear2 = this.head.getChild("ear2");
		this.horn = this.head.getChild("horn");
		this.horn2 = this.head.getChild("horn2");
		this.cape = this.neck.getChild("cape");
		this.body = root.getChild("body");
	}

	public static LayerDefinition getTexturedModelData() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition tail1 = partdefinition.addOrReplaceChild("tail0", CubeListBuilder.create().texOffs(0, 35).addBox(-2.0F, 14.0F, 7.0F, 4.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.0F, 0.0F));

		PartDefinition tail2 = partdefinition.addOrReplaceChild("tail1", CubeListBuilder.create().texOffs(25, 39).addBox(0.0F, 14.0F, 0.0F, 3.0F, 3.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.5F, -2.5F, 14.0F));

		PartDefinition tail3 = partdefinition.addOrReplaceChild("tail2", CubeListBuilder.create().texOffs(48, 20).addBox(0.0F, 14.0F, 0.0F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(27, 20).addBox(1.0F, 10.5F, 3.0F, 1.0F, 9.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, -2.0F, 20.0F));


		PartDefinition leg1 = partdefinition.addOrReplaceChild("leg1", CubeListBuilder.create(), PartPose.offset(-3.0F, 15.0F, -6.5F));

		PartDefinition leg1_r1 = leg1.addOrReplaceChild("leg1_r1", CubeListBuilder.create().texOffs(26, 50).addBox(-4.0F, -1.0F, -2.0F, 2.0F, 9.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 1.0F, 0.5F, 0.2618F, 0.0F, 0.0F));

		PartDefinition leg0 = partdefinition.addOrReplaceChild("leg0", CubeListBuilder.create(), PartPose.offset(2.0F, 15.0F, -6.5F));

		PartDefinition leg0_r1 = leg0.addOrReplaceChild("leg0_r1", CubeListBuilder.create().texOffs(15, 50).addBox(1.0F, -1.0F, -2.0F, 2.0F, 9.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 1.0F, 0.5F, 0.2618F, 0.0F, 0.0F));

		PartDefinition neck = partdefinition.addOrReplaceChild("neck", CubeListBuilder.create(), PartPose.offset(0.0F, 11.0F, -7.0F));

		PartDefinition neck_r1 = neck.addOrReplaceChild("neck_r1", CubeListBuilder.create().texOffs(37, 11).addBox(-3.0F, -14.0F, 3.0F, 5.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 11.0F, 5.0F, 0.9163F, 0.0F, 0.0F));

		PartDefinition neck_r2 = neck.addOrReplaceChild("neck_r2", CubeListBuilder.create().texOffs(0, 48).addBox(-2.0F, -20.0F, -3.0F, 3.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 10.0F, 5.0F, 0.48F, 0.0F, 0.0F));

		PartDefinition head = neck.addOrReplaceChild("head", CubeListBuilder.create().texOffs(37, 0).addBox(-3.5F, -4.0F, -4.0F, 4.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -6.0F, -5.0F, 0.1745F, 0.0F, 0.0F));

		PartDefinition noose = head.addOrReplaceChild("noose", CubeListBuilder.create().texOffs(48, 29).addBox(-3.0F, -1.0F, -3.0F, 3.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.0F, -4.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition ear1 = head.addOrReplaceChild("ear1", CubeListBuilder.create().texOffs(58, 0).addBox(0.0F, -1.0F, 0.0F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -3.0F, 0.0F, 0.0F, 0.5236F, -0.3927F));

		PartDefinition ear2 = head.addOrReplaceChild("ear2", CubeListBuilder.create().texOffs(58, 4).addBox(-3.0F, -1.0F, 0.0F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, -3.0F, 0.0F, 0.0F, -0.5236F, 0.3927F));

		PartDefinition horn = head.addOrReplaceChild("horn", CubeListBuilder.create().texOffs(37, 52).addBox(-2.0F, -8.0F, -0.5F, 5.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -4.0F, 0.0F, -0.7418F, -0.0436F, 0.5236F));

		PartDefinition horn2 = head.addOrReplaceChild("horn2", CubeListBuilder.create().texOffs(50, 52).addBox(-2.0F, -8.0F, 0.0F, 5.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, -4.0F, 0.0F, -0.6981F, 0.0F, -0.6545F));

		PartDefinition cape = neck.addOrReplaceChild("cape", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -6.0F, -6.0F, -0.2182F, 0.0F, 0.0F));

		PartDefinition cape_r1 = cape.addOrReplaceChild("cape_r1", CubeListBuilder.create().texOffs(46, 39).addBox(-2.5F, -20.0F, -3.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 17.0F, 11.0F, 0.48F, 0.0F, 0.0F));

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 20).addBox(-4.0F, -4.0F, -8.0F, 7.0F, 8.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 12.0F, -1.0F));

		PartDefinition body_r1 = body.addOrReplaceChild("body_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -16.8F, -2.5F, 7.0F, 8.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 13.0F, -1.0F, -0.0873F, 0.0F, 0.0F));



		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

		float f = ageInTicks - (float)entity.tickCount;
		float f2 = entity.getTailAnimation(f);

		this.tail0.yRot = Mth.sin(f2) * (float)Math.PI * 0.05F;
		this.tail1.yRot = Mth.sin(f2) * (float)Math.PI * 0.1F;
		this.tail2.yRot = Mth.sin(f2) * (float)Math.PI * 0.15F;
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
		root.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
		//Do not render children of "root", root.render() already does that.
	}

	@Override
	public ModelPart root() {
		return root;
	}
}