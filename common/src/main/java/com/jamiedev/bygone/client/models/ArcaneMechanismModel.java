package com.jamiedev.bygone.client.models;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;

public class ArcaneMechanismModel<T extends Entity> extends EntityModel<T> {

    private final ModelPart arcane_mechanism;
    private final ModelPart arcane_core;
    private final ModelPart corepiece1;
    private final ModelPart corepiece2;
    private final ModelPart corepiece3;
    private final ModelPart corepiece4;
    private final ModelPart corepiece5;
    private final ModelPart corepiece6;
    private final ModelPart corepiece7;
    private final ModelPart corepiece8;
    private final ModelPart cogs;
    private final ModelPart verdigris_cog;
    private final ModelPart cogpiece1;
    private final ModelPart cogpiece2;
    private final ModelPart cogpiece3;
    private final ModelPart cogpiece4;
    private final ModelPart verdigris_cog2;
    private final ModelPart cogpiece5;
    private final ModelPart cogpiece6;
    private final ModelPart cogpiece7;
    private final ModelPart cogpiece8;
    private final ModelPart portal;
    private final ModelPart outer_ring;
    private final ModelPart inner_ring;
    private final ModelPart inner_ring2;
    private final ModelPart middle_ring;
    private final ModelPart middle_ring2;

    public ArcaneMechanismModel(ModelPart root) {
        this.arcane_mechanism = root.getChild("arcane_mechanism");
        this.arcane_core = this.arcane_mechanism.getChild("arcane_core");
        this.corepiece1 = this.arcane_core.getChild("corepiece1");
        this.corepiece2 = this.arcane_core.getChild("corepiece2");
        this.corepiece3 = this.arcane_core.getChild("corepiece3");
        this.corepiece4 = this.arcane_core.getChild("corepiece4");
        this.corepiece5 = this.arcane_core.getChild("corepiece5");
        this.corepiece6 = this.arcane_core.getChild("corepiece6");
        this.corepiece7 = this.arcane_core.getChild("corepiece7");
        this.corepiece8 = this.arcane_core.getChild("corepiece8");
        this.cogs = this.arcane_mechanism.getChild("cogs");
        this.verdigris_cog = this.cogs.getChild("verdigris_cog");
        this.cogpiece1 = this.verdigris_cog.getChild("cogpiece1");
        this.cogpiece2 = this.verdigris_cog.getChild("cogpiece2");
        this.cogpiece3 = this.verdigris_cog.getChild("cogpiece3");
        this.cogpiece4 = this.verdigris_cog.getChild("cogpiece4");
        this.verdigris_cog2 = this.cogs.getChild("verdigris_cog2");
        this.cogpiece5 = this.verdigris_cog2.getChild("cogpiece5");
        this.cogpiece6 = this.verdigris_cog2.getChild("cogpiece6");
        this.cogpiece7 = this.verdigris_cog2.getChild("cogpiece7");
        this.cogpiece8 = this.verdigris_cog2.getChild("cogpiece8");
        this.portal = this.arcane_mechanism.getChild("portal");
        this.outer_ring = this.portal.getChild("outer_ring");
        this.inner_ring = this.portal.getChild("inner_ring");
        this.inner_ring2 = this.portal.getChild("inner_ring2");
        this.middle_ring = this.portal.getChild("middle_ring");
        this.middle_ring2 = this.portal.getChild("middle_ring2");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition arcane_mechanism = partdefinition.addOrReplaceChild("arcane_mechanism", CubeListBuilder.create(), PartPose.offset(0.0F, 16.0F, 0.3333F));

        PartDefinition arcane_core = arcane_mechanism.addOrReplaceChild("arcane_core", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, -0.3333F));

        PartDefinition corepiece1 = arcane_core.addOrReplaceChild("corepiece1", CubeListBuilder.create().texOffs(64, 24).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, -2.0F, 2.0F));

        PartDefinition corepiece2 = arcane_core.addOrReplaceChild("corepiece2", CubeListBuilder.create().texOffs(64, 0).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, -2.0F, -2.0F));

        PartDefinition corepiece3 = arcane_core.addOrReplaceChild("corepiece3", CubeListBuilder.create().texOffs(64, 8).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, -2.0F, -2.0F));

        PartDefinition corepiece4 = arcane_core.addOrReplaceChild("corepiece4", CubeListBuilder.create().texOffs(64, 16).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, -2.0F, 2.0F));

        PartDefinition corepiece5 = arcane_core.addOrReplaceChild("corepiece5", CubeListBuilder.create().texOffs(48, 64).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 2.0F, 2.0F));

        PartDefinition corepiece6 = arcane_core.addOrReplaceChild("corepiece6", CubeListBuilder.create().texOffs(32, 64).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 2.0F, 2.0F));

        PartDefinition corepiece7 = arcane_core.addOrReplaceChild("corepiece7", CubeListBuilder.create().texOffs(64, 56).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 2.0F, -2.0F));

        PartDefinition corepiece8 = arcane_core.addOrReplaceChild("corepiece8", CubeListBuilder.create().texOffs(64, 64).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 2.0F, -2.0F));

        PartDefinition cogs = arcane_mechanism.addOrReplaceChild("cogs", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, -0.3333F));

        PartDefinition verdigris_cog = cogs.addOrReplaceChild("verdigris_cog", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, -1.5708F));

        PartDefinition cogpiece1 = verdigris_cog.addOrReplaceChild("cogpiece1", CubeListBuilder.create().texOffs(48, 48).addBox(-4.0F, 0.0F, -4.0F, 8.0F, 0.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, 0.0F, -4.0F));

        PartDefinition cogpiece2 = verdigris_cog.addOrReplaceChild("cogpiece2", CubeListBuilder.create().texOffs(0, 56).addBox(-4.0F, 0.0F, -4.0F, 8.0F, 0.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, 0.0F, 4.0F));

        PartDefinition cogpiece3 = verdigris_cog.addOrReplaceChild("cogpiece3", CubeListBuilder.create().texOffs(0, 64).addBox(-4.0F, 0.0F, -4.0F, 8.0F, 0.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, 0.0F, 4.0F));

        PartDefinition cogpiece4 = verdigris_cog.addOrReplaceChild("cogpiece4", CubeListBuilder.create().texOffs(32, 56).addBox(-4.0F, 0.0F, -4.0F, 8.0F, 0.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, 0.0F, -4.0F));

        PartDefinition verdigris_cog2 = cogs.addOrReplaceChild("verdigris_cog2", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.5708F, 0.0F, 0.0F));

        PartDefinition cogpiece5 = verdigris_cog2.addOrReplaceChild("cogpiece5", CubeListBuilder.create().texOffs(48, 48).addBox(-4.0F, 0.0F, -4.0F, 8.0F, 0.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, 0.0F, -4.0F));

        PartDefinition cogpiece6 = verdigris_cog2.addOrReplaceChild("cogpiece6", CubeListBuilder.create().texOffs(0, 56).addBox(-4.0F, 0.0F, -4.0F, 8.0F, 0.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, 0.0F, 4.0F));

        PartDefinition cogpiece7 = verdigris_cog2.addOrReplaceChild("cogpiece7", CubeListBuilder.create().texOffs(0, 64).addBox(-4.0F, 0.0F, -4.0F, 8.0F, 0.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, 0.0F, 4.0F));

        PartDefinition cogpiece8 = verdigris_cog2.addOrReplaceChild("cogpiece8", CubeListBuilder.create().texOffs(32, 56).addBox(-4.0F, 0.0F, -4.0F, 8.0F, 0.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, 0.0F, -4.0F));

        PartDefinition portal = arcane_mechanism.addOrReplaceChild("portal", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, -1.3333F, -3.1416F, 0.0F, 3.1416F));

        PartDefinition outer_ring = portal.addOrReplaceChild("outer_ring", CubeListBuilder.create().texOffs(0, 0).addBox(-16.0F, -16.0F, 0.0F, 32.0F, 32.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -1.0F));

        PartDefinition inner_ring = portal.addOrReplaceChild("inner_ring", CubeListBuilder.create().texOffs(48, 32).addBox(-8.0F, -8.0F, 0.0F, 16.0F, 16.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 1.0F));

        PartDefinition inner_ring2 = portal.addOrReplaceChild("inner_ring2", CubeListBuilder.create().texOffs(48, 32).addBox(-8.0F, -8.0F, 0.0F, 16.0F, 16.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -3.0F));

        PartDefinition middle_ring = portal.addOrReplaceChild("middle_ring", CubeListBuilder.create().texOffs(0, 32).addBox(-12.0F, -12.0F, 0.0F, 24.0F, 24.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition middle_ring2 = portal.addOrReplaceChild("middle_ring2", CubeListBuilder.create().texOffs(0, 32).addBox(-12.0F, -12.0F, 0.0F, 24.0F, 24.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -2.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        arcane_mechanism.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }
}
