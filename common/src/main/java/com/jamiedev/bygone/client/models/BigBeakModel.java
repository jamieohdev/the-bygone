package com.jamiedev.bygone.client.models;

import com.google.common.collect.ImmutableList;
import com.jamiedev.bygone.client.models.animations.BigBeakAnimations;
import com.jamiedev.bygone.common.entity.BigBeakEntity;
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
import net.minecraft.world.entity.animal.Parrot;

public class BigBeakModel<T extends BigBeakEntity> extends HierarchicalModel<T> {

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
        this.root  = root;
        this.rightLeg = root.getChild("rightLeg");
        this.leftLeg = root.getChild("leftLeg");
        this.rightWing = root.getChild("rightWing");
        this.leftWing = root.getChild("leftWing");
       // this.feathers = root.getChild("feathers");
        this.head = root.getChild("head");
        this.body = root.getChild("body");
       // this.tail = root.getChild("tail");
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition rightLeg = partdefinition.addOrReplaceChild("rightLeg", CubeListBuilder.create().texOffs(64, 20).mirror().addBox(-1.5F, 0.0F, -2.0F, 3.0F, 17.0F, 4.0F, new CubeDeformation(-0.01F)).mirror(false), PartPose.offset(-3.5F, 7.0F, 0.0F));

        PartDefinition leftLeg = partdefinition.addOrReplaceChild("leftLeg", CubeListBuilder.create().texOffs(64, 20).addBox(-1.5F, 0.0F, -2.0F, 3.0F, 17.0F, 4.0F, new CubeDeformation(-0.01F)), PartPose.offset(3.5F, 7.0F, 0.0F));

        PartDefinition rightWing = partdefinition.addOrReplaceChild("rightWing", CubeListBuilder.create().texOffs(43, 37).mirror().addBox(-2.0F, -8.0F, 0.0F, 2.0F, 13.0F, 16.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-5.0F, 2.0F, -13.0F));

        PartDefinition leftWing = partdefinition.addOrReplaceChild("leftWing", CubeListBuilder.create().texOffs(43, 37).addBox(0.0F, -8.0F, 0.0F, 2.0F, 13.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, 2.0F, -13.0F));

        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create()
                        .texOffs(0, 64).addBox(-3.0F, -19.25F, -3.5F, 6.0F, 24.0F, 7.0F, new CubeDeformation(0.0F))
                        .texOffs(0, 37).addBox(-2.0F, -21.25F, -16.5F, 4.0F, 9.0F, 17.0F, new CubeDeformation(0.0F))

                ,
                PartPose.offset(0.0F, -6.75F, -12.5F));

        //ModelPartData feathers = head.addChild("feathers", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -24.0F, -11.0F));
        //ModelPartData cube_r1 = feathers.addChild("cube_r1", ModelPartBuilder.create().uv(42, -2).cuboid(0.0F, -2.0F, 0.0F, 0.0F, 6.0F, 14.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

        //ModelPartData cube_r2 = feathers.addChild("cube_r2", ModelPartBuilder.create().uv(42, -2).cuboid(0.0F, -2.0F, 0.0F, 0.0F, 6.0F, 14.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -7.5F, -10.5F, 10.0F, 15.0F, 21.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.5F, -2.5F));

        PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(78, 1).addBox(-5.0F, 0.0F, -2.0F, 10.0F, 6.0F, 15.0F, new CubeDeformation(-0.25F)), PartPose.offset(0.0F, -7.5F, 10.5F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        root.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        //Do not render children of "root", root.render() already does that.
    }

    @Override
    public ModelPart root() {
        return root;
    }

    protected Iterable<ModelPart> getHeadParts() {
        return ImmutableList.of(this.head);
    }

    protected Iterable<ModelPart> getBodyParts() {
        return ImmutableList.of(this.body,  this.rightWing, this.leftWing, this.leftLeg,this.rightLeg);
    }
    private void setHeadAngles(float headYaw, float headPitch) {
        headYaw = Mth.clamp(headYaw, -30.0F, 30.0F);
        headPitch = Mth.clamp(headPitch, -25.0F, 45.0F);

        this.head.yRot = headYaw * 0.017453292F;
        this.head.xRot = headPitch * 0.017453292F;
    }

    @Override
    public void setupAnim(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {

        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.setHeadAngles(headYaw, headPitch);
        this.animate(entity.idleAnimationState, BigBeakAnimations.BIGBEAK_IDLE, animationProgress, 1f);
        this.animate(entity.flappingAnimationState, BigBeakAnimations.BIGBEAK_FLAP, animationProgress, 0.5f);
        this.animateWalk(BigBeakAnimations.BIGBEAK_FLAP, limbAngle, limbDistance,2f, 2.5f);

        this.animateWalk(BigBeakAnimations.BIGBEAK_WALKING_BEFUZZY_STYLED, limbAngle, limbDistance,2f, 2.5f);
    }
}
