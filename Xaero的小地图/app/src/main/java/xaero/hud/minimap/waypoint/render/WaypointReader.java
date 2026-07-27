package xaero.hud.minimap.waypoint.render;

import xaero.common.minimap.waypoints.Waypoint;
import xaero.hud.minimap.element.render.MinimapElementRenderLocation;

@Deprecated
/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/waypoint/render/WaypointReader.class */
public class WaypointReader extends WaypointMapRenderReader {
    @Override // xaero.common.minimap.element.render.MinimapElementReader, xaero.hud.minimap.element.render.MinimapElementReader
    @Deprecated
    public boolean isInteractable(MinimapElementRenderLocation location, Waypoint element) {
        return false;
    }

    @Override // xaero.common.minimap.element.render.MinimapElementReader, xaero.hud.minimap.element.render.MinimapElementReader
    @Deprecated
    public float getBoxScale(MinimapElementRenderLocation location, Waypoint element, WaypointMapRenderContext context) {
        return 1.0f;
    }
}
