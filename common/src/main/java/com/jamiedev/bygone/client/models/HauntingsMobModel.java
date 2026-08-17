package com.jamiedev.bygone.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.Entity;

// thank you tazer :O
// https://github.com/Alchemists-Of-Yore/No-Mans-Land/blob/1.21.1/src/main/java/com/farcr/nomansland/client/model/BuddyModel.java
public abstract class HauntingsMobModel<E extends Entity> extends HierarchicalModel<E> {
    private float fadeAlpha = 1.0f;
    public void setFadeAlpha(float newAlpha) {
        this.fadeAlpha = newAlpha;
    }

    public int modifyColor(int originalColor) {
        return FastColor.ARGB32.color(
            (int) (fadeAlpha * 255),
            FastColor.ARGB32.red(originalColor),
            FastColor.ARGB32.green(originalColor),
            FastColor.ARGB32.blue(originalColor)
        );
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int originalColor) {
        super.renderToBuffer(poseStack, buffer, packedLight, packedOverlay, this.modifyColor(originalColor));
    }
}
