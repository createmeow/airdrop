package xaero.hud.minimap.world.connection;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import xaero.common.minimap.waypoints.WaypointWorldConnectionManager;
import xaero.hud.minimap.world.MinimapWorld;
import xaero.hud.path.XaeroPath;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/world/connection/MinimapWorldConnectionManager.class */
public abstract class MinimapWorldConnectionManager {
    private Map<XaeroPath, Set<XaeroPath>> allConnections = new HashMap();
    private final boolean multiplayer;

    protected MinimapWorldConnectionManager(boolean multiplayer) {
        this.multiplayer = multiplayer;
    }

    public void addConnection(MinimapWorld world1, MinimapWorld world2) {
        addConnection(world1.getLocalWorldKey(), world2.getLocalWorldKey());
    }

    public void addConnection(XaeroPath worldKey1, XaeroPath worldKey2) {
        addOneWayConnection(worldKey1, worldKey2);
        addOneWayConnection(worldKey2, worldKey1);
    }

    private void addOneWayConnection(XaeroPath worldKey1, XaeroPath worldKey2) {
        Set<XaeroPath> connections = this.allConnections.get(worldKey1);
        if (connections == null) {
            Map<XaeroPath, Set<XaeroPath>> map = this.allConnections;
            HashSet hashSet = new HashSet();
            connections = hashSet;
            map.put(worldKey1, hashSet);
        }
        connections.add(worldKey2);
    }

    public void removeConnection(MinimapWorld world1, MinimapWorld world2) {
        removeConnection(world1.getLocalWorldKey(), world2.getLocalWorldKey());
    }

    protected void removeConnection(XaeroPath worldKey1, XaeroPath worldKey2) {
        removeOneWayConnection(worldKey1, worldKey2);
        removeOneWayConnection(worldKey2, worldKey1);
    }

    private void removeOneWayConnection(XaeroPath worldKey1, XaeroPath worldKey2) {
        Set<XaeroPath> connections = this.allConnections.get(worldKey1);
        if (connections == null) {
            return;
        }
        connections.remove(worldKey2);
    }

    public boolean isConnected(MinimapWorld world1, MinimapWorld world2) {
        Set<XaeroPath> connections;
        if (!this.multiplayer || world1 == world2) {
            return true;
        }
        if (world1 == null || world2 == null || (connections = this.allConnections.get(world1.getLocalWorldKey())) == null) {
            return false;
        }
        return connections.contains(world2.getLocalWorldKey());
    }

    public boolean isEmpty() {
        return this.allConnections.isEmpty();
    }

    public void save(PrintWriter writer) {
        if (!this.allConnections.isEmpty()) {
            Set<String> redundantConnections = new HashSet<>();
            for (Map.Entry<XaeroPath, Set<XaeroPath>> entry : this.allConnections.entrySet()) {
                XaeroPath worldKey = entry.getKey();
                Set<XaeroPath> connections = entry.getValue();
                for (XaeroPath c : connections) {
                    String fullConnection = String.valueOf(worldKey) + ":" + String.valueOf(c);
                    if (!redundantConnections.contains(fullConnection)) {
                        writer.println("connection:" + fullConnection);
                        redundantConnections.add(String.valueOf(c) + ":" + String.valueOf(worldKey));
                    }
                }
            }
        }
    }

    public void swapConnections(MinimapWorld world1, MinimapWorld world2) {
        swapConnections(world1.getLocalWorldKey(), world2.getLocalWorldKey());
    }

    private void swapConnections(XaeroPath worldKey1, XaeroPath worldKey2) {
        Set<XaeroPath> connections1 = new HashSet<>(this.allConnections.getOrDefault(worldKey1, new HashSet()));
        Set<XaeroPath> connections2 = new HashSet<>(this.allConnections.getOrDefault(worldKey2, new HashSet()));
        for (XaeroPath c : connections1) {
            if (!c.equals(worldKey2)) {
                removeConnection(worldKey1, c);
            }
        }
        for (XaeroPath c2 : connections2) {
            if (!c2.equals(worldKey1)) {
                addConnection(worldKey1, c2);
            }
        }
        for (XaeroPath c3 : connections2) {
            if (!c3.equals(worldKey1)) {
                removeConnection(worldKey2, c3);
            }
        }
        for (XaeroPath c4 : connections1) {
            if (!c4.equals(worldKey2)) {
                addConnection(worldKey2, c4);
            }
        }
    }

    public void renameDimension(String oldName, String newName) {
        Set<XaeroPath> keysCopy = new HashSet<>(this.allConnections.keySet());
        for (XaeroPath worldKey : keysCopy) {
            if (worldKey.getNodeCount() > 1 && worldKey.getRoot().getLastNode().equals(oldName)) {
                XaeroPath nonDimPart = worldKey.getSubPath(1);
                swapConnections(worldKey, XaeroPath.root(newName).resolve(nonDimPart));
            }
        }
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/world/connection/MinimapWorldConnectionManager$Builder.class */
    public static final class Builder {
        private boolean multiplayer;

        private Builder() {
        }

        public Builder setDefault() {
            setMultiplayer(true);
            return this;
        }

        public Builder setMultiplayer(boolean multiplayer) {
            this.multiplayer = multiplayer;
            return this;
        }

        public MinimapWorldConnectionManager build() {
            return new WaypointWorldConnectionManager(this.multiplayer);
        }

        public static Builder begin() {
            return new Builder().setDefault();
        }
    }
}
