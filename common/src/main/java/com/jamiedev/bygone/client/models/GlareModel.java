package com.jamiedev.bygone.client.models;
// Made with Blockbench 4.11.2
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.jamiedev.bygone.client.models.animations.GlareAnimations;
import com.jamiedev.bygone.common.entity.GlareEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class GlareModel<T extends GlareEntity> extends HierarchicalModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    private final ModelPart root;
    private final ModelPart Head;
    private final ModelPart Face;
    private final ModelPart neutral;
    private final ModelPart tired;
    private final ModelPart closed;
    private final ModelPart angry;
    private final ModelPart Body;

    public GlareModel(ModelPart root) {
        this.root = root.getChild("root");
        this.Head = this.root.getChild("Head");
        this.Face = this.Head.getChild("Face");
        this.neutral = this.Face.getChild("neutral");
        this.tired = this.Face.getChild("tired");
        this.closed = this.Face.getChild("closed");
        this.angry = this.Face.getChild("angry");
        this.Body = this.root.getChild("Body");
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition ModelPartData = meshdefinition.getRoot();

        PartDefinition root = ModelPartData.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition Head = root.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(0, 27).addBox(-6.0F, -11.0F, -6.0F, 12.0F, 10.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-7.0F, -13.0F, -7.0F, 14.0F, 13.0F, 14.0F, new CubeDeformation(0.0F))
                .texOffs(0, 80).mirror().addBox(-7.0F, 0.0F, -7.0F, 14.0F, 2.0F, 14.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, -12.0F, 0.0F));

        PartDefinition Face = Head.addOrReplaceChild("Face", CubeListBuilder.create(), PartPose.offset(5.0F, -1.0F, -5.0F));

        PartDefinition neutral = Face.addOrReplaceChild("neutral", CubeListBuilder.create().texOffs(104, 0).addBox(-11.0F, -10.0F, -1.0F, 12.0F, 10.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition tired = Face.addOrReplaceChild("tired", CubeListBuilder.create().texOffs(104, 10).addBox(-11.0F, -10.0F, -1.0F, 12.0F, 10.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition closed = Face.addOrReplaceChild("closed", CubeListBuilder.create().texOffs(104, 20).addBox(-11.0F, -10.0F, -1.0F, 12.0F, 10.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition angry = Face.addOrReplaceChild("angry", CubeListBuilder.create().texOffs(104, 30).addBox(-11.0F, -10.0F, -1.0F, 12.0F, 10.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition body = root.addOrReplaceChild("Body", CubeListBuilder.create()
                        .texOffs(0, 49).mirror().addBox(-4.5F, 0.0F, -4.5F, 9.0F, 6.0F, 9.0F, new CubeDeformation(0.0F)).mirror(false)
                        .texOffs(0, 64).mirror().addBox(-4.5F, 0.0F, -4.5F, 9.0F, 7.0F, 9.0F, new CubeDeformation(0.25F)).mirror(false),
                PartPose.offset(0.0F, -12.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }


    @Override
    public ModelPart root() {
        return root;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        root.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }

    @Override
    public void setupAnim(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);

        this.animate(entity.idleAnimationState, GlareAnimations.GLARE_FACE_IDLE, animationProgress, 1f);
        this.animate(entity.idleAnimationState, GlareAnimations.GLARE_BODY_IDLE, animationProgress, 1f);
        this.animateWalk(GlareAnimations.GLARE_BODY_MOVE, limbAngle, limbDistance, 2f, 2.5f);

    }

}