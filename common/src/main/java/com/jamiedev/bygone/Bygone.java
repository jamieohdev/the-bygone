package com.jamiedev.bygone;

import com.google.common.collect.ImmutableMap;
import com.jamiedev.bygone.init.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.AxeItem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

// This class is part of the common project meaning it is shared between all supported loaders. Code written here can only
// import and access the vanilla codebase, libraries used by vanilla, and optionally third party libraries that provide
// common compatible binaries. This means common code can not directly use loader specific concepts such as Forge events
// however it will be compatible with all supported mod loaders.
public class Bygone {


    public static final String MOD_ID = "bygone";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static void init() {


    }

    public static void registerStrippables() {

        if (AxeItem.STRIPPABLES instanceof ImmutableMap) {
            AxeItem.STRIPPABLES = new HashMap<>(AxeItem.STRIPPABLES);
        }

        AxeItem.STRIPPABLES.put(JamiesModBlocks.ANCIENT_LOG, JamiesModBlocks.STRIPPED_ANCIENT_LOG);
        AxeItem.STRIPPABLES.put(JamiesModBlocks.ANCIENT_WOOD, JamiesModBlocks.STRIPPED_ANCIENT_WOOD);
    }

    public static ResourceLocation id(String id){
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, id);
    }

    public static void registerBuiltIn() {
        JamiesModBlocks.init();
        JamiesModBlockEntities.init();
        JamiesModItems.init();
        JamiesModEntityTypes.postInit();
        JamiesModBiomes.init();
        JamiesModItemGroup.registerItemgroups();
        JamiesModFeatures.init();
        JamiesModStructures.init();
        JamiesModParticleTypes.init();
        JamiesModSoundEvents.init();
    }

}