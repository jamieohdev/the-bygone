package com.jamiedev.bygone.client.models;

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

public class MoobooModel<T extends Entity> extends EntityModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart leg1;
    private final ModelPart leg2;
    private final ModelPart leg3;
    private final ModelPart leg4;

    public MoobooModel(ModelPart root) {
        this.head = root.getChild("head");
        this.body = root.getChild("body");
        this.leg1 = root.getChild("leg1");
        this.leg2 = root.getChild("leg2");
        this.leg3 = root.getChild("leg3");
        this.leg4 = root.getChild("leg4");
    }

    public static LayerDefinition getTexturedModelData()  {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition ModelPartData = meshdefinition.getRoot();

        PartDefinition head = ModelPartData.addOrReplaceChild("head",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-4.0F, -19.0F, -6.0F, 8.0F, 8.0F, 6.0F,
                                new CubeDeformation(0.0F))
                .texOffs(22, 0)
                        .addBox(4.0F, -20.0F, -4.0F, 1.0F, 3.0F, 1.0F,
                                new CubeDeformation(0.0F))
                .texOffs(22, 0)
                        .addBox(-5.0F, -20.0F, -4.0F, 1.0F, 3.0F, 1.0F,
                                new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 4.0F, -8.0F, 0.0F, 0.0F, -3.1416F));

        PartDefinition body = ModelPartData.addOrReplaceChild("body", CubeListBuilder.create().texOffs(18, 4)
                .addBox(-6.0F, -10.0F, 6.0F, 12.0F, 18.0F, 10.0F,
                        new CubeDeformation(0.0F))
                .texOffs(52, 0)
                .addBox(-2.0F, 2.0F, 5.0F, 4.0F, 6.0F, 1.0F,
                        new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 5.0F, 2.0F, 1.5708F, 0.0F, -3.1416F));

        PartDefinition leg1 = ModelPartData.addOrReplaceChild("leg1", CubeListBuilder.create()
                .texOffs(0, 16)
                .addBox(-2.0F, 1.0F, -2.0F, 4.0F, 12.0F, 4.0F,
                        new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-4.0F, 12.0F, 7.0F, 0.0F, 0.0F, -3.1416F));

        PartDefinition leg2 = ModelPartData.addOrReplaceChild("leg2", CubeListBuilder.create()
                .texOffs(0, 16)
                .addBox(-2.0F, 1.0F, -2.0F, 4.0F, 12.0F, 4.0F,
                        new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(4.0F, 12.0F, 7.0F, 0.0F, 0.0F, -3.1416F));

        PartDefinition leg3 = ModelPartData.addOrReplaceChild("leg3", CubeListBuilder.create()
                .texOffs(0, 16)
                .addBox(-2.0F, 1.0F, -1.0F, 4.0F, 12.0F, 4.0F,
                        new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-4.0F, 12.0F, -6.0F, 0.0F, 0.0F, -3.1416F));

        PartDefinition leg4 = ModelPartData.addOrReplaceChild("leg4", CubeListBuilder.create()
                .texOffs(0, 16)
                .addBox(-2.0F, 1.0F, -1.0F, 4.0F, 12.0F, 4.0F,
                        new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(4.0F, 12.0F, -6.0F, 0.0F, 0.0F, -3.1416F));

        return LayerDefinition.create(meshdefinition, 64, 32);
    }


    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
       head.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        body.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        leg1.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        leg2.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        leg3.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        leg4.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }

    @Override
    public void setupAnim(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        this.head.xRot = headPitch * 0.017453292F;
        this.head.yRot = headYaw * 0.017453292F;
        this.leg4.xRot = Mth.cos(limbAngle * 0.6662F) * 1.4F * limbDistance;
        this.leg3.xRot = Mth.cos(limbAngle * 0.6662F + 3.1415927F) * 1.4F * limbDistance;
        this.leg2.xRot = Mth.cos(limbAngle * 0.6662F + 3.1415927F) * 1.4F * limbDistance;
        this.leg1.xRot = Mth.cos(limbAngle * 0.6662F) * 1.4F * limbDistance;
    }
}
