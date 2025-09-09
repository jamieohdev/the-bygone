package com.jamiedev.bygone.client.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.ParticleGroup;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import java.util.Optional;

public class RafflesiaSporeParticle extends TextureSheetParticle {

    RafflesiaSporeParticle(ClientLevel world, SpriteSet spriteProvider, double x, double y, double z) {
        super(world, x, y - 0.125, z);
        this.setSize(0.01F, 0.01F);
        this.pickSprite(spriteProvider);
        this.quadSize *= this.random.nextFloat() * 0.6F + 0.2F;
        this.lifetime = (int)(16.0 / (Math.random() * 0.8 + 0.2));
        this.hasPhysics = false;
        this.friction = 1.0F;
        this.gravity = 0.0F;
    }

    RafflesiaSporeParticle(ClientLevel world, SpriteSet spriteProvider, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        super(world, x, y - 0.125, z, velocityX, velocityY, velocityZ);
        this.setSize(0.01F, 0.01F);
        this.pickSprite(spriteProvider);
        this.quadSize *= this.random.nextFloat() * 0.6F + 0.6F;
        this.lifetime = (int)(16.0 / (Math.random() * 0.8 + 0.2));
        this.hasPhysics = false;
        this.friction = 1.0F;
        this.gravity = 0.0F;
    }
    protected RafflesiaSporeParticle(ClientLevel world, double x, double y, double z, float randomVelocityXMultiplier, float randomVelocityYMultiplier, float randomVelocityZMultiplier, double velocityX, double velocityY, double velocityZ, float scaleMultiplier, SpriteSet spriteProvider, float colorMultiplier, int baseMaxAge, float gravityStrength, boolean collidesWithWorld) {
        super(world, x, y, z);
        this.setSize(0.01F, 0.01F);
        this.pickSprite(spriteProvider);
        this.quadSize *= this.random.nextFloat() * 0.6F + 0.2F;
        this.lifetime = (int)(16.0 / (Math.random() * 0.8 + 0.2));
        this.hasPhysics = false;
        this.friction = 1.0F;
        this.gravity = 0.0F;
    }

    public RafflesiaSporeParticle(SporeBlossomAirFactory sporeBlossomAirFactory, ClientLevel clientWorld, SpriteSet spriteProvider, double d, double e, double f, double v, double v1, double v2) {
        super(clientWorld, d, e, f);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public static class SporeBlossomAirFactory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteProvider;

        public SporeBlossomAirFactory(SpriteSet spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public Particle createParticle(SimpleParticleType simpleParticleType, ClientLevel clientWorld, double d, double e, double f, double g, double h, double i) {
            RafflesiaSporeParticle waterSuspendParticle = new RafflesiaSporeParticle(this, clientWorld,
                    this.spriteProvider, d, e, f, 0.0, -0.800000011920929, 0.0) {
                @Override
                public Optional<ParticleGroup> getParticleGroup() {
                    return Optional.of(ParticleGroup.SPORE_BLOSSOM);
                }
            };
            waterSuspendParticle.lifetime = Mth.randomBetweenInclusive(clientWorld.random, 500, 1000);
            waterSuspendParticle.gravity = 0.01F;
            waterSuspendParticle.setColor(0.582F, 0.448F, 0.082F);
            return waterSuspendParticle;
        }
    }

    public static class Factory
            implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteProvider;

        public Factory(SpriteSet spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public Particle createParticle(SimpleParticleType simpleParticleType, ClientLevel clientWorld, double d, double e, double f, double g, double h, double i) {
            double j = (double)clientWorld.random.nextFloat() * -1.9 * (double)clientWorld.random.nextFloat() * 0.1;
            RafflesiaSporeParticle waterSuspendParticle = new RafflesiaSporeParticle(clientWorld, this.spriteProvider, d, e, f, 0.0, j, 0.0);
            waterSuspendParticle.setColor(0.582F, 0.448F, 0.082F);
            waterSuspendParticle.setSize(0.001F, 0.001F);
            return waterSuspendParticle;
        }

    }
}