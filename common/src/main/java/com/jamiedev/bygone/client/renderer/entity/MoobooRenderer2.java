package com.jamiedev.bygone.client.renderer.entity;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.client.JamiesModModelLayers;
import com.jamiedev.bygone.common.entity.MoobooEntity;
import com.jamiedev.bygone.common.entity.MoobooEntity2;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.model.SlimeModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.entity.layers.SlimeOuterLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.monster.Vex;

import javax.annotation.Nullable;

public class MoobooRenderer2 extends MobRenderer<MoobooEntity2, CowModel<MoobooEntity2>> {
    private static final ResourceLocation COW_LOCATION = Bygone.id("textures/entity/mooboo.png");

    SlimeRenderer ref;

    public MoobooRenderer2(EntityRendererProvider.Context context) {
        super(context, new CowModel(context.bakeLayer(JamiesModModelLayers.MOOBOO)), 0.7F);

    }

    @Override
    public ResourceLocation getTextureLocation(MoobooEntity2 moobooEntity2) {
        return COW_LOCATION;
    }


    protected int getBlockLight(Vex vexEntity, BlockPos blockPos) {
        return 15;
    }

    @Nullable
    @Override
    protected RenderType getRenderType(MoobooEntity2 p_230496_1_, boolean p_230496_2_, boolean p_230496_3_, boolean p_230496_4_) {
        return RenderType.entityTranslucent(this.getTextureLocation(p_230496_1_));
    }
}
