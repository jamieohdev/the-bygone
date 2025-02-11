package com.jamiedev.mod.common.client.models;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.Entity;
import java.util.Arrays;

@Environment(EnvType.CLIENT)
public class JellyshroomModel<T extends Entity> extends HierarchicalModel<T> {
    private final ModelPart[] tentacles = new ModelPart[8];
    private final ModelPart root;

    public JellyshroomModel(ModelPart root) {
        this.root = root;
        Arrays.setAll(this.tentacles, (index) -> {
            return root.getChild(getTentacleName(index));
        });
    }

    private static String getTentacleName(int index) {
        return "tentacle" + index;
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();
        CubeDeformation dilation = new CubeDeformation(0.02F);

        modelPartData.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -8.0F, -6.0F, 12.0F, 16.0F, 12.0F, dilation), PartPose.offset(0.0F, 8.0F, 0.0F));

        CubeListBuilder modelPartBuilder = CubeListBuilder.create().texOffs(48, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 18.0F, 2.0F);

        for(int k = 0; k < 8; ++k) {
            double d = (double)k * Math.PI * 2.0 / 8.0;
            float f = (float)Math.cos(d) * 5.0F;
            float g = 15.0F;
            float h = (float)Math.sin(d) * 5.0F;
            d = (double)k * Math.PI * -2.0 / 8.0 + 1.5707963267948966;
            float l = (float)d;
            modelPartData.addOrReplaceChild(getTentacleName(k), modelPartBuilder, PartPose.offsetAndRotation(f, 15.0F, h, 0.0F, l, 0.0F));
        }

        return LayerDefinition.create(modelData, 64, 32);
    }

    public void setupAnim(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        ModelPart[] var7 = this.tentacles;
        int var8 = var7.length;

        for(int var9 = 0; var9 < var8; ++var9) {
            ModelPart modelPart = var7[var9];
            modelPart.xRot = animationProgress;
        }

    }

    public ModelPart root() {
        return this.root;
    }
}
