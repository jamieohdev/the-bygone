package com.jamiedev.bygone.core.registry;

import com.jamiedev.bygone.Bygone;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
public class BGParticleTypes
{

    public static final ParticleType<SimpleParticleType> AMBER_DUST = simple();
    public static final ParticleType<SimpleParticleType> RAFFLESIA_SPORES = simple();
    public static final ParticleType<SimpleParticleType> ALGAE_BLOOM = simple();

    public static final ParticleType<SimpleParticleType> BLEMISH = simple();
    public static final ParticleType<SimpleParticleType> SHELF = simple();
    public static final ParticleType<SimpleParticleType> ANCIENT_LEAVES = simple();
    public static final ParticleType<SimpleParticleType> SABLE_LEAVES = simple();
    public static final ParticleType<SimpleParticleType> UPSIDEDOWN = simple();
    public static final ParticleType<SimpleParticleType> WORM = simple();

    public static void init() {
        Registry.register(BuiltInRegistries.PARTICLE_TYPE, Bygone.id( "rafflesia_spores"), RAFFLESIA_SPORES);
        Registry.register(BuiltInRegistries.PARTICLE_TYPE, Bygone.id( "algae_bloom"), ALGAE_BLOOM);
        Registry.register(BuiltInRegistries.PARTICLE_TYPE, Bygone.id( "blemish_bubble"), BLEMISH);
        Registry.register(BuiltInRegistries.PARTICLE_TYPE, Bygone.id( "amber_dust"), AMBER_DUST);
        Registry.register(BuiltInRegistries.PARTICLE_TYPE, Bygone.id( "shelf"), SHELF);
        Registry.register(BuiltInRegistries.PARTICLE_TYPE, Bygone.id( "ancient_leaves"), ANCIENT_LEAVES);
        Registry.register(BuiltInRegistries.PARTICLE_TYPE, Bygone.id( "sable_leaves"), SABLE_LEAVES);
        Registry.register(BuiltInRegistries.PARTICLE_TYPE, Bygone.id( "upside_down_rain"), UPSIDEDOWN);
        Registry.register(BuiltInRegistries.PARTICLE_TYPE, Bygone.id( "worm"), WORM);
    }

    public static SimpleParticleType simple() {
        return new SimpleParticleType(false) { };
    }

}
