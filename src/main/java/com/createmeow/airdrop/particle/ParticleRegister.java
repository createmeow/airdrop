package com.createmeow.airdrop.particle;

import com.createmeow.airdrop.airDrop;
import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ParticleRegister {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(Registries.PARTICLE_TYPE, airDrop.MODID);

    public static final DeferredHolder<ParticleType<?>, ParticleType<AirdropParticleOptions>> AIRDROP_PARTICLE =
            PARTICLE_TYPES.register("airdrop_particle",
                    () -> new ParticleType<AirdropParticleOptions>(false) {
                        @Override
                        public MapCodec<AirdropParticleOptions> codec() {
                            return AirdropParticleOptions.CODEC;
                        }

                        @Override
                        public StreamCodec<? super ByteBuf, AirdropParticleOptions> streamCodec() {
                            return AirdropParticleOptions.STREAM_CODEC;
                        }
                    });

    public static void register(IEventBus bus) {
        PARTICLE_TYPES.register(bus);
    }
}