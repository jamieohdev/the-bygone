package com.jamiedev.bygone.client.models;

import com.jamiedev.bygone.common.entity.ScuttleEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.GuardianModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

// Made with Blockbench 4.11.1
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports
public class ScuttleModel extends HierarchicalModel<ScuttleEntity> {
    private static final String EYE = "eye";
    final ModelPart[] tail;
    private final ModelPart eye;
    private final ModelPart body;
    private final ModelPart tail3;
    private final ModelPart knub1;
    private final ModelPart knub2;
    private final ModelPart knub3;
    private final ModelPart knub4;
    private final ModelPart spine1;
    private final ModelPart spine2;
    private final ModelPart spine3;
    private final ModelPart spine4;
    private final ModelPart root;
    GuardianModel ref;

    public ScuttleModel(ModelPart root) {
        this.root = root;
        this.body = root.getChild("body");
        this.tail3 = this.body.getChild("tail3");
        this.knub4 = this.body.getChild("knub4");
        this.knub3 = this.body.getChild("knub3");
        this.knub2 = this.body.getChild("knub2");
        this.knub1 = this.body.getChild("knub1");
        this.eye = this.body.getChild("eye");

        this.tail = new ModelPart[2];


        this.spine1 = this.body.getChild("spine1");
        this.spine2 = this.body.getChild("spine2");
        this.spine3 = this.body.getChild("spine3");
        this.spine4 = this.body.getChild("spine4");
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();

        PartDefinition body = modelPartData.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -6.0F, -8.0F, 12.0F, 4.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 20.0F, 0.0F));
        body.addOrReplaceChild("eye", CubeListBuilder.create().texOffs(32, 21).addBox(-1.0F, 15.5F, 0.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -19.5F, -8.25F));

        PartDefinition tail3 = body.addOrReplaceChild("tail3", CubeListBuilder.create(), PartPose.offset(0.0F, -5.0F, 0.0F));

        PartDefinition tail3_r1 = tail3.addOrReplaceChild("tail3_r1", CubeListBuilder.create().texOffs(0, 16).addBox(0.0F, -2.5F, 7.0F, 1.0F, 7.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(24, 24).addBox(-1.0F, 0.0F, 4.0F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0436F, 0.0F));

        PartDefinition knub4 = body.addOrReplaceChild("knub4", CubeListBuilder.create(), PartPose.offset(6.0F, 4.0F, 7.0F));

        PartDefinition knub4_r1 = knub4.addOrReplaceChild("knub4_r1", CubeListBuilder.create().texOffs(32, 16).addBox(-1.0F, -3.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, -4.0F, -5.0F, 0.2618F, 0.0F, 0.0F));

        PartDefinition knub3 = body.addOrReplaceChild("knub3", CubeListBuilder.create(), PartPose.offset(0.0F, 4.0F, 7.0F));

        PartDefinition knub3_r1 = knub3.addOrReplaceChild("knub3_r1", CubeListBuilder.create().texOffs(16, 32).addBox(-1.0F, -3.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, -4.0F, -5.0F, 0.2618F, 0.0F, 0.0F));

        PartDefinition knub2 = body.addOrReplaceChild("knub2", CubeListBuilder.create(), PartPose.offset(6.0F, 4.0F, 0.0F));

        PartDefinition knub2_r1 = knub2.addOrReplaceChild("knub2_r1", CubeListBuilder.create().texOffs(8, 30).addBox(-1.0F, -3.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, -4.0F, -5.0F, 0.2618F, 0.0F, 0.0F));

        PartDefinition knub1 = body.addOrReplaceChild("knub1", CubeListBuilder.create(), PartPose.offset(0.0F, 4.0F, 0.0F));

        PartDefinition knub1_r1 = knub1.addOrReplaceChild("knub1_r1", CubeListBuilder.create().texOffs(0, 30).addBox(-1.0F, -3.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, -4.0F, -5.0F, 0.2618F, 0.0F, 0.0F));

        PartDefinition spine1 = body.addOrReplaceChild("spine1", CubeListBuilder.create(), PartPose.offset(0.0F, 4.0F, 0.0F));

        PartDefinition spine1_r1 = spine1.addOrReplaceChild("spine1_r1", CubeListBuilder.create().texOffs(24, 16).addBox(-1.0F, -6.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, -10.0F, -4.0F, -0.2618F, 0.0F, 0.0F));

        PartDefinition spine2 = body.addOrReplaceChild("spine2", CubeListBuilder.create(), PartPose.offset(6.0F, 4.0F, 1.0F));

        PartDefinition spine1_r2 = spine2.addOrReplaceChild("spine1_r2", CubeListBuilder.create().texOffs(24, 29).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, -10.0F, -6.0F, -0.2618F, 0.0F, 0.0F));

        PartDefinition spine3 = body.addOrReplaceChild("spine3", CubeListBuilder.create(), PartPose.offset(2.0F, 4.0F, 7.0F));

        PartDefinition spine1_r3 = spine3.addOrReplaceChild("spine1_r3", CubeListBuilder.create().texOffs(16, 16).addBox(-1.0F, -7.0F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, -10.0F, -6.0F, -0.2618F, 0.0F, 0.0F));

        PartDefinition spine4 = body.addOrReplaceChild("spine4", CubeListBuilder.create(), PartPose.offset(7.0F, 4.0F, 6.0F));

        PartDefinition spine1_r4 = spine4.addOrReplaceChild("spine1_r4", CubeListBuilder.create().texOffs(16, 25).addBox(-1.0F, -5.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, -10.0F, -6.0F, -0.2618F, 0.0F, 0.0F));
        return LayerDefinition.create(modelData, 64, 64);
    }

    private static float getAngle(int index, float animationProgress, float magnitude) {
        return 1.0F + Mth.cos(animationProgress * 1.5F + (float) index) * 0.01F - magnitude;
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(ScuttleEntity guardianEntity, float f, float g, float h, float i, float j) {
        float k = h - (float) guardianEntity.tickCount;
        this.body.yRot = i * 0.017453292F;
        this.body.xRot = j * 0.017453292F;

        Entity entity = Minecraft.getInstance().getCameraEntity();

        if (guardianEntity.hasProjTarget()) {
            entity = guardianEntity.getProjTarget();
        }

        if (entity != null) {
            Vec3 vec3d = entity.getEyePosition(0.0F);
            Vec3 vec3d2 = guardianEntity.getEyePosition(0.0F);
            double d = vec3d.y - vec3d2.y;
            if (d > 0.0) {
                this.eye.y = -19.5F;
            } else {
                this.eye.y = -20.5F;
            }

            Vec3 vec3d3 = guardianEntity.getViewVector(0.0F);
            vec3d3 = new Vec3(vec3d3.x, 0.0, vec3d3.z);
            Vec3 vec3d4 = (new Vec3(vec3d2.x - vec3d.x, 0.0, vec3d2.z - vec3d.z)).normalize().yRot(1.5707964F);
            double e = vec3d3.dot(vec3d4);
            this.eye.x = Mth.sqrt((float) Math.abs(e)) * 2.0F * (float) Math.signum(e);
        }

        this.eye.visible = true;
        float m = guardianEntity.getTailAngle(k);
        this.tail3.yRot = Mth.sin(m) * 3.1415927F * 0.15F;
    }

    @Override
    public void renderToBuffer(PoseStack matrices, VertexConsumer vertexConsumer, int light, int overlay, int color) {
        body.render(matrices, vertexConsumer, light, overlay, color);
        //tail3.render(matrices, vertexConsumer, light, overlay, color);

    }
}