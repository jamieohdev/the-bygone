package com.jamiedev.mod.common.client;


import com.jamiedev.mod.fabric.JamiesModFabric;
import net.minecraft.client.render.entity.model.EntityModelLayer;

public class JamiesModModelLayers {
    public static final EntityModelLayer DUCKIE =
            new EntityModelLayer(JamiesModFabric.getModId("duckie"), "main");
    public static final EntityModelLayer HOOK =
            new EntityModelLayer(JamiesModFabric.getModId("hook"), "main");

    public static final EntityModelLayer JAWS =
            new EntityModelLayer(JamiesModFabric.getModId("jaws"), "main");

    public static final EntityModelLayer MOOBOO  =
            new EntityModelLayer(JamiesModFabric.getModId("mooboo"), "main");

    public static final EntityModelLayer MOOBOO_TRANS  =
            new EntityModelLayer(JamiesModFabric.getModId("mooboo"), "outer");
    public static final EntityModelLayer SCUTTLE =
            new EntityModelLayer(JamiesModFabric.getModId("scuttle"), "main");

    public static final EntityModelLayer SCUTTLE_SPIKE =
            new EntityModelLayer(JamiesModFabric.getModId("scuttle_spike"), "main");
    public static final EntityModelLayer COELACANTH =
            new EntityModelLayer(JamiesModFabric.getModId("coelacanth"), "main");
    public static final EntityModelLayer TRILOBITE =
            new EntityModelLayer(JamiesModFabric.getModId("trilobite"), "main");
    public static final EntityModelLayer GLARE =
            new EntityModelLayer(JamiesModFabric.getModId("glare"), "main");

    public static final EntityModelLayer EXOTIC_ARROW =
            new EntityModelLayer(JamiesModFabric.getModId("exotic_arrow"), "main");

    public static final EntityModelLayer BIG_BEAK = new EntityModelLayer(JamiesModFabric.getModId("big_beak"), "main");
    public static final EntityModelLayer BIG_BEAK_SADDLE =  new EntityModelLayer(JamiesModFabric.getModId("big_beak"), "saddle");

    public static final EntityModelLayer BIG_BEAK_ARMOR =  new EntityModelLayer(JamiesModFabric.getModId("big_beak"), "armor");

}
