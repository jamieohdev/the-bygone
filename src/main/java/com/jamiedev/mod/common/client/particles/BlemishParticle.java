package com.jamiedev.mod.common.client.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.PortalParticle;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;

public class BlemishParticle extends PortalParticle
{
    private final double startX;
    private final double startY;
    private final double startZ;
    protected BlemishParticle(ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
        super(clientWorld, d, e, f, g, h, i);
        this.startX = this.x;
        this.startY = this.y;
        this.startZ = this.z;
    }

    public float getSize(float p_107608_) {
        float f = 1.0F - ((float)this.age + p_107608_) / ((float)this.maxAge * 1.5F);
        return this.scale * f;
    }

    public void tick() {
        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;
        if (this.age++ >= this.maxAge) {
            this.markDead();
        } else {
            float f = (float)this.age / (float)this.maxAge;


            this.x += this.velocityX * (double)f;
            this.y += this.velocityY * (double)f;
            this.z += this.velocityZ * (double)f;

            this.setPos(this.x, this.y, this.z);
        }
    }

    @Environment(value = EnvType.CLIENT)
    public static class BlemishBlockProvider implements ParticleFactory<SimpleParticleType> {
        private final SpriteProvider sprite;

        public BlemishBlockProvider(SpriteProvider p_107611_) {
            this.sprite = p_107611_;
        }

        public Particle createParticle(SimpleParticleType p_107622_, ClientWorld p_107623_, double p_107624_, double p_107625_, double p_107626_, double p_107627_, double p_107628_, double p_107629_) {
            BlemishParticle particle = new BlemishParticle(p_107623_, p_107624_, p_107625_, p_107626_, p_107627_, p_107628_, p_107629_);
            particle.setSprite(this.sprite);
            return particle;
        }
    }
}
