package com.jamiedev.mod.common.client.models;

import com.jamiedev.mod.common.entities.JawsEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.util.math.MathHelper;

public class JawsEntityModel extends SinglePartEntityModel<JawsEntity>

{
    private final ModelPart root;
    private final ModelPart head;

    public JawsEntityModel(ModelPart root) {
        this.root = root;
        this.head = root.getChild("head");

    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData modelPartData2 = modelPartData.addChild("head", ModelPartBuilder.create().uv(0, 0).cuboid(-6.0F, 10.0F, -8.0F, 12.0F, 12.0F, 16.0F).uv(0, 28).cuboid(-8.0F, 10.0F, -6.0F, 2.0F, 12.0F, 12.0F).uv(0, 28).cuboid(6.0F, 10.0F, -6.0F, 2.0F, 12.0F, 12.0F, true).uv(16, 40).cuboid(-6.0F, 8.0F, -6.0F, 12.0F, 2.0F, 12.0F).uv(16, 40).cuboid(-6.0F, 22.0F, -6.0F, 12.0F, 2.0F, 12.0F), ModelTransform.NONE);
        ModelPartBuilder modelPartBuilder = ModelPartBuilder.create().uv(0, 0).cuboid(-1.0F, -4.5F, -1.0F, 2.0F, 9.0F, 2.0F);



        modelPartData2.addChild("eye", ModelPartBuilder.create().uv(8, 0).cuboid(-1.0F, 15.0F, 0.0F, 2.0F, 2.0F, 1.0F), ModelTransform.pivot(0.0F, 0.0F, -8.25F));
        ModelPartData modelPartData3 = modelPartData2.addChild("tail0", ModelPartBuilder.create().uv(40, 0).cuboid(-2.0F, 14.0F, 7.0F, 4.0F, 4.0F, 8.0F), ModelTransform.NONE);
        ModelPartData modelPartData4 = modelPartData3.addChild("tail1", ModelPartBuilder.create().uv(0, 54).cuboid(0.0F, 14.0F, 0.0F, 3.0F, 3.0F, 7.0F), ModelTransform.pivot(-1.5F, 0.5F, 14.0F));
        modelPartData4.addChild("tail2", ModelPartBuilder.create().uv(41, 32).cuboid(0.0F, 14.0F, 0.0F, 2.0F, 2.0F, 6.0F).uv(25, 19).cuboid(1.0F, 10.5F, 3.0F, 1.0F, 9.0F, 9.0F), ModelTransform.pivot(0.5F, 0.5F, 6.0F));
        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public ModelPart getPart() {
        return this.root;
    }

    private static float getAngle(int index, float animationProgress, float magnitude) {
        return 1.0F + MathHelper.cos(animationProgress * 1.5F + (float)index) * 0.01F - magnitude;
    }


    @Override
    public void setAngles(JawsEntity guardianEntity, float f, float g, float h, float i, float j) {
        float k = h - (float)guardianEntity.age;
        this.head.yaw = i * 0.017453292F;
        this.head.pitch = j * 0.017453292F;
    }
}
