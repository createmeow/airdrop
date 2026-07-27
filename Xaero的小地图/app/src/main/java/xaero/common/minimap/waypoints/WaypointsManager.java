package xaero.common.minimap.waypoints;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import xaero.common.HudMod;
import xaero.hud.minimap.module.MinimapSession;
import xaero.hud.minimap.world.container.MinimapWorldContainerUtil;
import xaero.hud.module.HudModule;
import xaero.hud.path.XaeroPath;
import xaero.hud.path.XaeroPathReader;

@Deprecated
/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/minimap/waypoints/WaypointsManager.class */
public class WaypointsManager extends MinimapSession {
    private final XaeroPathReader pathReader;
    public static final Hashtable<String, Hashtable<Integer, Waypoint>> customWaypoints = new Hashtable<>();
    private final List<Waypoint> serverWaypoints;

    public WaypointsManager(HudMod modMain, HudModule<MinimapSession> module, ClientPacketListener connection) {
        super(modMain, module, connection);
        this.pathReader = new XaeroPathReader();
        this.serverWaypoints = new ArrayList();
    }

    private String pathToString(XaeroPath path) {
        if (path == null) {
            return null;
        }
        return path.toString();
    }

    private String worldPathToOldString(XaeroPath path) {
        if (path == null) {
            return null;
        }
        return path.getParent().toString() + "_" + path.getLastNode();
    }

    private String worldPathToLastNode(XaeroPath path) {
        if (path == null) {
            return null;
        }
        return path.getLastNode();
    }

    private String worldPathToContainerID(XaeroPath path) {
        if (path == null) {
            return null;
        }
        return path.getParent().toString();
    }

    @Deprecated
    public double getDimensionDivision(WaypointWorld waypointWorld) {
        return getDimensionHelper().getDimensionDivision(waypointWorld);
    }

    @Deprecated
    public double getDimCoordinateScale(WaypointWorld waypointWorld) {
        return getDimensionHelper().getDimCoordinateScale(waypointWorld);
    }

    @Deprecated
    public String getDimensionDirectoryName(ResourceKey<Level> dimKey) {
        return getDimensionHelper().getDimensionDirectoryName(dimKey);
    }

    @Deprecated
    public ResourceKey<Level> findDimensionKey(String validatedName) {
        return getDimensionHelper().findDimensionKeyForOldName(getMc().player, validatedName);
    }

    @Deprecated
    public ResourceKey<Level> getDimensionKeyForDirectoryName(String dirName) {
        return getDimensionHelper().getDimensionKeyForDirectoryName(dirName);
    }

    @Deprecated
    private String getMainContainer(boolean preIP6Fix, ClientPacketListener connection) {
        return getMainContainer(preIP6Fix ? 0 : 1, connection);
    }

    @Deprecated
    private String getMainContainer(int version, ClientPacketListener connection) {
        return pathToString(getWorldStateUpdater().getAutoRootContainerPath(version));
    }

    @Deprecated
    public String ignoreContainerCase(String potentialContainerID, String current) {
        return pathToString(getWorldStateUpdater().ignoreContainerCase(this.pathReader.read(potentialContainerID), this.pathReader.read(current)));
    }

    @Deprecated
    public String getNewAutoWorldID(ResourceKey<Level> dimId, boolean useWorldmap) {
        return getWorldStateUpdater().getPotentialWorldNode(dimId, useWorldmap);
    }

    @Deprecated
    public String getCurrentContainerAndWorldID() {
        return worldPathToOldString(getWorldState().getCurrentWorldPath());
    }

    @Deprecated
    public String getCurrentContainerID() {
        return pathToString(getWorldState().getCurrentContainerPath());
    }

    @Deprecated
    public String getCurrentWorldID() {
        return worldPathToLastNode(getWorldState().getCurrentWorldPath());
    }

    @Deprecated
    public WaypointWorld getCurrentWorld() {
        return (WaypointWorld) this.worldManager.getCurrentWorld();
    }

    @Deprecated
    public String getCurrentOriginContainerID() {
        return pathToString(getWorldState().getCurrentRootContainerPath());
    }

    @Deprecated
    public String getCurrentContainerAndWorldID(String autoContainer, String autoWorldID) {
        XaeroPath autoWorldPath = autoContainer == null ? null : this.pathReader.read(autoContainer).resolve(autoWorldID);
        return worldPathToOldString(getWorldState().getCurrentWorldPath(autoWorldPath));
    }

    @Deprecated
    public String getCurrentContainerID(String autoContainer) {
        XaeroPath customContainerPath = getWorldState().getCustomContainerPath();
        if (customContainerPath == null) {
            return autoContainer;
        }
        return pathToString(customContainerPath);
    }

    @Deprecated
    public String getCurrentWorldID(String autoWorldID) {
        XaeroPath customWorldPath = getWorldState().getCustomWorldPath();
        if (customWorldPath == null) {
            return autoWorldID;
        }
        return worldPathToLastNode(customWorldPath);
    }

    @Deprecated
    public WaypointWorld getCurrentWorld(String autoContainer, String autoWorldID) {
        XaeroPath autoWorldPath = autoContainer == null ? null : this.pathReader.read(autoContainer).resolve(autoWorldID);
        return (WaypointWorld) this.worldManager.getWorld(getWorldState().getCurrentWorldPath(autoWorldPath));
    }

    @Deprecated
    public String getCurrentOriginContainerID(String autoContainer) {
        XaeroPath customContainer = getWorldState().getCustomContainerPath();
        if (customContainer != null) {
            return customContainer.getRoot().getLastNode();
        }
        if (autoContainer == null) {
            return null;
        }
        return autoContainer.split("/")[0];
    }

    @Deprecated
    public WaypointWorld getAutoWorld() {
        return (WaypointWorld) this.worldManager.getAutoWorld();
    }

    @Deprecated
    public String getAutoRootContainerID() {
        return getWorldState().getAutoRootContainerPath().getLastNode();
    }

    @Deprecated
    public String getAutoContainerID() {
        return worldPathToContainerID(getWorldState().getAutoWorldPath());
    }

    @Deprecated
    public String getAutoWorldID() {
        return worldPathToLastNode(getWorldState().getAutoWorldPath());
    }

    @Deprecated
    public WaypointWorld getWorld(String container, String world) {
        return addWorld(container, world);
    }

    @Deprecated
    public WaypointWorld addWorld(String container, String world) {
        XaeroPath worldPath = this.pathReader.read(container).resolve(world);
        return (WaypointWorld) this.worldManager.addWorld(worldPath);
    }

    @Deprecated
    public WaypointWorldContainer getWorldContainer(String id) {
        return addWorldContainer(id);
    }

    @Deprecated
    public WaypointWorldContainer addWorldContainer(String id) {
        return (WaypointWorldContainer) this.worldManager.addWorldContainer(this.pathReader.read(id));
    }

    @Deprecated
    public WaypointWorldContainer getWorldContainerNullable(String id) {
        return (WaypointWorldContainer) this.worldManager.getWorldContainerNullable(this.pathReader.read(id));
    }

    @Deprecated
    public void removeContainer(String id) {
        this.worldManager.removeContainer(this.pathReader.read(id));
    }

    @Deprecated
    public boolean containerExists(String id) {
        return this.worldManager.containerExists(this.pathReader.read(id));
    }

    @Deprecated
    public void updateWorldIds() {
        getWorldStateUpdater().update();
    }

    @Deprecated
    private String getPotentialContainerID() {
        return pathToString(getWorldStateUpdater().getPotentialContainerPath());
    }

    @Deprecated
    public void updateWaypoints() {
    }

    @Deprecated
    public void createDeathpoint(Player p) {
        getWaypointSession().getDeathpointHandler().createDeathpoint(p);
    }

    @Deprecated
    private void createDeathpoint(Player p, WaypointWorld wpw, boolean temp) {
        getWaypointSession().getDeathpointHandler().createDeathpoint(p, wpw, temp);
    }

    @Deprecated
    public void createTemporaryWaypoints(WaypointWorld waypointWorld, int x, int y, int z) {
        getWaypointSession().getTemporaryHandler().createTemporaryWaypoint(waypointWorld, x, y, z);
    }

    @Deprecated
    public void createTemporaryWaypoints(WaypointWorld waypointWorld, int x, int y, int z, boolean yIncluded, double dimScale) {
        getWaypointSession().getTemporaryHandler().createTemporaryWaypoint(waypointWorld, x, y, z, yIncluded, dimScale);
    }

    @Deprecated
    public void createTemporaryWaypoints(WaypointWorld waypointWorld, int x, int y, int z, boolean yIncluded) {
        getWaypointSession().getTemporaryHandler().createTemporaryWaypoint(waypointWorld, x, y, z, yIncluded);
    }

    @Deprecated
    public boolean canTeleport(boolean displayingTeleportableWorld, WaypointWorld displayedWorld) {
        return getWaypointSession().getTeleport().canTeleport(displayingTeleportableWorld, displayedWorld);
    }

    @Deprecated
    public void teleportAnyway() {
        getWaypointSession().getTeleport().teleportAnyway();
    }

    @Deprecated
    public void teleportToWaypoint(Waypoint selected, WaypointWorld displayedWorld, Screen screen) {
        getWaypointSession().getTeleport().teleportToWaypoint(selected, displayedWorld, screen);
    }

    @Deprecated
    public void teleportToWaypoint(Waypoint selected, WaypointWorld displayedWorld, Screen screen, boolean respectHiddenCoords) {
        getWaypointSession().getTeleport().teleportToWaypoint(selected, displayedWorld, screen, respectHiddenCoords);
    }

    @Deprecated
    public boolean isWorldTeleportable(WaypointWorld displayedWorld) {
        return getWaypointSession().getTeleport().isWorldTeleportable(displayedWorld);
    }

    @Deprecated
    public boolean isTeleportationSafe(WaypointWorld displayedWorld) {
        return getWaypointSession().getTeleport().isTeleportationSafe(displayedWorld);
    }

    @Deprecated
    public WaypointSet getWaypoints() {
        WaypointWorld world = (WaypointWorld) this.worldManager.getCurrentWorld();
        if (world == null) {
            return null;
        }
        return (WaypointSet) world.getCurrentWaypointSet();
    }

    @Deprecated
    public void setWaypoints(WaypointSet waypoints) {
    }

    @Deprecated
    public List<Waypoint> getServerWaypoints() {
        return this.serverWaypoints;
    }

    @Deprecated
    public HashMap<String, WaypointWorldContainer> getWaypointMap() {
        return this.worldManager.getRootContainersDirect();
    }

    @Deprecated
    public void setCurrentSpawn(BlockPos currentSpawn, ClientLevel clientWorld) {
        getWorldStateUpdater().setCurrentWorldSpawn(currentSpawn);
    }

    @Deprecated
    public String getCustomContainerID() {
        return pathToString(getWorldState().getCustomContainerPath());
    }

    @Deprecated
    public void setCustomContainerID(String customContainerID) {
        if (customContainerID == null) {
            getWorldState().setCustomWorldPath(null);
            return;
        }
        XaeroPath newCustomContainerPath = this.pathReader.read(customContainerID);
        XaeroPath customWorldPath = getWorldState().getCustomWorldPath();
        getWorldState().setCustomWorldPath(newCustomContainerPath.resolve(customWorldPath == null ? "" : customWorldPath.getLastNode()));
    }

    @Deprecated
    public String getCustomWorldID() {
        return worldPathToLastNode(getWorldState().getCustomWorldPath());
    }

    @Deprecated
    public void setCustomWorldID(String customWorldID) {
        if (customWorldID == null) {
            getWorldState().setCustomWorldPath(null);
            return;
        }
        XaeroPath customWorldPath = getWorldState().getCustomWorldPath();
        XaeroPath newCustomContainerPath = customWorldPath == null ? XaeroPath.root("") : customWorldPath.getParent();
        getWorldState().setCustomWorldPath(newCustomContainerPath.resolve(customWorldID));
    }

    @Deprecated
    public static Hashtable<Integer, Waypoint> getCustomWaypoints(String modName) {
        Hashtable<Integer, Waypoint> wps = customWaypoints.get(modName);
        if (wps == null) {
            Hashtable<String, Hashtable<Integer, Waypoint>> hashtable = customWaypoints;
            Hashtable<Integer, Waypoint> hashtable2 = new Hashtable<>();
            wps = hashtable2;
            hashtable.put(modName, hashtable2);
        }
        return wps;
    }

    @Deprecated
    public boolean isMultiplayer(String containerId) {
        return MinimapWorldContainerUtil.isMultiplayer(this.pathReader.read(containerId));
    }

    @Deprecated
    private boolean hasServerLevelId(WaypointWorldRootContainer rootContainer) {
        return getWorldStateUpdater().hasServerLevelId(rootContainer);
    }

    @Deprecated
    private Object getAutoIdBase(WaypointWorldRootContainer rootContainer) {
        return getWorldStateUpdater().getAutoWorldNodeBase(rootContainer);
    }

    @Deprecated
    public void onServerLevelId(int id) {
        getWorldStateUpdater().onServerLevelId(id);
    }
}
