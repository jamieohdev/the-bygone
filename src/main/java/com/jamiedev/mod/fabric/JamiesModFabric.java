package com.jamiedev.mod.fabric;

import com.jamiedev.mod.common.entities.*;
import com.jamiedev.mod.fabric.init.*;
import com.jamiedev.mod.fabric.init.JamiesModItemGroup;
import com.jamiedev.mod.mixin.SpawnRestrictMixin;
import com.jamiedev.mod.common.network.SyncPlayerHookS2C;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.SpawnLocationTypes;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.item.AnimalArmorItem;
import net.minecraft.item.HoeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.jamiedev.mod.fabric.init.JamiesModEntityTypes.*;

public class JamiesModFabric implements ModInitializer {
	public static String MOD_ID = "bygone";

	public static AnimalArmorItem.Type BIG_BEAK_ARMOR;

	@Override
	public void onInitialize() {
		initEvents();

		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		SpawnRestriction.register(SCUTTLE, SpawnLocationTypes.IN_WATER, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, ScuttleEntity::canSpawn);
		SpawnRestriction.register(GLARE, SpawnLocationTypes.ON_GROUND, Heightmap.Type.MOTION_BLOCKING, GlareEntity::canSpawn);
		SpawnRestriction.register(BIG_BEAK, SpawnLocationTypes.ON_GROUND, Heightmap.Type.MOTION_BLOCKING, BigBeakEntity::canSpawn);
		SpawnRestriction.register(TRILOBITE, SpawnLocationTypes.IN_WATER, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, TrilobiteEntity::canSpawn);
		SpawnRestriction.register(COPPERBUG, SpawnLocationTypes.ON_GROUND, Heightmap.Type.MOTION_BLOCKING, CopperbugEntity::canSpawn);
		//SpawnRestriction.register(COPPERBUG, SpawnLocationTypes.IN_WATER, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, CopperbugEntity::canSpawn);

		SpawnRestrictMixin.callRegister(COELACANTH, SpawnLocationTypes.IN_WATER, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, CoelacanthEntity::canSpawn);
		JamiesModBlocks.init();
		JamiesModBlockEntities.init();
		JamiesModItems.init();
		JamiesModEntityTypes.init();
		JamiesModEntityTypes.postInit();
		JamiesModBiomes.init();
		JamiesModItemGroup.registerItemgroups();
		JamiesModFeatures.init();
		JamiesModStructures.init();
		JamiesModParticleTypes.init();
		JamiesModPortals.init();
		JamiesModSoundEvents.init();
		JamiesModMisc.init();
		JamiesModCriteria.init();
		JamiesModTradeOffers.init();

		JamiesModFabric.LOGGER.info("Registering Entities for " + JamiesModFabric.MOD_ID);

		PayloadTypeRegistry.playS2C().register(SyncPlayerHookS2C.PACkET_ID, SyncPlayerHookS2C.CODEC);
	}

	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	public static Identifier getModId(String id){
		return Identifier.of(MOD_ID, id);
	}



	public static void initEvents()
	{

		UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
			BlockPos pos = hitResult.getBlockPos();
			BlockState state = world.getBlockState(pos);
			ItemStack stack = player.getStackInHand(hand);


			if (stack.getItem() instanceof HoeItem && (state.isOf(JamiesModBlocks.CLAYSTONE) || state.isOf(JamiesModBlocks.MOSSY_CLAYSTONE))) {
				BlockPos blockAbovePos = pos.up();
				BlockState blockAboveState = world.getBlockState(blockAbovePos);
				if (blockAboveState.isAir()) {
					world.playSound(null, pos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
					world.setBlockState(pos, JamiesModBlocks.CLAYSTONE_FARMLAND.getDefaultState(), Block.NOTIFY_LISTENERS);

					if (!player.isCreative()) {
						stack.damage(1, player,  player.getPreferredEquipmentSlot(player.getActiveItem()));

					}

					return ActionResult.SUCCESS;
				}
			}
			if (stack.getItem() instanceof HoeItem && (state.isOf(JamiesModBlocks.COARSE_CLAYSTONE))) {
				BlockPos blockAbovePos = pos.up();
				BlockState blockAboveState = world.getBlockState(blockAbovePos);
				if (blockAboveState.isAir()) {
					world.playSound(null, pos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
					world.setBlockState(pos, JamiesModBlocks.CLAYSTONE.getDefaultState(), Block.NOTIFY_LISTENERS);

					if (!player.isCreative()) {
						stack.damage(1, player,  player.getPreferredEquipmentSlot(player.getActiveItem()));

					}

					return ActionResult.SUCCESS;
				}
			}
			return ActionResult.PASS;
		});
	}
}
