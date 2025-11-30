package com.jamiedev.bygone.core.registry;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.common.item.*;
import com.jamiedev.bygone.core.init.JamiesModToolMaterials;
import com.kekecreations.jinxedlib.core.util.JinxedRegistryHelper;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.material.Fluids;

import java.util.function.Supplier;

import static net.minecraft.world.item.Items.BUCKET;


public class BGItems {

    public static final Supplier<Item> BEAK_POTTERY_SHERD = registerItem(
            "beak_pottery_sherd",
            () -> new Item(new Item.Properties())
    );

    public static final Supplier<Item> MURKY_POTTERY_SHERD = registerItem(
            "murky_pottery_sherd",
            () -> new Item(new Item.Properties())
    );

    public static final Supplier<Item> PRIMORDIAL_FISH_BUCKET = registerItem(
            "primordial_fish_bucket",
            () -> new MobBucketItem(
                    BGEntityTypes.PRIMORDIAL_FISH.get(),
                    Fluids.WATER,
                    SoundEvents.BUCKET_EMPTY_FISH,
                    (new Item.Properties()).stacksTo(1).component(DataComponents.BUCKET_ENTITY_DATA, CustomData.EMPTY)
            )
    );

    public static final Supplier<Item> PRIMORDIAL_FISH = registerItem(
            "primordial_fish",
            () -> new Item(new Item.Properties().food(Foods.TROPICAL_FISH))
    );
    public static final Supplier<Item> COOKED_PRIMORDIAL_FISH = registerItem(
            "cooked_primordial_fish",
            () -> new Item(new Item.Properties().food(Foods.COOKED_SALMON))
    );

    public static final Supplier<Item> PRIMORDIAL_FISH_SPAWN_EGG = registerItem(
            "primordial_fish_spawn_egg",
            () -> new SpawnEggItem(BGEntityTypes.PRIMORDIAL_FISH.get(), 0x99c2fc, 0xa82c47, new Item.Properties())
    );
    public static final Supplier<Item> GUMBO_BOWL = registerItem(
            "gumbo_bowl",
            () -> new Item(new Item.Properties().craftRemainder(Items.BOWL).stacksTo(1))
    );
    public static final Supplier<Item> GUMBO_BOTTLE = registerItem(
            "gumbo_bottle",
            () -> new Item(new Item.Properties().craftRemainder(Items.GLASS_BOTTLE).stacksTo(16))
    );
    public static final Supplier<Item> VERDIGRIS_BLADE = registerItem(
            "verdigris_blade",
            () -> new VerdigrisBladeItem(
                    JamiesModToolMaterials.VERDIGRIS,
                    new Item.Properties().stacksTo(1).attributes(HoeItem.createAttributes(Tiers.IRON, -2.0F, 3.0F))
            )
    );
    public static final Supplier<Item> VERDIGRIS_BOW = registerItem(
            "verdigris_bow",
            () -> new VerdigrisBowItem(new Item.Properties().durability(100).stacksTo(1))
    );
    public static final Supplier<Item> HOOK = registerItem(
            "ancient_hook",
            () -> new HookItem(new Item.Properties().stacksTo(1).durability(100))
    );
    public static final Supplier<Item> WHIRLIWEED_BUNDLE = registerItem(
            "whirliweed_bundle",
            () -> new WhirliweedBundleItem(new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON))
    );
    public static final Supplier<Item> MALICIOUS_WAR_HORN = registerItem(
            "malicious_war_horn",
            () -> new MaliciousWarHornItem(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC).durability(100))
    );
    public static final Supplier<Item> ECHO_GONG = registerItem(
            "echo_gong",
            () -> new EchoGongItem(new Item.Properties().stacksTo(1).rarity(Rarity.RARE))
    );
    public static final Supplier<Item> ANCIENT_SIGN = registerItem(
            "ancient_sign", () ->
                    new SignItem(
                            new Item.Properties().stacksTo(16),
                            BGBlocks.ANCIENT_SIGN.get(),
                            BGBlocks.ANCIENT_WALL_SIGN.get()
                    )
    );
    public static final Supplier<Item> ANCIENT_HANGING_SIGN = registerItem(
            "ancient_hanging_sign", () ->
                    new HangingSignItem(
                            BGBlocks.ANCIENT_HANGING_SIGN.get(),
                            BGBlocks.ANCIENT_WALL_HANGING_SIGN.get(),
                            new Item.Properties().stacksTo(16)
                    )
    );
    public static final Supplier<Item> SABLE_SIGN = registerItem(
            "sable_sign", () ->
                    new SignItem(
                            new Item.Properties().stacksTo(16),
                            BGBlocks.SABLE_SIGN.get(),
                            BGBlocks.SABLE_WALL_SIGN.get()
                    )
    );
    public static final Supplier<Item> SABLE_HANGING_SIGN = registerItem(
            "sable_hanging_sign", () ->
                    new HangingSignItem(
                            BGBlocks.SABLE_HANGING_SIGN.get(),
                            BGBlocks.SABLE_WALL_HANGING_SIGN.get(),
                            new Item.Properties().stacksTo(16)
                    )
    );
    public static final Supplier<Item> SCALE = registerItem(
            "scale",
            () -> new Item(new Item.Properties().fireResistant())
    );
    public static final Supplier<Item> COELECANTH = registerItem(
            "coelacanth",
            () -> new Item(new Item.Properties().food(Foods.CHICKEN))
    );
    public static final Supplier<Item> COELECANTH_COOKED = registerItem(
            "cooked_coelacanth",
            () -> new Item(new Item.Properties().food(Foods.PUFFERFISH))
    );
    public static final Supplier<Item> EXOTIC_ARROW = registerItem(
            "exotic_arrow",
            () -> new ExoticArrowItem(new Item.Properties().fireResistant())
    );
    public static final Supplier<Item> EXOTIC_PLUMAGE = registerItem(
            "exotic_plumage",
            () -> new Item(new Item.Properties().fireResistant())
    );
    public static final Supplier<Item> GLOW_CHITIN = registerItem(
            "glow_chitin",
            () -> new Item(new Item.Properties().fireResistant())
    );
    public static final Supplier<Item> NECTAUR_PETAL = registerItem(
            "nectaur_petal",
            () -> new Item(new Item.Properties().fireResistant())
    );
    public static final Supplier<Item> SCUTTLE_SPIKE = registerItem(
            "scuttle_spike",
            () -> new Item(new Item.Properties().fireResistant())
    );
    public static final Supplier<Item> BEIGE_SLICE = registerItem(
            "glow_gourd_beige_slice",
            () -> new Item(new Item.Properties().food(Foods.CARROT))
    );
    public static final Supplier<Item> BEIGE_GOURD_SEEDS = registerItem(
            "glow_gourd_beige_seeds",
            () -> new ItemNameBlockItem(BGBlocks.GOURD_LANTERN_BEIGE.get(), new Item.Properties())
    );
    public static final Supplier<Item> MUAVE_SLICE = registerItem(
            "glow_gourd_muave_slice",
            () -> new Item(new Item.Properties().food(Foods.CARROT))
    );
    public static final Supplier<Item> MUAVE_GOURD_SEEDS = registerItem(
            "glow_gourd_muave_seeds",
            () -> new ItemNameBlockItem(BGBlocks.GOURD_LANTERN_MUAVE.get(), new Item.Properties())
    );
    public static final Supplier<Item> VERDANT_SLICE = registerItem(
            "glow_gourd_verdant_slice",
            () -> new Item(new Item.Properties().food(Foods.CARROT))
    );
    public static final Supplier<Item> VERDANT_GOURD_SEEDS = registerItem(
            "glow_gourd_verdant_seeds",
            () -> new ItemNameBlockItem(BGBlocks.GOURD_LANTERN_VERDANT.get(), new Item.Properties())
    );
    ;
    public static final Supplier<Item> GOURD_SOUP = registerItem(
            "glow_gourd_soup",
            () -> new Item(new Item.Properties().food(Foods.RABBIT_STEW).stacksTo(1))
    );
    public static final Supplier<Item> GOURD_DANGO = registerItem(
            "glow_gourd_dango", () -> new StandingAndWallBlockItem(
                    BGBlocks.GOURD_DANGO.get(), BGBlocks.GOURD_DANGO_WALL.get(),
                    new Item.Properties().food(Foods.GOLDEN_CARROT), Direction.DOWN
            )
    );
    public static final Supplier<Item> SCALE_HELMET = registerItem(
            "scale_helmet", () ->
                    new ArmorItem(
                            BGArmorMaterials.SCALE, ArmorItem.Type.HELMET,
                            new Item.Properties().fireResistant()
                    )
    );
    public static final Supplier<Item> SCALE_CHESTPLATE = registerItem(
            "scale_chestplate", () ->
                    new ArmorItem(
                            BGArmorMaterials.SCALE, ArmorItem.Type.CHESTPLATE,
                            new Item.Properties().fireResistant()
                    )
    );
    public static final Supplier<Item> SCALE_LEGGINGS = registerItem(
            "scale_leggings", () ->
                    new ArmorItem(
                            BGArmorMaterials.SCALE, ArmorItem.Type.LEGGINGS,
                            new Item.Properties().fireResistant()
                    )
    );
    public static final Supplier<Item> SCALE_BOOTS = registerItem(
            "scale_boots", () ->
                    new ArmorItem(
                            BGArmorMaterials.SCALE, ArmorItem.Type.BOOTS,
                            new Item.Properties().fireResistant()
                    )
    );
    public static final Supplier<Item> VERDIGRIS_SPURS = registerItem(
            "verdigris_spurs", () ->
                    new ArmorItem(
                            BGArmorMaterials.SCALE, ArmorItem.Type.BOOTS,
                            new Item.Properties().fireResistant()
                    )
    );
    public static final Supplier<Item> GOLD_BIG_BEAK_ARMOR = registerItem(
            "gold_beak_armor",
            () -> new CustomAnimalArmorItem(
                    ArmorMaterials.GOLD,
                    CustomAnimalArmorItem.BodyType.BIG_BEAK,
                    false,
                    (new Item.Properties()).stacksTo(1)
            )
    );
    public static final Supplier<Item> IRON_BIG_BEAK_ARMOR = registerItem(
            "iron_beak_armor",
            () -> new CustomAnimalArmorItem(
                    ArmorMaterials.IRON,
                    CustomAnimalArmorItem.BodyType.BIG_BEAK,
                    false,
                    (new Item.Properties()).stacksTo(1)
            )
    );
    public static final Supplier<Item> DIAMOND_BIG_BEAK_ARMOR = registerItem(
            "diamond_beak_armor",
            () -> new CustomAnimalArmorItem(
                    ArmorMaterials.DIAMOND,
                    CustomAnimalArmorItem.BodyType.BIG_BEAK,
                    false,
                    (new Item.Properties()).stacksTo(1)
            )
    );
    public static final Supplier<Item> BIG_BEAK_SPAWN_EGG = registerItem(
            "big_beak_spawn_egg",
            () -> new SpawnEggItem(BGEntityTypes.BIG_BEAK.get(), 0x85c70a, 0xffae00, new Item.Properties())
    );
    public static final Supplier<Item> COELACANTH_SPAWN_EGG = registerItem(
            "coelacanth_spawn_egg",
            () -> new SpawnEggItem(BGEntityTypes.COELACANTH.get(), 0x266a78, 0x238349, new Item.Properties())
    );
    public static final Supplier<Item> COPPERBUG_SPAWN_EGG = registerItem(
            "copperbug_spawn_egg",
            () -> new SpawnEggItem(BGEntityTypes.COPPERBUG.get(), 0x28e658, 0x25cba1, new Item.Properties())
    );
    public static final Supplier<Item> FUNGALPARENT_SPAWN_EGG = registerItem(
            "fungal_parent_spawn_egg",
            () -> new SpawnEggItem(BGEntityTypes.FUNGAL_PARENT.get(), 0xf5f7e3, 0x3ed8fe, new Item.Properties())
    );
    public static final Supplier<Item> LITHY_SPAWN_EGG = registerItem(
            "lithy_spawn_egg",
            () -> new SpawnEggItem(BGEntityTypes.LITHY.get(), 0x5C5B5A, 0x323232, new Item.Properties())
    );
    public static final Supplier<Item> MOOBOO_SPAWN_EGG = registerItem(
            "mooboo_spawn_egg",
            () -> new SpawnEggItem(BGEntityTypes.MOOBOO.get(), 0x5be3e8, 0xb2c7c8, new Item.Properties())
    );
    public static final Supplier<Item> PESKY_SPAWN_EGG = registerItem(
            "pesky_spawn_egg",
            () -> new SpawnEggItem(BGEntityTypes.PESKY.get(), 0x846B3C, 0xC66C11, new Item.Properties())
    );
    public static final Supplier<Item> PEST_SPAWN_EGG = registerItem(
            "pest_spawn_egg",
            () -> new SpawnEggItem(BGEntityTypes.PEST.get(), 0x846B3C, 0x6C8031, new Item.Properties())
    );
    public static final Supplier<Item> SABEAST_SPAWN_EGG = registerItem(
            "sabeast_spawn_egg",
            () -> new SpawnEggItem(BGEntityTypes.SABEAST.get(), 0x131110, 0x564E4A, new Item.Properties())
    );
    public static final Supplier<Item> AMOEBA_SPAWN_EGG = registerItem(
            "amoeba_spawn_egg",
            () -> new SpawnEggItem(BGEntityTypes.AMOEBA.get(), 0x6C9BB9, 0x8F8763, new Item.Properties())
    );
    public static final Supplier<Item> AQUIFAWN_SPAWN_EGG = registerItem(
            "aquifawn_spawn_egg",
            () -> new SpawnEggItem(BGEntityTypes.AQUIFAWN.get(), 0x0D2B50, 0x4E6F99, new Item.Properties())
    );
    public static final Supplier<Item> SCUTTLE_SPAWN_EGG = registerItem(
            "scuttle_spawn_egg",
            () -> new SpawnEggItem(BGEntityTypes.SCUTTLE.get(), 0x274772, 0xeab033, new Item.Properties())
    );
    public static final Supplier<Item> TRILOBITE_SPAWN_EGG = registerItem(
            "trilobite_spawn_egg",
            () -> new SpawnEggItem(BGEntityTypes.TRILOBITE.get(), 0x233c56, 0x14a4aa, new Item.Properties())
    );
    public static final Supplier<Item> WHISKBILL_SPAWN_EGG = registerItem(
            "whiskbill_spawn_egg",
            () -> new SpawnEggItem(BGEntityTypes.WHISKBILL.get(), 0x50692C, 0x70922D, new Item.Properties())
    );
    public static final Supplier<Item> NECTAUR_SPAWN_EGG = registerItem(
            "nectaur_spawn_egg",
            () -> new SpawnEggItem(BGEntityTypes.NECTAUR.get(), 0x50692C, 0x94262B, new Item.Properties())
    );
    public static final Supplier<Item> WRAITH_SPAWN_EGG = registerItem(
            "wraith_spawn_egg",
            () -> new SpawnEggItem(BGEntityTypes.WRAITH.get(), 0x3c3c3c, 0x9de8c0, new Item.Properties())
    );
    public static final Supplier<Item> AMOEBA_BUCKET = registerItem(
            (String) "bucket_o_amoeba", () ->
                    new MobBucketItem(
                            BGEntityTypes.AMOEBA.get(),
                            Fluids.WATER,
                            SoundEvents.BUCKET_EMPTY_FISH,
                            (new Item.Properties())
                                    .stacksTo(1).component(DataComponents.BUCKET_ENTITY_DATA, CustomData.EMPTY)
                    )
    );
    public static final Supplier<Item> AMOEBA_GEL = registerItem(
            "amoeba_gel",
            () -> new Item(new Item.Properties().fireResistant())
    );
    public static final Supplier<Item> AMOEBA_GEL_ON_A_STICK = registerItem((String)"amoeba_gel_on_a_stick", () ->
            new FoodOnAStickItem((
            new Item.Properties()).durability(25), BGEntityTypes.AQUIFAWN.get(), 7));

    public static final Supplier<Item> COELECANTH_BUCKET = registerItem(
            (String) "coelacanth_bucket", () ->
                    new MobBucketItem(
                            BGEntityTypes.COELACANTH.get(),
                            Fluids.WATER,
                            SoundEvents.BUCKET_EMPTY_FISH,
                            (new Item.Properties())
                                    .stacksTo(1).component(DataComponents.BUCKET_ENTITY_DATA, CustomData.EMPTY)
                    )
    );
    public static final Supplier<Item> ARCANE_CORE = registerItem(
            "arcane_core",
            () -> new Item(new Item.Properties().fireResistant())
    );
    public static final Supplier<Item> VERDIGRIS_SCRAP = registerItem(
            "verdigris_scrap",
            () -> new Item(new Item.Properties().fireResistant())
    );
    public static final Supplier<Item> VERDIGRIS_INGOT = registerItem(
            "verdigris_ingot",
            () -> new Item(new Item.Properties().fireResistant())
    );
    public static final Supplier<Item> AMARANTH_SEEDS = registerItem(
            "amaranth_seeds",
            () -> new ItemNameBlockItem(BGBlocks.AMARANTH_CROP.get(), new Item.Properties())
    );
    public static final Supplier<Item> AMARANTH_GRAIN = registerItem(
            "amaranth_grain",
            () -> new Item(new Item.Properties().fireResistant())
    );
    public static final Supplier<Item> PLAGA_SEEDS = registerItem(
            "plaga_seeds",
            () -> new ItemNameBlockItem(BGBlocks.PLAGA_CROP.get(), new Item.Properties())
    );
    public static final Supplier<Item> PLAGA = registerItem(
            "plaga", () -> new StandingAndWallBlockItem(
                    BGBlocks.PLAGA.get(), BGBlocks.PLAGA_WALL.get(),
                    new Item.Properties(), Direction.DOWN
            )
    );
    public static final Supplier<Item> CHANTRELLE_SEEDS = registerItem(
            "chantrelle_spores",
            () -> new ItemNameBlockItem(BGBlocks.CHANTRELLE.get(), new Item.Properties())
    );
    public static final Supplier<Item> ORANGE_FUNGI = registerItem(
            "orange_fungi_vines",
            () -> new ItemNameBlockItem(BGBlocks.ORANGE_FUNGI_VINES.get(), (new Item.Properties()))
    );
    public static final Supplier<Item> PINK_FUNGI = registerItem(
            "pink_fungi_vines",
            () -> new ItemNameBlockItem(BGBlocks.PINK_FUNGI_VINES.get(), (new Item.Properties()))
    );
    public static final Supplier<Item> PURPLE_FUNGI = registerItem(
            "purple_fungi_vines",
            () -> new ItemNameBlockItem(BGBlocks.PURPLE_FUNGI_VINES.get(), (new Item.Properties()))
    );
    public static final Supplier<Item> POULTRY = registerItem(
            "poultry",
            () -> new Item(new Item.Properties().food(Foods.CHICKEN))
    );
    public static final Supplier<Item> COOKED_POULTRY = registerItem(
            "cooked_poultry",
            () -> new Item(new Item.Properties().food(Foods.COOKED_CHICKEN))
    );
    public static final Supplier<Item> LITHOPLASM = registerItem(
            "lithoplasm",
            () -> new Item(new Item.Properties().fireResistant())
    );
    public static final Supplier<Item> SABLE_BRANCH = registerItem(
            "sable_branch",
            () -> new ItemNameBlockItem(BGBlocks.SABLE_BRANCH.get(), (new Item.Properties()))
    );
    public static final Supplier<Item> THORNY_SABLE_BRANCH = registerItem(
            "thorny_sable_branch",
            () -> new ItemNameBlockItem(BGBlocks.THORNY_SABLE_BRANCH.get(), (new Item.Properties()))
    );
    public static final Supplier<Item> BUCKET_O_BAITWORMS = registerItem(
            "bucket_o_baitworms",
            () -> new BucketOBaitwormsItem(new Item.Properties().craftRemainder(BUCKET).stacksTo(1))
    );
    public static final Supplier<Item> SABLE_GRASS = registerItem(
            "sable_grass",
            () -> new ItemNameBlockItem(BGBlocks.SABLE_GRASS.get(), (new Item.Properties()))
    );
    public static final Supplier<Item> SABLOSSOM = registerItem(
            "sablossom",
            () -> new ItemNameBlockItem(BGBlocks.SABLOSSOM.get(), (new Item.Properties()))
    );
    public static final Supplier<Item> ROASTED_SABLE_NUT = registerItem(
            "roasted_sable_nut",
            () -> new Item(new Item.Properties().food(Foods.BAKED_POTATO))
    );
    public static final Supplier<Item> ECTOPLASM_BUCKET = registerItem(
            (String) "ectoplasm_bucket",
            () -> new EctoplasmBucketItem((new Item.Properties()).craftRemainder(BUCKET).stacksTo(1))
    );
    public static final Supplier<Item> BREATH_POD = registerItem(
            "breath_pod", () ->
                    new BreathPodItem(new Item.Properties().fireResistant())
    );

    //public static final Supplier<Item> BRONZE_BUCKET = registerItem((String)"bronze_bucket", () -> new BucketItem(BGFluids.BRONZE_STILL.get(), (new Item.Properties()).craftRemainder(BUCKET).stacksTo(1)));
    public static final Supplier<Item> MUSIC_DISC_SHUFFLE = registerItem(
            "music_disc_shuffle",
            () -> new Item((new Item.Properties()).stacksTo(1)
                    .rarity(Rarity.RARE)
                    .jukeboxPlayable(BGJukeboxSongs.SHUFFLE))
    );
    public static final Supplier<Item> DEAD_RUGOSA_CORAL_FAN = registerItem(
            "dead_rugosa_coral_fan", () -> new StandingAndWallBlockItem(
                    BGBlocks.DEAD_RUGOSA_CORAL_FAN.get(),
                    BGBlocks.DEAD_RUGOSA_CORAL_WALL_FAN.get(),
                    new Item.Properties(),
                    Direction.DOWN
            )
    );
    public static final Supplier<Item> RUGOSA_CORAL_FAN = registerItem(
            "rugosa_coral_fan", () -> new StandingAndWallBlockItem(
                    BGBlocks.RUGOSA_CORAL_FAN.get(),
                    BGBlocks.RUGOSA_CORAL_WALL_FAN.get(),
                    new Item.Properties(),
                    Direction.DOWN
            )
    );
    public static final Supplier<Item> DEAD_TABULATA_CORAL_FAN = registerItem(
            "dead_tabulata_coral_fan", () -> new StandingAndWallBlockItem(
                    BGBlocks.DEAD_TABULATA_CORAL_FAN.get(),
                    BGBlocks.DEAD_TABULATA_CORAL_WALL_FAN.get(),
                    new Item.Properties(),
                    Direction.DOWN
            )
    );
    public static final Supplier<Item> TABULATA_CORAL_FAN = registerItem(
            "tabulata_coral_fan", () -> new StandingAndWallBlockItem(
                    BGBlocks.TABULATA_CORAL_FAN.get(),
                    BGBlocks.TABULATA_CORAL_WALL_FAN.get(),
                    new Item.Properties(),
                    Direction.DOWN
            )
    );
    public static final Supplier<Item> DEAD_PILLAR_CORAL_FAN = registerItem(
            "dead_pillar_coral_fan", () -> new StandingAndWallBlockItem(
                    BGBlocks.DEAD_PILLAR_CORAL_FAN.get(),
                    BGBlocks.DEAD_PILLAR_CORAL_WALL_FAN.get(),
                    new Item.Properties(),
                    Direction.DOWN
            )
    );
    public static final Supplier<Item> PILLAR_CORAL_FAN = registerItem(
            "pillar_coral_fan", () -> new StandingAndWallBlockItem(
                    BGBlocks.PILLAR_CORAL_FAN.get(),
                    BGBlocks.PILLAR_CORAL_WALL_FAN.get(),
                    new Item.Properties(),
                    Direction.DOWN
            )
    );
    public static final Supplier<Item> DEAD_THAMNOPORA_CORAL_FAN = registerItem(
            "dead_thamnopora_coral_fan", () -> new StandingAndWallBlockItem(
                    BGBlocks.DEAD_THAMNOPORA_CORAL_FAN.get(),
                    BGBlocks.DEAD_THAMNOPORA_CORAL_WALL_FAN.get(),
                    new Item.Properties(),
                    Direction.DOWN
            )
    );
    public static final Supplier<Item> THAMNOPORA_CORAL_FAN = registerItem(
            "thamnopora_coral_fan", () -> new StandingAndWallBlockItem(
                    BGBlocks.THAMNOPORA_CORAL_FAN.get(),
                    BGBlocks.THAMNOPORA_CORAL_WALL_FAN.get(),
                    new Item.Properties(),
                    Direction.DOWN
            )
    );

    public static final Supplier<Item> CARAPACE_GREAVES = registerItem(
            "carapace_greaves", () ->
                    new ArmorItem(
                            BGArmorMaterials.CARAPACE, ArmorItem.Type.LEGGINGS,
                            new Item.Properties().fireResistant().durability(ArmorItem.Type.LEGGINGS.getDurability(15)
                    )
    ));


    public static FoodProperties AMARANTH_LOAF_COMP = (new FoodProperties.Builder()).nutrition(0)
            .saturationModifier(0F)
            .effect(new MobEffectInstance(MobEffects.HEAL, 1, 0), 1.0F)
            .alwaysEdible()
            .build();
    public static final Supplier<Item> AMARANTH_LOAF = registerItem(
            "amaranth_loaf",
            () -> new Item(new Item.Properties().food(AMARANTH_LOAF_COMP))
    );
    public static final Supplier<Item> CHANTRELLE = registerItem(
            "chantrelle",
            () -> new Item(new Item.Properties().food(AMARANTH_LOAF_COMP))
    );
    public static FoodProperties SPEED_WHEAT_COMP = (new FoodProperties.Builder()).nutrition(0)
            .saturationModifier(0F)
            .effect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 70, 2), 1.0F)
            .alwaysEdible()
            .build();
    public static final Supplier<Item> SPEED_WHEAT = registerItem(
            "speed_wheat", () -> new BlockItem(
                    BGBlocks.SPEED_WHEAT.get(),
                    new Item.Properties().food(SPEED_WHEAT_COMP)
            )
    );
    Items item;
    ParticleTypes ref;
    ArmorMaterials ma;

    public static Supplier<Item> registerItem(String id, Supplier<Item> item) {
        return JinxedRegistryHelper.registerItem(Bygone.MOD_ID, id, item);
    }

    public static void addItemsToItemGroup() {


    }


    public static void init() {
        addItemsToItemGroup();
    }

}
