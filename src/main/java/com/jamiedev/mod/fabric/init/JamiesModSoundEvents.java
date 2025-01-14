package com.jamiedev.mod.fabric.init;

import com.jamiedev.mod.fabric.JamiesModFabric;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
public class JamiesModSoundEvents
{
    public static final Identifier AMBIENT_ANCIENTFOREST_ADDITIONS = JamiesModFabric.getModId("ambient.underhang.additions");
    public static final Identifier AMBIENT_AMBERDESERT_ADDITIONS = JamiesModFabric.getModId("ambient.amber_desert.additions");
    public static final Identifier AMBIENT_PRIMORDIALOCEAN_ADDITIONS = JamiesModFabric.getModId("ambient.primordial_ocean.additions");
    public static final Identifier MUSIC_ANCIENTFOREST_ADDITIONS = JamiesModFabric.getModId("music.bygone.underhang");
    public static final Identifier MUSIC_PRIMORDIALOCEAN_ADDITIONS = JamiesModFabric.getModId("music.bygone.primordial_ocean");
    public static final Identifier ENTITY_BIGBEAK_AMBIENT = JamiesModFabric.getModId("entity.bigbeak.ambient");
    public static final Identifier ENTITY_BIGBEAK_HURT = JamiesModFabric.getModId("entity.bigbeak.hurt");
    public static final Identifier ENTITY_BIGBEAK_DEATH = JamiesModFabric.getModId("entity.bigbeak.death");
    public static final Identifier ENTITY_BIGBEAK_JUMP = JamiesModFabric.getModId("entity.bigbeak.jump");
    public static final Identifier ENTITY_BIGBEAK_STEP = JamiesModFabric.getModId("entity.bigbeak.step");
    public static final Identifier ENTITY_BIGBEAK_STEP_WOOD = JamiesModFabric.getModId("entity.bigbeak.step_wood");
    public static final Identifier ENTITY_BIGBEAK_GALLOP = JamiesModFabric.getModId("entity.bigbeak.gallop");

    public static final Identifier ENTITY_MOOBOO_AMBIENT = JamiesModFabric.getModId("entity.mooboo.ambient");
    public static final Identifier ENTITY_MOOBOO_HURT = JamiesModFabric.getModId("entity.mooboo.hurt");
    public static final Identifier ENTITY_MOOBOO_DEATH = JamiesModFabric.getModId("entity.mooboo.death");

    public static final Identifier ENTITY_FUNGUSPARENT_AMBIENT = JamiesModFabric.getModId("entity.fungus_parent.ambient");
    public static final Identifier ENTITY_FUNGUSPARENT_BABY_AMBIENT = JamiesModFabric.getModId("entity.fungus_parent.ambient_baby");
    public static final Identifier ENTITY_FUNGUSPARENT_HURT = JamiesModFabric.getModId("entity.fungus_parent.hurt");
    public static final Identifier ENTITY_FUNGUSPARENT_DEATH = JamiesModFabric.getModId("entity.fungus_parent.death");
    
     public static SoundEvent AMBIENT_ANCIENTFOREST_ADDITIONS_EVENT = SoundEvent.of(AMBIENT_ANCIENTFOREST_ADDITIONS);
    public static SoundEvent AMBIENT_AMBERDESERT_ADDITIONS_EVENT = SoundEvent.of(AMBIENT_AMBERDESERT_ADDITIONS);
    public static SoundEvent AMBIENT_PRIMORDIALOCEAN_ADDITIONS_EVENT = SoundEvent.of(AMBIENT_PRIMORDIALOCEAN_ADDITIONS);
     public static SoundEvent MUSIC_ANCIENTFOREST_ADDITIONS_EVENT = SoundEvent.of(MUSIC_ANCIENTFOREST_ADDITIONS);
    public static SoundEvent MUSIC_PRIMORDIALOCEAN_ADDITIONS_EVENT = SoundEvent.of(MUSIC_PRIMORDIALOCEAN_ADDITIONS);

    public static SoundEvent BIGBEAK_AMBIENT_ADDITIONS_EVENT = SoundEvent.of(ENTITY_BIGBEAK_AMBIENT);
    public static SoundEvent BIGBEAK_HURT_ADDITIONS_EVENT = SoundEvent.of(ENTITY_BIGBEAK_HURT);
    public static SoundEvent BIGBEAK_DEATH_ADDITIONS_EVENT = SoundEvent.of(ENTITY_BIGBEAK_DEATH);
    public static SoundEvent BIGBEAK_JUMP_ADDITIONS_EVENT = SoundEvent.of(ENTITY_BIGBEAK_JUMP);
    public static SoundEvent BIGBEAK_STEP_ADDITIONS_EVENT = SoundEvent.of(ENTITY_BIGBEAK_STEP);
    public static SoundEvent BIGBEAK_STEP_WOOD_ADDITIONS_EVENT = SoundEvent.of(ENTITY_BIGBEAK_STEP_WOOD);
    public static SoundEvent BIGBEAK_GALLOP_ADDITIONS_EVENT = SoundEvent.of(ENTITY_BIGBEAK_GALLOP);

    public static SoundEvent MOOBOO_AMBIENT_ADDITIONS_EVENT = SoundEvent.of(ENTITY_MOOBOO_AMBIENT);
    public static SoundEvent MOOBOO_HURT_ADDITIONS_EVENT = SoundEvent.of(ENTITY_MOOBOO_HURT);
    public static SoundEvent MOOBOO_DEATH_ADDITIONS_EVENT = SoundEvent.of(ENTITY_MOOBOO_DEATH);

    public static SoundEvent FUNGUSPARENT_AMBIENT_ADDITIONS_EVENT = SoundEvent.of(ENTITY_FUNGUSPARENT_AMBIENT);
    public static SoundEvent FUNGUSPARENT_AMBIENT_BABY_ADDITIONS_EVENT = SoundEvent.of(ENTITY_FUNGUSPARENT_BABY_AMBIENT);
    public static SoundEvent FUNGUSPARENT_HURT_ADDITIONS_EVENT = SoundEvent.of(ENTITY_FUNGUSPARENT_HURT);
    public static SoundEvent FUNGUSPARENT_DEATH_ADDITIONS_EVENT = SoundEvent.of(ENTITY_FUNGUSPARENT_DEATH);


    private static SoundEvent register(String id) {
        return Registry.register(Registries.SOUND_EVENT, JamiesModFabric.getModId(id), SoundEvent.of(JamiesModFabric.getModId(id)));
    }

    private static RegistryEntry.Reference<SoundEvent> registerReference(String id) {
        return registerReference(JamiesModFabric.getModId(id), JamiesModFabric.getModId(id));
    }

    private static RegistryEntry.Reference<SoundEvent> registerReference(Identifier id, Identifier soundId) {
        return Registry.registerReference(Registries.SOUND_EVENT, id, SoundEvent.of(soundId));
    }

    public static void init()
    {
        Registry.register(Registries.SOUND_EVENT, JamiesModSoundEvents.AMBIENT_ANCIENTFOREST_ADDITIONS, JamiesModSoundEvents.AMBIENT_ANCIENTFOREST_ADDITIONS_EVENT);
        Registry.register(Registries.SOUND_EVENT, JamiesModSoundEvents.AMBIENT_AMBERDESERT_ADDITIONS, JamiesModSoundEvents.AMBIENT_AMBERDESERT_ADDITIONS_EVENT);
        Registry.register(Registries.SOUND_EVENT, JamiesModSoundEvents.AMBIENT_PRIMORDIALOCEAN_ADDITIONS, JamiesModSoundEvents.AMBIENT_PRIMORDIALOCEAN_ADDITIONS_EVENT);
        Registry.register(Registries.SOUND_EVENT, JamiesModSoundEvents.MUSIC_ANCIENTFOREST_ADDITIONS, JamiesModSoundEvents.MUSIC_ANCIENTFOREST_ADDITIONS_EVENT);
        Registry.register(Registries.SOUND_EVENT, JamiesModSoundEvents.MUSIC_PRIMORDIALOCEAN_ADDITIONS, JamiesModSoundEvents.MUSIC_PRIMORDIALOCEAN_ADDITIONS_EVENT);

        Registry.register(Registries.SOUND_EVENT, JamiesModSoundEvents.ENTITY_BIGBEAK_AMBIENT, JamiesModSoundEvents.BIGBEAK_AMBIENT_ADDITIONS_EVENT);
        Registry.register(Registries.SOUND_EVENT, JamiesModSoundEvents.ENTITY_BIGBEAK_HURT, JamiesModSoundEvents.BIGBEAK_HURT_ADDITIONS_EVENT);
        Registry.register(Registries.SOUND_EVENT, JamiesModSoundEvents.ENTITY_BIGBEAK_DEATH, JamiesModSoundEvents.BIGBEAK_DEATH_ADDITIONS_EVENT);
        Registry.register(Registries.SOUND_EVENT, JamiesModSoundEvents.ENTITY_BIGBEAK_JUMP, JamiesModSoundEvents.BIGBEAK_JUMP_ADDITIONS_EVENT);
        Registry.register(Registries.SOUND_EVENT, JamiesModSoundEvents.ENTITY_BIGBEAK_STEP, JamiesModSoundEvents.BIGBEAK_STEP_ADDITIONS_EVENT);
        Registry.register(Registries.SOUND_EVENT, JamiesModSoundEvents.ENTITY_BIGBEAK_STEP_WOOD, JamiesModSoundEvents.BIGBEAK_STEP_WOOD_ADDITIONS_EVENT);
        Registry.register(Registries.SOUND_EVENT, JamiesModSoundEvents.ENTITY_BIGBEAK_GALLOP, JamiesModSoundEvents.BIGBEAK_GALLOP_ADDITIONS_EVENT);

        Registry.register(Registries.SOUND_EVENT, JamiesModSoundEvents.ENTITY_MOOBOO_AMBIENT, JamiesModSoundEvents.MOOBOO_AMBIENT_ADDITIONS_EVENT);
        Registry.register(Registries.SOUND_EVENT, JamiesModSoundEvents.ENTITY_MOOBOO_HURT, JamiesModSoundEvents.MOOBOO_HURT_ADDITIONS_EVENT);
        Registry.register(Registries.SOUND_EVENT, JamiesModSoundEvents.ENTITY_MOOBOO_DEATH, JamiesModSoundEvents.MOOBOO_DEATH_ADDITIONS_EVENT);
        
        Registry.register(Registries.SOUND_EVENT, JamiesModSoundEvents.ENTITY_FUNGUSPARENT_AMBIENT, JamiesModSoundEvents.FUNGUSPARENT_AMBIENT_ADDITIONS_EVENT);
        Registry.register(Registries.SOUND_EVENT, JamiesModSoundEvents.ENTITY_FUNGUSPARENT_HURT, JamiesModSoundEvents.FUNGUSPARENT_HURT_ADDITIONS_EVENT);
        Registry.register(Registries.SOUND_EVENT, JamiesModSoundEvents.ENTITY_FUNGUSPARENT_DEATH, JamiesModSoundEvents.FUNGUSPARENT_DEATH_ADDITIONS_EVENT);

    }
}
