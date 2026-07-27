package xaero.hud.minimap.waypoint.render.world;

import xaero.common.minimap.waypoints.Waypoint;
import xaero.hud.minimap.element.render.MinimapElementRenderLocation;
import xaero.hud.minimap.waypoint.render.AbstractWaypointRenderProvider;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/waypoint/render/world/WaypointWorldRenderProvider.class */
public final class WaypointWorldRenderProvider extends AbstractWaypointRenderProvider<WaypointWorldRenderContext> {
    @Override // xaero.common.minimap.element.render.MinimapElementRenderProvider
    @Deprecated
    public void begin(int location, WaypointWorldRenderContext context) {
        begin(MinimapElementRenderLocation.fromIndex(location), (MinimapElementRenderLocation) context);
    }

    @Override // xaero.common.minimap.element.render.MinimapElementRenderProvider
    @Deprecated
    public Waypoint setupContextAndGetNext(int location, WaypointWorldRenderContext context) {
        return setupContextAndGetNext(MinimapElementRenderLocation.fromIndex(location), (MinimapElementRenderLocation) context);
    }

    @Override // xaero.common.minimap.element.render.MinimapElementRenderProvider
    @Deprecated
    public boolean hasNext(int location, WaypointWorldRenderContext context) {
        return hasNext(MinimapElementRenderLocation.fromIndex(location), (MinimapElementRenderLocation) context);
    }

    @Override // xaero.common.minimap.element.render.MinimapElementRenderProvider
    @Deprecated
    public Waypoint getNext(int location, WaypointWorldRenderContext context) {
        return getNext(MinimapElementRenderLocation.fromIndex(location), (MinimapElementRenderLocation) context);
    }

    @Override // xaero.common.minimap.element.render.MinimapElementRenderProvider
    @Deprecated
    public void end(int location, WaypointWorldRenderContext context) {
        end(MinimapElementRenderLocation.fromIndex(location), (MinimapElementRenderLocation) context);
    }
}
