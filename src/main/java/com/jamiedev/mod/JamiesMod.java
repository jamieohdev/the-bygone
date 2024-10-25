package com.jamiedev.mod;

import com.jamiedev.mod.entities.BigBeakEntity;
import com.jamiedev.mod.entities.DuckEntity;
import com.jamiedev.mod.init.*;
import com.jamiedev.mod.items.JamiesModItemGroup;
import com.jamiedev.mod.network.SyncPlayerHookS2C;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JamiesMod implements ModInitializer {
	public static String MOD_ID = "bygone";

	@Override
	public void onInitialize() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		JamiesModBlocks.init();
		JamiesModBlockEntities.init();
		JamiesModItems.init();
		JamiesModEntityTypes.init();
		JamiesModBiomes.init();
		JamiesModItemGroup.registerItemgroups();
		JamiesModFeatures.init();
		JamiesModStructures.init();
		JamiesModParticleTypes.init();
		JamiesModPortals.init();
		JamiesModSoundEvents.init();
		JamiesModMisc.init();
		JamiesModCriteria.init();

		JamiesMod.LOGGER.info("Registering Entities for " + JamiesMod.MOD_ID);

		PayloadTypeRegistry.playS2C().register(SyncPlayerHookS2C.PACkET_ID, SyncPlayerHookS2C.CODEC);

	}

	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	public static Identifier getModId(String id){
		return Identifier.of(MOD_ID, id);
	}

}

/**
 *
 *
 *
 * JAMIES MOD v7i643
 *
 * dimension
 *
 *
 */