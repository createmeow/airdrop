package xaero.hud.minimap.waypoint;

import net.minecraft.world.entity.Entity;
import xaero.common.minimap.waypoints.Waypoint;
import xaero.hud.minimap.module.MinimapSession;
import xaero.hud.minimap.waypoint.render.WaypointDeleter;
import xaero.hud.minimap.world.MinimapWorld;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/waypoint/DestinationHandler.class */
public class DestinationHandler {
    private final MinimapSession session;
    private final WaypointDeleter waypointReachDeleter;
    private Entity renderEntity;
    private MinimapWorld world;
    private boolean allSets;
    private boolean deathpoints;
    private double dimDiv;

    public DestinationHandler(MinimapSession session, WaypointDeleter waypointReachDeleter) {
        this.session = session;
        this.waypointReachDeleter = waypointReachDeleter;
    }

    public void begin(Entity renderEntity, MinimapWorld world, boolean allSets, boolean deathpoints) {
        this.waypointReachDeleter.begin();
        this.renderEntity = renderEntity;
        this.world = world;
        this.allSets = allSets;
        this.deathpoints = deathpoints;
        this.dimDiv = this.session.getDimensionHelper().getDimensionDivision(world);
    }

    public void handle(Waypoint waypoint) {
        if (!waypoint.isDestination()) {
            return;
        }
        if ((!this.deathpoints && waypoint.getPurpose().isDeath()) || System.currentTimeMillis() - waypoint.getCreatedAt() <= 5000) {
            return;
        }
        double correctOffX = this.renderEntity.getX() - waypoint.getX(this.dimDiv);
        double correctOffY = this.renderEntity.getY() - waypoint.getY();
        if (!waypoint.isYIncluded()) {
            correctOffY = 0.0d;
        }
        double correctOffZ = this.renderEntity.getZ() - waypoint.getZ(this.dimDiv);
        double correctDistance = Math.sqrt((correctOffX * correctOffX) + (correctOffY * correctOffY) + (correctOffZ * correctOffZ));
        if (correctDistance < 4.0d) {
            this.waypointReachDeleter.add(waypoint);
        }
    }

    public void end() {
        this.waypointReachDeleter.deleteCollected(this.session, this.world, this.allSets);
    }
}
