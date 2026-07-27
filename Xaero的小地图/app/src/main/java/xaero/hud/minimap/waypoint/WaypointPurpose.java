package xaero.hud.minimap.waypoint;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/waypoint/WaypointPurpose.class */
public enum WaypointPurpose {
    NORMAL(false, false),
    DEATH(true, true),
    OLD_DEATH(true, true),
    DESTINATION(false, true);

    private final boolean death;
    private final boolean destination;

    WaypointPurpose(boolean death, boolean destination) {
        this.death = death;
        this.destination = destination;
    }

    public boolean isDeath() {
        return this.death;
    }

    public boolean isDestination() {
        return this.destination;
    }
}
