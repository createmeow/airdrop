package xaero.common.minimap.radar.tracker.synced;

import java.util.UUID;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import xaero.common.server.radar.tracker.SyncedTrackedPlayer;

@Deprecated
/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/minimap/radar/tracker/synced/ClientSyncedTrackedPlayerManager.class */
public class ClientSyncedTrackedPlayerManager extends xaero.hud.minimap.player.tracker.synced.ClientSyncedTrackedPlayerManager {
    @Override // xaero.hud.minimap.player.tracker.synced.ClientSyncedTrackedPlayerManager
    @Deprecated
    public void remove(UUID id) {
        super.remove(id);
    }

    @Override // xaero.hud.minimap.player.tracker.synced.ClientSyncedTrackedPlayerManager
    @Deprecated
    public void update(UUID id, double x, double y, double z, ResourceKey<Level> dim) {
        super.update(id, x, y, z, dim);
    }

    @Override // xaero.hud.minimap.player.tracker.synced.ClientSyncedTrackedPlayerManager
    @Deprecated
    public Iterable<SyncedTrackedPlayer> getPlayers() {
        return super.getPlayers();
    }

    @Override // xaero.hud.minimap.player.tracker.synced.ClientSyncedTrackedPlayerManager
    @Deprecated
    public void reset() {
        super.reset();
    }
}
