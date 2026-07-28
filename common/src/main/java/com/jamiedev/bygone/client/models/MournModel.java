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

	public static final float SUBMERGE_DEPTH = 54.0F;

	private final ModelPart root;
	private final ModelPart blur;
	private final ModelPart armRoot;
	private final ModelPart bottomArm;
	private final ModelPart topArm;
	private final ModelPart hand;
	private final ModelPart thumbTop;
	private final ModelPart finger1Top;
	private final ModelPart finger1Bottom;
	private final ModelPart finger2Top;
	private final ModelPart finger2Bottom;
	private final ModelPart finger3Top;
	private final ModelPart finger3Bottom;

	private float spectralAlpha = 1.0F;

	public MournModel(ModelPart root) {
		this.root = root.getChild("root");
		this.blur = this.root.getChild("blur");
		this.armRoot = this.root.getChild("armRoot");
		this.bottomArm = this.armRoot.getChild("bottomArm");
		this.topArm = this.bottomArm.getChild("topArm");
		this.hand = this.topArm.getChild("hand");
		this.thumbTop = this.hand.getChild("thumbTop");
		this.finger1Top = this.hand.getChild("finger1Top");
		this.finger1Bottom = this.finger1Top.getChild("finger1Bottom");
		this.finger2Top = this.hand.getChild("finger2Top");
		this.finger2Bottom = this.finger2Top.getChild("finger2Bottom");
		this.finger3Top = this.hand.getChild("finger3Top");
		this.finger3Bottom = this.finger3Top.getChild("finger3Bottom");
	}

	public static LayerDefinition getTexturedModelData() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition blur = root.addOrReplaceChild("blur", CubeListBuilder.create(), PartPose.ZERO);
		blur.addOrReplaceChild("blurA", CubeListBuilder.create().texOffs(32, 10).addBox(-10.0F, -10.0F, 0.0F, 20.0F, 10.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));
		blur.addOrReplaceChild("blurB", CubeListBuilder.create().texOffs(32, 0).addBox(-10.0F, -10.0F, 0.0F, 20.0F, 10.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 2.3562F, 0.0F));

		PartDefinition armRoot = root.addOrReplaceChild("armRoot", CubeListBuilder.create(), PartPose.ZERO);

		PartDefinition bottomArm = armRoot.addOrReplaceChild("bottomArm", CubeListBuilder.create().texOffs(20, 27).addBox(-2.0F, -16.0F, -2.0F, 4.0F, 16.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7418F, 0.0F, 0.0F));

		PartDefinition topArm = bottomArm.addOrReplaceChild("topArm", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -15.0F, 0.0F, 1.1345F, 0.0F, 0.0F));
		topArm.addOrReplaceChild("topArm_r1", CubeListBuilder.create().texOffs(0, 27).addBox(-2.0F, -15.0F, -3.0F, 4.0F, 16.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition hand = topArm.addOrReplaceChild("hand", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -15.0F, 0.0F, 0.4363F, 0.0F, 0.0F));
		hand.addOrReplaceChild("hand_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -13.0F, -6.0F, 4.0F, 15.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition thumbTop = hand.addOrReplaceChild("thumbTop", CubeListBuilder.create().texOffs(48, 49).mirror().addBox(-2.0F, -5.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(5.0F, -1.0F, 0.0F, 0.7854F, 0.0F, 1.5272F));
		thumbTop.addOrReplaceChild("thumbBottom", CubeListBuilder.create()
				.texOffs(48, 60).mirror().addBox(-2.0F, -6.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 75).mirror().addBox(-2.0F, -11.0F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -4.96447F, -0.04289F, 0.7854F, 0.0F, 0.0F));

		PartDefinition finger1Top = hand.addOrReplaceChild("finger1Top", CubeListBuilder.create().texOffs(0, 49).addBox(-2.0F, -8.0F, -2.0F, 4.0F, 9.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, -13.0F, 0.0F, 0.0F, 0.0F, 0.2182F));
		finger1Top.addOrReplaceChild("finger1Bottom", CubeListBuilder.create()
				.texOffs(0, 62).addBox(-2.0F, -8.0F, -2.0F, 4.0F, 9.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(0, 75).addBox(-2.0F, -13.0F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition finger2Top = hand.addOrReplaceChild("finger2Top", CubeListBuilder.create().texOffs(16, 49).addBox(-2.0F, -8.0F, -2.0F, 4.0F, 9.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -13.0F, 0.0F, 0.3491F, 0.0F, 0.0F));
		finger2Top.addOrReplaceChild("finger2Bottom", CubeListBuilder.create()
				.texOffs(16, 62).addBox(-2.0F, -8.0F, -2.0F, 4.0F, 9.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(16, 75).addBox(-2.0F, -13.0F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition finger3Top = hand.addOrReplaceChild("finger3Top", CubeListBuilder.create().texOffs(32, 49).addBox(-2.0F, -8.0F, -2.0F, 4.0F, 9.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, -13.0F, 0.0F, 0.7854F, 0.0F, -0.2182F));
		finger3Top.addOrReplaceChild("finger3Bottom", CubeListBuilder.create()
				.texOffs(32, 62).addBox(-2.0F, -8.0F, -2.0F, 4.0F, 9.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(32, 75).addBox(-2.0F, -13.0F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public @NotNull ModelPart root() {
		return this.root;
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.spectralAlpha = SpectralRenderUtil.healthAlpha(entity);

		float emerge = 1.0F;
		float strike = 0.0F;
		if (entity instanceof MournEntity mourn) {
			float partialTick = Mth.frac(ageInTicks);
			emerge = mourn.getEmergeProgress(partialTick);
			strike = mourn.getStrikeProgress(partialTick);
		}

		this.armRoot.y += (1.0F - emerge) * SUBMERGE_DEPTH;
		this.armRoot.visible = emerge > 0.0F;
		this.blur.xScale = 1.0F - 0.3F * emerge;
		this.blur.zScale = this.blur.xScale;

		float sway = Mth.sin(ageInTicks * 0.05F) * 0.05F * emerge;
		this.bottomArm.zRot += sway;
		this.topArm.xRot -= sway * 0.5F;

		float clench = Mth.sin(strike * (float) Math.PI) * 0.7F;
		this.finger1Top.xRot += clench;
		this.finger2Top.xRot += clench;
		this.finger3Top.xRot += clench;
		this.finger1Bottom.xRot += clench * 0.8F;
		this.finger2Bottom.xRot += clench * 0.8F;
		this.finger3Bottom.xRot += clench * 0.8F;
		this.thumbTop.xRot += clench * 0.5F;
		this.hand.xRot += clench * 0.25F;

		float twitch = Mth.cos(ageInTicks * 0.18F) * 0.04F * emerge * (1.0F - strike);
		this.finger1Top.xRot += twitch;
		this.finger3Top.xRot -= twitch;
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
		this.root.render(poseStack, vertexConsumer, packedLight, packedOverlay, SpectralRenderUtil.applyAlpha(color, this.spectralAlpha));
	}
}
