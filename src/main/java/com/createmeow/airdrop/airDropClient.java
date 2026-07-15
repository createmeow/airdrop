package com.createmeow.airdrop;

import com.createmeow.airdrop.particle.AirdropParticle;
import com.createmeow.airdrop.particle.ParticleRegister;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

@EventBusSubscriber(modid = airDrop.MODID, value = Dist.CLIENT)
public class airDropClient {

    @SubscribeEvent
    static void onRegisterParticleProviders(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ParticleRegister.AIRDROP_PARTICLE.get(), AirdropParticle.Provider::new);
        airDrop.LOGGER.info("AirDrop client particle provider registered");
    }
}