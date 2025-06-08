package com.jamiedev.bygone.core.registry;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.common.block.*;
import com.jamiedev.bygone.common.block.gourds.GourdDangoBlock;
import com.jamiedev.bygone.common.block.gourds.GourdDangoWallBlock;
import com.jamiedev.bygone.common.block.gourds.GourdLanternBlock;
import com.jamiedev.bygone.common.block.gourds.GourdVineBlock;
import com.jamiedev.bygone.common.block.shelf.*;
import com.kekecreations.jinxedlib.core.util.JinxedRegistryHelper;

import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.worldgen.features.TreeFeatures;

import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

import java.util.function.Supplier;

import static net.minecraft.world.level.block.Blocks.DIRT;

public class BGBlocks {


    /**
     * TODO:
     *
     * Bystone / Byslate Polished (Textures WIP), Bricks, Shingles Chiseled variants (And stairs / slabs / walls)
     * Claystone Bricks Chiseled Shingles variants (And stairs / slabs / walls)
     * "Ventstone" Normal Bricks Chiseled Shingles (And stairs / slabs / walls)
     * "Whitestone" Normal Bricks Chiseled Shingles  (And stairs / slabs / walls)
     * RATTAN woodset???
     * Redplume Crop
     * Spiral Sprouts flower block
     *
     * Add "replaceables" and "enchantment_power_transmitter" tags
     * Fix Undergrass model
     */


    public static final Supplier<Block> POLISHED_BYSTONE = registerBlock("polished_bystone", () ->
            new Block(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_BROWN).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(0.8F)));

    public static final Supplier<Block> POLISHED_BYSTONE_SLAB = registerBlock("polished_bystone_slab", () ->
            new SlabBlock(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_BROWN).instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.STONE).ignitedByLava()));

    public static final Supplier<Block> POLISHED_BYSTONE_STAIRS = registerBlock("polished_bystone_stairs", () ->
            new StairBlock(BGBlocks.POLISHED_BYSTONE.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.QUARTZ_STAIRS).strength(2.0f)));

    public static final Supplier<Block> POLISHED_BYSTONE_WALL = registerBlock("polished_bystone_wall", () ->
            new WallBlock(BlockBehaviour.Properties.ofFullCopy(BGBlocks.POLISHED_BYSTONE.get()).strength(2.0f)));

    public static final Supplier<Block> POLISHED_BYSLATE = registerBlock("polished_byslate", () ->
            new Block(BlockBehaviour.Properties.of().mapColor(MapColor.DEEPSLATE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 6.0F).sound(SoundType.DEEPSLATE)));

    public static final Supplier<Block> POLISHED_BYSLATE_SLAB = registerBlock("polished_byslate_slab", () ->
            new SlabBlock(BlockBehaviour.Properties.of().mapColor(MapColor.DEEPSLATE).instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.STONE).ignitedByLava()));

    public static final Supplier<Block> POLISHED_BYSLATE_STAIRS = registerBlock("polished_byslate_stairs", () ->
            new StairBlock(BGBlocks.POLISHED_BYSLATE.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.QUARTZ_STAIRS).strength(2.0f)));

    public static final Supplier<Block> POLISHED_BYSLATE_WALL = registerBlock("polished_byslate_wall", () ->
            new WallBlock(BlockBehaviour.Properties.ofFullCopy(BGBlocks.POLISHED_BYSLATE.get()).strength(2.0f)));

    public static final Supplier<Block> POLISHED_BYSTONE_BRICK = registerBlock("polished_bystone_bricks", () ->
            new Block(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_BROWN).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(0.8F)));

    public static final Supplier<Block> POLISHED_BYSTONE_BRICK_SLAB = registerBlock("polished_bystone_bricks_slab", () ->
            new SlabBlock(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_BROWN).instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.STONE).ignitedByLava()));

    public static final Supplier<Block> POLISHED_BYSTONE_BRICK_STAIRS = registerBlock("polished_bystone_bricks_stairs", () ->
            new StairBlock(BGBlocks.POLISHED_BYSTONE_BRICK.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.QUARTZ_STAIRS).strength(2.0f)));

    public static final Supplier<Block> POLISHED_BYSTONE_BRICK_WALL = registerBlock("polished_bystone_bricks_wall", () ->
            new WallBlock(BlockBehaviour.Properties.ofFullCopy(BGBlocks.POLISHED_BYSTONE_BRICK.get()).strength(2.0f)));

    public static final Supplier<Block> POLISHED_BYSLATE_BRICK = registerBlock("polished_byslate_bricks", () ->
            new Block(BlockBehaviour.Properties.of().mapColor(MapColor.DEEPSLATE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 6.0F).sound(SoundType.DEEPSLATE)));

    public static final Supplier<Block> POLISHED_BYSLATE_BRICK_SLAB = registerBlock("polished_byslate_bricks_slab", () ->
            new SlabBlock(BlockBehaviour.Properties.of().mapColor(MapColor.DEEPSLATE).instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.STONE).ignitedByLava()));

    public static final Supplier<Block> POLISHED_BYSLATE_BRICK_STAIRS = registerBlock("polished_byslate_bricks_stairs", () ->
            new StairBlock(BGBlocks.POLISHED_BYSLATE_BRICK.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.QUARTZ_STAIRS).strength(2.0f)));

    public static final Supplier<Block> POLISHED_BYSLATE_BRICK_WALL = registerBlock("polished_byslate_bricks_wall", () ->
            new WallBlock(BlockBehaviour.Properties.ofFullCopy(BGBlocks.POLISHED_BYSLATE_BRICK.get()).strength(2.0f)));

    public static final Supplier<Block> CHISELED_POLISHED_BYSTONE = registerBlock("chiseled_polished_bystone", () ->
            new Block(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_BROWN).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(0.8F)));

    public static final Supplier<Block> CHISELED_POLISHED_BYSLATE = registerBlock("chiseled_polished_byslate", () ->
            new Block(BlockBehaviour.Properties.of().mapColor(MapColor.DEEPSLATE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 6.0F).sound(SoundType.DEEPSLATE)));

    public static final Supplier<Block> POLISHED_BYSTONE_SHINGLES = registerBlock("polished_bystone_shingles", () ->
            new Block(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_BROWN).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(0.8F)));

    public static final Supplier<Block> POLISHED_BYSLATE_SHINGLES = registerBlock("polished_byslate_shingles", () ->
            new Block(BlockBehaviour.Properties.of().mapColor(MapColor.DEEPSLATE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 6.0F).sound(SoundType.DEEPSLATE)));



    public static final Supplier<Block> BYGONE_PORTAL =  registerBlockWithoutBlockItem("bygone_portal", () ->
            new BygonePortalBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.NETHER_PORTAL).lightLevel((state) -> 6).noLootTable().noCollission().strength(-1.0f,3600000.0f)) );

    public static final Supplier<Block> BYGONE_PORTAL_FRAME = registerBlockWithoutBlockItem("bygone_portal_frame", () ->
            new BygonePortalFrameBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GRAY).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1000000.8F)));
    public static final Supplier<Block> BYGONE_PORTAL_FRAME_BLOCK = registerBlock("bygone_portal_frame_block", () ->
            new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GRAY).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1000000.8F)));
    public static final Supplier<Block> BYGONE_PORTAL_FRAME_PLACEABLE = registerBlockWithoutBlockItem("bygone_portal_frame_placeable", () ->
            new BygonePortalFramePlaceableBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GRAY).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1000000.8F)));

    public static final Supplier<Block> ARCANE_CORE = registerBlockWithoutBlockItem("arcane_core", () -> new ArcaneCoreBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_CYAN).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 3.0F)));

    public static final Supplier<Block> BYGONESTONE_IRON_ORE = registerBlock("bystone_iron_ore", () -> new DropExperienceBlock(ConstantInt.of(0),
            BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 3.0F)));
    public static final Supplier<Block>  BYGONESLATE_IRON_ORE = registerBlock("byslate_iron_ore", () -> new DropExperienceBlock(ConstantInt.of(0),
            BlockBehaviour.Properties.ofLegacyCopy(BYGONESTONE_IRON_ORE.get()).mapColor(MapColor.DEEPSLATE).strength(4.5F, 3.0F).sound(SoundType.DEEPSLATE)));

    public static final Supplier<Block>  BYGONESTONE_COAL_ORE = registerBlock("bystone_coal_ore", () -> new DropExperienceBlock(ConstantInt.of(0),
            BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 3.0F)));
    public static final Supplier<Block> BYGONESLATE_COAL_ORE = registerBlock("byslate_coal_ore", () -> new DropExperienceBlock(ConstantInt.of(0),
            BlockBehaviour.Properties.ofLegacyCopy(BYGONESTONE_COAL_ORE.get()).mapColor(MapColor.DEEPSLATE).strength(4.5F, 3.0F).sound(SoundType.DEEPSLATE)));

    public static final Supplier<Block>  BYGONESTONE_COPPER_ORE = registerBlock("bystone_copper_ore", () -> new DropExperienceBlock(ConstantInt.of(0),
            BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 3.0F)));
    public static final Supplier<Block> BYGONESLATE_COPPER_ORE = registerBlock("byslate_copper_ore", () -> new DropExperienceBlock(ConstantInt.of(0),
            BlockBehaviour.Properties.ofLegacyCopy(BYGONESTONE_COPPER_ORE.get()).mapColor(MapColor.DEEPSLATE).strength(4.5F, 3.0F).sound(SoundType.DEEPSLATE)));

    public static final Supplier<Block> BYSTONE = registerBlock("bystone", () ->
            new Block(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_BROWN).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(0.8F)));

    public static final Supplier<Block> BYSLATE = registerBlock("byslate", () ->
            new Block(BlockBehaviour.Properties.of().mapColor(MapColor.DEEPSLATE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 6.0F).sound(SoundType.DEEPSLATE)));

    public static final Supplier<Block> COBBLED_BYSTONE = registerBlock("cobbled_bystone", () ->
            new Block(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_BROWN).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(0.4F)));
    public static final Supplier<Block> COBBLED_BYSTONE_STAIRS = registerBlock("cobbled_bystone_stairs", () ->
            new StairBlock(BGBlocks.COBBLED_BYSTONE.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.QUARTZ_STAIRS).strength(2.0f)));

    public static final Supplier<Block> COBBLED_BYSTONE_SLAB = registerBlock("cobbled_bystone_slab", () ->
            new SlabBlock(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_BROWN).instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.STONE).ignitedByLava()));

    public static final Supplier<Block> COBBLED_BYSTONE_WALL = registerBlock("cobbled_bystone_wall", () ->
            new WallBlock(BlockBehaviour.Properties.ofFullCopy(BGBlocks.COBBLED_BYSTONE.get()).strength(2.0f)));
    public static final Supplier<Block> COBBLED_BYSLATE = registerBlock("cobbled_byslate", () ->
            new Block(BlockBehaviour.Properties.of().mapColor(MapColor.DEEPSLATE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.0F, 2.0F).sound(SoundType.DEEPSLATE)));

    public static final Supplier<Block> COBBLED_BYSLATE_STAIRS = registerBlock("cobbled_byslate_stairs", () ->
            new StairBlock(BGBlocks.COBBLED_BYSLATE.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.QUARTZ_STAIRS).strength(2.0f)));

    public static final Supplier<Block> COBBLED_BYSLATE_SLAB = registerBlock("cobbled_byslate_slab", () ->
            new SlabBlock(BlockBehaviour.Properties.ofFullCopy(BGBlocks.COBBLED_BYSLATE.get()).strength(2.0f)));

    public static final Supplier<Block> COBBLED_BYSLATE_WALL = registerBlock("cobbled_byslate_wall", () ->
            new WallBlock(BlockBehaviour.Properties.ofFullCopy(BGBlocks.COBBLED_BYSLATE.get()).strength(2.0f)));
    
    public static final Supplier<Block> CLOUD = registerBlock("cloud", () ->
            new HalfTransparentBlock(BlockBehaviour.Properties.of().mapColor(MapColor.QUARTZ).strength(0.001F).friction(0.989F)
                    .sound(SoundType.WOOL).noOcclusion().isValidSpawn(Blocks::never).isRedstoneConductor(Blocks::never).isSuffocating(Blocks::never).isViewBlocking(Blocks::never)));


    // Start of the Ancient Forests unique Blocks

    public static  final Supplier<Block> TALL_GRASS = registerBlockWithoutBlockItem("tall_grass", () -> new UpsidedownTallPlantBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).
            replaceable().noCollission().instabreak().sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XZ).ignitedByLava().pushReaction(PushReaction.DESTROY)));
    public static  final Supplier<Block> SHORT_GRASS = registerBlock("under_grass", () -> new UpsidedownShortPlantBlock(BlockBehaviour.Properties.of()
            .mapColor(MapColor.PLANT).replaceable().noCollission().instabreak().sound(SoundType.GRASS)
            .offsetType(BlockBehaviour.OffsetType.XYZ).ignitedByLava().pushReaction(PushReaction.DESTROY)));


    public static final Supplier<Block> CAVE_VINES = registerBlockWithoutBlockItem("cave_vines", () -> new AncientCaveVinesHeadBlock(BlockBehaviour.Properties.of()
            .mapColor(MapColor.PLANT).randomTicks().noCollission().lightLevel(CaveVines.emission(5)).instabreak()
            .sound(SoundType.CAVE_VINES).pushReaction(PushReaction.DESTROY)));
    public static final Supplier<Block> CAVE_VINES_PLANT = registerBlockWithoutBlockItem("cave_vines_plant", () -> new AncientCaveVinesBodyBlock(BlockBehaviour.Properties.of()
            .mapColor(MapColor.PLANT).noCollission().lightLevel(CaveVines.emission(5)).instabreak().sound(SoundType.CAVE_VINES)
            .pushReaction(PushReaction.DESTROY)));

    public static  final Supplier<Block> MONTSECHIA = registerBlock("montsechia", () -> new FlowerBlock(MobEffects.UNLUCK, 12.0F,BlockBehaviour.Properties.of().mapColor(MapColor.PLANT)
            .lightLevel((state) -> {
                return 12;
            }).noCollission().instabreak().sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XZ).pushReaction(PushReaction.DESTROY)));

    public static  final Supplier<Block> POTTED_MONTSECHIA = registerBlockWithoutBlockItem("potted_montsechia", () -> createFlowerPotBlock(MONTSECHIA.get()));
    public static  final Supplier<Block> SAGARIA = registerBlock("sagaria", () -> new FlowerBlock(MobEffects.LUCK, 12.0F,BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noCollission().lightLevel((state) -> {
     return 12;
    }).instabreak().sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XZ).pushReaction(PushReaction.DESTROY)));

    public static  final Supplier<Block> POTTED_SAGARIA = registerBlockWithoutBlockItem("potted_sagaria", () -> createFlowerPotBlock(SAGARIA.get()));

    public static  final Supplier<Block> ROSE = registerBlock("rose", () -> new FlowerBlock(MobEffects.LUCK, 12.0F,BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noCollission().lightLevel((state) -> {
        return 0;
    }).instabreak().sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XZ).pushReaction(PushReaction.DESTROY)));

    public static  final Supplier<Block> POTTED_ROSE = registerBlockWithoutBlockItem("potted_rose", () -> createFlowerPotBlock(ROSE.get()));


    public static  final Supplier<Block> RAFFLESIA = registerBlock("rafflesia", () -> new RafflesiaBlock(BlockBehaviour.Properties.of().mapColor(MapColor.CRIMSON_STEM).instabreak().noCollission().lightLevel((state) -> {
     return 12;
    }).sound(SoundType.SPORE_BLOSSOM).pushReaction(PushReaction.DESTROY)));

    public static final Supplier<Block> GOURD_VINE = registerBlock("gourd_vine", () -> new GourdVineBlock(BlockBehaviour.Properties.of().replaceable().noCollission()
            .randomTicks().instabreak().sound(SoundType.VINE).ignitedByLava().pushReaction(PushReaction.DESTROY)));

    public static final Supplier<Block> GOURD_LANTERN_VERDANT = registerBlock("glow_gourd_verdant", () -> new GourdLanternBlock(BlockBehaviour.Properties.of().mapColor(MapColor.GRASS)
            .forceSolidOn().instabreak().strength(0.1F).sound(SoundType.SHROOMLIGHT).lightLevel((state) -> {
                return 15;
            }).noOcclusion().pushReaction(PushReaction.DESTROY)));

    public static final Supplier<Block> GOURD_LANTERN_BEIGE = registerBlock("glow_gourd_beige", () -> new GourdLanternBlock(BlockBehaviour.Properties.of().mapColor(MapColor.SAND)
            .forceSolidOn().instabreak().strength(0.1F).sound(SoundType.SHROOMLIGHT).lightLevel((state) -> {
                return 15;
            }).noOcclusion().pushReaction(PushReaction.DESTROY)));

    public static final Supplier<Block> GOURD_LANTERN_MUAVE = registerBlock("glow_gourd_muave", () -> new GourdLanternBlock(BlockBehaviour.Properties.of().mapColor(MapColor.ICE)
            .forceSolidOn().instabreak().strength(0.1F).sound(SoundType.SHROOMLIGHT).lightLevel((state) -> {
                return 15;
            }).noOcclusion().pushReaction(PushReaction.DESTROY)));

    public static final Supplier<Block> GOURD_DANGO = registerBlockWithoutBlockItem("glow_gourd_dango", () -> new GourdDangoBlock(BlockBehaviour.Properties.of().instabreak()
            .sound(SoundType.BAMBOO).lightLevel((state) -> {
                return 15;
            })));

    public static final Supplier<Block> GOURD_DANGO_WALL = registerBlockWithoutBlockItem("glow_gourd_dango_wall", () -> new GourdDangoWallBlock(BlockBehaviour.Properties.of().instabreak()
            .sound(SoundType.BAMBOO).lightLevel((state) -> {
                return 15;
            })));

    public static final Supplier<Block> BIG_WHIRLIWEED = registerBlock("big_whirliweed", () -> new TallFlowerBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noCollission().instabreak().sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XZ).ignitedByLava().pushReaction(PushReaction.DESTROY)));
    public static final Supplier<Block> WHIRLIWEED = registerBlock("whirliweed", () -> new FlowerBlock(MobEffects.POISON, 12.0F,BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noCollission().instabreak().sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XZ).pushReaction(PushReaction.DESTROY)));

    public static final Supplier<Block> CLAYSTONE = registerBlock("claystone", () -> new Block(BlockBehaviour.Properties.ofLegacyCopy(DIRT).strength(1.0F, 3.0F).sound(SoundType.PACKED_MUD)));
    public static final Supplier<Block> COARSE_CLAYSTONE = registerBlock("coarse_claystone", () -> new Block(BlockBehaviour.Properties.ofLegacyCopy(DIRT).strength(1.0F, 3.0F).sound(SoundType.PACKED_MUD)));
    public static final Supplier<Block> CLAYSTONE_BRICKS = registerBlock("claystone_bricks", () -> new Block(BlockBehaviour.Properties.ofLegacyCopy(DIRT).strength(1.0F, 3.0F).sound(SoundType.PACKED_MUD)));

    public static final Supplier<Block> CLAYSTONE_BRICKS_STAIRS = registerBlock("claystone_bricks_stairs", () ->
            new StairBlock(BGBlocks.CLAYSTONE_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.QUARTZ_STAIRS).strength(2.0f)));

    public static final Supplier<Block> CLAYSTONE_BRICKS_SLAB = registerBlock("claystone_bricks_slab", () ->
            new SlabBlock(BlockBehaviour.Properties.ofFullCopy(BGBlocks.CLAYSTONE_BRICKS.get()).strength(2.0f)));

    public static final Supplier<Block> CLAYSTONE_BRICKS_WALL = registerBlock("claystone_bricks_wall", () ->
            new WallBlock(BlockBehaviour.Properties.ofFullCopy(BGBlocks.CLAYSTONE_BRICKS.get()).strength(2.0f)));

    public static final Supplier<Block> MOSSY_CLAYSTONE = registerBlock("mossy_claystone", () -> new MossyClaystoneBlock(BlockBehaviour.Properties.of().mapColor(MapColor.GRASS).randomTicks().strength(0.6F).sound(SoundType.GRASS)));
    public static final Supplier<Block> GRASSY_CLAYSTONE = registerBlockWithoutBlockItem("grassy_claystone", () -> new MossyClaystoneBlock(BlockBehaviour.Properties.of().mapColor(MapColor.GRASS).randomTicks().strength(0.6F).sound(SoundType.GRASS)));
    public static final Supplier<Block> ANCIENT_ROOTS = registerBlock("ancient_roots", () ->
            new AncientRootBlock(BlockBehaviour.Properties.of().mapColor(MapColor.GLOW_LICHEN).instrument(NoteBlockInstrument.BASS).strength(0.7F).sound(SoundType.MANGROVE_ROOTS).noOcclusion().isSuffocating(Blocks::never).isViewBlocking(Blocks::never).noOcclusion().ignitedByLava()));

    public static final Supplier<Block> ANCIENT_VINE = registerBlock("ancient_vines", () ->
            new VineBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).replaceable().noCollission().randomTicks().strength(0.2F).sound(SoundType.VINE).ignitedByLava().pushReaction(PushReaction.DESTROY)));

    public static final Supplier<Block> ANCIENT_SAPLING = registerBlock("ancient_sapling", () -> new AncientSaplingBlock(BGTreeGrowers.ANCIENT_TREE, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SAPLING)));
    public static final Supplier<Block> ANCIENT_LOG = registerBlock("ancient_log", () ->
            new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG).strength(2.0f)));
    public static final Supplier<Block> ANCIENT_LEAVES = registerBlock("ancient_leaves", () ->
            new AncientLeavesBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES)));
    public static final Supplier<Block> ANCIENT_WOOD = registerBlock("ancient_wood", () ->
            new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD).strength(2.0f)));

    public static final Supplier<Block> STRIPPED_ANCIENT_LOG = registerBlock("stripped_ancient_log", () ->
            new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_LOG).strength(2.0f)));

    public static final Supplier<Block> STRIPPED_ANCIENT_WOOD = registerBlock("stripped_ancient_wood", () ->
            new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_WOOD).strength(2.0f)));

    public static final Supplier<Block> ANCIENT_PLANKS = registerBlock("ancient_planks", () ->
            new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(2.0f)));

    public static final Supplier<Block> ANCIENT_STAIRS = registerBlock("ancient_stairs", () ->
            new StairBlock(ANCIENT_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_STAIRS).strength(2.0f)));

    public static final Supplier<Block> ANCIENT_SLAB = registerBlock("ancient_slab", () ->
            new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SLAB).strength(2.0f)));

    public static final Supplier<Block> ANCIENT_FENCE = registerBlock("ancient_fence", () ->
            new FenceBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_FENCE).strength(2.0f)));

    public static final Supplier<Block> ANCIENT_FENCE_GATE = registerBlock("ancient_fence_gate", () ->
            new FenceGateBlock(JamiesModWoodType.ANCIENT, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_FENCE_GATE).strength(2.0f)));

    public static final Supplier<Block> ANCIENT_DOOR = registerBlock("ancient_door", () ->
            new DoorBlock(JamiesModBlockSetType.ANCIENT, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_DOOR).strength(2.0f)));

    public static final Supplier<Block> ANCIENT_TRAPDOOR = registerBlock("ancient_trapdoor", () ->
            new TrapDoorBlock(JamiesModBlockSetType.ANCIENT, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_TRAPDOOR).strength(2.0f)));

    public static final Supplier<Block> ANCIENT_PRESSURE_PLATE = registerBlock("ancient_pressure_plate", () ->
            new PressurePlateBlock(JamiesModBlockSetType.ANCIENT, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PRESSURE_PLATE)));
    public static final Supplier<Block> ANCIENT_BUTTON = registerBlock("ancient_button", () ->
            new ButtonBlock(JamiesModBlockSetType.ANCIENT, 30, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_BUTTON)));

    public static final Supplier<Block> ANCIENT_SIGN = registerBlockWithoutBlockItem("ancient_sign", () ->
            new StandingSignBlock(JamiesModWoodType.ANCIENT, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SIGN).strength(1.0f).forceSolidOn()));

    public static final Supplier<Block> ANCIENT_WALL_SIGN = registerBlockWithoutBlockItem("ancient_wall_sign", () ->
            new WallSignBlock(JamiesModWoodType.ANCIENT, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WALL_SIGN).strength(1.0f).dropsLike(BGBlocks.ANCIENT_SIGN.get()).forceSolidOn()));

    public static final Supplier<Block> ANCIENT_HANGING_SIGN = registerBlockWithoutBlockItem("ancient_hanging_sign", () ->
            new CeilingHangingSignBlock(JamiesModWoodType.ANCIENT, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_HANGING_SIGN).strength(1.0f).forceSolidOn()));

    public static final Supplier<Block> ANCIENT_WALL_HANGING_SIGN = registerBlockWithoutBlockItem("ancient_wall_hanging_sign", () ->
            new WallHangingSignBlock(JamiesModWoodType.ANCIENT, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WALL_HANGING_SIGN).strength(1.0f).dropsLike(ANCIENT_HANGING_SIGN.get()).forceSolidOn()));

    public static final Supplier<Block> ALPHA_MOSS_CARPET = registerBlock("alpha_moss_carpet", () ->
            new CarpetBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GREEN).strength(0.1F).sound(SoundType.MOSS_CARPET).pushReaction(PushReaction.DESTROY)));
    public static final Supplier<Block> ALPHA_MOSS_BLOCK = registerBlock("alpha_moss_block", () ->
            new AlphaMossBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GREEN).strength(0.1F).sound(SoundType.MOSS).pushReaction(PushReaction.DESTROY)));
    public static final Supplier<Block> ALPHA_MOSSY_CLAYSTONE = registerBlock("alpha_mossy_claystone", () -> new GrassBlock(BlockBehaviour.Properties.of().mapColor(MapColor.GRASS).randomTicks().strength(0.6F).sound(SoundType.GRASS)));

    public static final Supplier<Block> CASTER = registerBlock("caster", () ->
            new CasterBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WATER).instrument(NoteBlockInstrument.XYLOPHONE).requiresCorrectToolForDrops()
                    .strength(2.8F)));

// Start of the Primordial Ocean blockset

    public static final Supplier<Block> BLUE_ALGAE = registerBlock("blue_algae", () -> new BlueAlgueBlock((SimpleParticleType) BGParticleTypes.ALGAE_BLOOM, BlockBehaviour.Properties.of().mapColor(MapColor.GLOW_LICHEN)
            .replaceable().noCollission().strength(0.2F).sound(SoundType.GLOW_LICHEN).lightLevel((state) -> {
                return 15;
            })
            .ignitedByLava().pushReaction(PushReaction.DESTROY)));

    public static final Supplier<Block> OCEANSTONE = registerBlock("oceanstone", () ->
            new Block(BlockBehaviour.Properties.of().mapColor(MapColor.WATER).instrument(NoteBlockInstrument.XYLOPHONE).requiresCorrectToolForDrops()
                    .strength(0.8F)));

    public static final Supplier<Block> PRIMORDIAL_SAND = registerBlock("primordial_sand", () ->
            new Block(BlockBehaviour.Properties.of().mapColor(MapColor.GRASS).sound(SoundType.SAND).instrument(NoteBlockInstrument.XYLOPHONE).requiresCorrectToolForDrops()
                    .strength(0.1F)));

    public static final Supplier<Block> GLOW_GRAVEL = registerBlock("glow_gravel", () ->
            new Block(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_CYAN).sound(SoundType.GRAVEL).instrument(NoteBlockInstrument.XYLOPHONE).requiresCorrectToolForDrops()
                    .strength(0.1F).lightLevel((state) -> {
                        return 4;
                    })));
    public static final Supplier<Block> VERDIGRIS_BLOCK = registerBlock("verdigris_block", () ->
            new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.NETHERITE_BLOCK).mapColor(MapColor.TERRACOTTA_GREEN).strength(20.0F, 600.0F)));
    public static final Supplier<Block> VERDIGRIS_SCRAP_BLOCK = registerBlock("verdigris_scrap_block", () ->
            new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.ANCIENT_DEBRIS).mapColor(MapColor.TERRACOTTA_GREEN).strength(20.0F, 600.0F)));

    public static final Supplier<Block> MALACHITE = registerBlock("malachite", () ->
            new Block(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_GREEN).sound(SoundType.AMETHYST).instrument(NoteBlockInstrument.XYLOPHONE).requiresCorrectToolForDrops()
                    .strength(0.8F)));
    public static final Supplier<Block> MALACHITE_CHISELED = registerBlock("malachite_chiseled", () ->
            new RotatedPillarBlock(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_GREEN).sound(SoundType.AMETHYST).instrument(NoteBlockInstrument.XYLOPHONE).requiresCorrectToolForDrops()
                    .strength(0.8F)));
    public static final Supplier<Block> MALACHITE_PILLAR = registerBlock("malachite_pillar", () ->
            new RotatedPillarBlock(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_GREEN).sound(SoundType.AMETHYST).instrument(NoteBlockInstrument.XYLOPHONE).requiresCorrectToolForDrops()
                    .strength(0.8F)));
    public static final Supplier<Block> MALACHITE_TILE = registerBlock("malachite_tile", () ->
            new Block(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_GREEN).sound(SoundType.AMETHYST).instrument(NoteBlockInstrument.XYLOPHONE).requiresCorrectToolForDrops()
                    .strength(0.8F)));
    public static final Supplier<Block> MALACHITE_DOOR = registerBlock("malachite_door", () ->
            new DoorBlock(JamiesModBlockSetType.ANCIENT, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_DOOR).sound(SoundType.AMETHYST).strength(2.0f)));

    public static final Supplier<Block> MALACHITE_STAIRS = registerBlock("malachite_stairs", () ->
            new StairBlock(BGBlocks.MALACHITE.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.QUARTZ_STAIRS).strength(2.0f)));

    public static final Supplier<Block> MALACHITE_SLAB = registerBlock("malachite_slab", () ->
            new SlabBlock(BlockBehaviour.Properties.ofFullCopy(BGBlocks.MALACHITE.get()).strength(2.0f)));

    public static final Supplier<Block> MALACHITE_WALL = registerBlock("malachite_wall", () ->
            new WallBlock(BlockBehaviour.Properties.ofFullCopy(BGBlocks.MALACHITE.get()).strength(2.0f)));

    public static final Supplier<Block> MALACHITE_TILE_STAIRS = registerBlock("malachite_tile_stairs", () ->
            new StairBlock(BGBlocks.MALACHITE.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.QUARTZ_STAIRS).strength(2.0f)));

    public static final Supplier<Block> MALACHITE_TILE_SLAB = registerBlock("malachite_tile_slab", () ->
            new SlabBlock(BlockBehaviour.Properties.ofFullCopy(BGBlocks.MALACHITE.get()).strength(2.0f)));

    public static final Supplier<Block> MALACHITE_TILE_WALL = registerBlock("malachite_tile_wall", () ->
            new WallBlock(BlockBehaviour.Properties.ofFullCopy(BGBlocks.MALACHITE.get()).strength(2.0f)));

    public static final Supplier<Block> PRIMORDIAL_VENT = registerBlock("primordial_vent", () ->
                                    new PrimordialVentBlock(true, BlockBehaviour.Properties.of()
    .mapColor(MapColor.PODZOL).instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.SUSPICIOUS_GRAVEL).lightLevel((state) -> {
                                                return 1;
                                            }).noOcclusion().ignitedByLava()));

    public static final Supplier<Block> PRIMORDIAL_VENTSTONE = registerBlock("primordial_ventstone", () ->
            new Block(BlockBehaviour.Properties.of().mapColor(MapColor.WATER).instrument(NoteBlockInstrument.XYLOPHONE).requiresCorrectToolForDrops()
                    .strength(0.8F)));

    public static final Supplier<Block> CRINOID = registerBlock("crinoid", () -> new CrinoidBlock(BlockBehaviour.Properties.of()
            .mapColor(MapColor.WATER).replaceable().noCollission().instabreak().sound(SoundType.WET_GRASS)
            .pushReaction(PushReaction.DESTROY)));

    public static final Supplier<Block> PRIMORDIAL_URCHIN = registerBlock("primordial_urchin", () -> new PrimordialUrchinBlock(BlockBehaviour.Properties.of()
            .mapColor(MapColor.WATER).replaceable().noCollission().instabreak().sound(SoundType.WET_GRASS)
            .pushReaction(PushReaction.DESTROY)));

    public static final Supplier<Block> DEAD_ORANGE_CORAL_BLOCK = registerBlock("dead_rugosa_coral_block", () -> new Block(
            BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GRAY).forceSolidOn().instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5F,
                    6.0F)));
    public static final Supplier<Block> ORANGE_CORAL_BLOCK = registerBlock("rugosa_coral_block", () -> new CoralBlock(DEAD_ORANGE_CORAL_BLOCK.get(),
            BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5F,
                    6.0F).sound(SoundType.CORAL_BLOCK)));
    public static final Supplier<Block> DEAD_ORANGE_CORAL = registerBlock("dead_rugosa_coral", () -> new BaseCoralPlantBlock(
            BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GRAY).forceSolidOn().instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops()
                    .noCollission().instabreak()));
    public static final Supplier<Block> ORANGE_CORAL = registerBlock("rugosa_coral", () -> new CoralPlantBlock(DEAD_ORANGE_CORAL.get(),
            BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).noCollission().instabreak().sound(SoundType.WET_GRASS)
                    .pushReaction(PushReaction.DESTROY)));

    public static final Supplier<Block> DEAD_ORANGE_CORAL_FAN = registerBlock("dead_rugosa_coral_fan", () -> new BaseCoralFanBlock(
            BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GRAY).forceSolidOn().instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops()
                    .noCollission().instabreak()));
    public static final Supplier<Block> ORANGE_CORAL_FAN = registerBlock("rugosa_coral_fan", () -> new CoralFanBlock(DEAD_ORANGE_CORAL_FAN.get(),
            BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_PINK).noCollission().instabreak().sound(SoundType.WET_GRASS)
                    .pushReaction(PushReaction.DESTROY)));
    public static final Supplier<Block> DEAD_ORANGE_CORAL_WALL_FAN = registerBlockWithoutBlockItem("dead_rugosa_coral_wall_fan", () -> new BaseCoralWallFanBlock(
            BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GRAY).forceSolidOn().instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops()
                    .noCollission().instabreak().dropsLike(DEAD_ORANGE_CORAL_FAN.get())));
    public static final Supplier<Block> ORANGE_CORAL_WALL_FAN = registerBlockWithoutBlockItem("rugosa_coral_wall_fan", () -> new CoralWallFanBlock(DEAD_ORANGE_CORAL_WALL_FAN.get(),
            BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_PINK).noCollission().instabreak().sound(SoundType.WET_GRASS)
                    .dropsLike(ORANGE_CORAL_FAN.get()).pushReaction(PushReaction.DESTROY)));

    public static final Supplier<Block> DEAD_BLUE_CORAL_BLOCK = registerBlock("dead_tabulata_coral_block", () -> new Block(
            BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GRAY).forceSolidOn().instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5F,
                    6.0F)));
    public static final Supplier<Block> BLUE_CORAL_BLOCK = registerBlock("tabulata_coral_block", () -> new CoralBlock(DEAD_BLUE_CORAL_BLOCK.get(),
            BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_PINK).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5F,
                    6.0F).sound(SoundType.CORAL_BLOCK)));
    public static final Supplier<Block> DEAD_BLUE_CORAL = registerBlock("dead_tabulata_coral", () -> new BaseCoralPlantBlock(
            BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GRAY).forceSolidOn().instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops()
                    .noCollission().instabreak()));
    public static final Supplier<Block> BLUE_CORAL = registerBlock("tabulata_coral", () -> new CoralPlantBlock(DEAD_BLUE_CORAL.get(),
            BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_PINK).noCollission().instabreak().sound(SoundType.WET_GRASS)
                    .pushReaction(PushReaction.DESTROY)));

    public static final Supplier<Block> DEAD_BLUE_CORAL_FAN = registerBlock("dead_tabulata_coral_fan", () -> new BaseCoralFanBlock(
            BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GRAY).forceSolidOn().instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops()
                    .noCollission().instabreak()));
    public static final Supplier<Block> BLUE_CORAL_FAN = registerBlock("tabulata_coral_fan", () -> new CoralFanBlock(DEAD_BLUE_CORAL_FAN.get(),
            BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLUE).noCollission().instabreak().sound(SoundType.WET_GRASS)
                    .pushReaction(PushReaction.DESTROY)));
    public static final Supplier<Block> DEAD_BLUE_CORAL_WALL_FAN = registerBlockWithoutBlockItem("dead_tabulata_coral_wall_fan", () -> new BaseCoralWallFanBlock(
            BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GRAY).forceSolidOn().instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops()
                    .noCollission().instabreak().dropsLike(DEAD_BLUE_CORAL_FAN.get())));
    public static final Supplier<Block> BLUE_CORAL_WALL_FAN = registerBlockWithoutBlockItem("tabulata_coral_wall_fan", () -> new CoralWallFanBlock(DEAD_BLUE_CORAL_WALL_FAN.get(),
            BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLUE).noCollission().instabreak().sound(SoundType.WET_GRASS)
                    .dropsLike(BLUE_CORAL_FAN.get()).pushReaction(PushReaction.DESTROY)));


    public static final Supplier<Block> CHARNIA = registerBlock("charnia", () -> new CharniaBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WATER)
            .replaceable().noCollission().instabreak().sound(SoundType.WET_GRASS).offsetType(BlockBehaviour.OffsetType.XZ).pushReaction(PushReaction.DESTROY)));


// Start of Blemish biome blocksets!
    public static final Supplier<Block> BLEMISH = registerBlock("blemish", () -> new BlemishBlock(BlockBehaviour.Properties.of().mapColor(MapColor.ICE).instabreak().sound(SoundType.WET_GRASS)));
    public static final Supplier<Block> BLEMISH_CATALYST = registerBlock("blemish_catalyst", () -> new BlemishCatalystBlock(BlockBehaviour.Properties.of().mapColor(MapColor.ICE).instabreak().sound(SoundType.WET_GRASS)));
    public static final Supplier<Block> BLEMISH_VEIN = registerBlock("blemish_vein", () -> new BlemishVeinBlock(BlockBehaviour.Properties.of().mapColor(MapColor.ICE).forceSolidOn().noCollission().strength(0.2F).sound(SoundType.SCULK_VEIN).pushReaction(PushReaction.DESTROY)));


// Start of Amber Desert blocksets!
    public static final Supplier<Block> AMBER_SAND = registerBlock("amber_sand", () ->
        new Block(BlockBehaviour.Properties.of().mapColor(MapColor.RAW_IRON).strength(0.35F)
                .sound(SoundType.GRAVEL)));
    
    public static final Supplier<Block> AMBER_SANDSTONE = registerBlock("amber_sandstone", () ->
            new Block(BlockBehaviour.Properties.of().mapColor(MapColor.RAW_IRON).strength(0.99F)
                    .sound(SoundType.STONE)));

    public static final Supplier<Block> CHISELED_AMBER_SANDSTONE = registerBlock("chiseled_amber_sandstone", () ->
            new Block(BlockBehaviour.Properties.of().mapColor(MapColor.RAW_IRON).strength(0.99F)
                    .sound(SoundType.STONE)));

    public static final Supplier<Block> SMOOTH_AMBER_SANDSTONE = registerBlock("smooth_amber_sandstone", () ->
            new Block(BlockBehaviour.Properties.of().mapColor(MapColor.RAW_IRON).strength(0.99F)
                    .sound(SoundType.STONE)));

    public static final Supplier<Block> SMOOTH_AMBER_SANDSTONE_STAIRS = registerBlock("smooth_amber_sandstone_stairs", () ->
            new StairBlock(BGBlocks.SMOOTH_AMBER_SANDSTONE.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.QUARTZ_STAIRS).strength(2.0f)));

    public static final Supplier<Block> SMOOTH_AMBER_SANDSTONE_SLAB = registerBlock("smooth_amber_sandstone_slab", () ->
            new SlabBlock(BlockBehaviour.Properties.ofFullCopy(BGBlocks.SMOOTH_AMBER_SANDSTONE.get()).strength(2.0f)));

    public static final Supplier<Block> CUT_AMBER_SANDSTONE = registerBlock("cut_amber_sandstone", () ->
            new Block(BlockBehaviour.Properties.of().mapColor(MapColor.RAW_IRON).strength(0.99F)
                    .sound(SoundType.STONE)));

    public static final Supplier<Block> CUT_AMBER_SANDSTONE_SLAB = registerBlock("cut_amber_sandstone_slab", () ->
            new SlabBlock(BlockBehaviour.Properties.ofFullCopy(BGBlocks.CUT_AMBER_SANDSTONE.get()).strength(2.0f)));


    public static final Supplier<Block> AMBER_SANDSTONE_STAIRS = registerBlock("amber_sandstone_stairs", () ->
            new StairBlock(BGBlocks.AMBER_SANDSTONE.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.QUARTZ_STAIRS).strength(2.0f)));

    public static final Supplier<Block> AMBER_SANDSTONE_SLAB = registerBlock("amber_sandstone_slab", () ->
            new SlabBlock(BlockBehaviour.Properties.ofFullCopy(BGBlocks.AMBER_SANDSTONE.get()).strength(2.0f)));

    public static final Supplier<Block> AMBER_SANDSTONE_WALL = registerBlock("amber_sandstone_wall", () ->
            new WallBlock(BlockBehaviour.Properties.ofFullCopy(BGBlocks.AMBER_SANDSTONE.get()).strength(2.0f)));


    public static final Supplier<Block> AMBER_CLUMP = registerBlock("amber_clump", () ->
            new AmberClumpBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).replaceable().noCollission().randomTicks().strength(0.2F).sound(SoundType.VINE).ignitedByLava().pushReaction(PushReaction.DESTROY).lightLevel((state) -> {
                return 10;
            })));


    public static final Supplier<Block> AMBER = registerBlock("amber", () ->
        new HalfTransparentBlock(BlockBehaviour.Properties.of().mapColor(MapColor.SAND).strength(1.0F)
                .sound(SoundType.GLASS).noOcclusion().isValidSpawn(Blocks::never).isRedstoneConductor(Blocks::never).isSuffocating(Blocks::never)
                .isViewBlocking(Blocks::never).lightLevel((state) -> {
                    return 4;
                })));

    public static final Supplier<Block> AMBER_BRICKS = registerBlock("amber_bricks", () -> new Block(
            BlockBehaviour.Properties.of().sound(SoundType.GLASS).mapColor(MapColor.COLOR_ORANGE).forceSolidOn().instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5F,
                    6.0F).lightLevel((state) -> {
                return 4;
            })));
    public static final Supplier<Block> FLOWING_AMBER = registerBlock("glowing_amber", () -> new Block(
            BlockBehaviour.Properties.of().sound(SoundType.GLASS).mapColor(MapColor.COLOR_ORANGE).forceSolidOn().instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5F,
                    6.0F).lightLevel((state) -> {
                return 5;
            })));
    public static final Supplier<Block> COBBLED_AMBER = registerBlock("cobbled_amber", () -> new Block(
            BlockBehaviour.Properties.of().sound(SoundType.GLASS).mapColor(MapColor.COLOR_ORANGE).forceSolidOn().instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5F,
                    6.0F).lightLevel((state) -> {
                return 2;
            })));

    public static final Supplier<Block> AMBERSTONE = registerBlock("amberstone", () -> new Block(
            BlockBehaviour.Properties.of().sound(SoundType.GLASS).mapColor(MapColor.COLOR_ORANGE).forceSolidOn().instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5F,
                    4.0F).lightLevel((state) -> {
                return 3;
            })));

    public static final Supplier<Block> COBBLED_AMBERSTONE = registerBlock("cobbled_amberstone", () -> new Block(
            BlockBehaviour.Properties.of().sound(SoundType.GLASS).mapColor(MapColor.COLOR_ORANGE).forceSolidOn().instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5F,
                    3.0F).lightLevel((state) -> {
                return 2;
            })));

    public static final Supplier<Block> POINTED_AMBER = registerBlock("pointed_amber", () -> new PointedAmberBlock2(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_BROWN).forceSolidOn()
            .instrument(NoteBlockInstrument.BASEDRUM).noOcclusion().sound(SoundType.POINTED_DRIPSTONE).randomTicks()
            .strength(1.5F, 3.0F).dynamicShape()
            .offsetType(BlockBehaviour.OffsetType.XZ).pushReaction(PushReaction.DESTROY).isRedstoneConductor(Blocks::never)));

    public static final Supplier<Block> CREOSOTE = registerBlock("creosote", () -> new CreosoteBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).replaceable()
            .noCollission().instabreak().sound(SoundType.GRASS).ignitedByLava().pushReaction(PushReaction.DESTROY)));
    public static final Supplier<Block> CREOSOTE_SPROUTS = registerBlock("creosote_sprouts", () -> new CreosoteSproutsBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).replaceable()
            .noCollission().instabreak().sound(SoundType.GRASS).ignitedByLava().pushReaction(PushReaction.DESTROY).lightLevel((state) -> {
                return 5;
            })));

// Farming / Agriculture blocksets

    public static final Supplier<Block> CLAYSTONE_FARMLAND = registerBlockWithoutBlockItem("claystone_farmland", () -> new ClaystoneFarmlandBlock(BlockBehaviour.Properties.of()
            .mapColor(MapColor.DIRT).randomTicks().strength(0.6F).sound(SoundType.GRAVEL)
            .isViewBlocking(Blocks::always).isSuffocating(Blocks::always)));

    public static final Supplier<Block> AMARANTH_CROP = registerBlockWithoutBlockItem("amaranth_crop", () -> new AmaranthCropBlock(BlockBehaviour.Properties.of()
            .mapColor(MapColor.PLANT).noCollission().randomTicks().instabreak().sound(SoundType.CROP)
            .pushReaction(PushReaction.DESTROY)));

    public static final Supplier<Block> PLAGA_CROP = registerBlockWithoutBlockItem("plaga_crop", () -> new PlagaCropBlock(BlockBehaviour.Properties.of()
            .mapColor(MapColor.PLANT).noCollission().randomTicks().instabreak().sound(SoundType.CROP)
            .pushReaction(PushReaction.DESTROY)));

    public static final Supplier<Block> PLAGA = registerBlockWithoutBlockItem("plaga", () -> new GourdDangoBlock(BlockBehaviour.Properties.of().instabreak()
            .sound(SoundType.BAMBOO).lightLevel((state) -> {
                return 5;
            })));

    public static final Supplier<Block> PLAGA_WALL = registerBlockWithoutBlockItem("plaga_wall", () -> new GourdDangoWallBlock(BlockBehaviour.Properties.of().instabreak()
            .sound(SoundType.BAMBOO).lightLevel((state) -> {
                return 5;
            })));

    public static final Supplier<Block> CHANTRELLE = registerBlockWithoutBlockItem("chantrelle", () -> new ChantrelleCropBlock(BlockBehaviour.Properties.of()
            .mapColor(MapColor.PLANT).noCollission().randomTicks().instabreak().sound(SoundType.CROP)
            .pushReaction(PushReaction.DESTROY).lightLevel((state) -> {
        return 6;
    })));


    public static final Supplier<Block> AMARANTH_BLOCK = registerBlock("bale_block", () -> new HayBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_PURPLE).instrument(NoteBlockInstrument.BANJO).strength(0.5F).sound(SoundType.GRASS)));


    public static final Supplier<Block> SPRINKER = registerBlock("ancient_sprinkler", () -> new SprinklerBlock(BlockBehaviour.Properties.of().randomTicks().instabreak().sound(SoundType.METAL)
            .pushReaction(PushReaction.DESTROY).noOcclusion()));

    public static final Supplier<Block> BELLADONNA = registerBlock("belladonna", () -> new PinkPetalsBlock(BlockBehaviour.Properties.of().mapColor(MapColor.ICE)
            .noCollission().sound(SoundType.PINK_PETALS).pushReaction(PushReaction.DESTROY)));
    public static final Supplier<Block> COLEUS = registerBlock("coleus", () -> new PinkPetalsBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_CYAN)
            .noCollission().sound(SoundType.PINK_PETALS).pushReaction(PushReaction.DESTROY)));


// Shelf Hollows blocksets

    public static final Supplier<Block> PROTOTAXITE_STEM = registerBlock("prototaxite_stem", () ->
            new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG).strength(2.0f)));

    public static final Supplier<Block> SHELF_MYCELIUM = registerBlock("shelf_mycelium", () -> new ShelfMyceliumBlock(BlockBehaviour.Properties.of().mapColor(MapColor.GRASS).randomTicks().strength(0.6F).sound(SoundType.GRASS)));
    public static final Supplier<Block> SHELF_MOLD_BLOCK = registerBlock("shelf_mold_block", () -> new ShelfMoldBlock(BlockBehaviour.Properties.of().mapColor(MapColor.GRASS).randomTicks().strength(0.6F).sound(SoundType.GRASS)));
    public static final Supplier<Block> SHELF_MOLD = registerBlock("shelf_mold", () -> new DoublePlantBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).replaceable().noCollission().instabreak().sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XZ).ignitedByLava().pushReaction(PushReaction.DESTROY)));

    public static final Supplier<Block> SHELF_MOLD_MOSS = registerBlock("shelf_mold_moss_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.GRASS).randomTicks().strength(0.6F).sound(SoundType.GRASS)));

    public static final Supplier<Block> ORANGE_MUSHROOM_BLOCK = registerBlock("orange_mushroom_block", () -> new ShelfMushroomBlock(
            BlockBehaviour.Properties.of().sound(SoundType.WOOD).mapColor(MapColor.COLOR_ORANGE).forceSolidOn().instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5F,
                    6.0F).lightLevel((state) -> {
                return 13;
            }), 0));
    public static final Supplier<Block> PINK_MUSHROOM_BLOCK = registerBlock("pink_mushroom_block", () -> new ShelfMushroomBlock(
            BlockBehaviour.Properties.of().sound(SoundType.WOOD).mapColor(MapColor.COLOR_ORANGE).forceSolidOn().instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5F,
                    6.0F).lightLevel((state) -> {
                return 14;
            }), 1));
    public static final Supplier<Block> PURPLE_MUSHROOM_BLOCK = registerBlock("purple_mushroom_block", () -> new ShelfMushroomBlock(
            BlockBehaviour.Properties.of().sound(SoundType.WOOD).mapColor(MapColor.COLOR_ORANGE).forceSolidOn().instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5F,
                    6.0F).lightLevel((state) -> {
                return 15;
            }), 2));

    public static final Supplier<Block> ORANGE_FUNGAL_BRICKS = registerBlock("orange_fungal_bricks", () -> new ShelfMushroomBlock(
            BlockBehaviour.Properties.of().sound(SoundType.WOOD).mapColor(MapColor.COLOR_ORANGE).forceSolidOn().instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5F,
                    6.0F).lightLevel((state) -> {
                return 6;
            }), 0));

    public static final Supplier<Block> ORANGE_FUNGAL_STAIRS = registerBlock("orange_fungal_stairs", () ->
            new StairBlock(BGBlocks.ORANGE_FUNGAL_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.QUARTZ_STAIRS).strength(2.0f)));

    public static final Supplier<Block> ORANGE_FUNGAL_SLAB = registerBlock("orange_fungal_slab", () ->
            new SlabBlock(BlockBehaviour.Properties.ofFullCopy(BGBlocks.ORANGE_FUNGAL_BRICKS.get()).strength(2.0f)));

    public static final Supplier<Block> ORANGE_FUNGAL_WALL = registerBlock("orange_fungal_wall", () ->
            new WallBlock(BlockBehaviour.Properties.ofFullCopy(BGBlocks.ORANGE_FUNGAL_BRICKS.get()).strength(2.0f)));

    public static final Supplier<Block> PINK_FUNGAL_BRICKS = registerBlock("pink_fungal_bricks", () -> new ShelfMushroomBlock(
            BlockBehaviour.Properties.of().sound(SoundType.WOOD).mapColor(MapColor.COLOR_ORANGE).forceSolidOn().instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5F,
                    6.0F).lightLevel((state) -> {
                return 7;
            }), 1));

    public static final Supplier<Block> PINK_FUNGAL_STAIRS = registerBlock("pink_fungal_stairs", () ->
            new StairBlock(BGBlocks.PINK_FUNGAL_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.QUARTZ_STAIRS).strength(2.0f)));

    public static final Supplier<Block> PINK_FUNGAL_SLAB = registerBlock("pink_fungal_slab", () ->
            new SlabBlock(BlockBehaviour.Properties.ofFullCopy(BGBlocks.PINK_FUNGAL_BRICKS.get()).strength(2.0f)));

    public static final Supplier<Block> PINK_FUNGAL_WALL = registerBlock("pink_fungal_wall", () ->
            new WallBlock(BlockBehaviour.Properties.ofFullCopy(BGBlocks.PINK_FUNGAL_BRICKS.get()).strength(2.0f)));

    public static final Supplier<Block> PURPLE_FUNGAL_BRICKS = registerBlock("purple_fungal_bricks", () -> new ShelfMushroomBlock(
            BlockBehaviour.Properties.of().sound(SoundType.WOOD).mapColor(MapColor.COLOR_ORANGE).forceSolidOn().instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5F,
                    6.0F).lightLevel((state) -> {
                return 8;
            }), 2));

    public static final Supplier<Block> PURPLE_FUNGAL_STAIRS = registerBlock("purple_fungal_stairs", () ->
            new StairBlock(BGBlocks.PURPLE_FUNGAL_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.QUARTZ_STAIRS).strength(2.0f)));

    public static final Supplier<Block> PURPLE_FUNGAL_SLAB = registerBlock("purple_fungal_slab", () ->
            new SlabBlock(BlockBehaviour.Properties.ofFullCopy(BGBlocks.PURPLE_FUNGAL_BRICKS.get()).strength(2.0f)));

    public static final Supplier<Block> PURPLE_FUNGAL_WALL = registerBlock("purple_fungal_wall", () ->
            new WallBlock(BlockBehaviour.Properties.ofFullCopy(BGBlocks.PURPLE_FUNGAL_BRICKS.get()).strength(2.0f)));

    public static Supplier<Block> SHELF_ROOTS = registerBlock("shelf_roots", () -> new RootsBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_CYAN).replaceable().noCollission().instabreak().sound(SoundType.ROOTS).offsetType(BlockBehaviour.OffsetType.XZ).pushReaction(PushReaction.DESTROY)));
    public static Supplier<Block> SHELF_WART_BLOCK = registerBlockWithoutBlockItem("shelf_wart_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.WARPED_WART_BLOCK).strength(1.0F).sound(SoundType.WART_BLOCK)));
    public static Supplier<Block> SHELF_FUNGUS = registerBlock("shelf_fungus", () -> new FungusBlock(TreeFeatures.WARPED_FUNGUS_PLANTED, SHELF_MYCELIUM.get(), BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_CYAN).instabreak().noCollission().sound(SoundType.FUNGUS).pushReaction(PushReaction.DESTROY)));

    public static Supplier<Block> SHELF_SPROUTS = registerBlock("shelf_sprouts", () -> new CreosoteSproutsBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).replaceable()
            .noCollission().instabreak().sound(SoundType.GRASS).ignitedByLava().pushReaction(PushReaction.DESTROY).lightLevel((state) -> {
                return 1;
            })));

    public static  final  Supplier<Block> ORANGE_FUNGI_VINES = registerBlockWithoutBlockItem("orange_fungi_vines", () -> new ShelfOrangeFungiVinesHeadBlock(BlockBehaviour.Properties.of()
            .mapColor(MapColor.PLANT).randomTicks().noCollission().lightLevel((state) -> {
                return 1;
            }).instabreak()
            .sound(SoundType.CAVE_VINES).pushReaction(PushReaction.DESTROY)));
    public static  final  Supplier<Block> ORANGE_FUNGI_PLANT = registerBlockWithoutBlockItem("orange_fungi_vines_plant", () -> new ShelfOrangeFungiVinesBodyBlock(BlockBehaviour.Properties.of()
            .mapColor(MapColor.PLANT).noCollission().lightLevel((state) -> {
                return 1;
            }).instabreak().sound(SoundType.CAVE_VINES)
            .pushReaction(PushReaction.DESTROY)));
    public static  final  Supplier<Block> PINK_FUNGI_VINES = registerBlockWithoutBlockItem("pink_fungi_vines", () -> new ShelfPinkFungiVinesHeadBlock(BlockBehaviour.Properties.of()
            .mapColor(MapColor.PLANT).randomTicks().noCollission().lightLevel((state) -> {
                return 1;
            }).instabreak()
            .sound(SoundType.CAVE_VINES).pushReaction(PushReaction.DESTROY)));
    public static  final  Supplier<Block> PINK_FUNGI_VINES_PLANT = registerBlockWithoutBlockItem("pink_fungi_vines_plant", () -> new ShelfPinkFungiVinesBodyBlock(BlockBehaviour.Properties.of()
            .mapColor(MapColor.PLANT).noCollission().lightLevel((state) -> {
                return 1;
            }).instabreak().sound(SoundType.CAVE_VINES)
            .pushReaction(PushReaction.DESTROY)));
    public static  final  Supplier<Block> PURPLE_FUNGI_VINES = registerBlockWithoutBlockItem("purple_fungi_vines", () -> new ShelfPurpleFungiVinesHeadBlock(BlockBehaviour.Properties.of()
            .mapColor(MapColor.PLANT).randomTicks().noCollission().lightLevel((state) -> {
                return 1;
            }).instabreak()
            .sound(SoundType.CAVE_VINES).pushReaction(PushReaction.DESTROY)));
    public static  final Supplier<Block> PURPLE_FUNGI_VINES_PLANT = registerBlockWithoutBlockItem("purple_fungi_vines_plant", () -> new ShelfPurpleFungiVinesBodyBlock(BlockBehaviour.Properties.of()
            .mapColor(MapColor.PLANT).noCollission().lightLevel((state) -> {
                return 1;
            }).instabreak().sound(SoundType.CAVE_VINES)
            .pushReaction(PushReaction.DESTROY)));


    /**
     public static Block PURPLE_FUNGI_FAN = registerBlockWithoutBlockItem("purple_fungi_fan", () -> new ShelfFungiFanBlock(AbstractBlock.Settings.create().mapColor(MapColor.GRAY).solid().instrument(NoteBlockInstrument.BASEDRUM).requiresTool().noCollision().breakInstantly()));
    public static Block  PURPLE_FUNGI_WALL_FAN = registerBlockWithoutBlockItem("purple_fungi_wall_fan", () -> new ShelfFungiWallFanBlock(AbstractBlock.Settings.create().mapColor(MapColor.GRAY).solid().instrument(NoteBlockInstrument.BASEDRUM).requiresTool().noCollision().breakInstantly().dropsLike(PURPLE_FUNGI_FAN)));

    public static Block PINK_FUNGI_FAN = registerBlockWithoutBlockItem("pink_fungi_fan", () -> new ShelfFungiFanBlock(AbstractBlock.Settings.create().mapColor(MapColor.GRAY).solid().instrument(NoteBlockInstrument.BASEDRUM).requiresTool().noCollision().breakInstantly()));
    public static Block  PINK_FUNGI_WALL_FAN = registerBlockWithoutBlockItem("pink_fungi_wall_fan", () -> new ShelfFungiWallFanBlock(AbstractBlock.Settings.create().mapColor(MapColor.GRAY).solid().instrument(NoteBlockInstrument.BASEDRUM).requiresTool().noCollision().breakInstantly().dropsLike(PINK_FUNGI_FAN)));

    public static Block ORANGE_FUNGI_FAN = registerBlockWithoutBlockItem("orange_fungi_fan", () -> new ShelfFungiFanBlock(AbstractBlock.Settings.create().mapColor(MapColor.GRAY).solid().instrument(NoteBlockInstrument.BASEDRUM).requiresTool().noCollision().breakInstantly()));
    public static Block  ORANGE_FUNGI_WALL_FAN = registerBlockWithoutBlockItem("orange_fungi_wall_fan", () -> new ShelfFungiWallFanBlock2(AbstractBlock.Settings.create().mapColor(MapColor.GRAY).solid().instrument(NoteBlockInstrument.BASEDRUM).requiresTool().noCollision().breakInstantly().dropsLike(ORANGE_FUNGI_FAN)));
**/

    private static Supplier<Block> registerBlock(String name,  Supplier<Block> block) {
        return JinxedRegistryHelper.registerBlock(Bygone.MOD_ID, name, true, block);
    }


    private static Supplier<Block> registerBlockWithoutBlockItem(String name, Supplier<Block> block) {
        return JinxedRegistryHelper.registerBlock(Bygone.MOD_ID, name, false, block);
    }


    public static Block createFlowerPotBlock(Block flower) {
        return new FlowerPotBlock(flower, BlockBehaviour.Properties.of().instabreak().noOcclusion().pushReaction(PushReaction.DESTROY));
    }
    
    public static void init() {}
}
