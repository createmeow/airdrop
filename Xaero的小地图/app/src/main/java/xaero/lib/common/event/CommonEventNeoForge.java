package xaero.lib.common.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PermissionsChangedEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.server.permission.events.PermissionGatherEvent;
import xaero.lib.common.permission.system.NeoForgePermissionSystem;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/event/CommonEventNeoForge.class */
public class CommonEventNeoForge extends CommonEvents {
    @SubscribeEvent
    public void clonePlayer(PlayerEvent.Clone event) {
        super.clonePlayer(event.getOriginal(), event.getEntity());
    }

    @SubscribeEvent
    public void playerLogIn(PlayerEvent.PlayerLoggedInEvent event) {
        super.playerLogIn(event.getEntity());
    }

    @SubscribeEvent
    public void playerTickPre(PlayerTickEvent.Pre event) {
        super.onPlayerTick(true, event.getEntity().getServer() != null, event.getEntity());
    }

    @SubscribeEvent
    public void playerTickPost(PlayerTickEvent.Post event) {
        super.onPlayerTick(false, event.getEntity().getServer() != null, event.getEntity());
    }

    @SubscribeEvent
    public void serverStarting(ServerStartingEvent event) {
        super.serverStarting(event.getServer());
    }

    @SubscribeEvent
    protected void onPlayerPermissionChange(PermissionsChangedEvent event) {
        super.onPlayerPermissionChange(event.getEntity());
    }

    @SubscribeEvent
    protected void onForgePermissionGather(PermissionGatherEvent.Nodes event) {
        NeoForgePermissionSystem.registerNodes(event);
    }
}
