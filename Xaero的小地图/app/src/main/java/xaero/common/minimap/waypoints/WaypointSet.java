package xaero.common.minimap.waypoints;

import java.util.ArrayList;

@Deprecated
/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/minimap/waypoints/WaypointSet.class */
public class WaypointSet extends xaero.hud.minimap.waypoint.set.WaypointSet {
    @Deprecated
    public WaypointSet(String name) {
        super(name);
    }

    @Override // xaero.hud.minimap.waypoint.set.WaypointSet
    @Deprecated
    public String getName() {
        return super.getName();
    }

    @Deprecated
    public ArrayList<Waypoint> getList() {
        return (ArrayList) this.list;
    }
}
