package xaero.common.server.mods.argonauts;

import xaero.common.server.radar.tracker.ISyncedPlayerTrackerSystem;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/server/mods/argonauts/SupportArgonautsServer.class */
public class SupportArgonautsServer {
    private final ISyncedPlayerTrackerSystem syncedPlayerTrackerSystem = new ArgonautsSyncedPlayerTrackerSystem();

    public ISyncedPlayerTrackerSystem getSyncedPlayerTrackerSystem() {
        return this.syncedPlayerTrackerSystem;
    }
}
