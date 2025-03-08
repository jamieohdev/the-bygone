package com.jamiedev.bygone.client;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.common.util.PlayerWithHook;
import com.jamiedev.bygone.client.models.*;
import com.jamiedev.bygone.client.particles.*;
import com.jamiedev.bygone.client.renderer.*;
import com.jamiedev.bygone.core.registry.*;
import com.jamiedev.bygone.common.item.VerdigrisBladeItem;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SoulParticle;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class BygoneClient {


    public static void registerRenderLayers(BiConsumer<Block, RenderType> consumer) {
        consumer.accept(BGBlocks.AMBER, RenderType.translucent());
        consumer.accept(BGBlocks.CLOUD, RenderType.translucent());
        consumer.accept(BGBlocks.BYGONE_PORTAL, RenderType.translucent());

        consumer.accept(BGBlocks.ANCIENT_LEAVES, RenderType.cutout());
        consumer.accept(BGBlocks.ANCIENT_ROOTS, RenderType.cutout());
        consumer.accept(BGBlocks.ANCIENT_SAPLING, RenderType.cutout());
        consumer.accept(BGBlocks.ANCIENT_VINE, RenderType.cutout());
        consumer.accept(BGBlocks.CHARNIA, RenderType.cutout());
        consumer.accept(BGBlocks.GOURD_LANTERN_BEIGE, RenderType.cutout());
        consumer.accept(BGBlocks.GOURD_LANTERN_MUAVE, RenderType.cutout());
        consumer.accept(BGBlocks.GOURD_LANTERN_VERDANT, RenderType.cutout());
        consumer.accept(BGBlocks.GOURD_DANGO, RenderType.cutout());
        consumer.accept(BGBlocks.GOURD_DANGO_WALL, RenderType.cutout());
        consumer.accept(BGBlocks.GOURD_VINE, RenderType.cutout());
        consumer.accept(BGBlocks.BIG_WHIRLIWEED, RenderType.cutout());
        consumer.accept(BGBlocks.WHIRLIWEED, RenderType.cutout());
        consumer.accept(BGBlocks.RAFFLESIA, RenderType.cutout());
        consumer.accept(BGBlocks.CAVE_VINES, RenderType.cutout());
        consumer.accept(BGBlocks.CAVE_VINES_PLANT, RenderType.cutout());
        consumer.accept(BGBlocks.MONTSECHIA, RenderType.cutout());
        consumer.accept(BGBlocks.SAGARIA, RenderType.cutout());
        consumer.accept(BGBlocks.ANCIENT_DOOR, RenderType.cutout());
        consumer.accept(BGBlocks.ANCIENT_TRAPDOOR, RenderType.cutout());
        consumer.accept(BGBlocks.POTTED_MONTSECHIA, RenderType.cutout());
        consumer.accept(BGBlocks.POTTED_SAGARIA, RenderType.cutout());
        consumer.accept(BGBlocks.SHORT_GRASS, RenderType.cutout());
        consumer.accept(BGBlocks.TALL_GRASS, RenderType.cutout());
        consumer.accept(BGBlocks.BLUE_ALGAE, RenderType.cutout());
        consumer.accept(BGBlocks.MALACHITE_DOOR, RenderType.cutout());

        consumer.accept(BGBlocks.BLEMISH_VEIN, RenderType.cutoutMipped());

        consumer.accept(BGBlocks.CRINOID, RenderType.cutout());
        consumer.accept(BGBlocks.PRIMORDIAL_URCHIN, RenderType.cutout());
        consumer.accept(BGBlocks.PRIMORDIAL_VENT, RenderType.cutout());

        consumer.accept(BGBlocks.DEAD_ORANGE_CORAL, RenderType.cutout());
        consumer.accept(BGBlocks.DEAD_ORANGE_CORAL_WALL_FAN, RenderType.cutout());
        consumer.accept(BGBlocks.DEAD_ORANGE_CORAL_FAN, RenderType.cutout());
        consumer.accept(BGBlocks.ORANGE_CORAL, RenderType.cutout());
        consumer.accept(BGBlocks.ORANGE_CORAL_FAN, RenderType.cutout());
        consumer.accept(BGBlocks.ORANGE_CORAL_WALL_FAN, RenderType.cutout());
        consumer.accept(BGBlocks.DEAD_BLUE_CORAL, RenderType.cutout());
        consumer.accept(BGBlocks.DEAD_BLUE_CORAL_WALL_FAN, RenderType.cutout());
        consumer.accept(BGBlocks.DEAD_BLUE_CORAL_FAN, RenderType.cutout());
        consumer.accept(BGBlocks.BLUE_CORAL, RenderType.cutout());
        consumer.accept(BGBlocks.BLUE_CORAL_FAN, RenderType.cutout());
        consumer.accept(BGBlocks.BLUE_CORAL_WALL_FAN, RenderType.cutout());
        consumer.accept(BGBlocks.BELLADONNA, RenderType.cutout());
        consumer.accept(BGBlocks.COLEUS, RenderType.cutout());

        consumer.accept(BGBlocks.SHELF_FUNGUS, RenderType.cutout());
        consumer.accept(BGBlocks.SHELF_ROOTS, RenderType.cutout());
        consumer.accept(BGBlocks.SHELF_SPROUTS, RenderType.cutout());
        consumer.accept(BGBlocks.SHELF_MOLD, RenderType.cutout());
        consumer.accept(BGBlocks.ORANGE_FUNGI_VINES, RenderType.cutout());
        consumer.accept(BGBlocks.ORANGE_FUNGI_PLANT, RenderType.cutout());
        consumer.accept(BGBlocks.PINK_FUNGI_VINES, RenderType.cutout());
        consumer.accept(BGBlocks.PINK_FUNGI_VINES_PLANT, RenderType.cutout());
        consumer.accept(BGBlocks.PURPLE_FUNGI_VINES, RenderType.cutout());
        consumer.accept(BGBlocks.PURPLE_FUNGI_VINES_PLANT, RenderType.cutout());

        // consumer.accept(JamiesModBlocks.ORANGE_FUNGI_FAN, RenderLayer.getCutout());
        //consumer.accept(JamiesModBlocks.ORANGE_FUNGI_WALL_FAN, RenderLayer.getCutout());
        // consumer.accept(JamiesModBlocks.PINK_FUNGI_FAN, RenderLayer.getCutout());
        // consumer.accept(JamiesModBlocks.PINK_FUNGI_WALL_FAN, RenderLayer.getCutout());
        // consumer.accept(JamiesModBlocks.PURPLE_FUNGI_FAN, RenderLayer.getCutout());
        // consumer.accept(JamiesModBlocks.PURPLE_FUNGI_WALL_FAN, RenderLayer.getCutout());

        consumer.accept(BGBlocks.CLAYSTONE_FARMLAND, RenderType.cutout());
        consumer.accept(BGBlocks.AMARANTH_CROP, RenderType.cutout());
        consumer.accept(BGBlocks.SPRINKER, RenderType.cutout());
        consumer.accept(BGBlocks.PLAGA_WALL, RenderType.cutout());
        consumer.accept(BGBlocks.PLAGA, RenderType.cutout());
        consumer.accept(BGBlocks.PLAGA_CROP, RenderType.cutout());
        consumer.accept(BGBlocks.CHANTRELLE, RenderType.cutout());

        consumer.accept(BGBlocks.POINTED_AMBER, RenderType.cutout());
        consumer.accept(BGBlocks.CREOSOTE, RenderType.cutout());
        consumer.accept(BGBlocks.CREOSOTE_SPROUTS, RenderType.cutout());
    }

    public static void createEntityRenderers() {
        BlockEntityRenderers.register(BGBlockEntities.CASTER, CasterBlockEntityRenderer::new);

        EntityRenderers.register(BGEntityTypes.GLARE, GlareRenderer::new);
        EntityRenderers.register(BGEntityTypes.COELACANTH, CoelacanthRenderer::new);
        EntityRenderers.register(BGEntityTypes.DUCK, DuckieRenderer::new);
        EntityRenderers.register(BGEntityTypes.SCUTTLE, ScuttleRenderer::new);
        EntityRenderers.register(BGEntityTypes.COPPERBUG, CopperbugRenderer::new);
        EntityRenderers.register(BGEntityTypes.HOOK, HookRenderer::new);
        EntityRenderers.register(BGEntityTypes.EXOTIC_ARROW, ExoticArrowRenderer::new);
        EntityRenderers.register(BGEntityTypes.SCUTTLE_SPIKE, ScuttleSpikeRenderer::new);
        EntityRenderers.register(BGEntityTypes.TRILOBITE, TrilobiteRenderer::new);
        EntityRenderers.register(BGEntityTypes.MOOBOO, MoobooRenderer::new);
        EntityRenderers.register(BGEntityTypes.FUNGAL_PARENT, FungalParentRenderer::new);
        EntityRenderers.register(BGEntityTypes.BIG_BEAK, BigBeakRenderer::new);
        EntityRenderers.register(BGEntityTypes.PEST, PestRenderer::new);
    }
    
    public static void createModelLayers(BiConsumer<ModelLayerLocation, Supplier<LayerDefinition>> consumer) {
        consumer.accept(JamiesModModelLayers.SCUTTLE_SPIKE, ScuttleSpikeModel::getTexturedModelData);
        consumer.accept(JamiesModModelLayers.COELACANTH, CoelacanthModel::getTexturedModelData);
        consumer.accept(JamiesModModelLayers.DUCKIE, DuckieModel::getTexturedModelData);
        consumer.accept(JamiesModModelLayers.GLARE, GlareModel::getTexturedModelData);
        consumer.accept(JamiesModModelLayers.SCUTTLE, ScuttleModel::getTexturedModelData);
        consumer.accept(JamiesModModelLayers.COPPERBUG, CopperbugModel::getTexturedModelData);
        consumer.accept(JamiesModModelLayers.TRILOBITE, TrilobiteModel::getTexturedModelData);
        consumer.accept(JamiesModModelLayers.MOOBOO, MoobooModel::getTexturedModelData);
        consumer.accept(JamiesModModelLayers.FUNGALPARENT, FungalParentModel::getTexturedModelData);
        consumer.accept(JamiesModModelLayers.BIG_BEAK, BigBeakModel::getTexturedModelData);
        consumer.accept(JamiesModModelLayers.BIG_BEAK_SADDLE, BigBeakModel::getTexturedModelData);
        consumer.accept(JamiesModModelLayers.BIG_BEAK_ARMOR, BigBeakModel::getTexturedModelData);
        consumer.accept(JamiesModModelLayers.PEST, PestModel::getTexturedModelData);
    }

    public static void registerModelPredicateProviders() {
        ItemProperties.register(BGItems.HOOK, Bygone.id("deployed"), (itemStack, clientWorld, livingEntity, seed) -> {
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

        ItemProperties.register(BGItems.VERDIGRIS_BLADE, Bygone.id("blocking"),
                (itemStack, clientWorld, livingEntity, seed) -> {
                    if (livingEntity == null) return 0;
                    if (livingEntity instanceof Player && livingEntity.isBlocking()) return 1;
                    return 0;
                }
        );
    }

    @SuppressWarnings("unchecked")
    public static <T extends ParticleOptions> void registerParticleFactories(BiConsumer<ParticleType<T>, ParticleEngine.SpriteParticleRegistration<T>> consumer) {
        consumer.accept((ParticleType<T>) BGParticleTypes.BLEMISH, p_107611_ -> (ParticleProvider<T>) new BlemishParticle.BlemishBlockProvider(p_107611_));
        consumer.accept((ParticleType<T>) BGParticleTypes.RAFFLESIA_SPORES, spriteProvider -> (ParticleProvider<T>) new RafflesiaSporeParticle.Factory(spriteProvider));
        consumer.accept((ParticleType<T>) BGParticleTypes.ALGAE_BLOOM, sprite -> (ParticleProvider<T>) new SoulParticle.EmissiveProvider(sprite));
        consumer.accept((ParticleType<T>) BGParticleTypes.SHELF, spriteProvider -> (ParticleProvider<T>) new ShelfParticle.Factory(spriteProvider));

        consumer.accept((ParticleType<T>) BGParticleTypes.AMBER_DUST, spriteProvider -> (ParticleProvider<T>) new AmberDustParticle.Factory(spriteProvider));
        consumer.accept((ParticleType<T>) BGParticleTypes.ANCIENT_LEAVES, spriteProvider -> (ParticleProvider<T>) new AncientLeavesParticle.Factory(spriteProvider));
    }

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
}
