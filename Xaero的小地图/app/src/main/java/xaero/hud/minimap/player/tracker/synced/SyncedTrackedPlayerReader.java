package xaero.hud.minimap.player.tracker.synced;

import java.util.UUID;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import xaero.common.server.radar.tracker.SyncedTrackedPlayer;
import xaero.hud.minimap.player.tracker.system.ITrackedPlayerReader;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/player/tracker/synced/SyncedTrackedPlayerReader.class */
public class SyncedTrackedPlayerReader implements ITrackedPlayerReader<SyncedTrackedPlayer> {
    @Override // xaero.hud.minimap.player.tracker.system.ITrackedPlayerReader
    public UUID getId(SyncedTrackedPlayer player) {
        return player.getId();
    }

    @Override // xaero.hud.minimap.player.tracker.system.ITrackedPlayerReader
    public double getX(SyncedTrackedPlayer player) {
        return player.getX();
    }

    @Override // xaero.hud.minimap.player.tracker.system.ITrackedPlayerReader
    public double getY(SyncedTrackedPlayer player) {
        return player.getY();
    }

    @Override // xaero.hud.minimap.player.tracker.system.ITrackedPlayerReader
    public double getZ(SyncedTrackedPlayer player) {
        return player.getZ();
    }

    @Override // xaero.hud.minimap.player.tracker.system.ITrackedPlayerReader
    public ResourceKey<Level> getDimension(SyncedTrackedPlayer player) {
        return player.getDimensionKey();
    }
}
