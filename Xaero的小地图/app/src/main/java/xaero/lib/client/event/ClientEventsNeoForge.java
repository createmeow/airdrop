package xaero.lib.client.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderFrameEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/client/event/ClientEventsNeoForge.class */
public class ClientEventsNeoForge extends ClientEvents {
    @SubscribeEvent
    public void onRenderTick(RenderFrameEvent.Pre event) {
        super.onRenderTick();
    }

    @SubscribeEvent
    public void handleDrawScreenEventPost(ScreenEvent.Render.Post event) {
        handleDrawScreenEventPost(event.getScreen());
    }

    @SubscribeEvent
    public void handleClientTickEvent(ClientTickEvent.Pre event) {
    }
}
