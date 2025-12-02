package com.jamiedev.bygone.core.registry;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

import java.util.function.Supplier;

import static com.jamiedev.bygone.Bygone.id;

public class BGSoundEvents {
    public static final ResourceLocation AMBIENT_ANCIENTFOREST_ADDITIONS = id("ambient.underhang.additions");
    public static final ResourceLocation AMBIENT_AMBERDESERT_ADDITIONS = id("ambient.amber_desert.additions");
    public static final ResourceLocation AMBIENT_PRIMORDIALOCEAN_ADDITIONS = id("ambient.primordial_ocean.additions");
    public static final ResourceLocation MUSIC_ALPHAHANG_ADDITIONS = id("music.bygone.alphahang");
    public static final ResourceLocation MUSIC_AMBER_DESERT_ADDITIONS = id("music.bygone.amber_desert");
    public static final ResourceLocation MUSIC_MEGALITH_FIELDS_ADDITIONS = id("music.bygone.megalith_fields");
    public static final ResourceLocation MUSIC_SHELFHOLLOWS_ADDITIONS = id("music.bygone.shelfhollows");
    public static final ResourceLocation MUSIC_ANCIENTFOREST_ADDITIONS = id("music.bygone.underhang");
    public static final ResourceLocation MUSIC_PRIMORDIALOCEAN_ADDITIONS = id("music.bygone.primordial_ocean");
    public static final ResourceLocation AMBIENT_SHELFHOLLOW_ADDITIONS = id("ambient.shelfhollow.additions");

    public static final ResourceLocation AMBIENT_SABLE_FOREST_LOOPS = id("ambient.sable_forest.loop");
    public static final ResourceLocation AMBIENT_SABLE_FOREST_ADDITIONS = id("ambient.sable_forest.additions");
    public static final Holder.Reference<SoundEvent> MUSIC_DISC_SHUFFLE = registerSoundEventHolder("music_disc.shuffle");
    public static final ResourceLocation BLOCK_MEGALITH_BLOCK_IDLE = id("block.megalith_block.idle");

    public static final ResourceLocation ENTITY_AMOEBA_AMBIENT = id("entity.amoeba.ambient");
    public static final ResourceLocation ENTITY_AMOEBA_FLOP = id("entity.amoeba.flop");
    public static final ResourceLocation ENTITY_AMOEBA_HURT = id("entity.amoeba.hurt");
    public static final ResourceLocation ENTITY_AMOEBA_DEATH = id("entity.amoeba.death");
    public static final ResourceLocation ENTITY_BIGBEAK_AMBIENT = id("entity.bigbeak.ambient");
    public static final ResourceLocation ENTITY_BIGBEAK_HURT = id("entity.bigbeak.hurt");
    public static final ResourceLocation ENTITY_BIGBEAK_DEATH = id("entity.bigbeak.death");
    public static final ResourceLocation ENTITY_BIGBEAK_JUMP = id("entity.bigbeak.jump");
    public static final ResourceLocation ENTITY_BIGBEAK_STEP = id("entity.bigbeak.step");
    public static final ResourceLocation ENTITY_BIGBEAK_STEP_WOOD = id("entity.bigbeak.step_wood");
    public static final ResourceLocation ENTITY_BIGBEAK_GALLOP = id("entity.bigbeak.gallop");
    public static final ResourceLocation ENTITY_COPPERBUG_AMBIENT = id("entity.copperbug.ambient");
    public static final ResourceLocation ENTITY_COPPERBUG_HURT = id("entity.copperbug.hurt");
    public static final ResourceLocation ENTITY_COPPERBUG_DEATH = id("entity.copperbug.death");
    public static final ResourceLocation ENTITY_COPPERBUG_EAT = id("entity.copperbug.eat");
    public static final ResourceLocation ENTITY_MOOBOO_AMBIENT = id("entity.mooboo.ambient");
    public static final ResourceLocation ENTITY_MOOBOO_HURT = id("entity.mooboo.hurt");
    public static final ResourceLocation ENTITY_MOOBOO_DEATH = id("entity.mooboo.death");
    public static final ResourceLocation ENTITY_MOOBOO_MILK = id("entity.mooboo.milk");
    public static final ResourceLocation ENTITY_FUNGUSPARENT_AMBIENT = id("entity.fungus_parent.ambient");
    public static final ResourceLocation ENTITY_FUNGUSPARENT_BABY_AMBIENT = id("entity.fungus_parent.ambient_baby");
    public static final ResourceLocation ENTITY_FUNGUSPARENT_HURT = id("entity.fungus_parent.hurt");
    public static final ResourceLocation ENTITY_FUNGUSPARENT_DEATH = id("entity.fungus_parent.death");
    public static final ResourceLocation ENTITY_PEST_AMBIENT = id("entity.pest.ambient");
    public static final ResourceLocation ENTITY_PEST_HURT = id("entity.pest.hurt");
    public static final ResourceLocation ENTITY_PEST_DEATH = id("entity.pest.death");
    public static final ResourceLocation ENTITY_PEST_EAT = id("entity.pest.eat");
    public static final ResourceLocation ENTITY_WHISKBILL_AMBIENT = id("entity.whiskbill.ambient");
    public static final ResourceLocation ENTITY_WHISKBILL_HURT = id("entity.whiskbill.hurt");
    public static final ResourceLocation ENTITY_WHISKBILL_DEATH = id("entity.whiskbill.death");
    public static final ResourceLocation ENTITY_WHISKBILL_ROAR = id("entity.whiskbill.roar");
    public static final ResourceLocation ENTITY_NECTAUR_AMBIENT = id("entity.nectaur.ambient");
    public static final ResourceLocation ENTITY_NECTAUR_BELLOW = id("entity.nectaur.bellow");
    public static final ResourceLocation ENTITY_NECTAUR_HURT = id("entity.nectaur.hurt");
    public static final ResourceLocation ENTITY_NECTAUR_DEATH = id("entity.nectaur.death");
    public static final ResourceLocation ENTITY_NECTAUR_SCREECH = id("entity.nectaur.screech");
    public static final ResourceLocation ENTITY_WRAITH_AMBIENT = id("entity.wraith.ambient");
    public static final ResourceLocation ENTITY_WRAITH_ATTACK = id("entity.wraith.attack");
    public static final ResourceLocation ENTITY_WRAITH_HURT = id("entity.wraith.hurt");
    public static final ResourceLocation ENTITY_WRAITH_DEATH = id("entity.wraith.death");
    public static final ResourceLocation ENTITY_WRAITH_TELEPORT = id("entity.wraith.teleport");
    public static final ResourceLocation ENTITY_WRAITH_FLY = id("entity.wraith.fly");
    public static final ResourceLocation ENTITY_LITHY_AMBIENT = id("entity.lithy.ambient");
    public static final ResourceLocation ENTITY_LITHY_TRIP = id("entity.lithy.trip");
    public static final ResourceLocation ENTITY_LITHY_HURT = id("entity.lithy.hurt");
    public static final ResourceLocation ENTITY_LITHY_DEATH = id("entity.lithy.death");
    
    public static final ResourceLocation ENTITY_SABEAST_AMBIENT = id("entity.sabeast.ambient");
    public static final ResourceLocation ENTITY_SABEAST_ATTACK = id("entity.sabeast.attack");
    public static final ResourceLocation ENTITY_SABEAST_HURT = id("entity.sabeast.hurt");
    public static final ResourceLocation ENTITY_SABEAST_DEATH = id("entity.sabeast.death");
    public static SoundEvent SABEAST_AMBIENT_ADDITIONS_EVENT = SoundEvent.createVariableRangeEvent(ENTITY_SABEAST_AMBIENT);
    public static SoundEvent SABEAST_ATTACK_ADDITIONS_EVENT = SoundEvent.createVariableRangeEvent(ENTITY_SABEAST_ATTACK);
    public static SoundEvent SABEAST_DEATH_ADDITIONS_EVENT = SoundEvent.createVariableRangeEvent(ENTITY_SABEAST_DEATH);
    public static SoundEvent SABEAST_HURT_ADDITIONS_EVENT = SoundEvent.createVariableRangeEvent(ENTITY_SABEAST_HURT);
    
    public static final ResourceLocation HOOK_RETRIEVE = id("entity.hook.retrieve");
    public static final ResourceLocation HOOK_HIT = id("entity.hook.hit");
    public static final ResourceLocation HOOK_THROW = id("entity.hook.throw");
    public static final SoundEvent HOOK_RETRIEVE_ADDITIONS_EVENT = SoundEvent.createVariableRangeEvent(HOOK_RETRIEVE);
    public static final SoundEvent HOOK_HIT_ADDITIONS_EVENT = SoundEvent.createVariableRangeEvent(HOOK_HIT);
    public static final SoundEvent HOOK_THROW_ADDITIONS_EVENT = SoundEvent.createVariableRangeEvent(HOOK_THROW);
    public static final ResourceLocation ITEM_WAR_HORN_USE_ID = id("item.war_horn.use");
    public static final SoundEvent WAR_HORN_USE_EVENT = SoundEvent.createVariableRangeEvent(ITEM_WAR_HORN_USE_ID);
    public static final Supplier<SoundEvent> WAR_HORN_USE = () -> WAR_HORN_USE_EVENT;
    public static final ResourceLocation ITEM_WHIRLIWEED_BUNDLE_USE_ID = id("item.whirliweed_bundle.use");
    public static final SoundEvent WHIRLIWEED_BUNDLE_USE_EVENT = SoundEvent.createVariableRangeEvent(ITEM_WHIRLIWEED_BUNDLE_USE_ID);
    public static final Supplier<SoundEvent> WHIRLIWEED_BUNDLE_USE = () -> WHIRLIWEED_BUNDLE_USE_EVENT;
    public static final ResourceLocation ITEM_ECHO_GONG_USE_ID = id("item.echo_gong.use");
    public static final SoundEvent ECHO_GONG_USE_EVENT = SoundEvent.createVariableRangeEvent(ITEM_ECHO_GONG_USE_ID);
    public static final Supplier<SoundEvent> ECHO_GONG_USE = () -> ECHO_GONG_USE_EVENT;
    public static final ResourceLocation ITEM_ECHO_GONG_CHARGE_ID = id("item.echo_gong.charge");
    public static final SoundEvent ECHO_GONG_CHARGE_EVENT = SoundEvent.createVariableRangeEvent(ITEM_ECHO_GONG_CHARGE_ID);
    public static final Supplier<SoundEvent> ECHO_GONG_CHARGE = () -> ECHO_GONG_CHARGE_EVENT;
    public static final ResourceLocation BLOCK_PORTAL_AMBIENT_ID = id("block.portal.ambient");
    public static final SoundEvent BLOCK_PORTAL_AMBIENT_EVENT = SoundEvent.createVariableRangeEvent(BLOCK_PORTAL_AMBIENT_ID);
    public static SoundEvent AMBIENT_SABLE_FOREST_LOOPS_EVENT = SoundEvent.createVariableRangeEvent(AMBIENT_SABLE_FOREST_LOOPS);
    public static SoundEvent AMBIENT_SABLE_FOREST_ADDITIONS_EVENT = SoundEvent.createVariableRangeEvent(AMBIENT_SABLE_FOREST_ADDITIONS);
    public static SoundEvent BLOCK_MEGALITH_BLOCK_IDLE_ADDITIONS_EVENT = SoundEvent.createVariableRangeEvent(BLOCK_MEGALITH_BLOCK_IDLE);
    public static SoundEvent WRAITH_AMBIENT_ADDITIONS_EVENT = SoundEvent.createVariableRangeEvent(ENTITY_WRAITH_AMBIENT);
    public static SoundEvent WRAITH_ATTACK_ADDITIONS_EVENT = SoundEvent.createVariableRangeEvent(ENTITY_WRAITH_ATTACK);
    public static SoundEvent WRAITH_DEATH_ADDITIONS_EVENT = SoundEvent.createVariableRangeEvent(ENTITY_WRAITH_DEATH);
    public static SoundEvent WRAITH_HURT_ADDITIONS_EVENT = SoundEvent.createVariableRangeEvent(ENTITY_WRAITH_HURT);
    public static SoundEvent WRAITH_FLY_ADDITIONS_EVENT = SoundEvent.createVariableRangeEvent(ENTITY_WRAITH_FLY);
    public static SoundEvent WRAITH_TELEPORT_ADDITIONS_EVENT = SoundEvent.createVariableRangeEvent(ENTITY_WRAITH_TELEPORT);


    public static SoundEvent AMOEBA_AMBIENT_ADDITIONS_EVENT = SoundEvent.createVariableRangeEvent(ENTITY_AMOEBA_AMBIENT);
    public static SoundEvent AMOEBA_FLOP_ADDITIONS_EVENT = SoundEvent.createVariableRangeEvent(ENTITY_AMOEBA_FLOP);
    public static SoundEvent AMOEBA_DEATH_ADDITIONS_EVENT = SoundEvent.createVariableRangeEvent(ENTITY_AMOEBA_DEATH);
    public static SoundEvent AMOEBA_HURT_ADDITIONS_EVENT = SoundEvent.createVariableRangeEvent(ENTITY_AMOEBA_HURT);
    
    public static SoundEvent LITHY_AMBIENT_ADDITIONS_EVENT = SoundEvent.createVariableRangeEvent(ENTITY_LITHY_AMBIENT);
    public static SoundEvent LITHY_TRIP_ADDITIONS_EVENT = SoundEvent.createVariableRangeEvent(ENTITY_LITHY_TRIP);
    public static SoundEvent LITHY_DEATH_ADDITIONS_EVENT = SoundEvent.createVariableRangeEvent(ENTITY_LITHY_DEATH);
    public static SoundEvent LITHY_HURT_ADDITIONS_EVENT = SoundEvent.createVariableRangeEvent(ENTITY_LITHY_HURT);
    public static SoundEvent AMBIENT_ANCIENTFOREST_ADDITIONS_EVENT = SoundEvent.createVariableRangeEvent(AMBIENT_ANCIENTFOREST_ADDITIONS);
    public static SoundEvent AMBIENT_AMBERDESERT_ADDITIONS_EVENT = SoundEvent.createVariableRangeEvent(AMBIENT_AMBERDESERT_ADDITIONS);
    public static SoundEvent AMBIENT_PRIMORDIALOCEAN_ADDITIONS_EVENT = SoundEvent.createVariableRangeEvent(AMBIENT_PRIMORDIALOCEAN_ADDITIONS);
    public static SoundEvent MUSIC_ALPHAHANG_ADDITIONS_EVENT = SoundEvent.createVariableRangeEvent(MUSIC_ALPHAHANG_ADDITIONS);
    public static SoundEvent MUSIC_ANCIENTFOREST_ADDITIONS_EVENT = SoundEvent.createVariableRangeEvent(MUSIC_ANCIENTFOREST_ADDITIONS);
    public static SoundEvent MUSIC_AMBERDESERT_ADDITIONS_EVENT = SoundEvent.createVariableRangeEvent(MUSIC_AMBER_DESERT_ADDITIONS);
    public static SoundEvent MUSIC_MEGALITH_FIELDS_ADDITIONS_EVENT = SoundEvent.createVariableRangeEvent(MUSIC_MEGALITH_FIELDS_ADDITIONS);
    public static SoundEvent MUSIC_SHELFHOLLOWS_ADDITIONS_EVENT = SoundEvent.createVariableRangeEvent(MUSIC_SHELFHOLLOWS_ADDITIONS);
    public static SoundEvent AMBIENT_SHELFHOLLOW_ADDITIONS_EVENT = SoundEvent.createVariableRangeEvent(AMBIENT_SHELFHOLLOW_ADDITIONS);
    public static SoundEvent MUSIC_PRIMORDIALOCEAN_ADDITIONS_EVENT = SoundEvent.createVariableRangeEvent(MUSIC_PRIMORDIALOCEAN_ADDITIONS);
    public static SoundEvent BIGBEAK_AMBIENT_ADDITIONS_EVENT = SoundEvent.createVariableRangeEvent(ENTITY_BIGBEAK_AMBIENT);
    public static SoundEvent BIGBEAK_HURT_ADDITIONS_EVENT = SoundEvent.createVariableRangeEvent(ENTITY_BIGBEAK_HURT);
    public static SoundEvent BIGBEAK_DEATH_ADDITIONS_EVENT = SoundEvent.createVariableRangeEvent(ENTITY_BIGBEAK_DEATH);
    public static SoundEvent BIGBEAK_JUMP_ADDITIONS_EVENT = SoundEvent.createVariableRangeEvent(ENTITY_BIGBEAK_JUMP);
    public static SoundEvent BIGBEAK_STEP_ADDITIONS_EVENT = SoundEvent.createVariableRangeEvent(ENTITY_BIGBEAK_STEP);
    public static SoundEvent BIGBEAK_STEP_WOOD_ADDITIONS_EVENT = SoundEvent.createVariableRangeEvent(ENTITY_BIGBEAK_STEP_WOOD);
    public static SoundEvent BIGBEAK_GALLOP_ADDITIONS_EVENT = SoundEvent.createVariableRangeEvent(ENTITY_BIGBEAK_GALLOP);
    public static SoundEvent COPPERBUG_AMBIENT_ADDITIONS_EVENT = SoundEvent.createVariableRangeEvent(ENTITY_COPPERBUG_AMBIENT);
    public static SoundEvent COPPERBUG_HURT_ADDITIONS_EVENT = SoundEvent.createVariableRangeEvent(ENTITY_COPPERBUG_HURT);
    public static SoundEvent COPPERBUG_DEATH_ADDITIONS_EVENT = SoundEvent.createVariableRangeEvent(ENTITY_COPPERBUG_DEATH);
    public static SoundEvent COPPERBUG_EAT_ADDITIONS_EVENT = SoundEvent.createVariableRangeEvent(ENTITY_COPPERBUG_EAT);
    public static SoundEvent MOOBOO_AMBIENT_ADDITIONS_EVENT = SoundEvent.createVariableRangeEvent(ENTITY_MOOBOO_AMBIENT);
    public static SoundEvent MOOBOO_HURT_ADDITIONS_EVENT = SoundEvent.createVariableRangeEvent(ENTITY_MOOBOO_HURT);
    public static SoundEvent MOOBOO_DEATH_ADDITIONS_EVENT = SoundEvent.createVariableRangeEvent(ENTITY_MOOBOO_DEATH);
    public static SoundEvent MOOBOO_MILK_ADDITIONS_EVENT = SoundEvent.createVariableRangeEvent(ENTITY_MOOBOO_MILK);
    public static SoundEvent FUNGUSPARENT_AMBIENT_ADDITIONS_EVENT = SoundEvent.createVariableRangeEvent(ENTITY_FUNGUSPARENT_AMBIENT);
    public static SoundEvent FUNGUSPARENT_AMBIENT_BABY_ADDITIONS_EVENT = SoundEvent.createVariableRangeEvent(ENTITY_FUNGUSPARENT_BABY_AMBIENT);
    public static SoundEvent FUNGUSPARENT_HURT_ADDITIONS_EVENT = SoundEvent.createVariableRangeEvent(ENTITY_FUNGUSPARENT_HURT);
    public static SoundEvent FUNGUSPARENT_DEATH_ADDITIONS_EVENT = SoundEvent.createVariableRangeEvent(ENTITY_FUNGUSPARENT_DEATH);
    public static SoundEvent NECTAUR_BELLOW_ADDITIONS_EVENT = SoundEvent.createVariableRangeEvent(ENTITY_NECTAUR_BELLOW);
    public static SoundEvent NECTAUR_AMBIENT_ADDITIONS_EVENT = SoundEvent.createVariableRangeEvent(ENTITY_NECTAUR_AMBIENT);
    public static SoundEvent NECTAUR_HURT_ADDITIONS_EVENT = SoundEvent.createVariableRangeEvent(ENTITY_NECTAUR_HURT);
    public static SoundEvent NECTAUR_DEATH_ADDITIONS_EVENT = SoundEvent.createVariableRangeEvent(ENTITY_NECTAUR_DEATH);
    public static SoundEvent NECTAUR_SCREECH_ADDITIONS_EVENT = SoundEvent.createVariableRangeEvent(ENTITY_NECTAUR_SCREECH);
    public static SoundEvent PEST_AMBIENT_ADDITIONS_EVENT = SoundEvent.createVariableRangeEvent(ENTITY_PEST_AMBIENT);
    public static SoundEvent PEST_HURT_ADDITIONS_EVENT = SoundEvent.createVariableRangeEvent(ENTITY_PEST_HURT);
    public static SoundEvent PEST_DEATH_ADDITIONS_EVENT = SoundEvent.createVariableRangeEvent(ENTITY_PEST_DEATH);
    public static SoundEvent PEST_EAT_ADDITIONS_EVENT = SoundEvent.createVariableRangeEvent(ENTITY_PEST_EAT);
    public static SoundEvent WHISKBILL_AMBIENT_ADDITIONS_EVENT = SoundEvent.createVariableRangeEvent(ENTITY_WHISKBILL_AMBIENT);
    public static SoundEvent WHISKBILL_HURT_ADDITIONS_EVENT = SoundEvent.createVariableRangeEvent(ENTITY_WHISKBILL_HURT);
    public static SoundEvent WHISKBILL_DEATH_ADDITIONS_EVENT = SoundEvent.createVariableRangeEvent(ENTITY_WHISKBILL_DEATH);
    public static SoundEvent WHISKBILL_ROAR_ADDITIONS_EVENT = SoundEvent.createVariableRangeEvent(ENTITY_WHISKBILL_ROAR);

    private static SoundEvent register(String id) {
        return Registry.register(BuiltInRegistries.SOUND_EVENT, id(id), SoundEvent.createVariableRangeEvent(id(id)));
    }

    private static Holder.Reference<SoundEvent> registerReference(String id) {
        return registerReference(id(id), id(id));
    }

    private static Holder.Reference<SoundEvent> registerReference(ResourceLocation id, ResourceLocation soundId) {
        return Registry.registerForHolder(BuiltInRegistries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(soundId));
    }

    public static Holder.Reference<SoundEvent> registerSoundEventHolder(String name) {
        ResourceLocation location = id(name);
        return Registry.registerForHolder(BuiltInRegistries.SOUND_EVENT, location, SoundEvent.createVariableRangeEvent(location));
    }


    public static void init() {
        Registry.register(BuiltInRegistries.SOUND_EVENT, BGSoundEvents.BLOCK_PORTAL_AMBIENT_ID, BLOCK_PORTAL_AMBIENT_EVENT);

        Registry.register(BuiltInRegistries.SOUND_EVENT, BGSoundEvents.AMBIENT_ANCIENTFOREST_ADDITIONS, BGSoundEvents.AMBIENT_ANCIENTFOREST_ADDITIONS_EVENT);
        Registry.register(BuiltInRegistries.SOUND_EVENT, BGSoundEvents.AMBIENT_AMBERDESERT_ADDITIONS, BGSoundEvents.AMBIENT_AMBERDESERT_ADDITIONS_EVENT);
        Registry.register(BuiltInRegistries.SOUND_EVENT, BGSoundEvents.AMBIENT_PRIMORDIALOCEAN_ADDITIONS, BGSoundEvents.AMBIENT_PRIMORDIALOCEAN_ADDITIONS_EVENT);
        Registry.register(BuiltInRegistries.SOUND_EVENT, BGSoundEvents.AMBIENT_SHELFHOLLOW_ADDITIONS, BGSoundEvents.AMBIENT_SHELFHOLLOW_ADDITIONS_EVENT);

        Registry.register(BuiltInRegistries.SOUND_EVENT, BGSoundEvents.AMBIENT_SABLE_FOREST_ADDITIONS, BGSoundEvents.AMBIENT_SABLE_FOREST_ADDITIONS_EVENT);
        Registry.register(BuiltInRegistries.SOUND_EVENT, BGSoundEvents.AMBIENT_SABLE_FOREST_LOOPS, BGSoundEvents.AMBIENT_SABLE_FOREST_LOOPS_EVENT);


        Registry.register(BuiltInRegistries.SOUND_EVENT, BGSoundEvents.MUSIC_ALPHAHANG_ADDITIONS, BGSoundEvents.MUSIC_ALPHAHANG_ADDITIONS_EVENT);
        Registry.register(BuiltInRegistries.SOUND_EVENT, BGSoundEvents.MUSIC_ANCIENTFOREST_ADDITIONS, BGSoundEvents.MUSIC_ANCIENTFOREST_ADDITIONS_EVENT);
        Registry.register(BuiltInRegistries.SOUND_EVENT, BGSoundEvents.MUSIC_AMBER_DESERT_ADDITIONS, BGSoundEvents.MUSIC_AMBERDESERT_ADDITIONS_EVENT);
        Registry.register(BuiltInRegistries.SOUND_EVENT, BGSoundEvents.MUSIC_MEGALITH_FIELDS_ADDITIONS, BGSoundEvents.MUSIC_MEGALITH_FIELDS_ADDITIONS_EVENT);
        Registry.register(BuiltInRegistries.SOUND_EVENT, BGSoundEvents.MUSIC_SHELFHOLLOWS_ADDITIONS, BGSoundEvents.MUSIC_SHELFHOLLOWS_ADDITIONS_EVENT);
        Registry.register(BuiltInRegistries.SOUND_EVENT, BGSoundEvents.MUSIC_PRIMORDIALOCEAN_ADDITIONS, BGSoundEvents.MUSIC_PRIMORDIALOCEAN_ADDITIONS_EVENT);


        Registry.register(BuiltInRegistries.SOUND_EVENT, BGSoundEvents.ENTITY_AMOEBA_AMBIENT, BGSoundEvents.AMOEBA_AMBIENT_ADDITIONS_EVENT);
        Registry.register(BuiltInRegistries.SOUND_EVENT, BGSoundEvents.ENTITY_AMOEBA_FLOP, BGSoundEvents.AMOEBA_FLOP_ADDITIONS_EVENT);
        Registry.register(BuiltInRegistries.SOUND_EVENT, BGSoundEvents.ENTITY_AMOEBA_HURT, BGSoundEvents.AMOEBA_HURT_ADDITIONS_EVENT);
        Registry.register(BuiltInRegistries.SOUND_EVENT, BGSoundEvents.ENTITY_AMOEBA_DEATH, BGSoundEvents.AMOEBA_DEATH_ADDITIONS_EVENT);
        
        Registry.register(BuiltInRegistries.SOUND_EVENT, BGSoundEvents.ENTITY_BIGBEAK_AMBIENT, BGSoundEvents.BIGBEAK_AMBIENT_ADDITIONS_EVENT);
        Registry.register(BuiltInRegistries.SOUND_EVENT, BGSoundEvents.ENTITY_BIGBEAK_HURT, BGSoundEvents.BIGBEAK_HURT_ADDITIONS_EVENT);
        Registry.register(BuiltInRegistries.SOUND_EVENT, BGSoundEvents.ENTITY_BIGBEAK_DEATH, BGSoundEvents.BIGBEAK_DEATH_ADDITIONS_EVENT);
        Registry.register(BuiltInRegistries.SOUND_EVENT, BGSoundEvents.ENTITY_BIGBEAK_JUMP, BGSoundEvents.BIGBEAK_JUMP_ADDITIONS_EVENT);
        Registry.register(BuiltInRegistries.SOUND_EVENT, BGSoundEvents.ENTITY_BIGBEAK_STEP, BGSoundEvents.BIGBEAK_STEP_ADDITIONS_EVENT);
        Registry.register(BuiltInRegistries.SOUND_EVENT, BGSoundEvents.ENTITY_BIGBEAK_STEP_WOOD, BGSoundEvents.BIGBEAK_STEP_WOOD_ADDITIONS_EVENT);
        Registry.register(BuiltInRegistries.SOUND_EVENT, BGSoundEvents.ENTITY_BIGBEAK_GALLOP, BGSoundEvents.BIGBEAK_GALLOP_ADDITIONS_EVENT);

        Registry.register(BuiltInRegistries.SOUND_EVENT, BGSoundEvents.ENTITY_COPPERBUG_AMBIENT, BGSoundEvents.COPPERBUG_AMBIENT_ADDITIONS_EVENT);
        Registry.register(BuiltInRegistries.SOUND_EVENT, BGSoundEvents.ENTITY_COPPERBUG_HURT, BGSoundEvents.COPPERBUG_HURT_ADDITIONS_EVENT);
        Registry.register(BuiltInRegistries.SOUND_EVENT, BGSoundEvents.ENTITY_COPPERBUG_DEATH, BGSoundEvents.COPPERBUG_DEATH_ADDITIONS_EVENT);
        Registry.register(BuiltInRegistries.SOUND_EVENT, BGSoundEvents.ENTITY_COPPERBUG_EAT, BGSoundEvents.COPPERBUG_EAT_ADDITIONS_EVENT);

        Registry.register(BuiltInRegistries.SOUND_EVENT, BGSoundEvents.ENTITY_MOOBOO_AMBIENT, BGSoundEvents.MOOBOO_AMBIENT_ADDITIONS_EVENT);
        Registry.register(BuiltInRegistries.SOUND_EVENT, BGSoundEvents.ENTITY_MOOBOO_HURT, BGSoundEvents.MOOBOO_HURT_ADDITIONS_EVENT);
        Registry.register(BuiltInRegistries.SOUND_EVENT, BGSoundEvents.ENTITY_MOOBOO_DEATH, BGSoundEvents.MOOBOO_DEATH_ADDITIONS_EVENT);
        Registry.register(BuiltInRegistries.SOUND_EVENT, BGSoundEvents.ENTITY_MOOBOO_MILK, BGSoundEvents.MOOBOO_MILK_ADDITIONS_EVENT);

        Registry.register(BuiltInRegistries.SOUND_EVENT, BGSoundEvents.ENTITY_FUNGUSPARENT_AMBIENT, BGSoundEvents.FUNGUSPARENT_AMBIENT_ADDITIONS_EVENT);
        Registry.register(BuiltInRegistries.SOUND_EVENT, BGSoundEvents.ENTITY_FUNGUSPARENT_HURT, BGSoundEvents.FUNGUSPARENT_HURT_ADDITIONS_EVENT);
        Registry.register(BuiltInRegistries.SOUND_EVENT, BGSoundEvents.ENTITY_FUNGUSPARENT_DEATH, BGSoundEvents.FUNGUSPARENT_DEATH_ADDITIONS_EVENT);

        Registry.register(BuiltInRegistries.SOUND_EVENT, BGSoundEvents.ENTITY_NECTAUR_AMBIENT, BGSoundEvents.NECTAUR_AMBIENT_ADDITIONS_EVENT);
        Registry.register(BuiltInRegistries.SOUND_EVENT, BGSoundEvents.ENTITY_NECTAUR_BELLOW, BGSoundEvents.NECTAUR_BELLOW_ADDITIONS_EVENT);
        Registry.register(BuiltInRegistries.SOUND_EVENT, BGSoundEvents.ENTITY_NECTAUR_HURT, BGSoundEvents.NECTAUR_HURT_ADDITIONS_EVENT);
        Registry.register(BuiltInRegistries.SOUND_EVENT, BGSoundEvents.ENTITY_NECTAUR_DEATH, BGSoundEvents.NECTAUR_DEATH_ADDITIONS_EVENT);
        Registry.register(BuiltInRegistries.SOUND_EVENT, BGSoundEvents.ENTITY_NECTAUR_SCREECH, BGSoundEvents.NECTAUR_SCREECH_ADDITIONS_EVENT);

        Registry.register(BuiltInRegistries.SOUND_EVENT, BGSoundEvents.ENTITY_PEST_AMBIENT, BGSoundEvents.PEST_AMBIENT_ADDITIONS_EVENT);
        Registry.register(BuiltInRegistries.SOUND_EVENT, BGSoundEvents.ENTITY_PEST_HURT, BGSoundEvents.PEST_HURT_ADDITIONS_EVENT);
        Registry.register(BuiltInRegistries.SOUND_EVENT, BGSoundEvents.ENTITY_PEST_DEATH, BGSoundEvents.PEST_DEATH_ADDITIONS_EVENT);
        Registry.register(BuiltInRegistries.SOUND_EVENT, BGSoundEvents.ENTITY_PEST_EAT, BGSoundEvents.PEST_EAT_ADDITIONS_EVENT);

        Registry.register(BuiltInRegistries.SOUND_EVENT, BGSoundEvents.ENTITY_WHISKBILL_AMBIENT, BGSoundEvents.WHISKBILL_AMBIENT_ADDITIONS_EVENT);
        Registry.register(BuiltInRegistries.SOUND_EVENT, BGSoundEvents.ENTITY_WHISKBILL_HURT, BGSoundEvents.WHISKBILL_HURT_ADDITIONS_EVENT);
        Registry.register(BuiltInRegistries.SOUND_EVENT, BGSoundEvents.ENTITY_WHISKBILL_DEATH, BGSoundEvents.WHISKBILL_DEATH_ADDITIONS_EVENT);
        Registry.register(BuiltInRegistries.SOUND_EVENT, BGSoundEvents.ENTITY_WHISKBILL_ROAR, BGSoundEvents.WHISKBILL_ROAR_ADDITIONS_EVENT);

        Registry.register(BuiltInRegistries.SOUND_EVENT, BGSoundEvents.HOOK_RETRIEVE, HOOK_RETRIEVE_ADDITIONS_EVENT);
        Registry.register(BuiltInRegistries.SOUND_EVENT, BGSoundEvents.HOOK_HIT, HOOK_HIT_ADDITIONS_EVENT);
        Registry.register(BuiltInRegistries.SOUND_EVENT, BGSoundEvents.HOOK_THROW, HOOK_THROW_ADDITIONS_EVENT);

        Registry.register(BuiltInRegistries.SOUND_EVENT, BGSoundEvents.ITEM_WAR_HORN_USE_ID, WAR_HORN_USE_EVENT);
        Registry.register(BuiltInRegistries.SOUND_EVENT, BGSoundEvents.ITEM_ECHO_GONG_USE_ID, ECHO_GONG_USE_EVENT);
        Registry.register(BuiltInRegistries.SOUND_EVENT, BGSoundEvents.ITEM_ECHO_GONG_CHARGE_ID, ECHO_GONG_CHARGE_EVENT);

        Registry.register(BuiltInRegistries.SOUND_EVENT, BGSoundEvents.ENTITY_WRAITH_AMBIENT, BGSoundEvents.WRAITH_AMBIENT_ADDITIONS_EVENT);
        Registry.register(BuiltInRegistries.SOUND_EVENT, BGSoundEvents.ENTITY_WRAITH_ATTACK, BGSoundEvents.WRAITH_ATTACK_ADDITIONS_EVENT);
        Registry.register(BuiltInRegistries.SOUND_EVENT, BGSoundEvents.ENTITY_WRAITH_HURT, BGSoundEvents.WRAITH_HURT_ADDITIONS_EVENT);
        Registry.register(BuiltInRegistries.SOUND_EVENT, BGSoundEvents.ENTITY_WRAITH_DEATH, BGSoundEvents.WRAITH_DEATH_ADDITIONS_EVENT);
        Registry.register(BuiltInRegistries.SOUND_EVENT, BGSoundEvents.ENTITY_WRAITH_FLY, BGSoundEvents.WRAITH_FLY_ADDITIONS_EVENT);
        Registry.register(BuiltInRegistries.SOUND_EVENT, BGSoundEvents.ENTITY_WRAITH_TELEPORT, BGSoundEvents.WRAITH_TELEPORT_ADDITIONS_EVENT);

        Registry.register(BuiltInRegistries.SOUND_EVENT, BGSoundEvents.ENTITY_LITHY_AMBIENT, BGSoundEvents.LITHY_AMBIENT_ADDITIONS_EVENT);
        Registry.register(BuiltInRegistries.SOUND_EVENT, BGSoundEvents.ENTITY_LITHY_TRIP, BGSoundEvents.LITHY_TRIP_ADDITIONS_EVENT);
        Registry.register(BuiltInRegistries.SOUND_EVENT, BGSoundEvents.ENTITY_LITHY_HURT, BGSoundEvents.LITHY_HURT_ADDITIONS_EVENT);
        Registry.register(BuiltInRegistries.SOUND_EVENT, BGSoundEvents.ENTITY_LITHY_DEATH, BGSoundEvents.LITHY_DEATH_ADDITIONS_EVENT);

        Registry.register(BuiltInRegistries.SOUND_EVENT, BGSoundEvents.ENTITY_SABEAST_AMBIENT, BGSoundEvents.SABEAST_AMBIENT_ADDITIONS_EVENT);
        Registry.register(BuiltInRegistries.SOUND_EVENT, BGSoundEvents.ENTITY_SABEAST_ATTACK, BGSoundEvents.SABEAST_ATTACK_ADDITIONS_EVENT);
        Registry.register(BuiltInRegistries.SOUND_EVENT, BGSoundEvents.ENTITY_SABEAST_HURT, BGSoundEvents.SABEAST_HURT_ADDITIONS_EVENT);
        Registry.register(BuiltInRegistries.SOUND_EVENT, BGSoundEvents.ENTITY_SABEAST_DEATH, BGSoundEvents.SABEAST_DEATH_ADDITIONS_EVENT);

        Registry.register(BuiltInRegistries.SOUND_EVENT, BGSoundEvents.BLOCK_MEGALITH_BLOCK_IDLE, BLOCK_MEGALITH_BLOCK_IDLE_ADDITIONS_EVENT);

    }
}
