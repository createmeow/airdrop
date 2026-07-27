package xaero.common.minimap.waypoints;

import java.io.PrintWriter;
import xaero.hud.minimap.world.MinimapWorld;
import xaero.hud.minimap.world.connection.MinimapWorldConnectionManager;
import xaero.hud.path.XaeroPathReader;

@Deprecated
/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/minimap/waypoints/WaypointWorldConnectionManager.class */
public class WaypointWorldConnectionManager extends MinimapWorldConnectionManager {
    private final XaeroPathReader pathReader;

    @Deprecated
    public WaypointWorldConnectionManager() {
        this(true);
    }

    @Deprecated
    public WaypointWorldConnectionManager(boolean multiplayer) {
        super(multiplayer);
        this.pathReader = new XaeroPathReader();
    }

    @Deprecated
    public void addConnection(WaypointWorld world1, WaypointWorld world2) {
        super.addConnection((MinimapWorld) world1, (MinimapWorld) world2);
    }

    @Deprecated
    void addConnection(String worldKey1, String worldKey2) {
        super.addConnection(this.pathReader.read(worldKey1), this.pathReader.read(worldKey2));
    }

    @Deprecated
    public void removeConnection(WaypointWorld world1, WaypointWorld world2) {
        super.removeConnection((MinimapWorld) world1, (MinimapWorld) world2);
    }

    @Deprecated
    private void removeConnection(String worldKey1, String worldKey2) {
        super.removeConnection(this.pathReader.read(worldKey1), this.pathReader.read(worldKey2));
    }

    @Deprecated
    public boolean isConnected(WaypointWorld world1, WaypointWorld world2) {
        return super.isConnected((MinimapWorld) world1, (MinimapWorld) world2);
    }

    @Override // xaero.hud.minimap.world.connection.MinimapWorldConnectionManager
    @Deprecated
    public boolean isEmpty() {
        return super.isEmpty();
    }

    @Override // xaero.hud.minimap.world.connection.MinimapWorldConnectionManager
    @Deprecated
    public void save(PrintWriter writer) {
        super.save(writer);
    }

    @Deprecated
    public void swapConnections(WaypointWorld world1, WaypointWorld world2) {
        super.swapConnections((MinimapWorld) world1, (MinimapWorld) world2);
    }
}
