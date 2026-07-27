package xaero.hud.minimap.world.container;

import com.google.common.collect.Iterables;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import xaero.common.HudMod;
import xaero.common.minimap.waypoints.WaypointWorldContainer;
import xaero.hud.minimap.MinimapLogs;
import xaero.hud.minimap.module.MinimapSession;
import xaero.hud.minimap.waypoint.server.ServerWaypointManager;
import xaero.hud.minimap.world.MinimapWorld;
import xaero.hud.minimap.world.container.config.RootConfig;
import xaero.hud.path.XaeroPath;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/world/container/MinimapWorldContainer.class */
public abstract class MinimapWorldContainer {
    private final HudMod modMain;
    protected final MinimapSession session;
    protected final Map<String, MinimapWorldContainer> subContainers;
    protected final Map<String, MinimapWorld> worlds;
    private final Map<String, String> worldNames;
    private final MinimapWorldRootContainer rootContainer;
    private final ServerWaypointManager serverWaypointManager;
    protected XaeroPath path;

    protected MinimapWorldContainer(HudMod modMain, MinimapSession session, XaeroPath path, MinimapWorldRootContainer rootContainer) {
        if (path.getLastNode().contains(":")) {
            throw new IllegalArgumentException();
        }
        this.modMain = modMain;
        this.session = session;
        this.path = path;
        this.rootContainer = rootContainer;
        this.worlds = new HashMap();
        this.subContainers = new HashMap();
        this.worldNames = new HashMap();
        this.serverWaypointManager = new ServerWaypointManager();
    }

    public void setPath(XaeroPath path) {
        if (path.getLastNode().contains(":")) {
            throw new IllegalArgumentException();
        }
        this.path = path;
        for (MinimapWorldContainer s : this.subContainers.values()) {
            s.setPath(path.resolve(s.getLastNode()));
        }
    }

    public MinimapWorldContainer addSubContainer(XaeroPath containerPath) {
        if (containerPath.getNodeCount() <= this.path.getNodeCount()) {
            throw new IllegalArgumentException();
        }
        String nextNode = containerPath.getAtIndex(this.path.getNodeCount()).getLastNode();
        MinimapWorldContainer sub = this.subContainers.get(nextNode);
        if (sub == null) {
            Map<String, MinimapWorldContainer> map = this.subContainers;
            MinimapWorldContainer minimapWorldContainerBuild = Builder.begin().setModMain(this.modMain).setSession(this.session).setPath(this.path.resolve(nextNode)).setRootContainer(getRoot()).build();
            sub = minimapWorldContainerBuild;
            map.put(nextNode, minimapWorldContainerBuild);
        }
        if (containerPath.getNodeCount() > this.path.getNodeCount() + 1) {
            return sub.addSubContainer(containerPath);
        }
        return sub;
    }

    public boolean containsSubContainer(XaeroPath containerPath) {
        if (containerPath.getNodeCount() <= this.path.getNodeCount()) {
            throw new IllegalArgumentException();
        }
        String nextNode = containerPath.getAtIndex(this.path.getNodeCount()).getLastNode();
        MinimapWorldContainer sub = this.subContainers.get(nextNode);
        if (sub == null) {
            return false;
        }
        if (containerPath.getNodeCount() == this.path.getNodeCount() + 1) {
            return true;
        }
        return sub.containsSubContainer(containerPath);
    }

    public boolean deleteSubContainer(XaeroPath containerPath) {
        if (containerPath.getNodeCount() <= this.path.getNodeCount()) {
            throw new IllegalArgumentException();
        }
        if (containerPath.getNodeCount() == this.path.getNodeCount() + 1) {
            return this.subContainers.remove(containerPath.getLastNode()) != null;
        }
        MinimapWorldContainer sub = this.subContainers.get(containerPath.getAtIndex(this.path.getNodeCount()).getLastNode());
        if (sub == null) {
            return false;
        }
        return sub.deleteSubContainer(containerPath);
    }

    public boolean isEmpty() {
        return this.subContainers.isEmpty() && this.worlds.isEmpty();
    }

    public MinimapWorld addWorld(String worldNode) {
        MinimapWorld world = this.worlds.get(worldNode);
        if (world != null) {
            return world;
        }
        MinimapWorld defaultWorld = this.worlds.get("waypoints");
        if (defaultWorld == null) {
            ResourceKey<Level> dimId = this.path.getNodeCount() < 2 ? null : this.session.getDimensionHelper().getDimensionKeyForDirectoryName(this.path.getAtIndex(1).getLastNode());
            MinimapWorld world2 = MinimapWorld.Builder.begin().setContainer(this).setNode(worldNode).setDimId(dimId).build();
            this.worlds.put(worldNode, world2);
            return world2;
        }
        this.worlds.put(worldNode, defaultWorld);
        try {
            Path defaultFile = this.session.getWorldManagerIO().getWorldFile(defaultWorld);
            defaultWorld.setNode(worldNode);
            Path fixedFile = this.session.getWorldManagerIO().getWorldFile(defaultWorld);
            if (Files.exists(defaultFile, new LinkOption[0])) {
                Files.move(defaultFile, fixedFile, new CopyOption[0]);
            }
        } catch (IOException e) {
            MinimapLogs.LOGGER.error("suppressed exception", e);
        }
        this.worlds.remove("waypoints");
        return defaultWorld;
    }

    public void addWorld(MinimapWorld world) {
        if (this.worlds.containsKey(world.getNode())) {
            throw new IllegalArgumentException();
        }
        this.worlds.put(world.getNode(), world);
    }

    public void removeWorld(String worldNode) {
        this.worlds.remove(worldNode);
    }

    public void setName(String worldNode, String name) {
        String current = this.worldNames.get(worldNode);
        if (current != null && !current.equals(name)) {
            this.worlds.get(worldNode).requestRemovalOnSave(current);
        }
        this.worldNames.put(worldNode, name);
    }

    public String getName(String worldNode) {
        String name;
        if (worldNode.equals("waypoints")) {
            return null;
        }
        String name2 = this.worldNames.get(worldNode);
        if (name2 != null) {
            return name2;
        }
        int numericName = this.worldNames.size() + 1;
        do {
            int i = numericName;
            numericName++;
            name = i;
        } while (this.worldNames.containsValue(name));
        setName(worldNode, name);
        return name;
    }

    public void removeName(String worldNode) {
        this.worldNames.remove(worldNode);
    }

    public String getLastNode() {
        return this.path.getLastNode();
    }

    public String getSubName() {
        String subName = getLastNode();
        if (!subName.startsWith("dim%")) {
            return subName;
        }
        ResourceKey<Level> dimensionKey = this.session.getDimensionHelper().getDimensionKeyForDirectoryName(subName);
        if (dimensionKey == null) {
            return "Dim. " + subName.substring(4);
        }
        if (dimensionKey.location().getNamespace().equals("minecraft")) {
            return dimensionKey.location().getPath();
        }
        return dimensionKey.location().toString();
    }

    public String getFullWorldName(String worldNode, String containerName) {
        ResourceKey<Level> dimId;
        String worldMapMWName;
        if (this.worlds.size() < 2 && !containerName.isEmpty()) {
            return containerName;
        }
        String worldName = getName(worldNode);
        String dimNode = this.path.getNodeCount() < 2 ? "" : this.path.getAtIndex(1).getLastNode();
        if (dimNode.startsWith("dim%") && (dimId = this.session.getDimensionHelper().getDimensionKeyForDirectoryName(dimNode)) != null && this.modMain.getSupportMods().worldmap() && getRoot().getPath().equals(this.session.getWorldState().getAutoRootContainerPath()) && (worldMapMWName = this.modMain.getSupportMods().worldmapSupport.tryToGetMultiworldName(dimId, worldNode)) != null && !worldMapMWName.equals(worldNode)) {
            worldName = worldMapMWName;
        }
        if (worldName == null) {
            return containerName;
        }
        return !containerName.isEmpty() ? worldName + " - " + containerName : worldName;
    }

    public XaeroPath getPath() {
        return this.path;
    }

    public MinimapWorld getFirstWorld() {
        if (!this.worlds.isEmpty()) {
            return this.worlds.values().stream().findFirst().orElse(null);
        }
        for (MinimapWorldContainer sub : this.subContainers.values()) {
            MinimapWorld subFirst = sub.getFirstWorld();
            if (subFirst != null) {
                return subFirst;
            }
        }
        return null;
    }

    public MinimapWorld getFirstWorldConnectedTo(MinimapWorld refWorld) {
        if (!this.worlds.isEmpty()) {
            MinimapWorldRootContainer rootContainer = getRoot();
            for (MinimapWorld world : this.worlds.values()) {
                if (rootContainer.getSubWorldConnections().isConnected(refWorld, world)) {
                    return world;
                }
            }
        }
        for (MinimapWorldContainer sub : this.subContainers.values()) {
            MinimapWorld subFirst = sub.getFirstWorldConnectedTo(refWorld);
            if (subFirst != null) {
                return subFirst;
            }
        }
        return null;
    }

    public String toString() {
        return String.valueOf(this.path) + " sc:" + this.subContainers.size() + " w:" + this.worlds.size();
    }

    public Iterable<MinimapWorld> getWorlds() {
        return this.worlds.values();
    }

    public List<MinimapWorld> getWorldsCopy() {
        return new ArrayList(this.worlds.values());
    }

    public Iterable<MinimapWorldContainer> getSubContainers() {
        return this.subContainers.values();
    }

    public Iterable<MinimapWorld> getAllWorldsIterable() {
        Iterable<MinimapWorld> allWorlds = this.worlds.values();
        for (MinimapWorldContainer sub : this.subContainers.values()) {
            allWorlds = Iterables.concat(allWorlds, sub.getAllWorldsIterable());
        }
        return allWorlds;
    }

    public XaeroPath fixPathCharacterCases(XaeroPath containerPath) {
        if (containerPath.equals(this.path)) {
            return this.path;
        }
        if (!containerPath.isSubOf(this.path)) {
            return null;
        }
        for (Map.Entry<String, MinimapWorldContainer> entry : this.subContainers.entrySet()) {
            XaeroPath subSearch = entry.getValue().fixPathCharacterCases(containerPath);
            if (subSearch != null) {
                return subSearch;
            }
        }
        XaeroPath fixedContainerPath = this.path;
        for (int i = this.path.getNodeCount(); i < containerPath.getNodeCount(); i++) {
            fixedContainerPath = fixedContainerPath.resolve(containerPath.getAtIndex(i).getLastNode());
        }
        return fixedContainerPath;
    }

    public MinimapWorldRootContainer getRoot() {
        return this.rootContainer;
    }

    public RootConfig getRootConfig() {
        return getRoot().getConfig();
    }

    public Path getDirectoryPath() {
        Path worldFolder = this.modMain.getMinimapFolder();
        return this.path.applyToFilePath(worldFolder);
    }

    public MinimapSession getSession() {
        return this.session;
    }

    public ServerWaypointManager getServerWaypointManager() {
        return this.serverWaypointManager;
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/world/container/MinimapWorldContainer$Builder.class */
    public static final class Builder {
        private HudMod modMain;
        private MinimapSession session;
        private XaeroPath path;
        private MinimapWorldRootContainer rootContainer;

        private Builder() {
        }

        public Builder setDefault() {
            setModMain(null);
            setSession(null);
            setPath(null);
            setRootContainer(null);
            return this;
        }

        public Builder setModMain(HudMod modMain) {
            this.modMain = modMain;
            return this;
        }

        public Builder setSession(MinimapSession session) {
            this.session = session;
            return this;
        }

        public Builder setPath(XaeroPath path) {
            this.path = path;
            return this;
        }

        public Builder setRootContainer(MinimapWorldRootContainer rootContainer) {
            this.rootContainer = rootContainer;
            return this;
        }

        public MinimapWorldContainer build() {
            if (this.modMain == null || this.session == null || this.path == null || this.rootContainer == null) {
                throw new IllegalStateException();
            }
            return new WaypointWorldContainer(this.modMain, this.session, this.path, this.rootContainer);
        }

        public static Builder begin() {
            return new Builder().setDefault();
        }
    }
}
