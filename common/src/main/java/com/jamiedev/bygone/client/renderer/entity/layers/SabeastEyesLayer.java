package com.jamiedev.bygone.client.renderer.entity.layers;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.client.models.SabeastModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.world.entity.Entity;

public class SabeastEyesLayer<T extends Entity, M extends SabeastModel<T>> extends EyesLayer<T, M> {
    private static final RenderType WRAITH_EYES = RenderType.eyes(Bygone.id("textures/entity/sabeast_outer.png"));

    public SabeastEyesLayer(RenderLayerParent<T, M> p_117507_) {
        super(p_117507_);
    }

    public RenderType renderType() {
        return WRAITH_EYES;
    }
}
