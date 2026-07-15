package com.createmeow.airdrop.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public class AirdropParticle extends TextureSheetParticle {
    private final SpriteSet sprites;

    protected AirdropParticle(ClientLevel level, double x, double y, double z,
                              float r, float g, float b, float ageRatio,
                              double xd, double yd, double zd, SpriteSet sprites) {
        super(level, x, y, z, xd, yd, zd);
        this.sprites = sprites;
        this.setSpriteFromAge(sprites);

        // Large size
        this.quadSize = 0.4f + (float) Math.random() * 0.2f;
        this.setSize(this.quadSize, this.quadSize);

        // Long lifetime: ~20 seconds = 400 ticks with some variation
        this.lifetime = 380 + (int) (Math.random() * 40);
        if (ageRatio > 0) {
            this.age = (int) (this.lifetime * ageRatio);
        }

        // Color
        this.rCol = r;
        this.gCol = g;
        this.bCol = b;
        this.alpha = 0.85f;

        // Physics - float up fast
        this.gravity = -0.2f;
        this.xd = xd + (Math.random() - 0.5) * 0.02;
        this.yd = yd + (Math.random() - 0.5) * 0.01;
        this.zd = zd + (Math.random() - 0.5) * 0.02;
        this.friction = 0.98f;

        // Keep particle alive
        this.hasPhysics = false;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick() {
        super.tick();
        // Fade out near end of life
        if (this.age > this.lifetime * 0.8f) {
            this.alpha = 0.85f * (1.0f - (this.age - this.lifetime * 0.8f) / (this.lifetime * 0.2f));
        }
        this.setSpriteFromAge(this.sprites);
    }

    @Override
    public int getLightColor(float partialTick) {
        return LightTexture.FULL_BRIGHT;
    }

    public static class Provider implements ParticleProvider<AirdropParticleOptions> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Nullable
        @Override
        public Particle createParticle(AirdropParticleOptions options, ClientLevel level,
                                        double x, double y, double z,
                                        double xSpeed, double ySpeed, double zSpeed) {
            return new AirdropParticle(level, x, y, z,
                    options.r, options.g, options.b, options.ageRatio,
                    xSpeed, ySpeed, zSpeed, this.sprites);
        }
    }
}