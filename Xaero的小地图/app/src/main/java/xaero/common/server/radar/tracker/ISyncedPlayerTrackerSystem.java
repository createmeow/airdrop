package xaero.common.server.radar.tracker;

import net.minecraft.world.entity.player.Player;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/server/radar/tracker/ISyncedPlayerTrackerSystem.class */
public interface ISyncedPlayerTrackerSystem {
    int getTrackingLevel(Player player, Player player2);

    boolean isPartySystem();
}
