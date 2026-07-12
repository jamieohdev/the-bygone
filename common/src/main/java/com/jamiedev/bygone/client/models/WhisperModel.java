package com.jamiedev.bygone.client.models;

import com.jamiedev.bygone.client.renderer.SpectralRenderUtil;
import com.jamiedev.bygone.common.entity.WhisperEntity;
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

public class WhisperModel<T extends Entity> extends HierarchicalModel<T> {

	private final ModelPart all;
	private final ModelPart body;
	private final ModelPart tail;
	private float spectralAlpha = 1.0F;

	public WhisperModel(ModelPart root) {
		this.all = root.getChild("all");
		this.body = this.all.getChild("body");
		this.tail = this.all.getChild("tail");
	}

	public static LayerDefinition getTexturedModelData() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition all = partdefinition.addOrReplaceChild("all", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition body = all.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -11.0F, -3.0F, 6.0F, 8.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition tail = all.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(0, 14).addBox(-2.0F, -3.0F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public @NotNull ModelPart root() {
		return all;
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.spectralAlpha = SpectralRenderUtil.healthAlpha(entity);

		this.all.y += Mth.sin(ageInTicks * 0.12F) * 0.6F;
		this.body.zRot = Mth.sin(ageInTicks * 0.08F) * 0.06F;
		this.tail.zRot = Mth.sin(ageInTicks * 0.16F) * 0.25F;
		this.tail.xRot = Mth.cos(ageInTicks * 0.16F) * 0.15F;

		if (entity instanceof WhisperEntity) {
			this.body.yRot = Mth.sin(ageInTicks * 0.05F) * 0.1F;
		}
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
		all.render(poseStack, vertexConsumer, packedLight, packedOverlay, SpectralRenderUtil.applyAlpha(color, this.spectralAlpha));
	}
}
