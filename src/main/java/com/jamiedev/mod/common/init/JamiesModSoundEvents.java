package com.jamiedev.mod.common.init;

import com.jamiedev.mod.common.JamiesMod;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
public class JamiesModSoundEvents
{
    public static final Identifier AMBIENT_ANCIENTFOREST_ADDITIONS = JamiesMod.getModId("ambient.underhang.additions");
    public static final Identifier AMBIENT_AMBERDESERT_ADDITIONS = JamiesMod.getModId("ambient.amber_desert.additions");
    public static final Identifier AMBIENT_PRIMORDIALOCEAN_ADDITIONS = JamiesMod.getModId("ambient.primordial_ocean.additions");
    public static final Identifier MUSIC_ANCIENTFOREST_ADDITIONS = JamiesMod.getModId("music.bygone.underhang");
    public static final Identifier MUSIC_PRIMORDIALOCEAN_ADDITIONS = JamiesMod.getModId("music.bygone.primordial_ocean");
    public static final Identifier ENTITY_BIGBEAK_AMBIENT = JamiesMod.getModId("entity.bigbeak.ambient");
    public static final Identifier ENTITY_BIGBEAK_HURT = JamiesMod.getModId("entity.bigbeak.hurt");
    public static final Identifier ENTITY_BIGBEAK_DEATH = JamiesMod.getModId("entity.bigbeak.death");
    public static final Identifier ENTITY_BIGBEAK_JUMP = JamiesMod.getModId("entity.bigbeak.jump");
    public static final Identifier ENTITY_BIGBEAK_STEP = JamiesMod.getModId("entity.bigbeak.step");
    public static final Identifier ENTITY_BIGBEAK_STEP_WOOD = JamiesMod.getModId("entity.bigbeak.step_wood");
    public static final Identifier ENTITY_BIGBEAK_GALLOP = JamiesMod.getModId("entity.bigbeak.gallop");
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



    private static SoundEvent register(String id) {
        return Registry.register(Registries.SOUND_EVENT, JamiesMod.getModId(id), SoundEvent.of(JamiesMod.getModId(id)));
    }

    private static RegistryEntry.Reference<SoundEvent> registerReference(String id) {
        return registerReference(JamiesMod.getModId(id), JamiesMod.getModId(id));
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

    }
}
