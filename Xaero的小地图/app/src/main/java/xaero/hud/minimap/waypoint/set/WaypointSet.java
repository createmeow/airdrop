package xaero.hud.minimap.waypoint.set;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import xaero.common.minimap.waypoints.Waypoint;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/waypoint/set/WaypointSet.class */
public abstract class WaypointSet {
    private String name;
    protected List<Waypoint> list = new ArrayList();

    protected WaypointSet(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public Iterable<Waypoint> getWaypoints() {
        return this.list;
    }

    public void addTo(List<Waypoint> collector) {
        collector.addAll(this.list);
    }

    public void add(Waypoint waypoint, boolean front) {
        if (front) {
            this.list.add(0, waypoint);
        } else {
            this.list.add(waypoint);
        }
    }

    public void add(Waypoint waypoint) {
        add(waypoint, false);
    }

    public void addAll(Collection<Waypoint> waypoints, boolean front) {
        if (front) {
            this.list.addAll(0, waypoints);
        } else {
            this.list.addAll(waypoints);
        }
    }

    public void addAll(Collection<Waypoint> waypoints) {
        addAll(waypoints, false);
    }

    public void remove(Waypoint waypoint) {
        this.list.remove(waypoint);
    }

    public Waypoint remove(int slot) {
        return this.list.remove(slot);
    }

    public void removeAll(Collection<Waypoint> waypoints) {
        this.list.removeAll(waypoints);
    }

    public void clear() {
        this.list.clear();
    }

    public boolean isEmpty() {
        return this.list.isEmpty();
    }

    public int size() {
        return this.list.size();
    }

    public Waypoint get(int slot) {
        return this.list.get(slot);
    }

    public Waypoint set(int slot, Waypoint waypoint) {
        return this.list.set(slot, waypoint);
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/waypoint/set/WaypointSet$Builder.class */
    public static final class Builder {
        private String name;

        private Builder() {
        }

        public Builder setDefault() {
            setName(null);
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public WaypointSet build() {
            if (this.name == null) {
                throw new IllegalStateException();
            }
            return new xaero.common.minimap.waypoints.WaypointSet(this.name);
        }

        public static Builder begin() {
            return new Builder().setDefault();
        }
    }
}
