package com.jamiedev.bygone.client.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.tags.FluidTags;

public class WormParticle extends TextureSheetParticle {

    WormParticle(ClientLevel world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteSet spriteProvider) {
        super(world, x, y, z, 0, 0, 0);
        this.setSize(0.02F, 0.02F);
        this.pickSprite(spriteProvider);
        this.quadSize = 0.1F + this.random.nextFloat() * 0.05F;
        this.lifetime = 40 + this.random.nextInt(20);
        this.gravity = -0.04F;
        
        this.xd = velocityX;
        this.yd = velocityY;
        this.zd = velocityZ;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        
        if (this.lifetime-- <= 0) {
            this.remove();
            return;
        }
        
        BlockPos pos = BlockPos.containing(this.x, this.y, this.z);
        BlockPos below = pos.below();
        
        boolean isInWater = this.level.getFluidState(pos).is(FluidTags.WATER);
        boolean isWaterBelow = this.level.getFluidState(below).is(FluidTags.WATER);
        boolean isAtSurface = isWaterBelow && !isInWater;
        
        if (!isInWater && !isWaterBelow) {
            this.remove();
            return;
        }
        
        if (isInWater && !isAtSurface) {
            this.yd = 0.04F;
        } else if (isAtSurface) {
            if (this.yd < 0) {
                this.yd = -this.yd * 0.6F;
            } else {
                this.yd *= 0.8F;
            }
        }
        
        this.yd -= (double)this.gravity;
        this.move(this.xd, this.yd, this.zd);
        this.xd *= 0.98F;
        this.zd *= 0.98F;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteProvider;

        public Factory(SpriteSet spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return new WormParticle(world, x, y, z, velocityX, velocityY, velocityZ, this.spriteProvider);
        }
    }
}
