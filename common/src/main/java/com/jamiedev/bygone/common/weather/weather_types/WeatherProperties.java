package com.jamiedev.bygone.common.weather.weather_types;

import com.jamiedev.bygone.Bygone;
import com.jamiedev.bygone.common.weather.BygoneWeather;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

public class WeatherProperties {
    public static class WeatherProperty<T> {
        private T value;
        private boolean isDirty = false;
        public boolean checkDirty() {
            if (isDirty) {
                isDirty = false;
                return true;
            }
            return false;
        }

        public void setValue(T newValue) {
            if (this.value.equals(newValue)) return;
            this.value = newValue;
            this.isDirty = true;
        }

        public T getValue() {
            return this.value;
        }

        private final Codec<T> codec;
        public Codec<T> codec() {
            return this.codec;
        }

        public Optional<Tag> encodeSelf() {
            return codec.encodeStart(NbtOps.INSTANCE, value).result();
        }

        public void appendToTag(CompoundTag compoundTag) {
            encodeSelf().ifPresent((tag) -> compoundTag.put(getIdentifier(), tag));
        }

        public void parseSelf(CompoundTag compoundTag) {
            if (compoundTag.get(this.getIdentifier()) == null) return;
            codec.parse(NbtOps.INSTANCE, compoundTag.get(this.getIdentifier()))
                .result().ifPresent(this::setValue);
        }

        private final String identifier;
        public String getIdentifier() {
            return this.identifier;
        }

        private final ResourceLocation ownerIdentifier;
        public ResourceLocation getOwner() {
            return this.ownerIdentifier;
        }

        private boolean sendToClient = true;
        public boolean shouldSync() { return this.sendToClient; }
        public WeatherProperty<T> setSync(boolean sendToClient) {
            this.sendToClient = sendToClient;
            return this;
        }

        WeatherProperty(String identifier, T value, Codec<T> codec, ResourceLocation ownerIdentifier) {
            this.value = value;
            this.identifier = identifier;
            this.codec = codec;
            this.ownerIdentifier = ownerIdentifier;
        }
    }

    public static WeatherProperty<Boolean> ofBool(String identifier, Boolean defaultValue, ResourceLocation ownerIdentifier) {
        return new WeatherProperty<Boolean>(
            identifier, defaultValue, Codec.BOOL, ownerIdentifier);
    }

    public static WeatherProperty<Integer> ofInt(String identifier, Integer defaultValue, ResourceLocation ownerIdentifier) {
        return new WeatherProperty<Integer>(
            identifier, defaultValue, Codec.INT, ownerIdentifier);
    }

    public static WeatherProperty<Float> ofFloat(String identifier, Float defaultValue, ResourceLocation ownerIdentifier) {
        return new WeatherProperty<Float>(
            identifier, defaultValue, Codec.FLOAT, ownerIdentifier);
    }
}