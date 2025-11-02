package com.jamiedev.bygone.client.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

public class SablossomParticle extends TextureSheetParticle {

    protected SablossomParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        this.friction = 0.96F;
        this.speedUpWhenYMotionIsBlocked = true;
        this.quadSize *= 0.75F;
        this.xd *= 0.8F;
        this.yd *= 0.8F;
        this.zd *= 0.8F;
    }

    private static float magic(float f, float g, float h) {
        if (f >= 1.0F - g) {
            return (1.0F - f) / g;
        } else {
            return f <= h ? f / h : 1.0F;
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level.getBlockState(BlockPos.containing(this.x, this.y, this.z)).isAir()) {
            this.remove();
        } else {
            this.setAlpha(magic(this.clamp(this.age), 0.3F, 0.5F));
            if (Math.random() > 0.95 || this.age == 1) {
                this.setParticleSpeed(-0.05F + 0.1F * Math.random(), -0.05F + 0.1F * Math.random(), -0.05F + 0.1F * Math.random());
            }
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public int getLightColor(float partialTick) {
        float f = ((float) this.age + partialTick) / (float) this.lifetime;
        f = Mth.clamp(f, 0.0F, 1.0F);
        int i = super.getLightColor(partialTick);
        int j = i & 0xFF;
        int k = i >> 16 & 0xFF;
        j += (int) (f * 15.0F * 16.0F);
        if (j > 240) {
            j = 240;
        }

        return j | k << 16;
    }

    private float clamp(float f) {
        return Mth.clamp(f / this.lifetime, 0.0F, 1.0F);
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteProvider;

        public Factory(SpriteSet spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(
                SimpleParticleType type,
                ClientLevel level,
                double x,
                double y,
                double z,
                double xSpeed,
                double ySpeed,
                double zSpeed
        ) {
            SablossomParticle fireflyParticle = new SablossomParticle(
                    level, x, y, z, 0.5 - level.random.nextDouble(),
                    level.random.nextBoolean() ? ySpeed : -ySpeed, 0.5 - level.random.nextDouble()
            );
            fireflyParticle.setLifetime(level.random.nextIntBetweenInclusive(200, 300));
            fireflyParticle.scale(1.5F);
            fireflyParticle.pickSprite(this.spriteProvider);
            fireflyParticle.setAlpha(0.0F);
            return fireflyParticle;
        }
    }
}
