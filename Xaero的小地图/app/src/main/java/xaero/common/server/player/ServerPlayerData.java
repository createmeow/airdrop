package xaero.common.server.player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import net.minecraft.server.level.ServerPlayer;
import xaero.common.server.radar.tracker.SyncedTrackedPlayer;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/server/player/ServerPlayerData.class */
public class ServerPlayerData {
    private final UUID playerId;
    private SyncedTrackedPlayer lastSyncedData;
    private Set<UUID> currentlySyncedPlayers;
    private long lastTrackedPlayerSync;
    private int clientModNetworkVersion;
    private Object opacData;

    public ServerPlayerData(UUID playerId) {
        this.playerId = playerId;
    }

    public SyncedTrackedPlayer getLastSyncedData() {
        return this.lastSyncedData;
    }

    public SyncedTrackedPlayer ensureLastSyncedData() {
        if (this.lastSyncedData == null) {
            this.lastSyncedData = new SyncedTrackedPlayer(this.playerId, 0.0d, 0.0d, 0.0d, null);
        }
        return this.lastSyncedData;
    }

    public Set<UUID> getCurrentlySyncedPlayers() {
        return this.currentlySyncedPlayers;
    }

    public Set<UUID> ensureCurrentlySyncedPlayers() {
        if (this.currentlySyncedPlayers == null) {
            this.currentlySyncedPlayers = new HashSet();
        }
        return this.currentlySyncedPlayers;
    }

    public long getLastTrackedPlayerSync() {
        return this.lastTrackedPlayerSync;
    }

    public void setLastTrackedPlayerSync(long lastTrackedPlayerSync) {
        this.lastTrackedPlayerSync = lastTrackedPlayerSync;
    }

    public static ServerPlayerData get(ServerPlayer player) {
        ServerPlayerData result = ((IServerPlayer) player).getXaeroMinimapPlayerData();
        if (result == null) {
            ServerPlayerData serverPlayerData = new ServerPlayerData(player.getUUID());
            result = serverPlayerData;
            ((IServerPlayer) player).setXaeroMinimapPlayerData(serverPlayerData);
        }
        return result;
    }

    public boolean hasMod() {
        return this.clientModNetworkVersion != 0;
    }

    public void setClientModNetworkVersion(int clientModNetworkVersion) {
        this.clientModNetworkVersion = clientModNetworkVersion;
    }

    public int getClientModNetworkVersion() {
        return this.clientModNetworkVersion;
    }

    public void setOpacData(Object opacData) {
        this.opacData = opacData;
    }

    public Object getOpacData() {
        return this.opacData;
    }
}
