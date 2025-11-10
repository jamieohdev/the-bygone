package com.jamiedev.bygone.client;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.client.models.*;
import com.jamiedev.bygone.client.particles.*;
import com.jamiedev.bygone.client.renderer.BygonePortalRenderer;
import com.jamiedev.bygone.client.renderer.entity.*;
import com.jamiedev.bygone.common.item.VerdigrisBladeItem;
import com.jamiedev.bygone.common.util.PlayerWithHook;
import com.jamiedev.bygone.core.registry.*;
import net.minecraft.client.model.CowModel;
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
import net.minecraft.resources.ResourceLocation;
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
        consumer.accept(BGBlocks.AMBER.get(), RenderType.translucent());
        consumer.accept(BGBlocks.CLOUD.get(), RenderType.translucent());
        consumer.accept(BGBlocks.BYGONE_PORTAL.get(), RenderType.translucent());

        consumer.accept(BGBlocks.SEAGLASS.get(), RenderType.translucent());
        consumer.accept(BGBlocks.SEAGLASS_PANE.get(), RenderType.translucent());
        consumer.accept(BGBlocks.COBBLED_SEAGLASS.get(), RenderType.translucent());
        consumer.accept(BGBlocks.BREATH_POD.get(), RenderType.cutout());

        consumer.accept(BGBlocks.ANCIENT_LEAVES.get(), RenderType.cutout());
        consumer.accept(BGBlocks.ANCIENT_ROOTS.get(), RenderType.cutout());
        consumer.accept(BGBlocks.ANCIENT_SAPLING.get(), RenderType.cutout());
        consumer.accept(BGBlocks.ANCIENT_VINE.get(), RenderType.cutout());
        consumer.accept(BGBlocks.CHARNIA.get(), RenderType.cutout());
        consumer.accept(BGBlocks.GOURD_LANTERN_BEIGE.get(), RenderType.cutout());
        consumer.accept(BGBlocks.GOURD_LANTERN_MUAVE.get(), RenderType.cutout());
        consumer.accept(BGBlocks.GOURD_LANTERN_VERDANT.get(), RenderType.cutout());
        consumer.accept(BGBlocks.GOURD_DANGO.get(), RenderType.cutout());
        consumer.accept(BGBlocks.GOURD_DANGO_WALL.get(), RenderType.cutout());
        consumer.accept(BGBlocks.GOURD_VINE.get(), RenderType.cutout());
        consumer.accept(BGBlocks.WHIRLIWEED.get(), RenderType.cutout());
        consumer.accept(BGBlocks.BIG_WHIRLIWEED.get(), RenderType.cutout());

        consumer.accept(BGBlocks.RAFFLESIA.get(), RenderType.cutout());
        consumer.accept(BGBlocks.CAVE_VINES.get(), RenderType.cutout());
        consumer.accept(BGBlocks.CAVE_VINES_PLANT.get(), RenderType.cutout());
        consumer.accept(BGBlocks.MONTSECHIA.get(), RenderType.cutout());
        consumer.accept(BGBlocks.SAGARIA.get(), RenderType.cutout());
        consumer.accept(BGBlocks.ANCIENT_DOOR.get(), RenderType.cutout());
        consumer.accept(BGBlocks.ANCIENT_TRAPDOOR.get(), RenderType.cutout());
        consumer.accept(BGBlocks.POTTED_MONTSECHIA.get(), RenderType.cutout());
        consumer.accept(BGBlocks.POTTED_SAGARIA.get(), RenderType.cutout());
        consumer.accept(BGBlocks.SHORT_GRASS.get(), RenderType.cutout());
        consumer.accept(BGBlocks.TALL_GRASS.get(), RenderType.cutout());
        consumer.accept(BGBlocks.BLUE_ALGAE.get(), RenderType.cutout());
        consumer.accept(BGBlocks.MALACHITE_DOOR.get(), RenderType.cutout());
        consumer.accept(BGBlocks.POTTED_ROSE.get(), RenderType.cutout());
        consumer.accept(BGBlocks.ROSE.get(), RenderType.cutout());
        consumer.accept(BGBlocks.SPEED_WHEAT.get(), RenderType.cutout());

        consumer.accept(BGBlocks.SABLE_TRAPDOOR.get(), RenderType.cutout());
        consumer.accept(BGBlocks.SABLE_SAPLING.get(), RenderType.cutout());
        consumer.accept(BGBlocks.SABLE_LEAVES.get(), RenderType.cutout());
        consumer.accept(BGBlocks.SABLE_BRANCH.get(), RenderType.cutout());
        consumer.accept(BGBlocks.SABLE_BRANCH_PLANT.get(), RenderType.cutout());
        consumer.accept(BGBlocks.THORNY_SABLE_BRANCH.get(), RenderType.cutout());
        consumer.accept(BGBlocks.THORNY_SABLE_BRANCH_PLANT.get(), RenderType.cutout());
        consumer.accept(BGBlocks.SABLOSSOM.get(), RenderType.cutout());
        consumer.accept(BGBlocks.SABLE_GRASS.get(), RenderType.cutout());
        consumer.accept(BGBlocks.SABLE_GRASS_PLANT.get(), RenderType.cutout());
        consumer.accept(BGBlocks.SABLENUT.get(), RenderType.cutout());

        consumer.accept(BGBlocks.UMBRAL_GRASS.get(), RenderType.cutout());
        consumer.accept(BGBlocks.TALL_UMBRAL_GRASS.get(), RenderType.cutout());
        consumer.accept(BGBlocks.MOON_BLOSSOM.get(), RenderType.cutout());
        consumer.accept(BGBlocks.POTTED_MOON_BLOSSOM.get(), RenderType.cutout());

        consumer.accept(BGBlocks.AMBER_CLUMP.get(), RenderType.cutout());

        consumer.accept(BGBlocks.BLEMISH_VEIN.get(), RenderType.cutoutMipped());

        consumer.accept(BGBlocks.CRINOID.get(), RenderType.cutout());
        consumer.accept(BGBlocks.PRIMORDIAL_URCHIN.get(), RenderType.cutout());
        consumer.accept(BGBlocks.PRIMORDIAL_VENT.get(), RenderType.cutout());

        consumer.accept(BGBlocks.DEAD_RUGOSA_CORAL.get(), RenderType.cutout());
        consumer.accept(BGBlocks.DEAD_RUGOSA_CORAL_WALL_FAN.get(), RenderType.cutout());
        consumer.accept(BGBlocks.DEAD_RUGOSA_CORAL_FAN.get(), RenderType.cutout());
        consumer.accept(BGBlocks.RUGOSA_CORAL.get(), RenderType.cutout());
        consumer.accept(BGBlocks.RUGOSA_CORAL_FAN.get(), RenderType.cutout());
        consumer.accept(BGBlocks.RUGOSA_CORAL_WALL_FAN.get(), RenderType.cutout());
        consumer.accept(BGBlocks.DEAD_TABULATA_CORAL.get(), RenderType.cutout());
        consumer.accept(BGBlocks.DEAD_TABULATA_CORAL_WALL_FAN.get(), RenderType.cutout());
        consumer.accept(BGBlocks.DEAD_TABULATA_CORAL_FAN.get(), RenderType.cutout());
        consumer.accept(BGBlocks.TABULATA_CORAL.get(), RenderType.cutout());
        consumer.accept(BGBlocks.TABULATA_CORAL_FAN.get(), RenderType.cutout());
        consumer.accept(BGBlocks.TABULATA_CORAL_WALL_FAN.get(), RenderType.cutout());
        consumer.accept(BGBlocks.DEAD_PILLAR_CORAL.get(), RenderType.cutout());
        consumer.accept(BGBlocks.DEAD_PILLAR_CORAL_WALL_FAN.get(), RenderType.cutout());
        consumer.accept(BGBlocks.DEAD_PILLAR_CORAL_FAN.get(), RenderType.cutout());
        consumer.accept(BGBlocks.PILLAR_CORAL.get(), RenderType.cutout());
        consumer.accept(BGBlocks.PILLAR_CORAL_FAN.get(), RenderType.cutout());
        consumer.accept(BGBlocks.PILLAR_CORAL_WALL_FAN.get(), RenderType.cutout());
        consumer.accept(BGBlocks.DEAD_THAMNOPORA_CORAL.get(), RenderType.cutout());
        consumer.accept(BGBlocks.DEAD_THAMNOPORA_CORAL_WALL_FAN.get(), RenderType.cutout());
        consumer.accept(BGBlocks.DEAD_THAMNOPORA_CORAL_FAN.get(), RenderType.cutout());
        consumer.accept(BGBlocks.THAMNOPORA_CORAL.get(), RenderType.cutout());
        consumer.accept(BGBlocks.THAMNOPORA_CORAL_FAN.get(), RenderType.cutout());
        consumer.accept(BGBlocks.THAMNOPORA_CORAL_WALL_FAN.get(), RenderType.cutout());
        consumer.accept(BGBlocks.BELLADONNA.get(), RenderType.cutout());
        consumer.accept(BGBlocks.COLEUS.get(), RenderType.cutout());

        consumer.accept(BGBlocks.SHELF_FUNGUS.get(), RenderType.cutout());
        consumer.accept(BGBlocks.SHELF_ROOTS.get(), RenderType.cutout());
        consumer.accept(BGBlocks.SHELF_SPROUTS.get(), RenderType.cutout());
        consumer.accept(BGBlocks.SHELF_MOLD.get(), RenderType.cutout());
        consumer.accept(BGBlocks.ORANGE_FUNGI_VINES.get(), RenderType.cutout());
        consumer.accept(BGBlocks.ORANGE_FUNGI_PLANT.get(), RenderType.cutout());
        consumer.accept(BGBlocks.PINK_FUNGI_VINES.get(), RenderType.cutout());
        consumer.accept(BGBlocks.PINK_FUNGI_VINES_PLANT.get(), RenderType.cutout());
        consumer.accept(BGBlocks.PURPLE_FUNGI_VINES.get(), RenderType.cutout());
        consumer.accept(BGBlocks.PURPLE_FUNGI_VINES_PLANT.get(), RenderType.cutout());

        // consumer.accept(JamiesModBlocks.ORANGE_FUNGI_FAN, RenderLayer.getCutout());
        //consumer.accept(JamiesModBlocks.ORANGE_FUNGI_WALL_FAN, RenderLayer.getCutout());
        // consumer.accept(JamiesModBlocks.PINK_FUNGI_FAN, RenderLayer.getCutout());
        // consumer.accept(JamiesModBlocks.PINK_FUNGI_WALL_FAN, RenderLayer.getCutout());
        // consumer.accept(JamiesModBlocks.PURPLE_FUNGI_FAN, RenderLayer.getCutout());
        // consumer.accept(JamiesModBlocks.PURPLE_FUNGI_WALL_FAN, RenderLayer.getCutout());

        consumer.accept(BGBlocks.CLAYSTONE_FARMLAND.get(), RenderType.cutout());
        consumer.accept(BGBlocks.AMARANTH_CROP.get(), RenderType.cutout());
        consumer.accept(BGBlocks.SPRINKER.get(), RenderType.cutout());
        consumer.accept(BGBlocks.PLAGA_WALL.get(), RenderType.cutout());
        consumer.accept(BGBlocks.PLAGA.get(), RenderType.cutout());
        consumer.accept(BGBlocks.PLAGA_CROP.get(), RenderType.cutout());
        consumer.accept(BGBlocks.CHANTRELLE.get(), RenderType.cutout());
        consumer.accept(BGBlocks.POTTED_CHANTRELLE.get(), RenderType.cutout());

        consumer.accept(BGBlocks.POINTED_AMBER.get(), RenderType.cutout());
        consumer.accept(BGBlocks.CREOSOTE.get(), RenderType.cutout());
        consumer.accept(BGBlocks.CREOSOTE_SPROUTS.get(), RenderType.cutout());

        consumer.accept(BGBlocks.ICE_BOUQUET.get(), RenderType.cutout());

        consumer.accept(BGBlocks.AMPHORA.get(), RenderType.cutout());
        consumer.accept(BGBlocks.BLACK_AMPHORA.get(), RenderType.cutout());
        consumer.accept(BGBlocks.BLUE_AMPHORA.get(), RenderType.cutout());
        consumer.accept(BGBlocks.BROWN_AMPHORA.get(), RenderType.cutout());
        consumer.accept(BGBlocks.CYAN_AMPHORA.get(), RenderType.cutout());
        consumer.accept(BGBlocks.GILDED_AMPHORA.get(), RenderType.cutout());
        consumer.accept(BGBlocks.GRAY_AMPHORA.get(), RenderType.cutout());
        consumer.accept(BGBlocks.GREEN_AMPHORA.get(), RenderType.cutout());
        consumer.accept(BGBlocks.LIGHT_BLUE_AMPHORA.get(), RenderType.cutout());
        consumer.accept(BGBlocks.LIGHT_GRAY_AMPHORA.get(), RenderType.cutout());
        consumer.accept(BGBlocks.LIME_AMPHORA.get(), RenderType.cutout());
        consumer.accept(BGBlocks.MAGENTA_AMPHORA.get(), RenderType.cutout());
        consumer.accept(BGBlocks.ORANGE_AMPHORA.get(), RenderType.cutout());
        consumer.accept(BGBlocks.PINK_AMPHORA.get(), RenderType.cutout());
        consumer.accept(BGBlocks.PURPLE_AMPHORA.get(), RenderType.cutout());
        consumer.accept(BGBlocks.RED_AMPHORA.get(), RenderType.cutout());
        consumer.accept(BGBlocks.WHITE_AMPHORA.get(), RenderType.cutout());
        consumer.accept(BGBlocks.YELLOW_AMPHORA.get(), RenderType.cutout());

        consumer.accept(BGBlocks.MEGALITH_TOTEM.get(), RenderType.cutout());

        consumer.accept(BGBlocks.PRISTINE_VERDIGRIS_COG.get(), RenderType.cutout());
        consumer.accept(BGBlocks.TARNISHED_VERDIGRIS_COG.get(), RenderType.cutout());
        consumer.accept(BGBlocks.BROKEN_VERDIGRIS_COG.get(), RenderType.cutout());
        consumer.accept(BGBlocks.RAMSHACKLED_VERDIGRIS_COG.get(), RenderType.cutout());

        consumer.accept(BGBlocks.DOGU.get(), RenderType.cutout());
    }

    public static void createEntityRenderers() {
        //BlockEntityRenderers.register(BGBlockEntities.CASTER.get(), CasterBlockEntityRenderer::new);

        //BlockEntityRenderers.register(BGBlockEntities.AMPHORA.get(), AmphoraBlockEntityRenderer::new);
        BlockEntityRenderers.register(BGBlockEntities.BYGONE_PORTAL.get(), BygonePortalRenderer::new);

        EntityRenderers.register(BGEntityTypes.AMOEBA.get(), AmoebaRenderer::new);
        EntityRenderers.register(BGEntityTypes.AQUIFAWN.get(), AquifawnRenderer::new);
        EntityRenderers.register(BGEntityTypes.GLARE.get(), GlareRenderer::new);
        EntityRenderers.register(BGEntityTypes.COELACANTH.get(), CoelacanthRenderer::new);
        EntityRenderers.register(BGEntityTypes.DUCK.get(), DuckieRenderer::new);
        EntityRenderers.register(BGEntityTypes.SCUTTLE.get(), ScuttleRenderer::new);
        EntityRenderers.register(BGEntityTypes.COPPERBUG.get(), CopperbugRenderer::new);
        EntityRenderers.register(BGEntityTypes.HOOK.get(), HookRenderer::new);
        EntityRenderers.register(BGEntityTypes.EXOTIC_ARROW.get(), ExoticArrowRenderer::new);
        EntityRenderers.register(BGEntityTypes.NECTAUR_PETAL.get(), NectaurPetalRenderer::new);
        EntityRenderers.register(BGEntityTypes.SCUTTLE_SPIKE.get(), ScuttleSpikeRenderer::new);
        EntityRenderers.register(BGEntityTypes.TRILOBITE.get(), TrilobiteRenderer::new);
        EntityRenderers.register(BGEntityTypes.MOOBOO.get(), MoobooRenderer::new);
        EntityRenderers.register(BGEntityTypes.FUNGAL_PARENT.get(), FungalParentRenderer::new);
        EntityRenderers.register(BGEntityTypes.BIG_BEAK.get(), BigBeakRenderer::new);
        EntityRenderers.register(BGEntityTypes.PEST.get(), PestRenderer::new);
        EntityRenderers.register(BGEntityTypes.PESKY.get(), PeskyRenderer::new);
        EntityRenderers.register(BGEntityTypes.WHISKBILL.get(), WhiskbillRenderer::new);
        EntityRenderers.register(BGEntityTypes.NECTAUR.get(), NectaurRenderer::new);
        EntityRenderers.register(BGEntityTypes.LITHY.get(), LithyRenderer::new);
        EntityRenderers.register(BGEntityTypes.WISP.get(), WispRenderer::new);
        EntityRenderers.register(BGEntityTypes.WRAITH.get(), WraithRenderer::new);
        EntityRenderers.register(BGEntityTypes.SABEAST.get(), SabeastRenderer::new);
    }

    public static void createModelLayers(BiConsumer<ModelLayerLocation, Supplier<LayerDefinition>> consumer) {
        consumer.accept(JamiesModModelLayers.SCUTTLE_SPIKE, ScuttleSpikeModel::getTexturedModelData);
        consumer.accept(JamiesModModelLayers.COELACANTH, CoelacanthModel::getTexturedModelData);
        consumer.accept(JamiesModModelLayers.DUCKIE, DuckieModel::getTexturedModelData);
        consumer.accept(JamiesModModelLayers.GLARE, GlareModel::getTexturedModelData);
        consumer.accept(JamiesModModelLayers.SCUTTLE, ScuttleModel::getTexturedModelData);
        consumer.accept(JamiesModModelLayers.COPPERBUG, CopperbugModel::getTexturedModelData);
        consumer.accept(JamiesModModelLayers.TRILOBITE, TrilobiteModel::getTexturedModelData);
        consumer.accept(JamiesModModelLayers.MOOBOO, CowModel::createBodyLayer);
        consumer.accept(JamiesModModelLayers.MOOBOO_TRANS, CowModel::createBodyLayer);
        consumer.accept(JamiesModModelLayers.FUNGALPARENT, FungalParentModel::getTexturedModelData);
        consumer.accept(JamiesModModelLayers.BIG_BEAK, BigBeakModel::getTexturedModelData);
        consumer.accept(JamiesModModelLayers.BIG_BEAK_SADDLE, BigBeakModel::getTexturedModelData);
        consumer.accept(JamiesModModelLayers.BIG_BEAK_ARMOR, BigBeakModel::getTexturedModelData);
        consumer.accept(JamiesModModelLayers.PEST, PestModel::getTexturedModelData);
        consumer.accept(JamiesModModelLayers.PESKY, PestModel::getTexturedModelData);
        consumer.accept(JamiesModModelLayers.WHISKBILL, WhiskbillModel::getTexturedModelData);
        consumer.accept(JamiesModModelLayers.NECTAUR, NectaurModel::getTexturedModelData);
        consumer.accept(JamiesModModelLayers.LITHY, LithyModel::getTexturedModelData);
        consumer.accept(JamiesModModelLayers.WISP, WispModel::getTexturedModelData);
        consumer.accept(JamiesModModelLayers.WRAITH, WraithModel::getTexturedModelData);
        consumer.accept(JamiesModModelLayers.SABEAST, SabeastModel::getTexturedModelData);
        consumer.accept(JamiesModModelLayers.AMOEBA, AmoebaModel::getTexturedModelData);
        consumer.accept(JamiesModModelLayers.AMOEBA_OUTER, AmoebaModel::createOuterLayer);
        consumer.accept(JamiesModModelLayers.AQUIFAWN, AquifawnModel::getTexturedModelData);
    }

    public static void registerModelPredicateProviders() {
        ItemProperties.register(
                BGItems.HOOK.get(), Bygone.id("deployed"), (itemStack, clientWorld, livingEntity, seed) -> {
                    if (livingEntity instanceof Player) {
                        for (InteractionHand value : InteractionHand.values()) {
                            ItemStack heldStack = livingEntity.getItemInHand(value);

                            if (heldStack == itemStack && (((PlayerWithHook) livingEntity).bygone$getHook() != null && !((PlayerWithHook) livingEntity).bygone$getHook()
                                    .isRemoved())) {
                                return 1;
                            }
                        }
                    }

                    if (livingEntity == null) {
                        return 0.0F;
                    }
                    return 0;
                }
        );

        ItemProperties.register(
                BGItems.VERDIGRIS_BLADE.get(), Bygone.id("blocking"),
                (itemStack, clientWorld, livingEntity, seed) -> {
                    if (livingEntity == null) {
                        return 0;
                    }
                    if (livingEntity instanceof Player && livingEntity.isBlocking()) {
                        return 1;
                    }
                    return 0;
                }
        );

        ItemProperties.register(
                BGItems.ECHO_GONG.get(),
                ResourceLocation.withDefaultNamespace("tooting"),
                (p_234978_, p_234979_, p_234980_, p_234981_) -> p_234980_ != null
                        && p_234980_.isUsingItem() && p_234980_.getUseItem() == p_234978_ ? 1.0F : 0.0F
        );

        ItemProperties.register(
                BGItems.MALICIOUS_WAR_HORN.get(),
                ResourceLocation.withDefaultNamespace("tooting"),
                (p_234978_, p_234979_, p_234980_, p_234981_) -> p_234980_ != null
                        && p_234980_.isUsingItem() && p_234980_.getUseItem() == p_234978_ ? 1.0F : 0.0F
        );
    }

    @SuppressWarnings("unchecked")
    public static <T extends ParticleOptions> void registerParticleFactories(BiConsumer<ParticleType<T>, ParticleEngine.SpriteParticleRegistration<T>> consumer) {
        consumer.accept(
                (ParticleType<T>) BGParticleTypes.BLEMISH,
                p_107611_ -> (ParticleProvider<T>) new BlemishParticle.BlemishBlockProvider(p_107611_)
        );
        consumer.accept(
                (ParticleType<T>) BGParticleTypes.RAFFLESIA_SPORES,
                spriteProvider -> (ParticleProvider<T>) new RafflesiaSporeParticle.Factory(spriteProvider)
        );
        consumer.accept(
                (ParticleType<T>) BGParticleTypes.ALGAE_BLOOM,
                sprite -> (ParticleProvider<T>) new SoulParticle.EmissiveProvider(sprite)
        );
        consumer.accept(
                (ParticleType<T>) BGParticleTypes.SHELF,
                spriteProvider -> (ParticleProvider<T>) new ShelfParticle.Factory(spriteProvider)
        );

        consumer.accept(
                (ParticleType<T>) BGParticleTypes.AMBER_DUST,
                spriteProvider -> (ParticleProvider<T>) new AmberDustParticle.Factory(spriteProvider)
        );
        consumer.accept(
                (ParticleType<T>) BGParticleTypes.ANCIENT_LEAVES,
                spriteProvider -> (ParticleProvider<T>) new AncientLeavesParticle.Factory(spriteProvider)
        );
        consumer.accept(
                (ParticleType<T>) BGParticleTypes.SABLE_LEAVES,
                spriteProvider -> (ParticleProvider<T>) new AncientLeavesParticle.Factory(spriteProvider)
        );
        consumer.accept(
                (ParticleType<T>) BGParticleTypes.UPSIDEDOWN,
                spriteProvider -> (ParticleProvider<T>) new UpsidedownDropParticle.Provider(spriteProvider)
        );

        consumer.accept(
                (ParticleType<T>) BGParticleTypes.WORM,
                spriteProvider -> (ParticleProvider<T>) new WormParticle.Factory(spriteProvider)
        );

        consumer.accept(
                (ParticleType<T>) BGParticleTypes.SABLOSSOM,
                spriteProvider -> (ParticleProvider<T>) new SablossomParticle.Factory(spriteProvider)
        );

    }

    public static boolean isWeaponBlocking(LivingEntity entity) {
        return entity.isUsingItem() && (canWeaponBlock(entity));
    }

    public static boolean canWeaponBlock(LivingEntity entity) {
        if ((entity.getMainHandItem().getItem() instanceof VerdigrisBladeItem)) {
            Item weaponItem = entity.getOffhandItem().getItem() instanceof VerdigrisBladeItem ? entity.getMainHandItem()
                    .getItem() : entity.getOffhandItem().getItem();
            return weaponItem instanceof VerdigrisBladeItem;
        }
        return false;
    }
}
