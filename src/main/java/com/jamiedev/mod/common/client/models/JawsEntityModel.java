package com.jamiedev.mod.common.client.models;

import com.jamiedev.mod.common.entities.JawsEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

public class JawsEntityModel extends HierarchicalModel<JawsEntity>

{
    private final ModelPart root;
    private final ModelPart head;

    public JawsEntityModel(ModelPart root) {
        this.root = root;
        this.head = root.getChild("head");

    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();
        PartDefinition modelPartData2 = modelPartData.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, 10.0F, -8.0F, 12.0F, 12.0F, 16.0F).texOffs(0, 28).addBox(-8.0F, 10.0F, -6.0F, 2.0F, 12.0F, 12.0F).texOffs(0, 28).addBox(6.0F, 10.0F, -6.0F, 2.0F, 12.0F, 12.0F, true).texOffs(16, 40).addBox(-6.0F, 8.0F, -6.0F, 12.0F, 2.0F, 12.0F).texOffs(16, 40).addBox(-6.0F, 22.0F, -6.0F, 12.0F, 2.0F, 12.0F), PartPose.ZERO);
        CubeListBuilder modelPartBuilder = CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -4.5F, -1.0F, 2.0F, 9.0F, 2.0F);



        modelPartData2.addOrReplaceChild("eye", CubeListBuilder.create().texOffs(8, 0).addBox(-1.0F, 15.0F, 0.0F, 2.0F, 2.0F, 1.0F), PartPose.offset(0.0F, 0.0F, -8.25F));
        PartDefinition modelPartData3 = modelPartData2.addOrReplaceChild("tail0", CubeListBuilder.create().texOffs(40, 0).addBox(-2.0F, 14.0F, 7.0F, 4.0F, 4.0F, 8.0F), PartPose.ZERO);
        PartDefinition modelPartData4 = modelPartData3.addOrReplaceChild("tail1", CubeListBuilder.create().texOffs(0, 54).addBox(0.0F, 14.0F, 0.0F, 3.0F, 3.0F, 7.0F), PartPose.offset(-1.5F, 0.5F, 14.0F));
        modelPartData4.addOrReplaceChild("tail2", CubeListBuilder.create().texOffs(41, 32).addBox(0.0F, 14.0F, 0.0F, 2.0F, 2.0F, 6.0F).texOffs(25, 19).addBox(1.0F, 10.5F, 3.0F, 1.0F, 9.0F, 9.0F), PartPose.offset(0.5F, 0.5F, 6.0F));
        return LayerDefinition.create(modelData, 64, 64);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    private static float getAngle(int index, float animationProgress, float magnitude) {
        return 1.0F + Mth.cos(animationProgress * 1.5F + (float)index) * 0.01F - magnitude;
    }


    @Override
    public void setAngles(JawsEntity guardianEntity, float f, float g, float h, float i, float j) {
        float k = h - (float)guardianEntity.tickCount;
        this.head.yRot = i * 0.017453292F;
        this.head.xRot = j * 0.017453292F;
    }
}
