package com.jamiedev.bygone.core.registry;

import com.jamiedev.bygone.Bygone;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
public class BGSoundEvents
{
    public static final ResourceLocation AMBIENT_ANCIENTFOREST_ADDITIONS = Bygone.id("ambient.underhang.additions");
    public static final ResourceLocation AMBIENT_AMBERDESERT_ADDITIONS = Bygone.id("ambient.amber_desert.additions");
    public static final ResourceLocation AMBIENT_PRIMORDIALOCEAN_ADDITIONS = Bygone.id("ambient.primordial_ocean.additions");
    public static final ResourceLocation MUSIC_ALPHAHANG_ADDITIONS = Bygone.id("music.bygone.alphahang");
    public static final ResourceLocation MUSIC_ANCIENTFOREST_ADDITIONS = Bygone.id("music.bygone.underhang");
    public static final ResourceLocation MUSIC_PRIMORDIALOCEAN_ADDITIONS = Bygone.id("music.bygone.primordial_ocean");
    public static final ResourceLocation AMBIENT_SHELFHOLLOW_ADDITIONS = Bygone.id("ambient.shelfhollow.additions");

    public static final ResourceLocation ENTITY_BIGBEAK_AMBIENT = Bygone.id("entity.bigbeak.ambient");
    public static final ResourceLocation ENTITY_BIGBEAK_HURT = Bygone.id("entity.bigbeak.hurt");
    public static final ResourceLocation ENTITY_BIGBEAK_DEATH = Bygone.id("entity.bigbeak.death");
    public static final ResourceLocation ENTITY_BIGBEAK_JUMP = Bygone.id("entity.bigbeak.jump");
    public static final ResourceLocation ENTITY_BIGBEAK_STEP = Bygone.id("entity.bigbeak.step");
    public static final ResourceLocation ENTITY_BIGBEAK_STEP_WOOD = Bygone.id("entity.bigbeak.step_wood");
    public static final ResourceLocation ENTITY_BIGBEAK_GALLOP = Bygone.id("entity.bigbeak.gallop");

    public static final ResourceLocation ENTITY_COPPERBUG_AMBIENT = Bygone.id("entity.copperbug.ambient");
    public static final ResourceLocation ENTITY_COPPERBUG_HURT = Bygone.id("entity.copperbug.hurt");
    public static final ResourceLocation ENTITY_COPPERBUG_DEATH = Bygone.id("entity.copperbug.death");
    public static final ResourceLocation ENTITY_COPPERBUG_EAT = Bygone.id("entity.copperbug.eat");

    public static final ResourceLocation ENTITY_MOOBOO_AMBIENT = Bygone.id("entity.mooboo.ambient");
    public static final ResourceLocation ENTITY_MOOBOO_HURT = Bygone.id("entity.mooboo.hurt");
    public static final ResourceLocation ENTITY_MOOBOO_DEATH = Bygone.id("entity.mooboo.death");

    public static final ResourceLocation ENTITY_FUNGUSPARENT_AMBIENT = Bygone.id("entity.fungus_parent.ambient");
    public static final ResourceLocation ENTITY_FUNGUSPARENT_BABY_AMBIENT = Bygone.id("entity.fungus_parent.ambient_baby");
    public static final ResourceLocation ENTITY_FUNGUSPARENT_HURT = Bygone.id("entity.fungus_parent.hurt");
    public static final ResourceLocation ENTITY_FUNGUSPARENT_DEATH = Bygone.id("entity.fungus_parent.death");

    public static final ResourceLocation ENTITY_PEST_AMBIENT = Bygone.id("entity.pest.ambient");
    public static final ResourceLocation ENTITY_PEST_HURT = Bygone.id("entity.pest.hurt");
    public static final ResourceLocation ENTITY_PEST_DEATH = Bygone.id("entity.pest.death");
    public static final ResourceLocation ENTITY_PEST_EAT = Bygone.id("entity.pest.eat");

    public static final ResourceLocation ENTITY_WHISKBILL_AMBIENT = Bygone.id("entity.whiskbill.ambient");
    public static final ResourceLocation ENTITY_WHISKBILL_HURT = Bygone.id("entity.whiskbill.hurt");
    public static final ResourceLocation ENTITY_WHISKBILL_DEATH = Bygone.id("entity.whiskbill.death");
    public static final ResourceLocation ENTITY_WHISKBILL_ROAR = Bygone.id("entity.whiskbill.roar");

    public static final ResourceLocation ENTITY_NECTAUR_AMBIENT = Bygone.id("entity.nectaur.ambient");
    public static final ResourceLocation ENTITY_NECTAUR_BELLOW = Bygone.id("entity.nectaur.bellow");
    public static final ResourceLocation ENTITY_NECTAUR_HURT = Bygone.id("entity.nectaur.hurt");
    public static final ResourceLocation ENTITY_NECTAUR_DEATH = Bygone.id("entity.nectaur.death");
    public static final ResourceLocation ENTITY_NECTAUR_SCREECH = Bygone.id("entity.nectaur.screech");

    public static final ResourceLocation HOOK_RETRIEVE = Bygone.id("entity.hook.retrieve");
    public static final ResourceLocation HOOK_HIT = Bygone.id("entity.hook.hit");
    public static final ResourceLocation HOOK_THROW = Bygone.id("entity.hook.throw");

    public static final SoundEvent HOOK_RETRIEVE_ADDITIONS_EVENT = SoundEvent.createVariableRangeEvent(HOOK_RETRIEVE);
    public static final SoundEvent HOOK_HIT_ADDITIONS_EVENT = SoundEvent.createVariableRangeEvent(HOOK_HIT);
    public static final SoundEvent HOOK_THROW_ADDITIONS_EVENT = SoundEvent.createVariableRangeEvent(HOOK_THROW);
    
    public static SoundEvent AMBIENT_ANCIENTFOREST_ADDITIONS_EVENT = SoundEvent.createVariableRangeEvent(AMBIENT_ANCIENTFOREST_ADDITIONS);
    public static SoundEvent AMBIENT_AMBERDESERT_ADDITIONS_EVENT = SoundEvent.createVariableRangeEvent(AMBIENT_AMBERDESERT_ADDITIONS);
    public static SoundEvent AMBIENT_PRIMORDIALOCEAN_ADDITIONS_EVENT = SoundEvent.createVariableRangeEvent(AMBIENT_PRIMORDIALOCEAN_ADDITIONS);
    public static SoundEvent MUSIC_ALPHAHANG_ADDITIONS_EVENT = SoundEvent.createVariableRangeEvent(MUSIC_ALPHAHANG_ADDITIONS);
    public static SoundEvent MUSIC_ANCIENTFOREST_ADDITIONS_EVENT = SoundEvent.createVariableRangeEvent(MUSIC_ANCIENTFOREST_ADDITIONS);
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
        return Registry.register(BuiltInRegistries.SOUND_EVENT, Bygone.id(id), SoundEvent.createVariableRangeEvent(Bygone.id(id)));
    }

    private static Holder.Reference<SoundEvent> registerReference(String id) {
        return registerReference(Bygone.id(id), Bygone.id(id));
    }

    private static Holder.Reference<SoundEvent> registerReference(ResourceLocation id, ResourceLocation soundId) {
        return Registry.registerForHolder(BuiltInRegistries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(soundId));
    }

    public static void init()
    {
        Registry.register(BuiltInRegistries.SOUND_EVENT, BGSoundEvents.AMBIENT_ANCIENTFOREST_ADDITIONS, BGSoundEvents.AMBIENT_ANCIENTFOREST_ADDITIONS_EVENT);
        Registry.register(BuiltInRegistries.SOUND_EVENT, BGSoundEvents.AMBIENT_AMBERDESERT_ADDITIONS, BGSoundEvents.AMBIENT_AMBERDESERT_ADDITIONS_EVENT);
        Registry.register(BuiltInRegistries.SOUND_EVENT, BGSoundEvents.AMBIENT_PRIMORDIALOCEAN_ADDITIONS, BGSoundEvents.AMBIENT_PRIMORDIALOCEAN_ADDITIONS_EVENT);
        Registry.register(BuiltInRegistries.SOUND_EVENT, BGSoundEvents.AMBIENT_SHELFHOLLOW_ADDITIONS, BGSoundEvents.AMBIENT_SHELFHOLLOW_ADDITIONS_EVENT);
        Registry.register(BuiltInRegistries.SOUND_EVENT, BGSoundEvents.MUSIC_ALPHAHANG_ADDITIONS, BGSoundEvents.MUSIC_ALPHAHANG_ADDITIONS_EVENT);
        Registry.register(BuiltInRegistries.SOUND_EVENT, BGSoundEvents.MUSIC_ANCIENTFOREST_ADDITIONS, BGSoundEvents.MUSIC_ANCIENTFOREST_ADDITIONS_EVENT);
        Registry.register(BuiltInRegistries.SOUND_EVENT, BGSoundEvents.MUSIC_PRIMORDIALOCEAN_ADDITIONS, BGSoundEvents.MUSIC_PRIMORDIALOCEAN_ADDITIONS_EVENT);

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
    }
}
