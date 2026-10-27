package com.jamiedev.bygone.core.registry;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.function.Function;

public class BGRenderTypes extends RenderType {
    private static RenderType BYGONE_PORTAL;
    private static final Function<ResourceLocation, RenderType> REAVER_BODY_DEPTH = Util.memoize(texture ->
            new BGRenderTypes("bygone_reaver_depth", List.of(
                    RENDERTYPE_ENTITY_CUTOUT_NO_CULL_SHADER,
                    new TextureStateShard(texture, false, false),
                    NO_TRANSPARENCY, LEQUAL_DEPTH_TEST, NO_CULL, LIGHTMAP, OVERLAY, DEPTH_WRITE), false));
    private static final Function<ResourceLocation, RenderType> REAVER_AFTERIMAGES = Util.memoize(texture ->
            new BGRenderTypes("bygone_reaver_afterimages", List.of(
                    RENDERTYPE_ENTITY_TRANSLUCENT_SHADER,
                    new TextureStateShard(texture, false, false),
                    TRANSLUCENT_TRANSPARENCY, LEQUAL_DEPTH_TEST, NO_CULL, LIGHTMAP, OVERLAY, COLOR_WRITE), true));

    public BGRenderTypes(String name, VertexFormat format, VertexFormat.Mode mode, int bufferSize, boolean affectsCrumbling, boolean sortOnUpload, Runnable setupState, Runnable clearState) {
        super(name, format, mode, bufferSize, affectsCrumbling, sortOnUpload, setupState, clearState);
    }

    private BGRenderTypes(String name, List<RenderStateShard> states, boolean sortOnUpload) {
        this(name, DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 1536, false, sortOnUpload,
                () -> states.forEach(RenderStateShard::setupRenderState),
                () -> states.forEach(RenderStateShard::clearRenderState));
    }

    public static RenderType reaverBodyDepth(ResourceLocation texture) {
        return REAVER_BODY_DEPTH.apply(texture);
    }

    public static RenderType reaverAfterimages(ResourceLocation texture) {
        return REAVER_AFTERIMAGES.apply(texture);
    }

    public static RenderType endPortal() {
        return BYGONE_PORTAL;
    }
}
