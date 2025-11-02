package com.jamiedev.bygone.client.models;

import com.jamiedev.bygone.client.models.animations.WhiskbillAnimations;
import com.jamiedev.bygone.common.entity.WhiskbillEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class WhiskbillModel<T extends WhiskbillEntity> extends HierarchicalModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    private final ModelPart root;
    private final ModelPart Body;
    private final ModelPart Tail;
    private final ModelPart Head;
    private final ModelPart whisker2;
    private final ModelPart whisker3;
    private final ModelPart whisker1;
    private final ModelPart whisker4;
    private final ModelPart Leg_BL;
    private final ModelPart leg_BR;
    private final ModelPart Leg_FL;
    private final ModelPart Leg_FR;

    public WhiskbillModel(ModelPart root) {
        this.root = root.getChild("root");
        this.Body = this.root.getChild("Body");
        this.Tail = this.Body.getChild("Tail");
        this.Head = this.Body.getChild("Head");
        this.whisker2 = this.Head.getChild("whisker2");
        this.whisker3 = this.Head.getChild("whisker3");
        this.whisker1 = this.Head.getChild("whisker1");
        this.whisker4 = this.Head.getChild("whisker4");
        this.Leg_BL = this.Body.getChild("Leg_BL");
        this.leg_BR = this.Body.getChild("leg_BR");
        this.Leg_FL = this.Body.getChild("Leg_FL");
        this.Leg_FR = this.Body.getChild("Leg_FR");
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition Body = root.addOrReplaceChild("Body", CubeListBuilder.create().texOffs(0, 105).addBox(-13.5F, 2.25F, -18.0F, 27.0F, 0.0F, 36.0F, new CubeDeformation(0.0F))
                .texOffs(0, 54).addBox(-13.5F, -9.75F, -18.0F, 27.0F, 15.0F, 36.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -10.25F, 2.0F));

        PartDefinition Tail = Body.addOrReplaceChild("Tail", CubeListBuilder.create().texOffs(0, 0).addBox(-10.5F, -3.0F, -5.0F, 21.0F, 13.0F, 41.0F, new CubeDeformation(0.0F))
                .texOffs(131, 7).addBox(-3.5F, -3.0F, -6.0F, 7.0F, 6.0F, 35.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -5.75F, 18.0F));

        PartDefinition Head = Body.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(126, 48).addBox(-9.5F, -4.0F, -19.0F, 19.0F, 8.0F, 22.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.75F, -21.0F));

        PartDefinition whisker2 = Head.addOrReplaceChild("whisker2", CubeListBuilder.create().texOffs(126, 78).addBox(-2.0F, 0.0F, -10.0F, 17.0F, 0.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.5F, 4.0F, -6.0F, 0.0F, 0.0F, 0.4363F));

        PartDefinition whisker3 = Head.addOrReplaceChild("whisker3", CubeListBuilder.create().texOffs(126, 78).mirror().addBox(-15.0F, 0.0F, -10.0F, 17.0F, 0.0F, 14.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-9.5F, 4.0F, -6.0F, 0.0F, 0.0F, -0.4363F));

        PartDefinition whisker1 = Head.addOrReplaceChild("whisker1", CubeListBuilder.create().texOffs(28, 141).addBox(-3.0F, -11.0F, 0.0F, 7.0F, 15.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(9.5F, -4.0F, -5.0F));

        PartDefinition whisker4 = Head.addOrReplaceChild("whisker4", CubeListBuilder.create().texOffs(28, 141).mirror().addBox(-4.0F, -11.0F, 0.0F, 7.0F, 15.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-9.5F, -4.0F, -5.0F));

        PartDefinition Leg_BL = Body.addOrReplaceChild("Leg_BL", CubeListBuilder.create().texOffs(42, 141).addBox(-4.5F, 11.0F, 6.0F, 8.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(126, 121).addBox(-4.5F, -3.0F, -4.0F, 8.0F, 16.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(13.0F, -2.75F, 10.0F));

        PartDefinition leg_BR = Body.addOrReplaceChild("leg_BR", CubeListBuilder.create().texOffs(42, 141).mirror().addBox(-3.5F, 11.0F, 6.0F, 8.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(126, 121).mirror().addBox(-3.5F, -3.0F, -4.0F, 8.0F, 16.0F, 10.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-13.0F, -2.75F, 10.0F));

        PartDefinition Leg_FL = Body.addOrReplaceChild("Leg_FL", CubeListBuilder.create().texOffs(126, 92).addBox(-3.0F, -4.0F, -4.0F, 8.0F, 19.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(0, 141).addBox(-7.0F, 13.0F, -4.0F, 4.0F, 2.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(13.0F, -4.75F, -10.0F));

        PartDefinition Leg_FR = Body.addOrReplaceChild("Leg_FR", CubeListBuilder.create().texOffs(126, 92).mirror().addBox(-5.0F, -4.0F, -4.0F, 8.0F, 19.0F, 10.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 141).mirror().addBox(3.0F, 13.0F, -4.0F, 4.0F, 2.0F, 10.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-13.0F, -4.75F, -10.0F));

        return LayerDefinition.create(meshdefinition, 256, 256);
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

    @Override
    public void setupAnim(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);

        this.animate(entity.idleAnimationState, WhiskbillAnimations.WHISKBILL_IDLE, animationProgress, 1f);
        this.animateWalk(WhiskbillAnimations.WHISKBILL_WALK, limbAngle, limbDistance, 2f, 2.5f);

    }
}