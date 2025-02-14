package com.jamiedev.bygone;

import com.jamiedev.bygone.init.*;
import com.jamiedev.bygone.network.PacketHandler;
import com.jamiedev.bygone.network.SyncPlayerHookS2C;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.saveddata.maps.MapDecorationTypes;

public class BygoneFabric implements ModInitializer {


	@Override
	public void onInitialize() {
		initEvents();

		Bygone.registerBuiltIn();

		Bygone.registerSpawnPlacements(SpawnPlacements::register);
		JamiesModPortalsFabric.init();

		BygoneFabric.addCompostables();
		BygoneFabric.addFuels();

		JamiesModCriteria.init();

		TradeOfferHelper.registerVillagerOffers(VillagerProfession.CARTOGRAPHER, 2, //10 1 12 10
				factories -> factories.add(new VillagerTrades.TreasureMapForEmeralds(10, JamiesModTag.ON_BYGONE_PORTAL_MAPS, "Bygone Portal Map", MapDecorationTypes.GREEN_BANNER, 12, 10)));
		TradeOfferHelper.registerWanderingTraderOffers(1, factories -> {
			factories.add(new VillagerTrades.ItemsForEmeralds(JamiesModBlocks.ALPHA_MOSS_BLOCK, 2, 1, 6, 8));
			factories.add(new VillagerTrades.TreasureMapForEmeralds(10, JamiesModTag.ON_BYGONE_PORTAL_MAPS, "Bygone Portal Map", MapDecorationTypes.GREEN_BANNER, 12, 10));
		});

		initAttributes();


		Bygone.addValidBlocks((type, block) -> type.addSupportedBlock(block));

		Bygone.registerStrippables();
        Bygone.LOGGER.info("Registering Entities for {}", Bygone.MOD_ID);

		PacketHandler.registerPackets();

	}

	public static void initAttributes() {
		Bygone.initAttributes(FabricDefaultAttributeRegistry::register);
	}

	public static void addCompostables() {
		CompostingChanceRegistry compostingRegistry = CompostingChanceRegistry.INSTANCE;
		float LEAVES_CHANCE = compostingRegistry.get(Items.OAK_LEAVES);

		compostingRegistry.add(JamiesModBlocks.ANCIENT_SAPLING, 0.5F);
		compostingRegistry.add(JamiesModBlocks.CAVE_VINES_PLANT, 0.3F);
		compostingRegistry.add(JamiesModBlocks.CAVE_VINES, 0.3F);
		compostingRegistry.add(JamiesModBlocks.CHARNIA, 0.2F);
		compostingRegistry.add(JamiesModBlocks.MONTSECHIA, 0.2F);
		compostingRegistry.add(JamiesModBlocks.RAFFLESIA, 0.8F);
		compostingRegistry.add(JamiesModBlocks.SAGARIA, 0.2F);
		compostingRegistry.add(JamiesModBlocks.SHORT_GRASS, 0.2F);
		compostingRegistry.add(JamiesModBlocks.TALL_GRASS, 0.4F);
		compostingRegistry.add(JamiesModBlocks.ANCIENT_ROOTS, 0.3F);
		compostingRegistry.add(JamiesModBlocks.ANCIENT_VINE, 0.3F);
		compostingRegistry.add(JamiesModBlocks.ANCIENT_LEAVES, 0.3F);

		compostingRegistry.add(JamiesModBlocks.ALPHA_MOSS_CARPET, 0.3F);
		compostingRegistry.add(JamiesModBlocks.ALPHA_MOSS_BLOCK, 0.3F);

		compostingRegistry.add(JamiesModBlocks.CREOSOTE_SPROUTS, 0.2F);
		compostingRegistry.add(JamiesModBlocks.SHELF_MOLD, 0.2F);
		compostingRegistry.add(JamiesModBlocks.SHELF_ROOTS, 0.2F);
		compostingRegistry.add(JamiesModBlocks.SHELF_FUNGUS, 0.2F);
		compostingRegistry.add(JamiesModBlocks.SHELF_SPROUTS, 0.2F);

		compostingRegistry.add(JamiesModBlocks.ORANGE_FUNGI_VINES, 0.1F);
		compostingRegistry.add(JamiesModBlocks.PINK_FUNGI_VINES, 0.1F);
		compostingRegistry.add(JamiesModBlocks.PURPLE_FUNGI_VINES, 0.1F);
		compostingRegistry.add(JamiesModBlocks.BELLADONNA,0.1F);
		compostingRegistry.add(JamiesModBlocks.COLEUS, 0.1F);
	}

	public static void addFuels() {
		FuelRegistry fuelRegistry = FuelRegistry.INSTANCE;
		final FlammableBlockRegistry flammableBlockRegistry = FlammableBlockRegistry.getDefaultInstance();

		fuelRegistry.add(JamiesModBlocks.ANCIENT_SAPLING, 300);
		fuelRegistry.add(JamiesModBlocks.CAVE_VINES_PLANT, 200);
		fuelRegistry.add(JamiesModBlocks.CAVE_VINES, 200);
		fuelRegistry.add(JamiesModBlocks.CHARNIA, 50);
		fuelRegistry.add(JamiesModBlocks.MONTSECHIA, 50);
		fuelRegistry.add(JamiesModBlocks.RAFFLESIA, 500);
		fuelRegistry.add(JamiesModBlocks.SAGARIA, 50);
		fuelRegistry.add(JamiesModBlocks.SHORT_GRASS, 300);
		fuelRegistry.add(JamiesModBlocks.TALL_GRASS, 200);
		fuelRegistry.add(JamiesModBlocks.ANCIENT_ROOTS, 50);
		fuelRegistry.add(JamiesModBlocks.ALPHA_MOSS_CARPET, 30);
		fuelRegistry.add(JamiesModBlocks.ALPHA_MOSS_BLOCK, 60);
		fuelRegistry.add(JamiesModBlocks.BELLADONNA, 50);
		fuelRegistry.add(JamiesModBlocks.COLEUS, 50);

		fuelRegistry.add(JamiesModBlocks.ANCIENT_WOOD, 300);
		fuelRegistry.add(JamiesModBlocks.ANCIENT_LOG, 300);
		fuelRegistry.add(JamiesModBlocks.ANCIENT_PLANKS, 300);
		fuelRegistry.add(JamiesModBlocks.ANCIENT_SLAB, 150);
		fuelRegistry.add(JamiesModBlocks.ANCIENT_STAIRS, 300);
		fuelRegistry.add(JamiesModBlocks.ANCIENT_PRESSURE_PLATE, 300);
		fuelRegistry.add(JamiesModBlocks.ANCIENT_BUTTON, 100);
		fuelRegistry.add(JamiesModBlocks.ANCIENT_TRAPDOOR, 300);
		fuelRegistry.add(JamiesModBlocks.ANCIENT_FENCE_GATE, 300);
		fuelRegistry.add(JamiesModBlocks.ANCIENT_FENCE, 300);
		fuelRegistry.add(JamiesModBlocks.ANCIENT_DOOR, 200);

		flammableBlockRegistry.add(JamiesModBlocks.ANCIENT_WOOD, 5, 20);
		flammableBlockRegistry.add(JamiesModBlocks.ANCIENT_LOG, 5, 20);
		flammableBlockRegistry.add(JamiesModBlocks.ANCIENT_PLANKS, 5, 20);
		flammableBlockRegistry.add(JamiesModBlocks.ANCIENT_SLAB, 5, 20);
		flammableBlockRegistry.add(JamiesModBlocks.ANCIENT_STAIRS, 5, 20);
		flammableBlockRegistry.add(JamiesModBlocks.ANCIENT_PRESSURE_PLATE, 5, 20);
		flammableBlockRegistry.add(JamiesModBlocks.ANCIENT_BUTTON, 5, 20);
		flammableBlockRegistry.add(JamiesModBlocks.ANCIENT_TRAPDOOR, 5, 20);
		flammableBlockRegistry.add(JamiesModBlocks.ANCIENT_FENCE_GATE, 5, 20);
		flammableBlockRegistry.add(JamiesModBlocks.ANCIENT_FENCE, 5, 20);
		flammableBlockRegistry.add(JamiesModBlocks.ANCIENT_DOOR, 5, 20);

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
