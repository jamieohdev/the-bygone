package com.jamiedev.bygone.client.models;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;

public class DuckieModel<T extends Entity> extends AgeableListModel<T> {
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart wing_left;
    private final ModelPart wing_right;
    private final ModelPart tail;

    public DuckieModel(ModelPart root) {
        this.head = root.getChild("head");
        this.body = root.getChild("body");
        this.wing_left = root.getChild("wing_left");
        this.wing_right = root.getChild("wing_right");
        this.tail = root.getChild("tail");
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();
        PartDefinition head = modelPartData.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 19).addBox(-4.0F, -19.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(0, 35).addBox(-3.0F, -13.5F, -8.0F, 6.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-1.0F, -14.5F, -5.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition body = modelPartData.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -11.0F, -3.0F, 10.0F, 7.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition wing_left = modelPartData.addOrReplaceChild("wing_left", CubeListBuilder.create().texOffs(34, 19).addBox(5.0F, -10.0F, -1.0F, 1.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition wing_right = modelPartData.addOrReplaceChild("wing_right", CubeListBuilder.create().texOffs(24, 27).addBox(-6.0F, -10.0F, -1.0F, 1.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition tail = modelPartData.addOrReplaceChild("tail", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition tail_r1 = tail.addOrReplaceChild("tail_r1", CubeListBuilder.create().texOffs(32, 0).addBox(-9.0F, -9.0F, -1.0F, 10.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, -4.0F, 9.0F, -0.3491F, 0.0F, 0.0F));
        return LayerDefinition.create(modelData, 64, 64);
    }

    @Override
    public void renderToBuffer(PoseStack matrices, VertexConsumer vertexConsumer, int light, int overlay, int color) {
        head.render(matrices, vertexConsumer, light, overlay, color);
        body.render(matrices, vertexConsumer, light, overlay, color);
        wing_left.render(matrices, vertexConsumer, light, overlay, color);
        wing_right.render(matrices, vertexConsumer, light, overlay, color);
        tail.render(matrices, vertexConsumer, light, overlay, color);
    }

    @Override
    protected Iterable<ModelPart> headParts() {
        return ImmutableList.of(this.head);
    }

    @Override
    protected Iterable<ModelPart> bodyParts() {
        return ImmutableList.of(this.body, this.wing_right, this.wing_left, this.tail);
    }

    @Override
    public void setupAnim(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        this.head.xRot = headPitch * 0.017453292F;
        this.head.yRot = headYaw * 0.017453292F;

    }
}