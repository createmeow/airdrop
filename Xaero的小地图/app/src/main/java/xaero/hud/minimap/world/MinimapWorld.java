package xaero.hud.minimap.world;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import xaero.common.minimap.waypoints.WaypointWorld;
import xaero.common.minimap.waypoints.WaypointWorldContainer;
import xaero.hud.minimap.waypoint.set.WaypointSet;
import xaero.hud.minimap.world.container.MinimapWorldContainer;
import xaero.hud.minimap.world.container.config.RootConfig;
import xaero.hud.path.XaeroPath;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/world/MinimapWorld.class */
public abstract class MinimapWorld {
    private String node;
    private ResourceKey<Level> dimId;
    private MinimapWorldContainer container;
    private Long slimeChunkSeed;
    protected final Map<String, WaypointSet> waypointSets = new LinkedHashMap();
    private final List<String> toRemoveOnSave = new ArrayList();
    private String currentWaypointSetId = "gui.xaero_default";

    protected MinimapWorld(MinimapWorldContainer container, String node, ResourceKey<Level> dimId) {
        this.container = container;
        this.node = node;
        this.dimId = dimId;
        addWaypointSet("gui.xaero_default");
    }

    public WaypointSet getCurrentWaypointSet() {
        return this.waypointSets.get(this.currentWaypointSetId);
    }

    public void addWaypointSet(String s) {
        this.waypointSets.put(s, WaypointSet.Builder.begin().setName(s).build());
    }

    public void cleanupOnSave(Path worldFile) throws IOException {
        Path folder = worldFile.getParent();
        for (String s : this.toRemoveOnSave) {
            Path path = folder.resolve(this.node + "_" + s + ".txt");
            Files.deleteIfExists(path);
        }
    }

    public XaeroPath getLocalWorldKey() {
        XaeroPath containerKey = this.container.getPath();
        if (containerKey.getNodeCount() < 2) {
            return XaeroPath.root(this.node);
        }
        return containerKey.getSubPath(1).resolve(this.node);
    }

    public WaypointSet addWaypointSet(WaypointSet set) {
        return this.waypointSets.put(set.getName(), set);
    }

    public WaypointSet getWaypointSet(String key) {
        return this.waypointSets.get(key);
    }

    public WaypointSet removeWaypointSet(String key) {
        return this.waypointSets.remove(key);
    }

    public Iterable<WaypointSet> getIterableWaypointSets() {
        return this.waypointSets.values();
    }

    public String getCurrentWaypointSetId() {
        return this.currentWaypointSetId;
    }

    public void setCurrentWaypointSetId(String currentWaypointSetId) {
        this.currentWaypointSetId = currentWaypointSetId;
    }

    public String getNode() {
        return this.node;
    }

    public XaeroPath getFullPath() {
        return this.container.getPath().resolve(this.node);
    }

    public void setNode(String node) {
        this.node = node;
    }

    public MinimapWorldContainer getContainer() {
        return this.container;
    }

    public void setContainer(MinimapWorldContainer container) {
        this.container = container;
    }

    public void requestRemovalOnSave(String name) {
        this.toRemoveOnSave.add(name);
    }

    public boolean hasSomethingToRemoveOnSave() {
        return !this.toRemoveOnSave.isEmpty();
    }

    public ResourceKey<Level> getDimId() {
        return this.dimId;
    }

    public void setDimId(ResourceKey<Level> dimId) {
        this.dimId = dimId;
    }

    public int getSetCount() {
        return this.waypointSets.size();
    }

    public Long getSlimeChunkSeed() {
        return this.slimeChunkSeed;
    }

    public void setSlimeChunkSeed(Long slimeChunkSeed) {
        this.slimeChunkSeed = slimeChunkSeed;
    }

    public RootConfig getRootConfig() {
        return getContainer().getRootConfig();
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/world/MinimapWorld$Builder.class */
    public static final class Builder {
        private MinimapWorldContainer container;
        private String node;
        private ResourceKey<Level> dimId;

        private Builder() {
        }

        private Builder setDefault() {
            setContainer(null);
            setNode(null);
            setDimId(null);
            return this;
        }

        public Builder setContainer(MinimapWorldContainer container) {
            this.container = container;
            return this;
        }

        public Builder setNode(String node) {
            this.node = node;
            return this;
        }

        public Builder setDimId(ResourceKey<Level> dimId) {
            this.dimId = dimId;
            return this;
        }

        public MinimapWorld build() {
            if (this.container == null || this.node == null) {
                throw new IllegalStateException();
            }
            if (!(this.container instanceof WaypointWorldContainer)) {
                throw new IllegalStateException("invalid world container class");
            }
            return new WaypointWorld((WaypointWorldContainer) this.container, this.node, this.dimId);
        }

        public static Builder begin() {
            return new Builder().setDefault();
        }
    }
}
