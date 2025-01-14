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
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.particle.SoulParticle;
import net.minecraft.client.render.DimensionEffects;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.client.render.block.entity.HangingSignBlockEntityRenderer;
import net.minecraft.client.render.block.entity.SignBlockEntityRenderer;
import net.minecraft.component.DataComponentTypes;

import java.util.Objects;
public class JamiesModClient implements ClientModInitializer {
    public static Identifier BYGONE = JamiesModFabric.getModId("bygone");
    public  static DimensionEffects.SkyType BYGONE_SKY;

    public static boolean isWeaponBlocking(LivingEntity entity) {
        return entity.isUsingItem() && (canWeaponBlock(entity));
    }

    public static boolean canWeaponBlock(LivingEntity entity) {
        if ((entity.getMainHandStack().getItem() instanceof VerdigrisBladeItem)) {
            Item weaponItem = entity.getOffHandStack().getItem() instanceof VerdigrisBladeItem ? entity.getMainHandStack().getItem() : entity.getOffHandStack().getItem();
            return weaponItem instanceof VerdigrisBladeItem;
        }
       return false;
    }
    public static boolean isBlockingOnViaVersion(LivingEntity entity) {
        Item item = entity.getMainHandStack().getItem() instanceof VerdigrisBladeItem ? entity.getMainHandStack().getItem() : entity.getOffHandStack().getItem();
        return item instanceof VerdigrisBladeItem && item.getComponents() != null && item.getComponents().contains(DataComponentTypes.FOOD) && Objects.requireNonNull(item.getComponents().get(DataComponentTypes.FOOD)).eatSeconds() == 3600;
    }
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.AMBER, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.CLOUD, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.BYGONE_PORTAL, RenderLayer.getTranslucent());

        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.ANCIENT_LEAVES, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.ANCIENT_ROOTS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.ANCIENT_SAPLING, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.ANCIENT_VINE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.CHARNIA, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.GOURD_LANTERN_BEIGE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.GOURD_LANTERN_MUAVE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.GOURD_LANTERN_VERDANT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.GOURD_DANGO, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.GOURD_DANGO_WALL, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.GOURD_VINE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.BIG_WHIRLIWEED, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.WHIRLIWEED, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.RAFFLESIA, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.CAVE_VINES, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.CAVE_VINES_PLANT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.MONTSECHIA, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.SAGARIA, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.ANCIENT_DOOR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.ANCIENT_TRAPDOOR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.POTTED_MONTSECHIA, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.POTTED_SAGARIA, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.SHORT_GRASS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.TALL_GRASS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.BLUE_ALGAE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.MALACHITE_DOOR, RenderLayer.getCutout());

        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.BLEMISH_VEIN, RenderLayer.getCutoutMipped());

        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.CRINOID, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.PRIMORDIAL_URCHIN, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.PRIMORDIAL_VENT, RenderLayer.getCutout());

        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.DEAD_ORANGE_CORAL, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.DEAD_ORANGE_CORAL_WALL_FAN, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.DEAD_ORANGE_CORAL_FAN, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.ORANGE_CORAL, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.ORANGE_CORAL_FAN, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.ORANGE_CORAL_WALL_FAN, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.DEAD_BLUE_CORAL, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.DEAD_BLUE_CORAL_WALL_FAN, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.DEAD_BLUE_CORAL_FAN, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.BLUE_CORAL, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.BLUE_CORAL_FAN, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.BLUE_CORAL_WALL_FAN, RenderLayer.getCutout());

        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.SHELF_FUNGUS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.SHELF_ROOTS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.SHELF_SPROUTS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.SHELF_MOLD, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.ORANGE_FUNGI_VINES, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.ORANGE_FUNGI_PLANT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.PINK_FUNGI_VINES, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.PINK_FUNGI_VINES_PLANT, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.PURPLE_FUNGI_VINES, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.PURPLE_FUNGI_VINES_PLANT, RenderLayer.getCutout());

        // BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.ORANGE_FUNGI_FAN, RenderLayer.getCutout());
        //BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.ORANGE_FUNGI_WALL_FAN, RenderLayer.getCutout());
       // BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.PINK_FUNGI_FAN, RenderLayer.getCutout());
       // BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.PINK_FUNGI_WALL_FAN, RenderLayer.getCutout());
       // BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.PURPLE_FUNGI_FAN, RenderLayer.getCutout());
       // BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.PURPLE_FUNGI_WALL_FAN, RenderLayer.getCutout());

        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.CLAYSTONE_FARMLAND, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.AMARANTH_CROP, RenderLayer.getCutout());

        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.POINTED_AMBER, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.CREOSOTE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(JamiesModBlocks.CREOSOTE_SPROUTS, RenderLayer.getCutout());

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
            return new FlyingItemEntityRenderer<>(context, 1.0F, true);
        });

        EntityRendererRegistry.register(JamiesModEntityTypes.BIG_BEAK, BigBeakRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(JamiesModModelLayers.BIG_BEAK, BigBeakModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(JamiesModModelLayers.BIG_BEAK_SADDLE, BigBeakModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(JamiesModModelLayers.BIG_BEAK_ARMOR, BigBeakModel::getTexturedModelData);

        ParticleFactoryRegistry.getInstance().register(JamiesModParticleTypes.BLEMISH, BlemishParticle.BlemishBlockProvider::new);
        ParticleFactoryRegistry.getInstance().register(JamiesModParticleTypes.RAFFLESIA_SPORES, RafflesiaSporeParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(JamiesModParticleTypes.ALGAE_BLOOM, SoulParticle.SculkSoulFactory::new);
        ParticleFactoryRegistry.getInstance().register(JamiesModParticleTypes.SHELF, ShelfParticle.Factory::new);

        ParticleFactoryRegistry.getInstance().register(JamiesModParticleTypes.AMBER_DUST, AmberDustParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(JamiesModParticleTypes.ANCIENT_LEAVES, AncientLeavesParticle.Factory::new);

        DimensionRenderingRegistry.registerDimensionEffects(BYGONE, BygoneDimensionEffects.INSTANCE);
        DimensionRenderingRegistry.registerSkyRenderer(JamiesModDimension.BYGONE_LEVEL_KEY, BygoneSkyRenderer.INSTANCE);

        registerModelPredicateProviders();

        ClientPlayNetworking.registerGlobalReceiver(SyncPlayerHookS2C.PACkET_ID, (payload, context) -> {
            context.client().execute(() -> SyncPlayerHookPacketHandler.handle(payload, context));
        });

        TexturedRenderLayers.SIGN_TYPE_TEXTURES.put(JamiesModWoodType.ANCIENT, TexturedRenderLayers.getSignTextureId(JamiesModWoodType.ANCIENT));
        BlockEntityRendererFactories.register(JamiesModBlockEntities.MOD_SIGN_BLOCK_ENTITY, SignBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(JamiesModBlockEntities.MOD_HANGING_SIGN_BLOCK_ENTITY, HangingSignBlockEntityRenderer::new);
     //   BlockEntityRendererFactories.register(JamiesModBlockEntities.BRUSHABLE_BLOCK, BygoneBrushableBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(JamiesModBlockEntities.CASTER, CasterBlockEntityRenderer::new);


    }
    public static void registerModelPredicateProviders() {
        ModelPredicateProviderRegistry.register(JamiesModItems.HOOK, JamiesModFabric.getModId("deployed"), (itemStack, clientWorld, livingEntity, seed) -> {
            if (livingEntity instanceof PlayerEntity) {
                for (Hand value : Hand.values())
                {
                    ItemStack heldStack = livingEntity.getStackInHand(value);

                    if (heldStack == itemStack && (((PlayerWithHook)livingEntity).bygone$getHook() != null && !((PlayerWithHook)livingEntity).bygone$getHook().isRemoved()))
                    {
                        return 1;
                    }
                }
            }

            if (livingEntity == null) return 0.0F;
            return 0;
        });

        ModelPredicateProviderRegistry.register(JamiesModItems.VERDIGRIS_BLADE, JamiesModFabric.getModId("blocking"),
                (itemStack, clientWorld, livingEntity, seed) -> {
                    if (livingEntity == null) return 0;
                    if (livingEntity instanceof PlayerEntity && livingEntity.isBlocking()) return 1;
                    return 0;
                }
        );
    }


}
