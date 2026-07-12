package com.jamiedev.bygone.client.models;

import com.jamiedev.bygone.client.renderer.SpectralRenderUtil;
import com.jamiedev.bygone.common.entity.MournEntity;
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

public class MournModel<T extends Entity> extends HierarchicalModel<T> {

	private final ModelPart all;
	private final ModelPart body;
	private final ModelPart armLeft;
	private final ModelPart armRight;
	private float spectralAlpha = 1.0F;

	public MournModel(ModelPart root) {
		this.all = root.getChild("all");
		this.body = this.all.getChild("body");
		this.armLeft = this.all.getChild("armLeft");
		this.armRight = this.all.getChild("armRight");
	}

	public static LayerDefinition getTexturedModelData() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition all = partdefinition.addOrReplaceChild("all", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition body = all.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -24.0F, -3.0F, 8.0F, 24.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition armLeft = all.addOrReplaceChild("armLeft", CubeListBuilder.create().texOffs(30, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 16.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, -22.0F, 0.0F));

		PartDefinition armRight = all.addOrReplaceChild("armRight", CubeListBuilder.create().texOffs(40, 0).mirror().addBox(-1.0F, 0.0F, -1.0F, 2.0F, 16.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-5.0F, -22.0F, 0.0F));

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

		float stretch = 1.0F;
		if (entity instanceof MournEntity mourn) {
			stretch = mourn.getStretchProgress(Mth.frac(ageInTicks));
		}

		this.all.yScale = 0.12F + 0.88F * stretch;
		float spread = 1.0F + (1.0F - stretch) * 0.9F;
		this.all.xScale = spread;
		this.all.zScale = spread;

		this.body.zRot = Mth.sin(ageInTicks * 0.06F) * 0.04F * stretch;
		float swing = Mth.cos(limbSwing * 0.6662F) * 0.6F * limbSwingAmount * stretch;
		this.armLeft.xRot = swing;
		this.armRight.xRot = -swing;
		this.armLeft.zRot = -0.1F * stretch + Mth.sin(ageInTicks * 0.08F) * 0.05F;
		this.armRight.zRot = 0.1F * stretch - Mth.sin(ageInTicks * 0.08F) * 0.05F;
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
		all.render(poseStack, vertexConsumer, packedLight, packedOverlay, SpectralRenderUtil.applyAlpha(color, this.spectralAlpha));
	}
}
