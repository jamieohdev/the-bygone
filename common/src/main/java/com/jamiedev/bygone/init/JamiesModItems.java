package com.jamiedev.bygone.init;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.items.*;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.BoatItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.HangingSignItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.item.Tiers;

public class JamiesModItems
{
    public static Item registerItem(String id, Item item){
        return Registry.register(BuiltInRegistries.ITEM, Bygone.id(id), item);
    }


    public static FoodProperties AMARANTH_LOAF_COMP  = (new FoodProperties.Builder()).nutrition(0).saturationModifier(0F).effect(new MobEffectInstance(MobEffects.HEAL, 1, 0), 1.0F).alwaysEdible().build();



    Items items;

   // public static final Item JAMIES_ITEM = registerItem("jamies_item", new Item(new Item.Settings().fireproof()));

    public static final Item VERDIGRIS_BLADE = registerItem("verdigris_blade", new VerdigrisBladeItem(JamiesModToolMaterials.VERDIGRIS, new Item.Properties().stacksTo(1).attributes(HoeItem.createAttributes(Tiers.IRON, -2.0F, 3.0F))));
    public static final Item VERDIGRIS_BOW = registerItem("verdigris_bow", new VerdigrisBowItem(new Item.Properties().durability(100).stacksTo(1)));

    BowItem ref;

    public static final Item HOOK = registerItem("ancient_hook", new HookItem(new Item.Properties().stacksTo(1).durability(100)));

    public static final Item ANCIENT_SIGN = registerItem("ancient_sign",
            new SignItem(new Item.Properties().stacksTo(16), JamiesModBlocks.ANCIENT_SIGN, JamiesModBlocks.ANCIENT_WALL_SIGN));

    public static final Item ANCIENT_HANGING_SIGN = registerItem("ancient_hanging_sign",
            new HangingSignItem(JamiesModBlocks.ANCIENT_HANGING_SIGN, JamiesModBlocks.ANCIENT_WALL_HANGING_SIGN, new Item.Properties().stacksTo(16)));

    public static final Item SCALE = registerItem("scale", new Item(new Item.Properties().fireResistant()));


    public static final Item ANCIENT_BOAT = registerItem("ancient_boat", new BoatItem(false, net.minecraft.world.entity.vehicle.Boat.Type.OAK, (new Item.Properties()).stacksTo(1)));
    public static final Item ANCIENT_CHEST_BOAT = registerItem("ancient_chest_boat", new BoatItem(true, net.minecraft.world.entity.vehicle.Boat.Type.OAK, (new Item.Properties()).stacksTo(1)));

    public static final Item COELECANTH = registerItem("coelacanth", new Item(new Item.Properties().food(Foods.CHICKEN)));
    public static final Item COELECANTH_COOKED = registerItem("cooked_coelacanth", new Item(new Item.Properties().food(Foods.PUFFERFISH)));
    public static final Item EXOTIC_ARROW = registerItem("exotic_arrow", new ExoticArrowItem(new Item.Properties().fireResistant()));
    public static final Item EXOTIC_PLUMAGE = registerItem("exotic_plumage", new Item(new Item.Properties().fireResistant()));
    public static final Item GLOW_CHITIN = registerItem("glow_chitin", new Item(new Item.Properties().fireResistant()));
    public static final Item SCUTTLE_SPIKE = registerItem("scuttle_spike", new Item(new Item.Properties().fireResistant()));
    ArmorMaterials mat;

    public static final Item BEIGE_SLICE = registerItem("glow_gourd_beige_slice", new Item(new Item.Properties().food(Foods.CARROT)));
    public static final Item MUAVE_SLICE = registerItem("glow_gourd_muave_slice", new Item(new Item.Properties().food(Foods.CARROT)));
    public static final Item VERDANT_SLICE = registerItem("glow_gourd_verdant_slice", new Item(new Item.Properties().food(Foods.CARROT)));
    //public static final Item GOURD_DANGO = registerItem("glow_gourd_dango", (Item)(new AliasedBlockItem(JamiesModBlocks.GOURD_DANGO, (new Item.Settings()).food(FoodComponents.SWEET_BERRIES))));

    public static final Item GOURD_SOUP = registerItem("glow_gourd_soup", new Item(new Item.Properties().food(Foods.RABBIT_STEW).stacksTo(1)));
    public static final Item GOURD_DANGO = registerItem("glow_gourd_dango", new StandingAndWallBlockItem(JamiesModBlocks.GOURD_DANGO, JamiesModBlocks.GOURD_DANGO_WALL,
            new Item.Properties().food(Foods.GOLDEN_CARROT), Direction.DOWN));
    public static final Item SCALE_HELMET = registerItem("scale_helmet",
            new ArmorItem(JamiesModArmorMaterials.SCALE, ArmorItem.Type.HELMET,
                    new Item.Properties().fireResistant()));
    public static final Item SCALE_CHESTPLATE = registerItem("scale_chestplate",
            new ArmorItem(JamiesModArmorMaterials.SCALE, ArmorItem.Type.CHESTPLATE,
                    new Item.Properties().fireResistant()));
    public static final Item SCALE_LEGGINGS = registerItem("scale_leggings",
            new ArmorItem(JamiesModArmorMaterials.SCALE, ArmorItem.Type.LEGGINGS,
                    new Item.Properties().fireResistant()));
    public static final Item SCALE_BOOTS = registerItem("scale_boots",
            new ArmorItem(JamiesModArmorMaterials.SCALE, ArmorItem.Type.BOOTS,
                    new Item.Properties().fireResistant()));
    public static final Item GOLD_BIG_BEAK_ARMOR = registerItem("gold_beak_armor", new CustomAnimalArmorItem(ArmorMaterials.GOLD, CustomAnimalArmorItem.BodyType.BIG_BEAK, false, (new Item.Properties()).stacksTo(1)));
    public static final Item IRON_BIG_BEAK_ARMOR = registerItem("iron_beak_armor", new CustomAnimalArmorItem(ArmorMaterials.IRON, CustomAnimalArmorItem.BodyType.BIG_BEAK, false, (new Item.Properties()).stacksTo(1)));
    public static final Item DIAMOND_BIG_BEAK_ARMOR = registerItem("diamond_beak_armor", new CustomAnimalArmorItem(ArmorMaterials.DIAMOND, CustomAnimalArmorItem.BodyType.BIG_BEAK, false, (new Item.Properties()).stacksTo(1)));
    public static final Item BIG_BEAK_SPAWN_EGG = registerItem("big_beak_spawn_egg", new SpawnEggItem(JamiesModEntityTypes.BIG_BEAK, 0x85c70a, 0xffae00, new Item.Properties()));
    public static final Item COELACANTH_SPAWN_EGG = registerItem("coelacanth_spawn_egg", new SpawnEggItem(JamiesModEntityTypes.COELACANTH, 0x266a78, 0x238349, new Item.Properties()));
    public static final Item COPPERBUG_SPAWN_EGG = registerItem("copperbug_spawn_egg", new SpawnEggItem(JamiesModEntityTypes.COPPERBUG, 0x28e658, 0x25cba1, new Item.Properties()));

    public static final Item FUNGALPARENT_SPAWN_EGG = registerItem("fungal_parent_spawn_egg", new SpawnEggItem(JamiesModEntityTypes.FUNGAL_PARENT, 0xf5f7e3, 0x3ed8fe, new Item.Properties()));


    public static final Item MOOBOO_SPAWN_EGG = registerItem("mooboo_spawn_egg", new SpawnEggItem(JamiesModEntityTypes.MOOBOO, 0x5be3e8, 0xb2c7c8, new Item.Properties()));
    public static final Item SCUTTLE_SPAWN_EGG = registerItem("scuttle_spawn_egg", new SpawnEggItem(JamiesModEntityTypes.SCUTTLE, 0x274772, 0xeab033, new Item.Properties()));
    public static final Item TRILOBITE_SPAWN_EGG = registerItem("trilobite_spawn_egg", new SpawnEggItem(JamiesModEntityTypes.TRILOBITE, 0x233c56, 0x14a4aa, new Item.Properties()));

    public static final Item ARCANE_CORE = registerItem("arcane_core", new Item(new Item.Properties().fireResistant()));//((BlockItem)(new ArcaneCoreItem(JamiesModBlocks.ARCANE_CORE, new Item.Settings()))));

    public static final Item VERDIGRIS_SCRAP = registerItem("verdigris_scrap", new Item(new Item.Properties().fireResistant()));
    public static final Item VERDIGRIS_INGOT = registerItem("verdigris_ingot", new Item(new Item.Properties().fireResistant()));

    public static final Item AMARANTH_SEEDS = registerItem("amaranth_seeds", new ItemNameBlockItem(JamiesModBlocks.AMARANTH_CROP, new Item.Properties()));

    public static final Item AMARANTH_GRAIN = registerItem("amaranth_grain", new Item(new Item.Properties().fireResistant()));

    public static final Item AMARANTH_LOAF = registerItem("amaranth_loaf", new Item(new Item.Properties().food(AMARANTH_LOAF_COMP)));

  //  public static final Item ORANGE_FUNGI = registerItem("orange_fungi_fan", (BlockItem)(new VerticallyAttachableBlockItem(JamiesModBlocks.ORANGE_FUNGI_FAN, JamiesModBlocks.ORANGE_FUNGI_WALL_FAN, new Item.Settings(), Direction.DOWN)));

  //  public static final Item PINK_FUNGI = registerItem("pink_fungi_fan", (BlockItem)(new VerticallyAttachableBlockItem(JamiesModBlocks.PINK_FUNGI_FAN, JamiesModBlocks.PINK_FUNGI_WALL_FAN, new Item.Settings(), Direction.DOWN)));
    
  //  public static final Item PURPLE_FUNGI = registerItem("purple_fungi_fan", (BlockItem)(new VerticallyAttachableBlockItem(JamiesModBlocks.PURPLE_FUNGI_FAN, JamiesModBlocks.PURPLE_FUNGI_WALL_FAN, new Item.Settings(), Direction.DOWN)));

    public static final Item ORANGE_FUNGI  = registerItem("orange_fungi_vines", new ItemNameBlockItem(JamiesModBlocks.ORANGE_FUNGI_VINES, (new Item.Properties())));
    public static final Item PINK_FUNGI  = registerItem("pink_fungi_vines", new ItemNameBlockItem(JamiesModBlocks.PINK_FUNGI_VINES, (new Item.Properties())));
    public static final Item PURPLE_FUNGI  = registerItem("purple_fungi_vines", new ItemNameBlockItem(JamiesModBlocks.PURPLE_FUNGI_VINES, (new Item.Properties())));

    public static void addItemsToItemGroup() {


    }


    public static void init(){
      /*  ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.NATURAL_BLOCKS).register(entries -> {

        });

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.INGREDIENTS).register(entries -> {

        });

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(entries -> {

        });


        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.BUILDING_BLOCKS).register(entries -> {


        });*/

        addItemsToItemGroup();
    }

}
