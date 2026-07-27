package xaero.common.minimap.waypoints.render;

import xaero.common.minimap.waypoints.Waypoint;
import xaero.hud.minimap.element.render.MinimapElementRenderLocation;
import xaero.hud.minimap.waypoint.render.WaypointMapRenderContext;

@Deprecated
/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/minimap/waypoints/render/WaypointReader.class */
public class WaypointReader extends xaero.hud.minimap.waypoint.render.WaypointReader {
    @Override // xaero.common.minimap.element.render.MinimapElementReader
    @Deprecated
    public boolean isInteractable(int location, Waypoint element) {
        return isInteractable(MinimapElementRenderLocation.fromIndex(location), element);
    }

    @Override // xaero.common.minimap.element.render.MinimapElementReader
    @Deprecated
    public float getBoxScale(int location, Waypoint element, WaypointMapRenderContext context) {
        return getBoxScale(MinimapElementRenderLocation.fromIndex(location), element, context);
    }
}
