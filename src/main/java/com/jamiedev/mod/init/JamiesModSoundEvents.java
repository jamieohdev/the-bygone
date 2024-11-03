package com.jamiedev.mod.init;

import com.jamiedev.mod.JamiesMod;
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
     public static SoundEvent AMBIENT_ANCIENTFOREST_ADDITIONS_EVENT = SoundEvent.of(AMBIENT_ANCIENTFOREST_ADDITIONS);
    public static SoundEvent AMBIENT_AMBERDESERT_ADDITIONS_EVENT = SoundEvent.of(AMBIENT_AMBERDESERT_ADDITIONS);
    public static SoundEvent AMBIENT_PRIMORDIALOCEAN_ADDITIONS_EVENT = SoundEvent.of(AMBIENT_PRIMORDIALOCEAN_ADDITIONS);
     public static SoundEvent MUSIC_ANCIENTFOREST_ADDITIONS_EVENT = SoundEvent.of(MUSIC_ANCIENTFOREST_ADDITIONS);
    public static SoundEvent MUSIC_PRIMORDIALOCEAN_ADDITIONS_EVENT = SoundEvent.of(MUSIC_PRIMORDIALOCEAN_ADDITIONS);
    public static final SoundEvent BLOCK_JAMIESBLOCK_BREAK = register("block.jamiesblock.break");

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
    }
}
