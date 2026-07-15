package com.createmeow.airdrop.particle;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.codec.StreamCodec;

public class AirdropParticleOptions implements ParticleOptions {
    public static final MapCodec<AirdropParticleOptions> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.FLOAT.fieldOf("r").forGetter(o -> o.r),
                    Codec.FLOAT.fieldOf("g").forGetter(o -> o.g),
                    Codec.FLOAT.fieldOf("b").forGetter(o -> o.b),
                    Codec.FLOAT.fieldOf("age_ratio").forGetter(o -> o.ageRatio)
            ).apply(instance, AirdropParticleOptions::new)
    );

    public static final StreamCodec<ByteBuf, AirdropParticleOptions> STREAM_CODEC = StreamCodec.of(
            (buf, opt) -> {
                buf.writeFloat(opt.r);
                buf.writeFloat(opt.g);
                buf.writeFloat(opt.b);
                buf.writeFloat(opt.ageRatio);
            },
            buf -> new AirdropParticleOptions(buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat())
    );

    public final float r, g, b;
    public final float ageRatio;

    public AirdropParticleOptions(float r, float g, float b, float ageRatio) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.ageRatio = ageRatio;
    }

    @Override
    public ParticleType<?> getType() {
        return ParticleRegister.AIRDROP_PARTICLE.get();
    }
}