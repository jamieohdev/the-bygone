package com.jamiedev.mod.common.client.models;

import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
public class GlareModel<E extends Entity> extends EntityModel<E>
{
    private final ModelPart head;
    private final ModelPart body;
    public GlareModel(ModelPart root) {
        this.head = root.getChild("head");
        this.body = root.getChild("body");
    }
    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData head = modelPartData.addChild("head", ModelPartBuilder.create().uv(0, 0).cuboid(-8.0F, -28.0F, -8.0F, 16.0F, 11.0F, 16.0F, new Dilation(0.0F))
                .uv(0, 47).cuboid(-7.0F, -27.0F, -7.0F, 14.0F, 10.0F, 14.0F, new Dilation(0.0F))
                .uv(0, 27).cuboid(-8.0F, -20.0F, -8.0F, 16.0F, 4.0F, 16.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        ModelPartData body = modelPartData.addChild("body", ModelPartBuilder.create().uv(50, 13).cuboid(-7.0F, -16.0F, -7.0F, 14.0F, 5.0F, 14.0F, new Dilation(0.0F))
                .uv(51, 34).cuboid(-6.5F, -13.0F, -6.5F, 13.0F, 5.0F, 13.0F, new Dilation(0.0F))
                .uv(57, 53).cuboid(-6.0F, -9.0F, -6.0F, 12.0F, 4.0F, 12.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
        return TexturedModelData.of(modelData, 128, 128);
    }
    @Override
    public void setAngles(E entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        this.head.pitch = headPitch * 0.017453292F;
        this.head.yaw = headYaw * 0.017453292F;
        this.body.pitch = headPitch * 0.017453292F;
        this.body.yaw =  headYaw * 0.017453292F;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, int color) {
        head.render(matrices, vertexConsumer, light, overlay, color);
        body.render(matrices, vertexConsumer, light, overlay, color);
    }
}
