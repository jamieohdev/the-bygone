package com.jamiedev.bygone.client;

import com.jamiedev.bygone.Bygone;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import org.jetbrains.annotations.NotNull;

public class LithoClientExtensions implements IClientFluidTypeExtensions {

    private static final ResourceLocation LITHO_STILL = Bygone.id("block/litho_still");

    private static final ResourceLocation LITHO_FLOW =  Bygone.id("block/litho_flow");

    @Override
    public @NotNull ResourceLocation getStillTexture() {
        return LITHO_STILL;
    }

    @Override
    public @NotNull ResourceLocation getFlowingTexture() {
        return LITHO_FLOW;
    }
}