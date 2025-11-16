package com.jamiedev.bygone.client.models;

import net.minecraft.client.model.ColorableHierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class PrimordialFishModelB<T extends Entity> extends ColorableHierarchicalModel<T> {
    private final ModelPart root;
    private final ModelPart tail;

    public PrimordialFishModelB(ModelPart root) {
        super();
        this.root = root;
        this.tail = root.getChild("tail");
    }

    public static LayerDefinition createBodyLayer(CubeDeformation cubeDeformation) {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition root = meshdefinition.getRoot();

        PartDefinition body = root.addOrReplaceChild(
                "body",
                CubeListBuilder.create()
                        .texOffs(0, 21)
                        .addBox(-2.0F, -1.0F, -4.0F, 4.0F, 4.0F, 7.0F, cubeDeformation),
                PartPose.offset(0.0F, 21.0F, 3.0F)
        );

        PartDefinition fin_left = body.addOrReplaceChild(
                "fin_left",
                CubeListBuilder.create(),
                PartPose.offset(0.0F, 0.0F, 0.0F)
        );

        PartDefinition fin_right = body.addOrReplaceChild(
                "fin_right",
                CubeListBuilder.create(),
                PartPose.offset(0.0F, 0.0F, 0.0F)
        );


        PartDefinition right_fin = fin_right.addOrReplaceChild(
                "right_fin",
                CubeListBuilder.create()
                        .texOffs(0, 16)
                        .addBox(-4.0F, -3.0F, 0.0F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-2.0F, 3.0F, -2.0F, 0.0F, 0.7854F, 0.0F)
        );

        PartDefinition left_fin = fin_left.addOrReplaceChild(
                "left_fin",
                CubeListBuilder.create()
                        .texOffs(0, 12)
                        .addBox(0.0F, -3.0F, 0.0F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(2.0F, 3.0F, -2.0F, 0.0F, -0.7854F, 0.0F)
        );

        PartDefinition fin_top = root.addOrReplaceChild(
                "fin_top",
                CubeListBuilder.create()
                        .texOffs(16, 9)
                        .addBox(0.0F, -4.0F, -1.0F, 0.0F, 4.0F, 8.0F, cubeDeformation),
                PartPose.offset(0.0F, 20.0F, 0.0F)
        );

        PartDefinition fin_bottom = root.addOrReplaceChild(
                "fin_bottom",
                CubeListBuilder.create()
                        .texOffs(16, 4)
                        .addBox(0.0F, 0.0F, -1.0F, 0.0F, 4.0F, 8.0F, cubeDeformation),
                PartPose.offset(0.0F, 24.0F, 0.0F)
        );

        PartDefinition tail = root.addOrReplaceChild(
                "tail",
                CubeListBuilder.create()
                        .texOffs(21, 16)
                        .addBox(0.0F, -3.0F, 0.0F, 0.0F, 6.0F, 5.0F, cubeDeformation),
                PartPose.offset(0.0F, 22.0F, 6.0F)
        );

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    public @NotNull ModelPart root() {
        return this.root;
    }

    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        float tailFlopScale = 1.0F;
        if (!entity.isInWater()) {
            tailFlopScale = 1.5F;
        }

        this.tail.yRot = -tailFlopScale * 0.45F * Mth.sin(0.6F * ageInTicks);
    }
}