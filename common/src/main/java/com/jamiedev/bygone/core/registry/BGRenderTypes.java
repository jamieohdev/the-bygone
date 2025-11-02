package com.jamiedev.bygone.core.registry;

import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderType;

public class BGRenderTypes extends RenderType {
    private static RenderType BYGONE_PORTAL;

    static {

    }

    public BGRenderTypes(String name, VertexFormat format, VertexFormat.Mode mode, int bufferSize, boolean affectsCrumbling, boolean sortOnUpload, Runnable setupState, Runnable clearState) {
        super(name, format, mode, bufferSize, affectsCrumbling, sortOnUpload, setupState, clearState);
    }

    public static RenderType endPortal() {
        return BYGONE_PORTAL;
    }
}
