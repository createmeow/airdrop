package xaero.hud.minimap.world;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import xaero.common.HudMod;
import xaero.common.minimap.waypoints.Waypoint;
import xaero.hud.minimap.module.MinimapSession;
import xaero.hud.minimap.world.container.MinimapWorldContainer;
import xaero.hud.minimap.world.container.MinimapWorldRootContainer;
import xaero.hud.path.XaeroPath;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/world/MinimapWorldManager.class */
public class MinimapWorldManager {
    private final HudMod modMain;
    private final MinimapSession session;
    private final Map<String, MinimapWorldRootContainer> rootContainers = new HashMap();
    private final Map<ResourceLocation, Int2ObjectMap<Waypoint>> customWaypoints = new Hashtable();

    public MinimapWorldManager(HudMod modMain, MinimapSession session) {
        this.modMain = modMain;
        this.session = session;
    }

    public MinimapWorldContainer getWorldContainer(XaeroPath path) {
        return addWorldContainer(path);
    }

    public MinimapWorldContainer getWorldContainerNullable(XaeroPath path) {
        if (containerExists(path)) {
            return addWorldContainer(path);
        }
        return null;
    }

    public MinimapWorldRootContainer getRootWorldContainer(String rootContainerId) {
        return getRootWorldContainer(XaeroPath.root(rootContainerId));
    }

    public MinimapWorldRootContainer getRootWorldContainer(XaeroPath rootContainerPath) {
        return getWorldContainer(rootContainerPath).getRoot();
    }

    public MinimapWorldContainer addWorldContainer(XaeroPath path) {
        XaeroPath rootPath = path.getRoot();
        MinimapWorldRootContainer rootContainer = this.rootContainers.get(rootPath.getLastNode());
        if (rootContainer == null) {
            Map<String, MinimapWorldRootContainer> map = this.rootContainers;
            String lastNode = rootPath.getLastNode();
            MinimapWorldRootContainer minimapWorldRootContainerBuild = MinimapWorldRootContainer.Builder.begin().setModMain(this.modMain).setSession(this.session).setPath(rootPath).build();
            rootContainer = minimapWorldRootContainerBuild;
            map.put(lastNode, minimapWorldRootContainerBuild);
            this.session.getWorldManagerIO().onRootContainerAdded(rootContainer);
        }
        if (path.getNodeCount() > 1) {
            return rootContainer.addSubContainer(path);
        }
        return rootContainer;
    }

    public void addRootWorldContainer(MinimapWorldRootContainer container) {
        if (this.rootContainers.containsKey(container.getPath().getLastNode())) {
            throw new IllegalArgumentException();
        }
        this.rootContainers.put(container.getPath().getLastNode(), container);
    }

    public boolean removeContainer(XaeroPath path) {
        if (path.getNodeCount() == 1) {
            return this.rootContainers.remove(path.getLastNode()) != null;
        }
        MinimapWorldRootContainer rootContainer = this.rootContainers.get(path.getRoot().getLastNode());
        if (rootContainer == null) {
            return false;
        }
        return rootContainer.deleteSubContainer(path);
    }

    public boolean containerExists(XaeroPath path) {
        MinimapWorldRootContainer rootContainer = this.rootContainers.get(path.getRoot().getLastNode());
        if (rootContainer == null) {
            return false;
        }
        if (path.getNodeCount() == 1) {
            return true;
        }
        return rootContainer.containsSubContainer(path);
    }

    public MinimapWorld getWorld(XaeroPath worldPath) {
        return addWorld(worldPath);
    }

    public MinimapWorld addWorld(XaeroPath worldPath) {
        if (worldPath == null) {
            return null;
        }
        MinimapWorldContainer wc = addWorldContainer(worldPath.getParent());
        return wc.addWorld(worldPath.getLastNode());
    }

    public MinimapWorld getCurrentWorld() {
        return getCurrentWorld(this.session.getWorldState().getAutoWorldPath());
    }

    public MinimapWorld getCurrentWorld(XaeroPath autoWorldPath) {
        return getWorld(this.session.getWorldState().getCurrentWorldPath(autoWorldPath));
    }

    @Nullable
    public MinimapWorld getAutoWorld() {
        return getWorld(this.session.getWorldState().getAutoWorldPath());
    }

    public Iterable<MinimapWorldRootContainer> getRootContainers() {
        return this.rootContainers.values();
    }

    public MinimapWorldRootContainer getAutoRootContainer() {
        return getRootWorldContainer(this.session.getWorldState().getAutoRootContainerPath());
    }

    public MinimapWorldRootContainer getCurrentRootContainer() {
        MinimapWorld currentWorld = getCurrentWorld();
        if (currentWorld == null) {
            return null;
        }
        return currentWorld.getContainer().getRoot();
    }

    @Deprecated
    public HashMap<String, MinimapWorldRootContainer> getRootContainersDirect() {
        return (HashMap) this.rootContainers;
    }

    public Int2ObjectMap<Waypoint> getCustomWaypoints(ResourceLocation modId) {
        return this.customWaypoints.computeIfAbsent(modId, s -> {
            return Int2ObjectMaps.synchronize(new Int2ObjectOpenHashMap());
        });
    }

    public boolean hasCustomWaypoints() {
        return !this.customWaypoints.isEmpty();
    }

    public Iterable<Waypoint> getCustomWaypoints() {
        Stream<R> streamFlatMap = this.customWaypoints.values().stream().flatMap(modMap -> {
            return modMap.values().stream();
        });
        Objects.requireNonNull(streamFlatMap);
        return streamFlatMap::iterator;
    }
}
