package com.jamiedev.bygone.client.models;

import com.jamiedev.bygone.client.renderer.SpectralRenderUtil;
import com.jamiedev.bygone.common.entity.ScareEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class ScareModel2<T extends Entity> extends HierarchicalModel<T> {

	private final ModelPart all;
	private final ModelPart head;
	private final ModelPart body;
	private final ModelPart armLeft;
	private final ModelPart armRight;
	private float spectralAlpha = 1.0F;

	public ScareModel2(ModelPart root) {
		this.all = root.getChild("all");
		this.body = this.all.getChild("body");
		this.head = this.body.getChild("head");
		this.armLeft = this.body.getChild("armLeft");
		this.armRight = this.body.getChild("armRight");
	}

	public static LayerDefinition getTexturedModelData() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition all = partdefinition.addOrReplaceChild("all", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition body = all.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0)
				.addBox(-5.0F, -9.0F, -1.0F, 7.0F, 9.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, 24.0F, -2.0F));

		PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 16).
				addBox(-3.0F, -4.0F, -1.0F, 5.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 16.0F, -1.0F));

		PartDefinition armLeft = body.addOrReplaceChild("armLeft", CubeListBuilder.create().texOffs(20, 16)
				.addBox(-8.0F, -2.0F, -1.0F, 8.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offset(-4.0F, 18.0F, 0.0F));

		PartDefinition armRight = body.addOrReplaceChild("armRight", CubeListBuilder.create().texOffs(20, 20)
				.addBox(3.0F, -2.0F, -1.0F, 8.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 18.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public @NotNull ModelPart root() {
		return all;
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.spectralAlpha = SpectralRenderUtil.healthAlpha(entity);

		this.all.y += Mth.sin(ageInTicks * 0.1F) * 0.8F;
		this.head.yRot = netHeadYaw * ((float) Math.PI / 180F);
		this.head.xRot = headPitch * ((float) Math.PI / 180F);

		float flap = Mth.sin(ageInTicks * 0.2F) * 0.25F;
		this.armLeft.zRot = -0.3F + flap;
		this.armRight.zRot = 0.3F - flap;

		if (entity instanceof ScareEntity) {
			this.body.zRot = Mth.sin(ageInTicks * 0.07F) * 0.05F;
		}
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
		all.render(poseStack, vertexConsumer, packedLight, packedOverlay, SpectralRenderUtil.applyAlpha(color, this.spectralAlpha));
	}
}