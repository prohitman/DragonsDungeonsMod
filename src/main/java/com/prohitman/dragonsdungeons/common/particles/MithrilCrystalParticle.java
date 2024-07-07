package com.prohitman.dragonsdungeons.common.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)

public class MithrilCrystalParticle extends TextureSheetParticle {
    private final SpriteSet sprites;

    protected MithrilCrystalParticle(ClientLevel world, double x, double y, double z, SpriteSet sprites) {
        super(world, x, y, z);
        this.setColor(1.0F, 1.0F, 1.0F); // White color
        this.setSize(0.1F, 0.1F); // Small size
        this.gravity = 0.0F; // No gravity
        this.lifetime = 15; // Lasts for 40 ticks (2 seconds)
        this.setAlpha(1.0F); // Fully opaque
        this.hasPhysics = false; // Doesn't move
        this.sprites = sprites;
        this.setSpriteFromAge(sprites);
    }

    @Override
    public void tick() {
        // No movement, just fade out
        super.tick();
        this.setSpriteFromAge(this.sprites);
    }

    @Override
    protected int getLightColor(float pPartialTick) {
        return 240;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet pSprites) {
            this.sprites = pSprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType pType, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            MithrilCrystalParticle bloodDripParticle = new MithrilCrystalParticle(pLevel, pX, pY, pZ, this.sprites);
            return bloodDripParticle;
        }
    }
}
