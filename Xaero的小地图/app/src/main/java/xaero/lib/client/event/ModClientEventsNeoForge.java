package xaero.lib.client.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import xaero.lib.common.resource.ShaderResourceReloadListener;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/client/event/ModClientEventsNeoForge.class */
public class ModClientEventsNeoForge {
    private final ClientEvents clientEvents;

    public ModClientEventsNeoForge(ClientEvents clientEvents) {
        this.clientEvents = clientEvents;
    }

    @SubscribeEvent
    public void handleRegisterClientResourceReloadListeners(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener(new ShaderResourceReloadListener());
    }

    @SubscribeEvent
    public void onModelBakeEvent(ModelEvent.BakingCompleted event) {
    }
}
