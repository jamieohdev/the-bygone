package com.jamiedev.bygone.client.particles;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.ScalableParticleOptionsBase;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class LithoParticleOptions extends ScalableParticleOptionsBase {
    public static final Vector3f PLASM_PARTICLE_COLOR = Vec3.fromRGB24(14151396).toVector3f();
    public static final net.minecraft.core.particles.DustParticleOptions REDSTONE;
    public static final MapCodec<DustParticleOptions> CODEC;
    public static final StreamCodec<RegistryFriendlyByteBuf, DustParticleOptions> STREAM_CODEC;

    static {
        REDSTONE = new net.minecraft.core.particles.DustParticleOptions(PLASM_PARTICLE_COLOR, 1.0F);
        CODEC = RecordCodecBuilder.mapCodec((p_341566_) -> p_341566_.group(ExtraCodecs.VECTOR3F.fieldOf("color").forGetter(DustParticleOptions::getColor), SCALE.fieldOf("scale").forGetter(ScalableParticleOptionsBase::getScale)).apply(p_341566_, net.minecraft.core.particles.DustParticleOptions::new));
        STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VECTOR3F, DustParticleOptions::getColor, ByteBufCodecs.FLOAT, ScalableParticleOptionsBase::getScale, net.minecraft.core.particles.DustParticleOptions::new);
    }

    private final Vector3f color;

    public LithoParticleOptions(Vector3f color, float scale) {
        super(scale);
        this.color = color;
    }

    public ParticleType<DustParticleOptions> getType() {
        return ParticleTypes.DUST;
    }

    public Vector3f getColor() {
        return this.color;
    }
}

