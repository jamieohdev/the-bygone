package com.jamiedev.bygone.core.registry;

import com.jamiedev.bygone.Bygone;
import com.kekecreations.jinxedlib.core.util.JinxedRegistryHelper;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.JukeboxSong;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class BGJukeboxSongs
{
    static ResourceKey<JukeboxSong> SHUFFLE = create("shuffle");

    public static <T extends BlockEntity> Supplier<BlockEntityType<T>> register(String name, Supplier<BlockEntityType<T>> type) {
        return JinxedRegistryHelper.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Bygone.MOD_ID, name, type);
    }

    SoundEvents ref;

    private static ResourceKey<JukeboxSong> create(String name) {
        return ResourceKey.create(Registries.JUKEBOX_SONG, Bygone.id(name));
    }

    private static void register(BootstrapContext<JukeboxSong> context, ResourceKey<JukeboxSong> key, Holder.Reference<SoundEvent> soundEvent, int lengthInSeconds, int comparatorOutput) {
        context.register(key, new JukeboxSong(soundEvent, Component.translatable(Util.makeDescriptionId("jukebox_song", key.location())), (float)lengthInSeconds, comparatorOutput));
    }

    private static void register(BootstrapContext<JukeboxSong> context, ResourceKey<JukeboxSong> key, Holder<SoundEvent> sound, float length, int output) {
        context.register(key, new JukeboxSong(sound, Component.translatable(Util.makeDescriptionId("jukebox_song", key.location())), length, output));
    }

    static void bootstrap(BootstrapContext<JukeboxSong> context) {
        register(context, SHUFFLE, BGSoundEvents.MUSIC_DISC_SHUFFLE, 178, 1);
    }
}
