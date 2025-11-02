package com.jamiedev.bygone.client.models;

import com.jamiedev.bygone.common.entity.WispEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class WispModel<T extends WispEntity> extends EntityModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    private final ModelPart bb_main;

    public WispModel(ModelPart root) {
        this.bb_main = root.getChild("bb_main");
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -9.0F, -4.0F, 9.0F, 9.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition leg_right_r1 = bb_main.addOrReplaceChild("leg_right_r1", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -1.0F, 2.0F, 0.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(2, 0).addBox(0.0F, -1.0F, -2.0F, 0.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.4363F));

        PartDefinition arm_left_r1 = bb_main.addOrReplaceChild("arm_left_r1", CubeListBuilder.create().texOffs(0, 4).addBox(0.0F, -1.0F, -6.0F, 0.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

        PartDefinition arm_right_r1 = bb_main.addOrReplaceChild("arm_right_r1", CubeListBuilder.create().texOffs(4, 0).addBox(0.0F, 0.0F, 6.0F, 0.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(WispEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        bb_main.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }
}