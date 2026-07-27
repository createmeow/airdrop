package xaero.hud.minimap.waypoint;

import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import xaero.common.HudMod;
import xaero.common.minimap.waypoints.Waypoint;
import xaero.common.minimap.waypoints.WaypointsManager;
import xaero.hud.minimap.BuiltInHudModules;
import xaero.hud.minimap.common.config.option.MinimapProfiledConfigOptions;
import xaero.hud.minimap.module.MinimapSession;
import xaero.hud.minimap.waypoint.set.WaypointSet;
import xaero.hud.minimap.world.MinimapWorldManager;
import xaero.lib.client.config.ClientConfigManager;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/waypoint/WaypointCollector.class */
public class WaypointCollector {
    private final MinimapSession session;

    public WaypointCollector(MinimapSession session) {
        this.session = session;
    }

    public void collect(List<Waypoint> destination) {
        MinimapSession session = (MinimapSession) BuiltInHudModules.MINIMAP.getCurrentSession();
        MinimapWorldManager manager = session.getWorldManager();
        if (manager.getCurrentWorld() != null) {
            ClientConfigManager configManager = HudMod.INSTANCE.getHudConfigs().getClientConfigManager();
            if (((Boolean) configManager.getEffective(MinimapProfiledConfigOptions.WAYPOINTS_ALL_SETS)).booleanValue()) {
                for (WaypointSet set : manager.getCurrentWorld().getIterableWaypointSets()) {
                    set.addTo(destination);
                }
            } else {
                manager.getCurrentWorld().getCurrentWaypointSet().addTo(destination);
            }
        }
        Hashtable<String, Hashtable<Integer, Waypoint>> customWaypoints = WaypointsManager.customWaypoints;
        if (!customWaypoints.isEmpty()) {
            Collection<Hashtable<Integer, Waypoint>> modTables = customWaypoints.values();
            for (Hashtable<Integer, Waypoint> modTable : modTables) {
                destination.addAll(modTable.values());
            }
        }
        if (!manager.hasCustomWaypoints()) {
            return;
        }
        for (Waypoint waypoint : manager.getCustomWaypoints()) {
            destination.add(waypoint);
        }
    }
}
