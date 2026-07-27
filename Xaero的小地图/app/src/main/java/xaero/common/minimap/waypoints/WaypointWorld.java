package xaero.common.minimap.waypoints;

import java.util.HashMap;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import xaero.hud.minimap.world.MinimapWorld;
import xaero.hud.minimap.world.container.MinimapWorldContainer;
import xaero.hud.path.XaeroPath;

@Deprecated
/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/minimap/waypoints/WaypointWorld.class */
public class WaypointWorld extends MinimapWorld {
    private final HashMap<String, Boolean> serverWaypointsDisabled;
    private final HashMap<Integer, Waypoint> serverWaypoints;

    @Deprecated
    public WaypointWorld(WaypointWorldContainer container, String id, ResourceKey<Level> dimId) {
        super(container, id, dimId);
        this.serverWaypointsDisabled = new HashMap<>();
        this.serverWaypoints = new HashMap<>();
    }

    private String worldPathToOldString(XaeroPath path) {
        if (path == null) {
            return null;
        }
        return path.getParent().toString() + "_" + path.getLastNode();
    }

    @Deprecated
    public WaypointSet getCurrentSet() {
        return (WaypointSet) super.getCurrentWaypointSet();
    }

    @Deprecated
    public void addSet(String s) {
        super.addWaypointSet(s);
    }

    @Deprecated
    public String getInternalWorldKey() {
        return super.getLocalWorldKey().toString();
    }

    @Deprecated
    public HashMap<String, Boolean> getServerWaypointsDisabled() {
        return this.serverWaypointsDisabled;
    }

    @Deprecated
    public HashMap<Integer, Waypoint> getServerWaypoints() {
        return this.serverWaypoints;
    }

    @Deprecated
    public HashMap<String, WaypointSet> getSets() {
        return (HashMap) this.waypointSets;
    }

    @Deprecated
    public String getCurrent() {
        return super.getCurrentWaypointSetId();
    }

    @Deprecated
    public void setCurrent(String current) {
        super.setCurrentWaypointSetId(current);
    }

    @Deprecated
    public String getId() {
        return getNode();
    }

    @Deprecated
    public String getFullId() {
        return worldPathToOldString(getFullPath());
    }

    @Deprecated
    public void setId(String id) {
        super.setNode(id);
    }

    @Override // xaero.hud.minimap.world.MinimapWorld
    @Deprecated
    public WaypointWorldContainer getContainer() {
        return (WaypointWorldContainer) super.getContainer();
    }

    @Deprecated
    public void setContainer(WaypointWorldContainer container) {
        super.setContainer((MinimapWorldContainer) container);
    }

    @Override // xaero.hud.minimap.world.MinimapWorld
    @Deprecated
    public ResourceKey<Level> getDimId() {
        return super.getDimId();
    }
}
