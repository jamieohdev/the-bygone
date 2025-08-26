package com.jamiedev.bygone;

import com.jamiedev.bygone.core.init.JamiesModTag;
import com.jamiedev.bygone.core.registry.BGBlocks;
import com.jamiedev.bygone.core.registry.BGCriteria;
import com.jamiedev.bygone.core.registry.BGMobEffectsFabric;
import com.jamiedev.bygone.core.registry.BGDataComponentsFabric;
import com.jamiedev.bygone.core.network.PacketHandler;
import com.jamiedev.bygone.common.util.VexDeathTracker;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.saveddata.maps.MapDecorationTypes;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.server.level.ServerLevel;

public class BygoneFabric implements ModInitializer {


	@Override
	public void onInitialize() {
		initEvents();
		BGMobEffectsFabric.init();
		BGDataComponentsFabric.init();
		Bygone.init();

		Bygone.registerBuiltIn();

		Bygone.registerSpawnPlacements(SpawnPlacements::register);
		JamiesModPortalsFabric.init();

		BGCriteria.init();

		TradeOfferHelper.registerVillagerOffers(VillagerProfession.CARTOGRAPHER, 2, //10 1 12 10
				factories -> factories.add(new VillagerTrades.TreasureMapForEmeralds(10, JamiesModTag.ON_BYGONE_PORTAL_MAPS, "Bygone Portal Map", MapDecorationTypes.GREEN_BANNER, 12, 10)));
		TradeOfferHelper.registerWanderingTraderOffers(1, factories -> {
			factories.add(new VillagerTrades.ItemsForEmeralds(BGBlocks.ALPHA_MOSS_BLOCK.get(), 2, 1, 6, 8));
			factories.add(new VillagerTrades.TreasureMapForEmeralds(10, JamiesModTag.ON_BYGONE_PORTAL_MAPS, "Bygone Portal Map", MapDecorationTypes.GREEN_BANNER, 12, 10));
		});

		Bygone.initAttributes(FabricDefaultAttributeRegistry::register);


		Bygone.addValidBlocks((type, block) -> type.addSupportedBlock(block));

		Bygone.registerStrippables();
		Bygone.addFlammable();
        Bygone.LOGGER.info("Registering Entities for {}", Bygone.MOD_ID);

		PacketHandler.registerPackets();

		ServerLivingEntityEvents.AFTER_DEATH.register((entity, damageSource) -> {
			Bygone.LOGGER.info("Entity died: {} (type: {})", entity.getClass().getSimpleName(), entity.getType());
			if (entity instanceof Vex vex && entity.level() instanceof ServerLevel serverLevel) {
				VexDeathTracker.onVexDeath(vex, serverLevel);
			}
		});

	}

	public static void initEvents()
	{

		UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
			BlockPos pos = hitResult.getBlockPos();
			BlockState state = world.getBlockState(pos);
			ItemStack stack = player.getItemInHand(hand);


			if (stack.getItem() instanceof HoeItem && (state.is(BGBlocks.CLAYSTONE.get()) || state.is(BGBlocks.MOSSY_CLAYSTONE.get()))) {
				BlockPos blockAbovePos = pos.above();
				BlockState blockAboveState = world.getBlockState(blockAbovePos);
				if (blockAboveState.isAir()) {
					world.playSound(null, pos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 1.0F);
					world.setBlock(pos, BGBlocks.CLAYSTONE_FARMLAND.get().defaultBlockState(), Block.UPDATE_CLIENTS);

					if (!player.isCreative()) {
						stack.hurtAndBreak(1, player,  player.getEquipmentSlotForItem(player.getUseItem()));

					}

					return InteractionResult.SUCCESS;
				}
			}
			if (stack.getItem() instanceof HoeItem && (state.is(BGBlocks.COARSE_CLAYSTONE.get()))) {
				BlockPos blockAbovePos = pos.above();
				BlockState blockAboveState = world.getBlockState(blockAbovePos);
				if (blockAboveState.isAir()) {
					world.playSound(null, pos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 1.0F);
					world.setBlock(pos, BGBlocks.CLAYSTONE.get().defaultBlockState(), Block.UPDATE_CLIENTS);

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
