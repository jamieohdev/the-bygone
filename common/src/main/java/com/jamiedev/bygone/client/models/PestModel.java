package com.jamiedev.bygone.client.models;

import com.jamiedev.bygone.entities.PestEntity;
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
import net.minecraft.world.entity.animal.Animal;

public class PestModel<T extends Animal> extends EntityModel<PestEntity> {

    private final ModelPart jaw;
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart left_arm;
    private final ModelPart right_arm;
    private final ModelPart left_thigh;
    private final ModelPart right_thigh;
    private final ModelPart left_foot;
    private final ModelPart right_foot;

    public PestModel(ModelPart root) {
        this.head = root.getChild("head");
        this.jaw = root.getChild("jaw");
        this.body = root.getChild("body");
        this.left_arm = root.getChild("left_arm");
        this.right_arm = root.getChild("right_arm");
        this.left_thigh = root.getChild("left_thigh");
        this.right_thigh = root.getChild("right_thigh");
        this.left_foot = root.getChild("left_foot");
        this.right_foot = root.getChild("right_foot");
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body1 = partdefinition.addOrReplaceChild("body1", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -2.0F, 2.0F, 6.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 24.0F, -7.0F));

        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 15).addBox(-2.5F, -4.0F, -5.0F, 5.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 16.5F, -3.0F));

        PartDefinition jaw = partdefinition.addOrReplaceChild("jaw", CubeListBuilder.create().texOffs(0, 24).addBox(-2.5F, 0.0F, -5.0F, 5.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 16.5F, -3.0F));

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -2.0F, -10.0F, 6.0F, 5.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 16.0F, 7.0F));

        PartDefinition left_arm = partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(28, 31).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 17.0F, -3.0F));

        PartDefinition right_arm = partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(32, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, 17.0F, -3.0F));

        PartDefinition left_thigh = partdefinition.addOrReplaceChild("left_thigh", CubeListBuilder.create().texOffs(0, 30).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 16.0F, 2.5F));

        PartDefinition right_thigh = partdefinition.addOrReplaceChild("right_thigh", CubeListBuilder.create().texOffs(14, 31).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, 16.0F, 2.5F));

        PartDefinition left_foot = partdefinition.addOrReplaceChild("left_foot", CubeListBuilder.create().texOffs(20, 15).addBox(-1.0F, 5.5F, -3.7F, 2.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 14.5F, 4.2F));

        PartDefinition right_foot = partdefinition.addOrReplaceChild("right_foot", CubeListBuilder.create().texOffs(20, 23).addBox(-1.0F, 5.5F, -3.7F, 2.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, 14.5F, 4.2F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(PestEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        jaw.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        head.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        body.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        left_arm.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        right_arm.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        left_thigh.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        right_thigh.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        left_foot.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        right_foot.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }

}