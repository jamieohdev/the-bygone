package com.jamiedev.mod.common.client.models;

import com.jamiedev.mod.common.entities.MoobooEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class MoobooModel<T extends Entity> extends EntityModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart leg1;
    private final ModelPart leg2;
    private final ModelPart leg3;
    private final ModelPart leg4;

    public MoobooModel(ModelPart root) {
        this.head = root.getChild("head");
        this.body = root.getChild("body");
        this.leg1 = root.getChild("leg1");
        this.leg2 = root.getChild("leg2");
        this.leg3 = root.getChild("leg3");
        this.leg4 = root.getChild("leg4");
    }

    public static TexturedModelData getTexturedModelData()  {
        ModelData meshdefinition = new ModelData();
        ModelPartData ModelPartData = meshdefinition.getRoot();

        ModelPartData head = ModelPartData.addChild("head",
                ModelPartBuilder.create().uv(0, 0)
                        .cuboid(-4.0F, -19.0F, -6.0F, 8.0F, 8.0F, 6.0F,
                                new Dilation(0.0F))
                .uv(22, 0)
                        .cuboid(4.0F, -20.0F, -4.0F, 1.0F, 3.0F, 1.0F,
                                new Dilation(0.0F))
                .uv(22, 0)
                        .cuboid(-5.0F, -20.0F, -4.0F, 1.0F, 3.0F, 1.0F,
                                new Dilation(0.0F)),
                ModelTransform.of(0.0F, 4.0F, -8.0F, 0.0F, 0.0F, -3.1416F));

        ModelPartData body = ModelPartData.addChild("body", ModelPartBuilder.create().uv(18, 4)
                .cuboid(-6.0F, -10.0F, 6.0F, 12.0F, 18.0F, 10.0F,
                        new Dilation(0.0F))
                .uv(52, 0)
                .cuboid(-2.0F, 2.0F, 5.0F, 4.0F, 6.0F, 1.0F,
                        new Dilation(0.0F)),
                ModelTransform.of(0.0F, 5.0F, 2.0F, 1.5708F, 0.0F, -3.1416F));

        ModelPartData leg1 = ModelPartData.addChild("leg1", ModelPartBuilder.create()
                .uv(0, 16)
                .cuboid(-2.0F, 1.0F, -2.0F, 4.0F, 12.0F, 4.0F,
                        new Dilation(0.0F)),
                ModelTransform.of(-4.0F, 12.0F, 7.0F, 0.0F, 0.0F, -3.1416F));

        ModelPartData leg2 = ModelPartData.addChild("leg2", ModelPartBuilder.create()
                .uv(0, 16)
                .cuboid(-2.0F, 1.0F, -2.0F, 4.0F, 12.0F, 4.0F,
                        new Dilation(0.0F)),
                ModelTransform.of(4.0F, 12.0F, 7.0F, 0.0F, 0.0F, -3.1416F));

        ModelPartData leg3 = ModelPartData.addChild("leg3", ModelPartBuilder.create()
                .uv(0, 16)
                .cuboid(-2.0F, 1.0F, -1.0F, 4.0F, 12.0F, 4.0F,
                        new Dilation(0.0F)),
                ModelTransform.of(-4.0F, 12.0F, -6.0F, 0.0F, 0.0F, -3.1416F));

        ModelPartData leg4 = ModelPartData.addChild("leg4", ModelPartBuilder.create()
                .uv(0, 16)
                .cuboid(-2.0F, 1.0F, -1.0F, 4.0F, 12.0F, 4.0F,
                        new Dilation(0.0F)),
                ModelTransform.of(4.0F, 12.0F, -6.0F, 0.0F, 0.0F, -3.1416F));

        return TexturedModelData.of(meshdefinition, 64, 32);
    }


    @Override
    public void render(MatrixStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
       head.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        body.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        leg1.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        leg2.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        leg3.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        leg4.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }

    @Override
    public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        this.head.pitch = headPitch * 0.017453292F;
        this.head.yaw = headYaw * 0.017453292F;
        this.leg4.pitch = MathHelper.cos(limbAngle * 0.6662F) * 1.4F * limbDistance;
        this.leg3.pitch = MathHelper.cos(limbAngle * 0.6662F + 3.1415927F) * 1.4F * limbDistance;
        this.leg2.pitch = MathHelper.cos(limbAngle * 0.6662F + 3.1415927F) * 1.4F * limbDistance;
        this.leg1.pitch = MathHelper.cos(limbAngle * 0.6662F) * 1.4F * limbDistance;
    }
}
