package com.jamiedev.bygone.client.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.SimpleParticleType;

public class UpsidedownDropParticle extends TextureSheetParticle {
    protected UpsidedownDropParticle(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z, (double)0.0F, (double)0.0F, (double)0.0F);
        this.xd *= (double)0.3F;
        this.yd = Math.random() * (double)0.2F + (double)0.1F;
        this.zd *= (double)0.3F;
        this.setSize(0.01F, 0.01F);
        this.gravity = -0.06F;
        this.lifetime = (int)((double)8.0F / (Math.random() * 0.8 + 0.2));
    }

    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.lifetime-- <= 0) {
            this.remove();
        } else {
            this.yd -= (double)this.gravity;
            this.move(this.xd, this.yd, this.zd);
            this.xd *= (double)0.98F;
            this.yd *= (double)0.98F;
            this.zd *= (double)0.98F;
            if (this.onGround) {
                if (Math.random() < (double)0.5F) {
                    this.remove();
                }

                this.xd *= (double)0.7F;
                this.zd *= (double)0.7F;
            }

            BlockPos blockpos = BlockPos.containing(this.x, this.y, this.z);
            double d0 = Math.max(this.level.getBlockState(blockpos).getCollisionShape(this.level, blockpos).max(Direction.Axis.Y, this.x - (double)blockpos.getX(), this.z - (double)blockpos.getZ()), (double)this.level.getFluidState(blockpos).getHeight(this.level, blockpos));
            if (d0 > (double)0.0F && this.y < (double)blockpos.getY() + d0) {
                this.remove();
            }
        }

    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;

        public Provider(SpriteSet sprites) {
            this.sprite = sprites;
        }

        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            UpsidedownDropParticle waterdropparticle = new UpsidedownDropParticle(level, x, y, z);
            waterdropparticle.pickSprite(this.sprite);
            return waterdropparticle;
        }
    }
}
