package xaero.common.server.mods.ftbteams;

import dev.ftb.mods.ftbteams.api.FTBTeamsAPI;
import dev.ftb.mods.ftbteams.api.Team;
import dev.ftb.mods.ftbteams.api.TeamRank;
import net.minecraft.world.entity.player.Player;
import xaero.common.server.radar.tracker.ISyncedPlayerTrackerSystem;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/server/mods/ftbteams/FTBTeamsSyncedPlayerTrackerSystem.class */
public class FTBTeamsSyncedPlayerTrackerSystem implements ISyncedPlayerTrackerSystem {
    @Override // xaero.common.server.radar.tracker.ISyncedPlayerTrackerSystem
    public int getTrackingLevel(Player tracker, Player tracked) {
        Team trackedTeam;
        if (FTBTeamsAPI.api().getManager().arePlayersInSameTeam(tracker.getUUID(), tracked.getUUID())) {
            return 2;
        }
        Team trackerTeam = (Team) FTBTeamsAPI.api().getManager().getTeamForPlayerID(tracker.getUUID()).orElse(null);
        if (trackerTeam != null && (trackedTeam = (Team) FTBTeamsAPI.api().getManager().getTeamForPlayerID(tracked.getUUID()).orElse(null)) != null && trackerTeam.getRankForPlayer(tracked.getUUID()) == TeamRank.ALLY && trackedTeam.getRankForPlayer(tracker.getUUID()) == TeamRank.ALLY) {
            return 1;
        }
        return 0;
    }

    @Override // xaero.common.server.radar.tracker.ISyncedPlayerTrackerSystem
    public boolean isPartySystem() {
        return true;
    }
}
