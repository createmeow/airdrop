package xaero.common.server.mods.ftbteams;

import xaero.common.server.radar.tracker.ISyncedPlayerTrackerSystem;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/server/mods/ftbteams/SupportFTBTeamsServer.class */
public class SupportFTBTeamsServer {
    private final ISyncedPlayerTrackerSystem syncedPlayerTrackerSystem = new FTBTeamsSyncedPlayerTrackerSystem();

    public ISyncedPlayerTrackerSystem getSyncedPlayerTrackerSystem() {
        return this.syncedPlayerTrackerSystem;
    }
}
