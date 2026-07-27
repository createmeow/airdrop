package xaero.common.events;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import xaero.common.HudMod;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/events/CommonEventsNeoForge.class */
public class CommonEventsNeoForge extends CommonEvents {
    public CommonEventsNeoForge(HudMod modMain) {
        super(modMain);
    }

    @SubscribeEvent
    public void onPlayerClone(PlayerEvent.Clone event) {
        onPlayerClone(event.getOriginal(), event.getEntity(), !event.isWasDeath());
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        onServerStarting(event.getServer());
    }

    @SubscribeEvent
    public void onServerStopped(ServerStoppedEvent event) {
        onServerStopped(event.getServer());
    }

    @SubscribeEvent
    public void onPlayerLogIn(PlayerEvent.PlayerLoggedInEvent event) {
        onPlayerLogIn(event.getEntity());
    }

    @SubscribeEvent
    public void handlePlayerTickStart(PlayerTickEvent.Pre event) {
        super.handlePlayerTickStart(event.getEntity());
    }
}
