package com.jamiedev.mod.client.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticleGroup;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.MathHelper;

import java.util.Optional;

public class AmberDustParticle extends SpriteBillboardParticle {

    AmberDustParticle(ClientWorld world, SpriteProvider spriteProvider, double x, double y, double z) {
        super(world, x, y - 0.125, z);
        this.setBoundingBoxSpacing(0.01F, 0.01F);
        this.setSprite(spriteProvider);
        this.scale *= this.random.nextFloat() * 0.6F + 0.2F;
        this.maxAge = (int)(16.0 / (Math.random() * 0.8 + 0.2));
        this.collidesWithWorld = false;
        this.velocityMultiplier = 1.0F;
        this.gravityStrength = 0.0F;
    }

    AmberDustParticle(ClientWorld world, SpriteProvider spriteProvider, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        super(world, x, y - 0.125, z, velocityX, velocityY, velocityZ);
        this.setBoundingBoxSpacing(0.01F, 0.01F);
        this.setSprite(spriteProvider);
        this.scale *= this.random.nextFloat() * 0.6F + 0.6F;
        this.maxAge = (int)(16.0 / (Math.random() * 0.8 + 0.2));
        this.collidesWithWorld = false;
        this.velocityMultiplier = 1.0F;
        this.gravityStrength = 0.0F;
    }
    protected  AmberDustParticle(ClientWorld world, double x, double y, double z, float randomVelocityXMultiplier, float randomVelocityYMultiplier, float randomVelocityZMultiplier, double velocityX, double velocityY, double velocityZ, float scaleMultiplier, SpriteProvider spriteProvider, float colorMultiplier, int baseMaxAge, float gravityStrength, boolean collidesWithWorld) {
        super(world, x, y, z);
        this.setBoundingBoxSpacing(0.01F, 0.01F);
        this.setSprite(spriteProvider);
        this.scale *= this.random.nextFloat() * 0.6F + 0.2F;
        this.maxAge = (int)(16.0 / (Math.random() * 0.8 + 0.2));
        this.collidesWithWorld = false;
        this.velocityMultiplier = 1.0F;
        this.gravityStrength = 0.0F;
    }

    public  AmberDustParticle(AmberDustParticle.SporeBlossomAirFactory sporeBlossomAirFactory, ClientWorld clientWorld, SpriteProvider spriteProvider, double d, double e, double f, double v, double v1, double v2) {
        super(clientWorld, d, e, f);
    }

    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
    }

    public static class SporeBlossomAirFactory implements ParticleFactory<SimpleParticleType> {
        private final SpriteProvider spriteProvider;

        public SporeBlossomAirFactory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            AmberDustParticle waterSuspendParticle = new AmberDustParticle(this, clientWorld,
                    this.spriteProvider, d, e, f, 0.0, -0.800000011920929, 0.0) {
                public Optional<ParticleGroup> getGroup() {
                    return Optional.of(ParticleGroup.SPORE_BLOSSOM_AIR);
                }
            };
            waterSuspendParticle.maxAge = MathHelper.nextBetween(clientWorld.random, 500, 1000);
            waterSuspendParticle.gravityStrength = 0.01F;
            waterSuspendParticle.setColor(0.582F, 0.448F, 0.082F);
            return waterSuspendParticle;
        }
    }

    @Environment(value = EnvType.CLIENT)
    public static class Factory
            implements ParticleFactory<SimpleParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            double j = (double)clientWorld.random.nextFloat() * -1.9 * (double)clientWorld.random.nextFloat() * 0.1;
            AmberDustParticle waterSuspendParticle = new  AmberDustParticle(clientWorld, this.spriteProvider, d, e, f, 0.0, j, 0.0);
            waterSuspendParticle.setColor(0.882F, 0.448F, 0.005F);
            waterSuspendParticle.setBoundingBoxSpacing(0.001F, 0.001F);
            return waterSuspendParticle;
        }

    }
}