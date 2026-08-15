package com.jamiedev.bygone.client.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;

public class ArcaneSymbolParticle extends TextureSheetParticle {

	public ArcaneSymbolParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
		super(level, x, y, z);
		this.setSize(1, 1);
		this.quadSize = 0.2F;
		this.setAlpha(1);
		this.xd = xSpeed;
		this.yd = ySpeed;
		this.zd = zSpeed;
		this.hasPhysics = false;
		this.lifetime = (int)(Math.random() * (double)10.0F) + 30;
	}

	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
	}

	@Override
	public void move(double x, double y, double z) {
		this.setBoundingBox(this.getBoundingBox().move(x, y, z));
		this.setLocationFromBoundingbox();
	}

	@Override
	public int getLightColor(float partialTick) {
		return 240;
	}

	@Override
	public void tick() {
		if (this.age++ >= this.lifetime) this.remove();
	}

	public static class Provider implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet sprite;

		public Provider(SpriteSet sprites) {
			this.sprite = sprites;
		}

		public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			ArcaneSymbolParticle particle = new ArcaneSymbolParticle(level, x, y, z, xSpeed, ySpeed, zSpeed);
			particle.pickSprite(this.sprite);
			return particle;
		}
	}

}
