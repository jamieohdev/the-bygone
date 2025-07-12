package com.jamiedev.bygone.client.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.PortalParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;

public class BlemishParticle extends PortalParticle
{
    ParticleTypes ref;
    private final double startX;
    private final double startY;
    private final double startZ;
    protected BlemishParticle(ClientLevel clientWorld, double d, double e, double f, double g, double h, double i) {
        super(clientWorld, d, e, f, g, h, i);
        this.startX = this.x;
        this.startY = this.y;
        this.startZ = this.z;
    }

    @Override
    public float getQuadSize(float p_107608_) {
        float f = 1.0F - ((float)this.age + p_107608_) / ((float)this.lifetime * 1.5F);
        return this.quadSize * f;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            float f = (float)this.age / (float)this.lifetime;


            this.x += this.xd * (double)f;
            this.y += this.yd * (double)f;
            this.z += this.zd * (double)f;

            this.setPos(this.x, this.y, this.z);
        }
    }

    public static class BlemishBlockProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;

        public BlemishBlockProvider(SpriteSet p_107611_) {
            this.sprite = p_107611_;
        }

        @Override
        public Particle createParticle(SimpleParticleType p_107622_, ClientLevel p_107623_, double p_107624_, double p_107625_, double p_107626_, double p_107627_, double p_107628_, double p_107629_) {
            BlemishParticle particle = new BlemishParticle(p_107623_, p_107624_, p_107625_, p_107626_, p_107627_, p_107628_, p_107629_);
            particle.pickSprite(this.sprite);
            return particle;
        }
    }
}
