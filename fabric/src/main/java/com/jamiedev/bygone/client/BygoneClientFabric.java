package com.jamiedev.bygone.client;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.client.particles.*;
import com.jamiedev.bygone.client.renderer.*;
import com.jamiedev.bygone.items.VerdigrisBladeItem;
import com.jamiedev.bygone.block.JamiesModWoodType;
import com.jamiedev.bygone.init.*;
import com.jamiedev.bygone.network.SyncPlayerHookS2C;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.DimensionRenderingRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.particle.SoulParticle;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.blockentity.HangingSignRenderer;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;

import java.util.Objects;
public class BygoneClientFabric implements ClientModInitializer {
    public static ResourceLocation BYGONE = Bygone.id("bygone");
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
        BygoneClient.registerRenderLayers(BlockRenderLayerMap.INSTANCE::putBlock);
        BygoneClient.createEntityRenderers();
        BygoneClient.createModelLayers((modelLayerLocation, layerDefinitionSupplier) -> EntityModelLayerRegistry.registerModelLayer(modelLayerLocation, layerDefinitionSupplier::get));


        ParticleFactoryRegistry.getInstance().register(JamiesModParticleTypes.BLEMISH, BlemishParticle.BlemishBlockProvider::new);
        ParticleFactoryRegistry.getInstance().register(JamiesModParticleTypes.RAFFLESIA_SPORES, RafflesiaSporeParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(JamiesModParticleTypes.ALGAE_BLOOM, SoulParticle.EmissiveProvider::new);
        ParticleFactoryRegistry.getInstance().register(JamiesModParticleTypes.SHELF, ShelfParticle.Factory::new);

        ParticleFactoryRegistry.getInstance().register(JamiesModParticleTypes.AMBER_DUST, AmberDustParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(JamiesModParticleTypes.ANCIENT_LEAVES, AncientLeavesParticle.Factory::new);

        DimensionRenderingRegistry.registerDimensionEffects(BYGONE, BygoneDimensionEffects.INSTANCE);
        DimensionRenderingRegistry.registerSkyRenderer(JamiesModDimension.BYGONE_LEVEL_KEY, BygoneSkyRenderer.INSTANCE);

        BygoneClient.registerModelPredicateProviders();

        ClientPlayNetworking.registerGlobalReceiver(SyncPlayerHookS2C.PACkET_ID, (payload, context) -> {
            context.client().execute(() -> ClientPacketHandler.handle(payload));
        });

        Sheets.SIGN_MATERIALS.put(JamiesModWoodType.ANCIENT, Sheets.getSignMaterial(JamiesModWoodType.ANCIENT));
        BlockEntityRenderers.register(JamiesModBlockEntities.MOD_SIGN_BLOCK_ENTITY, SignRenderer::new);
        BlockEntityRenderers.register(JamiesModBlockEntities.MOD_HANGING_SIGN_BLOCK_ENTITY, HangingSignRenderer::new);
     //   BlockEntityRendererFactories.register(JamiesModBlockEntities.BRUSHABLE_BLOCK, BygoneBrushableBlockEntityRenderer::new);
        BlockEntityRenderers.register(JamiesModBlockEntities.CASTER, CasterBlockEntityRenderer::new);


    }


}
