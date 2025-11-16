package com.jamiedev.bygone.client.models;

import net.minecraft.client.model.ColorableHierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class PrimordialFishModelA<T extends Entity> extends ColorableHierarchicalModel<T> {
    private final ModelPart root;
    private final ModelPart tail;

    public PrimordialFishModelA(ModelPart root) {
        this.root = root;
        this.tail = root.getChild("tail");
    }

    public static LayerDefinition createBodyLayer(CubeDeformation cubeDeformation) {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition root = meshDefinition.getRoot();
        int bodyYOffset = 22;
        root.addOrReplaceChild(
                "body",
                CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -1.5F, -3.0F, 2.0F, 3.0F, 6.0F, cubeDeformation),
                PartPose.offset(0.0F, bodyYOffset, 0.0F)
        );
        root.addOrReplaceChild(
                "tail",
                CubeListBuilder.create().texOffs(22, -6).addBox(0.0F, -1.5F, 0.0F, 0.0F, 3.0F, 6.0F, cubeDeformation),
                PartPose.offset(0.0F, bodyYOffset, 3.0F)
        );
        root.addOrReplaceChild(
                "right_fin",
                CubeListBuilder.create().texOffs(2, 16).addBox(-2.0F, -1.0F, 0.0F, 2.0F, 2.0F, 0.0F, cubeDeformation),
                PartPose.offsetAndRotation(-1.0F, bodyYOffset + 0.5F, 0.0F, 0.0F, ((float) Math.PI) / 4F, 0.0F)
        );
        root.addOrReplaceChild(
                "left_fin",
                CubeListBuilder.create().texOffs(2, 12).addBox(0.0F, -1.0F, 0.0F, 2.0F, 2.0F, 0.0F, cubeDeformation),
                PartPose.offsetAndRotation(1.0F, bodyYOffset + 0.5F, 0.0F, 0.0F, -((float) Math.PI) / 4F, 0.0F)
        );
        root.addOrReplaceChild(
                "top_fin",
                CubeListBuilder.create().texOffs(10, -5).addBox(0.0F, -3.0F, 0.0F, 0.0F, 3.0F, 6.0F, cubeDeformation),
                PartPose.offset(0.0F, bodyYOffset - 1.5F, -3.0F)
        );
        return LayerDefinition.create(meshDefinition, 32, 32);
    }

    @Override
    public @NotNull ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        float tailFlopScale = 1.0F;
        if (!entity.isInWater()) {
            tailFlopScale = 1.5F;
        }

        this.tail.yRot = -tailFlopScale * 0.45F * Mth.sin(0.6F * ageInTicks);
    }
}