package com.jamiedev.bygone.fabric;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.common.entities.*;
import com.jamiedev.bygone.fabric.init.*;
import com.jamiedev.bygone.mixin.SpawnRestrictMixin;
import com.jamiedev.bygone.network.SyncPlayerHookS2C;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.item.AnimalArmorItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;

import static com.jamiedev.bygone.fabric.init.JamiesModEntityTypes.*;

public class JamiesModFabric implements ModInitializer {

	public static AnimalArmorItem.BodyType BIG_BEAK_ARMOR;

	@Override
	public void onInitialize() {
		initEvents();

		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		SpawnPlacements.register(SCUTTLE, SpawnPlacementTypes.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ScuttleEntity::checkSurfaceWaterAnimalSpawnRules);
		SpawnPlacements.register(GLARE, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, GlareEntity::canSpawn);
		SpawnPlacements.register(BIG_BEAK, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, BigBeakEntity::canSpawn);
		SpawnPlacements.register(TRILOBITE, SpawnPlacementTypes.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, TrilobiteEntity::checkSurfaceWaterAnimalSpawnRules);
		SpawnPlacements.register(COPPERBUG, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, CopperbugEntity::canSpawn);
		//SpawnRestriction.register(COPPERBUG, SpawnLocationTypes.IN_WATER, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, CopperbugEntity::canSpawn);

		SpawnRestrictMixin.callRegister(COELACANTH, SpawnPlacementTypes.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, CoelacanthEntity::checkSurfaceWaterAnimalSpawnRules);
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

		Bygone.LOGGER.info("Registering Entities for " + Bygone.MOD_ID);

		PayloadTypeRegistry.playS2C().register(SyncPlayerHookS2C.PACkET_ID, SyncPlayerHookS2C.CODEC);
	}


	public static void initEvents()
	{

		UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
			BlockPos pos = hitResult.getBlockPos();
			BlockState state = world.getBlockState(pos);
			ItemStack stack = player.getItemInHand(hand);


			if (stack.getItem() instanceof HoeItem && (state.is(JamiesModBlocks.CLAYSTONE) || state.is(JamiesModBlocks.MOSSY_CLAYSTONE))) {
				BlockPos blockAbovePos = pos.above();
				BlockState blockAboveState = world.getBlockState(blockAbovePos);
				if (blockAboveState.isAir()) {
					world.playSound(null, pos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 1.0F);
					world.setBlock(pos, JamiesModBlocks.CLAYSTONE_FARMLAND.defaultBlockState(), Block.UPDATE_CLIENTS);

					if (!player.isCreative()) {
						stack.hurtAndBreak(1, player,  player.getEquipmentSlotForItem(player.getUseItem()));

					}

					return InteractionResult.SUCCESS;
				}
			}
			if (stack.getItem() instanceof HoeItem && (state.is(JamiesModBlocks.COARSE_CLAYSTONE))) {
				BlockPos blockAbovePos = pos.above();
				BlockState blockAboveState = world.getBlockState(blockAbovePos);
				if (blockAboveState.isAir()) {
					world.playSound(null, pos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 1.0F);
					world.setBlock(pos, JamiesModBlocks.CLAYSTONE.defaultBlockState(), Block.UPDATE_CLIENTS);

					if (!player.isCreative()) {
						stack.hurtAndBreak(1, player,  player.getEquipmentSlotForItem(player.getUseItem()));

					}

					return InteractionResult.SUCCESS;
				}
			}
			return InteractionResult.PASS;
		});
	}
}
