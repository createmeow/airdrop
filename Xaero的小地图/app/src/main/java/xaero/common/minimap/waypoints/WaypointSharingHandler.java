package xaero.common.minimap.waypoints;

import net.minecraft.client.gui.screens.Screen;
import xaero.common.HudMod;
import xaero.hud.minimap.module.MinimapSession;
import xaero.hud.minimap.world.MinimapWorld;

@Deprecated
/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/minimap/waypoints/WaypointSharingHandler.class */
public class WaypointSharingHandler extends xaero.hud.minimap.waypoint.WaypointSharingHandler {
    public static final String WAYPOINT_OLD_SHARE_PREFIX = "xaero_waypoint:";
    public static final String WAYPOINT_ADD_PREFIX = "xaero_waypoint_add:";
    public static final String WAYPOINT_SHARE_PREFIX = "xaero-waypoint:";

    public WaypointSharingHandler(HudMod modMain, MinimapSession session) {
        super(modMain, session);
    }

    @Deprecated
    public void shareWaypoint(Screen parent, Waypoint w, WaypointWorld wWorld) {
        shareWaypoint(parent, w, (MinimapWorld) wWorld);
    }

    @Override // xaero.hud.minimap.waypoint.WaypointSharingHandler
    @Deprecated
    public void shareWaypoint(Screen currentScreen, Waypoint waypoint, MinimapWorld minimapWorld) {
        super.shareWaypoint(currentScreen, waypoint, minimapWorld);
    }

    @Override // xaero.hud.minimap.waypoint.WaypointSharingHandler
    @Deprecated
    public void onWaypointReceived(String playerName, String text) {
        super.onWaypointReceived(playerName, text);
    }

    @Override // xaero.hud.minimap.waypoint.WaypointSharingHandler
    @Deprecated
    public void onWaypointAdd(String[] args) {
        super.onWaypointAdd(args);
    }
}
