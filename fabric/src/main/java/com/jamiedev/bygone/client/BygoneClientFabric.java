package com.jamiedev.bygone.client;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.client.renderer.*;
import com.jamiedev.bygone.items.VerdigrisBladeItem;
import com.jamiedev.bygone.block.JamiesModWoodType;
import com.jamiedev.bygone.init.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.DimensionRenderingRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;

import java.util.Objects;
public class BygoneClientFabric implements ClientModInitializer {
    public static ResourceLocation BYGONE = Bygone.id("bygone");

    public static boolean isBlockingOnViaVersion(LivingEntity entity) {
        Item item = entity.getMainHandItem().getItem() instanceof VerdigrisBladeItem ? entity.getMainHandItem().getItem() : entity.getOffhandItem().getItem();
        return item instanceof VerdigrisBladeItem && item.components() != null && item.components().has(DataComponents.FOOD) && Objects.requireNonNull(item.components().get(DataComponents.FOOD)).eatSeconds() == 3600;
    }
    @Override
    public void onInitializeClient() {
        BygoneClient.registerRenderLayers(BlockRenderLayerMap.INSTANCE::putBlock);
        BygoneClient.createEntityRenderers();
        BygoneClient.createModelLayers((modelLayerLocation, layerDefinitionSupplier) -> EntityModelLayerRegistry.registerModelLayer(modelLayerLocation, layerDefinitionSupplier::get));
        BygoneClient.registerParticleFactories((particleType, spriteParticleRegistration) -> ParticleFactoryRegistry.getInstance().register(particleType, spriteParticleRegistration::create));


        DimensionRenderingRegistry.registerDimensionEffects(BYGONE, BygoneDimensionEffects.INSTANCE);
        DimensionRenderingRegistry.registerSkyRenderer(JamiesModDimension.BYGONE_LEVEL_KEY, BygoneSkyRenderer.INSTANCE);

        BygoneClient.registerModelPredicateProviders();

        Sheets.SIGN_MATERIALS.put(JamiesModWoodType.ANCIENT, Sheets.getSignMaterial(JamiesModWoodType.ANCIENT));
        Sheets.HANGING_SIGN_MATERIALS.put(JamiesModWoodType.ANCIENT, Sheets.getHangingSignMaterial(JamiesModWoodType.ANCIENT));

     //   BlockEntityRendererFactories.register(JamiesModBlockEntities.BRUSHABLE_BLOCK, BygoneBrushableBlockEntityRenderer::new);
    }
}
