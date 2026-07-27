package xaero.common.minimap.waypoints.render;

import xaero.common.minimap.waypoints.Waypoint;
import xaero.hud.minimap.element.render.MinimapElementRenderLocation;
import xaero.hud.minimap.waypoint.render.WaypointMapRenderContext;

@Deprecated
/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/minimap/waypoints/render/WaypointRenderProvider.class */
public class WaypointRenderProvider extends xaero.hud.minimap.waypoint.render.WaypointRenderProvider {
    @Override // xaero.common.minimap.element.render.MinimapElementRenderProvider
    public void begin(int location, WaypointMapRenderContext context) {
        begin(MinimapElementRenderLocation.fromIndex(location), (MinimapElementRenderLocation) context);
    }

    @Override // xaero.common.minimap.element.render.MinimapElementRenderProvider
    public Waypoint setupContextAndGetNext(int location, WaypointMapRenderContext context) {
        return setupContextAndGetNext(MinimapElementRenderLocation.fromIndex(location), (MinimapElementRenderLocation) context);
    }

    @Override // xaero.common.minimap.element.render.MinimapElementRenderProvider
    public boolean hasNext(int location, WaypointMapRenderContext context) {
        return hasNext(MinimapElementRenderLocation.fromIndex(location), (MinimapElementRenderLocation) context);
    }

    @Override // xaero.common.minimap.element.render.MinimapElementRenderProvider
    public Waypoint getNext(int location, WaypointMapRenderContext context) {
        return getNext(MinimapElementRenderLocation.fromIndex(location), (MinimapElementRenderLocation) context);
    }

    @Override // xaero.common.minimap.element.render.MinimapElementRenderProvider
    public void end(int location, WaypointMapRenderContext context) {
        end(MinimapElementRenderLocation.fromIndex(location), (MinimapElementRenderLocation) context);
    }
}
