package com.jamiedev.mod.common.client;

import com.jamiedev.mod.common.client.particles.*;
import com.jamiedev.mod.common.items.VerdigrisBladeItem;
import com.jamiedev.mod.fabric.JamiesModFabric;
import com.jamiedev.mod.common.blocks.JamiesModWoodType;
import com.jamiedev.mod.common.client.models.*;
import com.jamiedev.mod.common.client.renderer.*;
import com.jamiedev.mod.fabric.init.*;
import com.jamiedev.mod.common.network.SyncPlayerHookS2C;
import com.jamiedev.mod.common.client.network.SyncPlayerHookPacketHandler;
import com.jamiedev.mod.common.util.PlayerWithHook;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.DimensionRenderingRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.particle.SoulParticle;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.blockentity.HangingSignRenderer;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import java.util.Objects;
public class JamiesModClient implements ClientModInitializer {
    public static ResourceLocation BYGONE = JamiesModFabric.getModId("bygone");
    public  static DimensionSpecialEffects.SkyType BYGONE_SKY;

    public static boolean isWeaponBlocking(LivingEntity entity) {
        return entity.isUsingItem() && (canWeaponBlock(entity));
    }

    public static boolean canWeaponBlock(LivingEntity entity) {
        if ((entity.getMainHandItem().getItem() instanceof VerdigrisBladeItem)) {
            Item weaponItem = entity.getOffhandItem().getItem() instanceof VerdigrisBladeItem ? entity.getMainHandItem().getItem() : entity.getOffhandItem().getItem();
            return weaponItem instanceof VerdigrisBladeItem;
        }
       return false;
    }
    public static boolean isBlockingOnViaVersion(LivingEntity entity) {
        Item item = entity.getMainHandItem().getItem() instanceof VerdigrisBladeItem ? entity.getMainHandItem().getItem() : entity.getOffhandItem().getItem();
        return item instanceof VerdigrisBladeItem && item.components() != null && item.components().has(DataComponents.FOOD) && Objects.requireNonNull(item.components().get(DataComponents.FOOD)).eatSeconds() == 3600;
    }
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.AMBER, RenderType.translucent());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.CLOUD, RenderType.translucent());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.BYGONE_PORTAL, RenderType.translucent());

        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.ANCIENT_LEAVES, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.ANCIENT_ROOTS, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.ANCIENT_SAPLING, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.ANCIENT_VINE, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.CHARNIA, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.GOURD_LANTERN_BEIGE, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.GOURD_LANTERN_MUAVE, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.GOURD_LANTERN_VERDANT, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.GOURD_DANGO, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.GOURD_DANGO_WALL, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.GOURD_VINE, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.BIG_WHIRLIWEED, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.WHIRLIWEED, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.RAFFLESIA, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.CAVE_VINES, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.CAVE_VINES_PLANT, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.MONTSECHIA, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.SAGARIA, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.ANCIENT_DOOR, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.ANCIENT_TRAPDOOR, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.POTTED_MONTSECHIA, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.POTTED_SAGARIA, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.SHORT_GRASS, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.TALL_GRASS, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.BLUE_ALGAE, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.MALACHITE_DOOR, RenderType.cutout());

        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.BLEMISH_VEIN, RenderType.cutoutMipped());

        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.CRINOID, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.PRIMORDIAL_URCHIN, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.PRIMORDIAL_VENT, RenderType.cutout());

        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.DEAD_ORANGE_CORAL, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.DEAD_ORANGE_CORAL_WALL_FAN, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.DEAD_ORANGE_CORAL_FAN, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.ORANGE_CORAL, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.ORANGE_CORAL_FAN, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.ORANGE_CORAL_WALL_FAN, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.DEAD_BLUE_CORAL, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.DEAD_BLUE_CORAL_WALL_FAN, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.DEAD_BLUE_CORAL_FAN, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.BLUE_CORAL, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.BLUE_CORAL_FAN, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.BLUE_CORAL_WALL_FAN, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.BELLADONNA, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.COLEUS, RenderType.cutout());

        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.SHELF_FUNGUS, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.SHELF_ROOTS, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.SHELF_SPROUTS, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.SHELF_MOLD, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.ORANGE_FUNGI_VINES, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.ORANGE_FUNGI_PLANT, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.PINK_FUNGI_VINES, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.PINK_FUNGI_VINES_PLANT, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.PURPLE_FUNGI_VINES, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.PURPLE_FUNGI_VINES_PLANT, RenderType.cutout());

        // BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.ORANGE_FUNGI_FAN, RenderLayer.getCutout());
        //BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.ORANGE_FUNGI_WALL_FAN, RenderLayer.getCutout());
       // BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.PINK_FUNGI_FAN, RenderLayer.getCutout());
       // BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.PINK_FUNGI_WALL_FAN, RenderLayer.getCutout());
       // BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.PURPLE_FUNGI_FAN, RenderLayer.getCutout());
       // BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.PURPLE_FUNGI_WALL_FAN, RenderLayer.getCutout());

        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.CLAYSTONE_FARMLAND, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.AMARANTH_CROP, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.SPRINKER, RenderType.cutout());

        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.POINTED_AMBER, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.CREOSOTE, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.CREOSOTE_SPROUTS, RenderType.cutout());

        EntityRendererRegistry.register(JamiesModEntityTypes.COELACANTH, CoelacanthRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(JamiesModModelLayers.COELACANTH, CoelacanthModel::getTexturedModelData);

        EntityRendererRegistry.register(JamiesModEntityTypes.DUCK, DuckieRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(JamiesModModelLayers.DUCKIE, DuckieModel::getTexturedModelData);

        EntityRendererRegistry.register(JamiesModEntityTypes.GLARE, GlareRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(JamiesModModelLayers.GLARE, GlareModel::getTexturedModelData);


        EntityRendererRegistry.register(JamiesModEntityTypes.SCUTTLE, ScuttleRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(JamiesModModelLayers.SCUTTLE, ScuttleModel::getTexturedModelData);

        EntityRendererRegistry.register(JamiesModEntityTypes.COPPERBUG, CopperbugRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(JamiesModModelLayers.COPPERBUG, CopperbugModel::getTexturedModelData);

        EntityRendererRegistry.register(JamiesModEntityTypes.HOOK, HookRenderer::new);
        EntityRendererRegistry.register(JamiesModEntityTypes.EXOTIC_ARROW, ExoticArrowRenderer::new);

        EntityModelLayerRegistry.registerModelLayer(JamiesModModelLayers.SCUTTLE_SPIKE, ScuttleSpikeModel::getTexturedModelData);
        EntityRendererRegistry.register(JamiesModEntityTypes.SCUTTLE_SPIKE, ScuttleSpikeRenderer::new);

        EntityRendererRegistry.register(JamiesModEntityTypes.TRILOBITE, TrilobiteRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(JamiesModModelLayers.TRILOBITE, TrilobiteModel::getTexturedModelData);

        EntityRendererRegistry.register(JamiesModEntityTypes.MOOBOO, MoobooRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(JamiesModModelLayers.MOOBOO, MoobooModel::getTexturedModelData);

        EntityRendererRegistry.register(JamiesModEntityTypes.FUNGAL_PARENT, FungalParentRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(JamiesModModelLayers.FUNGALPARENT, FungalParentModel::getTexturedModelData);

        EntityRendererRegistry.register(JamiesModEntityTypes.BYGONE_ITEM, (context) -> {
            return new ThrownItemRenderer<>(context, 1.0F, true);
        });

        EntityRendererRegistry.register(JamiesModEntityTypes.BIG_BEAK, BigBeakRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(JamiesModModelLayers.BIG_BEAK, BigBeakModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(JamiesModModelLayers.BIG_BEAK_SADDLE, BigBeakModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(JamiesModModelLayers.BIG_BEAK_ARMOR, BigBeakModel::getTexturedModelData);

        ParticleFactoryRegistry.getInstance().register(JamiesModParticleTypes.BLEMISH, BlemishParticle.BlemishBlockProvider::new);
        ParticleFactoryRegistry.getInstance().register(JamiesModParticleTypes.RAFFLESIA_SPORES, RafflesiaSporeParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(JamiesModParticleTypes.ALGAE_BLOOM, SoulParticle.EmissiveProvider::new);
        ParticleFactoryRegistry.getInstance().register(JamiesModParticleTypes.SHELF, ShelfParticle.Factory::new);

        ParticleFactoryRegistry.getInstance().register(JamiesModParticleTypes.AMBER_DUST, AmberDustParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(JamiesModParticleTypes.ANCIENT_LEAVES, AncientLeavesParticle.Factory::new);

        DimensionRenderingRegistry.registerDimensionEffects(BYGONE, BygoneDimensionEffects.INSTANCE);
        DimensionRenderingRegistry.registerSkyRenderer(JamiesModDimension.BYGONE_LEVEL_KEY, BygoneSkyRenderer.INSTANCE);

        registerModelPredicateProviders();

        ClientPlayNetworking.registerGlobalReceiver(SyncPlayerHookS2C.PACkET_ID, (payload, context) -> {
            context.client().execute(() -> SyncPlayerHookPacketHandler.handle(payload, context));
        });

        Sheets.SIGN_MATERIALS.put(JamiesModWoodType.ANCIENT, Sheets.getSignMaterial(JamiesModWoodType.ANCIENT));
        BlockEntityRenderers.register(JamiesModBlockEntities.MOD_SIGN_BLOCK_ENTITY, SignRenderer::new);
        BlockEntityRenderers.register(JamiesModBlockEntities.MOD_HANGING_SIGN_BLOCK_ENTITY, HangingSignRenderer::new);
     //   BlockEntityRendererFactories.register(JamiesModBlockEntities.BRUSHABLE_BLOCK, BygoneBrushableBlockEntityRenderer::new);
        BlockEntityRenderers.register(JamiesModBlockEntities.CASTER, CasterBlockEntityRenderer::new);


    }
    public static void registerModelPredicateProviders() {
        ItemProperties.register(JamiesModItems.HOOK, JamiesModFabric.getModId("deployed"), (itemStack, clientWorld, livingEntity, seed) -> {
            if (livingEntity instanceof Player) {
                for (InteractionHand value : InteractionHand.values())
                {
                    ItemStack heldStack = livingEntity.getItemInHand(value);

                    if (heldStack == itemStack && (((PlayerWithHook)livingEntity).bygone$getHook() != null && !((PlayerWithHook)livingEntity).bygone$getHook().isRemoved()))
                    {
                        return 1;
                    }
                }
            }

            if (livingEntity == null) return 0.0F;
            return 0;
        });

        ItemProperties.register(JamiesModItems.VERDIGRIS_BLADE, JamiesModFabric.getModId("blocking"),
                (itemStack, clientWorld, livingEntity, seed) -> {
                    if (livingEntity == null) return 0;
                    if (livingEntity instanceof Player && livingEntity.isBlocking()) return 1;
                    return 0;
                }
        );
    }


}
