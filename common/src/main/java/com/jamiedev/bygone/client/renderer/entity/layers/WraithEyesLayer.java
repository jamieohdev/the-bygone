package com.jamiedev.bygone.client.renderer.entity.layers;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.client.models.WraithModel;
import com.jamiedev.bygone.common.entity.WraithEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.world.entity.Entity;

public class WraithEyesLayer<T extends Entity, M extends WraithModel<T>> extends EyesLayer<T, M> {
    private static final RenderType WRAITH_EYES = RenderType.eyes(Bygone.id("textures/entity/wraith_outer.png"));

    public WraithEyesLayer(RenderLayerParent<T, M> p_117507_) {
        super(p_117507_);
    }

    public RenderType renderType() {
        return WRAITH_EYES;
    }
}
