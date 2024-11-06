package com.jamiedev.mod.client.models;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.util.math.MathHelper;

public class BigBeakModel<T extends Entity> extends AnimalModel<T> {

    private final ModelPart root;
    private final ModelPart rightLeg;
    private final ModelPart leftLeg;
    private final ModelPart rightWing;
    private final ModelPart leftWing;
    //private final ModelPart feathers;
    private final ModelPart head;
    private final ModelPart body;
    //private final ModelPart tail;

    public BigBeakModel(ModelPart root) {
        this.root = root.getChild("root");
        this.rightLeg = root.getChild("rightLeg");
        this.leftLeg = root.getChild("leftLeg");
        this.rightWing = root.getChild("rightWing");
        this.leftWing = root.getChild("leftWing");
       // this.feathers = root.getChild("feathers");
        this.head = root.getChild("head");
        this.body = root.getChild("body");
       // this.tail = root.getChild("tail");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData meshdefinition = new ModelData();
        ModelPartData partdefinition = meshdefinition.getRoot();

        ModelPartData root = partdefinition.addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        ModelPartData rightLeg = partdefinition.addChild("rightLeg", ModelPartBuilder.create().uv(64, 20).mirrored().cuboid(-1.5F, 0.0F, -2.0F, 3.0F, 17.0F, 4.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(-3.5F, 7.0F, 0.0F));

        ModelPartData leftLeg = partdefinition.addChild("leftLeg", ModelPartBuilder.create().uv(64, 20).cuboid(-1.5F, 0.0F, -2.0F, 3.0F, 17.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(3.5F, 7.0F, 0.0F));

        ModelPartData rightWing = partdefinition.addChild("rightWing", ModelPartBuilder.create().uv(43, 37).mirrored().cuboid(-2.0F, -8.0F, 0.0F, 2.0F, 13.0F, 16.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(-5.0F, 2.0F, -13.0F));

        ModelPartData leftWing = partdefinition.addChild("leftWing", ModelPartBuilder.create().uv(43, 37).cuboid(0.0F, -8.0F, 0.0F, 2.0F, 13.0F, 16.0F, new Dilation(0.0F)), ModelTransform.pivot(5.0F, 2.0F, -13.0F));

        ModelPartData head = partdefinition.addChild("head", ModelPartBuilder.create()
                        .uv(0, 64).cuboid(-3.0F, -19.25F, -3.5F, 6.0F, 24.0F, 7.0F, new Dilation(0.0F))
                        .uv(0, 37).cuboid(-2.0F, -21.25F, -16.5F, 4.0F, 9.0F, 17.0F, new Dilation(0.0F))

                ,
                ModelTransform.pivot(0.0F, -6.75F, -12.5F));

        //ModelPartData feathers = head.addChild("feathers", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -24.0F, -11.0F));
        //ModelPartData cube_r1 = feathers.addChild("cube_r1", ModelPartBuilder.create().uv(42, -2).cuboid(0.0F, -2.0F, 0.0F, 0.0F, 6.0F, 14.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

        //ModelPartData cube_r2 = feathers.addChild("cube_r2", ModelPartBuilder.create().uv(42, -2).cuboid(0.0F, -2.0F, 0.0F, 0.0F, 6.0F, 14.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

        ModelPartData body = partdefinition.addChild("body", ModelPartBuilder.create().uv(0, 0).cuboid(-5.0F, -7.5F, -10.5F, 10.0F, 15.0F, 21.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -0.5F, -2.5F));

        ModelPartData tail = body.addChild("tail", ModelPartBuilder.create().uv(78, 1).cuboid(-5.0F, 0.0F, -2.0F, 10.0F, 6.0F, 15.0F, new Dilation(-0.25F)), ModelTransform.pivot(0.0F, -7.5F, 10.5F));

        return TexturedModelData.of(meshdefinition, 128, 128);
    }


    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void render(MatrixStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        root.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        rightLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        leftLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        rightWing.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        leftWing.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
       // feathers.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        head.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        body.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
      //  tail.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }

    public void animateModel(ParrotEntity parrotEntity, float f, float g, float h) {
      //  this.animateModel(getPose(parrotEntity));
    }

    protected Iterable<ModelPart> getHeadParts() {
        return ImmutableList.of(this.head);
    }

    protected Iterable<ModelPart> getBodyParts() {
        return ImmutableList.of(this.body,  this.rightWing, this.leftWing, this.leftLeg,this.rightLeg);
    }

    @Override
    public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        this.head.pitch = headPitch * 0.017453292F;
        this.head.yaw = headYaw * 0.017453292F;
      //  this.feathers.pitch = this.head.pitch;
      //  this.feathers.yaw = this.head.yaw;
        this.rightLeg.pitch = MathHelper.cos(limbAngle * 0.6662F) * 1.4F * limbDistance;
        this.leftLeg.pitch = MathHelper.cos(limbAngle * 0.6662F + 3.1415927F) * 1.4F * limbDistance;
    }
}
